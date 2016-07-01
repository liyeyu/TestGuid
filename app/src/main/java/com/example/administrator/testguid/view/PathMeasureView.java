package com.example.administrator.testguid.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Liyeyu on 2016/6/4.
 */
public class PathMeasureView extends View {

    // 起始点
    private static final int[] START_POINT = new int[]{
            300, 270
    };
    // 爱心下端点
    private static final int[] BOTTOM_POINT = new int[]{
            300, 400
    };
    // 左侧控制点
    private static final int[] LEFT_CONTROL_POINT = new int[]{
            120, 210
    };
    // 右侧控制点
    private static final int[] RIGHT_CONTROL_POINT = new int[]{
            480, 210
    };
    private float[] mCurrentPosition = new float[2];
    private Path mPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;

    public PathMeasureView(Context context) {
        super(context);
    }

    public PathMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(mPath, mPaint);
        //绘制控制点
        canvas.drawCircle(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], 5, mPaint);
        canvas.drawCircle(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], 5, mPaint);
        //绘制移动的圆
        canvas.drawCircle(mCurrentPosition[0], mCurrentPosition[1], 10, mPaint);
    }

    protected void initPath() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);

        mPath = new Path();
        //相対移动m
        mPath.moveTo(START_POINT[0], START_POINT[1]);
        //控制点和目标点
        mPath.quadTo(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], BOTTOM_POINT[0], BOTTOM_POINT[1]);
        mPath.quadTo(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], START_POINT[0], START_POINT[1]);
        //是否闭合路径测量
        mPathMeasure = new PathMeasure(mPath,true);
        startPathAnim(3000);
    }

    // 开启路径动画
    public void startPathAnim(long duration) {
        //从0到path的总长
        ValueAnimator animator = ValueAnimator.ofFloat(0,mPathMeasure.getLength());
        //匀速线性插值器
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //当前动画播放位置
                float animatedValue = (float) animation.getAnimatedValue();
                //从当前动画位置获取当前的坐标点
                mPathMeasure.getPosTan(animatedValue,mCurrentPosition,null);
                postInvalidate();
            }
        });
        animator.setRepeatCount(30);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }
}
