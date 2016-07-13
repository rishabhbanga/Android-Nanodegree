package rishabhbanga.nanodegree.tnimdb.movie;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rishabhbanga.nanodegree.tnimdb.R;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.Movie;

/**
 * Created by erishba on 5/17/2016.
 */

public class MovieAdapter extends BaseAdapter {

    List<Movie> movieArrayList;
    LayoutInflater inflater;
    private static String IMAGE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private Context mContext;

    public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/";
    public static final String YOUTUBE_INTENT_BASE_URI = "vnd.youtube://";
    public static final String MOVIE_OBJECT = "movie_object";


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

        holder.tvMovieTitle.setText(movie.title);
        Picasso.with(mContext).load(getImageUri(movie.posterPath)).into(holder.imgPoster);

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_movie_title)
        TextView tvMovieTitle;

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

    public static String getMovieCategories(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String movieCategories = sharedPreferences.getString(context.getString(R.string.movies_categories_key), context.getString(R.string.default_movies_categories));
        return movieCategories;
    }


    public  static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}

