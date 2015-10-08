package com.example.amrmasoud.popmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by amr masoud on 9/22/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOST_POPULAR_TABLE = "CREATE TABLE " + MovieContract.POPMoviesEntry.TABLE_NAME + " (" +
                MovieContract.POPMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.COLUMN_ID + " LONG NOT NULL, " +
                MovieContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL, " +
                MovieContract.COLUMN_SORTED +" TEXT NOT NULL);";

        final String SQL_CREATE_HIGHEST_RATED_TABLE = "CREATE TABLE " + MovieContract.TOPMoviesEntry.TABLE_NAME + " (" +
                MovieContract.POPMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.COLUMN_ID + " LONG NOT NULL, " +
                MovieContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL, " +
                MovieContract.COLUMN_SORTED +" TEXT NOT NULL);";

        final String SQL_CREATE_FAV_TABLE = "CREATE TABLE " + MovieContract.FAVMoviesEntry.TABLE_NAME + " (" +
                MovieContract.POPMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.COLUMN_ID + " LONG NOT NULL, " +
                MovieContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL, " +
                MovieContract.COLUMN_SORTED +" TEXT NOT NULL);";

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + MovieContract.TrailersMovieEntry.TABLE_NAME + " (" +
                MovieContract.TrailersMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.COLUMN_ID + " LONG NOT NULL, " +
                MovieContract.TrailersMovieEntry.COLUMN_TRI_NAME + " TEXT NOT NULL, " +
                MovieContract.TrailersMovieEntry.COLUMN_TRI_LINK + " TEXT NOT NULL);";


        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + MovieContract.ReviewsMovieEntry.TABLE_NAME + " (" +
                MovieContract.ReviewsMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.COLUMN_ID + " LONG NOT NULL, " +
                MovieContract.ReviewsMovieEntry.COLUMN_REV_AUTHOR + " TEXT NOT NULL, " +
                MovieContract.ReviewsMovieEntry.COLUMN_REV_CONTENT + " TEXT NOT NULL);";


        db.execSQL(SQL_CREATE_MOST_POPULAR_TABLE);
        db.execSQL(SQL_CREATE_HIGHEST_RATED_TABLE);
        db.execSQL(SQL_CREATE_FAV_TABLE);
        db.execSQL(SQL_CREATE_TRAILERS_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.POPMoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TOPMoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FAVMoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailersMovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewsMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
