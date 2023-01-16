package com.wojcik.runningtracker.annotation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.wojcik.runningtracker.room.ExerciseEntity;
import com.wojcik.runningtracker.room.Repository;
import com.wojcik.runningtracker.utility.Calculate;

import java.time.LocalDateTime;

// TODO : need to save data to bundle
// TODO : might also need to update UI with values
public class AnnotatingViewModel extends AndroidViewModel {
    private float distanceTraveled = 0;
    private float totalTime = 0;
    private int year;
    private int month;
    private int day;
    private String opinion = "";
    private String weather = "";
    private String type = "";

    private Repository repository;

    public void setDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public AnnotatingViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }

    public void saveData(){
        repository.insert(new ExerciseEntity(
                year,
                month,
                day,
                distanceTraveled + 2330,
                totalTime,
                Calculate.calculateAvgPace(totalTime, distanceTraveled),
                opinion,
                weather,
                type));
    }

    public void showStuff(){
        if(repository.getAllExercises().getValue() == null)
            return;

        Log.d("the stuff", ""+repository.getAllExercises().getValue().toString());
    }


    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDistanceTraveled(float distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }


    public float getDistanceTraveled() {
        return distanceTraveled;
    }

    public float getTotalTime() {
        return totalTime;
    }
}
