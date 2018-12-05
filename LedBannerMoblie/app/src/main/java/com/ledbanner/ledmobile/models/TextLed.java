package com.ledbanner.ledmobile.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ledbanner.ledmobile.BR;

import java.io.Serializable;

public class TextLed extends BaseObservable implements Serializable {
    private String mContent;

    private int mSize;

    private boolean mIsBlinking;

    private boolean mIsLed;

    private boolean mIsRunning;

    private int mTextColor;

    private int mBackgroundColor;

    private boolean mIsRightToLeft;

    private long mTextSpeed;


    public TextLed(Builder builder) {
        this.mContent = builder.mContent;
        this.mSize = builder.mSize;
        this.mIsBlinking = builder.mIsBlinking;
        this.mIsLed = builder.mIsLed;
        this.mIsRunning = builder.mIsRunning;
        this.mTextColor = builder.mTextColor;
        this.mBackgroundColor = builder.mBackgroundColor;
        this.mIsRightToLeft = builder.mIsRightToLeft;
        this.mTextSpeed = builder.mTextSpeed;
    }


    public static class Builder {

        private String mContent;

        private int mSize;

        private boolean mIsBlinking;

        private boolean mIsLed;

        private boolean mIsRunning;

        private int mTextColor;

        private int mBackgroundColor;

        private boolean mIsRightToLeft;

        private long mTextSpeed;

        public Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public Builder setSize(int size) {
            mSize = size;
            return this;
        }

        public Builder setBlinking(boolean blinking) {
            mIsBlinking = blinking;
            return this;
        }

        public Builder setLed(boolean led) {
            mIsLed = led;
            return this;
        }

        public Builder setRunning(boolean running) {
            mIsRunning = running;
            return this;
        }

        public Builder setTextColor(int textColor) {
            mTextColor = textColor;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            mBackgroundColor = backgroundColor;
            return this;
        }

        public Builder setRightToLeft(boolean rightToLeft) {
            mIsRightToLeft = rightToLeft;
            return this;
        }

        public Builder setTextSpeed(long textSpeed) {
            mTextSpeed = textSpeed;
            return this;
        }

        public TextLed build() {
            return new TextLed(this);
        }
    }

    @Bindable
    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
        notifyPropertyChanged(BR.content);
    }

    @Bindable
    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
        notifyPropertyChanged(BR.size);
    }

    @Bindable
    public boolean isBlinking() {
        return mIsBlinking;
    }

    public void setBlinking(boolean blinking) {
        mIsBlinking = blinking;
        notifyPropertyChanged(BR.blinking);
    }

    @Bindable
    public boolean isLed() {
        return mIsLed;
    }

    public void setLed(boolean led) {
        mIsLed = led;
        notifyPropertyChanged(BR.led);
    }

    @Bindable
    public boolean isRunning() {
        return mIsRunning;
    }

    public void setRunning(boolean running) {
        mIsRunning = running;
        notifyPropertyChanged(BR.running);
    }

    @Bindable
    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        notifyPropertyChanged(BR.textColor);
    }

    @Bindable
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        notifyPropertyChanged(BR.backgroundColor);
    }

    @Bindable
    public boolean isRightToLeft() {
        return mIsRightToLeft;
    }

    public void setRightToLeft(boolean rightToLeft) {
        mIsRightToLeft = rightToLeft;
        notifyPropertyChanged(BR.rightToLeft);
    }

    @Bindable
    public long getTextSpeed() {
        return mTextSpeed;
    }

    public void setTextSpeed(long textSpeed) {
        mTextSpeed = textSpeed;
        notifyPropertyChanged(BR.textSpeed);
    }

}
