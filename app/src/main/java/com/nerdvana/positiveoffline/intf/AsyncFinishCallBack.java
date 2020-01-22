package com.nerdvana.positiveoffline.intf;

import android.content.Context;

public interface AsyncFinishCallBack {
    void doneProcessing();
    void retryProcessing();
    void error(String message);
}
