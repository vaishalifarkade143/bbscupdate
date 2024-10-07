package com.bbsc.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class userRoll {

    @SerializedName("update")
    @Expose
    private int update;

    @SerializedName("message")
    @Expose
    private String message;

    public int getUpdate() {
        return update;
    }

    public void setUpdate(int update) {
        this.update = update;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
