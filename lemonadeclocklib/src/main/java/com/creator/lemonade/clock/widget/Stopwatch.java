package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.AbsSavedState;

import com.creator.lemonade.clock.base.AbsClock;
import com.creator.lemonade.clock.graphics.StopwatchDrawable;
import com.creator.lemonade.clock.util.StopwatchModel;

/**
 * A stopwatch widget
 *
 * @author Felix.Liang
 */
@SuppressWarnings("unused")
public class Stopwatch extends AbsClock implements StopwatchModel.StopwatchWatcher {

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
        setBackground(mStopwatchDrawable);
        mStopwatchModel = new StopwatchModel();
        mStopwatchModel.setStopwatchListener(this);
    }

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

    public void setStopwatchListener(StopwatchListener listener) {
        if (mStopwatchListener != listener) {
            mStopwatchListener = listener;
        }
    }

    public static abstract class StopwatchListenerAdapter implements StopwatchListener {
    }

    public interface StopwatchListener {

        void onTimeChanged(long timeInMillis);

        void onStateChanged(boolean started, boolean paused);

        void onLap(long lapTimeInMillis);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mStopwatchModel.onAttachToWindow(getHandler());
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
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mStopwatchModel.setSuspend(visibility != VISIBLE);
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
    protected void onDetachedFromWindow() {
        mStopwatchModel.onDetachFromWindow();
        super.onDetachedFromWindow();
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
