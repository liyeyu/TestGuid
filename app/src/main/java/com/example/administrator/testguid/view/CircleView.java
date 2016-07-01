package com.example.administrator.testguid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/5/26.
 */
public class CircleView extends View {
    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        paint.setAlpha(90);
        paint.setAntiAlias(true);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
//        if(metrics.widthPixels>=metrics.heightPixels){
//            canvas.drawCircle(metrics.widthPixels/2,metrics.heightPixels/2,metrics.heightPixels/2,paint);
//        }else{
//            canvas.drawCircle(metrics.widthPixels/2,metrics.heightPixels/2,metrics.widthPixels/2,paint);
//        }
//        canvas.drawArc(100,100,300,300,0,90,true,paint);
        RectF rectF = new RectF(100,200,300,400);
        canvas.drawArc(rectF,130,140,true,paint);
    }
}
