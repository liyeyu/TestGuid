package com.example.administrator.testguid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.administrator.testguid.R;

/**
 * Created by Liyeyu on 2016/8/5.
 */
public class RoundImageView extends ImageView {

    private float mBorderThickness;
    private int mBorderInsideColor;
    private int mBorderOutsideColor;
    private int defaultColor = 0xFFFFFFFF;
    // 控件默认长、宽
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    private Paint mPaint;

    public RoundImageView(Context context) {
        super(context);
        initView(null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }


    private void initView(AttributeSet attrs) {
        if(attrs!=null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.rounded_image_view);
            mBorderThickness = a.getDimension(R.styleable.rounded_image_view_border_thickness,0);
            mBorderInsideColor = a.getColor(R.styleable.rounded_image_view_border_inside_color,defaultColor);
            mBorderOutsideColor = a.getColor(R.styleable.rounded_image_view_border_outside_color,defaultColor);
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();
        Drawable drawable = getDrawable();
        if(drawable==null){
            return;
        }
        if(getWidth()==0||getHeight()==0){
            return;
        }
        if(drawable.getClass() == NinePatchDrawable.class){
            return;
        }
        this.measure(0,0);
        if(defaultWidth==0){
            defaultWidth = getWidth();
        }
        if(defaultHeight==0){
            defaultHeight = getHeight();
        }

        int inRadius;
        int outRadius = (defaultWidth>defaultWidth?defaultWidth:defaultWidth)/2;
        if(mBorderInsideColor!=defaultColor && mBorderOutsideColor!=defaultColor){
            inRadius = (int) (outRadius - 2*mBorderThickness);
            drawCircleBorder(canvas,inRadius,mBorderInsideColor);
//            drawCircleBorder(canvas,outRadius,mBorderInsideColor);
        }else if(mBorderInsideColor==defaultColor && mBorderOutsideColor!=defaultColor){
            inRadius = (int) (outRadius - mBorderThickness);
            drawCircleBorder(canvas,inRadius,mBorderInsideColor);
        }else if(mBorderInsideColor!=defaultColor && mBorderOutsideColor==defaultColor){
            inRadius = (int) (outRadius - mBorderThickness);
            drawCircleBorder(canvas,outRadius,mBorderOutsideColor);
        }else{
            inRadius = outRadius;
        }
        Bitmap src = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = getCroppedRoundBitmap(src.copy(Bitmap.Config.ARGB_8888,true), inRadius);
        canvas.drawBitmap(bitmap,outRadius-inRadius,outRadius-inRadius,null);
    }

    private void initPaint() {
        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        //防抖动
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeWidth(mBorderThickness);
    }

    /**
     * 绘制圆
     * @param canvas
     * @param radius
     * @param color
     */
    private void drawCircleBorder(Canvas canvas, int radius, int color) {
        mPaint.setColor(color);
        canvas.drawCircle(defaultWidth/2,defaultHeight/2,radius,mPaint);
    }

    /**
     * 将bitmap裁剪成圆形
     * @param bmp
     * @param radius
     * @return
     */
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        //获取原始图片的宽高，防止拉伸，取矩形中心的内正方形
        int bmpRadius;
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int x,y;
        if(bmpWidth>bmpHeight){
            bmpRadius = bmpHeight;
            y = 0;
            x = (bmpWidth - bmpHeight)/2;
        }else{
            bmpRadius = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth)/2;
        }
        //生成正方形图片
        Bitmap rectBitmap = Bitmap.createBitmap(bmp, x, y, bmpRadius, bmpRadius);
        if(radius!=bmpRadius/2){
            rectBitmap = Bitmap.createScaledBitmap(rectBitmap,radius*2,radius*2,true);
        }
        bmpRadius = rectBitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(rectBitmap.getWidth(), rectBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        canvas.drawCircle(bmpRadius/2,bmpRadius/2,bmpRadius/2,mPaint);
//        canvas.drawCircle(bmpRadius / 2 + 0.7f, bmpRadius / 2 + 0.7f,
//                bmpRadius / 2 + 0.1f, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0,0,bmpRadius,bmpRadius);
        canvas.drawBitmap(rectBitmap,rect,rect,mPaint);
        return output;
    }
}
