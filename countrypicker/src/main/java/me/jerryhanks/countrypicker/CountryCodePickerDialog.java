package me.jerryhanks.countrypicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class CountryCodePickerDialog {
    private static Dialog INSTANCE = null;

    public static void openPickerDialog(final CountryPicker picker) {
        final Context context = picker.getContext();
        Dialog dialog = getDialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
            dialog.getWindow().setContentView(R.layout.layout_picker_dialog);

        //keyboard
        if (picker.isSearchAllowed() && picker.isDialogKeyboardAutoPopup()) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } else {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }


        //list all the countries
        List<Country> countries = loadDataFromJson(context);

        //set up dialog views
        //dialog views
        RecyclerView recyclerView_countryDialog = dialog.findViewById(R.id.recycler_countryDialog);
        final TextView textViewTitle = dialog.findViewById(R.id.textView_title);
        RelativeLayout rlQueryHolder = dialog.findViewById(R.id.rl_query_holder);
        ImageView imgClearQuery = dialog.findViewById(R.id.img_clear_query);
        final EditText editText_search = dialog.findViewById(R.id.editText_search);
        TextView textView_noResult = dialog.findViewById(R.id.textView_noresult);
        RelativeLayout rlHolder = dialog.findViewById(R.id.rl_holder);
        ImageView imgDismiss = dialog.findViewById(R.id.ivDismiss);


        //set click listeners
        imgDismiss.setOnClickListener(v -> dialog.dismiss());

        final CountryCodeAdapter.OnItemClickCallback callback = country -> {
            picker.updateCountry(country);
            dialog.dismiss();
        };

        final CountryCodeAdapter cca = new CountryCodeAdapter(context, callback, countries, rlQueryHolder, editText_search, textView_noResult, dialog, imgClearQuery);
        recyclerView_countryDialog.setLayoutManager(new LinearLayoutManager(context));
        recyclerView_countryDialog.setAdapter(cca);

        //fast scroller
        FastScroller fastScroller = dialog.findViewById(R.id.fastScroll);
        fastScroller.setRecyclerView(recyclerView_countryDialog);
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

    public static List<Country> loadDataFromJson(Context context) {

        InputStream inputStream = context.getResources().openRawResource(R.raw.country_codes);
        String jsonString = readJsonFile(inputStream);

        //create gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES);
        Gson gson = gsonBuilder.create();

        Country[] countries = gson.fromJson(jsonString, Country[].class);
        List<Country> countryList = Arrays.asList(countries);
        return countryList;
    }

    private static String readJsonFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte bufferByte[] = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(bufferByte)) != -1) {
                outputStream.write(bufferByte, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }

    private static void hideKeyboard(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
