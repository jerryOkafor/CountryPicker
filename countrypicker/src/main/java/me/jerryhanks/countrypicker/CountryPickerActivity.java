package me.jerryhanks.countrypicker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.List;
import java.util.Map;

public class CountryPickerActivity extends AppCompatActivity {

    private boolean showFastScroller = true;
    private int fastScrollerBubbleColor = 0;
    private int fastScrollerHandleColor = 0;
    private int fastScrollerBubbleTextAppearance = 0;
    private List<Country> countries;
    private TextView tvNoResult;
    private CountryPickerAdapter.OnItemClickCallback callback;
    private boolean showCountryCode;
    private String TAG = CountryPickerActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private CountryPickerAdapter pickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate called");
        setContentView(R.layout.activity_country_picker);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.inflateMenu(R.menu.picker_dialog);
        SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.search).getActionView();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_black);
            getSupportActionBar().setTitle(getString(R.string.select_country));
        }

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                showFastScroller = bundle.getBoolean(CountryPicker.EXTRA_SHOW_FAST_SCROLL);
                fastScrollerBubbleColor = bundle.getInt(CountryPicker.EXTRA_SHOW_FAST_SCROLL_BUBBLE_COLOR);
                fastScrollerHandleColor = bundle.getInt(CountryPicker.EXTRA_SHOW_FAST_SCROLL_HANDLER_COLOR);
                fastScrollerBubbleTextAppearance = bundle.getInt(CountryPicker.EXTRA_SHOW_FAST_SCROLL_BUBBLE_TEXT_APPEARANCE);
                showCountryCode = bundle.getBoolean(CountryPicker.EXTRA_SHOW_COUNTRY_CODE_IN_LIST);
            }
        }


        //list all the countries
        countries = Util.loadDataFromJson(this);

        //country Groups
        Map<String, List<Country>> countryGroup = Util.mapList(countries);
        callback = country -> {
            //set result and finish
            Intent intent = new Intent();
            intent.putExtra(CountryPicker.EXTRA_COUNTRY, country);
            setResult(Activity.RESULT_OK, intent);
            finish();
        };


        //recyclerView
        recyclerView = findViewById(R.id.recyclerCountryPicker);

        //no result tv
        tvNoResult = findViewById(R.id.tvNoResult);

        //create picker adapter
        pickerAdapter = new CountryPickerAdapter(this, callback, countries, countryGroup,
                searchView, tvNoResult, showCountryCode);

        //fast scroller
        FastScroller fastScroller = findViewById(R.id.fastScroll);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(pickerAdapter);

        fastScroller.setRecyclerView(recyclerView);
        if (isShowFastScroller()) {
            if (getFastScrollerBubbleColor() != 0) {
                fastScroller.setBubbleColor(getFastScrollerBubbleColor());
            }

            if (getFastScrollerHandleColor() != 0) {
                fastScroller.setHandleColor(getFastScrollerHandleColor());
            }

            if (getFastScrollerBubbleTextAppearance() != 0) {
                try {
                    fastScroller.setBubbleTextAppearance(getFastScrollerBubbleTextAppearance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            fastScroller.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "OnCreate Options menu called");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.picker_dialog, menu);
        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        pickerAdapter.setSearchView(searchView);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public int getFastScrollerBubbleColor() {
        return fastScrollerBubbleColor;
    }

    public int getFastScrollerHandleColor() {
        return fastScrollerHandleColor;
    }

    public int getFastScrollerBubbleTextAppearance() {
        return fastScrollerBubbleTextAppearance;
    }


    public boolean isShowFastScroller() {
        return showFastScroller;
    }
}
