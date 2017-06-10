package com.teamcadia.mathcadia.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Model.MapCharacter;
import com.teamcadia.mathcadia.Presenter.MapHandler;
import com.teamcadia.mathcadia.Tools.WorldContactListener;
import com.teamcadia.mathcadia.Tools.Hud;
import com.teamcadia.mathcadia.Tools.MapObjectCreator;
import sun.security.jgss.GSSCaller;

import java.util.ArrayList;
import java.util.Map;

/**
 * contains the logic for all map visualization and movement
 */
public class MapMovementScreen implements Screen {


    private Mathcadia game;
    public final String TAG = "mathTag";

    //Texture atlas for accessing textures
    private TextureAtlas atlas;

    //the camera determines what is displayed on screen
    //the viewport determines how the window is formatted to fit on the screen
    private OrthographicCamera camera;
    private StretchViewport gamePort;
    private Hud hud;

    //the map and renderer are used to display our tiled map on the screen
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    //create our character
    private MapCharacter player;

    //box2d variables
    private World world;
    //this gives a visual representation of the collision boxes
    private Box2DDebugRenderer b2dr;
    private MapObjectCreator worldCreator;

    //these rectangles keep track of door locations so we can change rooms
    private ArrayList<Rectangle> doors;

    private float mapWidth;
    private float mapHeight;

    private float w;
    private float h;


    public MapMovementScreen(Mathcadia game){

        this.game = game;

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();


    }

    /** this method is called whenever we set the MapMovementScreen as the main screen of the game
     * @since June 9 2017
     */
    @Override
    public void show() {

        //load the map
        map = MapHandler.loadMap(1, false);
        mapRenderer = new OrthogonalTiledMapRenderer(map);


        //create the objects in tbe map
        worldCreator = new MapObjectCreator(this);
        //set the contact listener for collisions
        world.setContactListener(new WorldContactListener());

        //here we find the size of the current room rectangle and use the height and width to format our camera
        mapWidth = Mathcadia.getMaps().getCurrentRoom().getWidth();
        mapHeight = Mathcadia.getMaps().getCurrentRoom().getHeight();
        Gdx.app.log(TAG, "Map width: " + mapWidth);
        Gdx.app.log(TAG, "Map height: " + mapHeight);


        camera = new OrthographicCamera();
        camera.position.set(MapHandler.getCameraPosition());


        camera.zoom = MapHandler.getCameraZoom(mapWidth, mapHeight);
        Gdx.app.log(TAG, "Original Zoom " + camera.zoom);
        camera.update();


        //add player to the map
        player = new MapCharacter(this);

        //initialize the variables for our MapHandler
        MapHandler.setVariables();


        //our viewport will be a stretchviewport and be applied to this screen
        gamePort = new StretchViewport(1000,510,camera);
        gamePort.apply();

    }


    /** called every cycle to update the player and our camera position
     * @since v1.0
     * @param dt the current game time
     */
    public void update(float dt){
        //check for player input
        handelInput(dt);

        //runs the movement simulation
        world.step(1/60f, 6, 2);


        //if we touch a door it will run this block
        if(Mathcadia.getMaps().getDoorTransition() > 0){
            int doorNum = Mathcadia.getMaps().getDoorTransition();
            // if the door is equal to the last or first in the array, load next or previous map
            if(doorNum == MapHandler.getDoors().size() || doorNum == 0) {
                constructMap();
                mapWidth = Mathcadia.getMaps().getCurrentRoom().getWidth();
                mapHeight = Mathcadia.getMaps().getCurrentRoom().getHeight();
                camera.position.set(MapHandler.getCameraPosition());
                //camera.zoom = MapHandler.getCameraZoom(mapWidth, mapHeight);
                camera.update();
            }
            //else just change the camera to the next room
            else {
                changeRooms(doorNum);
            }
        }
        player.update(dt);


    }

    /**changes the camera position and zoom to match the size and location of the next room
     *
     * @since v1.0
     * @param doorNum
     */
    private void changeRooms(int doorNum){
        movePlayer(doorNum);
        //move the camera and update its position
        camera.position.set(MapHandler.moveCamera(doorNum));
        mapWidth = Mathcadia.getMaps().getCurrentRoom().getWidth();
        mapHeight = Mathcadia.getMaps().getCurrentRoom().getHeight();
        //resize the camera for the new room size
        camera.zoom = MapHandler.getCameraZoom(mapWidth, mapHeight);
        camera.update();
    }

    /** loads the next map and creates all the related objects
     *
     * @since v1.0
     */
    private void constructMap(){
        boolean previousMap = false;

        if(Mathcadia.getMaps().getDoorTransition() == 1){
        //load the inside map
            previousMap = true;
            map = MapHandler.loadMap(1, previousMap);
            mapRenderer = new OrthogonalTiledMapRenderer(map);
            Mathcadia.getMaps().setDoorTransition(7);

        }
        else {
            //store the previous room index if we move back into the previous map
            MapHandler.previousRoomIndex = Mathcadia.getMaps().getRooms().indexOf(Mathcadia.getMaps().getCurrentRoom());
            //load the outside map
            map = MapHandler.loadMap(0, previousMap);
            mapRenderer = new OrthogonalTiledMapRenderer(map);
            Mathcadia.getMaps().setDoorTransition(0);
        }

        //erase the objects from the previous map, set up our new objects
        world.dispose();
        world = new World(new Vector2(0, 0), true);

        worldCreator = new MapObjectCreator(this);
        world.setContactListener(new WorldContactListener());
        MapHandler.setVariables();

        if(previousMap){
            //if moving into the previous map, get the index of the last room we were in and set it as the current room
            Mathcadia.getMaps().setCurrentRoom(Mathcadia.getMaps().getRooms().get(MapHandler.previousRoomIndex));
        }


        Gdx.app.log(TAG,"Loading map");

        //add player to new map
        player.defineCharacter();

        if(previousMap){
            changeRooms(Mathcadia.getMaps().getDoorTransition());
        }
        //reset our door transition since we are no longer switching rooms
        Mathcadia.getMaps().setDoorTransition(0);
    }

    private void movePlayer(int doorTransition) {
        Vector2 playerPosition = MapHandler.movePlayerToNextRoom(doorTransition);
        if(playerPosition != null)
        player.b2Body.setTransform(playerPosition,0);
        //reset the door until we walk through another
        Mathcadia.getMaps().setDoorTransition(0);
    }

    private void handelInput(float dt){

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Gdx.app.log(TAG,"Zoom before space: " + camera.zoom);
            camera.zoom += 0.02;
            camera.update();
            Gdx.app.log(TAG,"New Zoom: " + camera.zoom);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.b2Body.applyLinearImpulse(new Vector2(0, 10f), player.b2Body.getWorldCenter(), true);

           //Gdx.app.log(TAG, "moving up" );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.b2Body.applyLinearImpulse(new Vector2(10f, 0), player.b2Body.getWorldCenter(), true);

        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.b2Body.applyLinearImpulse(new Vector2(-10f, 0), player.b2Body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.b2Body.applyLinearImpulse(new Vector2(0, -10f), player.b2Body.getWorldCenter(), true);
        }
        //stop the player if the keys are not being pressed
        else if(!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            player.b2Body.setLinearVelocity(0, 0);
        }



    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0,0, 0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        mapRenderer.setView(camera);
        mapRenderer.render();


        //this draws the lines to show the areas of collisions for debugging
        //b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        //draw the hud with the play screen
        //game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {

        //when we resize we simply stretch the image using the viewport
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public World getWorld() { return world; }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap(){ return map; }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public ArrayList<Rectangle> getDoors() { return doors; }
    public void setDoors(ArrayList<Rectangle> doors) { this.doors = doors; }
}
