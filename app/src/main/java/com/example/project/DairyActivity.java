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

import java.util.ArrayList;
import java.util.Arrays;

public class DairyActivity extends AppCompatActivity {
    private EditText diaryEntryEditText;
    private ListView entriesListView;
    private ArrayList<String> entries;
    private ArrayAdapter<String> adapter;
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
        Log.d("DaityActivity", date);


        diaryEntryEditText = findViewById(R.id.diaryEntryEditText);
        entriesListView = findViewById(R.id.entriesListView);
        Button saveButton = findViewById(R.id.saveButton);
        Button calendarButton = findViewById(R.id.calendar_button);

        entries = new ArrayList<>();
        adapter = new DiaryAdapter(this, entries, date);
        entriesListView.setAdapter(adapter);



        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String entry = diaryEntryEditText.getText().toString().trim();
                if(!entry.isEmpty()){
                    entries.add(entry);
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
        if (savedEntries != null) {
            // Convert the saved entries back to an ArrayList
            String[] entriesArray = savedEntries.split(";");
            entries.addAll(Arrays.asList(entriesArray));
        }
        adapter.notifyDataSetChanged();
    }

    private void saveEntries(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Join the entries list into a single string separated by semicolons
        StringBuilder entriesString = new StringBuilder();
        for (String entry : entries) {
            entriesString.append(entry).append(";");
        }
        // Save the entries string to SharedPreferences
        editor.putString(date, entriesString.toString());
        editor.apply();
    }
}
