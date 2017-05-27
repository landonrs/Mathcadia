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
}
