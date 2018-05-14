package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.creator.lemonade.clock.BuildConfig;
import com.creator.lemonade.clock.R;
import com.creator.lemonade.clock.base.AbsClock;
import com.creator.lemonade.clock.graphics.ClockDrawable;
import com.creator.lemonade.clock.util.Ticker;

import java.util.Calendar;
import java.util.Locale;

/**
 * Clock can display current time
 *
 * @author Felix.Liang
 */
public class Clock extends AbsClock {

    /**
     * The logging tag used by this class with android.util.Log
     */
    private static final String LOG_TAG = "Lemonade_Clock";

    private static final boolean DEFAULT_SUPPORT_12_FORMAT = true;

    /**
     * {@link Ticker} controls the updates of time
     */
    private final Ticker mTicker;

    /**
     * {@link ClockDrawable} controls the drawing of clock
     */
    private final ClockDrawable mClockDrawable;

    /**
     * Indicates whether to support 12-hour time, default is {@link #DEFAULT_SUPPORT_12_FORMAT}
     */
    private boolean mSupport12Format;

    public Clock(Context context) {
        this(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mClockDrawable = new ClockDrawable();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Clock);
        final String timeZone = array.getString(R.styleable.Clock_timeZone);
        final int dialColor = array.getColor(R.styleable.Clock_dialColor, getThemeIntAttribute(R.attr.colorBackgroundFloating));
        final int hourColor = array.getColor(R.styleable.Clock_hourHandColor, getThemeIntAttribute(R.attr.colorPrimaryDark));
        final int minColor = array.getColor(R.styleable.Clock_minuteHandColor, getThemeIntAttribute(R.attr.colorPrimary));
        final int secColor = array.getColor(R.styleable.Clock_secondHandColor, getThemeIntAttribute(R.attr.colorAccent));
        final int hourTextColor = array.getColor(R.styleable.Clock_hourTextColor, getThemeIntAttribute(R.attr.colorPrimaryDark));
        final int minTextColor = array.getColor(R.styleable.Clock_minuteTextColor, getThemeIntAttribute(R.attr.colorPrimary));
        final int amPmTextColor = array.getColor(R.styleable.Clock_amPmTextColor, getThemeIntAttribute(R.attr.colorAccent));
        mSupport12Format = array.getBoolean(R.styleable.Clock_support12Format, DEFAULT_SUPPORT_12_FORMAT);
        array.recycle();
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/digit_font.ttf");
        mClockDrawable.setClockFontTypeface(typeface);
        mClockDrawable.setDialColor(dialColor);
        mClockDrawable.setHourHandColor(hourColor);
        mClockDrawable.setMinuteHandColor(minColor);
        mClockDrawable.setSecondHandColor(secColor);
        mClockDrawable.setHourTextColor(hourTextColor);
        mClockDrawable.setMinuteTextColor(minTextColor);
        mClockDrawable.setAmPmTextColor(amPmTextColor);
        // Set the background to clockDrawable, so that we can
        // update view by calling ClockDrawable#invalidateSelf()
        setBackground(mClockDrawable);
        mTicker = new Ticker(context, timeZone);
        mTicker.setOnTimeUpdateListener(new Ticker.OnTimeUpdateListener() {
            @Override
            public void onTimeChanged(Calendar time) {
                int hour = time.get(Calendar.HOUR_OF_DAY);
                int min = time.get(Calendar.MINUTE);
                int second = time.get(Calendar.SECOND);
                int millis = time.get(Calendar.MILLISECOND);
                if (BuildConfig.DEBUG) {
                    Log.v(LOG_TAG, String.format(Locale.getDefault(), "onTickerTimeChanged: %d:%d:%d.%d",
                            hour, min, second, millis));
                }
                mClockDrawable.setUse24Format(mSupport12Format && mTicker.is24Format());
                mClockDrawable.setTime(hour, min, second, millis);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTicker.onAttachedToWindow(getHandler());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTicker.onDetachedFromWindow();
    }
}
