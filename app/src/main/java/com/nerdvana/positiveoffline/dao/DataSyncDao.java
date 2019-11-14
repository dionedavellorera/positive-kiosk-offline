package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.User;

import java.util.List;

@Dao
public interface DataSyncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DataSync> dataSyncList);

    @Query("SELECT * FROM DataSync")
    LiveData<List<DataSync>> syncList();

    @Query("SELECT * FROM DataSync WHERE isSynced = '0'")
    List<DataSync> unsyncList();

    @Update()
    void update(DataSync dataSync);


}
