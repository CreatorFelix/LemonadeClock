package com.creator.lemonade.clock.util;

import android.os.SystemClock;

public class TimerModel {

    public static class TimerState {

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
    }
}
