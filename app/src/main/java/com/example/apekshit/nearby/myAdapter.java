package com.example.apekshit.nearby;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Apekshit on 23-07-2016.
 */public class myAdapter extends BaseAdapter {

    private final Activity context;
    private final ArrayList<String> name,open, vicinity;
    public myAdapter(Activity context,
                      ArrayList<String> name,ArrayList<String> open,ArrayList<String> vicinity) {

        this.context = context;
        this.name=name;
        this.open = open;
        this.vicinity=vicinity;

    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.row_layout, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
        TextView txtTitle2 = (TextView) rowView.findViewById(R.id.textView2);
        TextView txtTitle3 = (TextView) rowView.findViewById(R.id.textView3);

        txtTitle.setText(name.get(position));
        txtTitle2.setText(open.get(position));
        txtTitle3.setText(vicinity.get(position));

        return rowView;
    }
}
