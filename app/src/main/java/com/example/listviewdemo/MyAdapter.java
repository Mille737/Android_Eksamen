package com.example.listviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.listviewdemo.model.Note;
import java.util.List;

public class MyAdapter extends BaseAdapter {

    private final List<Note> data;

    private final LayoutInflater layoutInflater;

    public MyAdapter(Context context, List<Note> data) {
        this.data = data;
        // LayoutInflater og from er en statisk metode
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) { // bliver ikke kaldt
        return null;
    }

    @Override
    public long getItemId(int i) { // bliver kaldt 2 gange i starten
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = layoutInflater.inflate(R.layout.myrow, null);
        }
        TextView textView = view.findViewById(R.id.mytextView);
        textView.setText(data.get(i).getTitle());
        return view;
    }
}
