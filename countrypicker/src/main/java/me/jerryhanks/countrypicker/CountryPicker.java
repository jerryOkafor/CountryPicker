package me.jerryhanks.countrypicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

/**
 * @author Jerry Hanks on 12/22/17.
 */

public class CountryPicker {

    public static final int PICKER_REQUEST_CODE = 101;
    public static final String EXTRA_COUNTRY = "me.jerryhanks.countrypicker_EXTRA_COUNTRY";
    public static final String EXTRA_SHOW_FAST_SCROLL = "me.jerryhanks.countrypicker_EXTRA_SHOW_FAST_SCROLL";
    public static final String EXTRA_SHOW_FAST_SCROLL_BUBBLE_COLOR = "me.jerryhanks.countrypicker_EXTRA_SHOW_FAST_BUBBLE_COLOR";
    public static final String EXTRA_SHOW_FAST_SCROLL_HANDLER_COLOR = "me.jerryhanks.countrypicker_EXTRA_SHOW_FAST_HANDLE_COLOR";
    public static final String EXTRA_SHOW_FAST_SCROLL_BUBBLE_TEXT_APPEARANCE = "me.jerryhanks.countrypicker_EXTRA_SHOW_FAST_BUBBLE_TEXT_APPEARANCE";
    public static final String EXTRA_SHOW_COUNTRY_CODE_IN_LIST = "me.jerryhanks.countrypicker_EXTRA_SHOW_COUNTRY_CODE_IN_LIST";

    public static void showFullScreenPicker(@NonNull Activity activity,
                                            boolean showFastScroller,
                                            @ColorRes int fastScrollerBubbleColor,
                                            @ColorRes int fastScrollerHandleColor,
                                            @StyleRes int fastScrollerBubbleTextAppearance,
                                            boolean showCountryCodeInList) {
        Intent intent = new Intent(activity, CountryPickerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CountryPicker.EXTRA_SHOW_FAST_SCROLL, showFastScroller);
        bundle.putInt(CountryPicker.EXTRA_SHOW_FAST_SCROLL_BUBBLE_COLOR, fastScrollerBubbleColor);
        bundle.putInt(CountryPicker.EXTRA_SHOW_FAST_SCROLL_HANDLER_COLOR, fastScrollerHandleColor);
        bundle.putInt(CountryPicker.EXTRA_SHOW_FAST_SCROLL_BUBBLE_TEXT_APPEARANCE, fastScrollerBubbleTextAppearance);
        bundle.putBoolean(CountryPicker.EXTRA_SHOW_COUNTRY_CODE_IN_LIST, showCountryCodeInList);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, PICKER_REQUEST_CODE);
    }


    public static void showDialogPicker(
            Context context,
            CountryPickerDialog.OnCountrySelectedCallback callback,
            boolean showFastScroller,
            @ColorRes int fastScrollerBubbleColor,
            @ColorRes int fastScrollerHandleColor,
            @StyleRes int fastScrollerBubbleTextAppearance,
            boolean showCountryCodeInList, boolean isShowCountryCodeInList,
            boolean isDialogKeyboardAutoPopup, boolean isSearchAllowed) {
        CountryPickerDialog.openPickerDialog(context, callback, isShowCountryCodeInList,
                isSearchAllowed, isDialogKeyboardAutoPopup, showFastScroller,
                fastScrollerBubbleColor, fastScrollerHandleColor, fastScrollerBubbleTextAppearance);

    }
}
