package com.mindspace.app.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mindspace.app.data.model.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Insert
    long insert(Comment comment);

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY createdAt DESC")
    List<Comment> getCommentsByPost(int postId);

    @Query("SELECT * FROM comments WHERE isReported = 1 ORDER BY createdAt DESC")
    List<Comment> getReportedComments();

    @Query("DELETE FROM comments WHERE id = :id")
    void delete(int id);
}
