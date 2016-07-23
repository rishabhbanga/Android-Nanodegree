package rishabhbanga.nanodegree.tnimdb.movie;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
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
import rishabhbanga.nanodegree.tnimdb.retrofit.model.MovieInfo;

/**
 * Created by erishba on 7/22/2016.
 */

public class MovieFragment extends BaseFragment {


    private ArrayList<Movie> movieArrayList;
    private final String MOVIE_DATA = "movie_data";

    private int count = 0;

    private RetrofitManager retrofitManager;
    private String TAG = MovieFragment.class.getSimpleName();

    @Bind(R.id.gridView1)
    GridView gridView;

    MovieAdapter duplicateMovieAdapter;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get instance of retrofit manager class for network call
        retrofitManager = RetrofitManager.getInstance();

        //register the event bus for listening the movie categories change event
        EventBus.register(this);

        movieArrayList = new ArrayList<>();

        gridView.setDrawSelectorOnTop(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.post(new MoviesEventBus.MoviePosterSelectionEvent(movieArrayList.get(position)));
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            movieArrayList = savedInstanceState.getParcelableArrayList(MOVIE_DATA);
            setGridView(movieArrayList);
        } else {
            fetchData();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.movie_fragment;
    }

    /**
     * fetches the movies data from web and adds data to list {@link #movieArrayList}
     *
     * @param pageNumber       for getting the data of page number .
     * @param moviesCategories movie categories{popular, top_rated}
     */

    private void fetchMoviesFromWeb(int pageNumber, String moviesCategories) {
        Callback<MovieInfo> moviesInfoCallback = new Callback<MovieInfo>() {
            @Override
            public void onResponse(Response<MovieInfo> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    movieArrayList.addAll(response.body().movieList);
                    if (count == 0) {
                        setGridView(movieArrayList);
                        count++;
                    } else {
                        duplicateMovieAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        retrofitManager.getMovieInfo(moviesCategories, pageNumber, BuildConfig.MOVIE_API_KEY, moviesInfoCallback);
    }

    /**
     * set grid view with movie's thumbnail.
     *
     * @param movieArrayList
     */

    private void setGridView(final List<Movie> movieArrayList) {
        duplicateMovieAdapter = new MovieAdapter(getActivity(), movieArrayList);
        gridView.setAdapter(duplicateMovieAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_DATA, movieArrayList);
    }

    /**
     * fetch the data according to the categories of movie.
     * if the categories is favourite fetches data from the database.
     * else fetches from the web
     */

    private void fetchData() {
        String categories = MovieAdapterUtil.getMovieCategories(getActivity());
        if (categories.equals(getString(R.string.favorite_categories_value))) {
            Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

            movieArrayList.clear();
            Movie movie;
            while (cursor.moveToNext()) {
                movie = new Movie();
                movie.id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                movie.overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
                movie.voteAverage = cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING));
                movie.posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH));
                movie.title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
                movie.releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
                movieArrayList.add(movie);
            }
            setGridView(movieArrayList);
        } else {
            fetchMoviesFromWeb(1, categories);
        }
    }

    /**
     * handle the event of movie categories change and loads the UI with updated data
     * by making the network call according to categories.
     *
     * @param event
     */

    @Subscribe
    public void handlePreferenceChangeEvent(MoviesEventBus.PreferenceChangeEvent event) {
        if (event != null) {
            movieArrayList.clear();
            fetchData();
            if (movieArrayList.size() > 0) {
                duplicateMovieAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * for handling the event on pressing the favourite button of detail view.
     *
     * @param movieUnFavorite
     */

    @Subscribe
    public void handleMovieUnFavoriteEvent(MoviesEventBus.MovieUnFavorite movieUnFavorite) {
        fetchData();
    }
}