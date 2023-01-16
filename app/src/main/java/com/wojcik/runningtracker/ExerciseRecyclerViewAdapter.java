package com.wojcik.runningtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wojcik.runningtracker.room.ExerciseDatabase;
import com.wojcik.runningtracker.room.ExerciseEntity;
import com.wojcik.runningtracker.utility.TextFormatter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ExerciseRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseRecyclerViewAdapter.ExerciseViewHolder> {
    private Context context;
    private List<ExerciseEntity> exerciseList;

    private ExerciseRecyclerViewInterface exerciseRecyclerViewInterface;

    public ExerciseRecyclerViewAdapter(Context context) {
        this.context = context;
        this.exerciseList = new ArrayList<>();
    }

    public void updateExerciseList(List<ExerciseEntity> exerciseList,
                                   ExerciseRecyclerViewInterface exerciseRecyclerViewInterface){
        this.exerciseList.clear();
        this.exerciseList = exerciseList;
        this.exerciseRecyclerViewInterface = exerciseRecyclerViewInterface;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exercise_recycler_view_row, parent, false);

        return new ExerciseViewHolder(view, exerciseRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseEntity exercise = exerciseList.get(position);

        // need formatting
        holder.distanceText.setText(TextFormatter.formatDistance(exercise.getDistance()));

        holder.timeText.setText(TextFormatter.formatTime(exercise.getTime()));

        holder.opinionText.setText(exercise.getOpinion());
        holder.typeText.setText(exercise.getType());
        holder.weatherText.setText(exercise.getWeather());

        String fullDate = "" + exercise.getDay() + "/" + exercise.getMonth() + "/" + exercise.getYear();
        holder.startDateText.setText(fullDate);
    }


    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder{
        private TextView typeText;

        private TextView distanceText;
        private TextView timeText;

        private TextView opinionText;
        private TextView weatherText;

        private TextView startDateText;

        public ExerciseViewHolder(@NonNull View itemView, ExerciseRecyclerViewInterface exerciseRecyclerViewInterface) {
            super(itemView);

            typeText = itemView.findViewById(R.id.exerciseTypeText);
            distanceText = itemView.findViewById(R.id.exerciseDistanceText);
            timeText = itemView.findViewById(R.id.exerciseTimeText);
            opinionText = itemView.findViewById(R.id.exerciseOpinionText);
            weatherText = itemView.findViewById(R.id.exerciseWeatherText);
            startDateText = itemView.findViewById(R.id.exerciseDateText);


            itemView.setOnClickListener(view -> {
                if(exerciseRecyclerViewInterface == null)
                    return;

                int position = getAdapterPosition();
                if(position == RecyclerView.NO_POSITION)
                    return;

                exerciseRecyclerViewInterface.onItemClick(position);
            });
        }
    }
}
