package com.teamcadia.mathcadia;

import com.teamcadia.mathcadia.Screens.MainMenuScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teamcadia.mathcadia.Screens.MapMovementScreen;

public class Mathcadia extends Game {
	public final String TAG = "mathTag";
	public SpriteBatch batch;

    public static final int V_WIDTH = 640;
    public static final int V_HEIGHT = 320;
    public static final float PPM = 100;

    //bits for collision detection
	public static final short WALL_BIT = 1;
	public static final short PLAYER_BIT = 2;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MapMovementScreen(this));
		Gdx.app.log(TAG, "Creating game");
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		Gdx.app.log(TAG,"Disposing game");
	}
}
