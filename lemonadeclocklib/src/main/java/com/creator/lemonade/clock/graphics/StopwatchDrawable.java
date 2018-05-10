package com.creator.lemonade.clock.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.creator.lemonade.clock.base.AbsClockDrawable;

/**
 * This Class defines how to draw a stopwatch
 *
 * @author Felix.Liang
 */
public class StopwatchDrawable extends AbsClockDrawable {

    private long mCurrentTime;

    public StopwatchDrawable() {
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
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }
}
