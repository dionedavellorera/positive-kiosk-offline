package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.ServiceCharge;
import com.nerdvana.positiveoffline.repository.CutOffRepository;
import com.nerdvana.positiveoffline.repository.ServiceChargeRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServiceChargeViewModel extends AndroidViewModel {
    private ServiceChargeRepository serviceChargeRepository;

    public ServiceChargeViewModel(@NonNull Application application) {
        super(application);
        serviceChargeRepository = new ServiceChargeRepository(application);
    }

    public void insertServiceChargeSetting(ServiceCharge serviceCharge) {
        serviceChargeRepository.insertServiceChargeSetting(serviceCharge);
    }

    public void updateServiceChargeSetting(ServiceCharge serviceCharge) {
        serviceChargeRepository.updateServiceChargeSetting(serviceCharge);
    }

    public List<ServiceCharge> getServiceChargeList() throws ExecutionException, InterruptedException {
        return serviceChargeRepository.getServiceChargeList();
    }

    public ServiceCharge getActiveServiceCharge() throws ExecutionException, InterruptedException {
        return serviceChargeRepository.getActiveServiceCharge();
    }
}
