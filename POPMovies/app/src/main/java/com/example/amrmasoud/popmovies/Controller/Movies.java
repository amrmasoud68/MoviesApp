package com.example.amrmasoud.popmovies.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import com.example.amrmasoud.popmovies.R;

public class Movies extends ActionBarActivity implements Movies_Grid_Fragment.Callback {

    private String mSortBy;
    private boolean mTwoPane;
    private static String DETAIL_FRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        if (findViewById(R.id.fragment_details) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_details, new MovieDetailsFragment(), DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
        mSortBy =  getPreferredSortBy(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortBy = getPreferredSortBy(this);
        // update sort by in our second pane using the fragment manager
        if (sortBy != null && !sortBy.equals(mSortBy)) {
            Movies_Grid_Fragment ff = (Movies_Grid_Fragment)getSupportFragmentManager().findFragmentById(R.id.fragment_gridview);
            if ( null != ff ) {
                ff.onSortByChanged();
            }
            MovieDetailsFragment df = (MovieDetailsFragment)getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);
            if ( null != df ) {
                df.onMovieChanged();
            }
            mSortBy = sortBy;
        }

    }

    @Override
    public void onItemSelected(Uri idUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailsFragment.DETAIL_URI, idUri);

            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetails.class)
                    .setData(idUri);
            startActivity(intent);
        }
    }

    public static String getPreferredSortBy(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));
    }
}
