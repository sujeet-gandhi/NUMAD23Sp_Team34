package com.neu.numad23sp_team_34.wanderlust.details.Activities;

import java.util.List;

public class Trip {

    String tripId;
    String tripName;
    String tripDestination;
    String tripType;
    int tripPrice;
    float tripRating;
    String startDate;
    String endDate;
    String photoURL;
    boolean isFavorite;

    private List<String> keywords;

    private List<String> itinerary;


    private String username;

    private long timestamp;




    public Trip() {
    }

    public Trip(String tripId, String tripName, String tripDestination, String tripType, int tripPrice, float tripRating, String startDate, String endDate, String photoURL, boolean isFavorite,List<String> keywords, List<String> itinerary, String username) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.tripDestination = tripDestination;
        this.tripType = tripType;
        this.tripPrice = tripPrice;
        this.tripRating = tripRating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.photoURL = photoURL;
        this.isFavorite = isFavorite;
        this.keywords = keywords;
        this.itinerary = itinerary;
        this.username = username;
    }

    public Trip(String tripName, String tripDestination, String tripType, int tripPrice, float tripRating, String startDate, String endDate, String photoURL, boolean isFavorite, String username) {
        this.tripName = tripName;
        this.tripDestination = tripDestination;
        this.tripType = tripType;
        this.tripPrice = tripPrice;
        this.tripRating = tripRating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.photoURL = photoURL;
        this.isFavorite = isFavorite;
        this.keywords = keywords;
        this.itinerary = itinerary;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getTripType() {
        return tripType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public int getTripPrice() {
        return tripPrice;
    }

    public void setTripPrice(int tripPrice) {
        this.tripPrice = tripPrice;
    }

    public float getTripRating() {
        return tripRating;
    }

    public void setTripRating(float tripRating) {
        this.tripRating = tripRating;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<String> getItinerary() {
        return itinerary;
    }


    public void setItinerary(List<String> itinerary) {
        this.itinerary = itinerary;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }


    @Override
    public String toString() {
        return "Trip{" +
                "tripId='" + tripId + '\'' +
                ", tripName='" + tripName + '\'' +
                ", tripDestination='" + tripDestination + '\'' +
                ", tripType='" + tripType + '\'' +
                ", tripPrice=" + tripPrice +
                ", tripRating=" + tripRating +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", isFavorite=" + isFavorite +

                '}';
    }
}
