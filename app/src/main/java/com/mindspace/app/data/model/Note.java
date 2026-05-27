package com.mindspace.app.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "title")
    private String title;
    
    @ColumnInfo(name = "content")
    private String content;
    
    @ColumnInfo(name = "category")
    private String category;
    
    @ColumnInfo(name = "color_tag")
    private String colorTag;
    
    @ColumnInfo(name = "created_at")
    private long createdAt;
    
    @ColumnInfo(name = "updated_at")
    private long updatedAt;

    public Note() {
    }

    @Ignore
    public Note(String title, String content, String category, String colorTag) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.colorTag = colorTag;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColorTag() {
        return colorTag;
    }

    public void setColorTag(String colorTag) {
        this.colorTag = colorTag;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Category {
        public static final String WORK = "WORK";
        public static final String STUDY = "STUDY";
        public static final String LIFE = "LIFE";
        public static final String IDEA = "IDEA";
    }

    public static class ColorTag {
        public static final String PINK = "#FF6584";
        public static final String PURPLE = "#6C63FF";
        public static final String GREEN = "#4CAF50";
        public static final String ORANGE = "#FF9800";
        public static final String BLUE = "#2196F3";
    }
}
