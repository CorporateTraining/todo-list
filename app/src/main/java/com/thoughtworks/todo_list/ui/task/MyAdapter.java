package com.thoughtworks.todo_list.ui.task;

import android.content.Context;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.ui.task.model.TaskModel;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<TaskModel> itemDataList;
    private TaskViewModel taskViewModel;
    private Context context;

    public MyAdapter(List<TaskModel> itemDataList, TaskViewModel taskViewModel) {
        this.itemDataList = itemDataList;
        this.taskViewModel = taskViewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_task_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.checkBox.setTag(position);
        TaskModel itemData = itemDataList.get(position);
        Date date = itemData.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
        holder.setTitleText(itemData.getTitle());
        if (itemData.getChecked()) {
            modifyTextStyle(holder, Paint.STRIKE_THRU_TEXT_FLAG, R.color.colorTextDeleteGray);
        }
        holder.setDateText(simpleDateFormat.format(date));
        holder.setCheckBox(itemData.getChecked());
        holder.checkBox.setOnClickListener(getOnClickListener(holder));
    }

    @NotNull
    private View.OnClickListener getOnClickListener(@NonNull MyViewHolder holder) {
        return view -> {
            int tag = (int) holder.checkBox.getTag();
            TaskModel taskModel = itemDataList.get(tag);
            if (taskModel.getChecked()) {
                modifyCheckBoxStyle(holder, taskModel, false);
                modifyTextStyle(holder, 0, R.color.colorTextGray);
            }else{
                modifyCheckBoxStyle(holder, taskModel, true);
                modifyTextStyle(holder, Paint.STRIKE_THRU_TEXT_FLAG, R.color.colorTextDeleteGray);
            }
            taskViewModel.updateTask(taskModel);
        };
    }

    private void modifyTextStyle(@NonNull MyViewHolder holder, int paint, int color) {
        holder.title.setPaintFlags(paint);
        holder.title.setTextColor(ContextCompat.getColor(context, color));
    }

    private void modifyCheckBoxStyle(@NonNull MyViewHolder holder, TaskModel taskModel, boolean isChecked) {
        taskModel.setChecked(isChecked);
        holder.checkBox.setChecked(isChecked);
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, date;
        private CheckBox checkBox;

        public void setTitleText(String title) {
            this.title.setText(title);
        }

        public void setCheckBox(Boolean checkBox) {
            this.checkBox.setChecked(checkBox);
        }

        public void setDateText(String date) {
            this.date.setText(date);
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.item_title);
            this.date = itemView.findViewById(R.id.item_date);
            this.checkBox = itemView.findViewById(R.id.item_check_box);
        }
    }
}
