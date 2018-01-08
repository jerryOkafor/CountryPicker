package me.jerryhanks.countrypicker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.List;
import java.util.Map;

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class CountryPickerDialog {
    private static Dialog INSTANCE = null;

    public static void openPickerDialog(Context context,
                                        final OnCountrySelectedCallback callback,
                                        boolean showCountryCodeInList,
                                        boolean isSearchAllowed,
                                        boolean isDialogKeyboardAutoPopup, boolean isShowFastScroller, int fastScrollerBubbleColor, int fastScrollerHandleColor, int fastScrollerBubbleTextAppearance) {
        Dialog dialog = getDialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
            dialog.getWindow().setContentView(R.layout.dialog_country_picker);

        //keyboard
        if (isSearchAllowed && isDialogKeyboardAutoPopup) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }


        //list all the countries
        List<Country> countries = Util.loadDataFromJson(context);

        //country Groups
        Map<String, List<Country>> countryGroup = Util.mapList(countries);

        //set up dialog views
        //dialog views
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerCountryPicker);
        TextView tvNoResult = dialog.findViewById(R.id.tvNoResult);
        ImageView ivDismiss = dialog.findViewById(R.id.ivDismiss);
        SearchView searchView = dialog.findViewById(R.id.searchView);


        //set click listeners
        ivDismiss.setOnClickListener(v -> dialog.dismiss());

        final CountryPickerAdapter.OnItemClickCallback listener = country -> {
            if (callback != null)
                callback.updateCountry(country);
            dialog.dismiss();
        };

        final CountryPickerAdapter cca = new CountryPickerAdapter(context, listener, countries, countryGroup,
                searchView, tvNoResult, showCountryCodeInList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(cca);

        //fast scroller
        FastScroller fastScroller = dialog.findViewById(R.id.fastScroll);
        fastScroller.setRecyclerView(recyclerView);
        if (isShowFastScroller) {
            if (fastScrollerBubbleColor != 0) {
                fastScroller.setBubbleColor(fastScrollerBubbleColor);
            }

            if (fastScrollerHandleColor != 0) {
                fastScroller.setHandleColor(fastScrollerHandleColor);
            }

            if (fastScrollerBubbleTextAppearance != 0) {
                try {
                    fastScroller.setBubbleTextAppearance(fastScrollerBubbleTextAppearance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            fastScroller.setVisibility(View.GONE);
        }


        dialog.show();

    }

    private static Dialog getDialog(@Nullable Context context) {
        if (INSTANCE == null && context != null) {
            return new Dialog(context);
        }
        return INSTANCE;
    }


    public interface OnCountrySelectedCallback {
        void updateCountry(Country country);
    }
}
