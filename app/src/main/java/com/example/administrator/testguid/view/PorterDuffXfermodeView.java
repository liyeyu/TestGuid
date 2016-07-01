package com.example.administrator.testguid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.testguid.R;
import com.example.administrator.testguid.UiUtils;

/**
 * 混合图像模式实现水波纹
 * Created by Liyeyu on 2016/6/1.
 */
public class PorterDuffXfermodeView extends View {
    private static final int WAVE_TRANS_SPEED = 5;
    private Bitmap mSrcBitmap,mMaskBitmap;
    private Paint mBitmapPaint;
    private int mTotalWidth;
    private int mTotalHeight;
    private final PaintFlagsDrawFilter mDrawFilter;
    private Rect mSrcRect, mDestRect;
    private Rect mMaskSrcRect, mMaskDestRect;
    private final int mSpeed;
    private int mCurrentPosition;
    private int mCenterX;
    private final PorterDuffXfermode mXfermode;
    private int mCenterY;


    public PorterDuffXfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBitmap();
        initPaint();
        //绘制层面抗锯齿防抖动
        mDrawFilter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG,Paint.FILTER_BITMAP_FLAG);
        mSpeed = UiUtils.dipToPx(getContext(), WAVE_TRANS_SPEED);
        //波纹图片作为背景下层图片，取背景交集
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

        new Thread(){
            @Override
            public void run() {
                while (true){
                    mCurrentPosition += mSpeed;
                    if(mCurrentPosition>=mSrcBitmap.getWidth()){
                        mCurrentPosition = 0;
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.setDrawFilter(mDrawFilter);
//        canvas.drawColor(Color.TRANSPARENT);

        //缓存canvas配置
        int sc = canvas.saveLayer(0, 0, mTotalWidth, mTotalHeight, null, Canvas.ALL_SAVE_FLAG);
        // 设定要绘制的波纹部分
        mSrcRect.set(mCurrentPosition, 0, mCurrentPosition + mCenterX, mTotalHeight);
        // 绘制波纹部分
        canvas.drawBitmap(mSrcBitmap,mSrcRect,mDestRect,mBitmapPaint);
        // 设置图像的混合模式
        mBitmapPaint.setXfermode(mXfermode);
        //绘制遮罩圆
        canvas.drawBitmap(mMaskBitmap,mMaskSrcRect,mMaskDestRect,mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        //读取之前保存的canvas配置
        canvas.restoreToCount(sc);
    }

    private void initBitmap() {
        //波纹图
        mSrcBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.wave_2000))
                .getBitmap();
        //圆形图
        mMaskBitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.circle_500))
                .getBitmap();
    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        //防抖动
        mBitmapPaint.setDither(true);
        //开启图像过滤
        mBitmapPaint.setFilterBitmap(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;

        mCenterX = mTotalWidth / 2;
        mCenterY = mTotalHeight / 2;
        //前景矩形
        mSrcRect=new Rect();
        //背景矩形
        mDestRect = new Rect(0,0,mTotalWidth,mTotalHeight);
        int maskWidth = mMaskBitmap.getWidth();
        int maskHeight = mMaskBitmap.getHeight();
        mMaskSrcRect = new Rect(0, 0, maskWidth, maskHeight);
        mMaskDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);
    }

}
