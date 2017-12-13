package com.innorb.recipeapp.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.text.TextPaint;

import com.innorb.recipeapp.R;

import java.util.Locale;

public class Utility {
    private final Context mContext;
    private final ActionBar mActionBar;

    public Utility(Context context, ActionBar actionBar) {
        mContext = context;
        mActionBar = actionBar;
    }

    public static boolean isTablet(Context context) {
        return context != null && (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void setColorOfflineActionBar() {
        if (! isOnline(mContext)) {
            if (mActionBar != null) {
                mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(Constants.COLOR_BACKGROUND_ACTIONBAR_OFFLINE)));
            }
        }
    }

    public String appVersionName() throws PackageManager.NameNotFoundException {
        return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
    }

    public static boolean isPermissionExtStorage(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.pref_write_external_storage), 0);
        return pref.getBoolean(context.getString(R.string.pref_write_external_storage), false);
    }

    public static void RequestPermissionExtStorage(Activity thisActivity) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMgr!=null){
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null) && (networkInfo.getState() == NetworkInfo.State.CONNECTED);
        }
        return false;
    }

    public static void isDeniedPermissionExtStorage(Activity thisActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            thisActivity.getSharedPreferences(thisActivity.getString(R.string.pref_write_external_storage), 0).edit().clear().apply();
        }
    }

    public static Bitmap bitmapTitleImage(Context context, String string) {

        if ((context == null) || (string == null)) return null;

        string = string.toUpperCase(Locale.getDefault());

        Typeface typeface = ResourcesCompat.getFont(context, R.font.indie_flower);
        int fontSizePx = (int) (Constants.BITMAT_FONT_SIZE_DP * context.getResources().getDisplayMetrics().scaledDensity);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(typeface);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(context.getResources().getColor(R.color.white));
        paint.setTextSize(fontSizePx);
        paint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        int textHeight = (int) (fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading);
        TextPaint textPaint = new TextPaint(paint);

        Bitmap bitmap = Bitmap.createBitmap((int) textPaint.measureText(string),
                textHeight, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(bitmap);
        if ((bitmap.getHeight() > 0)) {
            myCanvas.drawText(string, 0, bitmap.getHeight(), paint);
            return bitmap;
        }
        return null;
    }
}