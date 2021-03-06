package com.innorb.recipeapp.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innorb.recipeapp.R;
import com.innorb.recipeapp.data.Contract;
import com.innorb.recipeapp.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.innorb.recipeapp.adapter.RecipeAdapter;

import timber.log.Timber;

import static com.innorb.recipeapp.utility.Constants.RECIPE_LOADER_ID;

public class RecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.rv_recipe)
    RecyclerView mRecyclerView;

    private RecipeAdapter mAdapter;
    private FragmentInteractionListener mListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            getActivity().getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        ButterKnife.bind(this, view);
        GridLayoutManager gridLayoutManager;

        if (Utility.isTablet(getActivity())) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                gridLayoutManager = new GridLayoutManager(getActivity(),
                        Integer.valueOf(getString(R.string.span_count_grid_recipe)));
                mRecyclerView.setLayoutManager(gridLayoutManager);

            } else {
                gridLayoutManager = new GridLayoutManager(getActivity(), Integer.valueOf(getString(R.string.tablet_span_count_grid_recipe)));
                mRecyclerView.setLayoutManager(gridLayoutManager);

            }

        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                if ((getActivity() != null) && (getActivity().findViewById(R.id.fragment_list_container) != null)) {
                    gridLayoutManager = new GridLayoutManager(getActivity(),
                            Integer.valueOf(getString(R.string.tablet_span_count_grid_recipe)));
                    mRecyclerView.setLayoutManager(gridLayoutManager);
                } else {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    mRecyclerView.setLayoutManager(layoutManager);
                }

            } else {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);
            }
        }

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecipeAdapter(this);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new RecipeFragmentAsyncTask(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if ((data != null) && (mAdapter != null)) {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(int clickItemIndex, String recipeName) {
        mListener.onFragmentInteraction(clickItemIndex, recipeName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (FragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getLocalClassName() + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface FragmentInteractionListener {
        void onFragmentInteraction(int id, String recipeName);
    }


    private static class RecipeFragmentAsyncTask extends AsyncTaskLoader<Cursor> {

        Cursor mRecipeData = null;

        RecipeFragmentAsyncTask(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            if (mRecipeData != null) {
                deliverResult(mRecipeData);
            } else {
                forceLoad();
            }

        }

        @Override
        public Cursor loadInBackground() {
            try {
                return getContext().getContentResolver().query(Contract.RecipeEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

            } catch (Exception e) {
                Timber.e("Failed to asynchronously load data. ");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void deliverResult(Cursor data) {
            if ((data != null) && (data.getCount() > 0)) {
                mRecipeData = data;
                super.deliverResult(data);
            } else {
                forceLoad();
            }

        }
    }

}