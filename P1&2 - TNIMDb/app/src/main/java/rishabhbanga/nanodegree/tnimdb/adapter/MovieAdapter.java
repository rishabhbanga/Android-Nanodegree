package rishabhbanga.nanodegree.tnimdb.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rishabhbanga.nanodegree.tnimdb.R;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.Movie;

/**
 * Created by erishba on 7/22/2016.
 */

public class MovieAdapter extends BaseAdapter

    {

        List<Movie> movieArrayList;
        LayoutInflater inflater;
        private static final String IMAGE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
        private Context mContext;

        public MovieAdapter(Context context, List<Movie> movieArrayList) {
        this.mContext = context;
        this.movieArrayList = movieArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

        @Override
        public int getCount() {
        return movieArrayList.size();
    }

        @Override
        public Object getItem(int position) {
        return null;
    }

        @Override
        public long getItemId(int position) {
        return position;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

        final Movie movie = movieArrayList.get(position);
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.single_movie_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        Picasso.with(mContext).load(getImageUri(movie.posterPath)).into(holder.imgPoster);

        return convertView;
    }

        static class ViewHolder {

            @Bind(R.id.img_movie_poster)
            ImageView imgPoster;

            @Bind(R.id.card_view)
            CardView cardView;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    public static String getImageUri(String uri) {
        return IMAGE_POSTER_BASE_URL + "/" + uri;
    }
}