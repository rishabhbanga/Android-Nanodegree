package com.rishabhbanga.portfolioapp_rishabhbanga;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchTNIMDb(View view)
    {
        try
        {
            Toast.makeText(getApplicationContext(),"Diverting to TNIMDb's repo" , Toast.LENGTH_SHORT).show();
            Intent iTNIMDb = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/rishabhbanga/Android-Nanodegree/tree/master/TNIMDb"));
            startActivity(iTNIMDb);
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a Web Browser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void launchfetchweather(View view)
    {
        try
        {
            Toast.makeText(getApplicationContext(),"Diverting to Fetch Weather App's repo" , Toast.LENGTH_SHORT).show();
            Intent iWeather = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/rishabhbanga/FetchWeatherApp"));
            startActivity(iWeather);
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a Web Browser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void launchss(View view)
    {
        Toast.makeText(getApplicationContext(),"Launching Spotify Streamer in 3" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"2" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"1" , Toast.LENGTH_SHORT).show();
    }

    public void launchscore(View view)
    {
        Toast.makeText(getApplicationContext(),"Launching Scores App in 3" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"2" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"1" , Toast.LENGTH_SHORT).show();
    }

    public void launchlib(View view)
    {
        Toast.makeText(getApplicationContext(),"Launching Library App in 3" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"2" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"1" , Toast.LENGTH_SHORT).show();
    }

    public void launchbuildbig(View view)
    {
        Toast.makeText(getApplicationContext(),"Launching Build It Bigger in 3" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"2" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"1" , Toast.LENGTH_SHORT).show();
    }

    public void launchxyz(View view)
    {
        Toast.makeText(getApplicationContext(),"Launching XYZ Reader in 3" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"2" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"1" , Toast.LENGTH_SHORT).show();
    }

    public void launchcapstone(View view)
    {
        Toast.makeText(getApplicationContext(),"Launching Capstone App in 3" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"2" , Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"1" , Toast.LENGTH_SHORT).show();
    }
}


