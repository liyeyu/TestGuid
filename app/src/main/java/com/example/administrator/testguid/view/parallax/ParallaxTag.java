package com.example.administrator.testguid.view.parallax;

/**
 * Created by Liyeyu on 2016/7/4.
 */
public class ParallaxTag {
    public float a_in;
    public float a_out;
    public float x_in;
    public float x_out;
    public float y_in;
    public float y_out;

    public boolean hasValue(){
        return a_in!=0||a_out!=0||x_in!=0||x_out!=0||y_in!=0||y_out!=0;
    }

    @Override
    public String toString() {
        return "ParallaxTag{" +
                "a_in=" + a_in +
                ", a_out=" + a_out +
                ", x_in=" + x_in +
                ", x_out=" + x_out +
                ", y_in=" + y_in +
                ", y_out=" + y_out +
                '}';
    }
}
