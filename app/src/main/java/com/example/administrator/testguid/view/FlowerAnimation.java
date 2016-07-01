package com.example.administrator.testguid.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import com.example.administrator.testguid.R;
import com.example.administrator.testguid.entity.Flower;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Liyeyu on 2016/6/6.
 */
public class FlowerAnimation extends View implements ValueAnimator.AnimatorUpdateListener {

    private WindowManager mManager;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    /**
     * 第一批个数
     */
    private int flowerCount = 12;
    private List<Flower> flowers1 = new ArrayList<>();
    //每朵花的y轴坐标，都是隐匿上上方
    private int[] yPoints = {-100, -50, -25, 0};
    /**
     * 曲线摇摆的幅度
     */
    private int range = (int) TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources()
                    .getDisplayMetrics());
    /**
     * 曲线高度个数分割
     */
    private int quadCount = 10;
    /**
     * 曲度
     */
    private float intensity = 0.2f;
    /**
     * 高度往上偏移量,把开始点移出屏幕顶部
     */
    private float dy = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            40, getResources().getDisplayMetrics());

    public FlowerAnimation(Context context) {
        super(context);
    }

    public FlowerAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        //获取屏幕宽高
        mManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels * 3 / 2;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPathMeasure = new PathMeasure();
        builderFollower(flowerCount, flowers1);

    }

    private void builderFollower(int count, List<Flower> flowers) {

        //花落下范围，最小为1/4
        Random random = new Random();
        int max = mWidth * 3 / 4;
        int min = mWidth / 4;
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(max) % (max - min) + min;
            Flower flower = new Flower();
            Path path = new Path();
            //第一条路径的初始位置
            CPoint cPoint = new CPoint(x, yPoints[(count%yPoints.length)]*(i%yPoints.length));
            //生成每路径10个坐标点
            List<CPoint> points = builderPath(cPoint);
            drawFlowerPath(path, points);
            flower.setPath(path);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_my_collect);
            flower.setImage(bitmap);
            flowers.add(flower);
        }
    }

    /**
     * 画路径
     *
     * @param point
     * @return
     */
    private List<CPoint> builderPath(CPoint point) {
        List<CPoint> points = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < quadCount; i++) {
            if (i == 0) {
                points.add(point);
            } else {
                CPoint tmp = new CPoint(0, 0);
                if (random.nextInt(100) % 2 == 0) {
                    tmp.x = point.x + random.nextInt(range);
                } else {
                    tmp.x = point.x - random.nextInt(range);
                }
                tmp.y = (int) (mHeight / (float) quadCount * i);
                points.add(tmp);
            }
        }
        return points;
    }

    /**
     * 画曲线
     *
     * @param path
     * @param points
     */
    private void drawFlowerPath(Path path, List<CPoint> points) {
        if (points.size() > 1) {
            for (int j = 0; j < points.size(); j++) {

                CPoint point = points.get(j);
                //根据坐标点和曲率计算偏移量
                if (j == 0) {
                    CPoint next = points.get(j + 1);
                    point.dx = ((next.x - point.x) * intensity);
                    point.dy = ((next.y - point.y) * intensity);
                } else if (j == points.size() - 1) {
                    CPoint prev = points.get(j - 1);
                    point.dx = ((point.x - prev.x) * intensity);
                    point.dy = ((point.y - prev.y) * intensity);
                } else {
                    CPoint next = points.get(j + 1);
                    CPoint prev = points.get(j - 1);
                    point.dx = ((next.x - prev.x) * intensity);
                    point.dy = ((next.y - prev.y) * intensity);
                }

                // create the cubic-spline path
                if (j == 0) {
                    path.moveTo(point.x, point.y);
                } else {
                    //3阶贝塞尔曲线
                    CPoint prev = points.get(j - 1);
                    path.cubicTo(prev.x + prev.dx, (prev.y + prev.dy), point.x
                            - point.dx, (point.y - point.dy), point.x, point.y);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawFlower(canvas, flowers1);
    }

    /**
     * @param canvas
     * @param fllowers
     */
    private void drawFlower(Canvas canvas, List<Flower> fllowers) {
        for (Flower flower : fllowers) {
            float[] pos = new float[2];
            canvas.drawPath(flower.getPath(), mPaint);
            mPathMeasure.setPath(flower.getPath(), false);
            mPathMeasure.getPosTan(mHeight * flower.getValue(), pos, null);
            // canvas.drawCircle(pos[0], pos[1], 10, mPaint);
            canvas.drawBitmap(flower.getImage(), pos[0], pos[1] - dy, null);
        }
    }

    ObjectAnimator mAnimator1;
    /**
     * 动画播放的时间
     */
    private int time = 4000;
    /**
     * 动画间隔
     */
    private int delay = 400;

    public void startAnimation() {
        if (mAnimator1 != null && mAnimator1.isRunning()) {
            mAnimator1.cancel();
        }
        mAnimator1 = ObjectAnimator.ofFloat(this, "phase1", 0f, 1f);
        mAnimator1.setDuration(time);
        mAnimator1.addUpdateListener(this);
        mAnimator1.setRepeatCount(100);
        mAnimator1.setRepeatMode(ObjectAnimator.RESTART);
        mAnimator1.start();
        mAnimator1.setInterpolator(new AccelerateInterpolator(1f));

    }

    /**
     * 更新小球的位置
     *
     * @param value
     * @param fllowers
     */
    private void updateValue(float value, List<Flower> fllowers) {
        for (Flower flower : fllowers) {
            flower.setValue(value);
        }
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        updateValue((Float) animation.getAnimatedValue(), flowers1);
        invalidate();
    }

    private class CPoint {

        public float x = 0f;
        public float y = 0f;

        /**
         * x-axis distance
         */
        public float dx = 0f;

        /**
         * y-axis distance
         */
        public float dy = 0f;

        public CPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
