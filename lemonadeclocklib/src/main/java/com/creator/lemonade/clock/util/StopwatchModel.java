package com.creator.lemonade.clock.util;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

/**
 * As a model of stopwatch, this class defines the basic logic and holds the state of stopwatch.
 *
 * @author Felix.Liang
 */
public class StopwatchModel {

    /**
     * The delayed time between two time updates.
     */
    private static final long UPDATE_DELAY_TIME = 30;

    private Handler mHandler;

    private StopwatchState mState;

    private StopwatchWatcher mStopwatchWatcher;

    /**
     * Field that indicates whether the stopwatch is running
     */
    private boolean mRunning;

    /**
     * @see #setSuspend(boolean)
     */
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

    public StopwatchModel() {
        mState = new StopwatchState();
    }

    private void onTimeChanged() {
        if (mStopwatchWatcher != null) mStopwatchWatcher.onTimeChanged(getStopwatchTime());
    }

    /**
     * This method should be called when this stopwatch attaches to its environment.
     *
     * @param handler handler
     */
    public void attach(@NonNull Handler handler) {
        mHandler = handler;
    }

    /**
     * This method should be called when this stopwatch detaches from its environment.
     */
    public void detach() {
        setSuspend(true);
        mHandler.removeCallbacks(mTick);
        updateRunning();
    }

    /**
     * Sets whether to suspend the update of stopwatch.
     *
     * @param suspend true for suspend, false otherwise
     */
    public void setSuspend(boolean suspend) {
        if (mSuspend != suspend) {
            mSuspend = suspend;
            updateRunning();
        }
    }

    /**
     * Starts or resumes the stopwatch.
     */
    public void startOrResume() {
        if (!isStarted()) {
            start();
        } else {
            resume();
        }
    }

    /**
     * Starts the stopwatch.
     *
     * @see #startOrResume()
     */
    private void start() {
        setStarted(true);
    }

    /**
     * Resumes the stopwatch if it is paused.
     *
     * @see #startOrResume()
     */
    private void resume() {
        if (isPaused()) {
            final long pauseTime = mState.pause;
            mState.base += (getElapsedTime() - pauseTime);
            mState.pause = StopwatchState.DEFAULT_TIME;
            if (mStopwatchWatcher != null)
                mStopwatchWatcher.onStateChanged(isStarted(), isPaused());
            updateRunning();
        }
    }

    /**
     * Pauses the stopwatch. Nothing happens if the stopwatch isn't started or has been paused.
     */
    public void pause() {
        if (isStarted() && !isPaused()) {
            mState.pause = getElapsedTime();
            if (mStopwatchWatcher != null)
                mStopwatchWatcher.onStateChanged(isStarted(), isPaused());
            updateRunning();
        }
    }

    /**
     * Gets current time of stopwatch.
     *
     * @return time in milliseconds
     */
    private long getStopwatchTime() {
        if (!isStarted()) return StopwatchState.DEFAULT_TIME;
        if (isPaused()) {
            return mState.pause - mState.base;
        } else {
            return getElapsedTime() - mState.base;
        }
    }

    /**
     * Get milliseconds since boot.
     *
     * @return milliseconds since boot
     * @see SystemClock#elapsedRealtime()
     */
    private long getElapsedTime() {
        return SystemClock.elapsedRealtime();
    }

    private void setStarted(boolean started) {
        if (mState.started != started) {
            mState.started = started;
            if (started) {
                mState.base = getElapsedTime();
            }
            if (mStopwatchWatcher != null)
                mStopwatchWatcher.onStateChanged(isStarted(), isPaused());
            updateRunning();
        }
    }

    /**
     * Adds a lap. This method will call {@link StopwatchWatcher#onLap(long)}.
     */
    public void lap() {
        if (mStopwatchWatcher != null) {
            mStopwatchWatcher.onLap(getStopwatchTime());
        }
    }

    /**
     * Indicates whether this stopwatch has been started.
     *
     * @return true if has been started, false otherwise
     */
    public boolean isStarted() {
        return mState.started;
    }

    /**
     * Indicates whether this stopwatch has been paused.
     *
     * @return true if has been paused, false otherwise
     */
    public boolean isPaused() {
        return mState.started && mState.pause != StopwatchState.DEFAULT_TIME;
    }

    /**
     * Resets the stopwatch.
     */
    public void reset() {
        setStarted(false);
        mState.clear();
        if (mStopwatchWatcher != null) mStopwatchWatcher.onTimeChanged(getStopwatchTime());
    }

    private void updateRunning() {
        final boolean started = mState.started;
        boolean running = !mSuspend && started && !isPaused();
        if (mRunning != running && mHandler != null) {
            if (running) {
                mHandler.post(mTick);
            } else {
                mHandler.removeCallbacks(mTick);
            }
            mRunning = running;
        }
    }

    /**
     * Gets the holding state of this stopwatch.
     *
     * @return The {@link StopwatchState} instance
     */
    public StopwatchState getState() {
        return mState;
    }

    /**
     * Sets the state of this stopwatch.
     *
     * @param ss The target state
     */
    public void setState(StopwatchState ss) {
        mState = new StopwatchState(ss);
        if (mStopwatchWatcher != null) {
            mStopwatchWatcher.onStateChanged(isStarted(), isPaused());
            mStopwatchWatcher.onTimeChanged(getStopwatchTime());
        }
        updateRunning();
    }

    /**
     * Register a callback to be invoked when the state of a stopwatch is changed
     *
     * @param watcher The callback to run
     */
    public void setStopwatchListener(StopwatchWatcher watcher) {
        if (mStopwatchWatcher != watcher) {
            mStopwatchWatcher = watcher;
        }
    }

    /**
     * Interface definition for a callback to be invoked when the state of a stopwatch is changed.
     */
    public interface StopwatchWatcher {

        void onTimeChanged(long timeInMillis);

        void onStateChanged(boolean started, boolean paused);

        void onLap(long lapTimeInMillis);
    }

    /**
     * A Parcelable implementation that used to hold the state of stopwatch.
     */
    public static class StopwatchState implements Parcelable {

        /**
         * A constant that used to define the default time
         */
        private static final int DEFAULT_TIME = 0;

        /**
         * Field that used to hold the elapsed real time at the start of stopwatch.
         *
         * @see SystemClock#elapsedRealtime()
         */
        private long base = DEFAULT_TIME;

        /**
         * Field that used to hold the elapsed real time at the pause of stopwatch.
         *
         * @see SystemClock#elapsedRealtime()
         */
        private long pause = DEFAULT_TIME;

        /**
         * Field that indicates whether the stopwatch has been started.
         */
        private boolean started;

        private StopwatchState() {
        }

        private StopwatchState(StopwatchState state) {
            if (state != null) {
                base = state.base;
                started = state.started;
                pause = state.pause;
            }
        }

        private StopwatchState(Parcel in) {
            started = in.readByte() != 0;
            base = in.readLong();
            pause = in.readLong();
        }

        /**
         * Clears the holding state
         */
        private void clear() {
            started = false;
            base = DEFAULT_TIME;
            pause = DEFAULT_TIME;
        }

        public static final Creator<StopwatchState> CREATOR = new Creator<StopwatchState>() {
            @Override
            public StopwatchState createFromParcel(Parcel in) {
                return new StopwatchState(in);
            }

            @Override
            public StopwatchState[] newArray(int size) {
                return new StopwatchState[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (started ? 1 : 0));
            dest.writeLong(base);
            dest.writeLong(pause);
        }

        @Override
        public String toString() {
            return "StopwatchState[" +
                    "base=" + base +
                    ", pause=" + pause +
                    ", started=" + started +
                    ']';
        }
    }
}
