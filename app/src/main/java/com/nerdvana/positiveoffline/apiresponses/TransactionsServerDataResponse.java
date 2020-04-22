package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionsServerDataResponse {

    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public class Transaction {

        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("room_id")
        @Expose
        private Integer roomId;
        @SerializedName("room_number")
        @Expose
        private Object roomNumber;
        @SerializedName("check_in_time")
        @Expose
        private Object checkInTime;
        @SerializedName("check_out_time")
        @Expose
        private Object checkOutTime;
        @SerializedName("control_number")
        @Expose
        private String controlNumber;
        @SerializedName("receipt_number")
        @Expose
        private String receiptNumber;
        @SerializedName("tin_number")
        @Expose
        private String tinNumber;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("cut_off_id")
        @Expose
        private Integer cutOffId;
        @SerializedName("has_special")
        @Expose
        private Integer hasSpecial;
        @SerializedName("is_void")
        @Expose
        private Integer isVoid;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
        @SerializedName("change")
        @Expose
        private Double change;
        @SerializedName("discount_amount")
        @Expose
        private Double discountAmount;
        @SerializedName("gross_sales")
        @Expose
        private Double grossSales;
        @SerializedName("net_sales")
        @Expose
        private Double netSales;
        @SerializedName("vat_amount")
        @Expose
        private Double vatAmount;
        @SerializedName("vat_exempt_sales")
        @Expose
        private Double vatExemptSales;
        @SerializedName("vatable_sales")
        @Expose
        private Double vatableSales;
        @SerializedName("completed_at")
        @Expose
        private String completedAt;
        @SerializedName("is_completed_by")
        @Expose
        private String isCompletedBy;
        @SerializedName("is_cancelled_by")
        @Expose
        private Object isCancelledBy;
        @SerializedName("is_cancelled")
        @Expose
        private Integer isCancelled;
        @SerializedName("is_cancelled_at")
        @Expose
        private Object isCancelledAt;
        @SerializedName("is_cut_off_at")
        @Expose
        private Object isCutOffAt;
        @SerializedName("is_cut_off_by")
        @Expose
        private Object isCutOffBy;
        @SerializedName("is_cut_off")
        @Expose
        private Integer isCutOff;
        @SerializedName("is_saved_by")
        @Expose
        private Object isSavedBy;
        @SerializedName("void_at")
        @Expose
        private Object voidAt;
        @SerializedName("is_void_by")
        @Expose
        private Object isVoidBy;
        @SerializedName("is_saved")
        @Expose
        private Integer isSaved;
        @SerializedName("is_completed")
        @Expose
        private Integer isCompleted;
        @SerializedName("is_backed_out")
        @Expose
        private Integer isBackedOut;
        @SerializedName("is_backed_out_by")
        @Expose
        private Object isBackedOutBy;
        @SerializedName("is_backed_out_at")
        @Expose
        private Object isBackedOutAt;
        @SerializedName("trans_name")
        @Expose
        private Object transName;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("service_charge_value")
        @Expose
        private String serviceChargeValue;
        @SerializedName("service_charge_is_percentage")
        @Expose
        private Integer serviceChargeIsPercentage;
        @SerializedName("is_shared")
        @Expose
        private Integer isShared;
        @SerializedName("saved_at")
        @Expose
        private Object savedAt;
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

        public Integer getRoomId() {
            return roomId;
        }

        public void setRoomId(Integer roomId) {
            this.roomId = roomId;
        }

        public Object getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(Object roomNumber) {
            this.roomNumber = roomNumber;
        }

        public Object getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(Object checkInTime) {
            this.checkInTime = checkInTime;
        }

        public Object getCheckOutTime() {
            return checkOutTime;
        }

        public void setCheckOutTime(Object checkOutTime) {
            this.checkOutTime = checkOutTime;
        }

        public String getControlNumber() {
            return controlNumber;
        }

        public void setControlNumber(String controlNumber) {
            this.controlNumber = controlNumber;
        }

        public String getReceiptNumber() {
            return receiptNumber;
        }

        public void setReceiptNumber(String receiptNumber) {
            this.receiptNumber = receiptNumber;
        }

        public String getTinNumber() {
            return tinNumber;
        }

        public void setTinNumber(String tinNumber) {
            this.tinNumber = tinNumber;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getIsSentToServer() {
            return isSentToServer;
        }

        public void setIsSentToServer(Integer isSentToServer) {
            this.isSentToServer = isSentToServer;
        }

        public Integer getCutOffId() {
            return cutOffId;
        }

        public void setCutOffId(Integer cutOffId) {
            this.cutOffId = cutOffId;
        }

        public Integer getHasSpecial() {
            return hasSpecial;
        }

        public void setHasSpecial(Integer hasSpecial) {
            this.hasSpecial = hasSpecial;
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

        public Double getChange() {
            return change;
        }

        public void setChange(Double change) {
            this.change = change;
        }

        public Double getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(Double discountAmount) {
            this.discountAmount = discountAmount;
        }

        public Double getGrossSales() {
            return grossSales;
        }

        public void setGrossSales(Double grossSales) {
            this.grossSales = grossSales;
        }

        public Double getNetSales() {
            return netSales;
        }

        public void setNetSales(Double netSales) {
            this.netSales = netSales;
        }

        public Double getVatAmount() {
            return vatAmount;
        }

        public void setVatAmount(Double vatAmount) {
            this.vatAmount = vatAmount;
        }

        public Double getVatExemptSales() {
            return vatExemptSales;
        }

        public void setVatExemptSales(Double vatExemptSales) {
            this.vatExemptSales = vatExemptSales;
        }

        public Double getVatableSales() {
            return vatableSales;
        }

        public void setVatableSales(Double vatableSales) {
            this.vatableSales = vatableSales;
        }

        public String getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(String completedAt) {
            this.completedAt = completedAt;
        }

        public String getIsCompletedBy() {
            return isCompletedBy;
        }

        public void setIsCompletedBy(String isCompletedBy) {
            this.isCompletedBy = isCompletedBy;
        }

        public Object getIsCancelledBy() {
            return isCancelledBy;
        }

        public void setIsCancelledBy(Object isCancelledBy) {
            this.isCancelledBy = isCancelledBy;
        }

        public Integer getIsCancelled() {
            return isCancelled;
        }

        public void setIsCancelled(Integer isCancelled) {
            this.isCancelled = isCancelled;
        }

        public Object getIsCancelledAt() {
            return isCancelledAt;
        }

        public void setIsCancelledAt(Object isCancelledAt) {
            this.isCancelledAt = isCancelledAt;
        }

        public Object getIsCutOffAt() {
            return isCutOffAt;
        }

        public void setIsCutOffAt(Object isCutOffAt) {
            this.isCutOffAt = isCutOffAt;
        }

        public Object getIsCutOffBy() {
            return isCutOffBy;
        }

        public void setIsCutOffBy(Object isCutOffBy) {
            this.isCutOffBy = isCutOffBy;
        }

        public Integer getIsCutOff() {
            return isCutOff;
        }

        public void setIsCutOff(Integer isCutOff) {
            this.isCutOff = isCutOff;
        }

        public Object getIsSavedBy() {
            return isSavedBy;
        }

        public void setIsSavedBy(Object isSavedBy) {
            this.isSavedBy = isSavedBy;
        }

        public Object getVoidAt() {
            return voidAt;
        }

        public void setVoidAt(Object voidAt) {
            this.voidAt = voidAt;
        }

        public Object getIsVoidBy() {
            return isVoidBy;
        }

        public void setIsVoidBy(Object isVoidBy) {
            this.isVoidBy = isVoidBy;
        }

        public Integer getIsSaved() {
            return isSaved;
        }

        public void setIsSaved(Integer isSaved) {
            this.isSaved = isSaved;
        }

        public Integer getIsCompleted() {
            return isCompleted;
        }

        public void setIsCompleted(Integer isCompleted) {
            this.isCompleted = isCompleted;
        }

        public Integer getIsBackedOut() {
            return isBackedOut;
        }

        public void setIsBackedOut(Integer isBackedOut) {
            this.isBackedOut = isBackedOut;
        }

        public Object getIsBackedOutBy() {
            return isBackedOutBy;
        }

        public void setIsBackedOutBy(Object isBackedOutBy) {
            this.isBackedOutBy = isBackedOutBy;
        }

        public Object getIsBackedOutAt() {
            return isBackedOutAt;
        }

        public void setIsBackedOutAt(Object isBackedOutAt) {
            this.isBackedOutAt = isBackedOutAt;
        }

        public Object getTransName() {
            return transName;
        }

        public void setTransName(Object transName) {
            this.transName = transName;
        }

        public Integer getBranchId() {
            return branchId;
        }

        public void setBranchId(Integer branchId) {
            this.branchId = branchId;
        }

        public String getServiceChargeValue() {
            return serviceChargeValue;
        }

        public void setServiceChargeValue(String serviceChargeValue) {
            this.serviceChargeValue = serviceChargeValue;
        }

        public Integer getServiceChargeIsPercentage() {
            return serviceChargeIsPercentage;
        }

        public void setServiceChargeIsPercentage(Integer serviceChargeIsPercentage) {
            this.serviceChargeIsPercentage = serviceChargeIsPercentage;
        }

        public Integer getIsShared() {
            return isShared;
        }

        public void setIsShared(Integer isShared) {
            this.isShared = isShared;
        }

        public Object getSavedAt() {
            return savedAt;
        }

        public void setSavedAt(Object savedAt) {
            this.savedAt = savedAt;
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
