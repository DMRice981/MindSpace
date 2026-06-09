package com.mindspace.app.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "community_posts")
public class CommunityPost {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String username;
    private String content;
    private String mood;
    private String tags;
    private long createdAt;
    private int likeCount;
    private int commentCount;
    private boolean isReported;

    public CommunityPost() {
    }

    @Ignore
    public CommunityPost(int userId, String username, String content, String mood) {
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.mood = mood;
        this.createdAt = System.currentTimeMillis();
        this.likeCount = 0;
        this.commentCount = 0;
        this.isReported = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }
}
