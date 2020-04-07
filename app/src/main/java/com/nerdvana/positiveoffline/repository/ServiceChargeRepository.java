package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.nerdvana.positiveoffline.dao.OrderDiscountsDao;
import com.nerdvana.positiveoffline.dao.ServiceChargeDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.ServiceCharge;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServiceChargeRepository {

    ServiceChargeDao serviceChargeDao;

    public ServiceChargeRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        serviceChargeDao = posDatabase.serviceChargeDao();
    }

    public void insertServiceChargeSetting(ServiceCharge serviceCharge) {
        new ServiceChargeRepository.insertServiceChargeSetting(serviceChargeDao).execute(serviceCharge);
    }

    public void updateServiceChargeSetting(ServiceCharge serviceCharge) {
        new ServiceChargeRepository.updateServiceChargeSetting(serviceChargeDao).execute(serviceCharge);
    }


    public List<ServiceCharge> getServiceChargeList() throws ExecutionException, InterruptedException {
        Callable<List<ServiceCharge>> callable = new Callable<List<ServiceCharge>>() {
            @Override
            public List<ServiceCharge> call() throws Exception {
                return serviceChargeDao.serviceChargeList();
            }
        };

        Future<List<ServiceCharge>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public ServiceCharge getActiveServiceCharge() throws ExecutionException, InterruptedException {
        Callable<ServiceCharge> callable = new Callable<ServiceCharge>() {
            @Override
            public ServiceCharge call() throws Exception {
                return serviceChargeDao.getActiveServiceCharge();
            }
        };

        Future<ServiceCharge> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    private static class insertServiceChargeSetting extends AsyncTask<ServiceCharge, Void, Void> {

        private ServiceChargeDao mAsyncTaskDao;

        insertServiceChargeSetting(ServiceChargeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ServiceCharge... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateServiceChargeSetting extends AsyncTask<ServiceCharge, Void, Void> {

        private ServiceChargeDao mAsyncTaskDao;

        updateServiceChargeSetting(ServiceChargeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ServiceCharge... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }




}
