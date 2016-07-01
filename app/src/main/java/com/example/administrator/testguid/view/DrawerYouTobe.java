package com.example.administrator.testguid.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.testguid.R;

/**
 * Created by Liyeyu on 2016/6/30.
 */
public class DrawerYouTobe extends ViewGroup {

    private ViewDragHelper mViewDragHelper;
    private View mHeaderView;
    private View mDescView;
    private int mTop;
    private int mDragRange;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private float mDragOffset;

    public DrawerYouTobe(Context context) {
        super(context);
        init();
    }
    public DrawerYouTobe(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawerYouTobe(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = getHeight() - mHeaderView.getHeight();
        //为子布局重排位置
        mHeaderView.layout(mHeaderView.getPaddingLeft(),mTop,
                r+mHeaderView.getPaddingRight(),mTop+mHeaderView.getMeasuredHeight());
        mDescView.layout(mDescView.getPaddingLeft(),mTop+mHeaderView.getMeasuredHeight(),
                r+mDescView.getPaddingRight(),mTop+b+mDescView.getPaddingBottom());

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = findViewById(R.id.viewHeader);
        mDescView = findViewById(R.id.viewDesc);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //继承ViewGroup必须重写
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        int measureWidth  = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight  = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(measureWidth,widthMeasureSpec,0)
                ,resolveSizeAndState(measureHeight,heightMeasureSpec,0));
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new YoutubeDrawerCallback());
    }

    class YoutubeDrawerCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==mHeaderView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mTop = top;
            //基于滑动区间的百分比
            mDragOffset = (float)top/mDragRange;
            //设置mHeaderView旋转固定点
            mHeaderView.setPivotX(mHeaderView.getWidth());
            mHeaderView.setPivotY(mHeaderView.getHeight());
            //设置mHeaderView缩放速度和比例
            mHeaderView.setScaleX(1-mDragOffset/2);
            mHeaderView.setScaleY(1-mDragOffset/2);
            mDescView.setAlpha(1-mDragOffset);
            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int paddingTop = getPaddingTop();
            if(mDragOffset>0.5f){
                //如果滑动超过一半，往下，否则往上
                paddingTop+=mDragRange;
            }
            //确定最终位置
            mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(),paddingTop);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int paddingTop = getPaddingTop();
            int paddingBottom = getHeight() - mHeaderView.getHeight() - mHeaderView.getPaddingBottom();
            return Math.min(Math.max(paddingTop,top),paddingBottom);
        }
    }

    @Override
    public void computeScroll() {
        //滑动结束
        if(mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = MotionEventCompat.getActionMasked(ev);
        if (( action != MotionEvent.ACTION_DOWN)) {
            mViewDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }
        if(action==MotionEvent.ACTION_UP || action==MotionEvent.ACTION_CANCEL){
            mViewDragHelper.cancel();
            return false;
        }
        final float x = ev.getX();
        final float y = ev.getY();
        boolean interceptTap = false;
        switch (action){
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                interceptTap = mViewDragHelper.isViewUnder(mHeaderView, (int) x, (int) y);
            }
                break;
            case MotionEvent.ACTION_MOVE: {
                //处理滑动幅度和方向
                float dx = Math.abs(x - mInitialMotionX);
                float dy = Math.abs(y - mInitialMotionY);
                //以敏感系数计算的view滑动值
                int touchSlop = mViewDragHelper.getTouchSlop();
                if (dx > dy && dy > touchSlop) {
                    mViewDragHelper.cancel();
                    return false;
                }
            }
                break;
        }

        return mViewDragHelper.shouldInterceptTouchEvent(ev)||interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
        boolean isHeaderViewUnder = mViewDragHelper.isViewUnder(mHeaderView, (int) x, (int) y);
        switch (action & MotionEventCompat.ACTION_MASK){
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
            }
                break;
            case MotionEvent.ACTION_UP: {
                float dx = x - mInitialMotionX;
                float dy = y - mInitialMotionY;
                int touchSlop = mViewDragHelper.getTouchSlop();
                //dx * dx + dy * dy < touchSlop * touchSlop &&
                if (dx * dx + dy * dy < touchSlop * touchSlop && isHeaderViewUnder) {
                    if (mDragOffset == 0) {
                        smoothSlideTo(1f);
                    }else{
                        smoothSlideTo(0f);
                    }
                }
            }
                break;
            }

        return isHeaderViewUnder && isViewHit(mHeaderView, (int) x, (int) y) || isViewHit(mDescView, (int) x, (int) y);
    }
    private boolean isViewHit(View view, int x, int y) {
            int[] viewLocation = new int[2];
            view.getLocationOnScreen(viewLocation);
            int[] parentLocation = new int[2];
            this.getLocationOnScreen(parentLocation);
            int screenX = parentLocation[0] + x;
            int screenY = parentLocation[1] + y;
            return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                    screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
        }
    boolean smoothSlideTo(float slideOffset) {
        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * mDragRange);
        if (mViewDragHelper.smoothSlideViewTo(mHeaderView, mHeaderView.getLeft(), y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }
}
