package myapplication.app;

/**
 * Created by Eleonore on 21/05/14.
 */
public class Place{

    private int idPlace;
    private String namePlace;
    private String tagPlace;
    private int tagId;
    private double gpsXPlace;
    private double gpsYPlace;
    private String addressPlace;
    private String descriptionPlace;
    private String openingsPlace;
    private double distanceToUser;

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
        this.distanceToUser = 0;

    }

    public Place(int id, String name, String tag, int tagid, double gpsX, double gpsY, String address, String description, String openings, double distance){
        this.idPlace = id;
        this.namePlace = name;
        this.tagPlace = tag;
        this.tagId = tagid;
        this.gpsXPlace = gpsX;
        this.gpsYPlace = gpsY;
        this.addressPlace = address;
        this.descriptionPlace = description;
        this.openingsPlace = openings;
        this.distanceToUser = distance;
    }

    public int getIdPlace(){ return this.idPlace ; }
    public String getNamePlace(){ return this.namePlace ; }
    public String getTagPlace(){ return this.tagPlace; }
    public int getTagId(){ return this.tagId; }
    public double getGpsXPlace(){ return this.gpsXPlace ; }
    public double getGpsYPlace(){ return this.gpsYPlace ; }
    public String getDescriptionPlace(){ return this.descriptionPlace; }
    public double getDistanceToUser(){ return this.distanceToUser; }

    public int compareTo(Place p2)
    {
        int resultat = 0;
        if (this.distanceToUser > p2.getDistanceToUser())
            resultat = 1;
        if (this.distanceToUser < p2.getDistanceToUser())
            resultat = -1;
        if (this.distanceToUser == p2.getDistanceToUser())
            resultat = 0;
        return resultat;
    }
}
