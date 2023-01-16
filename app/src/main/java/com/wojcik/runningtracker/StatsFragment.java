package com.wojcik.runningtracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wojcik.runningtracker.room.ExerciseEntity;
import com.wojcik.runningtracker.utility.TextFormatter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.getAllExercisesThisYear().observe(getViewLifecycleOwner(), exerciseEntities -> {
            setUpStats(exerciseEntities);

        });
    }

    private void setUpStats(List<ExerciseEntity> exerciseEntities) {
        updateStatsText(exerciseEntities,
                ((TextView) getView().findViewById(R.id.statsYearTimeText)),
                ((TextView) getView().findViewById(R.id.statsYearDistanceText)));

        updateStatsText(exerciseEntities.stream().filter(exerciseEntity -> {
                    if (exerciseEntity.getMonth() == LocalDateTime.now().getMonth().getValue()) {
                        return true;
                    } else {
                        return false;
                    }
                }).collect(Collectors.toList()),
                ((TextView) getView().findViewById(R.id.statsMonthTimeText)),
                ((TextView) getView().findViewById(R.id.statsMonthDistanceText)));

        updateStatsText(exerciseEntities.stream().filter(exerciseEntity -> {
                    if (exerciseEntity.getDay() == LocalDateTime.now().getDayOfMonth()) {
                        return true;
                    } else {
                        return false;
                    }
                }).collect(Collectors.toList()),
                ((TextView) getView().findViewById(R.id.statsTodayTimeText)),
                ((TextView) getView().findViewById(R.id.statsTodayDistanceText)));
    }

    private void updateStatsText(List<ExerciseEntity> exerciseEntities, TextView distanceText, TextView timeText) {
        float totalDistance = 0;
        float totalTime = 0;
        for (ExerciseEntity exerciseEntity : exerciseEntities) {
            totalDistance += exerciseEntity.getDistance();
            totalTime += exerciseEntity.getTime();
        }

        distanceText.setText("Total distance: " + TextFormatter.formatDistance(totalDistance));
        timeText.setText("Total time: " + TextFormatter.formatTime(totalTime));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }
}