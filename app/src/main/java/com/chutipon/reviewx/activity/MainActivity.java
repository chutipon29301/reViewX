package com.chutipon.reviewx.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chutipon.reviewx.R;
import com.chutipon.reviewx.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, MainFragment.getInstance(), "MainFragment")
                    .commit();
        }
        initInstance(savedInstanceState);
    }

    private void initInstance(Bundle savedInstanceState) {

    }
}
