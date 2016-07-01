package com.example.administrator.testguid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 模仿手写输入
 * Created by Liyeyu on 2016/6/4.
 */
public class PathTouchView extends View implements View.OnTouchListener{

    private Paint mPaint;
    private Path mPath;
    private float mPreX;
    private float mPreY;
    private float mMoveX;
    private float mMoveY;
    private long time;

    public PathTouchView(Context context) {
        super(context);
        initPaint();
    }

    public PathTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void touchMove(MotionEvent event) {
        mMoveX = event.getX();
        mMoveY = event.getY();
        //控制移动距离大于3才绘制
         float dx = Math.abs(mMoveX - mPreX);
         float dy = Math.abs(mMoveY - mPreY);

        if(dx>3||dy>3){
            //将每次移动细分看作是直线，那么以移动距离的一半作为控制点
            float mControlX = (mMoveX + mPreX)/2;
            float mControlY = (mMoveY + mPreY)/2;
            mPath.quadTo(mControlX,mControlY,mMoveX,mMoveY);
            mPreX = mMoveX;
            mPreY = mMoveY;
        }

    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        mPath = new Path();
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath,mPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()){
            case  MotionEvent.ACTION_DOWN:
                long timeMillis = System.currentTimeMillis();
                if(time - timeMillis>5*1000){
                    mPath.reset();
                }
                time = timeMillis;
                touchDown(event);
                break;
            case  MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case  MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }

    private void touchDown(MotionEvent event) {
//        mPath.reset();
        float mStartX = event.getX();
        float mStartY = event.getY();
        //前一个点
        mPreX = mStartX;
        mPreY = mStartY;
        mPath.moveTo(mStartX,mStartY);
    }

}
