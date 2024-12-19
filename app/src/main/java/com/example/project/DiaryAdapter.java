package com.example.project;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DiaryAdapter extends ArrayAdapter<String> {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private String date;
    public DiaryAdapter(Context context, ArrayList<String> entries, String date) {
        super(context, 0 ,entries);
        this.context = context;
        this.list = entries;
        this.date= date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //String entry = getItem(position);
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.diary_item, null);
        }
        TextView entryText = (TextView) view.findViewById(R.id.entry);
        Button detailButton = view.findViewById(R.id.DetailButton);

        entryText.setText(list.get(position));

        detailButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("date", date);
            intent.putStringArrayListExtra("list", list);
            getContext().startActivity(intent);

        });

        return view;
    }
}
