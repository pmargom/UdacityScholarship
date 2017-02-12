package com.example.pedromartingomez.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.pedromartingomez.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmargom on 8/2/17.
 */

public class MovieDBJsonUtils {

    private static final String MDB_MESSAGE_CODE = "status_code";
    private static final String MDB_LIST = "results";

    private static final String MDB_POSTER_PATH = "poster_path";
    private static final String MDB_ADULT = "adult";
    private static final String MDB_OVERVIEW = "overview";
    private static final String MDB_RELEASE_DATE = "release_date";
    private static final String MDB_GENRE_IDS = "genre_ids";
    private static final String MDB_ID = "id";
    private static final String MDB_ORIGINAL_TITLE = "original_title";
    private static final String MDB_TITLE = "title";
    private static final String MDB_BACKDROP_PATH = "backdrop_path";
    private static final String MDB_POPULARITY = "popularity";
    private static final String MDB_VOTE_COUNT = "vote_count";
    private static final String MDB_VIDEO = "video";
    private static final String MDB_VOTE_AVERAGE = "vote_average";




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

        String posterPath = jsonObject.getString(MDB_POSTER_PATH);
        boolean adult = jsonObject.getBoolean(MDB_ADULT);

        String overview = jsonObject.getString(MDB_OVERVIEW);
        String releaseDate = jsonObject.getString(MDB_RELEASE_DATE);
        JSONArray arrayOfGenreIds = jsonObject.getJSONArray(MDB_GENRE_IDS);
        List<Integer> genreIds = new ArrayList<>();
        for (int i = 0; i < arrayOfGenreIds.length(); i++) {
            genreIds.add((Integer) arrayOfGenreIds.get(i));
        }

        Integer id = jsonObject.getInt(MDB_ID);
        String originalTitle = jsonObject.getString(MDB_ORIGINAL_TITLE);
        String title = jsonObject.getString(MDB_TITLE);
        String backdropPath = jsonObject.getString(MDB_BACKDROP_PATH);
        Double popularity = jsonObject.getDouble(MDB_POPULARITY);
        Integer voteCount = jsonObject.getInt(MDB_VOTE_COUNT);
        boolean video = jsonObject.getBoolean(MDB_VIDEO);
        Double voteAverage = jsonObject.getDouble(MDB_VOTE_AVERAGE);

        Movie movie = new Movie();

        movie.setPosterPath(posterPath);
        movie.setAdult(adult);
        movie.setOverview(overview);
        movie.setReleaseDate(releaseDate);
        movie.setGenreIds(genreIds);
        movie.setId(id);
        movie.setOriginalTitle(originalTitle);
        movie.setTitle(title);
        movie.setBackdropPath(backdropPath);
        movie.setPopularity(popularity.floatValue());
        movie.setVoteCount(voteCount);
        movie.setVideo(video);
        movie.setVoteAverage(voteAverage.floatValue());

        return movie;
    }

}
