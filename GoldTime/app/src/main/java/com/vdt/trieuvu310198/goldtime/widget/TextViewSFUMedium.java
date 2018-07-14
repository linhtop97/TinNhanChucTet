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
public class TextViewSFUMedium extends TextView {
    public TextViewSFUMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewSFUMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewSFUMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("SFUAGBuchStencilBQMedium.TTF", context);
        setTypeface(customFont);
    }

}
