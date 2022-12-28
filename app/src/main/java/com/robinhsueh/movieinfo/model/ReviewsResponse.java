package com.robinhsueh.movieinfo.model;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class ReviewsResponse {

    @SerializedName("page")
    public int page;

    @SerializedName("results")
    public ArrayList<ReviewData> review_list;

    @SerializedName("total_pages")
    public int total_pages;


    /**
     * Individual Review Data
     */
    public static class ReviewData {

        /**
         * Author name
         */
        @SerializedName("author")
        private String author;

        /**
         * Author details
         */
        @SerializedName("author_details")
        private AuthorDetail authorDetail;

        /**
         * Review content
         */
        @SerializedName("content")
        private String content;

        /**
         * Create time as string type
         */
        @SerializedName("created_at")
        private String createTimeString;

        /**
         * Update time as string type
         */
        @SerializedName("updated_at")
        private String updateTimeString;

        /**
         * Url to source website
         */
        @SerializedName("url")
        private String sourceUrl;


        public String getAuthor() {
            return author;
        }

        public AuthorDetail getAuthorDetail() {
            return authorDetail;
        }

        public String getContent() {
            return content;
        }

        public Date getCreateTime() {
            SimpleDateFormat parser = new
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.TAIWAN);
            try {
                parser.setTimeZone(TimeZone.getTimeZone("UTC"));
                return parser.parse(createTimeString);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date(Long.MIN_VALUE);
            }
        }

        public Date getUpdateTime() {
            SimpleDateFormat parser = new
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.TAIWAN);
            try {
                parser.setTimeZone(TimeZone.getTimeZone("UTC"));
                return parser.parse(updateTimeString);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date(Long.MIN_VALUE);
            }
        }

        public String getSourceUrl() {
            return sourceUrl;
        }
    }

    /**
     * Individual Review Data
     */
    public static class AuthorDetail {

        /**
         * Author name
         */
        @SerializedName("name")
        public String name;

        /**
         * Author username
         */
        @SerializedName("username")
        public String username;

        /**
         * Author avatar path
         */
        @SerializedName("avatar_path")
        public String avatarPath;

        /**
         * Author rating score
         */
        @SerializedName("rating")
        public double rating;
    }
}

