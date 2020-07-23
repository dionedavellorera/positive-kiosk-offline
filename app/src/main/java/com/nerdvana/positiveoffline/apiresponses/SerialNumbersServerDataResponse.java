package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SerialNumbersServerDataResponse {

    @SerializedName("serial_numbers")
    @Expose
    private List<SerialNumber> serialNumbers = null;

    public List<SerialNumber> getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(List<SerialNumber> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }

    public class SerialNumber {

        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("for_update")
        @Expose
        private Integer forUpdate;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("is_void")
        @Expose
        private Integer isVoid;
        @SerializedName("is_void_at")
        @Expose
        private String isVoidAt;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
        @SerializedName("order_id")
        @Expose
        private Integer orderId;
        @SerializedName("product_core_id")
        @Expose
        private Integer productCoreId;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("serial_number")
        @Expose
        private String serialNumber;
        @SerializedName("transaction_id")
        @Expose
        private Integer transactionId;
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

        @SerializedName("to_id")
        @Expose
        private Integer toId;

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

        public Integer getBranchId() {
            return branchId;
        }

        public void setBranchId(Integer branchId) {
            this.branchId = branchId;
        }

        public Integer getForUpdate() {
            return forUpdate;
        }

        public void setForUpdate(Integer forUpdate) {
            this.forUpdate = forUpdate;
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

        public String getIsVoidAt() {
            return isVoidAt;
        }

        public void setIsVoidAt(String isVoidAt) {
            this.isVoidAt = isVoidAt;
        }

        public Integer getMachineId() {
            return machineId;
        }

        public void setMachineId(Integer machineId) {
            this.machineId = machineId;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getProductCoreId() {
            return productCoreId;
        }

        public void setProductCoreId(Integer productCoreId) {
            this.productCoreId = productCoreId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Integer getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(Integer transactionId) {
            this.transactionId = transactionId;
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
