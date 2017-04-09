package org.bts.atry.mymusicplayer;

/**
 * Created by Jeroen on 06/04/2017.
 */

public class SongObj {

    // Object Fields/Properties
    private String mTitle;
    private int mDataFile;
    private String mCountry;
    private String mDescription;

    //Object constructor
    public SongObj(String title, int resource, String country, String description) {
        this.mTitle = title;
        this.mDataFile = resource;
        this.mCountry = country;
        this.mDescription = description;
    }

    public String getTitle() {
        return mTitle;
    }

    //Getters and Setters
    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getdataFile() {
        return mDataFile;
    }

    public void setdataFile(int dataFile) {
        this.mDataFile = dataFile;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        this.mCountry = country;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

}
