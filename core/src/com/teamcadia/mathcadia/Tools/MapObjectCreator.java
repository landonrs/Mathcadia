package com.teamcadia.mathcadia.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.teamcadia.mathcadia.Screens.MapMovementScreen;

/**
 * Created by Richardo on 5/22/2017.
 */
public class MapObjectCreator {

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


                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
                fdef.shape = shape;
                body.createFixture(fdef);

        }
    }
}
