package com.i4creed.popularmovies.utils;

import android.annotation.SuppressLint;

import com.i4creed.popularmovies.model.Movie;
import com.i4creed.popularmovies.model.Review;
import com.i4creed.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Felix Houghton-Larsen on 20.02.2018 at 16:18.
 * Utility class for parsing json strings
 */

public class JsonUtils {

    private static final String JSON_ARRAY_RESULTS = "results";
    private static final String JSON_DATE_FORMAT = "yyyy-MM-dd";
    private static final String JSON_ID = "id";
    private static final String JSON_ORIGINAL_TITLE = "original_title";
    private static final String JSON_TITLE = "title";
    private static final String JSON_RELEASE_DATE = "release_date";
    private static final String JSON_POSTER_PATH = "poster_path";
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_OVERVIEW = "overview";
    private static final String JSON_TRAILER_NAME = "name";
    private static final String JSON_TRAILER_KEY = "key";
    private static final String JSON_REVIEW_AUTHOR = "author";
    private static final String JSON_REVIEW_CONTENT = "content";

    /**
     * Parses a json string into an array list of movies
     *
     * @param json json string
     * @return array list of movies
     */
    public static ArrayList<Movie> parseMovieListJson(String json) {
        ArrayList<Movie> movies = new ArrayList<>();
        JSONObject jsonMovies;
        try {
            jsonMovies = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        JSONArray movieArray = jsonMovies.optJSONArray(JSON_ARRAY_RESULTS);
        for (int i = 0; i < movieArray.length(); i++) {
            try {
                movies.add(parseMovieJson(movieArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }

    /**
     * Parses a Json object into a movie
     *
     * @param jsonMovie json object
     * @return movie
     */
    private static Movie parseMovieJson(JSONObject jsonMovie) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(JSON_DATE_FORMAT);
        int id = jsonMovie.optInt(JSON_ID);
        String originalTitle = jsonMovie.optString(JSON_ORIGINAL_TITLE);
        String title = jsonMovie.optString(JSON_TITLE);
        Date releaseDate = null;
        try {
            releaseDate = dateFormat.parse(jsonMovie.optString(JSON_RELEASE_DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String moviePosterUrl = jsonMovie.optString(JSON_POSTER_PATH);
        double voteAverage = jsonMovie.optDouble(JSON_VOTE_AVERAGE);
        String overview = jsonMovie.optString(JSON_OVERVIEW);
        return new Movie(id, originalTitle, title, releaseDate, moviePosterUrl, voteAverage, overview);
    }

    /**
     * Parses a json string into an array list of trailers
     *
     * @param json json string
     * @return array list of trailers
     */
    public static ArrayList<Trailer> parseTrailerListJson(String json) {
        ArrayList<Trailer> trailers = new ArrayList<>();
        JSONObject jsonTrailers;
        try {
            jsonTrailers = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        JSONArray trailerArray = jsonTrailers.optJSONArray(JSON_ARRAY_RESULTS);
        for (int i = 0; i < trailerArray.length(); i++) {
            try {
                trailers.add(parseTrailerJson(trailerArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return trailers;
    }

    /**
     * Parses a Json object into a trailer
     *
     * @param jsonTrailer json object
     * @return trailer
     */
    private static Trailer parseTrailerJson(JSONObject jsonTrailer) {
        String name = jsonTrailer.optString(JSON_TRAILER_NAME);
        String key = jsonTrailer.optString(JSON_TRAILER_KEY);
        return new Trailer(name, key);
    }

    /**
     * Parses a json string into an array list of reviews
     *
     * @param json json string
     * @return array list of reviews
     */
    public static ArrayList<Review> parseReviewListJson(String json) {
        ArrayList<Review> reviews = new ArrayList<>();
        JSONObject jsonReviews;
        try {
            jsonReviews = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        JSONArray reviewArray = jsonReviews.optJSONArray(JSON_ARRAY_RESULTS);
        for (int i = 0; i < reviewArray.length(); i++) {
            try {
                reviews.add(parseReviewJson(reviewArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviews;
    }

    /**
     * Parses a Json object into a review
     *
     * @param jsonReviews json object
     * @return review
     */
    private static Review parseReviewJson(JSONObject jsonReviews) {
        String author = jsonReviews.optString(JSON_REVIEW_AUTHOR);
        String content = jsonReviews.optString(JSON_REVIEW_CONTENT);
        return new Review(author, content);
    }
}
