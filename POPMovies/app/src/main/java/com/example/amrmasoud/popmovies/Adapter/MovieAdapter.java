package com.example.amrmasoud.popmovies.Adapter;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.amrmasoud.popmovies.Data.MovieContract;
import com.example.amrmasoud.popmovies.Model.MoviesElement;
import com.example.amrmasoud.popmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by amr masoud on 9/19/2015.
 */
class ViewHolder {

    ImageView mMoviePicture;
}
public class MovieAdapter extends CursorAdapter{

    ViewHolder  holder = null;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);


    }




    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        // well set up the ViewHolder
        holder = new ViewHolder();
        holder.mMoviePicture = (ImageView) view.findViewById(R.id.image);

        // store the holder with the view.
        view.setTag(holder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // we've just avoided calling findViewById() on resource every time
        // just use the viewHolder
        holder = (ViewHolder) view.getTag();

        MoviesElement movieItem = convertCursorRowToMovieObject(cursor);

        //load image with picasso and append base path url
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185"+movieItem.mPosterPath).resize(200,500).into(holder.mMoviePicture);

    }

    public MoviesElement convertCursorRowToMovieObject(Cursor cursor) {
        ContentValues cv = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, cv);
        // get row indices for our cursor
        MoviesElement obj =  new MoviesElement(
                cv.getAsLong(MovieContract.COLUMN_ID),
                cv.getAsString(MovieContract.COLUMN_TITLE),
                cv.getAsString(MovieContract.COLUMN_RELEASE_DATE),
                cv.getAsString(MovieContract.COLUMN_OVERVIEW),
                cv.getAsString(MovieContract.COLUMN_POSTER_PATH),
                cv.getAsString(MovieContract.COLUMN_VOTE_AVERAGE),
                cv.getAsString(MovieContract.COLUMN_SORTED)
        );

        return obj;
    }


}