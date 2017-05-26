package Test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Screens.MainMenuScreen;
import com.teamcadia.mathcadia.Screens.MapMovementScreen;

/**
 * Created by Richardo on 5/23/2017.
 */
public class MapTest extends Game {

    public final String TAG = "mathTag";
    public SpriteBatch batch;
    public Mathcadia game;

    @Override
    public void create () {
        batch = new SpriteBatch();
        this.setScreen(new MapMovementScreen(game));
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
