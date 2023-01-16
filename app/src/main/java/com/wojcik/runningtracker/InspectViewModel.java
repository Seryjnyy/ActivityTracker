package com.wojcik.runningtracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wojcik.runningtracker.room.ExerciseEntity;
import com.wojcik.runningtracker.room.Repository;

public class InspectViewModel extends AndroidViewModel {
    private LiveData<ExerciseEntity> exercise;
    private Application application;

    public InspectViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<ExerciseEntity> getExercise(int id) {
        if(exercise == null){
            exercise = new Repository(application).getExercise(id);
        }
        return exercise;
    }
}
