package com.innorb.recipeapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.innorb.recipeapp.BuildConfig;
import com.innorb.recipeapp.R;
import com.innorb.recipeapp.data.Contract;
import com.innorb.recipeapp.utility.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private final ListItemClickListener mOnClickListener;
    private Context mContext;
    private Cursor mCursor;
    private Handler mHandler = null;

    public RecipeAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        int layoutId = R.layout.list_recipe;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutId, parent, false);

        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeHolder holder, final int position) {
        mCursor.moveToPosition(position);

        String imageRecipe = mCursor.getString(mCursor.getColumnIndex(Contract.RecipeEntry.COLUMN_NAME_IMAGE));
        String nameRecipe = mCursor.getString(mCursor.getColumnIndex(Contract.RecipeEntry.COLUMN_NAME_NAME));
        int servingsRecipe = mCursor.getInt(mCursor.getColumnIndex(Contract.RecipeEntry.COLUMN_NAME_SERVINGS));

        int index = mCursor.getInt(mCursor.getColumnIndex(Contract.RecipeEntry._ID));

        final RequestOptions requestOptions;
        if(TextUtils.isEmpty(imageRecipe)){
            imageRecipe = null;
            requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fallback(R.drawable.no_media)
                    .fitCenter()
                    .placeholder(R.drawable.download_in_progress);

        }else {
            requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.no_media)
                    .fallback(R.drawable.no_media)
                    .fitCenter()
                    .placeholder(R.drawable.download_in_progress);

        }

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .asBitmap()
                .load(imageRecipe)
                .apply(requestOptions)
                .into(new SimpleTarget<Bitmap>(Constants.GLIDE_IMAGE_WIDTH_STEP, Constants.GLIDE_IMAGE_HEIGHT_STEP) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        holder.mTextViewRecipeName.setBackgroundResource(R.drawable.no_media);
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        holder.mTextViewRecipeName.setBackgroundResource(R.drawable.download_in_progress);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(mContext.getResources(), resource);
                        holder.mTextViewRecipeName.setBackground(drawable);
                    }
                });

        int fontCert =  R.array.com_google_android_gms_fonts_certs_prod;

        if(BuildConfig.DEBUG){
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
                holder.mTextViewRecipeName.setTypeface(typeface);
            }

        };

            FontsContractCompat
                    .requestFont(mContext, request, callback,
                            getHandlerThreadHandler());

        holder.mTextViewRecipeName.setText(nameRecipe);
        holder.mTextViewRecipeName.setContentDescription(nameRecipe);
        holder.mTextViewRecipeServings.setText(String.valueOf(servingsRecipe));
        holder.bind(index, nameRecipe);
    }

    private Handler getHandlerThreadHandler() {
        if (mHandler == null) {
                HandlerThread handlerThread = new HandlerThread("fonts");
                handlerThread.start();
                mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name)
        TextView mTextViewRecipeName;

        @BindView(R.id.tv_recipe_servings)
        TextView mTextViewRecipeServings;

        private String mRecipeName;

        private int mIdData;

        RecipeHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(int idData, String recipeName) {
            mIdData = idData;
            mRecipeName = recipeName;

        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(mIdData, mRecipeName);
        }

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

    public interface ListItemClickListener {

        void onListItemClick(int clickItemIndex, String recipeName);
    }
}