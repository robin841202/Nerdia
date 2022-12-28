package com.robinhsueh.movieinfo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class ImagesResponse {

    @SerializedName("backdrops")
    private ArrayList<ImageData> backdrops_list;

    @SerializedName("logos")
    private ArrayList<ImageData> logos_list;

    @SerializedName("posters")
    private ArrayList<ImageData> posters_list;

    @SerializedName("profiles")
    private ArrayList<ImageData> profiles_list;

    public ImagesResponse(ArrayList<ImageData> backdrops_list, ArrayList<ImageData> logos_list, ArrayList<ImageData> posters_list, ArrayList<ImageData> profiles_list) {
        this.backdrops_list = backdrops_list;
        this.logos_list = logos_list;
        this.posters_list = posters_list;
        this.profiles_list = profiles_list;
    }

    public ArrayList<ImageData> getBackdrops_list() {
        return backdrops_list;
    }

    public ArrayList<ImageData> getLogos_list() {
        return logos_list;
    }

    public ArrayList<ImageData> getPosters_list() {
        return posters_list;
    }

    public ArrayList<ImageData> getProfiles_list() {return profiles_list;}

    /**
     * Individual Image Data
     */
    public static class ImageData {

        /**
         * Language Code according in iso 639-1 ,ex: en,zh
         */
        @SerializedName("iso_639_1")
        private String languageCode;

        /**
         * Image Aspect Ratio
         */
        @SerializedName("aspect_ratio")
        private double aspectRatio;

        /**
         * Image Width
         */
        @SerializedName("width")
        private int width;

        /**
         * Image Height
         */
        @SerializedName("height")
        private int height;

        /**
         * Image File Path
         */
        @SerializedName("file_path")
        private String filePath;

        public String getLanguageCode() {
            return languageCode;
        }

        public double getAspectRatio() {
            return aspectRatio;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public String getFilePath() {
            return filePath;
        }
    }
}

