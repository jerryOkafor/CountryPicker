package me.jerryhanks.countrypicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private String TAG = MainActivity.class.getSimpleName();
    private CountryPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        Switch s7 = findViewById(R.id.switch7);
        s7.setOnCheckedChangeListener(this);
        s7.setChecked(picker.isShowFastScroller());


        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            String fullNumber = picker.getFullNumber();
            String fullNumberWithPlus = picker.getFullNumberWithPlus();
            String formattedPhone = picker.getFormattedFullNumber();

            Log.d(TAG, "FullNumber: " + fullNumber + " FullNumberWithPlus " + fullNumberWithPlus + " FullNumberFormatted " + formattedPhone);
        });
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
            case R.id.switch7:
                picker.setShowFastScroller(isChecked);
                break;
            default:
                break;
        }
    }
}


