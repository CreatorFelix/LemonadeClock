package com.creator.lemonade.clock.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
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
    private Paint mHandPaint;
    private Paint mHourTextPaint;
    private Paint mMinuteTextPaint;
    private Paint mSecondTextPaint;
    private Paint mMillisTextPaint;

    public StopwatchDrawable() {
        initPaint();
    }

    private void initPaint() {
        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
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
    }

    @Override
    public void setAlpha(int alpha) {
        mDialPaint.setAlpha(alpha);
        mHandPaint.setAlpha(alpha);
        mHourTextPaint.setAlpha(alpha);
        mMinuteTextPaint.setAlpha(alpha);
        mSecondTextPaint.setAlpha(alpha);
        mMillisTextPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mDialPaint.setColorFilter(colorFilter);
        mHandPaint.setColorFilter(colorFilter);
        mHourTextPaint.setColorFilter(colorFilter);
        mMinuteTextPaint.setColorFilter(colorFilter);
        mSecondTextPaint.setColorFilter(colorFilter);
        mMillisTextPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }
}
