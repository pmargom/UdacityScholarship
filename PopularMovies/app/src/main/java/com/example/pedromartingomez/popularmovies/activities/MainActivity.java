package com.example.pedromartingomez.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import com.example.pedromartingomez.popularmovies.R;
import com.example.pedromartingomez.popularmovies.adapters.MovieAdapter;
import com.example.pedromartingomez.popularmovies.utilities.MovieDBJsonUtils;
import com.example.pedromartingomez.popularmovies.utilities.NetworkUtils;
import com.example.pedromartingomez.popularmovies.models.Movie;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHnadler,
        LoaderCallbacks<Movie[]>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recyclerview_movie) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;
    private static final int MOVIE_LOADER_ID = 0;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private static String mSortParam = "top_rated"; // the inicial query result will use the top rated filter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
        setLoader();

        /*ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        Log.d(TAG, "onCreate: registering preference changed listener");
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);*/
        if (CheckConnection()) {
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, MainActivity.this);
        }

    }

    private void setUI() {
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    private void setLoader() {
        Log.d(TAG, "onCreate: registering preference changed listener");
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                .registerOnSharedPreferenceChangeListener(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");
            if (CheckConnection()) {
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            }
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
        mSortParam = sharedPreferences.getString(s, "");
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

    @Override
    public Loader<Movie[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie[]> (this) {

            Movie[] mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public Movie[] loadInBackground() {

                URL movieRequestUrl = NetworkUtils.buildUrl(MainActivity.this, mSortParam);

                try {
                    String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                    Movie[] jsonMoviesData = MovieDBJsonUtils.getFullMoviesDataFromJson(MainActivity.this, jsonMoviesResponse);

                    return jsonMoviesData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Movie[] data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
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

    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter.setMoviesData(data);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {

    }

    private void invalidateData() {
        mMovieAdapter.setMoviesData(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            invalidateData();
            if (CheckConnection()) {
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            }
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
