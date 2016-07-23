package rishabhbanga.nanodegree.tnimdb.movie;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.squareup.otto.Subscribe;

import butterknife.Bind;
import rishabhbanga.nanodegree.tnimdb.R;
import rishabhbanga.nanodegree.tnimdb.adapter.MovieAdapterUtil;
import rishabhbanga.nanodegree.tnimdb.base.BaseActivity;
import rishabhbanga.nanodegree.tnimdb.bus.MoviesEventBus;
import rishabhbanga.nanodegree.tnimdb.data.MovieDbHelper;
import rishabhbanga.nanodegree.tnimdb.settings.SettingsActivity;

public class MovieActivity extends BaseActivity {

    private static final String TAG = MovieActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Bind(R.id.movie_detail_container)
    FrameLayout detailContainer;

    private String DETAIL_FRAGMENT_TAG = "detail_fragment";

    MovieFragment movieFragment;

    private String MOVIE_FRAGMENT = "movie_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set toolbar.
        setSupportActionBar(toolbar);

        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        movieDbHelper.getWritableDatabase();

        if (savedInstanceState != null) {
            movieFragment = (MovieFragment) getFragmentManager().getFragment(savedInstanceState, MOVIE_FRAGMENT);
            getFragmentManager().beginTransaction().replace(R.id.movie_container, movieFragment).commit();

            if (detailContainer != null) {
                if (doesDetailFragmentExistInBackStack()) {
                    MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, movieDetailFragment, DETAIL_FRAGMENT_TAG)
                            .commit();
                }
            }
        } else {
            movieFragment = new MovieFragment();
            getFragmentManager().beginTransaction().add(R.id.movie_container, movieFragment).addToBackStack(MOVIE_FRAGMENT).commit();

        }
    }
    @Override
    protected int getLayout() {
        return R.layout.movie_activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * handle the event posted when clicked in the movie listing grid view
     * and launch {@link MovieDetailFragment} with the movie detail if it is tablet layout.
     * other wise launch the {@link MovieDetailActivity} with movie detail.
     *
     * @param moviePosterSelectionEvent
     */
    @Subscribe
    public void handleMoviePosterSelectionEvent(MoviesEventBus.MoviePosterSelectionEvent moviePosterSelectionEvent) {
        if (detailContainer != null) {
            if (moviePosterSelectionEvent.movie != null && detailContainer != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, MovieDetailFragment.newInstance(moviePosterSelectionEvent.movie), DETAIL_FRAGMENT_TAG)
                        .addToBackStack(DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
            intent.putExtra(MovieAdapterUtil.MOVIE_OBJECT, moviePosterSelectionEvent.movie);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getFragmentManager().putFragment(outState, MOVIE_FRAGMENT, movieFragment);
    }

    /**
     * checks {@link MovieDetailFragment} existence in back stack.
     *
     * @return returns true {@link MovieDetailFragment} exist in back stack.
     */
    private boolean doesDetailFragmentExistInBackStack() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1);
            String str = backEntry.getName();
            if (str.equals(DETAIL_FRAGMENT_TAG)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}