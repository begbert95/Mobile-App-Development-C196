package com.C196.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.C196.database.Repository;

import java.util.ArrayList;

@Entity (tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String start;
    private String end;
    private ArrayList<Course> courses;


    public Term(int id, String title, String start, String end){
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public Term(String title, String start, String end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public Term(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(Repository r) {
        for(Course c : r.getAllCourses())
            if(c.getTermID() == id)
                courses.add(c);
    }

    public boolean isSafeToDelete(){
        return courses.isEmpty();
    }
}
