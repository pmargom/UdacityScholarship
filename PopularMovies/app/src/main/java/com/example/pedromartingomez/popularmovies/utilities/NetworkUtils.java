package com.example.pedromartingomez.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by pmargom on 8/2/17.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = "";

    final static String QUERY_PARAM = "api_key";
    final static String DISCOVER_PARAM = "discover";
    final static String MOVIE_PARAM = "movie";
    final static String SORT_PARAM = "sort_by";

    /**
     * Builds the URL used to talk to the api server using a sorting (optional). This sorting is based
     * on the query capabilities of the api provider that we are using.
     *
     * @param sortByField The field that will be sorted for.
     * @param sortByDirection The direction that will be sorted for.
     * @return The URL to use to query the api server.
     */
    public static URL buildUrl(String sortByField, String sortByDirection) {
        Uri.Builder builder = Uri.parse(String.format("%s/%s/%s",
                API_BASE_URL, DISCOVER_PARAM, MOVIE_PARAM)).buildUpon();

        builder.appendQueryParameter(QUERY_PARAM, API_KEY);

        if (null != sortByField && !sortByField.equals("")) {
            builder.appendQueryParameter(SORT_PARAM, String.format("%s%s", sortByField, sortByDirection));
        }
        Uri builtUri = builder.build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
