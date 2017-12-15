package me.jerryhanks.countrypicker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.List;

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class CountryPickerDialog {
    private static Dialog INSTANCE = null;

    public static void openPickerDialog(final CountryPicker picker) {
        final Context context = picker.getContext();
        Dialog dialog = getDialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
            dialog.getWindow().setContentView(R.layout.layout_country_picker);

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
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_countryDialog);

        final TextView textViewTitle = dialog.findViewById(R.id.textView_title);

        RelativeLayout rlQueryHolder = dialog.findViewById(R.id.rl_query_holder);

        TextView tvNoResult = dialog.findViewById(R.id.textView_noresult);

        RelativeLayout rlHolder = dialog.findViewById(R.id.rl_holder);

        ImageView imgDismiss = dialog.findViewById(R.id.ivDismiss);
        SearchView searchView = dialog.findViewById(R.id.searchView);


        //set click listeners
        imgDismiss.setOnClickListener(v -> dialog.dismiss());

        final CountryAdapter.OnItemClickCallback callback = country -> {
            picker.updateCountry(country);
            dialog.dismiss();
        };

        final CountryAdapter cca = new CountryAdapter(context, callback, countries, rlQueryHolder, searchView, tvNoResult);
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
