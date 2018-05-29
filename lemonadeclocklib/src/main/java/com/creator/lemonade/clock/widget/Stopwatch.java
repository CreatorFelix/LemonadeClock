package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.AbsSavedState;

import com.creator.lemonade.clock.R;
import com.creator.lemonade.clock.base.AbsClock;
import com.creator.lemonade.clock.graphics.StopwatchDrawable;
import com.creator.lemonade.clock.util.StopwatchModel;

/**
 * This stopwatch widget provides the following methods to control the running of stopwatch:
 * <ul>
 * <li>{@link #startOrResume()}
 * <li>{@link #pause()}
 * <li>{@link #lap()}
 * <li>{@link #reset()}
 * </ul>
 * <br>
 * Also you can use the following methods to judge the state of the stopwatch:
 * <ul>
 * <li>{@link #isStarted()}
 * <li>{@link #isPaused()}
 * </ul>
 * <br>
 * A callback class {@link StopwatchListener} is used to deal with some tasks from outside.
 *
 * @author Felix.Liang
 */
@SuppressWarnings("unused")
public class Stopwatch extends AbsClock {

    /**
     * The logging tag used by this class with android.util.Log
     */
    private static final String LOG_TAG = "Lemonade_Stopwatch";

    /**
     * A {@link StopwatchModel} controls the running of stopwatch
     */
    private final StopwatchModel mStopwatchModel;

    /**
     * A {@link StopwatchDrawable} defines the drawing of stopwatch
     */
    private final StopwatchDrawable mStopwatchDrawable;

    private StopwatchListener mStopwatchListener;

    public Stopwatch(Context context) {
        this(context, null);
    }

    public Stopwatch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mStopwatchDrawable = new StopwatchDrawable();
        final int colorPrimary = getThemeIntAttribute(R.attr.colorPrimary);
        final int colorPrimaryDark = getThemeIntAttribute(R.attr.colorPrimaryDark);
        final int colorAccent = getThemeIntAttribute(R.attr.colorAccent);
        final int colorCenter = getCentralColor(colorPrimary, colorAccent);
        final int colorBackgroundFloating = getThemeIntAttribute(R.attr.colorBackgroundFloating);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Stopwatch);
        final int dialColor = array.getColor(R.styleable.Stopwatch_dialColor, colorBackgroundFloating);
        final int minColor = array.getColor(R.styleable.Stopwatch_minuteHandColor, colorPrimary);
        final int secColor = array.getColor(R.styleable.Stopwatch_secondHandColor, colorAccent);
        final int hourTextColor = array.getColor(R.styleable.Stopwatch_hourTextColor, colorPrimaryDark);
        final int minTextColor = array.getColor(R.styleable.Stopwatch_minuteTextColor, colorPrimary);
        final int secTextColor = array.getColor(R.styleable.Stopwatch_secondTextColor, colorAccent);
        final int millisTextColor = array.getColor(R.styleable.Stopwatch_millisecondTextColor, colorCenter);
        array.recycle();
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/digit_font.ttf");
        mStopwatchDrawable.setFontTypeFace(typeface);
        mStopwatchDrawable.setDialColor(dialColor);
        mStopwatchDrawable.setSecondHandColor(secColor);
        mStopwatchDrawable.setMinuteHandColor(minColor);
        mStopwatchDrawable.setHourTextColor(hourTextColor);
        mStopwatchDrawable.setMinuteTextColor(minTextColor);
        mStopwatchDrawable.setSecondTextColor(secTextColor);
        mStopwatchDrawable.setMillisTextColor(millisTextColor);
        setBackground(mStopwatchDrawable);
        mStopwatchModel = new StopwatchModel();
        mStopwatchModel.setStopwatchListener(new StopwatchModel.StopwatchWatcher() {
            @Override
            public void onTimeChanged(long timeInMillis) {
                mStopwatchDrawable.setTime(timeInMillis);
                if (mStopwatchListener != null) mStopwatchListener.onTimeChanged(timeInMillis);
            }

            @Override
            public void onStateChanged(boolean started, boolean paused) {
                if (mStopwatchListener != null) mStopwatchListener.onStateChanged(started, paused);
            }

            @Override
            public void onLap(long lapTimeInMillis) {
                if (mStopwatchListener != null) mStopwatchListener.onLap(lapTimeInMillis);
            }
        });
    }

    /**
     * Register a callback to be invoked when the state of a stopwatch is changed
     *
     * @param listener the callback to run
     */
    public void setStopwatchListener(StopwatchListener listener) {
        if (mStopwatchListener != listener) {
            mStopwatchListener = listener;
        }
    }

    /**
     * Interface definition for a callback to be invoked when the state of a stopwatch is changed.
     */
    public interface StopwatchListener {

        /**
         * Called when the time of stopwatch changes.
         *
         * @param timeInMillis the time of stopwatch in milliseconds
         */
        void onTimeChanged(long timeInMillis);

        /**
         * Called when the state of stopwatch changes.
         *
         * @param started true if the stopwatch has been started, false otherwise
         * @param paused  true if the stopwatch has been paused, false otherwise
         */
        void onStateChanged(boolean started, boolean paused);

        /**
         * Called when a lap is added.
         *
         * @param lapTimeInMillis current time of stopwatch in milliseconds
         * @see #lap()
         */
        void onLap(long lapTimeInMillis);
    }

    /**
     * This adapter class provides empty implementations of the methods from {@link StopwatchListener}.
     * Any custom listener that cares only about a subset of the methods of this listener can
     * simply subclass this adapter class instead of implementing the interface directly.
     */
    public static abstract class StopwatchListenerAdapter implements StopwatchListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onTimeChanged(long timeInMillis) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onStateChanged(boolean started, boolean paused) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onLap(long lapTimeInMillis) {
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mStopwatchModel.attach(getHandler());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        mStopwatchModel.setState(ss.stopwatchState);
        super.onRestoreInstanceState(ss.getSuperState());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mStopwatchModel.setSuspend(visibility != VISIBLE);
    }

    /**
     * Convenience method to start or stop the stopwatch.
     */
    public void startOrResume() {
        mStopwatchModel.startOrResume();
    }

    /**
     * Convenience method to pause the stopwatch.
     */
    public void pause() {
        mStopwatchModel.pause();
    }

    /**
     * Convenience method to add a lap. We can deal with the result by setting a {@link StopwatchListener} callback,
     * because {@link StopwatchListener#onLap(long)} will be called.
     *
     * @see #setStopwatchListener(StopwatchListener)
     */
    public void lap() {
        mStopwatchModel.lap();
    }

    /**
     * Convenience method to reset the stopwatch.
     */
    public void reset() {
        mStopwatchModel.reset();
    }

    /**
     * Indicates whether this stopwatch has been paused.
     *
     * @return true if has been paused, false otherwise
     */
    public boolean isPaused() {
        return mStopwatchModel.isPaused();
    }

    /**
     * Indicates whether this stopwatch has been started.
     *
     * @return true if has been started, false otherwise
     */
    public boolean isStarted() {
        return mStopwatchModel.isStarted();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.stopwatchState = mStopwatchModel.getState();
        return ss;
    }

    @Override
    protected void onDetachedFromWindow() {
        mStopwatchModel.detach();
        super.onDetachedFromWindow();
    }

    /**
     * Gets the central color between two colors.
     *
     * @param first  the first color
     * @param second the second color
     * @return the central color
     */
    private static int getCentralColor(@ColorInt int first, @ColorInt int second) {
        return Color.rgb((Color.red(first) + Color.red(second)) / 2,
                (Color.green(first) + Color.green(second)) / 2,
                (Color.blue(first) + Color.blue(second)) / 2);
    }

    /**
     * This is the persistent state that is saved by Stopwatch.  Only needed
     * if you are creating a subclass of Stopwatch that must save its own
     * state, in which case it should implement a subclass of this which
     * contains that state.
     */
    public static class SavedState extends AbsSavedState {

        public static final Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private StopwatchModel.StopwatchState stopwatchState;

        private SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            stopwatchState = in.readParcelable(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelable(stopwatchState, flags);
        }

        @Override
        public String toString() {
            return "SavedState{" +
                    "stopwatchState=" + stopwatchState.toString() +
                    '}';
        }
    }
}
