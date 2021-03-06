package com.madu.to_doapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskHolder> {
    private OnItemClickListener onItemClickListener;
    private OnCheckBoxClickListener onCheckBoxClickListener;

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            boolean isTitleSame = oldItem.getTitle().equals(newItem.getTitle());
            boolean isDescriptionSame = oldItem.getDescription().equals(newItem.getDescription());
            boolean isPrioritySame = Objects.equals(oldItem.getPriority(), newItem.getPriority());

            return isTitleSame && isDescriptionSame && isPrioritySame;
        }
    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = getItem(position);
        holder.textViewTitle.setText(task.getTitle());
        holder.textViewDescription.setText(task.getDescription());
        switch (task.getPriority()){
            case 1:
                holder.imageViewPriority.setColorFilter(ContextCompat.getColor(holder.imageViewPriority.getContext(), R.color.green_priority));
                break;
            case 2:
                holder.imageViewPriority.setColorFilter(ContextCompat.getColor(holder.imageViewPriority.getContext(), R.color.yellow_priority));
                break;
            case 3:
                holder.imageViewPriority.setColorFilter(ContextCompat.getColor(holder.imageViewPriority.getContext(), R.color.red_priority));
                break;
        }
        holder.checkBoxIsDone.setChecked(task.getDone());
    }

    public Task getTaskAt(int position){
        return getItem(position);
    }

    class TaskHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private ImageView imageViewPriority;
        private CheckBox checkBoxIsDone;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            imageViewPriority = itemView.findViewById(R.id.image_view_priority);
            checkBoxIsDone = itemView.findViewById(R.id.checkBox_isDone);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION){
                    onItemClickListener.onItemClick(getItem(position));
                }
            });

            checkBoxIsDone.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (onCheckBoxClickListener != null && position != RecyclerView.NO_POSITION){
                    onCheckBoxClickListener.onCheckBoxClick(getItem(position), checkBoxIsDone.isChecked());
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public interface OnCheckBoxClickListener{
        void onCheckBoxClick(Task task, Boolean isDone);
    }

    public void setOnCheckBoxClickListener(OnCheckBoxClickListener listener){
        this.onCheckBoxClickListener = listener;
    }
}
