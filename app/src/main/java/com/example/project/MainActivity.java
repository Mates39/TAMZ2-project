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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private ListView todayActivities;
    private ArrayList<String> activities;
    private ArrayAdapter<String> adapter;
    private String date;

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
        todayActivities = findViewById(R.id.TodayActivities);
        activities = new ArrayList<>();
        adapter = new DiaryAdapter(this, activities, date);
        todayActivities.setAdapter(adapter);

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
        if (savedEntries != null) {
            // Convert the saved entries back to an ArrayList
            String[] entriesArray = savedEntries.split(";");
            activities.addAll(Arrays.asList(entriesArray));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEntries(date);
    }
}