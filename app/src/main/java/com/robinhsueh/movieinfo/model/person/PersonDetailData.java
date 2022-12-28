package com.robinhsueh.movieinfo.model.person;

import com.robinhsueh.movieinfo.model.ImagesResponse;
import com.robinhsueh.movieinfo.model.movie.MovieData;
import com.robinhsueh.movieinfo.model.tvshow.TvShowData;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Person Detail Data Model, using @SerializedName to map to json key
 */
public class PersonDetailData {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("also_known_as")
    private ArrayList<String> alsoKnownAs;

    @SerializedName("biography")
    private String biography;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("deathday")
    private String deathday;

    @SerializedName("known_for_department")
    private String knownForDepartment;

    @SerializedName("place_of_birth")
    private String placeFrom;

    @SerializedName("profile_path")
    private String profilePath;

    @SerializedName("images")
    private ImagesResponse imagesResponse;

    @SerializedName("movie_credits")
    private CreditsMovieResponse creditsMovieResponse;

    @SerializedName("tv_credits")
    private CreditsTvShowResponse creditsTvShowResponse;

    public long getId() {return id;}

    public String getName() {
        return name;
    }

    public ArrayList<String> getAlsoKnownAs() {
        return alsoKnownAs;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public String getKnownForDepartment() {
        return knownForDepartment;
    }

    public String getPlaceFrom() {
        return placeFrom;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public ImagesResponse getImagesResponse() {
        return imagesResponse;
    }

    public CreditsMovieResponse getCreditsMovieResponse() {
        return creditsMovieResponse;
    }

    public CreditsTvShowResponse getCreditsTvShowResponse() {return creditsTvShowResponse;}

    /**
     * Credits Movie Response
     */
    public static class CreditsMovieResponse {

        @SerializedName("cast")
        private ArrayList<MovieData> castedMovie_list;

        @SerializedName("crew")
        private ArrayList<MovieData> crewedMovie_list;

        public CreditsMovieResponse(ArrayList<MovieData> castedMovie_list, ArrayList<MovieData> crewedMovie_list) {
            this.castedMovie_list = castedMovie_list;
            this.crewedMovie_list = crewedMovie_list;
        }

        public ArrayList<MovieData> getCastedMovie_list() {
            return castedMovie_list;
        }

        public ArrayList<MovieData> getCrewedMovie_list() {
            return crewedMovie_list;
        }


        /**
         * Get Movies List that without contains duplicated Id
         * @param items input movies list
         * @return
         */
        public ArrayList<MovieData> getMoviesWithoutDuplicatedId(ArrayList<MovieData> items){
            Map<Long, List<MovieData>> map = new HashMap<>();
            for(MovieData item : items){
                if(!map.containsKey(item.getId())){
                    map.put(item.getId(), new ArrayList<>(Collections.singletonList(item)));
                }else{
                    map.get(item.getId()).add(item);
                }
            }
            ArrayList<MovieData> result = new ArrayList<>();
            for(Map.Entry<Long,List<MovieData>> e : map.entrySet()){
                result.add(e.getValue().get(0));
            }
            return result;
        }


        /**
         * Sort Movies List by release date (descending)
         * @param list input movies list
         * @return
         */
        public ArrayList<MovieData> sortByReleaseDate(ArrayList<MovieData> list){
            Collections.sort(list, new Comparator<MovieData>() {
                DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                @Override
                public int compare(MovieData o1, MovieData o2) {
                    if (Strings.isNullOrEmpty(o1.getReleaseDate()))
                        return 1;
                    if (Strings.isNullOrEmpty(o2.getReleaseDate()))
                        return -1;
                    try {
                        Date d1 = parser.parse(o1.getReleaseDate());
                        Date d2 = parser.parse(o2.getReleaseDate());
                        return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
                        //return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
            return list;
        }

    }


    /**
     * Credits TvShow Response
     */
    public static class CreditsTvShowResponse {

        @SerializedName("cast")
        private ArrayList<TvShowData> castedTvShow_list;

        @SerializedName("crew")
        private ArrayList<TvShowData> crewedTvShow_list;

        public CreditsTvShowResponse(ArrayList<TvShowData> castedTvShow_list, ArrayList<TvShowData> crewedTvShow_list) {
            this.castedTvShow_list = castedTvShow_list;
            this.crewedTvShow_list = crewedTvShow_list;
        }

        public ArrayList<TvShowData> getCastedTvShow_list() {
            return castedTvShow_list;
        }

        public ArrayList<TvShowData> getCrewedTvShow_list() {
            return crewedTvShow_list;
        }


        /**
         * Get TvShows List that without contains duplicated Id
         * @param items input tvShows list
         * @return
         */
        public ArrayList<TvShowData> getTvShowsWithoutDuplicatedId(ArrayList<TvShowData> items){
            Map<Long, List<TvShowData>> map = new HashMap<>();
            for(TvShowData item : items){
                if(!map.containsKey(item.getId())){
                    map.put(item.getId(), new ArrayList<>(Collections.singletonList(item)));
                }else{
                    map.get(item.getId()).add(item);
                }
            }
            ArrayList<TvShowData> result = new ArrayList<>();
            for(Map.Entry<Long,List<TvShowData>> e : map.entrySet()){
                result.add(e.getValue().get(0));
            }
            return result;
        }


        /**
         * Sort TvShows List by release date (descending)
         * @param list input tvShows list
         * @return
         */
        public ArrayList<TvShowData> sortByReleaseDate(ArrayList<TvShowData> list){
            Collections.sort(list, new Comparator<TvShowData>() {
                DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                @Override
                public int compare(TvShowData o1, TvShowData o2) {
                    if (Strings.isNullOrEmpty(o1.getOnAirDate()))
                        return 1;
                    if (Strings.isNullOrEmpty(o2.getOnAirDate()))
                        return -1;
                    try {
                        Date d1 = parser.parse(o1.getOnAirDate());
                        Date d2 = parser.parse(o2.getOnAirDate());
                        return (d1.getTime() > d2.getTime() ? -1 : 1);     //descending
                        //return (d1.getTime() > d2.getTime() ? 1 : -1);     //ascending
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
            return list;
        }

    }


}
