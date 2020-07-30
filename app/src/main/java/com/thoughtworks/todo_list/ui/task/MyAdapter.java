package com.thoughtworks.todo_list.ui.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.task.Task;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Task> itemDataList;

    public MyAdapter(List<Task> itemDataList) {
        this.itemDataList = itemDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task itemData = itemDataList.get(position);
        holder.setTitleText(itemData.title);
        holder.setNumberText(String.valueOf(itemData.number));
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title, number;

        public void setTitleText(String title) {
            this.title.setText(title);
        }

        public void setNumberText(String number) {
            this.number.setText(number);
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.item_content);
            this.number = itemView.findViewById(R.id.item_date);
        }
    }
}
