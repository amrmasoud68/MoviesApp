package com.example.amrmasoud.popmovies.Controller;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.amrmasoud.popmovies.Data.MovieContract;
import com.example.amrmasoud.popmovies.FetchData.GetDataMovies;
import com.example.amrmasoud.popmovies.Adapter.MovieAdapter;
import com.example.amrmasoud.popmovies.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class Movies_Grid_Fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MovieAdapter mMovieAdapter;
   private GridView gridView;
    private int mPosition = GridView.INVALID_POSITION;
    private static final int MOVIES_LOADER = 0;
    private static final String SELECTED_KEY = "selected_position";

    // These indices are tied to MOVIES_COLUMNS.  If MOVIES_COLUMNS changes, these
    // must change.
    static final int COL_MOV_ID = 1;
    static final int COL_MOV_ORIGINAL_TITLE = 2;
    static final int COL_MOV_RELEASE_DATE = 3;
    static final int COL_MOV_OVERVIEW = 4;
    static final int COL_MOV_POSTER_PATH = 5;
    static final int COL_MOV_VOTE_AVERAGE = 6;
    static final int COL_MOV_SORT_BY = 7;



    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri idUri);
    }

    // These indices are tied to MOVIES_COLUMNS.  If MOVIES_COLUMNS changes, these
    // must change.


    public Movies_Grid_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        // Initialize our Adapter.
        mMovieAdapter = new MovieAdapter(getActivity(), null, 0);

        // Get a reference to the GridView, and attach this adapter to it.
        gridView = (GridView)rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mMovieAdapter);

        // On click on item listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String sortBy = getPreferredSortBy(getActivity());
                    //check about sort by to get correct uri
                    if (sortBy.equals("popularity.desc")) {
                        ((Callback) getActivity())
                                .onItemSelected(MovieContract.POPMoviesEntry.buildMoviesUriWithMovieId(
                                        cursor.getLong(COL_MOV_ID)
                                ));
                    } else if (sortBy.equals("vote_average.desc")){
                        ((Callback) getActivity())
                                .onItemSelected(MovieContract.TOPMoviesEntry.buildMoviesUriWithMovieId(
                                        cursor.getLong(COL_MOV_ID)
                                ));
                    } else {
                        ((Callback) getActivity())
                                .onItemSelected(MovieContract.FAVMoviesEntry.buildMoviesUriWithID(
                                        cursor.getLong(COL_MOV_ID)
                                ));
                    }
                }
                mPosition = position;
            }
        });

        //get position from bundle
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        onSortByChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to GridView.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(),SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }


    @Override

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortBy = getPreferredSortBy(getActivity());

        // build uri to get cursor with Sort by
        Uri moviesUriBySort;
        if (sortBy.equals("popularity.desc")) {
            moviesUriBySort = MovieContract.POPMoviesEntry.CONTENT_URI;
        } else if(sortBy.equals("vote_average.desc")){
            moviesUriBySort = MovieContract.TOPMoviesEntry.CONTENT_URI;
        } else  {
            moviesUriBySort = MovieContract.FAVMoviesEntry.CONTENT_URI;
        }

        return new CursorLoader(getActivity(),
                moviesUriBySort,
                null,
                null,
                null,
                null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);

        if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            gridView.smoothScrollToPosition(mPosition);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    void onSortByChanged( ) {
        updateMovies();
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    public void updateMovies() {
        GetDataMovies movieTask = new GetDataMovies(getActivity());
        String sortBy = getPreferredSortBy(getActivity());
        movieTask.execute(sortBy);
    }

    public static String getPreferredSortBy(FragmentActivity context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));
    }
}
