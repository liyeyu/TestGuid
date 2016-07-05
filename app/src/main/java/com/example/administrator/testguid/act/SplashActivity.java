package com.example.administrator.testguid.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.testguid.R;
import com.example.administrator.testguid.view.parallax.ParallaxView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ParallaxView parallaxView = (ParallaxView) findViewById(R.id.pv_splash);
        parallaxView.initPager(this,new int[]{
                R.layout.parallax_1,
                R.layout.parallax_2,
                R.layout.parallax_3});
    }
}
