package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EndOfDayServerDataResponse {

    @SerializedName("end_of_day")
    @Expose
    private List<EndOfDay> endOfDay = null;

    public List<EndOfDay> getEndOfDay() {
        return endOfDay;
    }

    public void setEndOfDay(List<EndOfDay> endOfDay) {
        this.endOfDay = endOfDay;
    }

    public class EndOfDay {
        @SerializedName("discount_amount")
        @Expose
        private Double discount_amount;
        @SerializedName("card_redeemed_from_prev_ar")
        @Expose
        private Double cardRedeemedFromPrevAr;
        @SerializedName("cash_redeemed_from_prev_ar")
        @Expose
        private Double cashRedeemedFromPrevAr;
        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("begOrNo")
        @Expose
        private String begOrNo;
        @SerializedName("begSales")
        @Expose
        private Double begSales;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("endOrNo")
        @Expose
        private String endOrNo;
        @SerializedName("endSales")
        @Expose
        private Double endSales;
        @SerializedName("gross_sales")
        @Expose
        private Double grossSales;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
        @SerializedName("net_sales")
        @Expose
        private Double netSales;
        @SerializedName("number_of_transactions")
        @Expose
        private Integer numberOfTransactions;
        @SerializedName("othersAmount")
        @Expose
        private Double othersAmount;
        @SerializedName("othersCount")
        @Expose
        private Integer othersCount;
        @SerializedName("pwdAmount")
        @Expose
        private Double pwdAmount;
        @SerializedName("pwdCount")
        @Expose
        private Integer pwdCount;
        @SerializedName("seniorAmount")
        @Expose
        private Double seniorAmount;
        @SerializedName("seniorCount")
        @Expose
        private Integer seniorCount;
        @SerializedName("total_card_payments")
        @Expose
        private Double totalCardPayments;
        @SerializedName("total_cash_amount")
        @Expose
        private Double totalCashAmount;
        @SerializedName("total_cash_payments")
        @Expose
        private Double totalCashPayments;
        @SerializedName("total_change")
        @Expose
        private String totalChange;
        @SerializedName("vat_amount")
        @Expose
        private Double vatAmount;
        @SerializedName("vat_exempt_sales")
        @Expose
        private Double vatExemptSales;
        @SerializedName("vatable_sales")
        @Expose
        private Double vatableSales;
        @SerializedName("void_amount")
        @Expose
        private Double voidAmount;
        @SerializedName("total_service_charge")
        @Expose
        private String totalServiceCharge;
        @SerializedName("total_payout")
        @Expose
        private String totalPayout;
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

        public Double getDiscount_amount() {
            return discount_amount;
        }

        public void setDiscount_amount(Double discount_amount) {
            this.discount_amount = discount_amount;
        }

        public Double getCardRedeemedFromPrevAr() {
            return cardRedeemedFromPrevAr;
        }

        public void setCardRedeemedFromPrevAr(Double cardRedeemedFromPrevAr) {
            this.cardRedeemedFromPrevAr = cardRedeemedFromPrevAr;
        }

        public Double getCashRedeemedFromPrevAr() {
            return cashRedeemedFromPrevAr;
        }

        public void setCashRedeemedFromPrevAr(Double cashRedeemedFromPrevAr) {
            this.cashRedeemedFromPrevAr = cashRedeemedFromPrevAr;
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

        public String getBegOrNo() {
            return begOrNo;
        }

        public void setBegOrNo(String begOrNo) {
            this.begOrNo = begOrNo;
        }

        public Double getBegSales() {
            return begSales;
        }

        public void setBegSales(Double begSales) {
            this.begSales = begSales;
        }

        public Integer getBranchId() {
            return branchId;
        }

        public void setBranchId(Integer branchId) {
            this.branchId = branchId;
        }

        public String getEndOrNo() {
            return endOrNo;
        }

        public void setEndOrNo(String endOrNo) {
            this.endOrNo = endOrNo;
        }

        public Double getEndSales() {
            return endSales;
        }

        public void setEndSales(Double endSales) {
            this.endSales = endSales;
        }

        public Double getGrossSales() {
            return grossSales;
        }

        public void setGrossSales(Double grossSales) {
            this.grossSales = grossSales;
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

        public Double getNetSales() {
            return netSales;
        }

        public void setNetSales(Double netSales) {
            this.netSales = netSales;
        }

        public Integer getNumberOfTransactions() {
            return numberOfTransactions;
        }

        public void setNumberOfTransactions(Integer numberOfTransactions) {
            this.numberOfTransactions = numberOfTransactions;
        }

        public Double getOthersAmount() {
            return othersAmount;
        }

        public void setOthersAmount(Double othersAmount) {
            this.othersAmount = othersAmount;
        }

        public Integer getOthersCount() {
            return othersCount;
        }

        public void setOthersCount(Integer othersCount) {
            this.othersCount = othersCount;
        }

        public Double getPwdAmount() {
            return pwdAmount;
        }

        public void setPwdAmount(Double pwdAmount) {
            this.pwdAmount = pwdAmount;
        }

        public Integer getPwdCount() {
            return pwdCount;
        }

        public void setPwdCount(Integer pwdCount) {
            this.pwdCount = pwdCount;
        }

        public Double getSeniorAmount() {
            return seniorAmount;
        }

        public void setSeniorAmount(Double seniorAmount) {
            this.seniorAmount = seniorAmount;
        }

        public Integer getSeniorCount() {
            return seniorCount;
        }

        public void setSeniorCount(Integer seniorCount) {
            this.seniorCount = seniorCount;
        }

        public Double getTotalCardPayments() {
            return totalCardPayments;
        }

        public void setTotalCardPayments(Double totalCardPayments) {
            this.totalCardPayments = totalCardPayments;
        }

        public Double getTotalCashAmount() {
            return totalCashAmount;
        }

        public void setTotalCashAmount(Double totalCashAmount) {
            this.totalCashAmount = totalCashAmount;
        }

        public Double getTotalCashPayments() {
            return totalCashPayments;
        }

        public void setTotalCashPayments(Double totalCashPayments) {
            this.totalCashPayments = totalCashPayments;
        }

        public String getTotalChange() {
            return totalChange;
        }

        public void setTotalChange(String totalChange) {
            this.totalChange = totalChange;
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

        public Double getVoidAmount() {
            return voidAmount;
        }

        public void setVoidAmount(Double voidAmount) {
            this.voidAmount = voidAmount;
        }

        public String getTotalServiceCharge() {
            return totalServiceCharge;
        }

        public void setTotalServiceCharge(String totalServiceCharge) {
            this.totalServiceCharge = totalServiceCharge;
        }

        public String getTotalPayout() {
            return totalPayout;
        }

        public void setTotalPayout(String totalPayout) {
            this.totalPayout = totalPayout;
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
