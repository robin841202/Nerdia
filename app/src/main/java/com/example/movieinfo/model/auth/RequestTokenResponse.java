package com.example.movieinfo.model.auth;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class RequestTokenResponse {
    @SerializedName("success")
    private boolean isSuccess;
    @SerializedName("expires_at")
    private String expiredDateString;

    @SerializedName("request_token")
    private String token;

    public RequestTokenResponse(boolean isSuccess, String expiredDateString, String token) {
        this.isSuccess = isSuccess;
        this.expiredDateString = expiredDateString;
        this.token = token;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Date getExpiredDate() {
        SimpleDateFormat formatter = new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date utcDate = formatter.parse(expiredDateString);
            return utcDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(Long.MIN_VALUE);
        }
    }

    public String getToken() {
        return token;
    }
}
