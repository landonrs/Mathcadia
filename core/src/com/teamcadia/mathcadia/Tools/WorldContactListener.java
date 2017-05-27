package com.teamcadia.mathcadia.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Presenter.MapHandler;

/**
 * Created by Richardo on 5/22/2017.
 */
public class WorldContactListener implements ContactListener {


    public WorldContactListener(){

    }

    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData() == "player" || fixB.getUserData() == "player"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture door = head == fixA ? fixB : fixA;

        }

        //right now we simply check if the player collides with a door body and call the switch room method
        //we will add more collision options later
        switch (cDef){
            case Mathcadia.DOOR_BIT | Mathcadia.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == Mathcadia.DOOR_BIT) {
                    MapHandler.switchRooms(fixA.getUserData());
                    Gdx.app.log(Mathcadia.TAG, "PLAYER TOUCHED DOOR " + fixA.getUserData());
                }

                else {
                    MapHandler.switchRooms(fixB.getUserData());
                    Gdx.app.log(Mathcadia.TAG, "PLAYER TOUCHED DOOR " + fixB.getUserData());
                }


                break;


        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
