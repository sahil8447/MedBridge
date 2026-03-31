package com.Arsenic.medbridge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Arsenic.medbridge.Patient;
import com.Arsenic.medbridge.R;

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {

    private List<Patient> list;

    public userAdapter(List<Patient> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userGender, userPriority, userDate, userAge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            userGender = itemView.findViewById(R.id.userGender);
            userPriority = itemView.findViewById(R.id.userPriority);
            userDate = itemView.findViewById(R.id.userDate);
            userAge = itemView.findViewById(R.id.userAge);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.analyzed_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Patient p = list.get(position);

        // 🔥 SAFE BINDING
        String name = p.name != null ? p.name : "";
        String age = p.age != null ? p.age : "0";
        String gender = p.gender != null ? p.gender : "";
        String priority = p.priority != null ? p.priority : "";
        String date = p.date != null ? p.date : "";

        holder.userName.setText(name + " (Age: " + age + ")");
        holder.userGender.setText(gender);
        holder.userPriority.setText(priority);
        holder.userDate.setText(date);
        holder.userAge.setText(age);

        // 🔥 Priority color
        if (priority.equalsIgnoreCase("P1")) {
            holder.userPriority.setTextColor(0xFFFF4444);
        } else if (priority.equalsIgnoreCase("P2")) {
            holder.userPriority.setTextColor(0xFFFFBB33);
        } else {
            holder.userPriority.setTextColor(0xFF00C851);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}