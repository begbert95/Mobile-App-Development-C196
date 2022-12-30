package com.C196.entities;

import androidx.room.Entity;

@Entity(tableName = "statuses")
public enum Status {
    InProgress,
    Completed,
    Dropped,
    PlanToTake
}
