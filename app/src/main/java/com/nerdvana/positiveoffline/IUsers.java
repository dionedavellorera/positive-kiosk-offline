package com.nerdvana.positiveoffline;

import com.nerdvana.positiveoffline.apiresponses.FetchCashDenominationResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchCreditCardResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchDiscountResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.apiresponses.TestResponse;
import com.nerdvana.positiveoffline.apiresponses.VerifyMachineResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IUsers {

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

}
