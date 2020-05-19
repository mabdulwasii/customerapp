package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDestinationRequestJson {
    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;

    @SerializedName("end_latitude")
    @Expose
    private String endLatitude;

    @SerializedName("end_longitude")
    @Expose
    private String endLongitude;

    @SerializedName("alamat_tujuan")
    @Expose
    private String destinationText;


    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getDestinationText() {
        return destinationText;
    }

    public void setDestinationText(String destinationText) {
        this.destinationText = destinationText;
    }

  /*  public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
*/
}
