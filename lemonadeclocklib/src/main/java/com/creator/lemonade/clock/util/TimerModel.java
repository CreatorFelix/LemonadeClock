package com.creator.lemonade.clock.util;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

/**
 * This class defines the basic logic and holds the state of a timer.
 *
 * @author Felix.Liang
 */
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

    /**
     * @see #setSuspend(boolean)
     */
    private boolean mSuspend;

    private final Runnable mTick = new Runnable() {
        @Override
        public void run() {
            if (mHandler != null && onTimeChanged()) {
                mHandler.postDelayed(mTick, UPDATE_DELAY_TIME);
            }
        }
    };

    public TimerModel() {
        mState = new TimerState();
    }

    private boolean onTimeChanged() {
        final long restTime = getTimerRestTime();
        if (mTimerWatcher != null) {
            mTimerWatcher.onTimeChanged(restTime, mState.total);
        }
        return restTime > 0;
    }

    /**
     * This method should be called when this timer model attaches to its environment.
     *
     * @param handler handler to handle messages
     */
    public void attach(@NonNull Handler handler) {
        mHandler = handler;
        updateRunning();
    }

    /**
     * This method should be called when this timer model detaches from its environment.
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

    /**
     * Gets the holding state of this timer model.
     *
     * @return The {@link TimerState} instance
     */
    public TimerState getState() {
        return mState;
    }

    /**
     * Sets the state of this timer.
     *
     * @param ss The target state
     */
    public void setState(TimerState ss) {
        mState = new TimerState(ss);
        if (mTimerWatcher != null) {
            mTimerWatcher.onTimeChanged(getTimerRestTime(), mState.total);
        }
        updateRunning();
    }

    private void setStarted(boolean started) {
        if (mState.started != started) {
            mState.started = started;
            if (started) {
                mState.base = getCurrentElapsedTime();
            }
            performStateChanged();
            updateRunning();
        }
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

    /**
     * Gets the rest time of this timer model.
     *
     * @return rest time in milliseconds
     */
    private long getTimerRestTime() {
        if (!isStarted()) return TimerState.DEFAULT_TIME;
        if (mState.base == TimerState.DEFAULT_TIME) {
            throw new IllegalStateException("Base time has not been initialized.");
        }
        final long totalTime = mState.total;
        if (isPaused()) {
            final long timeFromPause = mState.pause - mState.base;
            if (timeFromPause < 0 || timeFromPause > totalTime) {
                throw new IllegalStateException("Illegal timer state: " + mState.toString());
            }
            return totalTime - timeFromPause;
        } else {
            long elapsedFromBase = getCurrentElapsedTime() - mState.base;
            if (elapsedFromBase < 0) {
                throw new IllegalStateException("Elapsed time should <= base time: " + mState.toString());
            }
            long restTime = totalTime - elapsedFromBase;
            if (restTime <= 0) {
                performTimeout();
                return 0;
            }
            return restTime;
        }
    }

    /**
     * @return the milliseconds since boot
     * @see SystemClock#elapsedRealtime()
     */
    private long getCurrentElapsedTime() {
        return SystemClock.elapsedRealtime();
    }

    /**
     * Starts or resumes the timer.
     */
    public void startOrResume() {
        if (!isStarted()) {
            start();
        } else {
            resume();
        }
    }

    /**
     * Starts the timer.
     *
     * @see #startOrResume()
     */
    private void start() {
        setStarted(true);
    }

    /**
     * Resumes the timer if it is paused.
     *
     * @see #startOrResume()
     */
    private void resume() {
        if (isPaused()) {
            final long elapsedTimeFromPause = getCurrentElapsedTime() - mState.pause;
            mState.base += elapsedTimeFromPause;
            mState.pause = TimerState.DEFAULT_TIME;
            performStateChanged();
            updateRunning();
        }
    }

    /**
     * Pauses the timer. Nothing happens if the timer isn't started or has been paused.
     */
    public void pause() {
        if (isStarted() && !isPaused()) {
            mState.pause = getCurrentElapsedTime();
            performStateChanged();
            updateRunning();
        }
    }

    /**
     * Call this timer model's TimerWatcher, if it is defined. Performs all the actions
     * associated with changes of state.
     */
    private void performStateChanged() {
        if (mTimerWatcher != null) {
            mTimerWatcher.onStateChanged(isStarted(), isPaused());
        }
    }

    /**
     * Call this timer model's TimerWatcher, if it is defined. Performs all the actions
     * associated with timeout.
     */
    private void performTimeout() {
        if (mTimerWatcher != null) {
            mTimerWatcher.onTimeout();
        }
    }

    public void reset() {
        setStarted(false);
        mState.clear();
        performStateChanged();
    }

    public void setTotalTime(long totalTime) {
        if (totalTime > 0 && mState.total != totalTime) {
            reset();
            mState.total = totalTime;
        }
    }

    public boolean isPaused() {
        return mState.started && mState.pause != TimerState.DEFAULT_TIME;
    }

    public boolean isStarted() {
        return mState.started;
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

        void onTimeout();
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
