package com.creator.lemonade.clock.base;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
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

    private static final int[] SWEEP_COLORS = new int[3];
    private static final float[] COLOR_POSITIONS = {0, 0.5f, 1};
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


    /**
     * Create a sweep gradient shader
     *
     * @param mainColor the main color of shader
     * @return The {@link SweepGradient}
     */
    protected static Shader createSweepShader(int mainColor) {
        SWEEP_COLORS[0] = SWEEP_COLORS[2] = mainColor;
        SWEEP_COLORS[1] = Color.argb(0, Color.red(mainColor), Color.green(mainColor), Color.blue(mainColor));
        return new SweepGradient(0, 0, SWEEP_COLORS, COLOR_POSITIONS);
    }
}
