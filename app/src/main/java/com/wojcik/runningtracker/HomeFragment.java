package com.wojcik.runningtracker;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wojcik.runningtracker.room.ExerciseEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ExerciseRecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private List<ExerciseEntity> exerciseEntityList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivityViewModel mainActivityViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);


        // TODO : change resource names
        RecyclerView longestDistancesRecyclerView = getView().findViewById(R.id.topDistancesRecyclerView);

        ExerciseRecyclerViewAdapter longestDistanceAdapter = new ExerciseRecyclerViewAdapter(requireActivity());

        longestDistancesRecyclerView.setAdapter(longestDistanceAdapter);
        longestDistancesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(longestDistancesRecyclerView.getContext(), DividerItemDecoration.VERTICAL);

        longestDistancesRecyclerView.addItemDecoration(dividerItemDecoration);

        mainActivityViewModel.getAllExercises().observe(getViewLifecycleOwner(), exerciseEntities -> {
            exerciseEntityList = exerciseEntities;
            longestDistanceAdapter.updateExerciseList(exerciseEntityList.stream().sorted(Comparator.comparing(ExerciseEntity::getDistance).reversed()).limit(3).collect(Collectors.toList()), this);
        });

        ((Button) getView().findViewById(R.id.trackExercise)).setOnClickListener(view1 -> {
            trackLocation();
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onItemClick(int position) {
        if(exerciseEntityList == null)
            return;

        Intent intent = new Intent(requireActivity(), InspectExercise.class);
        intent.putExtra("id", exerciseEntityList.get(position).getId());
        startActivity(intent);
    }

    private void trackLocation() {
        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startRunningActivity();
        }else{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startRunningActivity(){
        Intent intent = new Intent(requireActivity(), ExerciseActivity.class);
        startActivity(intent);
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                // Check if location permission was granted and that it was fine location, not coarse
                if(isGranted && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    startRunningActivity();
                }else{
                    weRequireFineLocation();
                }
            });

    private void weRequireFineLocation(){
        // explain to user that the feature is unavailable because the feature requires
        // a permission that the user has denied. but don't link them to settings or anything

        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(requireActivity());
        alertDialogBuilder
                .setTitle("Location access")
                .setMessage("Sorry, we require precise location permission to accurately track your activity.");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialogInterface, i) -> {});
        alertDialog.show();
    }

}