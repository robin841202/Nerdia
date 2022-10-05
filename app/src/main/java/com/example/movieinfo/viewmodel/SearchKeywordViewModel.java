package com.example.movieinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchKeywordViewModel extends ViewModel {
    private final MutableLiveData<String> keyWord = new MutableLiveData<>();

    public void setKeyWord(String newKeyWord) {
        keyWord.setValue(newKeyWord);
    }
    public LiveData<String> getKeyWord() {
        return keyWord;
    }
}
