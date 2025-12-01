package com.project.healthai.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.healthai.R;
import com.project.healthai.data.local.entities.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises = new ArrayList<>();
    private final Context context;
    private final OnExerciseClickListener listener;

    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise);
    }

    public ExerciseAdapter(Context context, OnExerciseClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_exercise_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final ImageView gifImageView;
        private final TextView nameTextView;
        private final TextView targetTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            gifImageView = itemView.findViewById(R.id.exerciseGif);
            nameTextView = itemView.findViewById(R.id.exerciseName);
            targetTextView = itemView.findViewById(R.id.exerciseTarget);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onExerciseClick(exercises.get(position));
                }
            });
        }

        public void bind(Exercise exercise) {
            nameTextView.setText(exercise.getName());
            targetTextView.setText("Target: " + exercise.getTarget());

            if (exercise.getGifUrl() != null && !exercise.getGifUrl().isEmpty()) {
                Log.d("ExerciseAdapter", "Loading GIF for: " + exercise.getName());

                Glide.with(context)
                        .asGif()
                        .load(exercise.getGifUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(gifImageView);
            } else {
                gifImageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}
