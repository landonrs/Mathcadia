package com.teamcadia.mathcadia.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.teamcadia.mathcadia.Screens.MapMovementScreen;

/**
 * Created by Richardo on 5/22/2017.
 */
public class MapObjectCreator {

    private final String TAG = "MATHTAG";


    public MapObjectCreator(MapMovementScreen screen){

        World world = screen.getWorld();
        TiledMap map = screen.getMap();


        //create fixtures for world
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create wall bodies/fixtures
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){

                Rectangle rect = ((RectangleMapObject) object).getRectangle();

           /* Gdx.app.log(TAG, "original width: " + rect.getWidth());
            Gdx.app.log(TAG, "original height: " + rect.getHeight());
                rect.setSize(rect.getWidth() / 32, rect.getHeight() / 32);
            Gdx.app.log(TAG, "original X: " + rect.getX());
            Gdx.app.log(TAG, "original Y: " + rect.getY());
                rect.setPosition(rect.getX() / 16f, rect.getY() / 16f);*/


                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX()+ rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
                //Gdx.app.log(TAG, "COLLISION OBJECT width: " + rect.getWidth());
                //Gdx.app.log(TAG, "COLLISION OBJECT height: " + rect.getHeight());
                //Gdx.app.log(TAG, "COLLISION OBJECT X: " + bdef.position.x);
                //Gdx.app.log(TAG, "COLLISION OBJECT Y: " + bdef.position.y);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
                fdef.shape = shape;
                body.createFixture(fdef);

        }
    }
}
