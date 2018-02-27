package com.i4creed.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Felix Houghton-Larsen on 20.02.2018 at 16:19.
 * This class models a Movie
 */

public class Movie implements Parcelable {
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
    private int id;
    private String originalTitle;
    private String title;
    private Date releaseDate;
    private String moviePosterUrl;
    private double voteAverage;
    private String overview;

    public Movie(int id, String originalTitle, String title, Date releaseDate, String moviePosterUrl, double voteAverage, String overview) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePosterUrl = moviePosterUrl;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        title = in.readString();
        releaseDate = new Date(in.readLong());
        moviePosterUrl = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(title);
        dest.writeLong(releaseDate.getTime());
        dest.writeString(moviePosterUrl);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);

    }
}
