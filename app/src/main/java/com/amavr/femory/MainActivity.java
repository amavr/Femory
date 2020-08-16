package com.amavr.femory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amavr.femory.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        App app = (App)getApplication();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance(app.repository))
                    .commitNow();
        }
    }

    public void setHeader(String text){
        getSupportActionBar().setTitle(text);
    }
}
