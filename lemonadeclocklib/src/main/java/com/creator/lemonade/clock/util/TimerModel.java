package com.creator.lemonade.clock.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

public class TimerModel {

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
