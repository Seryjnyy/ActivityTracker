package com.wojcik.runningtracker.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// TODO : maybe add note and link to image on phone

@Entity(tableName = "exercise_table")
public class ExerciseEntity {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    // For better sorting and filtering, also represents start date time
    private int year;
    private int month;
    private int day;

    private float distance;
    private float time;
    private float avgPace;

    private String opinion;
    private String weather;
    private String type;

    public ExerciseEntity(int year, int month, int day, float distance, float time, float avgPace, String opinion, String weather, String type) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.opinion = opinion;
        this.weather = weather;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public float getDistance() {
        return distance;
    }

    public float getTime() {
        return time;
    }

    public float getAvgPace() {
        return avgPace;
    }

    public String getOpinion() {
        return opinion;
    }

    public String getWeather() {
        return weather;
    }

    public String getType() {
        return type;
    }


}
