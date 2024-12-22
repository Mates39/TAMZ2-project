package com.example.project;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class DairyActivity extends AppCompatActivity {
    private EditText diaryEntryEditText;
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


        diaryEntryEditText = findViewById(R.id.diaryEntryEditText);
        entriesView = findViewById(R.id.entriesView);
        entriesView.setLayoutManager(new LinearLayoutManager(this));
        Button saveButton = findViewById(R.id.saveButton);
        Button calendarButton = findViewById(R.id.calendar_button);
        entries = new ArrayList<>();
        adapter = new DiaryAdapter(this, entries, date);
        entriesView.setAdapter(adapter);

        EditText text = findViewById(R.id.diaryEntryEditText);
        EditText type = findViewById(R.id.editTextNumber);
        EditText time = findViewById(R.id.editTextTime2);



        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String entry = diaryEntryEditText.getText().toString().trim();
                if(!entry.isEmpty()){
                    //entries.add(new EntryItem(time.toString(), text.toString(),Integer.parseInt(type.toString())));
                    entries.add(new EntryItem("11:30", "schuzka", 1));
                    adapter.notifyDataSetChanged();
                    diaryEntryEditText.setText("");
                    saveEntries(date);
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
}
