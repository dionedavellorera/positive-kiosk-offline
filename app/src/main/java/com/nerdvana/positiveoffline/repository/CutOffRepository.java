package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.nerdvana.positiveoffline.dao.CutOffDao;
import com.nerdvana.positiveoffline.dao.EndOfDayDao;
import com.nerdvana.positiveoffline.dao.PaymentsDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.Payments;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CutOffRepository {
    private CutOffDao cutOffDao;
    private EndOfDayDao endOfDayDao;
    private PaymentsDao paymentsDao;

    public CutOffRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        cutOffDao = posDatabase.cutOffDao();
        endOfDayDao = posDatabase.endOfDayDao();
        paymentsDao = posDatabase.paymentsDao();

    }

    public long insert(CutOff cutOffList) throws ExecutionException, InterruptedException {
        return new CutOffRepository.insertAsyncTask(cutOffDao).execute(cutOffList).get();
    }

    public long insertEndOfDay(EndOfDay endOfDay) throws ExecutionException, InterruptedException {
        return new CutOffRepository.insertEndOfDayAsyncTask(endOfDayDao).execute(endOfDay).get();
    }

    public void update(CutOff cutOff) throws ExecutionException, InterruptedException {
        new CutOffRepository.updateAsyncTask(cutOffDao).execute(cutOff);
    }

    public void update(EndOfDay endOfDay) throws ExecutionException, InterruptedException {
        new CutOffRepository.updateEndOfDayAsyncTask(endOfDayDao).execute(endOfDay);
    }

    public void update(Payments payments) throws ExecutionException, InterruptedException {
        new CutOffRepository.updatePaymentAsyncTask(paymentsDao).execute(payments);
    }


    private static class updatePaymentAsyncTask extends AsyncTask<Payments, Void, Void> {

        private PaymentsDao mAsyncTaskDao;

        updatePaymentAsyncTask(PaymentsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Payments... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class updateEndOfDayAsyncTask extends AsyncTask<EndOfDay, Void, Void> {

        private EndOfDayDao mAsyncTaskDao;

        updateEndOfDayAsyncTask(EndOfDayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EndOfDay... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }


    private static class updateAsyncTask extends AsyncTask<CutOff, Void, Void> {

        private CutOffDao mAsyncTaskDao;

        updateAsyncTask(CutOffDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CutOff... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class insertEndOfDayAsyncTask extends AsyncTask<EndOfDay, Void, Long> {

        private EndOfDayDao mAsyncTaskDao;

        insertEndOfDayAsyncTask(EndOfDayDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final EndOfDay... params) {
            return mAsyncTaskDao.insert(params[0]);
        }
    }

    private static class insertAsyncTask extends AsyncTask<CutOff, Void, Long> {

        private CutOffDao mAsyncTaskDao;

        insertAsyncTask(CutOffDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final CutOff... params) {
            return mAsyncTaskDao.insert(params[0]);
        }
    }

    public CutOff getCutOff(final long cut_off_id) throws ExecutionException, InterruptedException {
        Callable<CutOff> callable = new Callable<CutOff>() {
            @Override
            public CutOff call() throws Exception {
                return cutOffDao.cutOffData(cut_off_id);
            }
        };

        Future<CutOff> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public EndOfDay getEndOfDay(final long end_of_day_id) throws ExecutionException, InterruptedException {
        Callable<EndOfDay> callable = new Callable<EndOfDay>() {
            @Override
            public EndOfDay call() throws Exception {
                return endOfDayDao.endOfDayData(end_of_day_id);
            }
        };

        Future<EndOfDay> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public List<CutOff> getUnCutOffData() throws ExecutionException, InterruptedException {
        Callable<List<CutOff>> callable = new Callable<List<CutOff>>() {
            @Override
            public List<CutOff> call() throws Exception {
                return cutOffDao.unCutOffTransactions();
            }
        };

        Future<List<CutOff>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Payments> getAllPayments() throws ExecutionException, InterruptedException {
        Callable<List<Payments>> callable = new Callable<List<Payments>>() {
            @Override
            public List<Payments> call() throws Exception {
                return paymentsDao.paymentsForCutOff();
            }
        };

        Future<List<Payments>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


}
