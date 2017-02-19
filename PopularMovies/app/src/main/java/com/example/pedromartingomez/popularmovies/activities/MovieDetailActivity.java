package com.example.pedromartingomez.popularmovies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pedromartingomez.popularmovies.R;
import com.example.pedromartingomez.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverViewTextView;
    private TextView mOriginalTitleTextView;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUI();
        loadDataPassedInIntent();
        bindUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUI() {
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mVoteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);
        mOverViewTextView = (TextView) findViewById(R.id.tv_overview);
        mOriginalTitleTextView = (TextView) findViewById(R.id.tv_original_title);
    }

    private void loadDataPassedInIntent() {
        Intent intent = getIntent();

        mMovie = intent.getParcelableExtra("MOVIE");
    }

    private void bindUI() {

        Picasso.with(this)
                .load(String.format("%s%s", "http://image.tmdb.org/t/p/w185", mMovie.getPosterPath()))
                .placeholder(R.drawable.img_default)
                .error(R.drawable.img_default)
                .into(mPosterImageView);

        mTitleTextView.setText(mMovie.getTitle());
        mReleaseDateTextView.setText(mMovie.getReleaseDate());
        mVoteAverageTextView.setText(String.valueOf(mMovie.getVoteAverage()));
        mOverViewTextView.setText(mMovie.getOverview());
        mOriginalTitleTextView.setText(mMovie.getOriginalTitle());

    }
}
