package com.example.pedromartingomez.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by pedromartingomez on 6/2/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private MovieModel[] mMoviesData;
    private final MovieAdapterOnClickHnadler mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHnadler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mMovieTextView;
        public final ImageView mPosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            this.mMovieTextView = (TextView) itemView.findViewById(R.id.tv_movie_data);
            this.mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieModel movieAtPosition = mMoviesData[adapterPosition];
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
        MovieModel movieModel = mMoviesData[position];
        movieAdapterViewHolder.mMovieTextView.setText(movieModel.getTitle());
        Picasso.with(movieAdapterViewHolder.itemView.getContext())
                .load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg")
                //.error(R.drawable.imagenotfound)
                .into(movieAdapterViewHolder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData) return 0;
        return mMoviesData.length;
    }

    public void setMoviesData(MovieModel[] moviesData) {
        this.mMoviesData = moviesData;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHnadler {
        void onClick(MovieModel movie);
    }
}


