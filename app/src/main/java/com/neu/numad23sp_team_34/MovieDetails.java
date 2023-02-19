package com.neu.numad23sp_team_34;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MovieDetails implements Parcelable {

    private final String title;
    private final String imageUrl;

    private final String rating;
    private final String releaseDate;
    private final String runTime;
    private final String plot;


    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRunTime() {
        return runTime;
    }

    public String getPlot() {
        return plot;
    }

    /**
     * Constructs a person object with the specified name and age.
     * @param title - name of the link.
     * @param rating
     * @param releaseDate
     * @param runTime
     * @param plot
     *
     */
    public MovieDetails(String title, String imageUrl, String rating, String releaseDate, String runTime, String plot) {
        this.title = title;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.runTime = runTime;
        this.plot = plot;
        this.imageUrl = imageUrl;

    }

    protected MovieDetails(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
        runTime = in.readString();
        plot = in.readString();


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


    public String getImageUrl() {
        return imageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeString(title);
        parcel.writeString(imageUrl);
        parcel.writeString(rating);
        parcel.writeString(releaseDate);
        parcel.writeString(runTime);
        parcel.writeString(plot);

    }
}