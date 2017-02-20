package com.example.pedromartingomez.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pmargom on 19/2/17.
 */

public class MovieResponse {
    @SerializedName("results")
    public List<Movie> results;
}
