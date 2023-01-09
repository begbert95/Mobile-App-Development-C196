package com.C196.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.C196.entities.Assessment;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM Assessments ORDER BY ID")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM Assessments WHERE ID = :id")
    Assessment lookupAssessment(int id);
}
