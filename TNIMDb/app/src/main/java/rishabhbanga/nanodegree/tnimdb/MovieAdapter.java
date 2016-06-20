package rishabhbanga.nanodegree.tnimdb;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by erishba on 5/17/2016.
 */

public class MovieAdapter extends ArrayAdapter<MyMovie> {

    public MovieAdapter(Activity context, List<MyMovie> movieList) {
        super(context, 0, movieList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         MyMovie m = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }

        ImageView iconView = (ImageView)convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(m.getPath()).into(iconView);

        return convertView;
    }

}
