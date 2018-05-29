package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.creator.lemonade.clock.base.AbsClock;
import com.creator.lemonade.clock.graphics.TimerDrawable;
import com.creator.lemonade.clock.util.TimerModel;

public class Timer extends AbsClock {

    /**
     * The logging tag used by this class with android.util.Log
     */
    private static final String LOG_TAG = "Lemonade_Timer";

    /**
     * A {@link TimerModel} controls the running of timer.
     */
    private final TimerModel mTimerModel;

    /**
     * A {@link TimerDrawable} defines the drawing of timer.
     */
    private final TimerDrawable mTimerDrawable;

    public Timer(Context context) {
        this(context, null);
    }

    public Timer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTimerDrawable = new TimerDrawable();
        mTimerModel = new TimerModel();
        mTimerModel.setTimerListener(new TimerModel.TimerWatcher() {
            @Override
            public void onTimeChanged(long restTime, long totalTime) {

            }

            @Override
            public void onStateChanged(boolean started, boolean paused) {

            }

            @Override
            public void onTimeout() {

            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTimerModel.attach(getHandler());
    }

    @Override
    protected void onDetachedFromWindow() {
        mTimerModel.detach();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mTimerModel.setSuspend(visibility != VISIBLE);
    }
}
