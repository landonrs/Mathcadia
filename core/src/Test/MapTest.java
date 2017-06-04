package Test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Model.WorldMap;
import com.teamcadia.mathcadia.Screens.MainMenuScreen;
import com.teamcadia.mathcadia.Screens.MapMovementScreen;

/**
 * Created by Richardo on 5/23/2017.
 */
public class MapTest extends Game {

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

        //this.setScreen(new MapMovementScreen(this));
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