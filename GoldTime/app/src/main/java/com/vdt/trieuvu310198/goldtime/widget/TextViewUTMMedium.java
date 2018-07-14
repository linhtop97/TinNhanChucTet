package com.vdt.trieuvu310198.goldtime.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Binh NK on 3/21/2017.
 */

@SuppressLint("AppCompatCustomView")
public class TextViewUTMMedium extends TextView {
    public TextViewUTMMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewUTMMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewUTMMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("UTMSwissCondensed.TTF", context);
        setTypeface(customFont);
    }

}
