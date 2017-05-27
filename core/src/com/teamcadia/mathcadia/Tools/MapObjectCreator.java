package com.teamcadia.mathcadia.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Screens.MapMovementScreen;

import java.util.ArrayList;

/**
 * Created by Richardo on 5/22/2017.
 */
public class MapObjectCreator {

    private final String TAG = "MATHTAG";


    public MapObjectCreator(MapMovementScreen screen){

        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);
        int tileSize = (int) mapLayer.getTileWidth();

        int mapWidth = mapLayer.getWidth() * tileSize;
        int mapHeight = mapLayer.getHeight() * tileSize;

        ArrayList<Rectangle> doors = new ArrayList<Rectangle>();



        //create fixtures for world
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create wall bodies/fixtures
        for(MapObject object: map.getLayers().get("Collisions").getObjects().getByType(RectangleMapObject.class)){

                Rectangle rect = ((RectangleMapObject) object).getRectangle();


                //create body objects for player to collide with
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX()+ rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
                fdef.shape = shape;
                fdef.filter.categoryBits = Mathcadia.WALL_BIT;
                body.createFixture(fdef);

        }

        //now add the doors to our world
        int doorNumber = 1;
        for(MapObject object: map.getLayers().get("Doors").getObjects().getByType(RectangleMapObject.class)){


            Rectangle rect = ((RectangleMapObject) object).getRectangle();



            //create body objects for player to collide with
            bdef.type = BodyDef.BodyType.KinematicBody;
            bdef.position.set((rect.getX()+ rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));

            // change the rectangle's position to match the map
            rect.setPosition(bdef.position.x, bdef.position.y);

            //add the door to our list so we can move between them later
            doors.add(rect);

            Gdx.app.log(TAG, "bdef X: " + bdef.position.x);
            Gdx.app.log(TAG, "bdef " + doorNumber + " Y: " + bdef.position.y);
            Gdx.app.log(TAG, "door X: " + rect.getX());
            Gdx.app.log(TAG, "door " + doorNumber + " Y: " + rect.getY());

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            fdef.filter.categoryBits = Mathcadia.DOOR_BIT;
            fdef.filter.maskBits = Mathcadia.PLAYER_BIT;

            //here we label the current door with a number for future reference
            body.createFixture(fdef).setUserData(doorNumber);
            doorNumber++;

        }

        //update our current doors list
        screen.setDoors(doors);

        //now add Room rectangles
        ArrayList<Rectangle> rooms = new ArrayList<Rectangle>();
        int roomNum = 1;
        for(MapObject object: map.getLayers().get("Rooms").getObjects().getByType(RectangleMapObject.class)){


            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //create body objects for player to collide with
            bdef.type = BodyDef.BodyType.KinematicBody;
            bdef.position.set((rect.getX()+ rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));

            // change the rectangle's position to match the map
            rect.setPosition(bdef.position.x, bdef.position.y);

            //add the door to our list so we can move between them later
            rooms.add(rect);

            Gdx.app.log(TAG, "bdef X: " + bdef.position.x);
            Gdx.app.log(TAG, "bdef " + roomNum + " Y: " + bdef.position.y);
            Gdx.app.log(TAG, "room X: " + rect.getX());
            Gdx.app.log(TAG, "room " + roomNum + " Y: " + rect.getY());

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            fdef.filter.maskBits = 0;


            //here we label the current door with a number for future reference
            body.createFixture(fdef).setUserData(roomNum);

            roomNum++;

        }
        Mathcadia.getMaps().setRooms(rooms);

    }
}
