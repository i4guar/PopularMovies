package com.i4creed.popularmovies.backgroundtasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.i4creed.popularmovies.data.FavoriteMoviesContract;
import com.i4creed.popularmovies.model.Movie;
import com.i4creed.popularmovies.utils.CursorUtils;
import com.i4creed.popularmovies.utils.JsonUtils;
import com.i4creed.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by i4gua on 24-Feb-18 at 13:40.
 * This LoaderCallback differentiates between an content provider request and an http request
 * for populating a view with an array list of movies
 */
public class MovieQueryLoaderCallback implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    public static final String URL_EXTRA = "search url";
    public static final String CONTENT_PROVIDER_EXTRA = "cp extra";


    private AsyncTaskLoaderCompleteListener<ArrayList<Movie>> listener;
    private Context context;

    /**
     * The constructor
     * @param listener is informed when data is available
     * @param context current context
     */
    public MovieQueryLoaderCallback(AsyncTaskLoaderCompleteListener<ArrayList<Movie>> listener, Context context) {
        this.listener = listener;
        this.context  = context;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        boolean contentProvider = args.getBoolean(CONTENT_PROVIDER_EXTRA, false);
        if (contentProvider) {
            return new AsyncTaskLoader<ArrayList<Movie>>(context) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }
                    forceLoad();
                }

                @Override
                public ArrayList<Movie> loadInBackground() {
                    Cursor c = context.getContentResolver().query(FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,null, null, null, null);
                    return CursorUtils.cursorToList(c);
                }

            };
        } else {
            return new AsyncTaskLoader<ArrayList<Movie>>(context) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }
                    forceLoad();
                }

                @Override
                public ArrayList<Movie> loadInBackground() {
                    String url = args.getString(URL_EXTRA);
                    if (url == null || TextUtils.isEmpty(url)) {
                        return null;
                    }

                    try {
                        URL movieDbUrl = new URL(url);
                        return JsonUtils.parseMovieListJson(NetworkUtils.getResponseFromHttpUrl(movieDbUrl));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

            };
        }
    }



    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        listener.onTaskComplete(data);

    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}
