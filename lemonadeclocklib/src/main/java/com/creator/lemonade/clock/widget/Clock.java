package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

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
        mTicker = new Ticker(context);
        mClockDrawable = new ClockDrawable();
        // Set the background to clockDrawable, so that we can
        // update view by calling ClockDrawable#invalidateSelf()
        setBackground(mClockDrawable);
        mTicker.setOnTimeUpdateListener(new Ticker.OnTimeUpdateListener() {
            @Override
            public void onTimeChanged(Calendar time) {
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
