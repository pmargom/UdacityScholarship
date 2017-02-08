package com.example.pedromartingomez.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedromartingomez.popularmovies.utilities.MovieDBJsonUtils;
import com.example.pedromartingomez.popularmovies.utilities.NetworkUtils;
import com.example.pedromartingomez.popularmovies.models.Movie;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHnadler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //mRecyclerView.setLayoutManager(layoutManager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the weather data. */
        loadMovieData();
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

    private void loadMovieData() {
        showMovieDataView();
        new FetchMovieTask().execute();
    }

    @Override
    public void onClick(Movie movie) {
        // TODO: Load DetailActivity
        Toast.makeText(this, movie.getTitle(), Toast.LENGTH_LONG).show();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            //if (params.length == 0) return null;

            //String sortValue = params[0];
            // TODO: Prepare NetworkUtils
            URL movieRequestUrl = NetworkUtils.buildUrl("", "");

            try {
                // TODO: Prepare MovieDbJsonUtils
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                Movie[] jsonMoviesData = MovieDBJsonUtils
                        .getFullMoviesDataFromJson(MainActivity.this, jsonMoviesResponse);

                return jsonMoviesData;

                /*Movie[] movies = new Movie[15];
                for (int i = 0; i < 15; i++) {
                    Movie movie = new Movie();
                    movie.setTitle("Lo que el viento se llevó");
                    movies[i] = movie;
                }
                return movies;
                */

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                mMovieAdapter.setMoviesData(movieData);
            } else {
                showErrorMessage();
            }
        }
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
            mMovieAdapter.setMoviesData(null);
            loadMovieData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
