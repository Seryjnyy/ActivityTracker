package com.wojcik.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wojcik.runningtracker.annotation.AnnotatingActivity;
import com.wojcik.runningtracker.utility.TextFormatter;

import java.time.LocalDateTime;

public class ExerciseActivity extends AppCompatActivity {
    private ActiveExerciseViewModel activeExerciseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        // TODO : im not sure about this, it doesn't pass in a factory like the examples do also not much info about this
        activeExerciseViewModel = new ViewModelProvider(this).get(ActiveExerciseViewModel.class);
        activeExerciseViewModel.startTrackingService();

        final Observer<Float> distanceTraveledObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float distance) {
                ((TextView)findViewById(R.id.distanceTraveledText)).setText(TextFormatter.formatDistance(distance));
            }
        };

        final Observer<Float> totalTimeObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float time) {
                ((TextView)findViewById(R.id.totalTimeText)).setText(TextFormatter.formatTime(time));
            }
        };

        final Observer<Float> avgPaceObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float avgPace) {
                ((TextView)findViewById(R.id.avgPaceText)).setText("" + avgPace);
            }
        };

        activeExerciseViewModel.getDistanceTraveled().observe(this, distanceTraveledObserver);
        activeExerciseViewModel.getTotalTime().observe(this, totalTimeObserver);
        activeExerciseViewModel.getAvgPace().observe(this, avgPaceObserver);
    }

    public void startTrackingService(View view){
//        boolean started = activeExerciseViewModel.startTrackingService();
//
//        if(started){
//            ((Button) view).setEnabled(false);
//            ((Button) findViewById(R.id.stopTracking)).setEnabled(true);
//        }
    }

    public void stopTrackingService(View view){
        activeExerciseViewModel.stopTrackingService(this.getApplication());

        ((Button) view).setEnabled(false);
//        ((Button) findViewById(R.id.startTracking)).setEnabled(true);

        startAnnotatingActivity();
    }

    private void startAnnotatingActivity(){
        Intent intent = new Intent(this, AnnotatingActivity.class);
//        intent.putExtra("distance", activeExerciseViewModel.getDistanceTraveled().getValue());
//        intent.putExtra("time", activeExerciseViewModel.getTotalTime().getValue());
//
//
//        if(activeExerciseViewModel.getStartDateTime() == null){
//            intent.putExtra("year", LocalDateTime.now().getYear());
//            intent.putExtra("month", LocalDateTime.now().getMonth());
//            intent.putExtra("day", LocalDateTime.now().getDayOfMonth());
//        }else{
//            intent.putExtra("year", activeExerciseViewModel.getStartDateTime().getYear());
//            intent.putExtra("month", activeExerciseViewModel.getStartDateTime().getMonth().getValue());
//            intent.putExtra("day", activeExerciseViewModel.getStartDateTime().getDayOfMonth());
//        }

        startActivity(intent);
    }

}