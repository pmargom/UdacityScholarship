package com.example.pedromartingomez.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.example.pedromartingomez.popularmovies.R;
import com.example.pedromartingomez.popularmovies.adapters.MovieAdapter;
import com.example.pedromartingomez.popularmovies.models.MovieResponse;
import com.example.pedromartingomez.popularmovies.utilities.ApiRequest;
import com.example.pedromartingomez.popularmovies.utilities.NetworkUtils;
import com.example.pedromartingomez.popularmovies.models.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHnadler,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recyclerview_movie)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private static String mSortParam = "top_rated"; // the initial query result will use the top rated filter
    private ApiRequest mApiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();

        mApiRequest = ApiRequest.getInstance(this);

        loadData();
    }

    private void setUI() {
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        Log.d(TAG, "onCreate: registering preference changed listener");
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                .registerOnSharedPreferenceChangeListener(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            PREFERENCES_HAVE_BEEN_UPDATED = false;
            Log.d(TAG, "onStart: preferences were updated");
            invalidateData();
            loadData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mApiRequest.getRequestQueue() != null) {
            mApiRequest.getRequestQueue().cancelAll(TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
        mSortParam = sharedPreferences.getString(s, "");
        invalidateData();
        loadData();
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {  
        Intent startMovieDetailActivity = new Intent(this, MovieDetailActivity.class);
        startMovieDetailActivity.putExtra("MOVIE", movie);
        startActivity(startMovieDetailActivity);
    }

    private void loadData() {

        if (!CheckConnection()) {
            return;
        }

        mApiRequest.addToRequestQueue(buildRequest());
    }

    private StringRequest buildRequest() {

        mLoadingIndicator.setVisibility(View.VISIBLE);

        String url = NetworkUtils.buildUrl(MainActivity.this, mSortParam);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response from Volley: " + response);

                try (Reader reader = new StringReader(response)){
                    Gson gson = new GsonBuilder().create();

                    MovieResponse movieResponse = gson.fromJson(reader, MovieResponse.class);
                    List<Movie> data = movieResponse.results;
                    mMovieAdapter.setMoviesData(data);
                    if (null == data) {
                        showErrorMessage();
                    } else {
                        showMovieDataView();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "Error");
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        });

        stringRequest.setTag(TAG);
        return stringRequest;
    }

    public boolean CheckConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean testConnection = netInfo != null && netInfo.isConnected();
        if (!testConnection) {
            Snackbar.make(this.mRecyclerView, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
        }
        return testConnection;
    }

    private void invalidateData() {
        mMovieAdapter.setMoviesData(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)   {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            invalidateData();
            loadData();
            return true;
        }

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
