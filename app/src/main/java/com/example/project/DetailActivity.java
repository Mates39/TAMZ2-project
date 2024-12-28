package com.example.project;

import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    ArrayList<EntryItem> entries = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String date = getIntent().getStringExtra("date");
        int position = getIntent().getIntExtra("position", 1000);

        loadEntries(date);

        EditText text = findViewById(R.id.detailEntryText);
        text.setText(entries.get(position).text);
        EditText time = findViewById(R.id.detailEntryTime);
        time.setText(entries.get(position).time);
        EditText type = findViewById(R.id.detailEntryType);
        type.setText(String.valueOf(entries.get(position).type));
        Button button = findViewById(R.id.button);
        Button deleteButton = findViewById(R.id.deleteBtn);
        Button editButton = findViewById(R.id.editBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EntryItem item = entries.get(position);
                item.text = text.getText().toString();
                item.type = Integer.parseInt(type.getText().toString());
                item.time = time.getText().toString();
                saveEntries(date);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                entries.remove(position);
                saveEntries(date);
                finish();
            }
        });
    }

    private void loadEntries(String date){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppData", MODE_PRIVATE);
        String savedEntries = sharedPreferences.getString(date, null);
        entries.clear();
        if (savedEntries != null && savedEntries.length() != 0) {
            // Convert the saved entries back to an ArrayList
            String[] entriesArray = savedEntries.split(";");
            for(String s : entriesArray)
            {
                String[] values = s.split("\\$");
                Log.d("as", values[0]);
                entries.add(new EntryItem(values[0], values[1], Integer.parseInt(values[2])));
            }
        }
    }

    private void saveEntries(String date){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Join the entries list into a single string separated by semicolons
        StringBuilder entriesString = new StringBuilder();
        editor.clear();
        for (EntryItem item : entries) {
            entriesString.append(item.toString()).append(";");
        }
        // Save the entries string to SharedPreferences
        editor.putString(date, entriesString.toString());
        editor.apply();
    }
}
