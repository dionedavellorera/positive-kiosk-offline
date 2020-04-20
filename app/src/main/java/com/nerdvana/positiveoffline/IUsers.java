package com.nerdvana.positiveoffline;

import com.nerdvana.positiveoffline.apiresponses.FetchCashDenominationResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchCreditCardResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchDiscountResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomStatusResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.apiresponses.TestResponse;
import com.nerdvana.positiveoffline.apiresponses.VerifyMachineResponse;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IUsers {

    @GET("getinfo")
    Call<ResponseBody> sirGelo();

    //repatch data
    @POST("rePatch")
    @FormUrlEncoded
    Call<ResponseBody> repatchData(@FieldMap Map<String, String> params);
    //endregion


    //region fetch data for offline trans
    @POST("fetchEndOfDayOffline")
    @FormUrlEncoded
    Call<EndOfDay> fetchEndOfDayOffline(@FieldMap Map<String, Object> params);
    @POST("fetchPostedDiscountsOffline")
    @FormUrlEncoded
    Call<PostedDiscounts> fetchPostedDiscountsOffline(@FieldMap Map<String, Object> params);
    @POST("fetchPaymentsOffline")
    @FormUrlEncoded
    Call<Payments> fetchPaymentsOffline(@FieldMap Map<String, Object> params);
    @POST("fetchOrdersOffline")
    @FormUrlEncoded
    Call<Orders> fetchOrdersOffline(@FieldMap Map<String, Object> params);
    @POST("fetchOrderDiscountsOffline")
    @FormUrlEncoded
    Call<OrderDiscounts> fetchOrderDiscountsOffline(@FieldMap Map<String, Object> params);
    @POST("fetchTransactionsOffline")
    @FormUrlEncoded
    Call<Transactions> fetchTransactionsOffline(@FieldMap Map<String, Object> params);
    @POST("fetchOrDetailsOffline")
    @FormUrlEncoded
    Call<OrDetails> fetchOrDetailsOffline(@FieldMap Map<String, Object> params);
    @POST("fetchCutOffOffline")
    @FormUrlEncoded
    Call<CutOff> fetchCutOffOffline(@FieldMap Map<String, Object> params);
    //endregion
    //region submit data to server of offline transactions
    @POST("addPostedDiscountsOffline")
    @FormUrlEncoded
    Call<ResponseBody> addPostedDiscountsOffline(@FieldMap Map<String, Object> params);
    @POST("addPaymentsOffline")
    @FormUrlEncoded
    Call<ResponseBody> addPaymentsOffline(@FieldMap Map<String, Object> params);
    @POST("addOrdersOffline")
    @FormUrlEncoded
    Call<ResponseBody> addOrdersOffline(@FieldMap Map<String, Object> params);
    @POST("addOrderDiscountsOffline")
    @FormUrlEncoded
    Call<ResponseBody> addOrderDiscountsOffline(@FieldMap Map<String, Object> params);
    @POST("addTransactionsOffline")
    @FormUrlEncoded
    Call<ResponseBody> addTransactionsOffline(@FieldMap Map<String, Object> params);
    @POST("addOrDetailsOffline")
    @FormUrlEncoded
    Call<ResponseBody> addOrDetailsOffline(@FieldMap Map<String, Object> params);
    @POST("addCutOffOffline")
    @FormUrlEncoded
    Call<ResponseBody> addCutOffOffline(@FieldMap Map<String, Object> params);
    @POST("addEndOfDayOffline")
    @FormUrlEncoded
    Call<ResponseBody> addEndOfDayOffline(@FieldMap Map<String, Object> params);
    //endregion



    @POST("dioneIsPerfect")
    @FormUrlEncoded
    Call<ResponseBody> sendData(@FieldMap Map<String, Object> params);

    @POST("test")
    @FormUrlEncoded
    Call<TestResponse> sendTestRequest(@FieldMap Map<String, String> params);

    @POST("verifyMachine")
    @FormUrlEncoded
    Call<VerifyMachineResponse> sendVerifyMachineRequest(@FieldMap Map<String, String> params);

    @POST("fetchUser")
    @FormUrlEncoded
    Call<FetchUserResponse> fetchUserRequest(@FieldMap Map<String, String> params);

    @POST("fetchProducts")
    @FormUrlEncoded
    Call<FetchProductsResponse> fetchProductsRequest(@FieldMap Map<String, String> params);

    @POST("fetchPaymentType")
    @FormUrlEncoded
    Call<FetchPaymentTypeResponse> fetchPaymentTypeRequest(@FieldMap Map<String, String> params);

    @POST("fetchCreditCard")
    @FormUrlEncoded
    Call<FetchCreditCardResponse> fetchCreditCardRequest(@FieldMap Map<String, String> params);

    @POST("fetchCashDenomination")
    @FormUrlEncoded
    Call<FetchCashDenominationResponse> fetchDenominationRequest(@FieldMap Map<String, String> params);

    @POST("fetchDiscount")
    @FormUrlEncoded
    Call<FetchDiscountResponse> fetchDiscountRequest(@FieldMap Map<String, String> params);

    @POST("fetchRoom")
    @FormUrlEncoded
    Call<FetchRoomResponse> fetchRoomRequest(@FieldMap Map<String, String> params);

    @POST("fetchRoomStatus")
    @FormUrlEncoded
    Call<FetchRoomStatusResponse> fetchRoomStatusRequest(@FieldMap Map<String, String> params);

}
