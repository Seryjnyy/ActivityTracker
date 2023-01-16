package com.wojcik.runningtracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.wojcik.runningtracker.room.ExerciseEntity;
import com.wojcik.runningtracker.room.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private LiveData<List<ExerciseEntity>> allExercises;
    private LiveData<List<ExerciseEntity>> allExercisesThisYear;

    private LiveData<List<ExerciseEntity>> topThreeLongestDistances;
    private LiveData<List<ExerciseEntity>> topThreeLongestTimes;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        Repository repo = new Repository(application);

        allExercises = repo.getAllExercises();
        allExercisesThisYear = repo.getAllExercisesInYear(LocalDateTime.now().getYear());

        topThreeLongestDistances = repo.getAllExercises();
        topThreeLongestTimes = repo.getAllExercises();
    }

    public LiveData<List<ExerciseEntity>> getAllExercises() {
        return allExercises;
    }

    public LiveData<List<ExerciseEntity>> getAllExercisesThisYear() {
        return allExercisesThisYear;
    }

    public LiveData<List<ExerciseEntity>> getTopThreeLongestDistances() {
        return topThreeLongestDistances;
    }

    public LiveData<List<ExerciseEntity>> getTopThreeLongestTimes() {
        return topThreeLongestTimes;
    }
}
