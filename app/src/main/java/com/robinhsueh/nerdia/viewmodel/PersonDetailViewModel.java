package com.robinhsueh.nerdia.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.robinhsueh.nerdia.model.person.PersonDetailData;
import com.robinhsueh.nerdia.model.repository.PersonRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PersonDetailViewModel extends ViewModel {
    private final String LOG_TAG = "PersonDetailViewModel";
    private PersonRepository repository;

    // PersonDetail LiveData
    private MutableLiveData<PersonDetailData> personDetailLiveData;

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        repository = new PersonRepository();
        personDetailLiveData = new MutableLiveData<>();
    }

    /**
     * Call repository to get person detail and update to liveData
     *
     * @param personId        Person Id
     * @param subRequestType Can do subRequest in the same time  ex: images
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getPersonDetail(long personId, String subRequestType, String imageLanguages) {
        Call<PersonDetailData> response = repository.getPersonDetail(personId, subRequestType, imageLanguages);
        response.enqueue(new Callback<PersonDetailData>() {
            @Override
            public void onResponse(@NonNull Call<PersonDetailData> call, @NonNull Response<PersonDetailData> response) {
                if (response.isSuccessful()) { // Request successfully
                    if (response.body() != null) { // Data exists
                        // post result data to liveData
                        personDetailLiveData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PersonDetailData> call, @NonNull Throwable t) {
                // post null to liveData
                personDetailLiveData.postValue(null);
                Log.d(LOG_TAG, String.format("data fetch failed: getPersonDetail,\n %s ", t.getMessage()));
            }
        });
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<PersonDetailData> getPersonDetailLiveData() {
        return personDetailLiveData;
    }

}
