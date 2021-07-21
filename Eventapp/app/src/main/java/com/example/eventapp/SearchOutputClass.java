package com.example.eventapp;

public class SearchOutputClass {
    private String Date;
    private String EventName;
    private String Venue;
    private String Id;
    private String mycategory;
    private String latitude;
    private String longitude;

    public SearchOutputClass(String eventName,String venue, String date,String id, String Category,String lat, String lng) {
        this.Date = date;
        this.EventName = eventName;
        this.Venue = venue;
        this.Id = id;
        this.mycategory = Category;
        this.latitude = lat;
        this.longitude = lng;
    }

    public String getDate() {
        return Date;
    }

    public String getEventName() {
        return EventName;
    }

    public String getVenue() {
        return Venue;
    }

    public String getCategory() {
        return mycategory;
    }

    public String getId() {
        return Id;
    }

    public String getLat(){return latitude; }

    public String getLng(){return longitude;}

    public void setDate(String date) {
        Date = date;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setLat(String lat) { latitude = lat; }

    public void setLng(String lng) {
        longitude = lng;
    }

    public void setCategory(String Category) {
        Category = Category;
    }



    @Override
    public String toString() {
        return "SearchOutputClass{" +
                "Date='" + Date + '\'' +
                ", EventName='" + EventName + '\'' +
                ", venue='" + Venue + '\'' +
                ", Id='" + Id + '\'' +
                '}';
    }
}
