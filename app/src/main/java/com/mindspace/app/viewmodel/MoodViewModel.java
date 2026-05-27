package com.mindspace.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mindspace.app.data.model.MoodRecord;
import com.mindspace.app.data.repository.MoodRepository;

import java.util.List;

public class MoodViewModel extends AndroidViewModel {
    
    private final MoodRepository repository;
    private final LiveData<List<MoodRecord>> allMoods;
    private final MutableLiveData<String> selectedMoodType = new MutableLiveData<>();
    private final MutableLiveData<String> currentMoodContent = new MutableLiveData<>();

    public MoodViewModel(@NonNull Application application) {
        super(application);
        repository = new MoodRepository(application);
        allMoods = repository.getAllMoods();
    }

    public LiveData<List<MoodRecord>> getAllMoods() {
        return allMoods;
    }

    public LiveData<List<MoodRecord>> getRecentMoods(int limit) {
        return repository.getRecentMoods(limit);
    }

    public LiveData<List<MoodRecord>> getMoodsBetween(long startTime, long endTime) {
        return repository.getMoodsBetween(startTime, endTime);
    }

    public LiveData<List<MoodRecord>> getMoodsByType(String moodType) {
        return repository.getMoodsByType(moodType);
    }

    public LiveData<String> getSelectedMoodType() {
        return selectedMoodType;
    }

    public void setSelectedMoodType(String moodType) {
        selectedMoodType.setValue(moodType);
    }

    public LiveData<String> getCurrentMoodContent() {
        return currentMoodContent;
    }

    public void setCurrentMoodContent(String content) {
        currentMoodContent.setValue(content);
    }

    public void insert(MoodRecord mood) {
        repository.insert(mood);
    }

    public void update(MoodRecord mood) {
        repository.update(mood);
    }

    public void delete(MoodRecord mood) {
        repository.delete(mood);
    }

    public void deleteAllMoods() {
        repository.deleteAllMoods();
    }

    public void saveMood(String moodType, String content) {
        int score = getScoreForMoodType(moodType);
        MoodRecord mood = new MoodRecord(moodType, score, content);
        insert(mood);
    }

    private int getScoreForMoodType(String moodType) {
        switch (moodType) {
            case MoodRecord.MoodType.HAPPY:
            case MoodRecord.MoodType.EXCITED:
                return 5;
            case MoodRecord.MoodType.NORMAL:
                return 3;
            case MoodRecord.MoodType.SAD:
                return 2;
            case MoodRecord.MoodType.ANGRY:
                return 1;
            default:
                return 3;
        }
    }

    public void getAverageMoodScore(long startTime, MoodRepository.AverageScoreCallback callback) {
        repository.getAverageMoodScore(startTime, callback);
    }

    public void getMoodCountByType(String moodType, MoodRepository.CountCallback callback) {
        repository.getMoodCountByType(moodType, callback);
    }
}
