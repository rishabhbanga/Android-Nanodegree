package rishabhbanga.nanodegree.tnimdb.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import rishabhbanga.nanodegree.tnimdb.bus.Event;

/**
 * Created by erishba on 7/9/2016.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
    }

    // abstract method returns id of layout in the form of R.layout.layout_name.
    protected abstract int getLayout();

    @Override
    protected void onResume() {
        Event.register(this);
        super.onResume();
    }

    // un-register event(Otto) on onPause() if any subclasses have registered for listening to events
    @Override
    protected void onPause() {
        Event.unregister(this);
        super.onPause();
    }
}
