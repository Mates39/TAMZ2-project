package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private ListView todayActivities;
    private ArrayList<EntryItem> activities;
    private DiaryAdapter adapter;
    private String date;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CalendarView cal = findViewById(R.id.calendarView);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        date = sdf.format(new Date());
        activities = new ArrayList<EntryItem>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiaryAdapter(this, activities, date);
        recyclerView.setAdapter(adapter);




        cal.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = String.format("%02d/%02d/%d", month+1, dayOfMonth, year);
            Intent intent = new Intent(MainActivity.this, DairyActivity.class);
            intent.putExtra("date", date);

            startActivity(intent);
        });

    }
    private void loadEntries(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppData", MODE_PRIVATE);
        String savedEntries = sharedPreferences.getString(date, null);
        activities.clear();
        if (savedEntries != null && savedEntries.length() != 0) {
            // Convert the saved entries back to an ArrayList
            String[] entriesArray = savedEntries.split(";");
            for(String s : entriesArray)
            {
                String[] values = s.split("\\$");
                Log.d("as", values[0]);
                activities.add(new EntryItem(values[0], values[1], Integer.parseInt(values[2])));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEntries(date);
    }
}