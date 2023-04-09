package com.neu.numad23sp_team_34.project;

import java.util.List;

public class Story {
    private String id;
    private String title;
    private String description;
    private String review;
    private float rating;

    private List<String> itinerary;


    public Story(String storyId, String storyTitle, String storyDescription, String review, float rating, List<String> itinerary) {
        this.id = storyId;
        this.title = storyTitle;
        this.description = storyDescription;
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


    // Add getters and setters for each field
}
