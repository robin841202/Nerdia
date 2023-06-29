package com.robinhsueh.nerdia.model.repository;



import com.robinhsueh.nerdia.BuildConfig;
import com.robinhsueh.nerdia.model.StaticParameter;
import com.robinhsueh.nerdia.model.person.PeopleResponse;
import com.robinhsueh.nerdia.model.person.PersonDetailData;
import com.robinhsueh.nerdia.model.service.IPersonService;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonRepository {
    private final IPersonService service;
    private final String apiKey = BuildConfig.TMDB_API_KEY;
    private final String language;
    private final String region;

    public PersonRepository() {
        this.language = Locale.TRADITIONAL_CHINESE.toLanguageTag();
        this.region = Locale.TAIWAN.getCountry();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticParameter.TmdbApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IPersonService.class);
    }

    // region MVVM architecture using LiveData

    /**
     * Get Person Detail By Movie Id (using LiveData)
     *
     * @param movieId        Movie Id
     * @param subRequestType Can do subRequest in the same time  ex: images
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public Call<PersonDetailData> getPersonDetail(long movieId, String subRequestType, String imageLanguages) {
        return service.getPersonDetail(movieId, apiKey, language, subRequestType, imageLanguages);
    }

    /**
     * Search People By Keyword (using LiveData)
     *
     * @param keyWord keyword for searching
     * @param page    target page
     */
    public Call<PeopleResponse> searchPeople(String keyWord, int page) {
        return service.searchPeople(apiKey, keyWord, page, language, region);
    }
    // endregion
}
