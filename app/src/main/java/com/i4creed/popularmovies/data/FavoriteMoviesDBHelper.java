package com.i4creed.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by i4gua on 24-Feb-18 at 18:03.
 * <p>
 * SOURCE: https://github.com/udacity/android-content-provider/blob/master/app/src/main/java/com/sam_chordas/android/androidflavors/data/FlavorsDBHelper.java
 */

public class FavoriteMoviesDBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = FavoriteMoviesDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES +
                "(" + FavoriteMoviesContract.FavoriteMovieEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID +
                " INTEGER, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE +
                " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_TITLE +
                " TEXT NOT NULL, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE +
                " INTEGER, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_URL +
                " TEXT, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE +
                " FLOAT, " +
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_OVERVIEW +
                " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES + "'");

        // re-create database
        onCreate(db);
    }
}
