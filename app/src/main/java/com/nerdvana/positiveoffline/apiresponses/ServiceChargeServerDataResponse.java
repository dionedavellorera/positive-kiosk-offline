package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceChargeServerDataResponse {
    @SerializedName("service_charge")
    @Expose
    private List<ServiceCharge> serviceCharge = null;

    public List<ServiceCharge> getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(List<ServiceCharge> serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public class ServiceCharge {

        @SerializedName("my_id")
        @Expose
        private Integer myId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("branch_id")
        @Expose
        private Integer branchId;
        @SerializedName("is_percentage")
        @Expose
        private Integer isPercentage;
        @SerializedName("is_selected")
        @Expose
        private Integer isSelected;
        @SerializedName("is_sent_to_server")
        @Expose
        private Integer isSentToServer;
        @SerializedName("machine_id")
        @Expose
        private Integer machineId;
        @SerializedName("value")
        @Expose
        private String value;
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

        public Integer getIsPercentage() {
            return isPercentage;
        }

        public void setIsPercentage(Integer isPercentage) {
            this.isPercentage = isPercentage;
        }

        public Integer getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(Integer isSelected) {
            this.isSelected = isSelected;
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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
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
