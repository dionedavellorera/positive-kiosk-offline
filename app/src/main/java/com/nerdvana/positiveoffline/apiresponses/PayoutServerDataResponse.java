package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayoutServerDataResponse {

    @SerializedName("payouts")
    @Expose
    private List<Payout> payouts = null;

    public List<Payout> getPayouts() {
        return payouts;
    }

    public void setPayouts(List<Payout> payouts) {
        this.payouts = payouts;
    }


    public class Payout {

        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("cut_off_id")
        @Expose
        private Integer cutOffId;
        @SerializedName("is_cut_off")
        @Expose
        private Integer isCutOff;
        @SerializedName("is_cut_off_at")
        @Expose
        private String isCutOffAt;
        @SerializedName("is_cut_off_by")
        @Expose
        private String isCutOffBy;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
        @SerializedName("manager_username")
        @Expose
        private String managerUsername;
        @SerializedName("reason")
        @Expose
        private String reason;
        @SerializedName("series_number")
        @Expose
        private String seriesNumber;
        @SerializedName("treg")
        @Expose
        private String treg;
        @SerializedName("username")
        @Expose
        private String username;
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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Integer getBranchId() {
            return branchId;
        }

        public void setBranchId(Integer branchId) {
            this.branchId = branchId;
        }

        public Integer getCutOffId() {
            return cutOffId;
        }

        public void setCutOffId(Integer cutOffId) {
            this.cutOffId = cutOffId;
        }

        public Integer getIsCutOff() {
            return isCutOff;
        }

        public void setIsCutOff(Integer isCutOff) {
            this.isCutOff = isCutOff;
        }

        public String getIsCutOffAt() {
            return isCutOffAt;
        }

        public void setIsCutOffAt(String isCutOffAt) {
            this.isCutOffAt = isCutOffAt;
        }

        public String getIsCutOffBy() {
            return isCutOffBy;
        }

        public void setIsCutOffBy(String isCutOffBy) {
            this.isCutOffBy = isCutOffBy;
        }

        public Integer getIsSentToServer() {
            return isSentToServer;
        }

        public void setIsSentToServer(Integer isSentToServer) {
            this.isSentToServer = isSentToServer;
        }

        public Integer getMachineId() {
            return machineId;
        }

        public void setMachineId(Integer machineId) {
            this.machineId = machineId;
        }

        public String getManagerUsername() {
            return managerUsername;
        }

        public void setManagerUsername(String managerUsername) {
            this.managerUsername = managerUsername;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getSeriesNumber() {
            return seriesNumber;
        }

        public void setSeriesNumber(String seriesNumber) {
            this.seriesNumber = seriesNumber;
        }

        public String getTreg() {
            return treg;
        }

        public void setTreg(String treg) {
            this.treg = treg;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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
