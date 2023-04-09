package com.neu.numad23sp_team_34.project;

import java.util.List;

public class Story {
    private String id;
    private String title;
    private String description;
    private String review;
    private float rating;
    private List<String> imageUrl;
    private String userName;
    private List<String> keywords;

    private List<String> itinerary;

    public Story() {

    }

    public Story(String id, String title, String description, String review, float rating,
                 List<String> itinerary, List<String> imageUrl, String userName, List<String> keywords) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.review = review;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.keywords = keywords;
        this.itinerary = itinerary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getItinerary() {
        return itinerary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setItinerary(List<String> itinerary) {
        this.itinerary = itinerary;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    // Add getters and setters for each field
}
