package com.C196.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.C196.entities.Course;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM courses ORDER BY ID")
    List<Course> getAllCourses();

    @Query("SELECT * FROM COURSES WHERE ID = :id")
    Course lookupCourse(int id);
}
