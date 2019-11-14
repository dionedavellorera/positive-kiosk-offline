package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.repository.DataSyncRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataSyncViewModel extends AndroidViewModel {

    private DataSyncRepository mRepository;
    private LiveData<List<DataSync>> mSyncData;

    public DataSyncViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataSyncRepository(application);
        mSyncData = mRepository.getAllSyncList();
    }


    public LiveData<List<DataSync>> getmSyncData() {
        return mSyncData;
    }

    public List<DataSync> getUnsyncedData() throws ExecutionException, InterruptedException {
        return mRepository.getUnsyncedList();
    }

    public void updateIsSynced(DataSync dataSync) {
        mRepository.update(dataSync);
    }

    public void insertData(List<DataSync> list) {
        mRepository.insert(list);
    }
}
