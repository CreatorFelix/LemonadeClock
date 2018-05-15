package com.creator.lemonade.clock.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;

import com.creator.lemonade.clock.base.AbsClockDrawable;

import java.util.Locale;

/**
 * This Class defines how to draw a stopwatch.
 *
 * @author Felix.Liang
 */
public class StopwatchDrawable extends AbsClockDrawable {

    /**
     * The initial offset degree of hand
     */
    private static final float INIT_DEGREE_OFFSET = 90;

    private long mCurrentTime;

    private Paint mDialPaint;
    private Paint mMinuteHandPaint;
    private Paint mSecondHandPaint;
    private Paint mHourTextPaint;
    private Paint mMinuteTextPaint;
    private Paint mSecondTextPaint;
    private Paint mMillisTextPaint;

    private int mColorMinuteHand;
    private int mColorSecondHand;
    private int mColorDial;
    private int mColorMillisText;
    private int mColorSecondText;
    private int mColorMinuteText;
    private int mColorHourText;

    private float mHourTextSize;
    private float mMinuteTextSize;
    private float mSecondTextSize;
    private float mMillisTextSize;

    private float mHourTextOffset;
    private float mMinTextOffset;
    private float mSecTextOffset;
    private float mMillisTextOffset;
    private float mDialRadius;
    private float mSecStrokeWidth;
    private float mMinStrokeWidth;
    private float mSecondHandRadius;
    private float mMinuteHandRadius;
    private float mTextExtraSpace;

    private float mSecondDeg;
    private float mMinuteDeg;

    private String mHour = null;
    private String mMinute = DEFAULT_TWO_DIGIT_TIME;
    private String mSecond = DEFAULT_TWO_DIGIT_TIME;
    private String mMillisecond = DEFAULT_TWO_DIGIT_TIME;

    public StopwatchDrawable() {
        initPaint();
    }

    private void initPaint() {
        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDialPaint.setStyle(Paint.Style.FILL);
        mMinuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinuteHandPaint.setStyle(Paint.Style.STROKE);
        mSecondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondHandPaint.setStyle(Paint.Style.STROKE);
        mSecondHandPaint.setStrokeCap(Paint.Cap.ROUND);
        mHourTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mMinuteTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSecondTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mMillisTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * Specify the time of stopwatch.
     *
     * @param timeInMillis time in milliseconds
     */
    public void setTime(long timeInMillis) {
        if (mCurrentTime != timeInMillis) {
            mCurrentTime = timeInMillis;
            onTimeChanged();
        }
    }

    /**
     * Called when the time of stopwatch is changed.
     */
    private void onTimeChanged() {
        final long timeInMillis = mCurrentTime;
        final long millis = timeInMillis % 1000;
        final long second = timeInMillis / 1000 % 60;
        final long minute = timeInMillis / 1000 / 60 % 60;
        final long hour = timeInMillis / 1000 / 60 / 60;
        float actualSecond = second + millis / 1000f;
        mSecondDeg = actualSecond / 60 * 360;
        float actualMinute = minute + actualSecond / 60f;
        mMinuteDeg = actualMinute / 60 * 360;
        if (hour <= 0) {
            mHour = null;
        } else if (hour < 10) {
            mHour = String.format(Locale.getDefault(), TWO_DIGIT_FORMAT, hour);
        } else {
            mHour = String.valueOf(TWO_DIGIT_FORMAT);
        }
        mMinute = String.format(Locale.getDefault(), TWO_DIGIT_FORMAT, minute);
        mSecond = String.format(Locale.getDefault(), TWO_DIGIT_FORMAT, second);
        mMillisecond = String.format(Locale.getDefault(), TWO_DIGIT_FORMAT, millis / 10);
        invalidateSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        final int shortAxis = getShortAxisLength();
        final float halfShortAxis = shortAxis * 0.5f;
        final float secondHandStrokeWidth = halfShortAxis * 0.06f;
        setSecondHandStrokeWidth(secondHandStrokeWidth);
        mSecondHandRadius = halfShortAxis - secondHandStrokeWidth / 2;
        mDialRadius = halfShortAxis * 0.95f - secondHandStrokeWidth;
        final float minuteHandStrokeWidth = halfShortAxis * 0.13f;
        setMinuteHandStrokeWidth(minuteHandStrokeWidth);
        mMinuteHandRadius = mDialRadius * 0.95f - minuteHandStrokeWidth / 2f;
        mTextExtraSpace = (mMinuteHandRadius * 2 - minuteHandStrokeWidth) * 0.9f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.save();
        canvas.rotate(-INIT_DEGREE_OFFSET);
        drawDial(canvas);
        drawHands(canvas);
        canvas.restore();
        drawTextTime(canvas);
    }

    /**
     * Draw the dial of stopwatch.
     *
     * @param canvas The canvas to draw into
     */
    private void drawDial(Canvas canvas) {
        canvas.drawCircle(0, 0, mDialRadius, mDialPaint);
    }

    /**
     * Draw all the hands.
     *
     * @param canvas The canvas to draw into
     */
    private void drawHands(Canvas canvas) {
        drawMinuteHand(canvas);
        drawSecondHand(canvas);
    }

    /**
     * Draw the minute hand.
     *
     * @param canvas The canvas to draw into
     */
    private void drawMinuteHand(Canvas canvas) {
        canvas.save();
        canvas.rotate(mMinuteDeg);
        canvas.drawArc(-mMinuteHandRadius, -mMinuteHandRadius, mMinuteHandRadius, mMinuteHandRadius,
                -180, 180, false, mMinuteHandPaint);
        canvas.restore();
    }

    /**
     * Draw the second hand.
     *
     * @param canvas The canvas to draw into
     */
    private void drawSecondHand(Canvas canvas) {
        canvas.save();
        canvas.rotate(mSecondDeg);
        canvas.drawArc(-mSecondHandRadius, -mSecondHandRadius, mSecondHandRadius, mSecondHandRadius,
                -180, 180, false, mSecondHandPaint);
        canvas.restore();
    }

    /**
     * Draw the time text.
     *
     * @param canvas The canvas to draw into
     */
    private void drawTextTime(Canvas canvas) {
        canvas.save();
        final float totalWidth = mTextExtraSpace;
        canvas.translate(-totalWidth / 2, 0);
        if (mHour == null) {
            float charWidth = totalWidth / 6f;
            float textSize = charWidth * 1.2f;
            setMinuteTextSize(textSize);
            setSecondTextSize(textSize);
            setMillisTextSize(textSize * 0.6f);
            drawTextMinute(canvas);
            canvas.translate(2.3f * charWidth, 0);
            drawTextSecond(canvas);
            canvas.translate(2.3f * charWidth, 0);
            drawTextMillisecond(canvas);
        } else {
            final int hourLength = mHour.length();
            float charWidth = totalWidth / (hourLength + 6f);
            float textSize = charWidth * 1.2f;
            setHourTextSize(textSize);
            setMinuteTextSize(textSize);
            setSecondTextSize(textSize);
            setMillisTextSize(textSize * 0.6f);
            drawTextHour(canvas);
            canvas.translate((hourLength + 0.2f) * charWidth, 0);
            drawTextMinute(canvas);
            canvas.translate(2.2f * charWidth, 0);
            drawTextSecond(canvas);
            canvas.translate(2.2f * charWidth, 0);
            drawTextMillisecond(canvas);
        }
        canvas.restore();
    }

    /**
     * Draw the hour text.
     *
     * @param canvas The canvas to draw into
     */
    private void drawTextHour(Canvas canvas) {
        canvas.drawText(mHour, 0, mHourTextOffset, mHourTextPaint);
    }

    /**
     * Draw the minute text.
     *
     * @param canvas The canvas to draw into
     */
    private void drawTextMinute(Canvas canvas) {
        if (mMinute != null) {
            canvas.drawText(mMinute, 0, mMinTextOffset, mMinuteTextPaint);
        }
    }

    /**
     * Draw the second text.
     *
     * @param canvas The canvas to draw into
     */
    private void drawTextSecond(Canvas canvas) {
        if (mSecond != null) {
            canvas.drawText(mSecond, 0, mSecTextOffset, mSecondTextPaint);
        }
    }

    /**
     * Draw the milliseconds text.
     *
     * @param canvas The canvas to draw into
     */
    private void drawTextMillisecond(Canvas canvas) {
        if (mMillisecond != null) {
            canvas.drawText(mMillisecond, 0, mMillisTextOffset, mMillisTextPaint);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlpha(int alpha) {
        mDialPaint.setAlpha(alpha);
        mMinuteHandPaint.setAlpha(alpha);
        mSecondHandPaint.setAlpha(alpha);
        mHourTextPaint.setAlpha(alpha);
        mMinuteTextPaint.setAlpha(alpha);
        mSecondTextPaint.setAlpha(alpha);
        mMillisTextPaint.setAlpha(alpha);
        invalidateSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mDialPaint.setColorFilter(colorFilter);
        mHourTextPaint.setColorFilter(colorFilter);
        mMinuteHandPaint.setColorFilter(colorFilter);
        mSecondHandPaint.setColorFilter(colorFilter);
        mMinuteTextPaint.setColorFilter(colorFilter);
        mSecondTextPaint.setColorFilter(colorFilter);
        mMillisTextPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    /**
     * Specify the color of minute hand.
     *
     * @param newColor The new color
     */
    public void setMinuteHandColor(int newColor) {
        if (mColorMinuteHand != newColor) {
            mMinuteHandPaint.setShader(createSweepShader(newColor));
            mColorMinuteHand = newColor;
            invalidateSelf();
        }
    }

    /**
     * Specify the color of second hand.
     *
     * @param newColor The new color
     */
    public void setSecondHandColor(@ColorInt int newColor) {
        if (mColorSecondHand != newColor) {
            mSecondHandPaint.setShader(createSweepShader(newColor));
            mColorSecondHand = newColor;
            invalidateSelf();
        }
    }

    /**
     * Specify the background color of dial.
     *
     * @param newColor The new color
     */
    public void setDialColor(@ColorInt int newColor) {
        if (mColorDial != newColor) {
            mDialPaint.setColor(newColor);
            mColorDial = newColor;
            invalidateSelf();
        }
    }

    /**
     * Specify the text color of second text.
     *
     * @param newColor The new text color
     */
    public void setSecondTextColor(@ColorInt int newColor) {
        if (mColorSecondText != newColor) {
            mSecondTextPaint.setColor(newColor);
            mColorSecondText = newColor;
            invalidateSelf();
        }
    }

    /**
     * Specify the text color of millisecond text.
     *
     * @param newColor The new text color
     */
    public void setMillisTextColor(@ColorInt int newColor) {
        if (mColorMillisText != newColor) {
            mMillisTextPaint.setColor(newColor);
            mColorMillisText = newColor;
            invalidateSelf();
        }
    }

    /**
     * Specify the text color of minute text.
     *
     * @param newColor The new text color
     */
    public void setMinuteTextColor(@ColorInt int newColor) {
        if (mColorMinuteText != newColor) {
            mMinuteTextPaint.setColor(newColor);
            mColorMinuteText = newColor;
            invalidateSelf();
        }
    }

    /**
     * Specify the text color of hour text.
     *
     * @param newColor The new text color
     */
    public void setHourTextColor(@ColorInt int newColor) {
        if (mColorHourText != newColor) {
            mHourTextPaint.setColor(newColor);
            mColorHourText = newColor;
            invalidateSelf();
        }
    }

    /**
     * Specify the text size of the hour text.
     *
     * @param newSize The new text size
     */
    private void setHourTextSize(float newSize) {
        if (mHourTextSize != newSize) {
            mHourTextPaint.setTextSize(newSize);
            mHourTextOffset = calculateTextVerticalOffset(mHourTextPaint);
            mHourTextSize = newSize;
            invalidateSelf();
        }
    }

    /**
     * Specify the text size of the minute text.
     *
     * @param newSize The new text size
     */
    private void setMinuteTextSize(float newSize) {
        if (mMinuteTextSize != newSize) {
            mMinuteTextPaint.setTextSize(newSize);
            mMinTextOffset = calculateTextVerticalOffset(mMinuteTextPaint);
            mMinuteTextSize = newSize;
            invalidateSelf();
        }
    }

    /**
     * Specify the text size of the second text.
     *
     * @param newSize The new text size
     */
    private void setSecondTextSize(float newSize) {
        if (mSecondTextSize != newSize) {
            mSecondTextPaint.setTextSize(newSize);
            mSecTextOffset = calculateTextVerticalOffset(mSecondTextPaint);
            mSecondTextSize = newSize;
            invalidateSelf();
        }
    }

    /**
     * Specify the text size of the milliseconds text.
     *
     * @param newSize The new text size
     */
    private void setMillisTextSize(float newSize) {
        if (mMillisTextSize != newSize) {
            mMillisTextPaint.setTextSize(newSize);
            mMillisTextOffset = calculateTextVerticalOffset(mMillisTextPaint);
            mMillisTextSize = newSize;
            invalidateSelf();
        }
    }

    /**
     * Specify the stroke width of second hand.
     *
     * @param strokeWidth The new stroke width
     */
    private void setSecondHandStrokeWidth(float strokeWidth) {
        if (mSecStrokeWidth != strokeWidth) {
            mSecondHandPaint.setStrokeWidth(strokeWidth);
            mSecStrokeWidth = strokeWidth;
            invalidateSelf();
        }
    }

    /**
     * Specify the stroke width of minute hand.
     *
     * @param strokeWidth The new stroke width
     */
    private void setMinuteHandStrokeWidth(float strokeWidth) {
        if (mMinStrokeWidth != strokeWidth) {
            mMinuteHandPaint.setStrokeWidth(strokeWidth);
            mMinStrokeWidth = strokeWidth;
            invalidateSelf();
        }
    }

    /**
     * Specify the stopwatch font typeface.
     *
     * @param typeface The {@link Typeface} of font
     */
    public void setFontTypeFace(@NonNull Typeface typeface) {
        mHourTextPaint.setTypeface(typeface);
        mMinuteTextPaint.setTypeface(typeface);
        mSecondTextPaint.setTypeface(typeface);
        mMillisTextPaint.setTypeface(typeface);
        invalidateSelf();
    }
}
