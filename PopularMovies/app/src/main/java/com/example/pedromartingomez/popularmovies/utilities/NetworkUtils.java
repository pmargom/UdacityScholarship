package com.example.pedromartingomez.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.pedromartingomez.popularmovies.R;

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
    private static String api_key = "";

    final static String QUERY_PARAM = "api_key";
    final static String MOVIE_PARAM = "movie";

    @Nullable
    public static String buildUrl(Context context, String sortBy) {
        api_key = context.getString(R.string.api_key);

        Uri builtUri = Uri.parse(API_BASE_URL)
                .buildUpon()
                .appendPath(MOVIE_PARAM)
                .appendPath(sortBy).appendQueryParameter(QUERY_PARAM, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.v(TAG, "Built URI " + url);
            return builtUri.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
