package com.creator.lemonade.clock.base;

import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

/**
 * The base class for clock drawable in which defines how the clock will be drawn
 *
 * @author Felix.Liang
 */
@SuppressWarnings("unused")
public abstract class AbsClockDrawable extends Drawable {

    private int mHeight;
    private int mWidth;
    private int mShortAxisLength;

    @Override
    @CallSuper
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mHeight = bounds.height();
        mWidth = bounds.width();
        mShortAxisLength = Math.min(mWidth, mHeight);
    }

    /**
     * Returns the height of this drawable
     *
     * @return The height
     */
    protected int getHeight() {
        return mHeight;
    }

    /**
     * Returns the width of this drawable
     *
     * @return The width
     */
    protected int getWidth() {
        return mWidth;
    }

    /**
     * Returns the length of shorter axis of this drawable
     *
     * @return The length of shorter axis
     */
    protected int getShortAxisLength() {
        return mShortAxisLength;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }



    /**
     * Calculates the offset of the text central axis
     *
     * @param textPaint the paint used to draw text
     * @return offset in pixels
     */
    protected static float calculateTextVerticalOffset(@NonNull Paint textPaint) {
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        return -(metrics.descent + metrics.ascent) / 2f;
    }
}
