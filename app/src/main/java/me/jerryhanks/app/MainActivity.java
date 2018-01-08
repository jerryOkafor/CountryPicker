package me.jerryhanks.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import me.jerryhanks.countrypicker.Country;
import me.jerryhanks.countrypicker.CountryPicker;
import me.jerryhanks.countrypicker.PhoneNumberEditText;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private PhoneNumberEditText picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
        picker = findViewById(R.id.countryPicker);
        Switch s1 = findViewById(R.id.switch1);
        s1.setOnCheckedChangeListener(this);
        s1.setChecked(picker.isShowCountryCodeInView());

        Switch s2 = findViewById(R.id.switch2);
        s2.setOnCheckedChangeListener(this);
        s2.setChecked(picker.isShowCountryCodeInList());

        Switch s3 = findViewById(R.id.switch3);
        s3.setOnCheckedChangeListener(this);
        s3.setChecked(picker.isShowCountryDialCodeInView());

        Switch s4 = findViewById(R.id.switch4);
        s4.setOnCheckedChangeListener(this);
        s4.setChecked(picker.isShowFullscreenDialog());


        Switch s7 = findViewById(R.id.switch5);
        s7.setOnCheckedChangeListener(this);
        s7.setChecked(picker.isShowFastScroller());

        //result tv
        TextView resultTv = findViewById(R.id.tvResult);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            String fullNumber = picker.getFullNumber();
            String fullNumberWithPlus = picker.getFullNumberWithPlus();
            String formattedPhone = picker.getFormattedFullNumber();

            resultTv.setText(getString(R.string.fmt_result, fullNumber, fullNumberWithPlus, formattedPhone));
        });

//        Button dialogPicker = findViewById(R.id.button2);
//        dialogPicker.setOnClickListener(v -> {
//            CountryPicker.showDialogPicker(this, country ->
//                            Toast.makeText(MainActivity.this, country.toString(), Toast.LENGTH_LONG).show(), true,
//                    0, 0, 0,
//                    true, true, true, true);
//        });
//
//        Button fullscreenPicker = findViewById(R.id.button3);
//        fullscreenPicker.setOnClickListener(v ->
//                CountryPicker.showFullScreenPicker(this, true, 0,
//                        0, 0, true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CountryPicker.PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                picker.handleActivityResult(data);
            if (data != null) {
                Country country = data.getParcelableExtra(CountryPicker.EXTRA_COUNTRY);
                Toast.makeText(this, country.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.switch1:
                picker.setShowCountryCodeInView(isChecked);
                break;
            case R.id.switch2:
                picker.setShowCountryCodeInList(isChecked);
                break;
            case R.id.switch3:
                picker.setShowCountryDialCodeInView(isChecked);
                break;
            case R.id.switch4:
                picker.setShowFullscreenDialog(isChecked);
                break;
            case R.id.switch5:
                picker.setShowFastScroller(isChecked);
                break;
            default:
                break;
        }
    }
}