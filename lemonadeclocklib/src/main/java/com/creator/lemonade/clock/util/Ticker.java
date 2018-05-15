package com.creator.lemonade.clock.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * The Class for updating current time
 *
 * @author Felix.Liang
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Ticker {

    private boolean mIs24Format;

    /**
     * The delayed time before next time update
     */
    private static final int UPDATE_DELAY_TIME = 30;

    /**
     * The application environment this ticker lives in
     */
    private final Context mContext;

    private Handler mHandler;
    private String mTimeZone;

    /**
     * {@link Calendar} for recording current time
     */
    private Calendar mTime;

    /**
     * Listener to  be notified upon current time update
     */
    private OnTimeUpdateListener mOnTimeUpdateListener;

    /**
     * The current updating state of the ticker
     */
    private boolean mUpdating;

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (mTimeZone == null && Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
                final String timeZone = intent.getStringExtra("time-zone");
                createTime(timeZone);
            }
            if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                update24Format(context);
            }
            onTimeChanged();
        }
    };

    private final Runnable mTick = new Runnable() {
        @Override
        public void run() {
            onTimeChanged();
            mHandler.postDelayed(mTick, UPDATE_DELAY_TIME);
        }
    };

    private void createTime(String timeZone) {
        if (timeZone != null) {
            mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        } else {
            mTime = Calendar.getInstance();
        }
    }

    public Ticker(Context context) {
        this(context, null);
    }

    public Ticker(Context context, String timeZone) {
        mContext = context;
        mTimeZone = timeZone;
        update24Format(context);
        createTime(mTimeZone);
    }

    private void update24Format(Context context) {
        mIs24Format = android.text.format.DateFormat.is24HourFormat(context);
    }

    /**
     * Indicates whether is 24-hour time
     *
     * @return true if is 24-hour time, false otherwise
     */
    public boolean is24Format() {
        return mIs24Format;
    }

    /**
     * This method should be called when the view in which this ticker lives has been attached to window
     *
     * @param handler handler from ui thread
     * @see View#onAttachedToWindow()
     */
    public void onAttachedToWindow(@NonNull Handler handler) {
        mHandler = handler;
        setUpdating(true);
        registerReceiver();
    }

    /**
     * This method should be called when the view in which this ticker lives has been detached window
     *
     * @see View#onDetachedFromWindow()
     */
    public void onDetachedFromWindow() {
        unRegisterReceiver();
        setUpdating(false);
        mOnTimeUpdateListener = null;
    }

    private void registerReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        mContext.registerReceiver(mIntentReceiver, filter, null, mHandler);
    }

    private void unRegisterReceiver() {
        mContext.unregisterReceiver(mIntentReceiver);
    }

    private void onTimeChanged() {
        if (mOnTimeUpdateListener != null) {
            mTime.setTimeInMillis(System.currentTimeMillis());
            mOnTimeUpdateListener.onTimeChanged(mTime);
        }
    }

    /**
     * Sets Listener to be notified for current time updates
     *
     * @param listener The listener
     */
    public void setOnTimeUpdateListener(OnTimeUpdateListener listener) {
        mOnTimeUpdateListener = listener;
    }

    /**
     * Interface to listener for updates of current time
     */
    public interface OnTimeUpdateListener {

        /**
         * Called when update current time
         *
         * @param time {@link Calendar} recording the current time
         */
        void onTimeChanged(Calendar time);
    }

    /**
     * Indicates which time zone is currently used by this ticker.
     *
     * @return The ID of the current time zone or null if the default time zone,
     * as set by the user, must be used
     * @see TimeZone
     * @see java.util.TimeZone#getAvailableIDs()
     * @see #setTimeZone(String)
     */
    public String getTimeZone() {
        return mTimeZone;
    }

    /**
     * Sets the specified time zone to use in this clock. When the time zone
     * is set through this method, system time zone changes (when the user
     * sets the time zone in settings for instance) will be ignored.
     *
     * @param timeZone The desired time zone's ID as specified in {@link TimeZone}
     *                 or null to user the time zone specified by the user
     *                 (system time zone)
     * @see #getTimeZone()
     * @see java.util.TimeZone#getAvailableIDs()
     * @see TimeZone#getTimeZone(String)
     */
    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
        createTime(timeZone);
        onTimeChanged();
    }

    /**
     * Sets current updating state of the ticker
     *
     * @param update True if the ticker is updating, false otherwise
     */
    public void setUpdating(boolean update) {
        if (mHandler != null) {
            if (mUpdating != update) {
                if (update) {
                    mHandler.post(mTick);
                } else {
                    mHandler.removeCallbacks(mTick);
                }
                mUpdating = update;
            }
        }
    }
}
