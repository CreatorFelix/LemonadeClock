package com.creator.lemonade.clock.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.creator.lemonade.clock.base.AbsClockDrawable;

/**
 * This class defines all the methods about drawing a clock, as an implementation of {@link AbsClockDrawable}
 *
 * @author Felix.Liang
 */
public class ClockDrawable extends AbsClockDrawable {

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
