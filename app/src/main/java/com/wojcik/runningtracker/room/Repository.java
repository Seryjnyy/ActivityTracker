package com.wojcik.runningtracker.room;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.wojcik.runningtracker.ExerciseTrackingService;

import java.util.List;

public class Repository {
    private ExerciseDao exerciseDao;
    private LiveData<List<ExerciseEntity>> allExercises;

    public Repository(Application application) {
        setUp(ExerciseDatabase.getDatabase(application));
    }

    public Repository(Context context){
        setUp(ExerciseDatabase.getDatabase(context));
    }

    private void setUp(ExerciseDatabase db){
        exerciseDao = db.exerciseDao();
        allExercises = exerciseDao.getAllExercises();
    }


    public void insert(ExerciseEntity exerciseEntity){
        ExerciseDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDao.insert(exerciseEntity);
        });
    }

    public LiveData<List<ExerciseEntity>> getAllExercises(){
        return allExercises;
    }

    public LiveData<List<ExerciseEntity>> getAllExercisesInYear(int year){
        return exerciseDao.getAllExercisesInYear(year);
    }

    public LiveData<ExerciseEntity> getExercise(int id){
        return exerciseDao.getExercise(id);
    }

    public ExerciseEntity getExerciseDirect(int id){
        return exerciseDao.getExerciseDirect(id);
    }

    public List<ExerciseEntity> getAllExercisesDirect(){
        return exerciseDao.getAllExercisesDirect();
    }

}
