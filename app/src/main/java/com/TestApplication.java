package com;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Liyeyu on 2016/6/22.
 */
public class TestApplication extends Application{


    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
//        mRefWatcher = LeakCanary.install(this);
    }

    public RefWatcher getRefWatcher(Context context) {
        TestApplication app = (TestApplication) context.getApplicationContext();
        return app.mRefWatcher;
    }
}
