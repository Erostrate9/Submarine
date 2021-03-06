package com.example.submarine.background;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.submarine.R;

public class BarView extends View {
    Bitmap bitmap_top;
    Bitmap bitmap_bottom;
    Paint paint;
    public float barWidth,barHeight;
    public float x,y_top,y_bottom;
    public float y_offset;
    float margin=400;

    public BarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //to read photo file
        bitmap_top = BitmapFactory.decodeResource(getResources(), R.drawable.bar_top);
        bitmap_bottom=BitmapFactory.decodeResource(getResources(), R.drawable.bar_bottom);
        barHeight=bitmap_top.getHeight();
        barWidth=bitmap_top.getWidth();
        x = -barWidth;
        y_offset = 0;
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap_top,x,y_top,paint);
        canvas.drawBitmap(bitmap_bottom,x,y_bottom,paint);
    }
}
