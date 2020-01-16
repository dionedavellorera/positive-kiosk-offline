package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.model.RoomWithRates;
import com.nerdvana.positiveoffline.repository.RoomsRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RoomsViewModel extends AndroidViewModel {

    private RoomsRepository roomsRepository;

    public RoomsViewModel(@NonNull Application application) {
        super(application);
        roomsRepository = new RoomsRepository(application);
    }

    public List<RoomWithRates> getRoomWithRates() throws ExecutionException, InterruptedException {
        return roomsRepository.getRoomWithRates();
    }

    public List<Rooms> getRooms() throws ExecutionException, InterruptedException {
        return roomsRepository.getRooms();
    }

    public LiveData<Rooms> getRoomsLiveData() {
        return roomsRepository.getRoomsLiveData();
    }

    public List<RoomStatus> getRoomStatus() throws ExecutionException, InterruptedException {
        return roomsRepository.getRoomStatus();
    }

    public void update(Rooms rooms) {
        roomsRepository.updateRoom(rooms);
    }

}
