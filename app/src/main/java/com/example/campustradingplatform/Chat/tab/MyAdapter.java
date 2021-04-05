package com.example.campustradingplatform.Chat.tab;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @author: 竹林
 * @date: 2021/4/6
 */
public class MyAdapter extends ArrayAdapter {
    public MyAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }


}
