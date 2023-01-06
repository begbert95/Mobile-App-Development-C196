package com.C196.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alerts")
public class Alert {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;
    private String date;

    public Alert(int id, String text, String date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public Alert(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
