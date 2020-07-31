package com.thoughtworks.todo_list.ui.task;

import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.task.model.TaskResponse;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<TaskResponse> itemDataList;

    public MyAdapter(List<TaskResponse> itemDataList) {
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
        TaskResponse itemData = itemDataList.get(position);
        Date date = itemData.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        holder.setTitleText(itemData.getTitle());
        if(itemData.getChecked()){
            holder.title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.setDateText(simpleDateFormat.format(date));
        holder.setIsChecked(itemData.getChecked());
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, date;
        private CheckBox isChecked;

        public void setTitleText(String title) {
            this.title.setText(title);
        }


        public void setIsChecked(Boolean isChecked) {
            this.isChecked.setChecked(isChecked);
        }

        public void setDateText(String date) {
            this.date.setText(date);
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.item_title);
            this.date = itemView.findViewById(R.id.item_date);
            this.isChecked = itemView.findViewById(R.id.item_check_box);
        }
    }
}
