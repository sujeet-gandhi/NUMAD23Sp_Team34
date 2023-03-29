package com.neu.numad23sp_team_34.project;

public class Story {
    private String id;
    private String title;
    private String description;
    private String itinerary;
    private String review;
    private float rating;

    public Story(String id, String title, String description, String review, float rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.itinerary = itinerary;
        this.review = review;
        this.rating = rating;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
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


    // Add getters and setters for each field
}
