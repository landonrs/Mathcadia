package com.teamcadia.mathcadia.Presenter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.teamcadia.mathcadia.Mathcadia;
import com.teamcadia.mathcadia.Model.WorldMap;

import java.util.ArrayList;

import static com.badlogic.gdx.utils.JsonValue.ValueType.object;

/**
 * handles the logic for transitions between rooms and maps
 *
 */
public class MapHandler {

    private static WorldMap maps;
    public static int currentRoomIndex;
    //these rectangles keep track of door locations so we can change rooms
    private static ArrayList<Rectangle> doors;
    private static ArrayList<Rectangle> rooms;

    public static int previousRoomIndex;

    private static boolean switchingMaps;


    /** uses the map loader to create the map object and returns it to the {@link com.teamcadia.mathcadia.Screens.MapMovementScreen}
     *
     * @param mapIndex tells us which map to load
     * @param previousMap determines which door we are entering
     * @return
     */
    public static TiledMap loadMap(int mapIndex, boolean previousMap){

        maps = Mathcadia.getMaps();

        String mapName = maps.getTiledBackgrounds().get(mapIndex);

        TiledMap LoadedMap = new TmxMapLoader().load(mapName);

        //now update the current map on our world map for saving purposes
        maps.setCurrentMap(LoadedMap);

        //if we walk into previous map, our current room will be the last room we were in on the previous map
        if(previousMap) {
            currentRoomIndex = previousRoomIndex;
            switchingMaps = true;
        }
        else
            currentRoomIndex = 0;

        return LoadedMap;
    }

    /** for now it sets our list of rooms, later it will initialize any more variables we need
     *
     */
    public static void setVariables(){
        rooms = maps.getRooms();
        Mathcadia.getMaps().setCurrentRoom(rooms.get(currentRoomIndex));
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

    /** Checks our tiled map door objects and determines where to move our player object
     * @param doorNum
     * @return
     */
    public static Vector2 movePlayerToNextRoom(int doorNum){

        Vector2 playerPosition;
        //keep track of the proper array index with this variable
        int doorIndex = doorNum - 1;

        //when we load the next map in our list we pass in the number 0, place us next to first door in array
        if(doorNum == 0) {
            playerPosition = new Vector2(doors.get(doorNum).getX() , doors.get(doorNum).getY() - 32);
        }

        //when we load the previous map we pass the size of the door array
        else if(doorNum == doors.size()){
            playerPosition = new Vector2((doors.get(doorIndex).getX()), doors.get(doorIndex).getY() + 32);
        }

        //if odd number door, move to the next occurring door in the array
        else if(doorNum % 2 != 0){
            //if the door's width is 16, we know the next door is facing East. Move the player to the right of the door
            if(doors.get(doorIndex).getWidth() == 16)
            playerPosition = new Vector2(doors.get(doorIndex + 1).getX() + 32, doors.get(doorIndex + 1).getY() ) ;
            //else the next door is facing down, move the player below the door
            else
                playerPosition = new Vector2((doors.get(doorIndex + 1).getX()), doors.get(doorIndex + 1).getY() - 32);
        }
        //if the door is even numbered, move to previous door in our array
        else
            //if the door's width is 16, we know the previous door is facing West. Move the player to the left of the door
            if(doors.get(doorIndex).getWidth() == 16)
                playerPosition = new Vector2(doors.get(doorIndex - 1).getX() - 32, doors.get(doorIndex - 1).getY()) ;
                //else the door is facing up, move the player above the door
            else
                playerPosition = new Vector2((doors.get(doorIndex - 1).getX()), doors.get(doorIndex - 1).getY() + 32);

        return playerPosition;
    }

    /** Finds the current room rectangle position and returns the points for the camera's center
     *
     * @return Vector2 for cameraPosition
     */
    public static Vector3 getCameraPosition(){
        //get the current room from current map
        Rectangle roomRect = Mathcadia.getMaps().getCurrentRoom();

        //return the center of the rectangle
        return new Vector3(roomRect.getX() , roomRect.getY(), 0);
    }

    /** determines how far away to set the camera's zoom so the entire room will fit on screen
     *
     * @param mapWidth
     * @param mapHeight
     * @return the zoom factor
     */
    public static float getCameraZoom(float mapWidth, float mapHeight){
        //get the current width and height of screen
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        //this will hold our zoom amount
        float zoom;

        //if the width is smaller than the height we will divide by the width
        if(w < h) {

            if (mapWidth < mapHeight)
                zoom = mapHeight / w;
            else
                zoom = mapWidth / w;
        }
        else {

            if (mapWidth < mapHeight)
                zoom = mapHeight / h;
            else
                zoom = mapWidth / h;
        }

        return zoom;

    }


    /** converts our object data into a number that we can use to determine what room we are in
     *
     * @param userData the label we place on each door to give it a unique ID
     */
    public static void switchRooms(Object userData) {

        //get the integer out of the data
        int currentDoor = (Integer) userData;

        //set the door so we can change our U.I.
        Mathcadia.getMaps().setDoorTransition(currentDoor);


    }

    /** moves through our array of rooms and finds the current one the player is located in
     *
     * @param doorNum
     * @return
     */
    public static Vector3 moveCamera(int doorNum){
        //if switching maps do not change the current room number, just move the camera
       if(switchingMaps){
           switchingMaps = false;
           return getCameraPosition();
       }

        int doorIndex = doorNum - 1;
        //if the door object has a height of 25, change the room index and move the camera to the next room
        if(doors.get(doorIndex).getHeight() == 25) {
            currentRoomIndex++;
        }
        //else we move back in the array
        else {
            currentRoomIndex--;
        }

        //set the current room in mathcadia
        Mathcadia.getMaps().setCurrentRoom(rooms.get(currentRoomIndex));

        //get the camera position based on the new room position
        return getCameraPosition();


    }



    public static ArrayList<Rectangle> getDoors() { return doors; }
    public static void setDoors(ArrayList<Rectangle> doors) {
        MapHandler.doors = doors;
    }

    public static void setRooms(ArrayList<Rectangle> rooms) {
        MapHandler.rooms = rooms;
    }

}
