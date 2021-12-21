package com.example.planahead;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Serializable
{

    private ArrayList<Object> eORt;
    private Context context;

    public RecyclerViewAdapter(ArrayList<Object> eORt, Context context) {
        this.eORt = eORt;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_taskitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // called every time a new item is added
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (eORt.get(position).getClass() == Event.class) {
            Event obj = (Event) eORt.get(position);
            holder.taskName.setText(obj.getName());
        }
        else if (eORt.get(position).getClass() == com.example.planahead.Task.class) {
            com.example.planahead.Task obj = (com.example.planahead.Task) eORt.get(position);
            holder.taskName.setText(obj.getName());
        }
        holder.postponeTaskButton.setOnClickListener(v -> {
        });

        /*holder.constraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskDetailActivity.class);
            intent.putExtra("calledFrom", String.valueOf(context));
            intent.putExtra("position", position);
            intent.putExtra("task", eORt.get(position));
            context.startActivity(intent);
        });*/
    }

    @Override
    public int getItemCount() {
        return eORt.size();
    }

    // holds each individual list item in memory
    public class ViewHolder extends RecyclerView.ViewHolder implements Serializable
    {
        TextView taskName;
        Button postponeTaskButton;
        ConstraintLayout constraintLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskNameID);
            postponeTaskButton = itemView.findViewById(R.id.pushBackButtonID);
            constraintLayout = itemView.findViewById(R.id.taskLayoutID);
        }
    }
}

