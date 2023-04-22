package com.neu.numad23sp_team_34.project;

import java.util.List;
import java.util.Objects;

public class Story {

    private String id;
    private String title;
    private String description;
    private String review;
    private float rating;
    private String userName;
    private List<String> keywords;
    private List<String> imageUrl;
    private List<String> itinerary;
    private List<String> favoriteUserIds;

    public Story() {

    }

    public Story(String id, String title, String description, String review, float rating,
                 List<String> itinerary, List<String> imageUrl, String userName, List<String> keywords,
                 List<String> favoriteUserIds) {
        this.id = id;
        this.title = title;
        this.review = review;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.keywords = keywords;
        this.itinerary = itinerary;
        this.description = description;
        this.favoriteUserIds = favoriteUserIds;
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

    public List<String> getFavoriteUserIds() {
        return favoriteUserIds;
    }

    public void setFavoriteUserIds(List<String> favoriteUserIds) {
        this.favoriteUserIds = favoriteUserIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Story story = (Story) o;
        return id.equals(story.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
