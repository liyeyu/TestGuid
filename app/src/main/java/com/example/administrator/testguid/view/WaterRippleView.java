package com.example.administrator.testguid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.testguid.UiUtils;

/**
 * Created by Liyeyu on 2016/5/26.
 */
public class WaterRippleView extends View {
    // 波纹颜色
    private static final int WAVE_PAINT_COLOR = 0x880000aa;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 5;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 3;
    // mXOneOffset代表当前第一条水波纹要移动的距离-px增量
    private final int mXOffsetSpeedOne;
    // mXOneOffset代表当前第二条水波纹要移动的距离-px增量
    private final int mXOffsetSpeedTwo;
    private final Paint mWavePaint;
    private final PaintFlagsDrawFilter mDrawFilter;
    // 正弦曲线 y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 20;
    private static final int OFFSET_Y = 0;

    //View的宽高
    private int mTotalWidth, mTotalHeight;
    //记录Y轴坐标点
    private float[] mYPositions;
    //第一条波纹的Y轴坐标点
    private float[] mResetOneYPositions;
    //第二条波纹的Y轴坐标点
    private float[] mResetTwoYPositions;
    private float mCycleFactorW;
    private int mXOneOffset;
    private int mXTwoOffset;

    public WaterRippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mXOffsetSpeedOne = UiUtils.dipToPx(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = UiUtils.dipToPx(context, TRANSLATE_X_SPEED_TWO);

        mWavePaint = new Paint();
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);

        //抗锯齿
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(mDrawFilter);
        resetPositionY();

        //画线
        for (int i = 0; i < mTotalWidth; i++) {

            canvas.drawLine(i, mTotalHeight - 400 - mResetOneYPositions[i] , i,
                    mTotalHeight, mWavePaint);

            // 绘制第二条水波纹
            canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] - 400, i,
                    mTotalHeight, mWavePaint);
        }

        // 改变两条波纹的移动点 偏移量 = 增量累计
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }

        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        postInvalidate();
    }

    /**
     * 更新Y轴
     */
    private void resetPositionY() {
        //第一条波浪线移动的Y轴的量 余量 = 总量 - 偏移量
        int yOneInterval  = mYPositions.length - mXOneOffset ;
        //复制原数组的
        // 保留在屏幕内的
        System.arraycopy(mYPositions,mXOneOffset ,mResetOneYPositions ,0,yOneInterval);
        System.arraycopy(mYPositions,0,mResetOneYPositions,yOneInterval,mXOneOffset );

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions,mXTwoOffset,mResetTwoYPositions,0,yTwoInterval);
        System.arraycopy(mYPositions,0,mResetTwoYPositions,yTwoInterval,mXTwoOffset);


    }

    /**
     * 水波纹控件大小
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalHeight = h;
        mTotalWidth = w;

        //水平变化的波纹，应当记录mTotalWidth组Y坐标点
        mYPositions = new float[mTotalWidth];
        mResetOneYPositions = new float[mTotalWidth];
        mResetTwoYPositions = new float[mTotalWidth];
        //三角函数正弦波动周期,以屏幕宽度作为一个周期正好续接动画效果
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);
        post(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<mTotalWidth;i++){
                    //正弦曲线求值，假设振幅是20，偏移量是0
                    mYPositions[i] = (float) (STRETCH_FACTOR_A*Math.sin(mCycleFactorW*i)+OFFSET_Y);
                }
            }
        });
    }
}
