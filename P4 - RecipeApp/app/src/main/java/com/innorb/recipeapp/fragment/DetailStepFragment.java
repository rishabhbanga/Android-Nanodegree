package com.innorb.recipeapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innorb.recipeapp.R;
import com.innorb.recipeapp.adapter.DetailStepAdapter;
import com.innorb.recipeapp.utility.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailStepFragment extends Fragment {

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
    @BindView(R.id.rv_detail_step)
    RecyclerView mRecyclerView;

    private String mDescription;
    private String mShortDescription;
    private String mThumbnailUrl;
    private String mVideoUri;

    public static DetailStepFragment newInstance(int idData, String description, String shortDescription, String thumbnailUrl, String videoUri) {
        DetailStepFragment detailStepFragment = new DetailStepFragment();

        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_DETAIL_STEP_ID, idData);
        args.putString(Constants.BUNDLE_DETAIL_STEP_DESCRIPTION, description);
        args.putString(Constants.BUNDLE_DETAIL_STEP_SHORT_DESCRIPTION, shortDescription);
        args.putString(Constants.BUNDLE_DETAIL_STEP_THUMBNAILURL, thumbnailUrl);
        args.putString(Constants.BUNDLE_DETAIL_STEP_VIDEOURI, videoUri);


        detailStepFragment.setArguments(args);

        return detailStepFragment;
    }

    private String getDescription() {
        return (getArguments() == null) ? null: getArguments().getString(Constants.BUNDLE_DETAIL_STEP_DESCRIPTION) ;
    }

    private String getShortDescription() {
        return (getArguments() == null) ? null: getArguments().getString(Constants.BUNDLE_DETAIL_STEP_SHORT_DESCRIPTION);
    }

    private String getThumbnailUrl() {
        return (getArguments() == null) ? null: getArguments().getString(Constants.BUNDLE_DETAIL_STEP_THUMBNAILURL);
    }

    private String getVideoUri() {
        return (getArguments() == null) ? null: getArguments().getString(Constants.BUNDLE_DETAIL_STEP_VIDEOURI);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDescription = getDescription();
        mShortDescription = getShortDescription();
        mThumbnailUrl = getThumbnailUrl();
        mVideoUri = getVideoUri();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        DetailStepAdapter adapter = new DetailStepAdapter(mDescription, mShortDescription, mThumbnailUrl, mVideoUri);

        mRecyclerView.setAdapter(adapter);

        return view;
    }

}
