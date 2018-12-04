package com.example.linhnb.ledbanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;

public class MyTextView extends View {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint();
        Rect r = new Rect();
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(705);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Bungee-Regular.otf");
        textPaint.setTypeface(typeface);
        textPaint.getTextBounds("Hello", 0, 5, r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText("HELLO", 0, y, textPaint);

//        Bitmap bm = StringToBitMap("Hello");
//        canvas.drawBitmap(bm, 0, 0, textPaint);

    }


}
