package com.example.administrator.testguid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Liyeyu on 2016/6/3.
 */
public class PathTestView extends View {
    public PathTestView(Context context) {
        super(context);
    }

    public PathTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path = new Path();
        path.addCircle(300,300,150, Path.Direction.CW);
        path.addCircle(380,380,150, Path.Direction.CW);
        //path的填充模式
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        //path切换逆填充模式
        //path.toggleInverseFillType();

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        canvas.drawPath(path,mPaint);
    }
}
