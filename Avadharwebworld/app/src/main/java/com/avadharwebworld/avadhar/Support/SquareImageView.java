package com.avadharwebworld.avadhar.Support;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by user-03 on 07-Dec-16.
 */

public class SquareImageView extends ImageView {
    private static Paint paint;
    public SquareImageView(Context context) {
        super(context);
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

}