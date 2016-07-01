package com.example.administrator.testguid.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * 搜索动画控件
 * Created by Liyeyu on 2016/6/7.
 */
public class SearchView extends View {

    private PathMeasure mMeasure;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatedValue = 0;
    private Animator.AnimatorListener mAnimatorListener;
    private Handler mAnimatorHandler;
    private int count = 0;
    private long defaultDuration = 2000;

    // 这个视图拥有的状态
    public static enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    // 画笔
    private Paint mPaint;

    // View 宽高
    private int mViewWidth;
    private int mViewHeight;

    // 放大镜与外部圆环
    private Path path_search;
    private Path path_circle;

    // 当前的状态(非常重要)
    private State mCurrentState = State.NONE;

    // 控制各个过程的动画
    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchingAnimator;
    private ValueAnimator mEndingAnimator;

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    // 判断是否已经搜索结束
    private boolean isOver = false;

    public SearchView(Context context) {
        super(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initPath();
        initListener();
        initHandler();
        initAnimator();
        // 进入开始动画
        mCurrentState = State.STARTING;
        mStartingAnimator.start();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        //圆形笔刷
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initPath() {
        path_search = new Path();
        path_circle = new Path();
        mMeasure = new PathMeasure();
        //以屏幕中点为圆心绘制圆
        //外环圆
        RectF rectInner = new RectF(-20, -20, 20, 20);
        path_search.addArc(rectInner, 45, 359.9f);
        //外环圆
        RectF rectOut = new RectF(-40, -40, 40, 40);
        path_circle.addArc(rectOut, 45, 359.9f);
        //取外圆和内圆45度直线作为把手
        mMeasure.setPath(path_circle, false);
        //初始位置
        float[] pos = new float[2];
        mMeasure.getPosTan(0, pos, null);
        path_search.lineTo(pos[0], pos[1]);
    }

    private void initListener() {
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // getHandle发消息通知动画状态更新
                mAnimatorHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mAnimatorHandler.sendEmptyMessage(0);
            }
        };

    }

    private void initHandler() {
        mAnimatorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (mCurrentState) {
                    case STARTING:
                        isOver = false;
                        mCurrentState = State.SEARCHING;
//                        mStartingAnimator.removeAllUpdateListeners();
                        mSearchingAnimator.start();
                        break;
                    case SEARCHING:
                        if (!isOver) {
                            mSearchingAnimator.resume();
                            count++;
                            if (count > 2) {
                                isOver = true;
                            }
                        } else {
//                            mSearchingAnimator.removeAllUpdateListeners();
                            mSearchingAnimator.pause();
                            mCurrentState = State.ENDING;
                            mEndingAnimator.start();
                        }
                        break;
                    case ENDING:
                        mCurrentState = State.NONE;
                        break;
                }
            }
        };
    }

    private void initAnimator() {
        mSearchingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        mSearchingAnimator.addUpdateListener(mUpdateListener);
        mSearchingAnimator.addListener(mAnimatorListener);
        mSearchingAnimator.setRepeatCount(Integer.MAX_VALUE);
        mStartingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        mStartingAnimator.addUpdateListener(mUpdateListener);
        mStartingAnimator.addListener(mAnimatorListener);
        mEndingAnimator = ValueAnimator.ofFloat(1, 0).setDuration(defaultDuration);
        mEndingAnimator.addUpdateListener(mUpdateListener);
        mEndingAnimator.addListener(mAnimatorListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }


    private void drawSearch(Canvas canvas) {
        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        canvas.drawColor(Color.parseColor("#0082D7"));

        switch (mCurrentState) {
            case NONE:
                canvas.drawPath(path_search, mPaint);
                break;
            case STARTING:
                mMeasure.setPath(path_search, false);
                //截取path路径
                Path dst = new Path();
                mMeasure.getSegment(mMeasure.getLength() * mAnimatedValue, mMeasure.getLength(), dst, true);
                canvas.drawPath(dst, mPaint);
                break;
            case SEARCHING:
                mMeasure.setPath(path_circle, false);
                Path dst2 = new Path();
                //以当前执行到的点作为终点
                float stop = mMeasure.getLength() * mAnimatedValue;
                float start = (float) (stop - ((0.5 - Math.abs(mAnimatedValue - 0.5)) * (mMeasure.getLength()/4)));
                mMeasure.getSegment(start, stop, dst2, true);
                canvas.drawPath(dst2, mPaint);
                break;
            case ENDING:
                mMeasure.setPath(path_search, false);
                Path dst3 = new Path();
                mMeasure.getSegment(mMeasure.getLength() * mAnimatedValue, mMeasure.getLength(), dst3, true);
                canvas.drawPath(dst3, mPaint);
                break;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSearch(canvas);
//        AnimatedVectorDrawable vectorDrawable = new AnimatedVectorDrawable();
//        vectorDrawable.
    }
}
