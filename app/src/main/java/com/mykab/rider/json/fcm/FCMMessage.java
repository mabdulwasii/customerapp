package com.mykab.rider.json.fcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class FCMMessage {

    @Expose
    @SerializedName("to")
    private String to;

    @Expose
    @SerializedName("content_available")
    private Boolean contentAvailable = true;

    @Expose
    @SerializedName("data")
    private Object data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getContentAvailable() {
        return contentAvailable;
    }

    public void setContentAvailable(Boolean contentAvailable) {
        this.contentAvailable = contentAvailable;
    }
}
