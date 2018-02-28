package com.i4creed.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.i4creed.popularmovies.R;
import com.i4creed.popularmovies.backgroundtasks.AsyncTaskLoaderCompleteListener;
import com.i4creed.popularmovies.backgroundtasks.MovieQueryLoaderCallback;
import com.i4creed.popularmovies.backgroundtasks.QueryLoaderCallback;
import com.i4creed.popularmovies.model.Movie;
import com.i4creed.popularmovies.utils.JsonUtils;
import com.i4creed.popularmovies.utils.MovieDbUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity for showing reviews
 */
public class ReviewsActivity extends AppCompatActivity {

    private static final int REVIEW_LOADER_ID = 20;

    @BindView(R.id.reviews_rv)
    RecyclerView reviewsRv;

    private ReviewsAdapter reviewsAdapter = new ReviewsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        final Movie movie = getIntent().getParcelableExtra(MainActivity.MOVIE_PARCELABLE_KEY);
        setTitle(movie.getTitle());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewsRv.setLayoutManager(linearLayoutManager);
        reviewsRv.setAdapter(reviewsAdapter);
        Bundle bundle = new Bundle();
        bundle.putString(MovieQueryLoaderCallback.URL_EXTRA, MovieDbUtils.MOVIE_DB_URL + String.format(MovieDbUtils.REVIEWS_PATH_FORMAT, movie.getId()) + MovieDbUtils.API_KEY_QUERY);
        getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, bundle, new QueryLoaderCallback(new AsyncTaskLoaderCompleteListener<String>() {
            @Override
            public void onTaskComplete(String result) {
                if (result != null && !result.equals("")) {
                    reviewsAdapter.setReviews(JsonUtils.parseReviewListJson(result));
                } else {
                    Toast.makeText(ReviewsActivity.this, R.string.failed_fetch, Toast.LENGTH_LONG).show();
                }
            }
        }, this));

    }
}
