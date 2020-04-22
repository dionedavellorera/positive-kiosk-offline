package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersServerDataResponse {

    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public class Order {

        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("core_id")
        @Expose
        private Integer coreId;
        @SerializedName("is_room_rate")
        @Expose
        private Integer isRoomRate;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("departmentId")
        @Expose
        private Integer departmentId;
        @SerializedName("categoryId")
        @Expose
        private Integer categoryId;
        @SerializedName("transaction_id")
        @Expose
        private Integer transactionId;
        @SerializedName("qty")
        @Expose
        private Integer qty;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("is_void")
        @Expose
        private Integer isVoid;
        @SerializedName("is_editing")
        @Expose
        private Integer isEditing;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("categoryName")
        @Expose
        private String categoryName;
        @SerializedName("departmentName")
        @Expose
        private String departmentName;
        @SerializedName("amount")
        @Expose
        private Double amount;
        @SerializedName("discountAmount")
        @Expose
        private Double discountAmount;
        @SerializedName("original_amount")
        @Expose
        private Double originalAmount;
        @SerializedName("vatAmount")
        @Expose
        private Double vatAmount;
        @SerializedName("vatExempt")
        @Expose
        private Double vatExempt;
        @SerializedName("vatable")
        @Expose
        private Double vatable;
        @SerializedName("is_discount_exempt")
        @Expose
        private Integer isDiscountExempt;
        @SerializedName("product_alacart_id")
        @Expose
        private Integer productAlacartId;
        @SerializedName("product_group_id")
        @Expose
        private Integer productGroupId;
        @SerializedName("orders_incremental_id")
        @Expose
        private Integer ordersIncrementalId;
        @SerializedName("notes")
        @Expose
        private Object notes;
        @SerializedName("is_take_out")
        @Expose
        private Integer isTakeOut;
        @SerializedName("serial_number")
        @Expose
        private String serialNumber;
        @SerializedName("is_fixed_asset")
        @Expose
        private Integer isFixedAsset;
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

        public Integer getCoreId() {
            return coreId;
        }

        public void setCoreId(Integer coreId) {
            this.coreId = coreId;
        }

        public Integer getIsRoomRate() {
            return isRoomRate;
        }

        public void setIsRoomRate(Integer isRoomRate) {
            this.isRoomRate = isRoomRate;
        }

        public Integer getBranchId() {
            return branchId;
        }

        public void setBranchId(Integer branchId) {
            this.branchId = branchId;
        }

        public Integer getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(Integer departmentId) {
            this.departmentId = departmentId;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public Integer getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(Integer transactionId) {
            this.transactionId = transactionId;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
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

        public Integer getIsEditing() {
            return isEditing;
        }

        public void setIsEditing(Integer isEditing) {
            this.isEditing = isEditing;
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

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Double getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(Double discountAmount) {
            this.discountAmount = discountAmount;
        }

        public Double getOriginalAmount() {
            return originalAmount;
        }

        public void setOriginalAmount(Double originalAmount) {
            this.originalAmount = originalAmount;
        }

        public Double getVatAmount() {
            return vatAmount;
        }

        public void setVatAmount(Double vatAmount) {
            this.vatAmount = vatAmount;
        }

        public Double getVatExempt() {
            return vatExempt;
        }

        public void setVatExempt(Double vatExempt) {
            this.vatExempt = vatExempt;
        }

        public Double getVatable() {
            return vatable;
        }

        public void setVatable(Double vatable) {
            this.vatable = vatable;
        }

        public Integer getIsDiscountExempt() {
            return isDiscountExempt;
        }

        public void setIsDiscountExempt(Integer isDiscountExempt) {
            this.isDiscountExempt = isDiscountExempt;
        }

        public Integer getProductAlacartId() {
            return productAlacartId;
        }

        public void setProductAlacartId(Integer productAlacartId) {
            this.productAlacartId = productAlacartId;
        }

        public Integer getProductGroupId() {
            return productGroupId;
        }

        public void setProductGroupId(Integer productGroupId) {
            this.productGroupId = productGroupId;
        }

        public Integer getOrdersIncrementalId() {
            return ordersIncrementalId;
        }

        public void setOrdersIncrementalId(Integer ordersIncrementalId) {
            this.ordersIncrementalId = ordersIncrementalId;
        }

        public Object getNotes() {
            return notes;
        }

        public void setNotes(Object notes) {
            this.notes = notes;
        }

        public Integer getIsTakeOut() {
            return isTakeOut;
        }

        public void setIsTakeOut(Integer isTakeOut) {
            this.isTakeOut = isTakeOut;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Integer getIsFixedAsset() {
            return isFixedAsset;
        }

        public void setIsFixedAsset(Integer isFixedAsset) {
            this.isFixedAsset = isFixedAsset;
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
