package com.example.administrator.testguid.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Liyeyu on 2016/7/1.
 */
public class QQDrawerLayout extends ViewGroup {

    private ViewDragHelper mViewDragHelper;

    public QQDrawerLayout(Context context) {
        super(context);
        init();
    }

    public QQDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QQDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new QQDrawerCallback());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    class QQDrawerCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }
    }

}
