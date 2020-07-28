package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> user);

    @Update
    void update(User user);

    @Query("SELECT * FROM User")
    LiveData<List<User>> userList();

    @Query("SELECT * FROM User WHERE username = :username")
    List<User> specificUser(String username);

    @Query("SELECT * FROM User WHERE is_logged_in = :isLoggedIn")
    List<User> searchLoggedInUser(Boolean isLoggedIn);

    @Query("SELECT * FROM User WHERE username = :username")
    User searchUsername(String username);

}
