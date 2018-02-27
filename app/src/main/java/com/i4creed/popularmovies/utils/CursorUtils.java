package com.i4creed.popularmovies.utils;

import android.database.Cursor;

import com.i4creed.popularmovies.data.FavoriteMoviesContract;
import com.i4creed.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by i4gua on 24-Feb-18 at 18:34.
 * Utility class for dealing with a cursor
 */
public class CursorUtils {
    /**
     * Converts a cursor into an array list of movies
     *
     * @param cursor from content provider
     * @return array list of movies
     */
    public static ArrayList<Movie> cursorToList(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID));
            String originalTitle = cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE));
            String title = cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_TITLE));
            Date releaseDate = new Date(cursor.getLong(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE)));
            String moviePosterUrl = cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_URL));
            double voteAverage = cursor.getDouble(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE));
            String overview = cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_OVERVIEW));
            movies.add(new Movie(id, originalTitle, title, releaseDate, moviePosterUrl, voteAverage, overview));
        }
        return movies;
    }
}
