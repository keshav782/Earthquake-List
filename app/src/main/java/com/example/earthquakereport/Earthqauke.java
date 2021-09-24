package com.example.earthquakereport;

public class Earthqauke {
    //magnitude of the earthquake
    private Double magnitude;

    // location of the earthquake
    private String location;

    //date of the location
    private long mTimeInMilliseconds;

    //url of the earthquake
    private String mUrl;

    public Earthqauke(Double Magnitude,String Location,long TimeInMilliseconds,String url){
        magnitude=Magnitude;
        location=Location;
        mTimeInMilliseconds=TimeInMilliseconds;
        mUrl= url;
    }
    public Double getMagnitude(){
        return magnitude;
    }
    public String getLocation(){
        return location;
    }
    public long getTimeInMilliseconds(){
        return mTimeInMilliseconds;
    }
    public String getUrl(){
        return mUrl;
    }

}
