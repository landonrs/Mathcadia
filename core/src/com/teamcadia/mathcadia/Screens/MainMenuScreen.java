package com.teamcadia.mathcadia.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.teamcadia.mathcadia.Mathcadia;

/**
 * displays the main menu of the game
 *
 * @author Landon Shumway
 * @since May 5 2017
 */
public class MainMenuScreen implements Screen {

    private Mathcadia game;
    private StretchViewport viewport;
    private OrthographicCamera camera;
    private Stage stage;
    private Texture img;

    public MainMenuScreen(Mathcadia mathcadia){
        this.game = mathcadia;
        camera = new OrthographicCamera();
        // viewport = new StretchViewport(Mathcadia.V_WIDTH, Mathcadia.V_HEIGHT,camera);
        // viewport.apply();
        img = new Texture(Gdx.files.internal("background/title.jpg"));


        camera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);
        camera.update();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(img, 0, 0);
        game.batch.end();



    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width,height);
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
        img.dispose();
        stage.dispose();
    }
}
