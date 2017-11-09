package com.creator.lemonade.clock.base;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Checkable;

import com.creator.lemonade.clock.R;
import com.creator.lemonade.clock.util.DimenTransformer;

/**
 * This is a base class for clock view
 *
 * @author Felix.Liang
 */
@SuppressWarnings("unused")
public abstract class AbsClock extends View implements Checkable {

    private int mColorPrimary;
    private int mColorPrimaryDark;
    private int mColorAccent;
    private int mColorBackgroundFloating;

    /**
     * The current checked state of the view
     */
    private boolean mChecked;

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
        initThemeAttributes(context.getTheme());
    }

    /**
     * Get some attributes from current theme, such as colorPrimary, colorPrimaryDark, etc.
     *
     * @param theme the theme of current context
     */
    private void initThemeAttributes(Resources.Theme theme) {
        TypedValue outValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, outValue, true);
        mColorPrimary = outValue.data;
        theme.resolveAttribute(R.attr.colorPrimaryDark, outValue, true);
        mColorPrimaryDark = outValue.data;
        theme.resolveAttribute(R.attr.colorAccent, outValue, true);
        mColorAccent = outValue.data;
        theme.resolveAttribute(R.attr.colorBackgroundFloating, outValue, true);
        mColorBackgroundFloating = outValue.data;
    }

    /**
     * Converts dp value to px value
     *
     * @param dpVal dp value to be converted
     * @return px value
     */
    protected int dp2px(float dpVal) {
        return DimenTransformer.dp2px(dpVal, getResources());
    }

    /**
     * Converts sp value to px value
     *
     * @param spVal sp value to be converted
     * @return px value
     */
    protected int sp2dp(float spVal) {
        return DimenTransformer.sp2px(spVal, getResources());
    }

    protected int getColorPrimary() {
        return mColorPrimary;
    }

    protected int getColorPrimaryDark() {
        return mColorPrimaryDark;
    }

    protected int getColorAccent() {
        return mColorAccent;
    }

    protected int getColorBackgroundFloating() {
        return mColorBackgroundFloating;
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
