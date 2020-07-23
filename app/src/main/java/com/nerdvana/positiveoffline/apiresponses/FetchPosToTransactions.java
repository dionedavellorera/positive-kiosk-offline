package com.nerdvana.positiveoffline.apiresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchPosToTransactions {
    @SerializedName("data")
    @Expose
    private List<ToTransactionsData> data;

    public List<ToTransactionsData> getData() {
        return data;
    }

    public void setData(List<ToTransactionsData> data) {
        this.data = data;
    }

    public class ToTransactionsData {
        @SerializedName("serial_number")
        @Expose
        private List<SerialNumbersServerDataResponse.SerialNumber> serialNumberList = null;
        @SerializedName("posted_discounts")
        @Expose
        private List<PostingDiscountServerDataResponse.PostingDiscount> postingDiscount = null;

        @SerializedName("offline_transactions")
        @Expose
        private TransactionsServerDataResponse.Transaction transactions = null;

        @SerializedName("offline_orders")
        @Expose
        private List<OrdersServerDataResponse.Order> orders = null;

        @SerializedName("order_discounts")
        @Expose
        private List<OrderDiscountsServerDataResponse.OrderDiscount> orderDiscount = null;


        public List<SerialNumbersServerDataResponse.SerialNumber> getSerialNumberList() {
            return serialNumberList;
        }

        public void setSerialNumberList(List<SerialNumbersServerDataResponse.SerialNumber> serialNumberList) {
            this.serialNumberList = serialNumberList;
        }

        public List<PostingDiscountServerDataResponse.PostingDiscount> getPostingDiscount() {
            return postingDiscount;
        }

        public void setPostingDiscount(List<PostingDiscountServerDataResponse.PostingDiscount> postingDiscount) {
            this.postingDiscount = postingDiscount;
        }

        public List<OrderDiscountsServerDataResponse.OrderDiscount> getOrderDiscount() {
            return orderDiscount;
        }

        public void setOrderDiscount(List<OrderDiscountsServerDataResponse.OrderDiscount> orderDiscount) {
            this.orderDiscount = orderDiscount;
        }

        public List<OrdersServerDataResponse.Order> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersServerDataResponse.Order> orders) {
            this.orders = orders;
        }

        public TransactionsServerDataResponse.Transaction getTransactions() {
            return transactions;
        }

        public void setTransactions(TransactionsServerDataResponse.Transaction transactions) {
            this.transactions = transactions;
        }
    }
}
