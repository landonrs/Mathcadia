package com.teamcadia.mathcadia.Model;

import com.badlogic.gdx.maps.tiled.TiledMap;

import java.util.ArrayList;

/**
 * Created by Richardo on 5/22/2017.
 */
public class WorldMap {

    private ArrayList<String> tiledBackgrounds;
    private TiledMap currentMap;

    public WorldMap(){
        tiledBackgrounds = new ArrayList<String>();


        //adding our test maps for now, the map names will be read from a file later
        tiledBackgrounds.add("background/MathcadiaTest_Outside.tmx");
        tiledBackgrounds.add("background/MathcadiaTest.tmx");
    }

    public ArrayList<String> getTiledBackgrounds() {
        return tiledBackgrounds;
    }


    public TiledMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(TiledMap currentMap) {
        this.currentMap = currentMap;
    }
}
