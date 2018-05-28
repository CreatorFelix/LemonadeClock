package com.creator.lemonade.clock.util;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

public class TimerModel {

    /**
     * The delayed time between two time updates.
     */
    private static final long UPDATE_DELAY_TIME = 30;

    private Handler mHandler;

    private TimerState mState;

    private TimerWatcher mTimerWatcher;

    /**
     * Field that indicates whether the timer is running
     */
    private boolean mRunning;

    private boolean mSuspend;

    private final Runnable mTick = new Runnable() {
        @Override
        public void run() {
            if (mHandler != null) {
                onTimeChanged();
                mHandler.postDelayed(mTick, UPDATE_DELAY_TIME);
            }
        }
    };

    public TimerModel() {
        mState = new TimerState();
    }

    private void onTimeChanged() {
    }

    /**
     * This method should be called when this timer attaches to its environment.
     *
     * @param handler handler to handle messages
     */
    public void attach(@NonNull Handler handler) {
        mHandler = handler;
    }

    /**
     * This method should be called when this timer detaches from its environment.
     */
    public void detach() {
        setSuspend(true);
        mHandler.removeCallbacks(mTick);
        updateRunning();
    }

    /**
     * Sets whether to suspend the update of timer.
     *
     * @param suspend true for suspend, false otherwise
     */
    public void setSuspend(boolean suspend) {
        if (mSuspend != suspend) {
            mSuspend = suspend;
            updateRunning();
        }
    }

    public TimerState getState() {
        return mState;
    }

    private void updateRunning() {
        final boolean running = isStarted() && !isPaused() && !mSuspend;
        if (mRunning != running) {
            if (running) {
                mHandler.post(mTick);
            } else {
                mHandler.removeCallbacks(mTick);
            }
            mRunning = running;
        }
    }

    public boolean isPaused() {
        return mState.started && mState.pause != TimerState.DEFAULT_TIME;
    }

    public boolean isStarted() {
        return mState.started && mState.pause == TimerState.DEFAULT_TIME;
    }

    /**
     * Register a callback to be invoked when the state of a timer is changed
     *
     * @param watcher callback to run
     */
    public void setTimerListener(TimerWatcher watcher) {
        if (mTimerWatcher != watcher) {
            mTimerWatcher = watcher;
        }
    }

    /**
     * Interface definition for a callback to be invoked when the state of a timer is changed.
     */
    public interface TimerWatcher {

        void onTimeChanged(long restTime, long totalTime);

        void onStateChanged(boolean started, boolean paused);
    }

    /**
     * A Parcelable implementation that used to hold the states of timer.
     */
    public static class TimerState implements Parcelable {

        /**
         * A constant that used to define the default time
         */
        private static final int DEFAULT_TIME = 0;

        /**
         * Field that used to hold the elapsed real time at the start of timer.
         *
         * @see SystemClock#elapsedRealtime()
         */
        private long base = DEFAULT_TIME;

        /**
         * Field that used to hold the elapsed real time at the pause of timer.
         *
         * @see SystemClock#elapsedRealtime()
         */
        private long pause = DEFAULT_TIME;

        /**
         * Field that used to hold the total time of timer which set by user.
         */
        private long total = DEFAULT_TIME;

        /**
         * Field that indicates whether the timer has been started.
         */
        private boolean started;

        private TimerState() {
        }

        private TimerState(TimerState state) {
            if (state != null) {
                base = state.base;
                total = state.total;
                pause = state.pause;
                started = state.started;
            }
        }

        private TimerState(Parcel in) {
            base = in.readLong();
            pause = in.readLong();
            total = in.readLong();
            started = in.readByte() != 0;
        }

        public static final Creator<TimerState> CREATOR = new Creator<TimerState>() {
            @Override
            public TimerState createFromParcel(Parcel in) {
                return new TimerState(in);
            }

            @Override
            public TimerState[] newArray(int size) {
                return new TimerState[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(base);
            dest.writeLong(pause);
            dest.writeLong(total);
            dest.writeByte((byte) (started ? 1 : 0));
        }

        /**
         * Clears the holding state
         */
        private void clear() {
            base = DEFAULT_TIME;
            pause = DEFAULT_TIME;
            total = DEFAULT_TIME;
            started = false;
        }

        @Override
        public String toString() {
            return "TimerState[" +
                    "base=" + base +
                    ", pause=" + pause +
                    ", total=" + total +
                    ", started=" + started +
                    ']';
        }
    }
}
