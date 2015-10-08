package com.example.amrmasoud.popmovies.Model;

import java.io.Serializable;

/**
 * Created by amr masoud on 9/19/2015.
 */
public class MoviesElement implements Serializable {
    public long mId;
    public String mTitle;
    public String mDate;
    public String mOverview;
    public String mPosterPath;
    public String mVoteAverage;
    public String mSorted;

    public MoviesElement(long mId, String mTitle, String mDate, String mOverview, String mPosterPath, String mVoteAverage, String mSorted) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDate = mDate;
        this.mOverview = mOverview;
        this.mPosterPath = mPosterPath;
        this.mVoteAverage = mVoteAverage;
        this.mSorted = mSorted;
    }
}
