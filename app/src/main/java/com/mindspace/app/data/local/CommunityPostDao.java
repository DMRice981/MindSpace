package com.mindspace.app.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mindspace.app.data.model.CommunityPost;

import java.util.List;

@Dao
public interface CommunityPostDao {
    @Insert
    long insert(CommunityPost post);

    @Query("SELECT * FROM community_posts ORDER BY createdAt DESC")
    List<CommunityPost> getAllPosts();

    @Query("SELECT * FROM community_posts WHERE isReported = 0 ORDER BY createdAt DESC")
    List<CommunityPost> getActivePosts();

    @Query("SELECT * FROM community_posts WHERE userId = :userId ORDER BY createdAt DESC")
    List<CommunityPost> getUserPosts(int userId);

    @Query("SELECT * FROM community_posts WHERE isReported = 1 ORDER BY createdAt DESC")
    List<CommunityPost> getReportedPosts();

    @Update
    void update(CommunityPost post);

    @Query("DELETE FROM community_posts WHERE id = :id")
    void delete(int id);

    @Query("UPDATE community_posts SET likeCount = likeCount + 1 WHERE id = :id")
    void incrementLike(int id);

    @Query("UPDATE community_posts SET commentCount = commentCount + 1 WHERE id = :id")
    void incrementCommentCount(int id);
}
