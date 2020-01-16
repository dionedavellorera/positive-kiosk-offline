package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.nerdvana.positiveoffline.dao.CutOffDao;
import com.nerdvana.positiveoffline.dao.DataSyncDao;
import com.nerdvana.positiveoffline.dao.EndOfDayDao;
import com.nerdvana.positiveoffline.dao.PaymentsDao;
import com.nerdvana.positiveoffline.dao.RoomRatesDao;
import com.nerdvana.positiveoffline.dao.RoomStatusDao;
import com.nerdvana.positiveoffline.dao.RoomsDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.model.RoomWithRates;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RoomsRepository {

    private RoomsDao roomsDao;
    private RoomRatesDao roomRatesDao;
    private RoomStatusDao roomStatusDao;

    private MutableLiveData<Rooms> roomsLiveData;

    public RoomsRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        roomsDao = posDatabase.roomsDao();
        roomRatesDao = posDatabase.roomRatesDao();
        roomStatusDao = posDatabase.roomStatusDao();

        roomsLiveData = new MutableLiveData<>();
    }

    public LiveData<Rooms> getRoomsLiveData() {
        return roomsDao.roomsLiveData();
    }

    public List<RoomWithRates> getRoomWithRates() throws ExecutionException, InterruptedException {
        Callable<List<RoomWithRates>> callable = new Callable<List<RoomWithRates>>() {
            @Override
            public List<RoomWithRates> call() throws Exception {
                return roomsDao.roomWithRatesList();
            }
        };

        Future<List<RoomWithRates>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Rooms> getRooms() throws ExecutionException, InterruptedException {
        Callable<List<Rooms>> callable = new Callable<List<Rooms>>() {
            @Override
            public List<Rooms> call() throws Exception {
                return roomsDao.rooms();
            }
        };

        Future<List<Rooms>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public void updateRoom(Rooms rooms) {
        new RoomsRepository.updateAsyncTask(roomsDao, rooms).execute();
    }


    private static class updateAsyncTask extends AsyncTask<Rooms, Void, Void> {

        private RoomsDao mAsyncTaskDao;

        private Rooms roomsData;
        updateAsyncTask(RoomsDao dao, Rooms rooms) {
            mAsyncTaskDao = dao;
            this.roomsData = rooms;
        }

        @Override
        protected Void doInBackground(final Rooms... params) {
            mAsyncTaskDao.update(roomsData);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public List<RoomStatus> getRoomStatus() throws ExecutionException, InterruptedException {
        Callable<List<RoomStatus>> callable = new Callable<List<RoomStatus>>() {
            @Override
            public List<RoomStatus> call() throws Exception {
                return roomStatusDao.getRoomStatus();
            }
        };

        Future<List<RoomStatus>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

}
