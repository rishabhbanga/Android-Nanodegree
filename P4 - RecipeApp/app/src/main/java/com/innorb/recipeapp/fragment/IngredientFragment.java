package com.innorb.recipeapp.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.innorb.recipeapp.R;
import com.innorb.recipeapp.activity.BaseActivity;
import com.innorb.recipeapp.adapter.IngredientAdapter;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.innorb.recipeapp.data.Contract;
import com.innorb.recipeapp.utility.Constants;
import com.innorb.recipeapp.utility.PrefManager;
import timber.log.Timber;

public class IngredientFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, IngredientAdapter.IngredientItemClickListener {
    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.rv_detail_ingredient)
    RecyclerView mRecyclerView;

    private FragmentIngredientListener mFragmentIngredientListener;
    private IngredientAdapter mAdapter;
    private static WeakReference<Integer> sWeakReference;

    public static IngredientFragment newInstance(int index) {
        IngredientFragment ingredientFragment = new IngredientFragment();

        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_INGREDIENT_ID, index);
        ingredientFragment.setArguments(args);

        return ingredientFragment;
    }

    public interface FragmentIngredientListener {
        void onFragmentIngredient(int position);
    }

    private int getShownIndex() {
        return (getArguments() == null) ? -1 : getArguments().getInt(Constants.EXTRA_INGREDIENT_ID, 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mDataId = getShownIndex();
        if (mDataId > 0) {
            sWeakReference = new WeakReference<>(mDataId);
            if (getActivity() != null) {
                getActivity().getSupportLoaderManager().initLoader(Constants.INGREDIENT_LOADER_ID, null, this);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);

        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new IngredientAdapter(this);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new IngredientFragmentAsyncTask(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter != null) {
            mAdapter.swapCursor(data);
        }
        int position = BaseActivity.getPositionIngredient();
        if (position == RecyclerView.NO_POSITION) position = 0;
        mRecyclerView.smoothScrollToPosition(position);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    @Override
    public void onIngredientItemClick(int position, int itemCount) {

        if (!PrefManager.isGeneralSettings(getActivity(), getString(R.string.pref_tab_layout))) {

            if (position == (itemCount - 2)) {
                Toast.makeText(getActivity(), R.string.text_click_end_ingredient, Toast.LENGTH_SHORT).show();
            } else if (position == (itemCount - 1)) {
                position = -1;
            }
            updatePosition(position + 1);
        } else {
            updatePosition(position);

        }
        mFragmentIngredientListener.onFragmentIngredient(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mFragmentIngredientListener = (IngredientFragment.FragmentIngredientListener) getActivity();

        } catch (ClassCastException e) {
            if (getActivity() != null){
                throw new ClassCastException(getActivity().getLocalClassName() + "must implement OnFragmentIngredientListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentIngredientListener = null;
    }

    private void updatePosition(int position) {
        if (mAdapter != null) {
            mRecyclerView.smoothScrollToPosition(position);
            mAdapter.notifyDataSetChanged();
        }
    }

    private static class IngredientFragmentAsyncTask extends AsyncTaskLoader<Cursor> {

        Cursor ingredientData = null;

        IngredientFragmentAsyncTask(Context context) {
            super(context);
        }


        @Override
        protected void onStartLoading() {
            if (ingredientData != null) {
                deliverResult(ingredientData);
            } else {
                forceLoad();
            }
        }

        @Override
        public Cursor loadInBackground() {
            try {
                if (sWeakReference != null) {

                    return getContext().getContentResolver().query(Contract.IngredientEntry.CONTENT_URI,
                            null,
                            Contract.IngredientEntry.COLUMN_NAME_RECIPES_ID + " = ?",
                            new String[]{String.valueOf(sWeakReference.get())},
                            Contract.IngredientEntry._ID + " ASC");
                } else {
                    return null;
                }

            } catch (Exception e) {
                Timber.e("Failed to asynchronously load data. ");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void deliverResult(Cursor data) {
            if ((data != null) && (data.getCount() > 0)) {
                ingredientData = data;
                super.deliverResult(data);
            } else {
                forceLoad();
            }
        }
    }

}
