package rishabhbanga.nanodegree.tnimdb.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.AdapterView;
import android.widget.GridView;

import rishabhbanga.nanodegree.tnimdb.R;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract;
import rishabhbanga.nanodegree.tnimdb.data.MovieContract.MovieEntry;
import rishabhbanga.nanodegree.tnimdb.sync.MovieSyncAdapter;

/**
 * Created by erishba on 5/17/2016.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int MOVIE_LOADER = 0;
    private String mSort;

    private MovieAdapter mAdapter;
    private static final String TAG = MainFragment.class.getSimpleName();
    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    private GridView gridView;

    private static final String[] MOVIE_COLUMNS = {
            MovieEntry.TABLE_NAME + "." + MovieEntry._ID,
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_MOVIE_POSTER_PATH,
        };

    // These indices are tied to MOVIE_COLUMNS.  If FORECAST_COLUMNS changes, these must change

    public static final int COL_MOVIE_ID = 1;
    public static final int COL_MOVIE_POSTER_PATH = 2;

    public MainFragment() {
    }

    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Uri movieUri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAdapter = new MovieAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.movie_grid);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry.buildMovieIdUri(
                                    cursor.getInt(COL_MOVIE_ID)
                            ));
                }
                mPosition = position;
            }
        });

         if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)){
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

            return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateMovie() {
        MovieSyncAdapter.syncImmediately(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSort != null && !mSort.equals(MyMovie.getPreferredSortBy(getActivity()))) {
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        }
    }

    // since the sort pref is read when the loader is created, restarting things are needed
    void onSortPreferenceChanged() {
        updateMovie();
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Now create and return a CursorLoader that will take care of creating a Cursor for the data being displayed.
        mSort = MyMovie.getPreferredSortBy(getActivity());
        Uri movieSortedByUri = MovieEntry.buildMovieListSortedByPreferenceUri(mSort);
        return new CursorLoader(
                getActivity(),
                //MovieEntry.CONTENT_URI,
                movieSortedByUri,
                MOVIE_COLUMNS,
                null,
                null,
                null
        ); //sortOrder
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);  //Swap cursor in forecast Adapter with new loaded one,i.e. data

        if (mPosition != GridView.INVALID_POSITION){
            gridView.setSelection(mPosition);
        }
   }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null); //Release any resource that might be being used
    }
}