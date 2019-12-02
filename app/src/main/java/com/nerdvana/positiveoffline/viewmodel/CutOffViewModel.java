package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.repository.CutOffRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CutOffViewModel extends AndroidViewModel {

    private CutOffRepository cutOffRepository;

    public CutOffViewModel(@NonNull Application application) {
        super(application);
        cutOffRepository = new CutOffRepository(application);
    }

    public long insertData(CutOff list) throws ExecutionException, InterruptedException {
        return cutOffRepository.insert(list);
    }

    public long insertData(EndOfDay endOfDay) throws ExecutionException, InterruptedException {
        return cutOffRepository.insertEndOfDay(endOfDay);
    }

    public void update(EndOfDay endOfDay) throws ExecutionException, InterruptedException {
        cutOffRepository.update(endOfDay);
    }

    public void update(CutOff cutOff) throws ExecutionException, InterruptedException {
        cutOffRepository.update(cutOff);
    }

    public void update(Payments payments) throws ExecutionException, InterruptedException {
        cutOffRepository.update(payments);
    }

    public CutOff getCutOff(long cut_off_id) throws ExecutionException, InterruptedException {
        return cutOffRepository.getCutOff(cut_off_id);
    }

    public EndOfDay getEndOfDay(long end_of_day_id) throws ExecutionException, InterruptedException {
        return cutOffRepository.getEndOfDay(end_of_day_id);
    }

    public List<CutOff> getUnCutOffData() throws ExecutionException, InterruptedException {
        return cutOffRepository.getUnCutOffData();
    }

    public List<Payments> getAllPayments() throws ExecutionException, InterruptedException {
        return cutOffRepository.getAllPayments();
    }

}
