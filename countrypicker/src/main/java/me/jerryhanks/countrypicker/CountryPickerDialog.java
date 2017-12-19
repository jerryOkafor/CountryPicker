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

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class CountryPickerDialog {
    private static Dialog INSTANCE = null;

    public static void openPickerDialog(final CountryPicker picker, boolean showCountryCodeInList) {
        final Context context = picker.getContext();
        Dialog dialog = getDialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
            dialog.getWindow().setContentView(R.layout.dialog_country_picker);

        //keyboard
        if (picker.isSearchAllowed() && picker.isDialogKeyboardAutoPopup()) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }


        //list all the countries
        List<Country> countries = Util.loadDataFromJson(context);

        //set up dialog views
        //dialog views
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerCountryPicker);
        TextView tvNoResult = dialog.findViewById(R.id.tvNoResult);
        ImageView ivDismiss = dialog.findViewById(R.id.ivDismiss);
        SearchView searchView = dialog.findViewById(R.id.searchView);


        //set click listeners
        ivDismiss.setOnClickListener(v -> dialog.dismiss());

        final CountryPickerAdapter.OnItemClickCallback callback = country -> {
            picker.updateCountry(country);
            dialog.dismiss();
        };

        final CountryPickerAdapter cca = new CountryPickerAdapter(context, callback, countries,
                searchView, tvNoResult, showCountryCodeInList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(cca);

        //fast scroller
        FastScroller fastScroller = dialog.findViewById(R.id.fastScroll);
        fastScroller.setRecyclerView(recyclerView);
        if (picker.isShowFastScroller()) {
            if (picker.getFastScrollerBubbleColor() != 0) {
                fastScroller.setBubbleColor(picker.getFastScrollerBubbleColor());
            }

            if (picker.getFastScrollerHandleColor() != 0) {
                fastScroller.setHandleColor(picker.getFastScrollerHandleColor());
            }

            if (picker.getFastScrollerBubbleTextAppearance() != 0) {
                try {
                    fastScroller.setBubbleTextAppearance(picker.getFastScrollerBubbleTextAppearance());
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


}
