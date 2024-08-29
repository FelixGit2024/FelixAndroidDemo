package com.firstproject.androiddemofx.listviewadapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firstproject.androiddemofx.R;
import com.firstproject.androiddemofx.model.ModelDay;

import java.util.ArrayList;

public class ListViewDayAdapt extends ArrayAdapter<ModelDay> {
    private Context mcontext;
    private int mresource;
    public ListViewDayAdapt(@NonNull Context context, int resource, @NonNull ArrayList<ModelDay> objects) {
        super(context, resource, objects);
        this.mcontext=context;
        this.mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(this.mcontext).inflate(this.mresource, parent, false);

        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView day = (TextView) convertView.findViewById(R.id.day);
        TextView dayContent = (TextView) convertView.findViewById(R.id.dayContent);
        TextView dayContentStatus = (TextView) convertView.findViewById(R.id.dayContentStatus);

        img.setImageResource(getItem(position).getImg());
        day.setText(getItem(position).getDay());
        dayContent.setText(getItem(position).getDayContent());
        dayContentStatus.setText(getItem(position).getDayContentStatus());

        return convertView;
    }
}
