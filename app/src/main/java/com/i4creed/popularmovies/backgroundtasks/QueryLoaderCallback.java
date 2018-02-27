package com.i4creed.popularmovies.backgroundtasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.i4creed.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by i4gua on 27-Feb-18 at 18:02.
 * This LoaderCallback handles a http request and informs the connected listener
 */
public class QueryLoaderCallback implements LoaderManager.LoaderCallbacks<String> {
    public static final String SEARCH_QUERY_URL_EXTRA = "search url";

    private AsyncTaskLoaderCompleteListener<String> listener;
    private Context context;

    /**
     * The constructor
     * @param listener is informed when a http answer is available
     * @param context current context
     */
    public QueryLoaderCallback(AsyncTaskLoaderCompleteListener<String> listener, Context context) {
        this.listener = listener;
        this.context  = context;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null){
                    return;
                }
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String url = args.getString(SEARCH_QUERY_URL_EXTRA);
                if( url == null || TextUtils.isEmpty(url)) {
                    return null;
                }

                try {
                    URL movieDbUrl = new URL(url);
                    return NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        listener.onTaskComplete(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


}