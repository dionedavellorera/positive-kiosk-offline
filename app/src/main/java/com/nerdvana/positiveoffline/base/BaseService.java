package com.nerdvana.positiveoffline.base;

import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.PosApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseService {

    public BaseService(PosApplication posApplication) {
        BusProvider.getInstance().register(this);
    }

    protected <T> void asyncRequest(final Call<T> apiCall) {
        apiCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {


                BusProvider.getInstance().post(response.body());
                try {
//                    BusProvider.getInstance().post(response.body());
                } catch (Exception e) {
//                    BusProvider.getInstance().post(new ApiError(e.getMessage()+ "\n" + call.request().url().encodedPath() + " \n" + call.request().url().query()));
//                    BusProvider.getInstance().post(new ProgressBarModel(false, e.getMessage() + "\n" + call.request().url().encodedPath() + " \n" + call.request().url().query()));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
//                BusProvider.getInstance().post(new ApiError(t.getLocalizedMessage()));
//                BusProvider.getInstance().post(new ProgressBarModel(false, t.getLocalizedMessage()));
            }
        });

    }

}
