package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchCreditCardResponse {
    @SerializedName("result")
    @Expose
    private List<Result> result;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Result {
        @SerializedName("core_id")
        @Expose
        private int coreId;
        @SerializedName("credit_card")
        @Expose
        private String creditCard;

        public int getCoreId() {
            return coreId;
        }

        public void setCoreId(int coreId) {
            this.coreId = coreId;
        }

        public String getCreditCard() {
            return creditCard;
        }

        public void setCreditCard(String creditCard) {
            this.creditCard = creditCard;
        }
    }
}
