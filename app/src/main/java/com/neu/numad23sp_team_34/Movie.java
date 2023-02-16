package com.neu.numad23sp_team_34;

public class Movie {

    private final String title;

    private final String year;

    private final String imageUrl;

    private final String imdbId;

    /**
     * Constructs a person object with the specified name and age.
     *  @param title - name of the link.
     * @param year -  Url.
     * @param imdbId
     *
     */
    public Movie(String title, String year, String imageUrl, String imdbId) {
        this.title = title;
        this.year = year;
        this.imageUrl = imageUrl;
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImdbId() {
        return imdbId;
    }
}
