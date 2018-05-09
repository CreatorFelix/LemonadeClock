package com.creator.lemonade.clock.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.creator.lemonade.clock.base.AbsClock;

/**
 * A stopwatch widget
 *
 * @author Felix.Liang
 */
public class Stopwatch extends AbsClock {

    public Stopwatch(Context context) {
        super(context);
    }

    public Stopwatch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
