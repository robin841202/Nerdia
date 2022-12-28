package com.robinhsueh.movieinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.robinhsueh.movieinfo.model.person.PersonDetailData;
import com.robinhsueh.movieinfo.model.repository.PersonRepository;


public class PersonDetailViewModel extends ViewModel {
    private PersonRepository repository;
    private LiveData<PersonDetailData> liveData;

    /**
     * Initialize ViewModel liveData, Prevent from triggering observer twice
     */
    public void init() {
        repository = new PersonRepository();
        liveData = repository.getPersonDetailLiveData();
    }

    /**
     * Call repository to get person detail and update to liveData
     *
     * @param personId        Person Id
     * @param subRequestType Can do subRequest in the same time  ex: images
     * @param imageLanguages Can include multiple languages of image ex:zh-TW,en
     */
    public void getPersonDetail(long personId, String subRequestType, String imageLanguages) {
        repository.getPersonDetail(personId, subRequestType, imageLanguages);
    }

    /**
     * Get the liveData to observe it
     *
     * @return
     */
    public LiveData<PersonDetailData> getPersonDetailLiveData() {
        return liveData;
    }

}
