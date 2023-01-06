package com.C196.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.C196.entities.Alert;

import java.util.ArrayList;

@Dao
public interface AlertDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Alert alert);

    @Update
    void update(Alert alert);

    @Delete
    void delete(Alert alert);

    @Query("SELECT * FROM Alerts ORDER BY ID")
    ArrayList<Alert> getAllAlerts();

    @Query("SELECT * FROM Alerts WHERE ID = :id")
    Alert lookupAlert(int id);
}
