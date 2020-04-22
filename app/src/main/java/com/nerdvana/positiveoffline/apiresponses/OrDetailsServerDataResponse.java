package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrDetailsServerDataResponse {

    @SerializedName("or_details")
    @Expose
    private List<OrDetail> orDetails = null;

    public List<OrDetail> getOrDetails() {
        return orDetails;
    }

    public void setOrDetails(List<OrDetail> orDetails) {
        this.orDetails = orDetails;
    }


    public class OrDetail {

        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("transaction_id")
        @Expose
        private Integer transactionId;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("is_percentage")
        @Expose
        private Integer isPercentage;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("business_style")
        @Expose
        private String businessStyle;
        @SerializedName("tin_number")
        @Expose
        private String tinNumber;
        @SerializedName("treg")
        @Expose
        private String treg;
        @SerializedName("created_at")
        @Expose
        private List<Object> createdAt = null;
        @SerializedName("updated_at")
        @Expose
        private List<Object> updatedAt = null;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;

        public Integer getMyId() {
            return myId;
        }

        public void setMyId(Integer myId) {
            this.myId = myId;
        }

        public Integer getBranchId() {
            return branchId;
        }

        public void setBranchId(Integer branchId) {
            this.branchId = branchId;
        }

        public Integer getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(Integer transactionId) {
            this.transactionId = transactionId;
        }

        public Integer getIsSentToServer() {
            return isSentToServer;
        }

        public void setIsSentToServer(Integer isSentToServer) {
            this.isSentToServer = isSentToServer;
        }

        public Integer getIsPercentage() {
            return isPercentage;
        }

        public void setIsPercentage(Integer isPercentage) {
            this.isPercentage = isPercentage;
        }

        public Integer getMachineId() {
            return machineId;
        }

        public void setMachineId(Integer machineId) {
            this.machineId = machineId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBusinessStyle() {
            return businessStyle;
        }

        public void setBusinessStyle(String businessStyle) {
            this.businessStyle = businessStyle;
        }

        public String getTinNumber() {
            return tinNumber;
        }

        public void setTinNumber(String tinNumber) {
            this.tinNumber = tinNumber;
        }

        public String getTreg() {
            return treg;
        }

        public void setTreg(String treg) {
            this.treg = treg;
        }

        public List<Object> getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(List<Object> createdAt) {
            this.createdAt = createdAt;
        }

        public List<Object> getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(List<Object> updatedAt) {
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
