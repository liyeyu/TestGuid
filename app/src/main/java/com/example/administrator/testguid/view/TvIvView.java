package com.example.administrator.testguid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.testguid.R;

/**
 * Created by Liyeyu on 2016/5/26.
 */
public class TvIvView extends View {
    public TvIvView(Context context) {
        super(context);
    }

    public TvIvView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(55);
        canvas.drawText("自定义",500,500,paint);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //图片
        canvas.drawBitmap(bitmap, 500, 500, paint);
    }
}
