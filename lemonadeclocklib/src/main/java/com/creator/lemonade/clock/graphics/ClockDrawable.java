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

import java.text.DateFormatSymbols;
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

    private boolean mUse24Format;

    private Paint mDialPaint;
    private Paint mHourHandPaint;
    private Paint mMinuteHandPaint;
    private Paint mSecondHandPaint;
    private Paint mHourTextPaint;
    private Paint mMinuteTextPaint;
    private Paint mAmPmTextPaint;

    private float mDialRadius;
    private float mHourRadius;
    private float mMinuteRadius;
    private float mSecondRadius;
    private float mHourHandStrokeWidth;
    private float mMinuteHandStrokeWidth;
    private float mSecondHandStrokeWidth;
    private float mHourVerticalOffset;
    private float mMinuteVerticalOffset;
    private float mAmPmVerticalOffset;

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
    private int mColorAmPmText;
    private float mHourTextSize;
    private float mMinuteTextSize;
    private float mAmPmTextSize;

    private float mHourDeg;
    private float mMinuteDeg;
    private float mSecondDeg;

    private String mAmPmStrings[];
    private String mHour;
    private String mMinute;
    private String mAmPm;
    private float mAmPmTranslateY;

    public ClockDrawable() {
        initPaint();
        initAmPmStrings();
    }

    /**
     * Initialize the string array of am/pm
     */
    private void initAmPmStrings() {
        DateFormatSymbols symbols = new DateFormatSymbols();
        mAmPmStrings = symbols.getAmPmStrings();
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
        mAmPmTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mAmPmTextPaint.setTextAlign(Paint.Align.CENTER);
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
        if (mUse24Format) {
            mHour = String.format(Locale.getDefault(), TWO_DIGIT_FORMAT, hour);
        } else {
            mHour = String.format(Locale.getDefault(), TWO_DIGIT_FORMAT, hour > 12 ? hour % 12 : hour);
            if (mAmPmStrings != null && mAmPmStrings.length > 1) {
                mAmPm = mAmPmStrings[hour > 12 ? 1 : 0];
            }
        }
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
        final float halfShortAxis = shortAxis * 0.5f;
        mDialRadius = halfShortAxis;
        final float hourHandStrokeWidth = halfShortAxis * 0.2f;
        setHourHandStrokeWidth(hourHandStrokeWidth);
        final float minuteHandStrokeWidth = halfShortAxis * 0.13f;
        setMinuteHandStrokeWidth(minuteHandStrokeWidth);
        final float secondHandStrokeWidth = halfShortAxis * 0.06f;
        setSecondHandStrokeWidth(secondHandStrokeWidth);
        final float hourTextSize = shortAxis * 0.17f;
        setHourTextSize(hourTextSize);
        final float minuteTextSize = shortAxis * 0.17f;
        setMinuteTextSize(minuteTextSize);
        final float amPmTextSize = shortAxis * 0.08f;
        setAmPmTextSize(amPmTextSize);
        mTextDistance = shortAxis * 0.025f;
        mHourRadius = halfShortAxis * 0.95f - hourHandStrokeWidth * 0.5f;
        mMinuteRadius = mHourRadius - hourHandStrokeWidth * 0.5f + minuteHandStrokeWidth * 0.5f;
        mSecondRadius = mHourRadius - hourHandStrokeWidth * 0.5f + secondHandStrokeWidth * 0.5f;
        mAmPmTranslateY = shortAxis * 0.18f;
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
        if (!isUse24Format() && !TextUtils.isEmpty(mAmPm)) {
            canvas.save();
            canvas.translate(0, mAmPmTranslateY);
            canvas.drawText(mAmPm, 0, mAmPmVerticalOffset, mAmPmTextPaint);
            canvas.restore();
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mDialPaint.setAlpha(alpha);
        mHourTextPaint.setAlpha(alpha);
        mMinuteTextPaint.setAlpha(alpha);
        mHourHandPaint.setAlpha(alpha);
        mAmPmTextPaint.setAlpha(alpha);
        mMinuteHandPaint.setAlpha(alpha);
        mSecondHandPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mDialPaint.setColorFilter(colorFilter);
        mHourTextPaint.setColorFilter(colorFilter);
        mMinuteTextPaint.setColorFilter(colorFilter);
        mAmPmTextPaint.setColorFilter(colorFilter);
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
            invalidateSelf();
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
            invalidateSelf();
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
            invalidateSelf();
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
            invalidateSelf();
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
            invalidateSelf();
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
            invalidateSelf();
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
            mHourVerticalOffset = calculateTextVerticalOffset(mHourTextPaint);
            mHourTextSize = newSize;
            invalidateSelf();
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
            mMinuteVerticalOffset = calculateTextVerticalOffset(mMinuteTextPaint);
            mMinuteTextSize = newSize;
            invalidateSelf();
        }
    }

    /**
     * Sets the color of am/pm text
     *
     * @param newColor The new text color
     */
    public void setAmPmTextColor(@ColorInt int newColor) {
        if (mColorAmPmText != newColor) {
            mAmPmTextPaint.setColor(newColor);
            mColorAmPmText = newColor;
            invalidateSelf();
        }
    }

    /**
     * Sets the size of am/pm text
     *
     * @param newSize The new text size
     */
    private void setAmPmTextSize(float newSize) {
        if (mAmPmTextSize != newSize) {
            mAmPmTextPaint.setTextSize(newSize);
            mAmPmVerticalOffset = calculateTextVerticalOffset(mAmPmTextPaint);
            mAmPmTextSize = newSize;
            invalidateSelf();
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
            invalidateSelf();
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
            invalidateSelf();
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
            invalidateSelf();
        }
    }

    /**
     * Sets the clock font typeface
     *
     * @param typeface The {@link Typeface}
     */
    public void setClockFontTypeface(@NonNull Typeface typeface) {
        mHourTextPaint.setTypeface(typeface);
        mMinuteTextPaint.setTypeface(typeface);
        mAmPmTextPaint.setFakeBoldText(true);
        mAmPmTextPaint.setTextScaleX(1.25f);
        mAmPmTextPaint.setLetterSpacing(0.15f);
        invalidateSelf();
    }

    /**
     * Indicates whether to use 24-hour time
     *
     * @return true if use 24-hour time,false otherwise
     */
    private boolean isUse24Format() {
        return mUse24Format;
    }

    /**
     * Sets whether to use 24-hour time
     *
     * @param use24Format true if use 24-hour time,false otherwise
     */
    public void setUse24Format(boolean use24Format) {
        if (mUse24Format != use24Format) {
            mUse24Format = use24Format;
            invalidateSelf();
        }
    }
}
