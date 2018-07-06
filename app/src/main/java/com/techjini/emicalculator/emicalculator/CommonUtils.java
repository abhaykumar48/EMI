package com.techjini.emicalculator.emicalculator;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigDecimal;

public class CommonUtils {

    public static BigDecimal getDecimalTwoDig(double amount) {
        try {
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(amount)).setScale(2, BigDecimal.ROUND_HALF_UP);
            return bigDecimal;
        } catch (Exception e) {
            e.printStackTrace();
            return new BigDecimal(0.00D);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
