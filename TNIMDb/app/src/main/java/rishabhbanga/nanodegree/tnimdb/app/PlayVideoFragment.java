package rishabhbanga.nanodegree.tnimdb.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;

import rishabhbanga.nanodegree.tnimdb.BuildConfig;
import rishabhbanga.nanodegree.tnimdb.R;

/**
 * Created by erishba on 6/29/2016.
 */

public class PlayVideoFragment extends DialogFragment {

    private static final String TAG = PlayVideoFragment.class.getSimpleName();
    private static final String EXTRA_ID = "movie_id";

    public static PlayVideoFragment newInstance(int movieId)
    {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ID, movieId);

        PlayVideoFragment fragment = new PlayVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_for_choosing)
                .setPositiveButton(R.string.dialog_browser,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getContext(),"Browser -" + id, Toast.LENGTH_SHORT).show();
                                //Open with Browser
                                try {
                                    openBrowserToView(getArguments().getInt(EXTRA_ID));
                                } catch (MalformedURLException e) {
                                    Log.d(TAG, "MalformedURLException", e);
                                }
                            }
                        })
                .setNegativeButton(R.string.dialog_youtube,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getContext(), "YouTube -" + id, Toast.LENGTH_SHORT).show();
                                // Open with YouTube
                                openYouTubeToView(getArguments().getInt(EXTRA_ID));
                            }
                        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void openBrowserToView (int movieId) throws MalformedURLException {

        final String BASE_URL = MyMovie.BaseMovieUrl;
        final String API_KEY = "api_key";
        final String VIDEOS = "videos";
        final String keyValue = BuildConfig.MOVIE_DB_API_KEY;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieId))
                .appendPath(VIDEOS)
                .appendQueryParameter(API_KEY, keyValue)
                .build();

        Log.d(TAG, "builtUri is " + builtUri.toString());

        }

    private void openYouTubeToView(int movieId){
        // if more info provided, the precise url will be parsed,, for now it is hard coded
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=COvnHv42T-A"));
        startActivity(i);
    }
}

