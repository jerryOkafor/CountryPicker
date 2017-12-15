package me.jerryhanks.countrypicker;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
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
    public static void openPickerDialog(final CountryPicker picker) {
        final Context context = picker.getContext();
        final Dialog dialog = new Dialog(context);

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
        ImageView imgDismiss = dialog.findViewById(R.id.img_dismiss);

        final CountryCodeAdapter cca = new CountryCodeAdapter(context, countries, rlQueryHolder, editText_search, textView_noResult, dialog, imgClearQuery);
        recyclerView_countryDialog.setLayoutManager(new LinearLayoutManager(context));
        recyclerView_countryDialog.setAdapter(cca);


        dialog.show();

    }

    private static List<Country> loadDataFromJson(Context context) {

        InputStream inputStream = context.getResources().openRawResource(R.raw.country_codes);
        String jsonString = readJsonFile(inputStream);

        //create gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES);
        Gson gson = gsonBuilder.create();

        Country[] countries = gson.fromJson(jsonString, Country[].class);
        List<Country> countryList = Arrays.asList(countries);
        for (Country country : countryList) {
            Log.d("TAG", country.toString());
        }
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

}
