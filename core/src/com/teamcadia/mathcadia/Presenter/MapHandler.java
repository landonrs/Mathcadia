package com.teamcadia.mathcadia.Presenter;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Model.WorldMap;

import java.util.ArrayList;

import static com.badlogic.gdx.utils.JsonValue.ValueType.object;

/**
 * Created by Richardo on 5/22/2017.
 */
public class MapHandler {

    private static WorldMap maps;

    public static TiledMap loadMap(int mapIndex){
        maps = Mathcadia.getMaps();

        String mapName = maps.getTiledBackgrounds().get(mapIndex);

        TiledMap LoadedMap = new TmxMapLoader().load(mapName);

        //now update the current map on our world map for saving purposes
        maps.setCurrentMap(LoadedMap);

        return LoadedMap;
    }

    /** Checks the current map for the player object layer and returns his position
     *
     * @since May 26 2017
     * @return Vector2 for player position
     */
    public static Vector2 getPlayerPosition(){
        //get the current map from our WorldMap class
        TiledMap map = Mathcadia.getMaps().getCurrentMap();

        //find the player rectangle object and return its position
        MapObject playerLocation = map.getLayers().get("Player").getObjects().getByType(RectangleMapObject.class).first();
        Rectangle playerRect = ((RectangleMapObject) playerLocation).getRectangle();


        return new Vector2(playerRect.getX(), playerRect.getY());

    }

    public static Vector2 movePlayerToNextRoom(int doorNum, ArrayList<Rectangle> doors){
        Vector2 playerPosition;
        //keep track of the proper array index with this variable
        int doorIndex = doorNum - 1;
        //if even number door, move to the next occurring door in the array
        if(doorNum % 2 != 0){
            //if the door's width is 16, we know the next door is facing East. Move the player to the right of the door
            if(doors.get(doorIndex).getWidth() == 16)
            playerPosition = new Vector2(doors.get(doorIndex + 1).getX() + 32, doors.get(doorIndex + 1).getY() ) ;
            //else the next door is facing down, move the player below the door
            else
                playerPosition = new Vector2((doors.get(doorIndex + 1).getX()), doors.get(doorIndex + 1).getY() - 32);
        }
        //if the door is odd numbered, move to previous door in our array
        else
            //if the door's width is 16, we know the previous door is facing West. Move the player to the left of the door
            if(doors.get(doorIndex).getWidth() == 16)
                playerPosition = new Vector2(doors.get(doorIndex - 1).getX() - 32, doors.get(doorIndex - 1).getY()) ;
                //else the door is facing up, move the player above the door
            else
                playerPosition = new Vector2((doors.get(doorIndex - 1).getX()), doors.get(doorIndex - 1).getY() + 32);

        return playerPosition;
    }

    /** Finds the current room rectangle position and returns either the points for the camera's center or zoom factor
     *
     * @return Vector2 for cameraPosition
     */
    public static Vector2 getCameraPosition(int roomIndex){
        //get the current room from current map
        TiledMap map = Mathcadia.getMaps().getCurrentMap();


        //find the player rectangle object and return its position
        MapObject roomLocation = map.getLayers().get("Rooms").getObjects().getByType(RectangleMapObject.class).get(roomIndex);
        Rectangle roomRect = ((RectangleMapObject) roomLocation).getRectangle();

        //return the center of the rectangle
        return new Vector2(roomRect.getX() + roomRect.getWidth() / 2, roomRect.getY() + roomRect.getHeight() / 2);
    }


    public static void switchRooms(Object userData) {

        //get the integer out of the data
        int currentDoor = (Integer) userData;

        //set the door so we can change our U.I.
        Mathcadia.getMaps().setDoorTransition(currentDoor);


    }
}
