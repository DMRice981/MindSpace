package com.mindspace.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mindspace.app.data.model.Note;
import com.mindspace.app.data.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    
    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotes;
    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>();
    private final MutableLiveData<String> selectedColor = new MutableLiveData<>();
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getRecentNotes(int limit) {
        return repository.getRecentNotes(limit);
    }

    public LiveData<List<Note>> getNotesByCategory(String category) {
        return repository.getNotesByCategory(category);
    }

    public LiveData<List<Note>> getNotesByColor(String colorTag) {
        return repository.getNotesByColor(colorTag);
    }

    public LiveData<List<Note>> getSearchResults(String keyword) {
        return repository.searchNotes(keyword);
    }

    public LiveData<String> getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String category) {
        selectedCategory.setValue(category);
    }

    public LiveData<String> getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(String color) {
        selectedColor.setValue(color);
    }

    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public void createNote(String title, String content, String category, String colorTag) {
        Note note = new Note(title, content, category, colorTag);
        insert(note);
    }

    public void getNoteById(int id, NoteRepository.NoteCallback callback) {
        repository.getNoteById(id, callback);
    }

    public void getNoteCountByCategory(String category, NoteRepository.CountCallback callback) {
        repository.getNoteCountByCategory(category, callback);
    }

    public void getTotalNoteCount(NoteRepository.CountCallback callback) {
        repository.getTotalNoteCount(callback);
    }
}
