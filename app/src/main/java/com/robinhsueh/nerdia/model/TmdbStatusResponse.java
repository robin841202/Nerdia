package com.robinhsueh.nerdia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response Data Model, using @SerializedName to map to json key
 */
public class TmdbStatusResponse {

    /**
     * Status code, 1: successful, >1: unsuccessful, for more details: https://www.themoviedb.org/documentation/api/status-codes
     */
    @SerializedName("status_code")
    private final int statusCode;

    /**
     * Status message
     */
    @SerializedName("status_message")
    private final String statusMessage;

    public TmdbStatusResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
