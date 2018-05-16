package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.creator.lemonade.clock.base.AbsClock;

public class Timer extends AbsClock {

    public Timer(Context context) {
        this(context, null);
    }

    public Timer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
