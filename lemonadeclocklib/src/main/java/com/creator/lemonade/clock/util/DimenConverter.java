package com.creator.lemonade.clock.util;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Class for dimension converting
 *
 * @author Felix.Liang
 */
public class DimenConverter {

    /**
     * Converts dp value to px value
     *
     * @param dpVal     dp value to be converted
     * @param resources application's resources
     * @return px value
     */
    public static int dp2px(float dpVal, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, resources.getDisplayMetrics());
    }

    /**
     * Converts sp value to px value
     *
     * @param spVal     sp value to be converted
     * @param resources application's resources
     * @return px value
     */
    public static int sp2px(float spVal, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, resources.getDisplayMetrics());
    }
}
