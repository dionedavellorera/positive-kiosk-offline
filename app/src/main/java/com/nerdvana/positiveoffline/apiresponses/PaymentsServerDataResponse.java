package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentsServerDataResponse {

    @SerializedName("payments")
    @Expose
    private List<Payment> payments = null;

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public class Payment {
        @SerializedName("is_redeemed")
        @Expose
        private Integer isRedeemed;
        @SerializedName("is_from_other_shift")
        @Expose
        private Integer isFromOtherShift;
        @SerializedName("is_redeemed_by")
        @Expose
        private String isRedeemedBy;
        @SerializedName("is_redeemed_at")
        @Expose
        private String isRedeemedAt;
        @SerializedName("is_redeemed_for")
        @Expose
        private String isRedeemedFor;
        @SerializedName("link_payment_id")
        @Expose
        private String linkPaymentId;
        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("core_id")
        @Expose
        private Integer coreId;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("transaction_id")
        @Expose
        private Integer transactionId;
        @SerializedName("cut_off_id")
        @Expose
        private Integer cutOffId;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("is_void")
        @Expose
        private Integer isVoid;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other_data")
        @Expose
        private String otherData;
        @SerializedName("amount")
        @Expose
        private Double amount;
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

        public Integer getIsRedeemed() {
            return isRedeemed;
        }

        public void setIsRedeemed(Integer isRedeemed) {
            this.isRedeemed = isRedeemed;
        }

        public Integer getIsFromOtherShift() {
            return isFromOtherShift;
        }

        public void setIsFromOtherShift(Integer isFromOtherShift) {
            this.isFromOtherShift = isFromOtherShift;
        }

        public String getIsRedeemedBy() {
            return isRedeemedBy;
        }

        public void setIsRedeemedBy(String isRedeemedBy) {
            this.isRedeemedBy = isRedeemedBy;
        }

        public String getIsRedeemedAt() {
            return isRedeemedAt;
        }

        public void setIsRedeemedAt(String isRedeemedAt) {
            this.isRedeemedAt = isRedeemedAt;
        }

        public String getIsRedeemedFor() {
            return isRedeemedFor;
        }

        public void setIsRedeemedFor(String isRedeemedFor) {
            this.isRedeemedFor = isRedeemedFor;
        }

        public String getLinkPaymentId() {
            return linkPaymentId;
        }

        public void setLinkPaymentId(String linkPaymentId) {
            this.linkPaymentId = linkPaymentId;
        }

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

        public Integer getCoreId() {
            return coreId;
        }

        public void setCoreId(Integer coreId) {
            this.coreId = coreId;
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

        public Integer getCutOffId() {
            return cutOffId;
        }

        public void setCutOffId(Integer cutOffId) {
            this.cutOffId = cutOffId;
        }

        public Integer getIsSentToServer() {
            return isSentToServer;
        }

        public void setIsSentToServer(Integer isSentToServer) {
            this.isSentToServer = isSentToServer;
        }

        public Integer getIsVoid() {
            return isVoid;
        }

        public void setIsVoid(Integer isVoid) {
            this.isVoid = isVoid;
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

        public String getOtherData() {
            return otherData;
        }

        public void setOtherData(String otherData) {
            this.otherData = otherData;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
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
