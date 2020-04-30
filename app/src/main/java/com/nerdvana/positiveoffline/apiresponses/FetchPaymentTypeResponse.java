package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchPaymentTypeResponse {
    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class MobilePayment {

        @SerializedName("payment_id")
        @Expose
        private int paymentId;
        @SerializedName("mobile_payment_id")
        @Expose
        private int mobilePaymentId;
        @SerializedName("mobile_payment")
        @Expose
        private String mobilePayment;
        @SerializedName("image_file")
        @Expose
        private String imageFile;

        public String getImageFile() {
            return imageFile;
        }

        public void setImageFile(String imageFile) {
            this.imageFile = imageFile;
        }

        public int getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(int paymentId) {
            this.paymentId = paymentId;
        }

        public int getMobilePaymentId() {
            return mobilePaymentId;
        }

        public void setMobilePaymentId(int mobilePaymentId) {
            this.mobilePaymentId = mobilePaymentId;
        }

        public String getMobilePayment() {
            return mobilePayment;
        }

        public void setMobilePayment(String mobilePayment) {
            this.mobilePayment = mobilePayment;
        }
    }

    public class AccountReceivable {

        @SerializedName("payment_id")
        @Expose
        private int paymentId;
        @SerializedName("ar_payment_id")
        @Expose
        private int arPaymentId;
        @SerializedName("account_receivable_payment")
        @Expose
        private String accountReceivablePayment;
        @SerializedName("image_file")
        @Expose
        private String imageFile;

        public int getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(int paymentId) {
            this.paymentId = paymentId;
        }

        public int getArPaymentId() {
            return arPaymentId;
        }

        public void setArPaymentId(int arPaymentId) {
            this.arPaymentId = arPaymentId;
        }

        public String getAccountReceivablePayment() {
            return accountReceivablePayment;
        }

        public void setAccountReceivablePayment(String accountReceivablePayment) {
            this.accountReceivablePayment = accountReceivablePayment;
        }

        public String getImageFile() {
            return imageFile;
        }

        public void setImageFile(String imageFile) {
            this.imageFile = imageFile;
        }
    }

    public class OnlinePayment {

        @SerializedName("payment_id")
        @Expose
        private int paymentId;
        @SerializedName("online_payment_id")
        @Expose
        private int onlinePaymentId;
        @SerializedName("online_payment")
        @Expose
        private String onlinePayment;
        @SerializedName("image_file")
        @Expose
        private String imageFile;

        public int getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(int paymentId) {
            this.paymentId = paymentId;
        }

        public int getOnlinePaymentId() {
            return onlinePaymentId;
        }

        public void setOnlinePaymentId(int onlinePaymentId) {
            this.onlinePaymentId = onlinePaymentId;
        }

        public String getOnlinePayment() {
            return onlinePayment;
        }

        public void setOnlinePayment(String onlinePayment) {
            this.onlinePayment = onlinePayment;
        }

        public String getImageFile() {
            return imageFile;
        }

        public void setImageFile(String imageFile) {
            this.imageFile = imageFile;
        }
    }
    public static class Result {
        @SerializedName("online_payment")
        @Expose
        private List<OnlinePayment> onlinePaymentList;
        @SerializedName("account_receivable")
        @Expose
        private List<AccountReceivable> accountReceivableList;
        @SerializedName("mobile_payment")
        @Expose
        private List<MobilePayment> mobilePaymentList;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("core_id")
        @Expose
        private String coreId;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("created_by")
        @Expose
        private Integer createdBy;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("image_file")
        @Expose
        private String image;

        public List<OnlinePayment> getOnlinePaymentList() {
            return onlinePaymentList;
        }

        public void setOnlinePaymentList(List<OnlinePayment> onlinePaymentList) {
            this.onlinePaymentList = onlinePaymentList;
        }

        public List<AccountReceivable> getAccountReceivableList() {
            return accountReceivableList;
        }

        public void setAccountReceivableList(List<AccountReceivable> accountReceivableList) {
            this.accountReceivableList = accountReceivableList;
        }

        public List<MobilePayment> getMobilePaymentList() {
            return mobilePaymentList;
        }

        public void setMobilePaymentList(List<MobilePayment> mobilePaymentList) {
            this.mobilePaymentList = mobilePaymentList;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCoreId() {
            return coreId;
        }

        public void setCoreId(String coreId) {
            this.coreId = coreId;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

    }
}

