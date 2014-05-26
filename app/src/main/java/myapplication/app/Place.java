package myapplication.app;

/**
 * Created by Eleonore on 21/05/14.
 */
public class Place {

    private int idPlace;
    private String namePlace;
    private String tagPlace;
    private int tagId;
    private float gpsXPlace;
    private float gpsYPlace;
    private String addressPlace;
    private String descriptionPlace;
    private String openingsPlace;

    public Place(){
        this.idPlace = 0;
        this.namePlace = "";
        this.tagPlace = "";
        this.tagId = 0;
        this.gpsXPlace = 0;
        this.gpsYPlace = 0;
        this.addressPlace = "";
        this.descriptionPlace = "";
        this.openingsPlace = "";
    }

    public Place(int id, String name, String tag, int tagid, float gpsX, float gpsY, String address, String description, String openings){
        this.idPlace = id;
        this.namePlace = name;
        this.tagPlace = tag;
        this.tagId = tagid;
        this.gpsXPlace = gpsX;
        this.gpsYPlace = gpsY;
        this.addressPlace = address;
        this.descriptionPlace = description;
        this.openingsPlace = openings;
    }

    public int getIdPlace(){ return this.idPlace ; }
    public String getNamePlace(){ return this.namePlace ; }
    public String getTagPlace(){ return this.tagPlace; }
    public int getTagId(){ return this.tagId; }
    public float getGpsXPlace(){ return this.gpsXPlace ; }
    public float getGpsYPlace(){ return this.gpsYPlace ; }
    public String getDescriptionPlace(){ return this.descriptionPlace; }

}
