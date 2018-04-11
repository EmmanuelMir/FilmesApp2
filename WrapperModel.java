package com.emmanuelmir.filmesapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

public class WrapperModel {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "api_key"
    })
    static class ApiKeyChave{
        @JsonProperty("api_key")
        private String api_key = "f6006d075c4c511ee2ccac2b49183665";

        public ApiKeyChave(){

        }

        public String getApi_key() {
            return api_key;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "success",
            "expires_at",
            "request_token"
    })

    /**
     * Escopo das classes gerados automaticamente no site http://www.jsonschema2pojo.org/
     */
    static class APIKeyModel {

        @JsonProperty("success")
        private boolean success;
        @JsonProperty("expires_at")
        private String expiresAt;
        @JsonProperty("request_token")
        private String requestToken;

        /**
         * No args constructor for use in serialization
         *
         */
        public APIKeyModel() {
        }

        /**
         *
         * @param expiresAt
         * @param requestToken
         * @param success
         */
        public APIKeyModel(boolean success, String expiresAt, String requestToken) {
            super();
            this.success = success;
            this.expiresAt = expiresAt;
            this.requestToken = requestToken;
        }

        @JsonProperty("success")
        public boolean isSuccess() {
            return success;
        }

        @JsonProperty("success")
        public void setSuccess(boolean success) {
            this.success = success;
        }

        public APIKeyModel withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        @JsonProperty("expires_at")
        public String getExpiresAt() {
            return expiresAt;
        }

        @JsonProperty("expires_at")
        public void setExpiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
        }

        public APIKeyModel withExpiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        @JsonProperty("request_token")
        public String getRequestToken() {
            return requestToken;
        }

        @JsonProperty("request_token")
        public void setRequestToken(String requestToken) {
            this.requestToken = requestToken;
        }

        public APIKeyModel withRequestToken(String requestToken) {
            this.requestToken = requestToken;
            return this;
        }

    }
    @JsonIgnoreProperties({"dates"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "page",
            "results",
            "total_results",
            "total_pages"
    })
    static class FilmesModel {

        @JsonProperty("page")
        private int page;
        @JsonProperty("results")
        private List<WrapperModel.FilmesModel.Result> results = null;
        @JsonProperty("total_results")
        private int totalResults;
        @JsonProperty("total_pages")
        private int totalPages;

        /**
         * No args constructor for use in serialization
         *
         */
        public FilmesModel() {
        }

        /**
         *
         * @param results
         * @param totalResults
         * @param page
         * @param totalPages
         */
        public FilmesModel(int page, List<WrapperModel.FilmesModel.Result> results, int totalResults, int totalPages) {
            super();
            this.page = page;
            this.results = results;
            this.totalResults = totalResults;
            this.totalPages = totalPages;
        }

        @JsonProperty("page")
        public int getPage() {
            return page;
        }

        @JsonProperty("page")
        public void setPage(int page) {
            this.page = page;
        }

        @JsonProperty("results")
        public List<WrapperModel.FilmesModel.Result> getResults() {
            return results;
        }

        @JsonProperty("results")
        public void setResults(List<WrapperModel.FilmesModel.Result> results) {
            this.results = results;
        }

        @JsonProperty("total_results")
        public int getTotalResults() {
            return totalResults;
        }

        @JsonProperty("total_results")
        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }

        @JsonProperty("total_pages")
        public int getTotalPages() {
            return totalPages;
        }

        @JsonProperty("total_pages")
        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }


        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonPropertyOrder({
                "poster_path",
                "adult",
                "overview",
                "release_date",
                "genre_ids",
                "id",
                "original_title",
                "original_language",
                "title",
                "backdrop_path",
                "popularity",
                "vote_count",
                "video",
                "vote_average"
        })
        @Entity(tableName = "filme")
        static class Result {

            @ColumnInfo(name = "poster_path")
            @JsonProperty("poster_path")
            private String posterPath;

            @ColumnInfo(name = "adult")
            @JsonProperty("adult")
            private boolean adult;

            @ColumnInfo(name = "overview")
            @JsonProperty("overview")
            private String overview;

            @ColumnInfo(name = "release_date")
            @JsonProperty("release_date")
            private String releaseDate;

            @ColumnInfo(name = "genre_ids")
            @JsonProperty("genre_ids")
            private List<Integer> genreIds = null;

            @JsonProperty("id")
            @PrimaryKey
            private int id;

            @ColumnInfo(name = "genre_ids")
            @JsonProperty("original_title")
            private String originalTitle;

            @ColumnInfo(name = "original_language")
            @JsonProperty("original_language")
            private String originalLanguage;

            @ColumnInfo(name = "title")
            @JsonProperty("title")
            private String title;

            @ColumnInfo(name = "backdrop_path")
            @JsonProperty("backdrop_path")
            private String backdropPath;

            @ColumnInfo(name = "popularity")
            @JsonProperty("popularity")
            private float popularity;

            @ColumnInfo(name = "vote_count")
            @JsonProperty("vote_count")
            private int voteCount;

            @ColumnInfo(name = "video")
            @JsonProperty("video")
            private boolean video;

            @ColumnInfo(name = "vote_average")
            @JsonProperty("vote_average")
            private float voteAverage;

            /**
             * No args constructor for use in serialization
             *
             */
            public Result() {
            }

            /**
             *
             * @param id
             * @param genreIds
             * @param title
             * @param releaseDate
             * @param overview
             * @param posterPath
             * @param originalTitle
             * @param voteAverage
             * @param originalLanguage
             * @param adult
             * @param backdropPath
             * @param voteCount
             * @param video
             * @param popularity
             */
            public Result(String posterPath, boolean adult, String overview, String releaseDate, List<Integer> genreIds, int id, String originalTitle, String originalLanguage, String title, String backdropPath, float popularity, int voteCount, boolean video, float voteAverage) {
                super();
                this.posterPath = posterPath;
                this.adult = adult;
                this.overview = overview;
                this.releaseDate = releaseDate;
                this.genreIds = genreIds;
                this.id = id;
                this.originalTitle = originalTitle;
                this.originalLanguage = originalLanguage;
                this.title = title;
                this.backdropPath = backdropPath;
                this.popularity = popularity;
                this.voteCount = voteCount;
                this.video = video;
                this.voteAverage = voteAverage;
            }

            @JsonProperty("poster_path")
            public String getPosterPath() {
                return posterPath;
            }

            @JsonProperty("poster_path")
            public void setPosterPath(String posterPath) {
                this.posterPath = posterPath;
            }

            @JsonProperty("adult")
            public boolean isAdult() {
                return adult;
            }

            @JsonProperty("adult")
            public void setAdult(boolean adult) {
                this.adult = adult;
            }

            @JsonProperty("overview")
            public String getOverview() {
                return overview;
            }

            @JsonProperty("overview")
            public void setOverview(String overview) {
                this.overview = overview;
            }

            @JsonProperty("release_date")
            public String getReleaseDate() {
                return releaseDate;
            }

            @JsonProperty("release_date")
            public void setReleaseDate(String releaseDate) {
                this.releaseDate = releaseDate;
            }

            @JsonProperty("genre_ids")
            public List<Integer> getGenreIds() {
                return genreIds;
            }

            @JsonProperty("genre_ids")
            public void setGenreIds(List<Integer> genreIds) {
                this.genreIds = genreIds;
            }

            @JsonProperty("id")
            public int getId() {
                return id;
            }

            @JsonProperty("id")
            public void setId(int id) {
                this.id = id;
            }

            @JsonProperty("original_title")
            public String getOriginalTitle() {
                return originalTitle;
            }

            @JsonProperty("original_title")
            public void setOriginalTitle(String originalTitle) {
                this.originalTitle = originalTitle;
            }

            @JsonProperty("original_language")
            public String getOriginalLanguage() {
                return originalLanguage;
            }

            @JsonProperty("original_language")
            public void setOriginalLanguage(String originalLanguage) {
                this.originalLanguage = originalLanguage;
            }

            @JsonProperty("title")
            public String getTitle() {
                return title;
            }

            @JsonProperty("title")
            public void setTitle(String title) {
                this.title = title;
            }

            @JsonProperty("backdrop_path")
            public String getBackdropPath() {
                return backdropPath;
            }

            @JsonProperty("backdrop_path")
            public void setBackdropPath(String backdropPath) {
                this.backdropPath = backdropPath;
            }

            @JsonProperty("popularity")
            public float getPopularity() {
                return popularity;
            }

            @JsonProperty("popularity")
            public void setPopularity(float popularity) {
                this.popularity = popularity;
            }

            @JsonProperty("vote_count")
            public int getVoteCount() {
                return voteCount;
            }

            @JsonProperty("vote_count")
            public void setVoteCount(int voteCount) {
                this.voteCount = voteCount;
            }

            @JsonProperty("video")
            public boolean isVideo() {
                return video;
            }

            @JsonProperty("video")
            public void setVideo(boolean video) {
                this.video = video;
            }

            @JsonProperty("vote_average")
            public float getVoteAverage() {
                return voteAverage;
            }

            @JsonProperty("vote_average")
            public void setVoteAverage(float voteAverage) {
                this.voteAverage = voteAverage;
            }

        }

    }

}
