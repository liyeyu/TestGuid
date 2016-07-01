package com.example.administrator.testguid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Liyeyu on 2016/5/26.
 */
public class PathView extends View {
    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
//        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
//        path.moveTo(100, 320);//设置Path的起点
//        path.quadTo(150, 310, 170, 400);  //设置路径点和终点
        path.moveTo(100,100);
        path.quadTo(150,110,200,300);
        canvas.drawPath(path,paint);
    }
}
