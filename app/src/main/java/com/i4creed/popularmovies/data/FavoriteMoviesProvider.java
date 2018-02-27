package com.i4creed.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by i4gua on 24-Feb-18 at 18:13.
 * Source: https://github.com/udacity/android-content-provider/blob/master/app/src/main/java/com/sam_chordas/android/androidflavors/data/FlavorsDBHelper.java
 */
public class FavoriteMoviesProvider extends ContentProvider {
    // Codes for the UriMatcher
    private static final int FAVORITE_MOVIES = 100;
    private static final int FAVORITE_MOVIES_WITH_ID = 200;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMoviesDBHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES, FAVORITE_MOVIES);
        matcher.addURI(authority, FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES + "/#", FAVORITE_MOVIES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FavoriteMoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            // All Flavors selected
            case FAVORITE_MOVIES: {
                cursor = dbHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return cursor;
            }
            // Individual flavor based on Id selected
            case FAVORITE_MOVIES_WITH_ID: {
                cursor = dbHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES,
                        projection,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return cursor;
            }
            default: {
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITE_MOVIES: {
                return FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_DIR_TYPE;
            }
            case FAVORITE_MOVIES_WITH_ID: {
                return FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIES: {
                long _id = db.insert(FavoriteMoviesContract.FavoriteMovieEntry.
                        TABLE_FAVORITE_MOVIES, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = FavoriteMoviesContract.FavoriteMovieEntry.buildFavoriteMoviesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match) {
            case FAVORITE_MOVIES:
                numDeleted = db.delete(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES + "'");
                break;
            case FAVORITE_MOVIES_WITH_ID:
                numDeleted = db.delete(FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numUpdated = 0;

        if (values == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIES: {
                numUpdated = db.update(FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITE_MOVIES_WITH_ID: {
                numUpdated = db.update(FavoriteMoviesContract.FavoriteMovieEntry.TABLE_FAVORITE_MOVIES,
                        values,
                        FavoriteMoviesContract.FavoriteMovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
