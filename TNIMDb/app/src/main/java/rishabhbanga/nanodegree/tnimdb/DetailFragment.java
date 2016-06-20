package rishabhbanga.nanodegree.tnimdb;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by erishba on 5/18/2016.
 */

public class DetailFragment extends Fragment {

    private MyMovie mMyMovie;

    public DetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            mMyMovie = intent.getParcelableExtra(Intent.EXTRA_TEXT);

        }

        TextView movieTitle = (TextView)rootView.findViewById(R.id.movie_title);
        movieTitle.setText(mMyMovie.getTitle());

        ImageView movieIcon = (ImageView)rootView.findViewById(R.id.movie_imgIcon);
        Picasso.with(getContext()).load(mMyMovie.getIconPath()).into(movieIcon);

        TextView movieYear = (TextView)rootView.findViewById(R.id.movie_release_year);
        movieYear.setText(MyMovie.getYear(mMyMovie.getReleaseDate()));

        TextView movieVote = (TextView)rootView.findViewById(R.id.movie_vote);
        movieVote.setText(MyMovie.getVote(mMyMovie.getVote_avg()));

        TextView movieDuration = (TextView)rootView.findViewById(R.id.movie_duration);
        movieDuration.setText(MyMovie.getDuration());

        TextView movieDesc = (TextView)rootView.findViewById(R.id.movie_overview);
        movieDesc.setText(mMyMovie.getDescription());

        return rootView;
    }
}
