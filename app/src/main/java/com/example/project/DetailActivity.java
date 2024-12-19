package com.example.project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ArrayList<String> entries = new ArrayList<>();

        String date = getIntent().getStringExtra("date");
        int position = getIntent().getIntExtra("position", 1000);

        TextView text = findViewById(R.id.textView2);
        Button button = findViewById(R.id.button);
        Button deleteButton = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("MyAppData", MODE_PRIVATE);
                String savedEntries = sp.getString(date, null);
                if(savedEntries != null)
                {
                    String[] entriesArray = savedEntries.split(";");
                    entries.addAll(Arrays.asList(entriesArray));
                    entries.remove(position);

                    SharedPreferences.Editor editor = sp.edit();
                    StringBuilder entriesString = new StringBuilder();
                    editor.clear();
                    if(!entries.isEmpty())
                    {
                        for (String entry : entries) {
                            entriesString.append(entry).append(";");
                        }
                        // Save the entries string to SharedPreferences
                        editor.putString(date, entriesString.toString());
                    }
                    editor.apply();
                    finish();
                }

            }
        });
    }
}
