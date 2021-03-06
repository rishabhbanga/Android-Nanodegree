package com.innorb.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.innorb.jokefactory.CrackJoke;

public class MainActivityFragment extends Fragment
{
    ProgressBar progressBar = null;
    protected String loadedJoke = null;
    private boolean testFlag = false;

    public MainActivityFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        Button button = root.findViewById(R.id.joke_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                fetchJoke();
            }
        });

        progressBar = root.findViewById(R.id.joke_progressbar);
        progressBar.setVisibility(View.GONE);

        AdView mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }

    public void fetchJoke(){
        new EndPointsAsync().execute(this);
    }

    public void launchCrackJokeActivity(){
        if(!testFlag) {
            Context context = getActivity();
            Intent intent = new Intent(context, CrackJoke.class);
            intent.putExtra(context.getString(R.string.jokeEnvelope), loadedJoke);
            context.startActivity(intent);
            progressBar.setVisibility(View.GONE);
        }
    }
}