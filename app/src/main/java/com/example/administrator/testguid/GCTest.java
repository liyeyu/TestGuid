package com.example.administrator.testguid;

import android.content.Context;

/**
 * Created by Liyeyu on 2016/6/23.
 */
public class GCTest {

    public static GCTest test;
    private Context context;

    public GCTest(Context context) {
        this.context = context;
    }


    public static GCTest get(Context context){
        if(test==null){
            test = new GCTest(context);
        }
        return test;
    }
}
