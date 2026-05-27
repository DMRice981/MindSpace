package com.mindspace.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mindspace.app.data.model.MoodRecord;
import com.mindspace.app.data.model.Note;
import com.mindspace.app.data.repository.MoodRepository;
import com.mindspace.app.data.repository.NoteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataViewModel extends AndroidViewModel {
    
    private final MoodRepository moodRepository;
    private final NoteRepository noteRepository;
    private final MutableLiveData<Map<String, Integer>> moodStatistics = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Integer>> noteStatistics = new MutableLiveData<>();
    private final MutableLiveData<Float> averageMoodScore = new MutableLiveData<>();

    public DataViewModel(@NonNull Application application) {
        super(application);
        moodRepository = new MoodRepository(application);
        noteRepository = new NoteRepository(application);
    }

    public LiveData<List<MoodRecord>> getRecentMoods(int limit) {
        return moodRepository.getRecentMoods(limit);
    }

    public LiveData<List<MoodRecord>> getMoodsBetween(long startTime, long endTime) {
        return moodRepository.getMoodsBetween(startTime, endTime);
    }

    public LiveData<List<Note>> getRecentNotes(int limit) {
        return noteRepository.getRecentNotes(limit);
    }

    public LiveData<Map<String, Integer>> getMoodStatistics() {
        return moodStatistics;
    }

    public LiveData<Map<String, Integer>> getNoteStatistics() {
        return noteStatistics;
    }

    public LiveData<Float> getAverageMoodScore() {
        return averageMoodScore;
    }

    public void loadMoodStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put(MoodRecord.MoodType.HAPPY, 0);
        stats.put(MoodRecord.MoodType.EXCITED, 0);
        stats.put(MoodRecord.MoodType.NORMAL, 0);
        stats.put(MoodRecord.MoodType.SAD, 0);
        stats.put(MoodRecord.MoodType.ANGRY, 0);
        
        final int[] pendingCount = {5};
        
        moodRepository.getMoodCountByType(MoodRecord.MoodType.HAPPY, count -> {
            stats.put(MoodRecord.MoodType.HAPPY, count);
            pendingCount[0]--;
            if (pendingCount[0] == 0) {
                moodStatistics.postValue(stats);
            }
        });
        
        moodRepository.getMoodCountByType(MoodRecord.MoodType.EXCITED, count -> {
            stats.put(MoodRecord.MoodType.EXCITED, count);
            pendingCount[0]--;
            if (pendingCount[0] == 0) {
                moodStatistics.postValue(stats);
            }
        });
        
        moodRepository.getMoodCountByType(MoodRecord.MoodType.NORMAL, count -> {
            stats.put(MoodRecord.MoodType.NORMAL, count);
            pendingCount[0]--;
            if (pendingCount[0] == 0) {
                moodStatistics.postValue(stats);
            }
        });
        
        moodRepository.getMoodCountByType(MoodRecord.MoodType.SAD, count -> {
            stats.put(MoodRecord.MoodType.SAD, count);
            pendingCount[0]--;
            if (pendingCount[0] == 0) {
                moodStatistics.postValue(stats);
            }
        });
        
        moodRepository.getMoodCountByType(MoodRecord.MoodType.ANGRY, count -> {
            stats.put(MoodRecord.MoodType.ANGRY, count);
            pendingCount[0]--;
            if (pendingCount[0] == 0) {
                moodStatistics.postValue(stats);
            }
        });
    }

    public void loadNoteStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put(Note.Category.WORK, 0);
        stats.put(Note.Category.STUDY, 0);
        stats.put(Note.Category.LIFE, 0);
        stats.put(Note.Category.IDEA, 0);
        
        final int[] pendingCount = {4};
        
        noteRepository.getNoteCountByCategory(Note.Category.WORK, count -> {
            stats.put(Note.Category.WORK, count);
            pendingCount[0]--;
            if (pendingCount[0] == 0) {
                noteStatistics.postValue(stats);
            }
        });
        
        noteRepository.getNoteCountByCategory(Note.Category.STUDY, count -> {
            stats.put(Note.Category.STUDY, count);
            pendingCount[0]--;
            if (pendingCount[0] == 0) {
                noteStatistics.postValue(stats);
            }
        });
        
        noteRepository.getNoteCountByCategory(Note.Category.LIFE, count -> {
            stats.put(Note.Category.LIFE, count);
            pendingCount[0]--;
            if (pendingCount[0] == 0) {
                noteStatistics.postValue(stats);
            }
        });
        
        noteRepository.getNoteCountByCategory(Note.Category.IDEA, count -> {
            stats.put(Note.Category.IDEA, count);
            pendingCount[0]--;
            if (pendingCount[0] == 0) {
                noteStatistics.postValue(stats);
            }
        });
    }

    public void loadAverageMoodScore(long startTime) {
        moodRepository.getAverageMoodScore(startTime, average -> {
            averageMoodScore.postValue(average);
        });
    }
}
