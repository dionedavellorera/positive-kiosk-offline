package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostingDiscountServerDataResponse {
    @SerializedName("posting_discount")
    @Expose
    private List<PostingDiscount> postingDiscount = null;

    public List<PostingDiscount> getPostingDiscount() {
        return postingDiscount;
    }

    public void setPostingDiscount(List<PostingDiscount> postingDiscount) {
        this.postingDiscount = postingDiscount;
    }

    public class PostingDiscount {
        @SerializedName("to_id")
        @Expose
        private Integer toId;
        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("transaction_id")
        @Expose
        private Integer transactionId;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("discount_name")
        @Expose
        private String discountName;
        @SerializedName("card_number")
        @Expose
        private String cardNumber;
        @SerializedName("amount")
        @Expose
        private Double amount;
        @SerializedName("cut_off_id")
        @Expose
        private Integer cutOffId;
        @SerializedName("discount_id")
        @Expose
        private Integer discountId;
        @SerializedName("end_of_day_id")
        @Expose
        private Integer endOfDayId;
        @SerializedName("discount_value")
        @Expose
        private Double discountValue;
        @SerializedName("is_percentage")
        @Expose
        private Integer isPercentage;
        @SerializedName("is_void")
        @Expose
        private Integer isVoid;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
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

        public Integer getToId() {
            return toId;
        }

        public void setToId(Integer toId) {
            this.toId = toId;
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

        public Integer getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(Integer transactionId) {
            this.transactionId = transactionId;
        }

        public Integer getBranchId() {
            return branchId;
        }

        public void setBranchId(Integer branchId) {
            this.branchId = branchId;
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

        public String getDiscountName() {
            return discountName;
        }

        public void setDiscountName(String discountName) {
            this.discountName = discountName;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Integer getCutOffId() {
            return cutOffId;
        }

        public void setCutOffId(Integer cutOffId) {
            this.cutOffId = cutOffId;
        }

        public Integer getDiscountId() {
            return discountId;
        }

        public void setDiscountId(Integer discountId) {
            this.discountId = discountId;
        }

        public Integer getEndOfDayId() {
            return endOfDayId;
        }

        public void setEndOfDayId(Integer endOfDayId) {
            this.endOfDayId = endOfDayId;
        }

        public Double getDiscountValue() {
            return discountValue;
        }

        public void setDiscountValue(Double discountValue) {
            this.discountValue = discountValue;
        }

        public Integer getIsPercentage() {
            return isPercentage;
        }

        public void setIsPercentage(Integer isPercentage) {
            this.isPercentage = isPercentage;
        }

        public Integer getIsVoid() {
            return isVoid;
        }

        public void setIsVoid(Integer isVoid) {
            this.isVoid = isVoid;
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
