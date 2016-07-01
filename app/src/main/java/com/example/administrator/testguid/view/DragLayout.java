package com.example.administrator.testguid.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**拖拽示例
 * Created by Liyeyu on 2016/6/30.
 */
public class DragLayout extends RelativeLayout {

    private ViewDragHelper mViewDragHelper;
    private int curLeft;
    private View mView;

    public DragLayout(Context context) {
        super(context);
        init();
    }


    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        mView = getChildAt(0);
    }


    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new MyDrawerCallback());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    class MyDrawerCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==mView;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mViewDragHelper.captureChildView(mView, pointerId);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int paddingLeft = getPaddingLeft();
            int paddingRight = getWidth()-child.getWidth()-getPaddingRight();
            return Math.min(Math.max(left,paddingLeft),paddingRight);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int paddingTop = getPaddingTop();
            int paddingBottom = getHeight() - child.getHeight() - getPaddingTop();
            return Math.min(Math.max(top,paddingTop),paddingBottom);
        }


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int actionMasked = MotionEventCompat.getActionMasked(ev);
        if(actionMasked==MotionEvent.ACTION_CANCEL||actionMasked==MotionEvent.ACTION_UP){
            mViewDragHelper.cancel();
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
