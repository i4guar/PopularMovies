package com.i4creed.popularmovies.utils;

import android.graphics.Point;
import android.view.Display;

import com.i4creed.popularmovies.BuildConfig;

/**
 * Created by Felix Houghton-Larsen on 22-Feb-18 at 16:19.
 * Utility class for showing the movies properly
 */

public class MovieDbUtils {

    public static final String MOVIE_DB_URL = "http://api.themoviedb.org/3";
    public static final String POPULAR_PATH = "/movie/popular";
    public static final String TOP_RATED_PATH = "/movie/top_rated";
    public static final String VIDEOS_PATH_FORMAT = "/movie/%s/videos";
    public static final String REVIEWS_PATH_FORMAT = "/movie/%s/reviews";
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String VIDEO_URL = "https://www.youtube.com/watch?v=";
    public static final String THUMBNAIL_URL = "https://img.youtube.com/vi/";
    public static final String THUMBNAIL_END = "/0.jpg";
    private static final String API_KEY = BuildConfig.API_KEY; //TODO insert your API KEY in gradle.properties
    public static final String API_KEY_QUERY = "?api_key=" + API_KEY;

    /**
     * Returns the appropriate path for the image depending on the screen width
     *
     * @param display The display of current device
     * @return String of path
     */
    public static String getQuality(Display display) {
        if (display != null) {
            Point dimensions = new Point();
            display.getSize(dimensions);
            int width = ((dimensions.x < dimensions.y) ? dimensions.x : dimensions.y) / 3;
            if (width <= 92) {
                return "w92";
            } else if (width <= 154) {
                return "w154";
            } else if (width <= 185) {
                return "w185";
            } else if (width <= 342) {
                return "w342";
            } else if (width <= 500) {
                return "w500";
            } else if (width <= 780) {
                return "w780";
            } else {
                return "original";
            }
        } else {
            return "w185";
        }
    }

}
