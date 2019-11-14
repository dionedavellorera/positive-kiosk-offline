package com.nerdvana.positiveoffline;

import com.nerdvana.positiveoffline.apirequests.TestRequest;
import com.nerdvana.positiveoffline.apiresponses.TestResponse;
import com.nerdvana.positiveoffline.base.BaseService;
import com.squareup.otto.Subscribe;

import retrofit2.Call;

public class UserServices extends BaseService {
    public UserServices(PosApplication minutesApplication) {
        super(minutesApplication);
    }

    @Subscribe
    public void onReceiveTestConnection(TestRequest testRequest) {
        IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
        Call<TestResponse> call = iUsers.sendTestRequest(
                testRequest.getMapValue());
        asyncRequest(call);
    }

}
