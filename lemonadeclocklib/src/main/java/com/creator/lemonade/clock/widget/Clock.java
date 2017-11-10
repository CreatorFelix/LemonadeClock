package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.creator.lemonade.clock.R;
import com.creator.lemonade.clock.base.AbsClock;
import com.creator.lemonade.clock.graphics.ClockDrawable;
import com.creator.lemonade.clock.util.Ticker;

import java.util.Calendar;

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

    /**
     * {@link Ticker} controls the updates of time
     */
    private final Ticker mTicker;

    /**
     * {@link ClockDrawable} controls the drawing of clock
     */
    private final ClockDrawable mClockDrawable;

    public Clock(Context context) {
        this(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mClockDrawable = new ClockDrawable();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Clock);
        final String timeZone = array.getString(R.styleable.Clock_timeZone);
        int dialColor = array.getColor(R.styleable.Clock_clockDialColor, getThemeIntAttribute(R.attr.colorBackgroundFloating));
        mClockDrawable.setDialColor(dialColor);
        int hourColor = array.getColor(R.styleable.Clock_hourHandColor, getThemeIntAttribute(R.attr.colorPrimaryDark));
        mClockDrawable.setHourHandColor(hourColor);
        int minColor = array.getColor(R.styleable.Clock_minuteHandColor, getThemeIntAttribute(R.attr.colorPrimary));
        mClockDrawable.setMinuteHandColor(minColor);
        final int secColor = array.getColor(R.styleable.Clock_secondHandColor, getThemeIntAttribute(R.attr.colorAccent));
        mClockDrawable.setSecondHandColor(secColor);
        int hourTextColor = array.getColor(R.styleable.Clock_hourTextColor, getThemeIntAttribute(R.attr.titleTextColor));
        mClockDrawable.setHourTextColor(hourTextColor);
        int minTextColor = array.getColor(R.styleable.Clock_minuteTextColor, getThemeIntAttribute(R.attr.subtitleTextColor));
        mClockDrawable.setMinuteTextColor(minTextColor);
        array.recycle();
        // Set the background to clockDrawable, so that we can
        // update view by calling ClockDrawable#invalidateSelf()
        setBackground(mClockDrawable);
        mTicker = new Ticker(context, timeZone);
        mTicker.setOnTimeUpdateListener(new Ticker.OnTimeUpdateListener() {
            @Override
            public void onTimeChanged(Calendar time) {
                Log.i(LOG_TAG, "onTickerTimeChanged:" + time.getTimeInMillis());
                int hour = time.get(Calendar.HOUR_OF_DAY);
                int min = time.get(Calendar.MINUTE);
                int second = time.get(Calendar.SECOND);
                int millis = time.get(Calendar.MILLISECOND);
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
