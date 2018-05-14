package com.creator.lemonade.clock.util;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

/**
 * As a model of stopwatch, this class defines the basic logic and keeps the state of stopwatch
 *
 * @author Felix.Liang
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class StopwatchModel {

    private static final long UPDATE_DELAY_TIME = 30;

    private Handler mHandler;

    private StopwatchState mState;

    private StopwatchWatcher mStopwatchWatcher;

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

    public StopwatchModel() {
        mState = new StopwatchState();
    }

    private void onTimeChanged() {
        if (mStopwatchWatcher != null) mStopwatchWatcher.onTimeChanged(getStopwatchTime());
    }

    public void onAttachToWindow(@NonNull Handler handler) {
        mHandler = handler;
    }

    public void onDetachFromWindow() {
        setSuspend(true);
        mHandler.removeCallbacks(mTick);
        updateRunning();
    }

    public void setSuspend(boolean suspend) {
        if (mSuspend != suspend) {
            mSuspend = suspend;
            updateRunning();
        }
    }

    public void startOrResume() {
        if (!isStarted()) {
            start();
        } else {
            resume();
        }
    }

    private void start() {
        setStarted(true);
    }

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

    public void pause() {
        if (isStarted() && !isPaused()) {
            mState.pause = getElapsedTime();
            if (mStopwatchWatcher != null)
                mStopwatchWatcher.onStateChanged(isStarted(), isPaused());
            updateRunning();
        }
    }

    private long getStopwatchTime() {
        if (!isStarted()) return StopwatchState.DEFAULT_TIME;
        if (isPaused()) {
            return mState.pause - mState.base;
        } else {
            return getElapsedTime() - mState.base;
        }
    }

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

    public void lap() {
        if (mStopwatchWatcher != null) {
            mStopwatchWatcher.onLap(getStopwatchTime());
        }
    }

    public boolean isStarted() {
        return mState.started;
    }

    public boolean isPaused() {
        return mState.started && mState.pause != StopwatchState.DEFAULT_TIME;
    }

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

    public StopwatchState getState() {
        return mState;
    }

    public void setState(StopwatchState ss) {
        mState = new StopwatchState(ss);
        if (mStopwatchWatcher != null) {
            mStopwatchWatcher.onStateChanged(isStarted(), isPaused());
            mStopwatchWatcher.onTimeChanged(getStopwatchTime());
        }
        updateRunning();
    }

    public void setStopwatchListener(StopwatchWatcher watcher) {
        if (mStopwatchWatcher != watcher) {
            mStopwatchWatcher = watcher;
        }
    }

    public interface StopwatchWatcher {

        void onTimeChanged(long timeInMillis);

        void onStateChanged(boolean started, boolean paused);

        void onLap(long lapTimeInMillis);
    }

    public static class StopwatchState implements Parcelable {

        private static final int DEFAULT_TIME = 0;
        private long base = DEFAULT_TIME;
        private long pause = DEFAULT_TIME;
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
