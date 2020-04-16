package com.mykab.rider.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateRecipientResponseJson {

        @SerializedName("error")
        @Expose
        private Boolean error;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private CreateRecipientResponse data;

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public CreateRecipientResponse getData() {
            return data;
        }

        public void setData(CreateRecipientResponse data) {
            this.data = data;
        }

}
