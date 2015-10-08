package com.example.amrmasoud.popmovies.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by amr masoud on 9/22/2015.
 */
public class MoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int POPMOVIES = 100;
    static final int TOPMOVIES = 200;
    static final int POPMOVIES_WITH_ID= 300;
    static final int TOPMOVIES_WITH_ID = 400;
    static final int FAVMOVIES = 500;
    static final int FAVMOVIES_WITH_ID = 600;
    static final int TRAILERSMOVIE = 700;
    static final int TRAILERSMOVIE_WITH_MOVIE_ID = 800;
    static final int REVIEWSMOVIE = 900;
    static final int REVIEWSMOVIE_WITH_MOVIE_ID = 1000;


    private static final SQLiteQueryBuilder sMoviesQueryBuilder;

    static{
        sMoviesQueryBuilder = new SQLiteQueryBuilder();
        //sMoviesQueryBuilder.setTables(MovieContract.MoviesEntry.TABLE_NAME );
    }



    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.TOPMoviesEntry.PATH_MOVIES, TOPMOVIES);
        matcher.addURI(authority, MovieContract.POPMoviesEntry.PATH_MOVIES, POPMOVIES);
        matcher.addURI(authority, MovieContract.TOPMoviesEntry.PATH_MOVIES  + "/*", TOPMOVIES_WITH_ID);
        matcher.addURI(authority, MovieContract.TOPMoviesEntry.PATH_MOVIES  + "/#", TOPMOVIES_WITH_ID);
        matcher.addURI(authority, MovieContract.POPMoviesEntry.PATH_MOVIES  + "/*", POPMOVIES_WITH_ID);
        matcher.addURI(authority, MovieContract.POPMoviesEntry.PATH_MOVIES  + "/#", POPMOVIES_WITH_ID);
        matcher.addURI(authority, MovieContract.FAVMoviesEntry.PATH_MOVIES, FAVMOVIES);
        matcher.addURI(authority, MovieContract.FAVMoviesEntry.PATH_MOVIES  + "/#",FAVMOVIES_WITH_ID );
        matcher.addURI(authority, MovieContract.FAVMoviesEntry.PATH_MOVIES  + "/*",FAVMOVIES_WITH_ID );
        matcher.addURI(authority, MovieContract.TrailersMovieEntry.PATH_TRAILERS, TRAILERSMOVIE);
        matcher.addURI(authority, MovieContract.TrailersMovieEntry.PATH_TRAILERS + "/#", TRAILERSMOVIE_WITH_MOVIE_ID);

        matcher.addURI(authority, MovieContract.ReviewsMovieEntry.PATH_REVIEWS, REVIEWSMOVIE);
        matcher.addURI(authority, MovieContract.ReviewsMovieEntry.PATH_REVIEWS  + "/#", REVIEWSMOVIE_WITH_MOVIE_ID);
        return matcher;
    }

    private static final String sPOPMoviesIDSelection =
            MovieContract.COLUMN_ID + " = ? ";
    private static final String sTOPMoviesIDSelection =
            MovieContract.COLUMN_ID + " = ? ";

    private static final String sMoviesIDSelection =
            MovieContract.COLUMN_ID + " = ? ";


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case POPMOVIES:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(MovieContract.POPMoviesEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }

            case TOPMOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(MovieContract.TOPMoviesEntry.TABLE_NAME,projection, selection,selectionArgs, null, null, sortOrder);
                break;
            }

            case POPMOVIES_WITH_ID: {
                retCursor = getPopMoviesWithMovieId(uri,projection,sortOrder);
                break;
            }

            case TOPMOVIES_WITH_ID: {
                retCursor = getTopMoviesWithMovieId(uri, projection, sortOrder);
                break;
            }

            case FAVMOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FAVMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case FAVMOVIES_WITH_ID:
                retCursor = getFavoriteMoviesWithMovieId(uri, projection, sortOrder);
                break;

            case TRAILERSMOVIE_WITH_MOVIE_ID:
                retCursor = getTrailerWithMovieId(uri, projection, sortOrder);
                break;
            case REVIEWSMOVIE_WITH_MOVIE_ID:
                retCursor = getReviewWithMovieId(uri, projection, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getFavoriteMoviesWithMovieId(Uri uri, String[] projection, String sortOrder) {
        String id = uri.getPathSegments().get(1);

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.FAVMoviesEntry.TABLE_NAME,
                projection,
                sMoviesIDSelection,
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }


    private Cursor getPopMoviesWithMovieId(Uri uri, String[] projection, String sortOrder) {
        String id = uri.getPathSegments().get(1);

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.POPMoviesEntry.TABLE_NAME,
                projection,
                sPOPMoviesIDSelection,
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTopMoviesWithMovieId(Uri uri, String[] projection, String sortOrder) {
        String id = uri.getPathSegments().get(1);

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.TOPMoviesEntry.TABLE_NAME,
                projection,
                sTOPMoviesIDSelection,
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTrailerWithMovieId(Uri uri, String[] projection, String sortOrder) {
        String id = uri.getPathSegments().get(1);

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.TrailersMovieEntry.TABLE_NAME,
                projection,
                sMoviesIDSelection,
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviewWithMovieId(Uri uri, String[] projection, String sortOrder) {
        String id = uri.getPathSegments().get(1);

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.ReviewsMovieEntry.TABLE_NAME,
                projection,
                sMoviesIDSelection,
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }


    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POPMOVIES:
                return MovieContract.POPMoviesEntry.CONTENT_TYPE;

            case POPMOVIES_WITH_ID:
                return MovieContract.POPMoviesEntry.CONTENT_ITEM_TYPE;
            case TOPMOVIES:
                return MovieContract.TOPMoviesEntry.CONTENT_TYPE;
            case TOPMOVIES_WITH_ID:
                return MovieContract.TOPMoviesEntry.CONTENT_ITEM_TYPE;
            case FAVMOVIES:
                return MovieContract.FAVMoviesEntry.CONTENT_TYPE;
            case FAVMOVIES_WITH_ID:
                return MovieContract.FAVMoviesEntry.CONTENT_ITEM_TYPE;
            case TRAILERSMOVIE:
                return MovieContract.TrailersMovieEntry.CONTENT_TYPE;
            case TRAILERSMOVIE_WITH_MOVIE_ID:
                return MovieContract.TrailersMovieEntry.CONTENT_ITEM_TYPE;

            case REVIEWSMOVIE:
                return MovieContract.ReviewsMovieEntry.CONTENT_TYPE;
            case REVIEWSMOVIE_WITH_MOVIE_ID:
                return MovieContract.ReviewsMovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TOPMOVIES: {
                long _topid = db.insert(MovieContract.TOPMoviesEntry.TABLE_NAME, null, values);
                if ( _topid > 0 )
                    returnUri = MovieContract.TOPMoviesEntry.buildMoviesUriWithID(_topid);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case POPMOVIES: {
                long _id = db.insert(MovieContract.POPMoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.POPMoviesEntry.buildMoviesUriWithID(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case FAVMOVIES_WITH_ID: {
                long _FAVid = db.insert(MovieContract.FAVMoviesEntry.TABLE_NAME, null, values);
                if (_FAVid > 0)
                    returnUri = MovieContract.FAVMoviesEntry.buildMoviesUriWithID(_FAVid);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POPMOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.POPMoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TOPMOVIES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TOPMoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TRAILERSMOVIE:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TrailersMovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case REVIEWSMOVIE:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.ReviewsMovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case POPMOVIES:
                rowsDeleted = db.delete(
                        MovieContract.POPMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TOPMOVIES:
                rowsDeleted = db.delete(
                        MovieContract.TOPMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVMOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                rowsDeleted = db.delete(
                        MovieContract.FAVMoviesEntry.TABLE_NAME, sMoviesIDSelection,   new String[]{id});
                break;
            case TRAILERSMOVIE_WITH_MOVIE_ID:
                String id1 = uri.getPathSegments().get(1);
                rowsDeleted = db.delete(
                        MovieContract.TrailersMovieEntry.TABLE_NAME, sMoviesIDSelection,   new String[]{id1});
                break;
            case REVIEWSMOVIE_WITH_MOVIE_ID:
                id = uri.getPathSegments().get(1);
                rowsDeleted = db.delete(
                        MovieContract.ReviewsMovieEntry.TABLE_NAME, sMoviesIDSelection,   new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case POPMOVIES:
                rowsUpdated = db.update(
                        MovieContract.POPMoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TOPMOVIES:
                rowsUpdated = db.update(
                        MovieContract.TOPMoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
