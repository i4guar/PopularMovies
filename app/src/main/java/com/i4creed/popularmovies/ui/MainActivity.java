package com.i4creed.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.i4creed.popularmovies.R;
import com.i4creed.popularmovies.backgroundtasks.AsyncTaskLoaderCompleteListener;
import com.i4creed.popularmovies.backgroundtasks.MovieQueryLoaderCallback;
import com.i4creed.popularmovies.model.Movie;
import com.i4creed.popularmovies.utils.MovieDbUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.i4creed.popularmovies.backgroundtasks.MovieQueryLoaderCallback.CONTENT_PROVIDER_EXTRA;
import static com.i4creed.popularmovies.backgroundtasks.MovieQueryLoaderCallback.URL_EXTRA;

/**
 * Created by Felix Houghton-Larsen on 20.02.2018 at 16:18.
 * MainActivity is the launch activity.
 * restoring configuration changes of layout manager
 * SOURCE:
 *  https://stackoverflow.com/questions/27816217/how-to-save-recyclerviews-scroll-position-using-recyclerview-state
 *  http://panavtec.me/retain-restore-recycler-view-scroll-position
 */
public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener,
        AsyncTaskLoaderCompleteListener<ArrayList<Movie>> {

    public static final String MOVIE_PARCELABLE_KEY = "movie";
    public static final String LAYOUT_MANAGER_PARCELABLE_KEY = "layoutManager";
    private static final int MOVIE_QUERY_LOADER = 22;
    @BindView(R.id.movies_rv)
    RecyclerView moviesRv;
    private MoviesAdapter moviesAdapter;

    private Parcelable layoutManagerState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesRv.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MoviesAdapter(this);
        moviesRv.setAdapter(moviesAdapter);
        LoaderManager loaderManager = getSupportLoaderManager();
        if (loaderManager.getLoader(MOVIE_QUERY_LOADER) == null) {
            loadData(MovieDbUtils.POPULAR_PATH);
        } else {
            loaderManager.initLoader(MOVIE_QUERY_LOADER, null,
                    new MovieQueryLoaderCallback(this, this));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAYOUT_MANAGER_PARCELABLE_KEY,
                moviesRv.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        layoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_PARCELABLE_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.sort_by_top_rated:
                loadData(MovieDbUtils.TOP_RATED_PATH);
                break;
            case R.id.sort_favorites:
                loadFavorites();
                break;
            case R.id.sort_by_popularity:
            default:
                loadData(MovieDbUtils.POPULAR_PATH);
        }
        return true;
    }

    /**
     * Loads favorite movies from the content provider into the adapter
     */
    private void loadFavorites() {
        Bundle queryBundle = new Bundle();
        queryBundle.putBoolean(CONTENT_PROVIDER_EXTRA, true);
        getSupportLoaderManager().restartLoader(MOVIE_QUERY_LOADER, queryBundle,
                new MovieQueryLoaderCallback(this, this));
    }


    /**
     * loads Data into the adapter by restarting loader
     *
     * @param sortPath appending path for sorting
     */
    private void loadData(String sortPath) {
        URL url = null;
        try {
            url = new URL(MovieDbUtils.MOVIE_DB_URL + sortPath + MovieDbUtils.API_KEY_QUERY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bundle queryBundle = new Bundle();
        queryBundle.putString(URL_EXTRA, url != null ? url.toString() : null);

        getSupportLoaderManager().restartLoader(MOVIE_QUERY_LOADER, queryBundle,
                new MovieQueryLoaderCallback(this, this));
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_PARCELABLE_KEY, moviesAdapter.getMovie(clickedItemIndex));
        startActivity(intent);
    }

    @Override
    public void onTaskComplete(ArrayList<Movie> result) {
        if (result != null) {
            moviesAdapter.setMoviesList(result);
            moviesRv.getLayoutManager().onRestoreInstanceState(layoutManagerState);
        } else {
            Toast.makeText(MainActivity.this,
                    R.string.failed_fetch, Toast.LENGTH_LONG).show();
        }
    }
}
