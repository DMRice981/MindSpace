package com.mindspace.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mindspace.app.data.model.MoodRecord;

import java.util.List;

@Dao
public interface MoodDao {

    @Query("SELECT * FROM mood_records ORDER BY created_at DESC")
    LiveData<List<MoodRecord>> getAllMoods();

    @Query("SELECT * FROM mood_records ORDER BY created_at DESC LIMIT :limit")
    LiveData<List<MoodRecord>> getRecentMoods(int limit);

    @Query("SELECT * FROM mood_records WHERE created_at >= :startTime ORDER BY created_at ASC")
    List<MoodRecord> getMoodsSince(long startTime);

    @Query("SELECT * FROM mood_records WHERE created_at BETWEEN :startTime AND :endTime ORDER BY created_at ASC")
    LiveData<List<MoodRecord>> getMoodsBetween(long startTime, long endTime);

    @Query("SELECT * FROM mood_records WHERE mood_type = :moodType ORDER BY created_at DESC")
    LiveData<List<MoodRecord>> getMoodsByType(String moodType);

    @Query("SELECT AVG(mood_score) FROM mood_records WHERE created_at >= :startTime")
    float getAverageMoodScore(long startTime);

    @Query("SELECT COUNT(*) FROM mood_records WHERE mood_type = :moodType")
    int getMoodCountByType(String moodType);

    @Query("SELECT * FROM mood_records WHERE id = :id")
    MoodRecord getMoodById(int id);

    @Insert
    long insert(MoodRecord mood);

    @Update
    void update(MoodRecord mood);

    @Delete
    void delete(MoodRecord mood);

    @Query("DELETE FROM mood_records")
    void deleteAllMoods();

    @Query("SELECT * FROM mood_records ORDER BY created_at DESC")
    List<MoodRecord> getAllMoodRecordsSync();
}
