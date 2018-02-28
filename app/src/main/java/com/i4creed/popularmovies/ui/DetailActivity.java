package com.i4creed.popularmovies.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.i4creed.popularmovies.R;
import com.i4creed.popularmovies.backgroundtasks.AsyncTaskLoaderCompleteListener;
import com.i4creed.popularmovies.backgroundtasks.MovieQueryLoaderCallback;
import com.i4creed.popularmovies.backgroundtasks.QueryLoaderCallback;
import com.i4creed.popularmovies.data.FavoriteMoviesContract;
import com.i4creed.popularmovies.model.Movie;
import com.i4creed.popularmovies.model.Trailer;
import com.i4creed.popularmovies.utils.JsonUtils;
import com.i4creed.popularmovies.utils.MovieDbUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Felix Houghton-Larsen on 20.02.2018 at 16:18.
 * The activity for the details of the movie
 */
public class DetailActivity extends AppCompatActivity {

    private static final int TRAILER_LOADER_ID = 21;
    private static final String FAVORITE_TAG = "true";
    private static final String NO_FAVORITE_TAG = "false";

    @BindView(R.id.movie_title_tv)
    TextView movieTitleTv;
    @BindView(R.id.movie_poster_iv)
    ImageView moviePosterIv;
    @BindView(R.id.movie_release_date_tv)
    TextView movieReleaseDateTv;
    @BindView(R.id.movie_average_rating_tv)
    TextView movieAverageRatingTv;
    @BindView(R.id.movie_overview_tv)
    TextView movieOverviewTv;
    @BindView(R.id.favorite_iv)
    ImageView favoritesIv;
    @BindView(R.id.trailers_ll)
    LinearLayout trailersLl;

    ArrayList<Trailer> trailers = new ArrayList<>();


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Movie movie = getIntent().getParcelableExtra(MainActivity.MOVIE_PARCELABLE_KEY);
        ButterKnife.bind(this);
        setTitle(movie.getTitle());
        movieTitleTv.setText(movie.getOriginalTitle());
        Picasso.with(this).load(MovieDbUtils.IMAGE_URL + MovieDbUtils.getQuality(getWindowManager().getDefaultDisplay()) + movie.getMoviePosterUrl()).into(moviePosterIv);
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        movieReleaseDateTv.setText(dateFormat.format(movie.getReleaseDate()));
        movieAverageRatingTv.setText(String.format("%s%s", movie.getVoteAverage(), getString(R.string.max_rating)));
        movieAverageRatingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ReviewsActivity.class);
                intent.putExtra(MainActivity.MOVIE_PARCELABLE_KEY, movie);
                startActivity(intent);
            }
        });

        new AsyncTask<Void, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Void... voids) {
                return getContentResolver().query(FavoriteMoviesContract.FavoriteMovieEntry.buildFavoriteMoviesUri(movie.getId()), null, null, null, null);

            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                if (cursor.getCount() != 0) {
                    setFavoriteIv(true);
                } else {
                    setFavoriteIv(false);
                }
            }
        }.execute();

        favoritesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getFavoriteIv()) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            getContentResolver().delete(FavoriteMoviesContract.FavoriteMovieEntry.buildFavoriteMoviesUri(movie.getId()), null, null);
                            return null;
                        }
                    }.execute();
                } else {
                    final ContentValues values = new ContentValues();
                    values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movie.getId());
                    values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
                    values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_TITLE, movie.getTitle());
                    values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate().getTime());
                    values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_URL, movie.getMoviePosterUrl());
                    values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
                    values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            getContentResolver().insert(FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI, values);
                            return null;
                        }
                    }.execute();
                }
                setFavoriteIv(!getFavoriteIv());
            }
        });
        movieOverviewTv.setText(movie.getOverview());
        Bundle bundle = new Bundle();
        bundle.putString(MovieQueryLoaderCallback.URL_EXTRA, MovieDbUtils.MOVIE_DB_URL + String.format(MovieDbUtils.VIDEOS_PATH_FORMAT, movie.getId()) + MovieDbUtils.API_KEY_QUERY);
        getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, bundle, new QueryLoaderCallback(new AsyncTaskLoaderCompleteListener<String>() {
            @Override
            public void onTaskComplete(String result) {
                if (result != null && !result.equals("")) {
                    trailers = JsonUtils.parseTrailerListJson(result);
                    trailersLl.removeAllViews();
                    for (Trailer trailer : trailers) {
                        View view = getLayoutInflater().inflate(R.layout.trailer_item, null);
                        view.setTag(trailer.getKey());
                        ImageView thumbnailIv = view.findViewById(R.id.thumbnail_iv);
                        TextView trailerTitleTv = view.findViewById(R.id.trailer_title_tv);
                        Picasso.with(DetailActivity.this).load(MovieDbUtils.THUMBNAIL_URL + trailer.getKey() + MovieDbUtils.THUMBNAIL_END).into(thumbnailIv);
                        trailerTitleTv.setText(trailer.getName());
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieDbUtils.VIDEO_URL + v.getTag()));
                                startActivity(intent);
                            }
                        });
                        trailersLl.addView(view);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, R.string.failed_fetch, Toast.LENGTH_LONG).show();
                }

            }
        }, this));

    }

    private boolean getFavoriteIv() {
        return favoritesIv.getTag().equals(FAVORITE_TAG);
    }

    private void setFavoriteIv(boolean favorite) {
        if (favorite) {
            favoritesIv.setBackground(getResources().getDrawable(R.drawable.ic_star));
            favoritesIv.setTag(FAVORITE_TAG);
        } else {
            favoritesIv.setBackground(getResources().getDrawable(R.drawable.ic_star_border));
            favoritesIv.setTag(NO_FAVORITE_TAG);
        }
    }
}
