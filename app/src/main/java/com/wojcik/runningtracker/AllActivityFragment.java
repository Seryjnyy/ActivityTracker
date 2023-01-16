package com.wojcik.runningtracker;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wojcik.runningtracker.room.ExerciseEntity;
import com.wojcik.runningtracker.room.Repository;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllActivityFragment extends Fragment implements ExerciseRecyclerViewInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllActivityFragment newInstance(String param1, String param2) {
        AllActivityFragment fragment = new AllActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private List<ExerciseEntity> exerciseEntityList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.purple_500)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        ExerciseRecyclerViewAdapter adapter = new ExerciseRecyclerViewAdapter(requireActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));


        MainActivityViewModel mainActivityViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.getAllExercises().observe(getViewLifecycleOwner(), exerciseEntities -> {
            exerciseEntityList = exerciseEntities;
            adapter.updateExerciseList(exerciseEntityList, this);
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_activity, container, false);
    }

    @Override
    public void onItemClick(int position) {
        if(exerciseEntityList == null)
            return;

        Intent intent = new Intent(requireActivity(), InspectExercise.class);
        intent.putExtra("id", exerciseEntityList.get(position).getId());
        startActivity(intent);
    }
}