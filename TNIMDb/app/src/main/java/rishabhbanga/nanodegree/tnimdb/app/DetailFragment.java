package rishabhbanga.nanodegree.tnimdb.app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import rishabhbanga.nanodegree.tnimdb.R;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieEntry;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract.FavMovieEntry;
import rishabhbanga.nanodegree.tnimdb.data.MovieDbHelper;

/**
 * Created by erishba on 5/18/2016.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 0;
    private final String TAG = DetailFragment.class.getSimpleName();
    public static final String MOVIE_KEY = "movie_id";
    private Uri mUri;
    String trailerKey;

    private MyMovie mMyMovie;
    private Button mPlayTrailerButton;
    private Button mSaveFavoriteButton;
    static final String DETAIL_URI = "URI";
    public static final String YOUTUBE_INTENT_BASE_URI = "vnd.youtube://";

    private static final String[] MOVIE_COLUMNS = {

            MovieEntry.TABLE_NAME + "." + MovieEntry._ID,
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_MOVIE_TITLE,
            MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieEntry.COLUMN_MOVIE_DESC,
            MovieEntry.COLUMN_MOVIE_POPULARITY,
            MovieEntry.COLUMN_MOVIE_VOTE,
            MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
            MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            MovieEntry.COLUMN_MOVIE_VOTE,
            MovieEntry.COLUMN_MOVIE_HAS_VIDEO,
            /* This works because the WeatherProvider returns location data joined with
              weather data, even though they're stored in two different tables.
              MyFavMovieEntry.COLUMN_MOVIE_ID */
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_DESC = 2;
    public static final int COL_MOVIE_RELEASE_DATE = 3;
    public static final int COL_MOVIE_POSTER_PATH = 4;
    public static final int COL_MOVIE_VOTE_AVG = 5;
    public static final int COL_MOVIE_POP = 6;
    private int mMovieId;

    public DetailFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(MOVIE_KEY, mMovieId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);
        // Retrieve the share menu item
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

    // method shares the  youtube url to social media
    private void shareMovieTrailerUrl() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity()).setType("text/plain")
                .setText(Uri.parse("Check this out!!" + "\n" + YOUTUBE_INTENT_BASE_URI + trailerKey).toString())
                .getIntent();
        if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();

        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        if (mUri != null) {

            mMovieId = MovieEntry.getMovieIdFromUri(mUri);
        }

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        if (savedInstanceState != null) {
            mMovieId = savedInstanceState.getInt(MOVIE_KEY);
        }
        super.onActivityCreated(savedInstanceState);
    }

    void onSortPreferenceChanged( String newSort ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            int movieId = MovieEntry.getMovieIdFromUri(uri);
            Uri updatedUri = MovieEntry.buildMovieIdUri(movieId);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        if ( null != mUri ) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            while (data.moveToNext()) {
                final int movieId = data.getInt(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID));
                // 0:false, 1:true
                int hasTrailer = data.getInt(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_HAS_VIDEO));

                if (mMovieId == movieId) {

                    String movieTitle =
                            data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_TITLE));
                    ((TextView) getView().findViewById(R.id.movie_title))
                            .setText(movieTitle);

                    String movieDesc =
                            data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_DESC));
                    ((TextView) getView().findViewById(R.id.movie_overview))
                            .setText(movieDesc);


                    ((TextView) getView().findViewById(R.id.movie_duration))
                            .setText(mMyMovie.getDuration());

                    double movieVote =
                            data.getDouble(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_VOTE));
                    ((TextView) getView().findViewById(R.id.movie_vote))
                            .setText(mMyMovie.getVote(movieVote));


                    String movieYear =
                            data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
                    ((TextView) getView().findViewById(R.id.movie_release_year))
                            .setText(mMyMovie.getYear(movieYear));


                    String icons =
                            data.getString(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_BACKDROP_PATH));

                    ImageView iconView = (ImageView) getView().findViewById(R.id.movie_imgIcon);
                    Picasso.with(getContext()).load(mMyMovie.BaseUrl + icons).into(iconView);

                    mSaveFavoriteButton = (Button) getView().findViewById(R.id.btn_favorite);
                    mSaveFavoriteButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            if (mMovieId != 0){

                                if (saveFavoriteMovie(mMovieId)) {

                                    Toast.makeText(getContext(), R.string.message_ok, Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(getContext(), R.string.message_already_saved, Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                Toast.makeText(getContext(), R.string.message_not_ok, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // if the previous request indicating that this movie has video,
                    // then set trailer play is visible.
                    if (hasTrailer == 0) {

                        ((TextView) getView().findViewById(R.id.movie_trailer))
                                .setVisibility(View.VISIBLE);
                        mPlayTrailerButton =((Button) getView().findViewById(R.id.play_trailer));
                        mPlayTrailerButton.setVisibility(View.VISIBLE);
                        mPlayTrailerButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                FragmentManager fm = getActivity()
                                        .getSupportFragmentManager();
                                PlayVideoFragment dialog = PlayVideoFragment.newInstance(movieId);
                                dialog.show(fm,TAG);
                            }
                        });
                    }

                    break;
                }
            }
        }
    }

    private boolean saveFavoriteMovie(int movieId){

        MovieDbHelper dbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri movieFavUri = FavMovieEntry.buildMyFavMovieIdUri(movieId);
        Cursor myFavMovieCursor = getActivity().getContentResolver().query(
                movieFavUri,
                null,
                null,
                null,
                null
        );

        if (myFavMovieCursor != null && myFavMovieCursor.moveToFirst()) {

            while (myFavMovieCursor.moveToNext()) {

                int mvId = myFavMovieCursor.getInt(myFavMovieCursor
                        .getColumnIndex(FavMovieEntry.COLUMN_MOVIE_ID));
                if (mvId == movieId){
                    return false;
                }
            }
        }

        myFavMovieCursor.close();
        ContentValues values = new ContentValues();
        values.put(FavMovieEntry.COLUMN_MOVIE_ID, movieId);
        long aRowId;
        aRowId = db.insert(FavMovieEntry.TABLE_NAME, null, values);

        return true;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}
