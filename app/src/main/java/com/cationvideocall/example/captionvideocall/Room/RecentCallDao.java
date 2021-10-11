package com.cationvideocall.example.captionvideocall.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecentCallDao {
    @Query("SELECT * FROM recentcallentity")
    LiveData<List<RecentCallEntity>> getAll();

    @Insert
    void insert(RecentCallEntity recentcallEntities);

    //최근기록 개수 조회하기.
    @Query("SELECT COUNT(*) FROM recentcallentity WHERE user_id = :user_id")
    Integer getCountRecentCall(String user_id);

    //최근기록 조회하기.
    @Query("SELECT * FROM recentcallentity WHERE user_id = :user_id")
    LiveData<List<RecentCallEntity>> getRecentCall(String user_id);

    //가장오래된 전화의 id(key) 찾기
    @Query("SELECT MIN(id) FROM recentcallentity WHERE user_id = :user_id")
    Integer getOlderKey(String user_id);

    //가장 예전에 온 전화 삭제
    @Query("DELETE FROM recentcallentity WHERE user_id = :user_id AND id = :olderKey")
    void deleteOlderCall(String user_id,Integer olderKey);

    @Delete
    void delete(RecentCallEntity recentcallEntity);
}
