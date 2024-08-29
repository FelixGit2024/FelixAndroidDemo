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
import com.firstproject.androiddemofx.model.ModelTask;

import java.util.ArrayList;

public class ListViewTaskAdapt extends ArrayAdapter<ModelTask> {
    private Context mcontext;
    private int mresource;
    public ListViewTaskAdapt(@NonNull Context context, int resource, @NonNull ArrayList<ModelTask> objects) {
        super(context, resource, objects);
        this.mcontext=context;
        this.mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(this.mcontext).inflate(this.mresource, parent, false);

        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView dayTask = (TextView) convertView.findViewById(R.id.dayTask);

        img.setImageResource(getItem(position).getImg());
        dayTask.setText(getItem(position).getDayTask());

        return convertView;
    }
}
