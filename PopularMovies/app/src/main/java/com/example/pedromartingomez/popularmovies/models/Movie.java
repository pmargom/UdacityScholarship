package com.example.pedromartingomez.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmargom on 8/2/17.
 */

public class Movie implements Parcelable {

    private static final String MDB_POSTER_PATH = "poster_path";
    private static final String MDB_ADULT = "adult";
    private static final String MDB_OVERVIEW = "overview";
    private static final String MDB_RELEASE_DATE = "release_date";
    private static final String MDB_GENRE_IDS = "genre_ids";
    private static final String MDB_ID = "id";
    private static final String MDB_ORIGINAL_TITLE = "original_title";
    private static final String MDB_ORIGINAL_LANGUAGE = "original_language";
    private static final String MDB_TITLE = "title";
    private static final String MDB_BACKDROP_PATH = "backdrop_path";
    private static final String MDB_POPULARITY = "popularity";
    private static final String MDB_VOTE_COUNT = "vote_count";
    private static final String MDB_VIDEO = "video";
    private static final String MDB_VOTE_AVERAGE = "vote_average";

    @SerializedName(MDB_POSTER_PATH)
    private String posterPath;

    @SerializedName(MDB_ADULT)
    private Boolean adult;

    @SerializedName(MDB_OVERVIEW)
    private String overview;

    @SerializedName(MDB_RELEASE_DATE)
    private String releaseDate;

    @SerializedName(MDB_GENRE_IDS)
    private List<Integer> genreIds = null;

    @SerializedName(MDB_ID)
    private Integer id;

    @SerializedName(MDB_ORIGINAL_TITLE)
    private String originalTitle;

    @SerializedName(MDB_ORIGINAL_LANGUAGE)
    private String originalLanguage;

    @SerializedName(MDB_TITLE)
    private String title;

    @SerializedName(MDB_BACKDROP_PATH)
    private String backdropPath;

    @SerializedName(MDB_POPULARITY)
    private Float popularity;

    @SerializedName(MDB_VOTE_COUNT)
    private Integer voteCount;

    @SerializedName(MDB_VIDEO)
    private Boolean video;

    @SerializedName(MDB_VOTE_AVERAGE)
    private Float voteAverage;

    public Movie() {}

    protected Movie(Parcel in) {
        posterPath = in.readString();
        adult = (Boolean)in.readValue(null);
        overview = in.readString();
        releaseDate = in.readString();
        genreIds = new ArrayList<>();
        in.readList(genreIds, null);
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readFloat();
        voteCount = in.readInt();
        video = (Boolean) in.readValue(null);
        voteAverage = in.readFloat();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeValue(adult);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeList(genreIds);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeFloat(popularity);
        dest.writeInt(voteCount);
        dest.writeValue(video);
        dest.writeFloat(voteAverage);
    }
}