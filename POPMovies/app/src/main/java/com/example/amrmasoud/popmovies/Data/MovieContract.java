package com.example.amrmasoud.popmovies.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by amr masoud on 9/22/2015.
 */
public class MovieContract {


    public static final String CONTENT_AUTHORITY = "com.example.amrmasoud.popmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Column with the id of the movie.
    public static final String COLUMN_ID = "movie_id";
    // Column with the id of the movie.
    public static final String COLUMN_TITLE = "original_title";
    // Column with the id of the movie.
    public static final String COLUMN_RELEASE_DATE = "release_date";
    // Column with the id of the movie.
    public static final String COLUMN_OVERVIEW = "overview";
    // Column with the id of the movie.
    public static final String COLUMN_POSTER_PATH = "poster_path";

    // Column with the id of the movie.
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";

    public static final String COLUMN_SORTED = "sorted_by";


    public static final class POPMoviesEntry implements BaseColumns {

        public static final String PATH_MOVIES = "popmovies";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "popmovies";


        public static Uri buildMoviesUriWithID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesUriWithMovieId(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }

    }


    public static final class TOPMoviesEntry implements BaseColumns {

        public static final String PATH_MOVIES = "topmovies";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "topmovies";


        public static Uri buildMoviesUriWithID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesUriWithMovieId(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }

    }


    public static final class FAVMoviesEntry implements BaseColumns {

        public static final String PATH_MOVIES = "favmovies";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "favmovies";


        public static Uri buildMoviesUriWithID(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);

        }

        public static Uri buildMoviesUriWithMovieId(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }

    }

    /* Inner class that defines the table contents of the Trailers table */
    public static final class TrailersMovieEntry implements BaseColumns {

        public static final String PATH_TRAILERS = "trailers";

        // Column with the name of the trailer.
        public static final String COLUMN_TRI_NAME = "trailer_name";
        // Column with the link of the trailer.
        public static final String COLUMN_TRI_LINK = "trailer_key";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String TABLE_NAME = "trailers";

        public static Uri buildTrailerUriWithMovieId(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }

    }

    /* Inner class that defines the table contents of the Reviews table */
    public static final class ReviewsMovieEntry implements BaseColumns {

        public static final String PATH_REVIEWS = "reviews";

        // Column with the AUTHOR of the REVIEW.
        public static final String COLUMN_REV_AUTHOR = "review_author";
        // Column with the content of the review.
        public static final String COLUMN_REV_CONTENT = "review_content";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String TABLE_NAME = "reviews";

        public static Uri buildReviewUriWithMovieId(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build();
        }

    }
}
