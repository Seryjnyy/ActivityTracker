package com.wojcik.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.TextView;

import com.wojcik.runningtracker.room.ExerciseEntity;
import com.wojcik.runningtracker.room.Repository;
import com.wojcik.runningtracker.utility.Calculate;
import com.wojcik.runningtracker.utility.TextFormatter;

public class InspectExercise extends AppCompatActivity {
    private final int NO_ID = -99;
    private InspectViewModel inspectViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_exercise);
        Repository repo;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(getIntent().hasExtra("id")){

            int id = getIntent().getIntExtra("id", NO_ID);
            if(id == NO_ID){
                // TODO : id wasn't passed through
                Log.d("no", "no id passed");
            }

            Log.d("gets", "here");
            inspectViewModel = new ViewModelProvider(this).get(InspectViewModel.class);
            inspectViewModel.getExercise(id).observe(this, exerciseEntity -> {
                // update view
                TextView typeText = findViewById(R.id.inspectTypeText);
                TextView distanceText = findViewById(R.id.inspectDistanceText);
                TextView timeText = findViewById(R.id.inspectTimeText);
                TextView avgPaceText = findViewById(R.id.inspectAvgPaceText);

                TextView opinionText = findViewById(R.id.inspectOpinionText);
                TextView weatherText = findViewById(R.id.inspectWeatherText);
                TextView dateText = findViewById(R.id.inspectDateText);

                typeText.setText(exerciseEntity.getType());
                distanceText.setText(TextFormatter.formatDistance(exerciseEntity.getDistance()));
                timeText.setText(TextFormatter.formatTime(exerciseEntity.getTime()));
                avgPaceText.setText(TextFormatter.formatAvgPace(Calculate.calculateAvgPace(exerciseEntity.getTime(), exerciseEntity.getDistance())));

                opinionText.setText(exerciseEntity.getOpinion());
                weatherText.setText(exerciseEntity.getWeather());
                dateText.setText(TextFormatter.formatDate(exerciseEntity.getDay(), exerciseEntity.getMonth(), exerciseEntity.getYear()));

            });

        }else{
            // TODO : display error
            Log.d("no", "no");
        }
    }
}