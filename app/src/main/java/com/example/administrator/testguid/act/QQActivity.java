package com.example.administrator.testguid.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.testguid.R;
import com.example.administrator.testguid.view.QQDrawerLayout;

public class QQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq);
        final QQDrawerLayout qqDrawerLayout = (QQDrawerLayout) findViewById(R.id.drawer_qq);
        findViewById(R.id.left_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qqDrawerLayout.isLeftIsOpened()){
                    qqDrawerLayout.closeLeftView();
                }else{
                    qqDrawerLayout.openLeftView();
                }
            }
        });
    }
}
