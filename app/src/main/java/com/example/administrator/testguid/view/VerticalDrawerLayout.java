package com.example.administrator.testguid.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 2层布局，上层遮罩，下层内容，
 * Created by Liyeyu on 2016/6/28.
 */
public class VerticalDrawerLayout extends ViewGroup {

    //当打开时遮罩最大位置
    public static int MAX_SCROLL_Y = 0;
    //下层内容页
    private View mContentView;
    //上层遮罩
    private View mDrawerView;
    private ViewDragHelper mViewDragHelper;
    private int mCurTop;
    private boolean isOpen;
    private MarginLayoutParams mContentParams;
    private MarginLayoutParams mDrawerParams;

    public VerticalDrawerLayout(Context context) {
        super(context);
        init();
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelperCallBack());
        //因为是垂直向上滑动，所以设置边界追踪
        // 为顶部
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
    }

    public class  ViewDragHelperCallBack extends ViewDragHelper.Callback{


        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //返回true捕获view
            return child==mDrawerView;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
//            super.onEdgeDragStarted(edgeFlags, pointerId);
            //setEdgeTrackingEnabled设置的边界滑动时触发
            //captureChildView是为了让tryCaptureView返回false依旧生效
            mViewDragHelper.captureChildView(mDrawerView,pointerId);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //按住view往水平拖动时回调 left = view的x点，dx = 增量
            return super.clampViewPositionHorizontal(child, left+mDrawerParams.leftMargin, dx);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //按住view往垂直拖动时回调 top = view的y点，dy = 增量
            //因为是垂直向上滑动，所以top最大值是0，最小值是-mDrawerView.getHeight()
            return Math.max(Math.min(top,MAX_SCROLL_Y),-mDrawerView.getHeight())+mDrawerParams.topMargin;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //计算当前位置显示的部分是总高度的百分比,xvel = x方向上的加速度,yvel = y方向上的加速度
            float movePercent = (releasedChild.getHeight() + releasedChild.getTop()) /(float)releasedChild.getHeight();
            //计算滑动到一半松开时mDrawerView应该往上还是往下
            int finalTop = movePercent>0.7f?MAX_SCROLL_Y+mDrawerParams.topMargin:-releasedChild.getHeight()-mDrawerParams.topMargin;
            //releasedChild 位置变化，移动view
            mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft()+mDrawerParams.leftMargin,finalTop);
            //位置改变，刷新,必须重绘才能生效
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mCurTop = top;
            //当changedView位置变化移出界面时，防止过度绘制
            changedView.setVisibility((changedView.getHeight()+top)<=MAX_SCROLL_Y?View.GONE:View.VISIBLE);
            //布局重排
            requestLayout();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            //只关注child在垂直方向的移动范围
            if(mDrawerView==null)return MAX_SCROLL_Y;
            return mDrawerView==child?mDrawerView.getHeight():MAX_SCROLL_Y;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 0;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            //根据状态和mCurTop判断当前view是显示还是隐藏
            if(state==ViewDragHelper.STATE_IDLE){
                isOpen = mDrawerView.getTop()==0;
            }
        }
    }

    @Override
    public void computeScroll() {
        //每次刷新都会计算位移情况
        //在拖动中重绘
        if(mViewDragHelper.continueSettling(true)){
            requestLayout();
        }
    }

    public void closeDrawer() {
        if(isOpen){
            mViewDragHelper.smoothSlideViewTo(mDrawerView,mDrawerView.getLeft(),-mDrawerView.getHeight());
            requestLayout();
        }
    }
    public void openDrawer() {
        if(!isOpen){
            mViewDragHelper.smoothSlideViewTo(mDrawerView,mDrawerView.getLeft(),MAX_SCROLL_Y);
            requestLayout();
        }
    }
    public boolean isDrawerOpened() {
        return isOpen;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //将事件交由mViewDragHelper分发
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        //设定ViewGroup为自动填充
        return new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT,MarginLayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            MarginLayoutParams params = (MarginLayoutParams) mContentView.getLayoutParams();
            //mContentView可能也是个viewGroup，需要排布
            mContentView.layout(params.leftMargin,params.topMargin
                    ,mContentView.getMeasuredWidth()+params.rightMargin,mContentView.getMeasuredHeight()+params.bottomMargin);
            params = (MarginLayoutParams) mDrawerView.getLayoutParams();
            mDrawerView.layout(params.leftMargin,mCurTop+params.topMargin
                    ,mDrawerView.getMeasuredWidth()+params.rightMargin,mCurTop+mDrawerView.getMeasuredHeight()+params.bottomMargin);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth  = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight  = MeasureSpec.getSize(heightMeasureSpec);
        //调用测量方法
        setMeasuredDimension(measureWidth,measureHeight);
        mContentView = getChildAt(0);
        mDrawerView = getChildAt(1);
        if(mContentView==null || mDrawerView==null){
            throw new NullPointerException("ContentView or DrawerView is null");
        }
        mContentParams = (MarginLayoutParams) mContentView.getLayoutParams();
        //计算ContentView和DrawerView的子view的测量参数
        int childMeasureWidth = MeasureSpec.makeMeasureSpec(measureWidth - (mContentParams.leftMargin + mContentParams.rightMargin), MeasureSpec.EXACTLY);
        int childMeasureHeight = MeasureSpec.makeMeasureSpec(measureHeight - (mContentParams.topMargin + mContentParams.bottomMargin), MeasureSpec.EXACTLY);
        mContentView.measure(childMeasureWidth,childMeasureHeight);
        mDrawerParams = (MarginLayoutParams) mDrawerView.getLayoutParams();
        childMeasureWidth = MeasureSpec.makeMeasureSpec(measureWidth - (mDrawerParams.leftMargin + mDrawerParams.rightMargin), MeasureSpec.EXACTLY);
        childMeasureHeight = MeasureSpec.makeMeasureSpec(measureHeight - (mDrawerParams.topMargin + mDrawerParams.bottomMargin), MeasureSpec.EXACTLY);
        mDrawerView.measure(childMeasureWidth,childMeasureHeight);
    }
}
