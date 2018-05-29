package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.AbsSavedState;

import com.creator.lemonade.clock.BuildConfig;
import com.creator.lemonade.clock.base.AbsClock;
import com.creator.lemonade.clock.graphics.TimerDrawable;
import com.creator.lemonade.clock.util.TimerModel;

import java.util.Locale;

/**
 * This timer widget provides the following methods to control the running of timer:
 * <ul>
 * <li>{@link #setTotalTime(long)}
 * <li>{@link #startOrResume()}
 * <li>{@link #pause()}
 * <li>{@link #rest()}
 * </ul>
 * Remember to set the total of a timer first before you start it.
 * <br>
 * Also you can use the following methods to judge the state of the timer:
 * <ul>
 * <li>{@link #isStarted()}
 * <li>{@link #isPaused()}
 * </ul>
 * <br>
 * A callback class {@link TimerListener} is convenience to deal with some tasks from outside.
 *
 * @author Felix.Liang
 */
@SuppressWarnings("unused")
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

    private TimerListener mTimerListener;

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
                mTimerDrawable.setTotalTime(totalTime);
                mTimerDrawable.setRestTime(restTime);
                if (mTimerListener != null) mTimerListener.onTimeChanged(restTime);
                if (BuildConfig.DEBUG) {
                    Log.v(LOG_TAG, String.format(Locale.getDefault(),
                            "onTimeChanged : restTime = %d, totalTime = %d", restTime, totalTime));
                }
            }

            @Override
            public void onStateChanged(boolean started, boolean paused) {
                if (mTimerListener != null) mTimerListener.onStateChanged(started, paused);
                if (BuildConfig.DEBUG) {
                    Log.v(LOG_TAG, String.format(Locale.getDefault(),
                            "onStateChanged : IsStarted = %s, IsPaused = %s", started, paused));
                }
            }

            @Override
            public void onTimeout() {
                if (mTimerListener != null) mTimerListener.onTimeout();
                if (BuildConfig.DEBUG) {
                    Log.v(LOG_TAG, "onTimeout");
                }
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

    /**
     * Convenience method to start or stop the timer.
     */
    public void startOrResume() {
        mTimerModel.startOrResume();
    }

    /**
     * Convenience method to pause the timer.
     */
    public void pause() {
        mTimerModel.pause();
    }

    /**
     * Convenience method to reset the timer.
     */
    public void rest() {
        mTimerModel.reset();
    }

    /**
     * Sets the total time of this timer, also this will stop and reset the timer.
     *
     * @param totalTime milliseconds to set
     */
    public void setTotalTime(long totalTime) {
        mTimerModel.setTotalTime(totalTime);
    }

    /**
     * Indicates whether this timer has been paused.
     *
     * @return true if has been paused, false otherwise
     */
    public boolean isPaused() {
        return mTimerModel.isPaused();
    }

    /**
     * Indicates whether this timer has been started.
     *
     * @return true if has been started, false otherwise
     */
    public boolean isStarted() {
        return mTimerModel.isStarted();
    }

    /**
     * Registers a callback to be invoked when the state of a timer changes.
     *
     * @param listener callback to run
     */
    public void setTimerListener(TimerListener listener) {
        if (mTimerListener != listener) {
            mTimerListener = listener;
        }
    }

    /**
     * Interface definition for a callback to be invoked when the state of a timer changes.
     */
    public interface TimerListener {

        /**
         * Called when the rest time of timer changes.
         *
         * @param restTime the rest milliseconds of the timer
         */
        void onTimeChanged(long restTime);

        /**
         * Called when the state of timer changes.
         *
         * @param started true if the timer has been started, false otherwise
         * @param paused  true if the timer has been paused, false otherwise
         */
        void onStateChanged(boolean started, boolean paused);

        /**
         * Called when the timer times out.
         */
        void onTimeout();
    }

    /**
     * This adapter class provides empty implementations of the methods from {@link TimerListener}.
     * Any custom listener that cares only about a subset of the methods of this listener can
     * simply subclass this adapter class instead of implementing the interface directly.
     */
    public static abstract class TimerListenerAdapter implements TimerListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onTimeChanged(long restTime) {
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
        public void onTimeout() {
        }
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
