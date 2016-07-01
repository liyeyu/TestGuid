package com.example.administrator.testguid.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.example.administrator.testguid.R;

/**
 * 深入了解path
 *
 * Created by Liyeyu on 2016/6/6.
 */
public class PathDevView extends View {

    private WindowManager mManager;
    private int mHeight;
    private int mWidth;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private PathMeasure mMeasure;
    private float currentValue;
    private float[] pos = new float[2];
    private float[] tan = new float[2];
    private Path mPath;
    private Paint mPaint;
    private SurfaceHolder mHolder;
    private Canvas mLockCanvas;

    public PathDevView(Context context) {
        super(context);
    }

    public PathDevView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(metrics);
        mHeight = metrics.heightPixels;
        mWidth = metrics.widthPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;       // 缩放图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cover, options);
        mMatrix = new Matrix();
        mMeasure = new PathMeasure();
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
//        mLockCanvas = mHolder.lockCanvas();
//        startAnimation();
    }

    private void drawCanvas(Canvas canvas){
        //将画布移动到屏幕中心，作为坐标系原点
//        Canvas canvas = mHolder.lockCanvas();
        canvas.translate(mWidth/2,mHeight/2);
        mPath.addCircle(0,0,200,Path.Direction.CW);//顺时针添加圆
        mMeasure.setPath(mPath,false);
        currentValue += 0.005f;                                  // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0.005f;
        }
//        //获取坐标点和正切值
//        mMeasure.getPosTan(mMeasure.getLength() * currentValue,pos,tan);
//        mMatrix.reset();
//        //根据正切值计算方位角
//        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
//        //以自身中点为圆心旋转图片
//        mMatrix.postRotate(degrees,mBitmap.getWidth()/2,mBitmap.getHeight()/2);
//        //相对自身中点位移到当前坐标
//        mMatrix.postTranslate(pos[0]-mBitmap.getWidth()/2,pos[1]-mBitmap.getHeight()/2);

        //取矩阵变换的值，包含坐标和正切信息
        mMeasure.getMatrix(mMeasure.getLength() * currentValue,mMatrix,PathMeasure.POSITION_MATRIX_FLAG|PathMeasure.TANGENT_MATRIX_FLAG);
        //坐标变换前先平移，调整中心点，矩阵前乘
        mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);

        canvas.drawPath(mPath,mPaint);
        canvas.drawBitmap(mBitmap,mMatrix,mPaint);
//        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCanvas(canvas);
        invalidate();
    }

    private void startAnimation(){
        ValueAnimator animator = ValueAnimator.ofFloat(0,mMeasure.getLength());
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(5000);
        animator.setRepeatCount(30);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                float animatedValue = (float) animation.getAnimatedValue();
//                mMeasure.getPosTan(animatedValue*currentValue,pos,tan);
            }
        });
        animator.start();
    }
}
