package com.i4creed.popularmovies.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.i4creed.popularmovies.R;
import com.i4creed.popularmovies.model.Movie;
import com.i4creed.popularmovies.utils.MovieDbUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Felix Houghton-Larsen on 20.02.2018.
 * Adapter for the recycler view of the movies
 * Implementation of ClickListener Source: Udacity Android Development Lesson 4 Video 20 Responding to Item Clicks
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    final private ListItemClickListener onClickListener;
    private ArrayList<Movie> moviesList = new ArrayList<>();

    public MoviesAdapter(ListItemClickListener listItemClickListener) {
        onClickListener = listItemClickListener;
    }

    public MoviesAdapter(ArrayList<Movie> moviesList, ListItemClickListener onClickListener) {
        this.moviesList = moviesList;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(moviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    /**
     * Set the data for adapter
     *
     * @param moviesList the array list of movies for the data
     */
    void setMoviesList(ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }

    /**
     * Returns the movie of the array list at the index
     *
     * @param index of the movie to return
     * @return movie at the index
     */
    public Movie getMovie(int index) {
        return moviesList.get(index);
    }

    /**
     * Click listener for list items
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * ViewHolder for the list items of movies
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_poster_iv)
        ImageView moviePosterIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            Context context = moviePosterIv.getContext();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            if (wm != null) {
                display = wm.getDefaultDisplay();
            }
            Picasso.with(context).load(MovieDbUtils.IMAGE_URL + MovieDbUtils.getQuality(display) + movie.getMoviePosterUrl()).into(moviePosterIv);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClick(clickedPosition);
        }
    }
}
