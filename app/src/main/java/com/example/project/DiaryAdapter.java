package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.MyViewHolder> {

    private ArrayList<EntryItem> list;
    private Context context;
    private String date;

    public DiaryAdapter(Context context, ArrayList<EntryItem> list, String date) {
        this.context = context;
        this.list = list;
        this.date = date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EntryItem obj = list.get(position);
        holder.time.setText(obj.time);
        holder.textView.setText(obj.text);
        holder.type = obj.type;

        holder.detailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("date", date);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        EditText time;
        TextView textView;
        int type;
        Button detailBtn;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.editTextTime);
            textView = itemView.findViewById(R.id.entry);
            layout = itemView.findViewById(R.id.entryLayout);
            detailBtn = itemView.findViewById(R.id.detailButton);
        }
    }
}