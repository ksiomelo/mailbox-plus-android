package com.mailboxplus;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StampsAdapter extends ArrayAdapter<Stamp> {

	private List<Stamp> stamps;
    private Context context;

    public StampsAdapter(Context context, int textViewResourceId, List<Stamp> objects){
        super(context, textViewResourceId, objects);
        this.stamps = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
    	return this.stamps.size();
    }
    
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;

        if (view == null) {
            LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = viewInflater.inflate(R.layout.stamp_row, null);
        }

        Stamp stamp = stamps.get(position);

        if (stamp != null) {
            TextView name = (TextView) view.findViewById(R.id.stamp_code);
            //TextView slogan = (TextView) view.findViewById(R.id.slogan);

            if (name != null) {
                name.setText(stamp.stamp_code);
            }

           
        }
        return view;
    }
}