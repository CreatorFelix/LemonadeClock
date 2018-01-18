package com.creator.lemonade.clock.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;

import com.creator.lemonade.clock.base.AbsClockDrawable;

import java.util.Locale;

/**
 * This class defines all the methods about drawing a clock, as an implementation of {@link AbsClockDrawable}
 *
 * @author Felix.Liang
 */
public class ClockDrawable extends AbsClockDrawable {

    /**
     * The initial offset degree of hand
     */
    private static final float INIT_DEGREE_OFFSET = 90;

    /**
     * The string used to convert value to two digit value
     */
    private static final String TWO_DIGIT_FORMAT = "%02d";

    private Paint mDialPaint;
    private Paint mHourHandPaint;
    private Paint mMinuteHandPaint;
    private Paint mSecondHandPaint;
    private Paint mHourTextPaint;
    private Paint mMinuteTextPaint;

    private float mDialRadius;
    private float mHourRadius;
    private float mMinuteRadius;
    private float mSecondRadius;
    private float mHourHandStrokeWidth;
    private float mMinuteHandStrokeWidth;
    private float mSecondHandStrokeWidth;
    private float mHourVerticalOffset;
    private float mMinuteVerticalOffset;

    /**
     * The extra distance between minute text and hour text
     */
    private float mTextDistance;

    private int mColorHourHand;
    private int mColorMinuteHand;
    private int mColorSecondHand;
    private int mColorDial;
    private int mColorHourText;
    private int mColorMinuteText;
    private float mHourTextSize;
    private float mMinuteTextSize;

    private float mHourDeg;
    private float mMinuteDeg;
    private float mSecondDeg;

    private final int[] mSweepColors = new int[3];
    private final float[] mColorPositions = {0, 0.5f, 1};

    private String mHour;
    private String mMinute;

    public ClockDrawable() {
        initPaint();
    }

    /**
     * Initialize all {@link Paint}
     */
    private void initPaint() {
        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDialPaint.setStyle(Paint.Style.FILL);
        mHourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourHandPaint.setStyle(Paint.Style.STROKE);
        mMinuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinuteHandPaint.setStyle(Paint.Style.STROKE);
        mSecondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondHandPaint.setStyle(Paint.Style.STROKE);
        mSecondHandPaint.setStrokeCap(Paint.Cap.ROUND);
        mHourTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mHourTextPaint.setTextAlign(Paint.Align.RIGHT);
        mMinuteTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mMinuteTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    /**
     * Sets the time which this drawable will show
     *
     * @param hour        The hour to show
     * @param minute      The minute to show
     * @param second      The second to show
     * @param millisecond The millisecond to show
     */
    public void setTime(int hour, int minute, int second, int millisecond) {
        onTimeChanged(hour, minute, second, millisecond);
        invalidateSelf();
    }

    /**
     * Called when current time has been changed
     *
     * @param hour        The hour to show
     * @param minute      The minute to show
     * @param second      The second to show
     * @param millisecond The millisecond to show
     */
    private void onTimeChanged(int hour, int minute, int second, int millisecond) {
        mHour = String.format(Locale.getDefault(), TWO_DIGIT_FORMAT, hour);
        mMinute = String.format(Locale.getDefault(), TWO_DIGIT_FORMAT, minute);
        float actualSecond = second + millisecond / 1000f;
        mSecondDeg = 360f * actualSecond / 60;
        float actualMinute = minute + actualSecond / 60;
        mMinuteDeg = 360f * actualMinute / 60;
        float actualHour = hour + actualMinute / 60;
        mHourDeg = 360f * actualHour / 12;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        final int shortAxis = getShortAxisLength();
        mDialRadius = shortAxis * 0.5f;
        final float hourHandStrokeWidth = mDialRadius * 0.2f;
        setHourHandStrokeWidth(hourHandStrokeWidth);
        final float minuteHandStrokeWidth = mDialRadius * 0.13f;
        setMinuteHandStrokeWidth(minuteHandStrokeWidth);
        final float secondHandStrokeWidth = mDialRadius * 0.06f;
        setSecondHandStrokeWidth(secondHandStrokeWidth);
        final float hourTextSize = shortAxis * 0.17f;
        setHourTextSize(hourTextSize);
        final float minuteTextSize = shortAxis * 0.17f;
        setMinuteTextSize(minuteTextSize);
        mTextDistance = shortAxis * 0.025f;
        mHourRadius = mDialRadius * 0.95f - hourHandStrokeWidth * 0.5f;
        mMinuteRadius = mHourRadius - hourHandStrokeWidth * 0.5f + minuteHandStrokeWidth * 0.5f;
        mSecondRadius = mHourRadius - hourHandStrokeWidth * 0.5f + secondHandStrokeWidth * 0.5f;
        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.save();
        canvas.rotate(-INIT_DEGREE_OFFSET);
        drawDial(canvas);
        drawHourHand(canvas);
        drawMinuteHand(canvas);
        drawSecondHand(canvas);
        canvas.restore();
        drawTextTime(canvas);
    }

    /**
     * Draw the dial
     *
     * @param canvas The canvas to draw into
     */
    private void drawDial(Canvas canvas) {
        canvas.drawCircle(0, 0, mDialRadius, mDialPaint);
    }

    private void drawHourHand(Canvas canvas) {
        canvas.save();
        canvas.rotate(mHourDeg);
        canvas.drawArc(-mHourRadius, -mHourRadius, mHourRadius, mHourRadius,
                -180, 180, false, mHourHandPaint);
        canvas.restore();
    }

    /**
     * Draw the minute hand
     *
     * @param canvas The canvas to draw into
     */
    private void drawMinuteHand(Canvas canvas) {
        canvas.save();
        canvas.rotate(mMinuteDeg);
        canvas.drawArc(-mMinuteRadius, -mMinuteRadius, mMinuteRadius, mMinuteRadius,
                -180, 180, false, mMinuteHandPaint);
        canvas.restore();
    }

    /**
     * Draw the second hand
     *
     * @param canvas The canvas to draw into
     */
    private void drawSecondHand(Canvas canvas) {
        canvas.save();
        canvas.rotate(mSecondDeg);
        canvas.drawArc(-mSecondRadius, -mSecondRadius, mSecondRadius, mSecondRadius,
                -180, 180, false, mSecondHandPaint);
        canvas.restore();
    }

    private void drawTextTime(Canvas canvas) {
        if (!TextUtils.isEmpty(mHour)) {
            canvas.drawText(mHour, -mTextDistance, mHourVerticalOffset, mHourTextPaint);
        }
        if (!TextUtils.isEmpty(mMinute)) {
            canvas.drawText(mMinute, mTextDistance, mMinuteVerticalOffset, mMinuteTextPaint);
        }
    }

    /**
     * Create a sweep gradient shader
     *
     * @param mainColor the main color of shader
     * @return The {@link SweepGradient}
     */
    private Shader createSweepShader(int mainColor) {
        mSweepColors[0] = mSweepColors[2] = mainColor;
        mSweepColors[1] = Color.argb(0, Color.red(mainColor), Color.green(mainColor), Color.blue(mainColor));
        return new SweepGradient(0, 0, mSweepColors, mColorPositions);
    }

    @Override
    public void setAlpha(int alpha) {
        mDialPaint.setAlpha(alpha);
        mHourTextPaint.setAlpha(alpha);
        mMinuteTextPaint.setAlpha(alpha);
        mHourHandPaint.setAlpha(alpha);
        mMinuteHandPaint.setAlpha(alpha);
        mSecondHandPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mDialPaint.setColorFilter(colorFilter);
        mHourTextPaint.setColorFilter(colorFilter);
        mMinuteTextPaint.setColorFilter(colorFilter);
        mHourHandPaint.setColorFilter(colorFilter);
        mMinuteHandPaint.setColorFilter(colorFilter);
        mSecondHandPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    /**
     * Sets the color of hour hand
     *
     * @param newColor The new color
     */
    public void setHourHandColor(@ColorInt int newColor) {
        if (mColorHourHand != newColor) {
            mHourHandPaint.setShader(createSweepShader(newColor));
            mColorHourHand = newColor;
        }
    }

    /**
     * Sets the color of minute hand
     *
     * @param newColor The new color
     */
    public void setMinuteHandColor(@ColorInt int newColor) {
        if (mColorMinuteHand != newColor) {
            mMinuteHandPaint.setShader(createSweepShader(newColor));
            mColorMinuteHand = newColor;
        }
    }

    /**
     * Sets the color of second hand
     *
     * @param newColor The new color
     */
    public void setSecondHandColor(@ColorInt int newColor) {
        if (mColorSecondHand != newColor) {
            mSecondHandPaint.setShader(createSweepShader(newColor));
            mColorSecondHand = newColor;
        }
    }

    /**
     * Sets the dial color
     *
     * @param newColor The new color
     */
    public void setDialColor(@ColorInt int newColor) {
        if (mColorDial != newColor) {
            mDialPaint.setColor(newColor);
            mColorDial = newColor;
        }
    }

    /**
     * Sets the hour's text color
     *
     * @param newColor The new color
     */
    public void setHourTextColor(@ColorInt int newColor) {
        if (mColorHourText != newColor) {
            mHourTextPaint.setColor(newColor);
            mColorHourText = newColor;
        }
    }

    /**
     * Sets the minute's text color
     *
     * @param newColor The new color
     */
    public void setMinuteTextColor(@ColorInt int newColor) {
        if (mColorMinuteText != newColor) {
            mMinuteTextPaint.setColor(newColor);
            mColorMinuteText = newColor;
        }
    }

    /**
     * Sets the hour's text size
     *
     * @param newSize The new text size
     */
    private void setHourTextSize(float newSize) {
        if (mHourTextSize != newSize) {
            mHourTextPaint.setTextSize(newSize);
            Paint.FontMetrics metrics = mHourTextPaint.getFontMetrics();
            mHourVerticalOffset = -(metrics.descent + metrics.ascent) / 2f;
            mHourTextSize = newSize;
        }
    }

    /**
     * Sets the minute's text size
     *
     * @param newSize The new text size
     */
    private void setMinuteTextSize(float newSize) {
        if (mMinuteTextSize != newSize) {
            mMinuteTextPaint.setTextSize(newSize);
            Paint.FontMetrics metrics = mMinuteTextPaint.getFontMetrics();
            mMinuteVerticalOffset = -(metrics.descent + metrics.ascent) / 2f;
            mMinuteTextSize = newSize;
        }
    }

    /**
     * Sets the stroke width of hour hand
     *
     * @param newWidth The new stroke width
     */
    private void setHourHandStrokeWidth(float newWidth) {
        if (mHourHandStrokeWidth != newWidth) {
            mHourHandPaint.setStrokeWidth(newWidth);
            mHourHandStrokeWidth = newWidth;
        }
    }

    /**
     * Sets the stroke width of minute hand
     *
     * @param newWidth The new stroke width
     */
    private void setMinuteHandStrokeWidth(float newWidth) {
        if (mMinuteHandStrokeWidth != newWidth) {
            mMinuteHandPaint.setStrokeWidth(newWidth);
            mMinuteHandStrokeWidth = newWidth;
        }
    }

    /**
     * Sets the stroke width of second hand
     *
     * @param newWidth The new stroke width
     */
    private void setSecondHandStrokeWidth(float newWidth) {
        if (mSecondHandStrokeWidth != newWidth) {
            mSecondHandPaint.setStrokeWidth(newWidth);
            mSecondHandStrokeWidth = newWidth;
        }
    }

    /**
     * Sets the clock font typeface
     *
     * @param typeface The {@link Typeface}
     */
    public void setClockFontTypeface(Typeface typeface) {
        mHourTextPaint.setTypeface(typeface);
        mMinuteTextPaint.setTypeface(typeface);
    }
}
