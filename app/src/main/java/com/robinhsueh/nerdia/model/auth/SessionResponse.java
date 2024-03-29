package com.robinhsueh.nerdia.model.auth;

import com.google.gson.annotations.SerializedName;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class SessionResponse {
    @SerializedName("success")
    private boolean isSuccess;
    @SerializedName("session_id")
    private String session;


    public SessionResponse(boolean isSuccess, String session) {
        this.isSuccess = isSuccess;
        this.session = session;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getSession() {
        return session;
    }
}
