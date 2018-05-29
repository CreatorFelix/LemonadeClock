package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.AbsSavedState;

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

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.timerState = mTimerModel.getState();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        mTimerModel.setState(ss.timerState);
        super.onRestoreInstanceState(ss.getSuperState());
    }

    /**
     * This is the persistent state that is saved by Timer.  Only needed
     * if you are creating a subclass of Timer that must save its own
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

        private TimerModel.TimerState timerState;

        private SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            timerState = in.readParcelable(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(timerState, flags);
        }

        @Override
        public String toString() {
            return "SavedState{" +
                    "timerState=" + timerState.toString() +
                    '}';
        }
    }
}
