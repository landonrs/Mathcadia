package com.teamcadia.mathcadia.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Screens.MapMovementScreen;

/**
 * Created by Richardo on 5/22/2017.
 */
public class MapCharacter extends Sprite {

    public World world;
    public Body b2Body;

    private float stateTimer;


    private TextureRegion stand;

    public MapCharacter(MapMovementScreen screen){

        world = screen.getWorld();

        Texture playerImage = new Texture(Gdx.files.internal("map_characters/chrom.png"));
        stand = new TextureRegion(playerImage, 0, 0, 32, 32);

        defineCharacter();

        setBounds(0,0, 1, 1);
        setRegion(stand);

    }

    public void update(float dt){
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2) ;

    }

    public void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(63/2f, 33/2f);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(.5f);
        fdef.filter.categoryBits = Mathcadia.PLAYER_BIT;
        fdef.filter.maskBits = Mathcadia.WALL_BIT;

        fdef.shape = shape;
        b2Body.createFixture(fdef);

    }
}
