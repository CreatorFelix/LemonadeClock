package com.creator.lemonade.clock.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.creator.lemonade.clock.base.AbsClockDrawable;

public class TimerDrawable extends AbsClockDrawable {

    private long mTotalTime;
    private long mRestTime;

    public TimerDrawable() {
    }

    public void setTotalTime(long totalTime) {
        if (mTotalTime != totalTime) {
            mTotalTime = totalTime;
            invalidateSelf();
        }
    }

    public void setRestTime(long restTime) {
        restTime = Math.min(Math.max(0, restTime), mTotalTime);
        if (mRestTime != restTime) {
            mRestTime = restTime;
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
