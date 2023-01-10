package com.C196.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.C196.dao.AssessmentDAO;
import com.C196.dao.CourseDAO;
import com.C196.dao.TermDAO;
import com.C196.entities.Assessment;
import com.C196.entities.Course;
import com.C196.entities.Term;

@Database(entities = {Course.class, Term.class, Assessment.class}, version = 2, exportSchema = false)
public abstract class DatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (DatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "C195Database.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
