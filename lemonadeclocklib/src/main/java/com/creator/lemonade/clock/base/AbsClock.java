package com.creator.lemonade.clock.base;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Checkable;

import com.creator.lemonade.clock.util.DimenConverter;

/**
 * This is a base class for clock view
 *
 * @author Felix.Liang
 */
@SuppressWarnings("unused")
public abstract class AbsClock extends View implements Checkable {

    /**
     * The current checked state of the view
     */
    private boolean mChecked;

    /**
     * The temporary {@link TypedValue} for holding attributes
     *
     * @see #getThemeIntAttribute(int)
     */
    private TypedValue mTempValue;

    public AbsClock(Context context) {
        this(context, null);
    }

    public AbsClock(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbsClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Converts dp value to px value
     *
     * @param dpVal dp value to be converted
     * @return px value
     */
    protected int dp2px(float dpVal) {
        return DimenConverter.dp2px(dpVal, getResources());
    }

    /**
     * Converts sp value to px value
     *
     * @param spVal sp value to be converted
     * @return px value
     */
    protected int sp2dp(float spVal) {
        return DimenConverter.sp2px(spVal, getResources());
    }

    /**
     * Get integer attribute from current theme, such as colorPrimary, colorPrimaryDark, etc.
     *
     * @param attrId The resource id of attribute
     * @return The value of attribute
     */
    protected int getThemeIntAttribute(int attrId) {
        if (mTempValue == null) {
            mTempValue = new TypedValue();
        }
        final Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(attrId, mTempValue, true);
        return mTempValue.data;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
