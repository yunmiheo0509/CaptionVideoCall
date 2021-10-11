package com.cationvideocall.example.captionvideocall.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecentCallEntity {
    @PrimaryKey(autoGenerate = true)    // 기본키 자동생성
    public int id;

    @ColumnInfo(name = "user_id")
    public String user_id;

    @ColumnInfo(name = "counter_id")
    public String counter_id;

    @ColumnInfo(name = "name")
    public String name;

    public RecentCallEntity(int id, String user_id, String counter_id, String name) {
        this.id = id;
        this.user_id = user_id;
        this.counter_id = counter_id;
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCounter_id() {
        return counter_id;
    }

    public void setCounter_id(String counter_id) {
        this.counter_id = counter_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

