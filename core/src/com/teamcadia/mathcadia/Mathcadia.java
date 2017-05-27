package com.teamcadia.mathcadia;

import com.teamcadia.mathcadia.Model.WorldMap;
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
	public static final String TAG = "mathTag";
	public SpriteBatch batch;
	//for holding our tiled map info
	private static WorldMap maps;

	public static final short PLAYER_BIT = 1;
	public static final short DOOR_BIT = 2;
	public static final short WALL_BIT = 4;
	public static final short SIGN_BIT = 8;
	public static final short NPC_BIT = 16;


	@Override
	public void create () {
		batch = new SpriteBatch();
		maps = new WorldMap();

		this.setScreen(new MapMovementScreen(this));
		//Gdx.app.log(TAG, "Creating game");

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

	public static WorldMap getMaps() { return maps; }
}
