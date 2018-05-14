package com.creator.lemonade.clock.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;

import com.creator.lemonade.clock.base.AbsClockDrawable;

/**
 * This Class defines how to draw a stopwatch
 *
 * @author Felix.Liang
 */
public class StopwatchDrawable extends AbsClockDrawable {

    private long mCurrentTime;
    private Paint mDialPaint;
    private Paint mMinuteHandPaint;
    private Paint mSecondHandPaint;
    private Paint mMinuteTextPaint;
    private Paint mSecondTextPaint;
    private Paint mMillisTextPaint;

    private int mColorMinuteHand;
    private int mColorSecondHand;
    private int mColorDial;
    private int mColorSecondText;
    private int mColorMillisText;
    private int mColorMinuteText;

    public StopwatchDrawable() {
        initPaint();
    }

    private void initPaint() {
        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinuteTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSecondTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mMillisTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setTime(long timeInMillis) {
        if (mCurrentTime != timeInMillis) {
            mCurrentTime = timeInMillis;
            invalidateSelf();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawDial(canvas);
        drawSecond(canvas);
        drawTextTime(canvas);
    }

    private void drawDial(Canvas canvas) {
    }

    private void drawSecond(Canvas canvas) {
    }

    private void drawTextTime(Canvas canvas) {
    }

    @Override
    public void setAlpha(int alpha) {
        mDialPaint.setAlpha(alpha);
        mMinuteHandPaint.setAlpha(alpha);
        mSecondHandPaint.setAlpha(alpha);
        mMinuteTextPaint.setAlpha(alpha);
        mSecondTextPaint.setAlpha(alpha);
        mMillisTextPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mDialPaint.setColorFilter(colorFilter);
        mMinuteHandPaint.setColorFilter(colorFilter);
        mSecondHandPaint.setColorFilter(colorFilter);
        mMinuteTextPaint.setColorFilter(colorFilter);
        mSecondTextPaint.setColorFilter(colorFilter);
        mMillisTextPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public void setMinuteHandColor(int newColor) {
        if (mColorMinuteHand != newColor) {
            mMinuteHandPaint.setColor(newColor);
            mColorMinuteHand = newColor;
            invalidateSelf();
        }
    }

    public void setSecondHandColor(@ColorInt int newColor) {
        if (mColorSecondHand != newColor) {
            mSecondHandPaint.setColor(newColor);
            mColorSecondHand = newColor;
            invalidateSelf();
        }
    }

    public void setDialColor(@ColorInt int newColor) {
        if (mColorDial != newColor) {
            mDialPaint.setColor(newColor);
            mColorDial = newColor;
            invalidateSelf();
        }
    }

    public void setSecondTextColor(@ColorInt int newColor) {
        if (mColorSecondText != newColor) {
            mSecondTextPaint.setColor(newColor);
            mColorSecondText = newColor;
            invalidateSelf();
        }
    }

    public void setMillisTextColor(@ColorInt int newColor) {
        if (mColorMillisText != newColor) {
            mMillisTextPaint.setColor(newColor);
            mColorMillisText = newColor;
            invalidateSelf();
        }
    }

    public void setMinuteTextColor(@ColorInt int newColor) {
        if (mColorMinuteText != newColor) {
            mMinuteTextPaint.setColor(newColor);
            mColorMinuteText = newColor;
            invalidateSelf();
        }
    }
}
