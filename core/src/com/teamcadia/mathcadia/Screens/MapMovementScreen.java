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
 * Created by Richardo on 5/22/2017.
 */
public class MapMovementScreen implements Screen {

    private Mathcadia game;
    public final String TAG = "mathTag";

    //Texture atlas for accessing textures
    private TextureAtlas atlas;

    private OrthographicCamera camera;
    private StretchViewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;
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

    @Override
    public void show() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();


        //load the map
        map = MapHandler.loadMap(1, false);
        mapRenderer = new OrthogonalTiledMapRenderer(map);


        //create the objects in tbe map
        worldCreator = new MapObjectCreator(this);
        world.setContactListener(new WorldContactListener());

        mapWidth = Mathcadia.getMaps().getCurrentRoom().getWidth();
        mapHeight = Mathcadia.getMaps().getCurrentRoom().getHeight();
        Gdx.app.log(TAG, "Map width: " + mapWidth);
        Gdx.app.log(TAG, "Map height: " + mapHeight);


        camera = new OrthographicCamera();
        camera.position.set(MapHandler.getCameraPosition());


        camera.zoom = MapHandler.getCameraZoom(mapWidth, mapHeight);
        Gdx.app.log(TAG, "Original Zoom " + camera.zoom);
        camera.update();


        player = new MapCharacter(this);

        //initialize the variables for our MapHandler
        MapHandler.setVariables();

        gamePort = new StretchViewport(1000,510,camera);
        gamePort.apply();

    }

    public void update(float dt){
        handelInput(dt);

        world.step(1/60f, 6, 2);


        if(Mathcadia.getMaps().getDoorTransition() > 0){
            int doorNum = Mathcadia.getMaps().getDoorTransition();
            // if the door is equal to the last in the array, load next map
            if(doorNum == MapHandler.getDoors().size() || doorNum == 0) {
                constructMap();
            }
            else {
                changeRooms(doorNum);
            }
        }
        player.update(dt);


    }

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
        else {
            camera.setToOrtho(false, 1007, 526);
            camera.zoom = (float) 1.007;
        }

        Gdx.app.log(TAG,"Loading map");

        //add player to new map
        player.defineCharacter();

        if(previousMap){
            changeRooms(Mathcadia.getMaps().getDoorTransition());
        }
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
