package com.creator.lemonade.sample.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.creator.lemonade.sample.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author Felix.Liang
 */
public class AndroidApplication extends Application {

    private static final String LOG_TAG = "Lemonade_" + AndroidApplication.class.getSimpleName();
    private final ActivityWatcher mActivityWatcher = new ActivityWatcher();

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakDetection();
        registerActivityLifecycleCallbacks(mActivityWatcher);
    }

    private void initLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }

    static class ActivityWatcher implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.i(LOG_TAG, activity.getClass().getSimpleName() + " >> onCreate");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.i(LOG_TAG, activity.getClass().getSimpleName() + " >> onStarted");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.i(LOG_TAG, activity.getClass().getSimpleName() + " >> onResume");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.i(LOG_TAG, activity.getClass().getSimpleName() + " >> onPause");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.i(LOG_TAG, activity.getClass().getSimpleName() + " >> onStop");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.i(LOG_TAG, activity.getClass().getSimpleName() + " >> onSaveInstanceState");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.i(LOG_TAG, activity.getClass().getSimpleName() + " >> onDestroy");
        }
    }
}
