package com.example.amrmasoud.popmovies.Controller;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrmasoud.popmovies.Adapter.ReviewAdapter;
import com.example.amrmasoud.popmovies.Adapter.TrailerAdapter;
import com.example.amrmasoud.popmovies.Data.MovieContract;
import com.example.amrmasoud.popmovies.FetchData.GetDataReview;
import com.example.amrmasoud.popmovies.FetchData.GetDataTrailer;
import com.example.amrmasoud.popmovies.Model.MoviesElement;
import com.example.amrmasoud.popmovies.Model.ReviewElement;
import com.example.amrmasoud.popmovies.Model.TrailerElement;
import com.example.amrmasoud.popmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;
    private static final int TRAILERS_LOADER = 1;
    private static final int REVIEWS_LOADER = 2;

    static final String DETAIL_URI = "URI";
    private Uri mUri;
    private ContentValues values = new ContentValues();
    private String movieId;
    private String share;
    Uri favoriteUriWithId;
    Uri trailerUriWithId;
    Uri reviewUriWithId;

    private TextView movieName;
    private TextView movieReleaseDate;
    private TextView movieRate;
    private TextView movieOverview;
    private ImageView movieImage;
    private CheckBox chkIos;
    private ListView list_trailer;
    private ListView list_review;
    private ImageView Share;

    ArrayList<ReviewElement> reviewList;
    ArrayList<TrailerElement> trailersList;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MovieDetailsFragment.DETAIL_URI);
        }

        if (mUri != null) {
            movieId = mUri.getPathSegments().get(1);
        }

        movieName = (TextView) rootView.findViewById(R.id.detailMovieName);
        movieReleaseDate = (TextView) rootView.findViewById(R.id.detailMovieReleaseDate);
        movieRate = (TextView) rootView.findViewById(R.id.detailMovieRate);
        movieOverview = (TextView) rootView.findViewById(R.id.detailMovieOverview);
        movieImage = (ImageView) rootView.findViewById(R.id.detailMovieImage);
        list_trailer = (ListView) rootView.findViewById(R.id.trailer_list);
        list_review = (ListView) rootView.findViewById(R.id.review_list);
        Share = (ImageView) rootView.findViewById(R.id.share_image);
        chkIos = (CheckBox) rootView.findViewById(R.id.FAVcheckBox);


        updateTrailersAndReviews();

        shareButton();

        chechbox();

        return rootView;
    }


    private void chechbox() {
        //get movie id from uri
        if (null != mUri) {


            if (movieId != null) {
                favoriteUriWithId = MovieContract.FAVMoviesEntry.buildMoviesUriWithMovieId(Long.valueOf(movieId));
            }

            Cursor favoriteCheck = getActivity().getContentResolver().query(favoriteUriWithId, null, null, null, null);
            if (favoriteCheck != null && favoriteCheck.getCount() > 0) {
                chkIos.setChecked(true);
            }

            chkIos.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //is chkIos checked?
                    if (((CheckBox) v).isChecked()) {


                        getActivity().getContentResolver().insert(favoriteUriWithId, values);

                        Toast.makeText(getActivity(),
                                "Added to favorite", Toast.LENGTH_LONG).show();
                    } else {

                        getActivity().getContentResolver().delete(favoriteUriWithId, null, null);

                        //make toast to user
                        Toast.makeText(getActivity(), "Deleted from favorite", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    private void shareButton() {


        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (share != null) {
                    ShareTrailer(share);
                } else {
                    Toast.makeText(getActivity(), "No Trailers available", Toast.LENGTH_LONG).show();
                    Log.d(LOG_TAG, "Share Action Provider is null?");
                }

            }
        });
    }

    private void inflateReviewList(Cursor data) {


        reviewList = convertCursorToReviewList(data);

        ReviewAdapter adapter = new ReviewAdapter(getActivity(), R.layout.review_item, reviewList);
        list_review.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list_review);

    }


    private void inflateTrailersList(Cursor data) {

        trailersList = convertCursorToTrailerList(data);

        TrailerAdapter adapter = new TrailerAdapter(getActivity(), R.layout.trailer_item, trailersList);
        list_trailer.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list_trailer);

        list_trailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //pass object of clicked item
                watchYoutubeVideo(trailersList.get(position).mLink);
            }
        });

        try{
            share = trailersList.get(0).mLink;
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    private void watchYoutubeVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        }
    }

    public ArrayList<TrailerElement> convertCursorToTrailerList(Cursor cursor) {
        ArrayList<TrailerElement> trailersList = new ArrayList<>();
        ContentValues cv = new ContentValues();
        while (cursor.moveToNext()) {
            DatabaseUtils.cursorRowToContentValues(cursor, cv);
            // get row indices for our cursor
            trailersList.add(new TrailerElement(
                    cv.getAsString(MovieContract.TrailersMovieEntry.COLUMN_TRI_NAME),
                    cv.getAsString(MovieContract.TrailersMovieEntry.COLUMN_TRI_LINK)
            ));
        }
        return trailersList;
    }

    public ArrayList<ReviewElement> convertCursorToReviewList(Cursor cursor) {
        ArrayList<ReviewElement> reviewsList = new ArrayList<>();
        ContentValues cv = new ContentValues();
        while (cursor.moveToNext()) {
            DatabaseUtils.cursorRowToContentValues(cursor, cv);
            // get row indices for our cursor
            reviewsList.add(new ReviewElement(
                    cv.getAsString(MovieContract.ReviewsMovieEntry.COLUMN_REV_AUTHOR),
                    cv.getAsString(MovieContract.ReviewsMovieEntry.COLUMN_REV_CONTENT)
            ));
        }
        return reviewsList;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(TRAILERS_LOADER, null, this);
        getLoaderManager().initLoader(REVIEWS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case DETAIL_LOADER:
                if (null != mUri) {
                    // Now create and return a CursorLoader that will take care of
                    // creating a Cursor for the data being displayed.
                    return new CursorLoader(
                            getActivity(),
                            mUri,
                            null,
                            null,
                            null,
                            null
                    );
                }
            case TRAILERS_LOADER:
                //build trailer uri with movie id
                if (movieId != null) {
                    trailerUriWithId = MovieContract.TrailersMovieEntry
                            .buildTrailerUriWithMovieId(Long.valueOf(movieId));
                    return new CursorLoader(
                            getActivity(),
                            trailerUriWithId,
                            null,
                            null,
                            null,
                            null
                    );
                }
            case REVIEWS_LOADER:
                //build review uri with movie id
                if (movieId != null) {
                    reviewUriWithId = MovieContract.ReviewsMovieEntry
                            .buildReviewUriWithMovieId(Long.valueOf(movieId));
                    return new CursorLoader(
                            getActivity(),
                            reviewUriWithId,
                            null,
                            null,
                            null,
                            null
                    );
                }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) {
            return;
        }

        switch (loader.getId()) {
            case DETAIL_LOADER:
                //get object from intent
                MoviesElement movieItem = convertCursorRowToMovieObject(data);

                values.put(MovieContract.COLUMN_ID, movieItem.mId);
                values.put(MovieContract.COLUMN_TITLE, movieItem.mTitle);
                values.put(MovieContract.COLUMN_RELEASE_DATE, movieItem.mDate);
                values.put(MovieContract.COLUMN_OVERVIEW, movieItem.mOverview);
                values.put(MovieContract.COLUMN_POSTER_PATH, movieItem.mPosterPath);
                values.put(MovieContract.COLUMN_VOTE_AVERAGE, movieItem.mVoteAverage);
                values.put(MovieContract.COLUMN_SORTED, movieItem.mSorted);
                //set data on views
                movieName.setText(movieItem.mTitle);
                movieReleaseDate.setText(movieItem.mDate);
                movieRate.setText(String.valueOf(movieItem.mVoteAverage) + "/10");
                movieOverview.setText(movieItem.mOverview);
                Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185" + movieItem.mPosterPath)
                        .into(movieImage);
                Log.v(LOG_TAG, "In onLoadFinished");

            case TRAILERS_LOADER:
                //inflate Trailer on Expandable list
                inflateTrailersList(data);
            case REVIEWS_LOADER:
                //inflate reviews on Expandable list
                inflateReviewList(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void ShareTrailer(String link) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + link);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public MoviesElement convertCursorRowToMovieObject(Cursor cursor) {
        ContentValues cv = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, cv);
        // get row indices for our cursor
        MoviesElement obj = new MoviesElement(
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

    void onMovieChanged() {
        // replace the uri, since the Movie has changed
        Uri uri = mUri;
        if (null != uri) {
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
            getLoaderManager().restartLoader(TRAILERS_LOADER, null, this);
            getLoaderManager().restartLoader(REVIEWS_LOADER, null, this);
        }
    }

    public void updateTrailersAndReviews() {
        //fetch trailers
        GetDataTrailer trailerTask = new GetDataTrailer(getActivity());
        trailerTask.execute(movieId);
        //fetch reviews
        GetDataReview reviewTask = new GetDataReview(getActivity());
        reviewTask.execute(movieId);
    }
}
