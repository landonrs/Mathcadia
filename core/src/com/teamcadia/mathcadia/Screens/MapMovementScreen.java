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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Model.MapCharacter;
import com.teamcadia.mathcadia.Tools.CollisionChecker;
import com.teamcadia.mathcadia.Tools.Hud;
import com.teamcadia.mathcadia.Tools.MapObjectCreator;

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

    private int mapWidth;
    private int mapHeight;

    public MapMovementScreen(Mathcadia game){

        this.game = game;

        //set up camera and viewport
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();




    }

    @Override
    public void show() {

        //set up hud to display score and time
        //hud = new Hud(game.batch);
        //load map file for display
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("background/MathcadiaTest_Outside.tmx");
        TiledMapTileLayer mainLayer = (TiledMapTileLayer) map.getLayers().get(0);

        int tileSize = (int) mainLayer.getTileWidth();

        mapWidth = mainLayer.getWidth(); //* tileSize;
        mapHeight = mainLayer.getHeight(); //* tileSize;
        Gdx.app.log(TAG, "map width: " + mapWidth );
        Gdx.app.log(TAG, "map height: " + mapHeight );

        //one tile in our map is 16x16, so we set the unit scale to 1/16 to make the tiles 1x1
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 16f);

        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();

        worldCreator = new MapObjectCreator(this);

        //create character for mario
        player = new MapCharacter(this);

        world.setContactListener(new CollisionChecker());


        camera = new OrthographicCamera();
        camera.setToOrtho(false ,mapWidth,mapHeight);
        mapRenderer.setView(camera);

    }

    public void update(float dt){
        handelInput(dt);

        world.step(1/60f, 6, 2);
        player.update(dt);



        //Gdx.app.log(TAG, "map view X: " + mapRenderer.getViewBounds().getX());
        //Gdx.app.log(TAG, "Player X: " + player.getX());
        camera.update();


    }

    private void handelInput(float dt){

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.b2Body.applyLinearImpulse(new Vector2(0, 1f), player.b2Body.getWorldCenter(), true);

           //Gdx.app.log(TAG, "moving up" );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.b2Body.applyLinearImpulse(new Vector2(1f, 0), player.b2Body.getWorldCenter(), true);

        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.b2Body.applyLinearImpulse(new Vector2(-1f, 0), player.b2Body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.b2Body.applyLinearImpulse(new Vector2(0, -1f), player.b2Body.getWorldCenter(), true);
        }
        //stop the player if the keys are not being pressed
        else if(!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            player.b2Body.setLinearVelocity(0, 0);
        }



    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0,0, 1,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //mapRenderer.setView(camera);
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

        camera.viewportWidth = 30f;                 // Viewport of 30 units!
        camera.viewportHeight = 30f * height/width; // Lets keep things in proportion.
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
}
