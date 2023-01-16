package com.wojcik.runningtracker.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ExerciseEntity exerciseEntity);

    @Query("SELECT * FROM exercise_table")
    LiveData<List<ExerciseEntity>> getAllExercises();

    @Query("SELECT * FROM exercise_table WHERE year = :year")
    LiveData<List<ExerciseEntity>> getAllExercisesInYear(int year);

    @Query("SELECT * FROM exercise_table WHERE id = :id")
    LiveData<ExerciseEntity> getExercise(int id);

    @Query("DELETE FROM exercise_table")
    void deleteAll();

    // For ContentProvider, without LiveData
    @Query("SELECT * FROM exercise_table WHERE id = :id")
    ExerciseEntity getExerciseDirect(int id);

    @Query("SELECT * FROM exercise_table")
    List<ExerciseEntity> getAllExercisesDirect();

}
