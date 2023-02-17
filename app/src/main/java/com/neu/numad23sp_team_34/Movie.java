package com.neu.numad23sp_team_34;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Movie implements Parcelable {

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

    protected Movie(Parcel in) {
        title = in.readString();
        year = in.readString();
        imageUrl = in.readString();
        imdbId = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeString(title);
        parcel.writeString(year);
        parcel.writeString(imageUrl);
        parcel.writeString(imdbId);

    }
}
