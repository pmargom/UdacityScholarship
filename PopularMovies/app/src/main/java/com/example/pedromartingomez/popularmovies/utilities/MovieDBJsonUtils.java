package com.example.pedromartingomez.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.pedromartingomez.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by pmargom on 8/2/17.
 */

public class MovieDBJsonUtils {

    private static final String MDB_LIST = "results";
    private static final String MDB_ORIGINAL_TITLE = "original_title";
    private static final String MDB_DESCRIPTION = "poster_path";
    private static final String MDB_MESSAGE_CODE = "status_code";

    /**
     * This method parses JSON from a web response and returns an array of Movies.
     * <p/>
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullMoviesDataFromJson function, leveraging the data we have stored in the JSON.
     *
     * @param moviesJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Movie[] getFullMoviesDataFromJson(Context context, String moviesJsonStr) throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (moviesJson.has(MDB_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(MDB_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = moviesJson.getJSONArray(MDB_LIST);

        Movie[] parsedMovies = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            /* Get the JSON object representing the movie */
            JSONObject movieJSONObject = movieArray.getJSONObject(i);

            Movie movie = buildMovie(movieJSONObject);
            parsedMovies[i] = movie;

        }

        return parsedMovies;
    }

    private static Movie buildMovie(JSONObject jsonObject) throws JSONException {
        Movie movie = new Movie();
        movie.setTitle(jsonObject.getString(MDB_ORIGINAL_TITLE));
        movie.setPosterPath(jsonObject.getString(MDB_DESCRIPTION));

        return movie;
    }

}
