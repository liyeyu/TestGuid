package com.example.administrator.testguid.entity;

import android.graphics.Bitmap;
import android.graphics.Path;

import java.io.Serializable;

/**
 * Created by Liyeyu on 2016/6/6.
 */
    public class Flower implements Serializable {

        private static final long serialVersionUID = 1L;
        private Bitmap image;
        private float x;
        private float y;
        private Path path;
        private float value;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
