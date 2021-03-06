package com.innorb.recipeapp.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.innorb.recipeapp.BuildConfig;
import com.innorb.recipeapp.R;
import com.innorb.recipeapp.activity.BaseActivity;
import com.innorb.recipeapp.activity.StepActivity;
import com.innorb.recipeapp.data.Contract;
import com.innorb.recipeapp.utility.Constants;
import com.innorb.recipeapp.utility.Utility;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    private final StepItemClickListener mOnStepClickLister;

    private Context mContext;
    private Cursor mCursor;
    private Handler mHandler = null;

    public StepAdapter(StepItemClickListener listener) {
        mOnStepClickLister = listener;
    }

    public interface StepItemClickListener {
        void onStepItemClick(int id, int position);
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutId = R.layout.list_step;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutId, parent, false);

        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(final StepHolder holder, final int position) {

        mCursor.moveToPosition(position);

        String imageStep = mCursor.getString(mCursor.getColumnIndex(Contract.StepEntry.COLUMN_NAME_VIDEO_URL));

        final int idStepDetail = mCursor.getInt(mCursor.getColumnIndex(Contract.StepEntry._ID));
        String shortDescription = mCursor.getString(mCursor.getColumnIndex(Contract.StepEntry.COLUMN_NAME_SHORT_DESCRIPTION));

        holder.bind(idStepDetail);

        final RequestOptions requestOptions;
        if (TextUtils.isEmpty(imageStep)) {

            imageStep = null;
            requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fallback(R.drawable.no_media)
                    .fitCenter()
                    .placeholder(R.drawable.download_in_progress);

        } else {

            requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.no_media)
                    .fallback(R.drawable.no_media)
                    .fitCenter()
                    .placeholder(R.drawable.download_in_progress);
        }

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .asBitmap()
                .load(imageStep)
                .apply(requestOptions)
                .into(new SimpleTarget<Bitmap>(Constants.GLIDE_IMAGE_WIDTH_STEP, Constants.GLIDE_IMAGE_HEIGHT_STEP) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                        int imageFallBack;
                        if (idStepDetail == StepActivity.getIdData() && (BaseActivity.getPositionStep() >= 0)) {
                            imageFallBack = R.drawable.no_media_grayscale;
                        } else {
                            imageFallBack = R.drawable.no_media;
                        }
                        holder.mTextViewShortDescription.setBackgroundResource(imageFallBack);
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        holder.mTextViewShortDescription.setBackgroundResource(R.drawable.download_in_progress);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(mContext.getResources(), resource);
                        if (idStepDetail == StepActivity.getIdData() && (BaseActivity.getPositionStep() >= 0)) {

                            float[] colorMatrix = {
                                    0.33f, 0.33f, 0.33f, 0, Constants.BRIGHTNESS_COLOR_GRAYSCALE,
                                    0.33f, 0.33f, 0.33f, 0, Constants.BRIGHTNESS_COLOR_GRAYSCALE,
                                    0.33f, 0.33f, 0.33f, 0, Constants.BRIGHTNESS_COLOR_GRAYSCALE,
                                    0, 0, 0, 1, 0
                            };

                            drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
                        } else {
                            drawable.setColorFilter(null);
                        }
                        AlphaAnimation alpha = new AlphaAnimation(0.1F, 1.0F);
                        alpha.setDuration(900);
                        alpha.setFillAfter(true);

                        holder.mTextViewShortDescription.startAnimation(alpha);
                        holder.mTextViewShortDescription.setBackground(drawable);
                    }
                });

        int fontCert = R.array.com_google_android_gms_fonts_certs_prod;

        if (BuildConfig.DEBUG) {
            fontCert = R.array.com_google_android_gms_fonts_certs_dev;
        }
        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms", "Permanent Marker",
                fontCert);

        FontsContractCompat.FontRequestCallback callback = new FontsContractCompat
                .FontRequestCallback() {
            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                holder.mTextViewShortDescription.setTypeface(typeface);
            }
        };

        FontsContractCompat
                .requestFont(mContext, request, callback,
                        getHandlerThreadHandler());


        String strTvShortDescription = String.valueOf(position + 1) + ". " + shortDescription;
        holder.mTextViewShortDescription.setText(strTvShortDescription);
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_short_description)
        TextView mTextViewShortDescription;

        private int mId;

        StepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bind(int idData) {
            mId = idData;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (Utility.isTablet(mContext) &&
                    (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                if (position == BaseActivity.getPositionStep()) {
                    view.setClickable(false);
                } else {
                    view.setClickable(true);
                    mOnStepClickLister.onStepItemClick(mId, position);
                }
            } else {
                view.setClickable(true);
                mOnStepClickLister.onStepItemClick(mId, position);
            }
        }
    }

    private Handler getHandlerThreadHandler() {
        if (mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("fonts");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Cursor swapCursor(Cursor c) {
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}