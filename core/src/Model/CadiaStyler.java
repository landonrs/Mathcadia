package Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Contains all aspects of the game that pertain to the styling of the UI. Singleton Implementation.
 *
 * @author Landon Shumway
 * @since May 5 2017
 */
public class CadiaStyler {

    private Skin skin;
    private Dialog dialogBox;
    private static CadiaStyler cadiaStyler = null;


    private CadiaStyler(){
        skin = new Skin(Gdx.files.internal("UI Style/uiskin.json"));
        dialogBox = new Dialog("",skin,"default");
    }

    public static CadiaStyler getInstance(){
        if (cadiaStyler == null){
            cadiaStyler = new CadiaStyler();
        }
        return cadiaStyler;
    }

    public Skin getSkin() {
        return skin;
    }

    public Dialog getDialogBox() {
        return dialogBox;
    }


}
