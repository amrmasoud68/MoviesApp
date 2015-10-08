package com.example.amrmasoud.popmovies.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amrmasoud.popmovies.Model.TrailerElement;
import com.example.amrmasoud.popmovies.R;

import java.util.ArrayList;

/**
 * Created by amr masoud on 10/4/2015.
 */

 class trailerHolder {

    ImageView imageHolder;
    TextView namekHolder;

}

public class TrailerAdapter extends ArrayAdapter<TrailerElement> {
    ArrayList<TrailerElement> trailerList;
    int resource;
    Context context;
    LayoutInflater inflater;

    public TrailerAdapter(Context context, int resource, ArrayList<TrailerElement> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        trailerList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        trailerHolder holder ;
        if(convertView==null) {
            convertView=inflater.inflate(resource,null);
            holder = new trailerHolder();
            holder.imageHolder = (ImageView) convertView.findViewById(R.id.trailer_image);
            holder.namekHolder = (TextView) convertView.findViewById(R.id.trailer_name);

            convertView.setTag(holder);
        } else {
            holder = (trailerHolder)convertView.getTag();
        }

        holder.imageHolder.setImageResource(R.drawable.ic_play);
        holder.namekHolder.setText(trailerList.get(position).mName);


        return convertView;
    }
}
