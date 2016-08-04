package rishabhbanga.nanodegree.tnimdb.movie;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rishabhbanga.nanodegree.tnimdb.BuildConfig;
import rishabhbanga.nanodegree.tnimdb.R;
import rishabhbanga.nanodegree.tnimdb.adapter.MovieAdapter;
import rishabhbanga.nanodegree.tnimdb.adapter.MovieAdapterUtil;
import rishabhbanga.nanodegree.tnimdb.base.BaseFragment;
import rishabhbanga.nanodegree.tnimdb.bus.EventBus;
import rishabhbanga.nanodegree.tnimdb.bus.MoviesEventBus;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract;
import rishabhbanga.nanodegree.tnimdb.retrofit.RetrofitManager;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.Movie;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieComment;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieComments;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieTrailer;
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieTrailerInfo;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieEntry;

/**
 * Created by erishba on 7/11/2016.
 */

public class MovieDetailFragment extends BaseFragment {

    private Boolean favorite = false;

    @Bind(R.id.img_movie_poster)
    ImageView moviePoster;

    @Bind(R.id.tv_movie_title)
    TextView movieTitle;

    @Bind(R.id.tv_release_date)
    TextView releaseDate;

    @Bind(R.id.tv_overview)
    TextView overView;

    @Bind(R.id.rb_movie_rating)
    RatingBar ratingBar;

    @Nullable
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Bind(R.id.ll_comments)
    LinearLayout llComments;

    @Nullable
    @Bind(R.id.ll_trailers)
    LinearLayout llTrailers;

    @Bind(R.id.tv_comment_title)
    TextView tvCommentTitle;

    @Nullable
    @Bind(R.id.tv_trailer_title)
    TextView tvTrailerTitle;

    //Movie movie;
    RetrofitManager retrofitManager = null;

    List<MovieComment> comments;
    List<MovieTrailer> trailers;

    private Movie mMovie;

    private Activity activity;

    String trailerKey;

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MovieAdapterUtil.MOVIE_OBJECT, movie);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(MovieAdapterUtil.MOVIE_OBJECT);
        }
    }

    //Sets movie details
    private void setData() {

        Picasso.with(activity).load(MovieAdapter.getImageUri(mMovie.posterPath))
                .into(moviePoster);
        movieTitle.setText(mMovie.title);
        ratingBar.setRating(mMovie.voteAverage);
        overView.setText(mMovie.overview);
        releaseDate.setText(mMovie.releaseDate);

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor movieCursor = contentResolver.query(MovieEntry.buildMovieUri(mMovie.id), null, null, null, null, null);

        if (movieCursor.getCount() > 0) {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fav));
            favorite = true;
        }

        String movieId = Integer.toString(mMovie.id);

        String categories = MovieAdapterUtil.getMovieCategories(getActivity());
        if (categories.equals(getString(R.string.favorite_categories_value))) {
            Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieCommentEntry.buildCommentUri(mMovie.id), null, null, new String[]{movieId}, null);
            MovieComment movieComment;
            if (cursor.getCount() > 0) {
                comments = new ArrayList<>();
                while (cursor.moveToNext()) {
                    movieComment = new MovieComment();
                    movieComment.author = cursor.getString(cursor.getColumnIndex(MovieContract.MovieCommentEntry.COLUMN_AUTHOR_NAME));
                    movieComment.content = cursor.getString(cursor.getColumnIndex(MovieContract.MovieCommentEntry.COLUMN_MOVIE_COMMENT));

                    comments.add(movieComment);
                }
                showMovieComments(comments);
            }

        } else {
            getCommentsFromWeb();
        }
    }

    //Plays Trailer
    @OnClick({R.id.iv_play_movie})

    public void onClick() {
        if (MovieAdapterUtil.isNetworkAvailable(getActivity())) {
            playTrailer(trailerKey);
        }
    }

    //Marks movie as Favorite
    @OnClick({R.id.fab})
    public void addFavorite(View view) {

        if (!favorite) {

            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fav));

            //Inserting movie description to the table movie
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.id);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, mMovie.title);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie.releaseDate);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, mMovie.posterPath);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, mMovie.voteAverage);
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, mMovie.overview);

            activity.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            ContentValues cv = null;
            //Inserting comments of respective movie in table comments
            if (comments != null) {
                for (MovieComment movieComment : comments) {
                    cv = new ContentValues();
                    cv.put(MovieContract.MovieCommentEntry.COLUMN_MOVIE_ID, mMovie.id);
                    cv.put(MovieContract.MovieCommentEntry.COLUMN_AUTHOR_NAME, movieComment.author);
                    cv.put(MovieContract.MovieCommentEntry.COLUMN_MOVIE_COMMENT, movieComment.content);
                    activity.getContentResolver().insert(MovieContract.MovieCommentEntry.CONTENT_URI, cv);
                }

            }
            favorite = true;
        } else {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unfav));
            int id = getActivity().getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(mMovie.id), null, null);
            EventBus.post(new MoviesEventBus.MovieUnFavorite());
            favorite = false;
        }
    }

    //Intent to open YouTube App
    private void playTrailer(String key) {
        if (key != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MovieAdapterUtil.YOUTUBE_INTENT_BASE_URI + key));
            // Verifies original intent will resolve to at least one activity
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    //Get Comments
    private void getCommentsFromWeb() {
        Callback<MovieComments> callback = new Callback<MovieComments>() {
            @Override
            public void onResponse(Response<MovieComments> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    comments = response.body().movieCommentList;
                    if (response.body().movieCommentList.size() > 0) {
                        showMovieComments(comments);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        retrofitManager.getComments(mMovie.id, BuildConfig.MOVIE_API_KEY, callback);
        getTrailerKeyFromWeb();

    }

    //Display comments on View
    private void showMovieComments(List<MovieComment> response) {

        tvCommentTitle.setVisibility(View.VISIBLE);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (MovieComment comment : response) {

            View view = layoutInflater.inflate(R.layout.layout_movie_comments, llComments, false);
            TextView tvCommenterName = ButterKnife.findById(view, R.id.tv_commenter_name);
            TextView tvComment = ButterKnife.findById(view, R.id.tv_comment);

            tvComment.setText(comment.content);
            tvCommenterName.setText(comment.author);

            llComments.addView(view);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Makes comment and Trailer Text View invisible
        tvCommentTitle.setVisibility(View.GONE);
        tvTrailerTitle.setVisibility(View.GONE);

        //Registers retrofit for network call
        retrofitManager = RetrofitManager.getInstance();

        setData();

    }

    @Override
    protected int getLayout() {

        return R.layout.movie_fragment_detail_tab;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflates the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);
        // Retrieves the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share) {
            shareMovieTrailerUrl();
        }

        return super.onOptionsItemSelected(item);
    }

    //Sharing YouTube link
    private void shareMovieTrailerUrl() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity()).setType("text/plain")
                .setText(Uri.parse(MovieAdapterUtil.YOUTUBE_INTENT_BASE_URI + trailerKey).toString())
                .getIntent();
        if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    //Getting Trailerkey from Web
    private void getTrailerKeyFromWeb() {
        if (trailers == null) {
            retrofit.Callback<MovieTrailerInfo> movieTrailerInfoCallback = new retrofit.Callback<MovieTrailerInfo>() {
                @Override
                public void onResponse(Response<MovieTrailerInfo> response, Retrofit retrofit) {
                    if (response.isSuccess() && response.body().movieTrailer.size() > 0) {
                        trailers = new ArrayList<>();
                        trailerKey = response.body().movieTrailer.get(0).key;
                        trailers.addAll(response.body().movieTrailer);
                        showMovieTrailer(trailers);

                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            };
            retrofitManager.getTrailer(mMovie.id, BuildConfig.MOVIE_API_KEY, movieTrailerInfoCallback);
        }
    }

    //View launches trailer on click Play Button
    private void showMovieTrailer(List<MovieTrailer> trailers) {
        tvTrailerTitle.setVisibility(View.VISIBLE);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 1; i < trailers.size(); i++)
        {
            View view = layoutInflater.inflate(R.layout.layout_movie_trailers, llComments, false);

            LinearLayout llTrailerWrapper = ButterKnife.findById(view, R.id.ll_trailer_wrapper);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(140, 140);
            layoutParams.rightMargin = 10;
            ImageView ivPlayIcon = new ImageView(getActivity());
            ivPlayIcon.setTag(trailers.get(i));
            ivPlayIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_button));
            ivPlayIcon.setLayoutParams(layoutParams);

            //Launches the youtube application with trailer
            ivPlayIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MovieTrailer movieTrailer = (MovieTrailer) v.getTag();
                    playTrailer(movieTrailer.key);
                }
            });

            LinearLayout.LayoutParams paramsTvTrailer = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            TextView tvTrailer = new TextView(getActivity());

            tvTrailer.setText("Trailer " + "#" + i);
            tvTrailer.setTextSize(16);
            tvTrailer.setTextColor(Color.parseColor("#000000"));
            tvTrailer.setGravity(Gravity.CENTER_VERTICAL);


        llTrailerWrapper.addView(ivPlayIcon, layoutParams);
            llTrailerWrapper.addView(tvTrailer, paramsTvTrailer);

            llTrailers.addView(llTrailerWrapper);
        }
    }
}