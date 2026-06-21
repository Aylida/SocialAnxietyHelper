package com.example.socialanxietyhelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Gorev> gorevList;
    private final OnTaskCheckedListener listener;

    public interface OnTaskCheckedListener {
        void onTaskCheckedChanged(boolean isChecked, int index);
    }

    public TaskAdapter(List<Gorev> gorevList, OnTaskCheckedListener listener) {
        this.gorevList = gorevList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Gorev gorev = gorevList.get(position);
        holder.txtTaskTitle.setText(gorev.getBaslik());
        holder.txtStars.setText(gorev.getZorluk() * 5 + " ★");

        if (gorev.isKilitli()) {
            holder.imgTaskStatus.setImageResource(R.drawable.ic_task_pending);
            holder.itemView.setAlpha(0.5f);
        } else if (gorev.isTamamlandi()) {
            holder.imgTaskStatus.setImageResource(R.drawable.ic_task_done);
            holder.itemView.setAlpha(1f);
            holder.imgTaskStatus.animate().rotation(360).setDuration(400).start();
        } else {
            // Pending görevler için boş daire
            holder.imgTaskStatus.setImageResource(R.drawable.ic_task_pending);
            holder.itemView.setAlpha(1f);
        }

        holder.itemView.setOnClickListener(v -> {
            if (!gorev.isKilitli()) {
                gorev.setTamamlandi(!gorev.isTamamlandi());
                notifyItemChanged(position);
                listener.onTaskCheckedChanged(gorev.isTamamlandi(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gorevList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTaskStatus;
        TextView txtTaskTitle, txtStars;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTaskStatus = itemView.findViewById(R.id.imgTaskStatus);
            txtTaskTitle = itemView.findViewById(R.id.txtTaskTitle);
            txtStars = itemView.findViewById(R.id.txtStars);
        }
    }
}
