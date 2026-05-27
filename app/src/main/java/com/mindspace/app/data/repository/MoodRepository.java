package com.mindspace.app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mindspace.app.data.local.AppDatabase;
import com.mindspace.app.data.local.MoodDao;
import com.mindspace.app.data.model.MoodRecord;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MoodRepository {
    
    private final MoodDao moodDao;
    private final LiveData<List<MoodRecord>> allMoods;
    private final ExecutorService executorService;

    public MoodRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        moodDao = database.moodDao();
        allMoods = moodDao.getAllMoods();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<MoodRecord>> getAllMoods() {
        return allMoods;
    }

    public LiveData<List<MoodRecord>> getRecentMoods(int limit) {
        return moodDao.getRecentMoods(limit);
    }

    public LiveData<List<MoodRecord>> getMoodsBetween(long startTime, long endTime) {
        return moodDao.getMoodsBetween(startTime, endTime);
    }

    public LiveData<List<MoodRecord>> getMoodsByType(String moodType) {
        return moodDao.getMoodsByType(moodType);
    }

    public void getMoodsSince(long startTime, MoodCallback callback) {
        executorService.execute(() -> {
            List<MoodRecord> moods = moodDao.getMoodsSince(startTime);
            callback.onSuccess(moods);
        });
    }

    public void getAverageMoodScore(long startTime, AverageScoreCallback callback) {
        executorService.execute(() -> {
            float average = moodDao.getAverageMoodScore(startTime);
            callback.onSuccess(average);
        });
    }

    public void getMoodCountByType(String moodType, CountCallback callback) {
        executorService.execute(() -> {
            int count = moodDao.getMoodCountByType(moodType);
            callback.onSuccess(count);
        });
    }

    public void insert(MoodRecord mood) {
        executorService.execute(() -> moodDao.insert(mood));
    }

    public void update(MoodRecord mood) {
        executorService.execute(() -> moodDao.update(mood));
    }

    public void delete(MoodRecord mood) {
        executorService.execute(() -> moodDao.delete(mood));
    }

    public void deleteAllMoods() {
        executorService.execute(moodDao::deleteAllMoods);
    }

    public interface MoodCallback {
        void onSuccess(List<MoodRecord> moods);
    }

    public interface AverageScoreCallback {
        void onSuccess(float average);
    }

    public interface CountCallback {
        void onSuccess(int count);
    }
}
