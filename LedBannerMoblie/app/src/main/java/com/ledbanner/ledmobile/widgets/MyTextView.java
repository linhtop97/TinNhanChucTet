package com.ledbanner.ledmobile.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.ledbanner.ledmobile.data.local.sharedprf.SharedPrefsImpl;
import com.ledbanner.ledmobile.data.local.sharedprf.SharedPrefsKey;

public class MyTextView extends android.support.v7.widget.AppCompatTextView {

    private AnimationSet mAnimationSet;
    private SharedPrefsImpl mSharedPrefs;

    public boolean isRTL() {
        return isRTL;
    }

    private boolean isRTL;
    // scrolling feature
    private Scroller mSlr;

    // milliseconds for a round of scrolling
    private int mRndDuration = 10000;

    // the X offset when paused
    private int mXPaused = 0;

    // whether it's being paused
    private boolean mPaused = true;

    public MyTextView(Context context) {
        super(context);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setIncludeFontPadding(false);
        setVisibility(GONE);
        mAnimationSet = new AnimationSet(true);
        mSharedPrefs = new SharedPrefsImpl(getContext());
        isRTL = mSharedPrefs.get(SharedPrefsKey.PREF_STYLE_SHOW, Boolean.class);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void startScroll() {
        setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
        mXPaused = -1 * getWidth();
        mPaused = true;
        if (isRTL) {
            resumeScroll();
        } else {
            resumeScrollRight();
        }
    }

    public void resumeScroll() {
        setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
        isRTL = true;
        mSharedPrefs.put(SharedPrefsKey.PREF_STYLE_SHOW, isRTL);
        if (!mPaused) return;


        setHorizontallyScrolling(true);

        mSlr = new Scroller(this.getContext(), new LinearInterpolator());
        setScroller(mSlr);

        int scrollingLen = calculateScrollingLen();
        int distance = scrollingLen - (getWidth() + mXPaused);
        int duration = (Double.valueOf(mRndDuration * distance * 1.00000
                / scrollingLen)).intValue();

        setVisibility(VISIBLE);
        mSlr.startScroll(mXPaused, 0, distance, 0, duration);
        invalidate();
        mPaused = false;
    }

    public void resumeScrollRight() {
        setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
        isRTL = false;
        mSharedPrefs.put(SharedPrefsKey.PREF_STYLE_SHOW, isRTL);
        if (!mPaused)
            return;

        setHorizontallyScrolling(true);

        mSlr = new Scroller(this.getContext(), new LinearInterpolator());
        setScroller(mSlr);

        int scrollingLen = calculateScrollingLen();
        int distance = scrollingLen - (getWidth() + mXPaused);
        int duration = (Double.valueOf(mRndDuration * distance * 1.00000 / scrollingLen)).intValue();
        setVisibility(VISIBLE);
        if (mXPaused == 0) {
            mSlr.startScroll(distance, 0, -distance, 0, duration);
        } else {
            mSlr.startScroll(distance + mXPaused, 0, -distance, 0, duration);
        }
        invalidate();
        mPaused = false;
    }

    /**
     * calculate the scrolling length of the text in pixel
     *
     * @return the scrolling length in pixels
     */
    private int calculateScrollingLen() {
        TextPaint tp = getPaint();
        Rect rect = new Rect();
        String strTxt = getText().toString();
        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
        int scrollingLen = rect.width() + getWidth();
        rect = null;
        return scrollingLen;
    }

    /**
     * pause scrolling the text
     */
    public void pauseScroll() {
        if (null == mSlr) return;

        if (mPaused)
            return;

        mPaused = true;

        // abortAnimation sets the current X to be the final X,
        // and sets isFinished to be true
        // so current position shall be saved
        //mXPaused = mSlr.getCurrX();
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        mSlr.abortAnimation();
    }

    public void stopScroll() {
        mSlr = null;
    }

    @Override
    /*
     * override the computeScroll to restart scrolling when finished so as that
     * the text is scrolled forever
     */
    public void computeScroll() {
        super.computeScroll();

        if (null == mSlr) return;

        if (mSlr.isFinished() && (!mPaused)) {
            startScroll();

        }
    }

    public int getRndDuration() {
        return mRndDuration;
    }

    public void setRndDuration(int duration) {
        this.mRndDuration = duration;
        if (isRTL) {
            pauseScroll();
            resumeScroll();
        } else {
            pauseScroll();
            resumeScrollRight();
        }
    }

    public boolean isPaused() {
        return mPaused;
    }


    public void addAnimationBlinking() {
        Animation animation = new AlphaAnimation(0.2f, 1f);
        animation.setDuration(300);
        animation.setFillAfter(false);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        mAnimationSet.addAnimation(animation);

    }

    public AnimationSet getAnimationSet() {
        return mAnimationSet;
    }
}