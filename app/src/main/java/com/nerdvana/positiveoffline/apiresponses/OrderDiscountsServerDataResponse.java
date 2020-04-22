package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDiscountsServerDataResponse {

    @SerializedName("order_discount")
    @Expose
    private List<OrderDiscount> orderDiscount = null;

    public List<OrderDiscount> getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(List<OrderDiscount> orderDiscount) {
        this.orderDiscount = orderDiscount;
    }


    public class OrderDiscount {

        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("transaction_id")
        @Expose
        private Integer transactionId;
        @SerializedName("order_id")
        @Expose
        private Integer orderId;
        @SerializedName("posted_discount_id")
        @Expose
        private Integer postedDiscountId;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("is_void")
        @Expose
        private Integer isVoid;
        @SerializedName("is_percentage")
        @Expose
        private Integer isPercentage;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
        @SerializedName("discount_name")
        @Expose
        private String discountName;
        @SerializedName("value")
        @Expose
        private Double value;
        @SerializedName("product_id")
        @Expose
        private Integer productId;
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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getPostedDiscountId() {
            return postedDiscountId;
        }

        public void setPostedDiscountId(Integer postedDiscountId) {
            this.postedDiscountId = postedDiscountId;
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

        public String getDiscountName() {
            return discountName;
        }

        public void setDiscountName(String discountName) {
            this.discountName = discountName;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
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
