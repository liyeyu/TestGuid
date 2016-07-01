package com.example.administrator.testguid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 双缓冲界面绘制
 * Created by Liyeyu on 2016/6/4.
 */
public class PathSurfaceView extends SurfaceView {
    private Paint mPaint;
    private SurfaceHolder mSurfaceHolder;
    private Canvas mLockCanvas;
    private Path mPath;
    private float mPreX;
    private float mPreY;
    private final Rect mInvalidRect = new Rect();
    private boolean isDrawing;
    private float mMoveX,mMoveY;

    public PathSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        mSurfaceHolder = getHolder();
        mPath = new Path();
    }

    public PathSurfaceView(Context context) {
        super(context);
    }


    private void drawCanvas() {
        //锁定画布
        mLockCanvas = mSurfaceHolder.lockCanvas();
        mLockCanvas.drawColor(Color.WHITE);
        mLockCanvas.drawPath(mPath, mPaint);
        //解锁提交修改
        mSurfaceHolder.unlockCanvasAndPost(mLockCanvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isDrawing) {
                    touchMove(event);
                    invalidate();
//                    Rect rect = touchMove(event);
//                    if (rect != null) {
//                        invalidate(rect);
//                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrawing)
                {
                    touchUp(event);
                    invalidate();
                    return true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private void touchDown(MotionEvent event) {
        isDrawing = true;
        mPath.reset();
        float mStartX = event.getX();
        float mStartY = event.getY();
        //前一个点
        mPreX = mStartX;
        mPreY = mStartY;
        mPath.moveTo(mStartX, mStartY);
    }

    private Rect touchMove(MotionEvent event) {
        mMoveX = event.getX();
        mMoveY = event.getY();
        //控制移动距离大于3才绘制
        float dx = Math.abs(mMoveX - mPreX);
        float dy = Math.abs(mMoveY - mPreY);

        if (dx > 3 || dy > 3) {

            mInvalidRect.set((int) mPreX , (int) mPreY ,
                    (int) mPreX, (int) mPreY);

            //将每次移动细分看作是直线，那么以移动距离的一半作为控制点
            float mControlX = (mMoveX + mPreX) / 2;
            float mControlY = (mMoveY + mPreY) / 2;
            mPath.quadTo(mControlX, mControlY, mMoveX, mMoveY);
            // union 创建一个容纳两个矩形的外包矩形
            /*areaToRefresh矩形扩大了border(宽和高扩大了两倍border)，
             * border值由设置手势画笔粗细值决定
             */
            mInvalidRect.union((int) mPreX, (int) mPreY,
                    (int) mPreX, (int) mPreY);
            mInvalidRect.union((int) mMoveX, (int) mMoveY ,
                    (int) mMoveX, (int) mMoveY);
            mPreX = mMoveX;
            mPreY = mMoveY;
            drawCanvas();
        }
        return mInvalidRect;
    }
    private void touchUp(MotionEvent event)
    {
        isDrawing = false;
    }
}
