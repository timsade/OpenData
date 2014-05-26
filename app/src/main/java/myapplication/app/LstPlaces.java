package myapplication.app;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Eleonore on 20/05/14.
 */
public class LstPlaces {

    private static ArrayList<Place> lstPlaces;

    public LstPlaces(){
        lstPlaces = new ArrayList<Place>();
    }

    public static ArrayList<Place> getLstPlaces(){ return lstPlaces; }

    public void addPlace(Place p){ this.lstPlaces.add(p); }

    public ArrayList<Place> getPlacesByTag(String tag){
        ArrayList<Place> pList = new ArrayList<Place>();
        for(Place p : lstPlaces){
            if(p.getTagPlace().equals(tag)){
                pList.add(p);
            }
        }
        return pList;
    }

}
