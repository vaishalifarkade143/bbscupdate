
package com.bbsc.Model;

import com.google.gson.annotations.SerializedName;

public class GlobleTime {
    @SerializedName("datetime")
    private String datetime;

    public String getDatetime() {

        return datetime;
    }

    public void setDatetime(String datetime) {

        this.datetime = datetime;
    }
}