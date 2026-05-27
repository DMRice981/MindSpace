package com.mindspace.app.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "mood_records")
public class MoodRecord {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "mood_type")
    private String moodType;
    
    @ColumnInfo(name = "mood_score")
    private int moodScore;
    
    private String content;
    
    @ColumnInfo(name = "created_at")
    private long createdAt;
    
    @ColumnInfo(name = "weather")
    private String weather;

    public MoodRecord() {
    }

    @Ignore
    public MoodRecord(String moodType, int moodScore, String content) {
        this.moodType = moodType;
        this.moodScore = moodScore;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.weather = null;
    }

    @Ignore
    public MoodRecord(String moodType, int moodScore, String content, String weather) {
        this.moodType = moodType;
        this.moodScore = moodScore;
        this.content = content;
        this.weather = weather;
        this.createdAt = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoodType() {
        return moodType;
    }

    public void setMoodType(String moodType) {
        this.moodType = moodType;
    }

    public int getMoodScore() {
        return moodScore;
    }

    public void setMoodScore(int moodScore) {
        this.moodScore = moodScore;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public static class MoodType {
        public static final String HAPPY = "HAPPY";
        public static final String EXCITED = "EXCITED";
        public static final String NORMAL = "NORMAL";
        public static final String SAD = "SAD";
        public static final String ANGRY = "ANGRY";
    }

    public static class Weather {
        public static final String SUNNY = "SUNNY";
        public static final String CLOUDY = "CLOUDY";
        public static final String RAINY = "RAINY";
        public static final String SNOWY = "SNOWY";
        public static final String WINDY = "WINDY";
    }
}
