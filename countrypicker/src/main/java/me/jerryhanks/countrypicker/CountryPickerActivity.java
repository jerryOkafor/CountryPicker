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

    private boolean showFastScroller = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_country_picker);


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

        final CountryAdapter.OnItemClickCallback callback = country -> {
            //set result and finish
            Intent intent = new Intent();
            intent.putExtra(CountryPicker.EXTRA_COUNTRY, country);
            setResult(Activity.RESULT_OK, intent);
            finish();
        };

        final CountryAdapter cca = new CountryAdapter(this, callback, countries, rlQueryHolder, searchView, tvNoResult);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cca);

        //fast scroller
        FastScroller fastScroller = findViewById(R.id.fastScroll);
        fastScroller.setRecyclerView(recyclerView);
        if (isShowFastScroller()) {
//            if (picker.getFastScrollerBubbleColor() != 0) {
//                fastScroller.setBubbleColor(picker.getFastScrollerBubbleColor());
//            }
//
//            if (picker.getFastScrollerHandleColor() != 0) {
//                fastScroller.setHandleColor(picker.getFastScrollerHandleColor());
//            }
//
//            if (picker.getFastScrollerBubbleTextAppearance() != 0) {
//                try {
//                    fastScroller.setBubbleTextAppearance(picker.getFastScrollerBubbleTextAppearance());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

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
}
