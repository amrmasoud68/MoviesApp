package com.example.amrmasoud.popmovies.FetchData;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.amrmasoud.popmovies.Data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by amr masoud on 9/27/2015.
 */
public class GetDataReview extends AsyncTask<String,Void,Void> {
    private final Context mContext;

    public GetDataReview(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {

            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/"+params[0]+"/reviews?";
            final String API_PARAM = "api_key";
            final String API_KEY = "3b78fe5b6acbdc3151cfd38432766908";

            //Url of json file no need to uri builder
            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.d("Reviews link", builtUri.toString());
            // Create the request to url, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();

        } catch (IOException e) {
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            e.printStackTrace();
            Log.i("Error",e.getMessage());
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                    Log.i("Error",e.getMessage());
                }
            }
        }

        try
        {
            GetReviewsDataFromJson(movieJsonStr, params[0]);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Error", e.getMessage());
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    private void GetReviewsDataFromJson(String movieJsonStr, String movieId) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String Array_RESULT = "results";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(Array_RESULT);

        // Insert the new trailers information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

        for(int i = 0; i < movieArray.length(); i++) {
            // Get the JSON object representing the time
            JSONObject movieObject = movieArray.getJSONObject(i);

            //get data from Object and append to content Values Vector
            ContentValues moviesValues = new ContentValues();

            moviesValues.put(MovieContract.COLUMN_ID,movieId);
            moviesValues.put(MovieContract.ReviewsMovieEntry.COLUMN_REV_AUTHOR, movieObject.getString(REVIEW_AUTHOR));
            moviesValues.put(MovieContract.ReviewsMovieEntry.COLUMN_REV_CONTENT, movieObject.getString(REVIEW_CONTENT));

            cVVector.add(moviesValues);
        }

        // build uri of Reviews and review with id
        Uri reviewUri = MovieContract.ReviewsMovieEntry.CONTENT_URI;
        Uri reviewWithIdUri = MovieContract.ReviewsMovieEntry.buildReviewUriWithMovieId(Long.valueOf(movieId));

        int deleted = 0;
        //delete data from database
        deleted = mContext.getContentResolver().delete(reviewWithIdUri,null,null);
        Log.d("Reviews Row Deleted ",String.valueOf(deleted));

        int inserted = 0;
        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(reviewUri, cvArray);
        }
        Log.d("Review Row Inserted ",String.valueOf(inserted));

    }

}
