package me.jerryhanks.countrypicker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.List;

public class CountryPickerActivity extends AppCompatActivity {

    private boolean showFastScroller = true;
    private int fastScrollerBubbleColor = 0;
    private int fastScrollerHandleColor = 0;
    private int fastScrollerBubbleTextAppearance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_country_picker);

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
        List<Country> countries = Util.loadDataFromJson(this);

        //set up dialog views
        //dialog views
        RecyclerView recyclerView = findViewById(R.id.recycler_countryDialog);

        RelativeLayout rlQueryHolder = findViewById(R.id.rl_query_holder);

        TextView tvNoResult = findViewById(R.id.textView_noresult);

        ImageView imgDismiss = findViewById(R.id.ivDismiss);
        SearchView searchView = findViewById(R.id.searchView);

        imgDismiss.setVisibility(View.INVISIBLE);

        final CountryPickerAdapter.OnItemClickCallback callback = country -> {
            //set result and finish
            Intent intent = new Intent();
            intent.putExtra(CountryPicker.EXTRA_COUNTRY, country);
            setResult(Activity.RESULT_OK, intent);
            finish();
        };

        final CountryPickerAdapter cca = new CountryPickerAdapter(this, callback, countries, rlQueryHolder, searchView, tvNoResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cca);

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
