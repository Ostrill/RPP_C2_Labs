package com.vladimir.rpp_lab_3_2.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vladimir.rpp_lab_3_2.R;
import com.vladimir.rpp_lab_3_2.database.StudentEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<StudentEntity> data = null;

    public ListAdapter(List<StudentEntity> list) {
        data = new ArrayList<>();
        data = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Time time = new Time();
        time.set(data.get(position).addTime);

        holder.nameText.setText(data.get(position).firstName + " " +
                data.get(position).midName + " " +
                data.get(position).lastName);
        holder.idText  .setText("#" + data.get(position).id);
        holder.timeText.setText(time.format("%H:%M:%S"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView idText;
        private TextView timeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.fullname_text);
            idText   = itemView.findViewById(R.id.id_text);
            timeText = itemView.findViewById(R.id.time_text);
        }
    }
}