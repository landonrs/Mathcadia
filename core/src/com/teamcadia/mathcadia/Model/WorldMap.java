package com.teamcadia.mathcadia.Model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Richardo on 5/22/2017.
 */
public class WorldMap {

    //these keep track of every individual map file
    private ArrayList<String> tiledBackgrounds;
    private TiledMap currentMap;
    //these keep track of rooms within map files
    private ArrayList<Rectangle> rooms;
    private Rectangle currentRoom;

    //this tells our screen if the player is moving through a door
    private int doorTransition;

    public WorldMap(){
        tiledBackgrounds = new ArrayList<String>();
        rooms = new ArrayList<Rectangle>();
        //start at 0 because we are not changing rooms
        doorTransition = 0;


        //adding our test maps for now, the map names will be read from a file later
        tiledBackgrounds.add("background/MathcadiaTest_Outside.tmx");
        tiledBackgrounds.add("background/MathcadiaTest.tmx");


    }


    //getters and setters
    public ArrayList<String> getTiledBackgrounds() {
        return tiledBackgrounds;
    }


    public TiledMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(TiledMap currentMap) {
        this.currentMap = currentMap;
    }


    public ArrayList<Rectangle> getRooms() { return rooms; }

    public void setRooms(ArrayList<Rectangle> rooms) { this.rooms = rooms; }

    public Rectangle getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Rectangle currentRoom) {
        this.currentRoom = currentRoom;
    }

    public int getDoorTransition() {
        return doorTransition;
    }

    public void setDoorTransition(int doorTransition) {
        this.doorTransition = doorTransition;
    }
}
