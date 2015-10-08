package com.example.amrmasoud.popmovies.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.amrmasoud.popmovies.Model.ReviewElement;
import com.example.amrmasoud.popmovies.R;

import java.util.ArrayList;

/**
 * Created by amr masoud on 10/4/2015.
 */
class reviewHolder {
    TextView nameHolder;
    TextView contentHolder;
}
public class ReviewAdapter extends ArrayAdapter<ReviewElement> {
    ArrayList<ReviewElement> reviewList;
    int resource;
    Context context;
    LayoutInflater inflater;

    public ReviewAdapter(Context context, int resource, ArrayList<ReviewElement> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        reviewList=objects;
        inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        reviewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(resource,null);
            holder = new reviewHolder();
            holder.nameHolder = (TextView) convertView.findViewById(R.id.review_author);
            holder.contentHolder = (TextView) convertView.findViewById(R.id.review_content);
            convertView.setTag(holder);
        } else {
            holder = (reviewHolder) convertView.getTag();
        }

        holder.nameHolder.setText(reviewList.get(position).mAuthor);
        holder.contentHolder.setText(reviewList.get(position).mContent);

        return convertView;
    }
}
