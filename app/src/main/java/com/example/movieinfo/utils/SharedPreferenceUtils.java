package com.example.movieinfo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.movieinfo.model.StaticParameter;
import com.example.movieinfo.model.user.UserData;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedPreferenceUtils {

    /**
     * Get or create a sharedPreference
     *
     * @param fileName sharedPreference fileName
     * @param context  context
     * @return SharedPreference object or null
     */
    @Nullable
    public static SharedPreferences getOrCreateSharedPreference(String fileName, Context context) {
        try {
            // Get or create a crypto key
            String cryptoKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            return EncryptedSharedPreferences.create(
                    fileName,
                    cryptoKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get userData from sharedPreference
     *
     * @param sp SharedPreference object
     * @return UserData or null
     */
    @Nullable
    public static UserData getUserDataFromSharedPreference(SharedPreferences sp) {
        UserData userData = null;
        String json = sp.getString(StaticParameter.SharedPreferenceFieldKey.SP_FIELD_TMDB_USERDATA_KEY, "");
        if (!Strings.isNullOrEmpty(json)) {
            Gson gson = new Gson();
            userData = gson.fromJson(json, UserData.class);
        }
        return userData;
    }

    /**
     * Get session from sharedPreference
     *
     * @param sp SharedPreference object
     * @return Session or empty string
     */
    @Nullable
    public static String getSessionFromSharedPreference(SharedPreferences sp) {
        return sp.getString(StaticParameter.SharedPreferenceFieldKey.SP_FIELD_TMDB_SESSION_KEY, "");
    }
}
