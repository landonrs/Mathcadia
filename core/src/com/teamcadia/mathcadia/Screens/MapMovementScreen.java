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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Model.MapCharacter;
import com.teamcadia.mathcadia.Presenter.MapHandler;
import com.teamcadia.mathcadia.Tools.WorldContactListener;
import com.teamcadia.mathcadia.Tools.Hud;
import com.teamcadia.mathcadia.Tools.MapObjectCreator;

import java.util.ArrayList;

/**
 * Created by Richardo on 5/22/2017.
 */
public class MapMovementScreen implements Screen {

    private Mathcadia game;
    public final String TAG = "mathTag";

    //Texture atlas for accessing textures
    private TextureAtlas atlas;

    private OrthographicCamera camera;
    private Viewport gamePort;
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

    private int mapWidth;
    private int mapHeight;


    public MapMovementScreen(Mathcadia game){

        this.game = game;

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();


    }

    @Override
    public void show() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();


        map = MapHandler.loadMap(1);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);
        int tileSize = (int) mapLayer.getTileWidth();

        mapWidth = mapLayer.getWidth() * tileSize;
        mapHeight = mapLayer.getHeight() * tileSize;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth,mapHeight);
        float x = mapWidth / w;
        camera.zoom = x;
        camera.update();



        worldCreator = new MapObjectCreator(this);
        world.setContactListener(new WorldContactListener());

        player = new MapCharacter(this);

    }

    public void update(float dt){
        handelInput(dt);

        world.step(1/60f, 6, 2);

        camera.update();

        if(Mathcadia.getMaps().getDoorTransition() > 0){
            movePlayer(Mathcadia.getMaps().getDoorTransition());
        }
        player.update(dt);


    }

    private void movePlayer(int doorTransition) {
        Vector2 playerPosition = MapHandler.movePlayerToNextRoom(doorTransition, doors);
        if(playerPosition != null)
        player.b2Body.setTransform(playerPosition,0);
        //reset the door until we walk through another
        Mathcadia.getMaps().setDoorTransition(0);
    }

    private void handelInput(float dt){

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


        b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        //draw the hud with the play screen
        //game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {

        float x;
        int previousWidth = (int) camera.viewportWidth;

        camera.viewportWidth = width;
        camera.viewportHeight = height;
        x = mapWidth / camera.viewportWidth;
        camera.zoom = x;
        camera.update();
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
