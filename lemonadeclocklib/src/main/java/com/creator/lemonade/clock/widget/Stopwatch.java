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
 * A stopwatch widget
 *
 * @author Felix.Liang
 */
@SuppressWarnings("unused")
public class Stopwatch extends AbsClock {

    /**
     * The logging tag used by this class with android.util.Log
     */
    private static final String LOG_TAG = "Lemonade_Stopwatch";

    private final StopwatchModel mStopwatchModel;

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

    public void setStopwatchListener(StopwatchListener listener) {
        if (mStopwatchListener != listener) {
            mStopwatchListener = listener;
        }
    }

    public static abstract class StopwatchListenerAdapter implements StopwatchListener {

        @Override
        public void onTimeChanged(long timeInMillis) {
        }

        @Override
        public void onStateChanged(boolean started, boolean paused) {
        }

        @Override
        public void onLap(long lapTimeInMillis) {
        }
    }

    public interface StopwatchListener {

        void onTimeChanged(long timeInMillis);

        void onStateChanged(boolean started, boolean paused);

        void onLap(long lapTimeInMillis);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mStopwatchModel.onAttachedToWindow(getHandler());
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

    public void startOrResume() {
        mStopwatchModel.startOrResume();
    }

    public void pause() {
        mStopwatchModel.pause();
    }

    public void lap() {
        mStopwatchModel.lap();
    }

    public void reset() {
        mStopwatchModel.reset();
    }

    public boolean isPaused() {
        return mStopwatchModel.isPaused();
    }

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
        mStopwatchModel.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }


    private static int getCentralColor(@ColorInt int first, @ColorInt int second) {
        return Color.rgb((Color.red(first) + Color.red(second)) / 2,
                (Color.green(first) + Color.green(second)) / 2,
                (Color.blue(first) + Color.blue(second)) / 2);
    }

    private static class SavedState extends AbsSavedState {

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
