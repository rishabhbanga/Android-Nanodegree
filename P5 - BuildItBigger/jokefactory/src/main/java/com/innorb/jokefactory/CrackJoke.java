package com.innorb.jokefactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by erishba on 12/24/2017.
 */

public class CrackJoke extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_joke);

        TextView textview = findViewById(R.id.joke_text);

        //Retrieve the joke from the Intent Extras
        String JokeResult = null;
        //the Intent that started us
        Intent intent = getIntent();
        JokeResult = intent.getStringExtra(getString(R.string.jokeEnvelope));

        String DigDeeper = intent.getStringExtra(getString(R.string.dig));

        if (JokeResult != null) {
            textview.setText(JokeResult);
        } else {
            textview.setText(DigDeeper);
        }
    }
}