package me.jerryhanks.countrypicker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.List;

public class CountryPickerActivity extends AppCompatActivity {

    private boolean showFastScroller = true;
    private int fastScrollerBubbleColor = 0;
    private int fastScrollerHandleColor = 0;
    private int fastScrollerBubbleTextAppearance = 0;
    private List<Country> countries;
    private RecyclerView recyclerView;
    private RelativeLayout rlQueryHolder;
    private TextView tvNoResult;
    private CountryPickerAdapter.OnItemClickCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_picker);
        Toolbar toolbar = findViewById(R.id.my_toolbar);

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
            }
        }


        //list all the countries
        countries = Util.loadDataFromJson(this);

        //set up dialog views
        //dialog views
        recyclerView = findViewById(R.id.recycler_countryDialog);

        rlQueryHolder = findViewById(R.id.rl_query_holder);

        tvNoResult = findViewById(R.id.textView_noresult);


        callback = country -> {
            //set result and finish
            Intent intent = new Intent();
            intent.putExtra(CountryPicker.EXTRA_COUNTRY, country);
            setResult(Activity.RESULT_OK, intent);
            finish();
        };


        //fast scroller
        FastScroller fastScroller = findViewById(R.id.fastScroll);
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

    public boolean isShowFastScroller() {
        return showFastScroller;
    }

    public void setShowFastScroller(boolean showFastScroller) {
        this.showFastScroller = showFastScroller;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.picker_dialog, menu);
        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        final CountryPickerAdapter cca = new CountryPickerAdapter(this, callback, countries, rlQueryHolder, searchView, tvNoResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cca);
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


}
