package com.example.project;

import androidx.annotation.NonNull;

public class EntryItem {
    public String time;
    public String text;
    public int type;

    public EntryItem(String time, String text, int type)
    {
        this.time = time;
        this.text = text;
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(time);
        sb.append("$");
        sb.append(text);
        sb.append("$");
        sb.append(type);
        return sb.toString();
    }
}
