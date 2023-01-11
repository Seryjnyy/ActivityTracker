package com.wojcik.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExerciseActivity extends AppCompatActivity {
    private ActiveExerciseViewModel activeExerciseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        // TODO : im not sure about this, it doesn't pass in a factory like the examples do also not much info about this
        activeExerciseViewModel = new ViewModelProvider(this).get(ActiveExerciseViewModel.class);
    }

    public void startTrackingService(View view){
        activeExerciseViewModel.startTrackingService();


        ((Button) view).setClickable(false);
        ((Button) findViewById(R.id.stopTracking)).setClickable(true);
    }

    public void stopTrackingService(View view){
        activeExerciseViewModel.stopTrackingService();

        ((Button) view).setClickable(false);
        ((Button) findViewById(R.id.startTracking)).setClickable(true);
    }

}