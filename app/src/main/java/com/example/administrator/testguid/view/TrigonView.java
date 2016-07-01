package com.example.administrator.testguid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/5/26.
 */
public class TrigonView extends View {
    public TrigonView(Context context) {
        super(context);
    }

    public TrigonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //实例化路径
        Path path = new Path();
        path.moveTo(100,100);//起点
        //顺序途经点
        path.lineTo(200,200);
        path.lineTo(100,200);
        //与起点闭合
        path.close();
        canvas.drawPath(path,paint);
    }
}
