package com.wojcik.runningtracker.annotation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.wojcik.runningtracker.MainActivity;
import com.wojcik.runningtracker.R;
import com.wojcik.runningtracker.utility.TextFormatter;

import java.time.LocalDateTime;
import java.util.List;

public class AnnotatingActivity extends AppCompatActivity {

    private AnnotatingViewModel annotatingViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotating_exercise_data);
//
//        annotatingViewModel = new ViewModelProvider(this).get(AnnotatingViewModel.class);
//        annotatingViewModel.setDistanceTraveled(savedInstanceState.getFloat("distance"));
//        annotatingViewModel.setTotalTime(savedInstanceState.getFloat("time"));
//        annotatingViewModel.setDate();


        // TODO : MOCK DATA, replace with values from the previous activity
        annotatingViewModel.setDistanceTraveled(257f);
        annotatingViewModel.setTotalTime(23f);
        annotatingViewModel.setStartDateTime(LocalDateTime.now());

        // setting text
        TextView timeText = findViewById(R.id.timeText);
        TextView totalDistanceText = findViewById(R.id.totalDistanceText);
        TextView startDateTimeText = findViewById(R.id.startDateTimeText);

        timeText.setText("Total time: " + annotatingViewModel.getTotalTime());
        totalDistanceText.setText("Total distance: " + annotatingViewModel.getStartDateTime());
        startDateTimeText.setText("Start Date: " + annotatingViewModel.getStartDateTime());

        ChipGroup opinionGroup = findViewById(R.id.opinionChipGroup);
        ChipGroup typeGroup = findViewById(R.id.typeChipGroup);
        ChipGroup weatherGroup = findViewById(R.id.weatherChipGroup);

        for(int i = 0; i < opinionGroup.getChildCount(); i++){
            Chip chip = (Chip) opinionGroup.getChildAt(i);
            chip.setText(TextFormatter.getEmojiFromName(chip.getText().toString()));
        }

        for(int i = 0; i < typeGroup.getChildCount(); i++){
            Chip chip = (Chip) typeGroup.getChildAt(i);
            chip.setText(TextFormatter.getEmojiFromName(chip.getText().toString()));
        }

        for(int i = 0; i < weatherGroup.getChildCount(); i++){
            Chip chip = (Chip) weatherGroup.getChildAt(i);
            chip.setText(TextFormatter.getEmojiFromName(chip.getText().toString()));
        }



        typeGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if(checkedIds.isEmpty())
                return;

            String text = ((Chip)group.findViewById(group.getCheckedChipId())).getText().toString();

            annotatingViewModel.setType(text);
        });

        opinionGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if(checkedIds.isEmpty())
                return;

            String text = ((Chip)group.findViewById(group.getCheckedChipId())).getText().toString();

            annotatingViewModel.setOpinion(text);
        });

        weatherGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if(checkedIds.isEmpty())
                return;

            String text = ((Chip)group.findViewById(group.getCheckedChipId())).getText().toString();

            annotatingViewModel.setWeather(text);
        });

    }

    public void saveExerciseData(View view) {

        annotatingViewModel.saveData();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // TODO : temp method remove this, its for seeing content of db
    public void showStuffTemp(View view) {
        annotatingViewModel.showStuff();
    }
}