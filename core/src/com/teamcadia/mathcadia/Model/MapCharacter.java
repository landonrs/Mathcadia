package com.teamcadia.mathcadia.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Presenter.MapHandler;
import com.teamcadia.mathcadia.Screens.MapMovementScreen;

/**
 * Created by Richardo on 5/22/2017.
 */
public class MapCharacter extends Sprite {

    public World world;
    public Body b2Body;
    public MapMovementScreen screen;
    public BodyDef bdef;

    private float stateTimer;

    //this is used when we first start the game to place the character in the correct position
    private boolean startingPosition;


    private TextureRegion stand;

    public MapCharacter(MapMovementScreen screen){

        this.screen = screen;

        startingPosition = true;

        Texture playerImage = new Texture(Gdx.files.internal("map_characters/chrom.png"));
        stand = new TextureRegion(playerImage, 0, 0, 32, 32);

        defineCharacter();

        setBounds(0,0, 32, 32);
        setRegion(stand);


    }

    public void update(float dt){
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2) ;

    }

    public void defineCharacter() {
        bdef = new BodyDef();
        world = screen.getWorld();

        if(startingPosition) {
            bdef.position.set(MapHandler.getPlayerPosition().x, MapHandler.getPlayerPosition().y);
            startingPosition = false;
        }
        else
            bdef.position.set(MapHandler.movePlayerToNextRoom(Mathcadia.getMaps().getDoorTransition()));
        /*Gdx.app.log(Mathcadia.TAG, "Player x: "  + bdef.position.x);
        Gdx.app.log(Mathcadia.TAG, "Player y: "   + bdef.position.y);*/
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3f);
        fdef.filter.categoryBits = Mathcadia.PLAYER_BIT;
        fdef.filter.maskBits = Mathcadia.DOOR_BIT | Mathcadia.WALL_BIT;

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData("player");

    }
}
