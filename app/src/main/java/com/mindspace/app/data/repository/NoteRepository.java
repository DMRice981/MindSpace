package com.mindspace.app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mindspace.app.data.local.AppDatabase;
import com.mindspace.app.data.local.NoteDao;
import com.mindspace.app.data.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    
    private final NoteDao noteDao;
    private final LiveData<List<Note>> allNotes;
    private final ExecutorService executorService;

    public NoteRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getRecentNotes(int limit) {
        return noteDao.getRecentNotes(limit);
    }

    public LiveData<List<Note>> getNotesByCategory(String category) {
        return noteDao.getNotesByCategory(category);
    }

    public LiveData<List<Note>> getNotesByColor(String colorTag) {
        return noteDao.getNotesByColor(colorTag);
    }

    public LiveData<List<Note>> searchNotes(String keyword) {
        return noteDao.searchNotes(keyword);
    }

    public void getNoteById(int id, NoteCallback callback) {
        executorService.execute(() -> {
            Note note = noteDao.getNoteById(id);
            callback.onSuccess(note);
        });
    }

    public void getNoteCountByCategory(String category, CountCallback callback) {
        executorService.execute(() -> {
            int count = noteDao.getNoteCountByCategory(category);
            callback.onSuccess(count);
        });
    }

    public void getTotalNoteCount(CountCallback callback) {
        executorService.execute(() -> {
            int count = noteDao.getTotalNoteCount();
            callback.onSuccess(count);
        });
    }

    public void insert(Note note) {
        executorService.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        executorService.execute(() -> {
            note.setUpdatedAt(System.currentTimeMillis());
            noteDao.update(note);
        });
    }

    public void delete(Note note) {
        executorService.execute(() -> noteDao.delete(note));
    }

    public void deleteAllNotes() {
        executorService.execute(noteDao::deleteAllNotes);
    }

    public interface NoteCallback {
        void onSuccess(Note note);
    }

    public interface CountCallback {
        void onSuccess(int count);
    }
}
