package rishabhbanga.nanodegree.tnimdb.app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import rishabhbanga.nanodegree.tnimdb.R;

/**
 * Created by erishba on 5/17/2016.
 */

public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Read weather icon ID from cursor
        String icons = cursor.getString(MainFragment.COL_MOVIE_POSTER_PATH);

        ImageView iconView = (ImageView)view.findViewById(R.id.movie_image);
        Picasso.with(context).load(MyMovie.BaseUrl + icons).into(iconView);
       }
}
