package com.example.project;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class DairyActivity extends AppCompatActivity {
    private EditText diaryEntryText;
    private EditText diaryEntryType;
    private EditText diaryEntryTime;
    private RecyclerView entriesView;
    private ArrayList<EntryItem> entries;
    private DiaryAdapter adapter;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dairy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calendar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        date = getIntent().getStringExtra("date");


        diaryEntryText = findViewById(R.id.diaryEntryText);
        diaryEntryType = findViewById(R.id.diaryEntryType);
        diaryEntryTime = findViewById(R.id.diaryEntryTime);
        entriesView = findViewById(R.id.entriesView);
        entriesView.setLayoutManager(new LinearLayoutManager(this));
        Button saveButton = findViewById(R.id.saveButton);
        Button calendarButton = findViewById(R.id.calendar_button);
        entries = new ArrayList<>();
        adapter = new DiaryAdapter(this, entries, date);
        entriesView.setAdapter(adapter);




        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String entryText = diaryEntryText.getText().toString().trim();
                String entryType = diaryEntryType.getText().toString().trim();
                String entryTime = diaryEntryTime.getText().toString().trim();
                if(!entryText.isEmpty() && !entryType.isEmpty()){
                    EntryItem item = new EntryItem(entryTime, entryText, Integer.parseInt(entryType));
                    entries.add(item);
                    adapter.notifyDataSetChanged();
                    diaryEntryText.setText("");
                    diaryEntryType.setText("");
                    diaryEntryTime.setText("");
                    saveEntries(date);
                    setNotification(item, entryTime);
                    Toast.makeText(DairyActivity.this, "entry saved", Toast.LENGTH_SHORT).show();

                }
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEntries(date);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void loadEntries(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppData", MODE_PRIVATE);
        String savedEntries = sharedPreferences.getString(date, null);
        entries.clear();
        if (savedEntries != null && savedEntries.length() != 0) {
            // Convert the saved entries back to an ArrayList
            String[] entriesArray = savedEntries.split(";");
            for(String s : entriesArray)
            {
                String[] values = s.split("\\$");
                entries.add(new EntryItem(values[0], values[1], Integer.parseInt(values[2])));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void saveEntries(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Join the entries list into a single string separated by semicolons
        StringBuilder entriesString = new StringBuilder();
        for (EntryItem item : entries) {
            entriesString.append(item.toString()).append(";");
        }
        // Save the entries string to SharedPreferences
        editor.putString(date, entriesString.toString());
        editor.apply();
    }

    private void setNotification(EntryItem entry, String time){
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("entry", "Upcomig event: " + entry.text + " at " + entry.time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, entry.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.add(Calendar.MINUTE, -30);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void deleteNotification(EntryItem entry){
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, entry.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
