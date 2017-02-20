package com.example.pedromartingomez.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.example.pedromartingomez.popularmovies.R;
import com.example.pedromartingomez.popularmovies.models.Movie;
import com.example.pedromartingomez.popularmovies.utilities.ApiRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pedromartingomez on 6/2/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Movie> mMoviesData;
    private final MovieAdapterOnClickHnadler mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHnadler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_poster)
        NetworkImageView mPosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movieAtPosition = mMoviesData.get(adapterPosition);
            mClickHandler.onClick(movieAtPosition);
        }

    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie movie = mMoviesData.get(position);
        movieAdapterViewHolder.mPosterImageView
                .setImageUrl(String.format("%s%s", "http://image.tmdb.org/t/p/w185", movie.getPosterPath())
                        , ApiRequest.getInstance(movieAdapterViewHolder.itemView.getContext()).getImageLoader());

    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.size();
    }

    public void setMoviesData(List<Movie> moviesData) {
        this.mMoviesData = moviesData;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHnadler {
        void onClick(Movie movie);
    }
}


