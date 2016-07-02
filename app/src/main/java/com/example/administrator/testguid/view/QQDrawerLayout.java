package com.example.administrator.testguid.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.nineoldandroids.view.ViewHelper;

/**
 * 仿qq侧滑
 * Created by Liyeyu on 2016/7/1.
 */
public class QQDrawerLayout extends ViewGroup {

    private ViewDragHelper mViewDragHelper;
    private View mBottomView;
    private View mTopView;
    private float mCurMovePercent;
    private float mInitXpos;
    private float mInitYpos;

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
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() == 2) {
            MarginLayoutParams params = (MarginLayoutParams) mBottomView.getLayoutParams();
            mBottomView.layout(params.leftMargin, params.topMargin,
                    mBottomView.getMeasuredWidth()+params.leftMargin,
                    mBottomView.getMeasuredHeight()+params.topMargin);

            params = (MarginLayoutParams) mTopView.getLayoutParams();
            mTopView.layout(params.leftMargin, params.topMargin,
                    mTopView.getMeasuredWidth()+params.leftMargin,
                    mTopView.getMeasuredHeight()+params.topMargin);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
        if (getChildCount() == 2) {
            mBottomView = getChildAt(0);
            mTopView = getChildAt(1);
            mBottomView.setTag("mBottomView");
            mTopView.setTag("mTopView");
            //对侧边菜单栏宽度进行处理
            MarginLayoutParams params = (MarginLayoutParams) mBottomView.getLayoutParams();
            int width = (params.width<0||params.width>700)?600:params.width;
            int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            measureChild(mBottomView,widthSpec,heightMeasureSpec);
            measureChild(mTopView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    //通过smoothSlideViewTo打开侧栏，因为其他两个方法只能在callback的release中调运
    public void openLeftView() {
        smoothSlideViewTo(mBottomView.getMeasuredWidth());
    }

    //通过smoothSlideViewTo关闭侧栏，因为其他两个方法只能在callback的release中调运
    public void closeLeftView() {
        smoothSlideViewTo(0);
    }
    //判断当前左边侧栏是否打开状态，通过mLeftShowSize进行判断
    public boolean isLeftIsOpened() {
        return mTopView.getLeft() == mBottomView.getMeasuredWidth();
    }

    private void smoothSlideViewTo(int finalLeft) {
        if (mViewDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            mViewDragHelper.abort();
        }
        mViewDragHelper.smoothSlideViewTo(mTopView, finalLeft, mTopView.getTop());
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;

        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN:
                mInitXpos = ev.getX();
                mInitYpos = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX()-mInitXpos) - Math.abs(ev.getY()-mInitYpos) > ViewConfiguration.getTouchSlop()) {
                    result = true;
                    //ACTION_MOVE时已经过了tryCaptureView，故想挪动则使用captureChildView即可
                    mViewDragHelper.captureChildView(mTopView, ev.getPointerId(0));
                }
                break;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev) || result;
    }

    class QQDrawerCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            mViewDragHelper.captureChildView(mTopView,pointerId);
            return child==mTopView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        //计算出mTopView变化时占总变化宽度的的实时百分比
            if (changedView == mTopView) {
                mCurMovePercent = (mBottomView.getMeasuredWidth()-left) / (float)mBottomView.getMeasuredWidth();
//                Log.i("qq -- ","Width:"+mBottomView.getMeasuredWidth()+" left:"+left+" mCurMovePercent:"+mCurMovePercent);
            }
            //设定mTopView缩放区间为[1,0.8]
            float topScale = (float) (0.8 + mCurMovePercent * (1-0.8));
            ViewHelper.setScaleX(mTopView,topScale);
            ViewHelper.setScaleY(mTopView,topScale);
            //设定mTopView缩放区间为[0.8,1]
            float btomScale = (float) (1+0.8-topScale);
            ViewHelper.setScaleX(mBottomView,btomScale);
            ViewHelper.setScaleY(mBottomView,btomScale);
            ViewHelper.setAlpha(mBottomView,topScale);
            //让侧边栏动画初始位置为-mBottomView.getMeasuredWidth()
            float btmTransX = -mBottomView.getMeasuredWidth() * mCurMovePercent;
            ViewHelper.setTranslationX(mBottomView, btmTransX);    //mBottomView.setTranslationX();
        }
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == mTopView) {
                int finalLeft = (xvel>0&&mCurMovePercent<0.5f)?mBottomView.getMeasuredWidth():0;
                Log.i("加速度","xvel:"+xvel+" mCurMovePercent:"+mCurMovePercent);
                mViewDragHelper.settleCapturedViewAt(finalLeft,mTopView.getTop());
                invalidate();
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mBottomView.getMeasuredWidth();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //书评挪动时最大位置的处理
            return Math.min(getViewHorizontalDragRange(child),Math.max(0,left));
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);
        }
    }
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
