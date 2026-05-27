package com.mindspace.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mindspace.app.data.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY updated_at DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes ORDER BY updated_at DESC LIMIT :limit")
    LiveData<List<Note>> getRecentNotes(int limit);

    @Query("SELECT * FROM notes WHERE category = :category ORDER BY updated_at DESC")
    LiveData<List<Note>> getNotesByCategory(String category);

    @Query("SELECT * FROM notes WHERE color_tag = :colorTag ORDER BY updated_at DESC")
    LiveData<List<Note>> getNotesByColor(String colorTag);

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' ORDER BY updated_at DESC")
    LiveData<List<Note>> searchNotes(String keyword);

    @Query("SELECT * FROM notes WHERE id = :id")
    Note getNoteById(int id);

    @Query("SELECT COUNT(*) FROM notes WHERE category = :category")
    int getNoteCountByCategory(String category);

    @Query("SELECT COUNT(*) FROM notes")
    int getTotalNoteCount();

    @Insert
    long insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM notes")
    void deleteAllNotes();
}
