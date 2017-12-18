package me.jerryhanks.countrypicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class CountryPicker extends TextInputEditText {
    private final static int EXTRA_PADDING = 5;
    private static final String TAG = CountryPicker.class.getSimpleName();
    public static final int PICKER_REQUEST_CODE = 101;
    public static final String EXTRA_COUNTRY = "me.jerryhanks.countrypicker_EXTRA_COUNTRY";
    public static final String EXTRA_SHOW_FAST_SCROLL = "me.jerryhanks.countrypicker_EXTRA_SHOW_FAST_SCROLL";
    public static final String EXTRA_SHOW_FAST_SCROLL_BUBBLE_COLOR = "me.jerryhanks.countrypicker_EXTRA_SHOW_FAST_BUBBLE_COLOR";
    public static final String EXTRA_SHOW_FAST_SCROLL_HANDLER_COLOR = "me.jerryhanks.countrypicker_EXTRA_SHOW_FAST_HANDLE_COLOR";
    public static final String EXTRA_SHOW_FAST_SCROLL_BUBBLE_TEXT_APPEARANCE = "me.jerryhanks.countrypicker_EXTRA_SHOW_FAST_BUBBLE_TEXT_APPEARANCE";
    private static final String CP_PREF_FILE = "cp_pref_file";
    public static final String EXTRA_SHOW_COUNTRY_CODE_IN_LIST = "me.jerryhanks.countrypicker_EXTRA_SHOW_COUNTRY_CODE_IN_LIST";
    private String lastSelectionTag = "last_selection_tag";
    private String defaultCountryName;
    private String preferredCountries;

    private boolean isRTL;
    private boolean searchAllowed;
    private boolean dialogKeyboardAutoPopup;
    private boolean showFastScroller;
    private int fastScrollerBubbleColor;
    private int fastScrollerHandleColor;
    private int fastScrollerBubbleTextAppearance;
    private boolean autoDetectCountryEnabled;
    private boolean showFullscreenDialog;
    private boolean showCountryCodeInView;
    private boolean showCountryDialCodeInView;
    private boolean rememberLastSelection;
    private boolean setCountryCodeBorder;
    private boolean showCountryCodeInList;

    private Language languageToApply = Language.ENGLISH;
    private AutoDetectionPref selectedAutoDetectionPref;
    private Country selectedCountry = new Country("Nigeria", "+234", "NG");
    private BitmapDrawable chip;

    public CountryPicker(Context context) {
        this(context, null);

    }

    public CountryPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CountryPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CountryPicker, defStyleAttr, 0);
            try {

                //show country code in view: false by default
                showCountryCodeInView = a.getBoolean(R.styleable.CountryPicker_cp_showCountryCodeInView, true);

                //show country code in list: false by default
                showCountryCodeInList = a.getBoolean(R.styleable.CountryPicker_cp_showCountryCodeInList, true);

                //show country dial code : true by default
                showCountryDialCodeInView = a.getBoolean(R.styleable.CountryPicker_cp_showCountryDialCodeInView, true);

                //default Country : null/empty by default
                defaultCountryName = a.getString(R.styleable.CountryPicker_cp_defaultCountryName);

                //remember last selection : false by default
                rememberLastSelection = a.getBoolean(R.styleable.CountryPicker_cp_rememberLastSelection, false);

                //show fullscreen Dialog : false by default, let the user decide
                //when they want to show full screen
                showFullscreenDialog = a.getBoolean(R.styleable.CountryPicker_cp_showFullScreeDialog, false);

                //show fast scroller : true by default, always show fast scroll
                //let the user decide when they want to hide the fast scroll
                showFastScroller = a.getBoolean(R.styleable.CountryPicker_cp_showFastScroll, true);

                //bubble color
                fastScrollerBubbleColor = a.getColor(R.styleable.CountryPicker_cp_fastScrollerBubbleColor, 0);

                //scroller handle color
                fastScrollerHandleColor = a.getColor(R.styleable.CountryPicker_cp_fastScrollerHandleColor, 0);

                //scroller text appearance
                fastScrollerBubbleTextAppearance = a.getResourceId(R.styleable.CountryPicker_cp_fastScrollerBubbleTextAppearance, 0);

                //allow the user to search : true by default, let the user search all the time
                //and decide when they do not want search
                searchAllowed = a.getBoolean(R.styleable.CountryPicker_cp_searchAllowed, true);

                //keyboard auto pop up when dialog is showing : true by default
                //always show the keyboard as soon as possible
                dialogKeyboardAutoPopup = a.getBoolean(R.styleable.CountryPicker_cp_dialogKeyboardAutoPopup, true);

                //country auto detection pref : default to SIM_NETWORK_LOCALE in that order
                int autoDetectionPrefValue = a.getInt(R.styleable.CountryPicker_cp_countryAutoDetectionPref, 123);
                selectedAutoDetectionPref = AutoDetectionPref.getPrefForValue(String.valueOf(autoDetectionPrefValue));

                //auto detect country : default to true, always try to detect the country of the user
                autoDetectCountryEnabled = a.getBoolean(R.styleable.CountryPicker_cp_autoDetectCountry, true);

                //set the border around country code
                setCountryCodeBorder = a.getBoolean(R.styleable.CountryPicker_cp_setCountryCodeBorder, false);

                //preferred countries
                preferredCountries = a.getString(R.styleable.CountryPicker_cp_preferredCountries);

            } finally {
                a.recycle();
            }
        }


        //if remember last selection is set and default country is not set
        if (TextUtils.isEmpty(defaultCountryName) && rememberLastSelection) {
            Log.d(TAG, "Loading last saved country code!");
            loadLastSelectedCountryCode();
        }

        //load the default country if it was set by the user
        if (!TextUtils.isEmpty(defaultCountryName)) {
            setSelectedCountry(getCountryForName(languageToApply, defaultCountryName));
        }

        //implement auto Country detection if it is set
        if (isAutoDetectCountryEnabled() && !isInEditMode()) {
            startAutoCountryDetection(true);
        }

        isRTL = isRTLLanguage();
        setInputType(InputType.TYPE_CLASS_NUMBER);

        updateSelectedCountry(selectedCountry);

    }

    /**
     * Detects and load the detect country
     *
     * @param resetDefault used to reset to the default country when loading the detected country fail
     */
    private void startAutoCountryDetection(boolean resetDefault) {
        try {
            boolean successfullyDetected = false;
            for (int i = 0; i < selectedAutoDetectionPref.representation.length(); i++) {
                switch (selectedAutoDetectionPref.representation.charAt(i)) {
                    case '1':
                        Log.d(TAG, "setAutoDetectedCountry: Setting using SIM");
                        successfullyDetected = detectSIMCountry(false);
                        Log.d(TAG, "setAutoDetectedCountry: Result of sim country detection:" + successfullyDetected + " current country:" + getSelectedCountryName());
                        break;
                    case '2':
                        Log.d(TAG, "setAutoDetectedCountry: Setting using NETWORK");
                        successfullyDetected = detectNetworkCountry(false);
                        Log.d(TAG, "setAutoDetectedCountry: Result of network country detection:" + successfullyDetected + " current country:" + getSelectedCountryName());
                        break;
                    case '3':
                        Log.d(TAG, "setAutoDetectedCountry: Setting using LOCALE");
                        successfullyDetected = detectLocaleCountry(false);
                        Log.d(TAG, "setAutoDetectedCountry: Result of LOCALE country detection:" + successfullyDetected + " current country:" + getSelectedCountryName());
                        break;
                }
                if (successfullyDetected) {
                    break;
                }
            }

            if (!successfullyDetected && resetDefault) {
                resetToDefaultCountry();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "setAutoDetectCountry: Exception" + e.getMessage());
            if (resetDefault) {
                resetToDefaultCountry();
            }
        }

    }

    /**
     * Return the selected Country name
     *
     * @return {@code String} name of the selected country
     */
    private String getSelectedCountryName() {
        return this.selectedCountry.getName();
    }

    /**
     * Updates the CountryPicker with the selected Country
     *
     * @param country Selected country
     */
    private void updateSelectedCountry(Country country) {
        CharSequence dialCode;
        CharSequence code;
        int drawableId;
        if (country == null) {
            dialCode = "+123";
            code = "NG";
            drawableId = R.drawable.ng;
        } else {
            dialCode = country.getDialCode();
            code = country.getCode();
            drawableId = Util.getFlagResID(country);
        }

        chip = createClusterBitmap(dialCode, code, drawableId);
        setCompoundDrawablesWithIntrinsicBounds(chip, null, null, null);
        setCompoundDrawablePadding(10);

    }


    /**
     * Draws the Country flag, code and dial code and return it as a drawable
     *
     * @param dialCode   Country dial code
     * @param code       County code.
     * @param drawableId Drawable id for the flag
     */
    private BitmapDrawable createClusterBitmap(CharSequence dialCode, CharSequence code, int drawableId) {
        View wrapper = LayoutInflater.from(getContext()).inflate(R.layout.picker_view,
                null);

        TextView tvCode = wrapper.findViewById(R.id.tvShortCode);
        tvCode.setTypeface(getTypeface());
        tvCode.setTextSize(getTextSize());
        tvCode.setTextColor(getTextColors());

        if (isShowCountryCodeInView()) {
            tvCode.setText(getContext().getString(R.string.fmt_code, code));
        }

        if (isShowCountryDialCodeInView()) {
            tvCode.setText(getContext().getString(R.string.fmt_dial_code, dialCode));
        }

        if (isShowCountryDialCodeInView() && isShowCountryCodeInView()) {
            tvCode.setText(getContext().getString(R.string.fmt_code_and_dial_code, code, dialCode));
        }

        if (!isShowCountryCodeInView() && !isShowCountryDialCodeInView()) {
            tvCode.setVisibility(View.GONE);
        }


        ImageView ivFlag = wrapper.findViewById(R.id.ivFlag);
        ivFlag.setImageResource(drawableId);


        wrapper.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        wrapper.layout(0, 0, wrapper.getMeasuredWidth(), wrapper.getMeasuredHeight());

        final Bitmap clusterBitmap = Bitmap.createBitmap(wrapper.getMeasuredWidth(),
                wrapper.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(clusterBitmap);
        wrapper.draw(canvas);

        return new BitmapDrawable(clusterBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final Rect bounds = chip.getBounds();
        final int x = (int) event.getX();
        int iconXRect = isRTL ? getRight() - bounds.width() - EXTRA_PADDING :
                getLeft() + bounds.width() + EXTRA_PADDING;

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                if (isRTL ? x >= iconXRect : x <= iconXRect) {
                    launchCountrySelectionDialog();
                }
            }
            break;

        }
        return super.onTouchEvent(event);

    }

    /**
     * Checks if RTL is enabled in the device
     *
     * @return {@code true} if enabled or {@code false} otherwise
     */
    private boolean isRTLLanguage() {
        // as getLayoutDirection was introduced in API 17, under 17 we default to LTR
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return false;
        }
        Configuration config = getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public boolean isSearchAllowed() {
        return searchAllowed;
    }

    public boolean isDialogKeyboardAutoPopup() {
        return dialogKeyboardAutoPopup;
    }

    public boolean isShowFastScroller() {
        return showFastScroller;
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

    public void updateCountry(Country country) {
        this.selectedCountry = country;
        updateSelectedCountry(this.selectedCountry);

    }

    public String getFullNumber() {
        return getFullNumberWithPlus().replace("+", " ");
    }

    public String getFullNumberWithPlus() {
        return this.selectedCountry.getDialCode() + getText().toString();
    }

    public String getFormattedFullNumber() {
        String formattedFullNumber;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            formattedFullNumber = PhoneNumberUtils.formatNumber(getFullNumberWithPlus(), getSelectedCountryCode());
        } else {
            formattedFullNumber = PhoneNumberUtils.formatNumber(getFullNumberWithPlus());
        }

        return formattedFullNumber;
    }

    private String getSelectedCountryCode() {
        if (this.selectedCountry == null)
            return "";
        return this.selectedCountry.getCode();
    }

    public boolean isAutoDetectCountryEnabled() {
        return autoDetectCountryEnabled;
    }

    public void setAutoDetectCountryEnabled(boolean autoDetectCountryEnabled) {
        this.autoDetectCountryEnabled = autoDetectCountryEnabled;
    }

    Language getLanguageToApply() {
        if (languageToApply == null) {
            updateLanguageToApply();
        }
        return languageToApply;
    }

    private void updateLanguageToApply() {
        languageToApply = Language.ENGLISH;

    }

    void setLanguageToApply(Language languageToApply) {
        this.languageToApply = languageToApply;
    }

    private void resetToDefaultCountry() {
        // TODO: 12/15/17 Implement rollback to the default country
    }

    /**
     * This will detect country from SIM info and then load it into CCP.
     *
     * @param resetDefault true if want to reset to default country when sim country cannot be detected. if false, then it
     *                     will not change currently selected country
     * @return true if it successfully sets country, false otherwise
     */
    public boolean detectSIMCountry(boolean resetDefault) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String simCountryISO = telephonyManager.getSimCountryIso();
            if (simCountryISO == null || simCountryISO.isEmpty()) {
                if (resetDefault) {
                    resetToDefaultCountry();
                }
                return false;
            }
            setSelectedCountry(getCountryForName(getLanguageToApply(), simCountryISO));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (resetDefault) {
                resetToDefaultCountry();
            }
            return false;
        }
    }

    /**
     * This will detect country from NETWORK info and then load it into CCP.
     *
     * @param resetDefault true if want to reset to default country when network country cannot be detected. if false, then it
     *                     will not change currently selected country
     * @return true if it successfully sets country, false otherwise
     */
    public boolean detectNetworkCountry(boolean resetDefault) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String networkCountryISO = telephonyManager.getNetworkCountryIso();
            if (networkCountryISO == null || networkCountryISO.isEmpty()) {
                if (resetDefault) {
                    resetToDefaultCountry();
                }
                return false;
            }
            setSelectedCountry(getCountryForName(getLanguageToApply(), networkCountryISO));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (resetDefault) {
                resetToDefaultCountry();
            }
            return false;
        }
    }

    /**
     * This will detect country from LOCALE info and then load it into CCP.
     *
     * @param resetDefault true if want to reset to default country when locale country cannot be detected. if false, then it
     *                     will not change currently selected country
     * @return true if it successfully sets country, false otherwise
     */
    public boolean detectLocaleCountry(boolean resetDefault) {
        try {
            String localeCountryISO = getContext().getResources().getConfiguration().locale.getCountry();
            if (localeCountryISO == null || localeCountryISO.isEmpty()) {
                if (resetDefault) {
                    resetToDefaultCountry();
                }
                return false;
            }
            setSelectedCountry(getCountryForName(getLanguageToApply(), localeCountryISO));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (resetDefault) {
                resetToDefaultCountry();
            }
            return false;
        }
    }

    private Country getCountryForName(Language languageToApply, String countryCode) {
        List<Country> countries = Util.loadDataFromJson(getContext());
        for (Country country : countries) {
            if (countryCode.equals(country.getCode()))
                return country;
        }
        return new Country("Nigeria", "+234", "NG");

    }

    /**
     * This will update the pref for country auto detection.
     * Remeber, this will not call setAutoDetectedCountry() to update country. This must be called separately.
     *
     * @param selectedAutoDetectionPref new detection pref
     */
    public void setCountryAutoDetectionPref(AutoDetectionPref selectedAutoDetectionPref) {
        this.selectedAutoDetectionPref = selectedAutoDetectionPref;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
        saveLastSelectedCountryCode(this.selectedCountry.getCode());
        updateSelectedCountry(this.selectedCountry);
    }

    public boolean isShowCountryCodeInView() {
        return showCountryCodeInView;
    }

    public void setShowCountryCodeInView(boolean show) {
        this.showCountryCodeInView = show;
        updateSelectedCountry(this.selectedCountry);
    }

    public boolean isShowCountryDialCodeInView() {
        return showCountryDialCodeInView;
    }

    public void setShowCountryDialCodeInView(boolean show) {
        this.showCountryDialCodeInView = show;
        updateSelectedCountry(this.selectedCountry);
    }

    public boolean isSetCountryCodeBorder() {
        return setCountryCodeBorder;
    }

    public void setSetCountryCodeBorder(boolean setCountryCodeBorder) {
        this.setCountryCodeBorder = setCountryCodeBorder;
        updateSelectedCountry(this.selectedCountry);
    }

    public boolean isShowCountryCodeInList() {
        return showCountryCodeInList;
    }

    public void setShowCountryCodeInList(boolean show) {
        this.showCountryCodeInList = show;
    }

    public String getPreferredCountries() {
        return preferredCountries;
    }

    public void setPreferredCountries(String preferredCountries) {
        this.preferredCountries = preferredCountries;
    }

    public void setShowFullscreenDialog(boolean showFullscreenDialog) {
        this.showFullscreenDialog = showFullscreenDialog;
    }

    public boolean isShowFullscreenDialog() {
        return showFullscreenDialog;
    }

    public void setShowFastScroller(boolean showFastScroller) {
        this.showFastScroller = showFastScroller;
    }

    public enum Language {
        ENGLISH("en");

        String code;

        Language(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public enum AutoDetectionPref {
        SIM_ONLY("1"), //sim only
        NETWORK_ONLY("2"), //network only
        LOCALE_ONLY("3"), //local only
        SIM_NETWORK("12"), //sim  then network
        NETWORK_SIM("21"), //network
        SIM_LOCALE("13"), //sim then local
        LOCALE_SIM("31"), //local then sim
        NETWORK_LOCALE("23"), //network then local
        LOCALE_NETWORK("32"), //local then network
        SIM_NETWORK_LOCALE("123"), //sim then network then local
        SIM_LOCALE_NETWORK("132"), //sim then local then network
        NETWORK_SIM_LOCALE("213"), //network then sim then local
        NETWORK_LOCALE_SIM("231"), //network the local then sim
        LOCALE_SIM_NETWORK("312"), //local then sim then network
        LOCALE_NETWORK_SIM("321"); //local then network then sim

        String representation;

        AutoDetectionPref(String representation) {
            this.representation = representation;
        }

        public static AutoDetectionPref getPrefForValue(String value) {
            for (AutoDetectionPref autoDetectionPref : AutoDetectionPref.values()) {
                if (autoDetectionPref.representation.equals(value)) {
                    return autoDetectionPref;
                }
            }
            return SIM_NETWORK_LOCALE;
        }
    }

    public void launchCountrySelectionDialog() {
        if (showFullscreenDialog) {
            try {
                Intent intent = new Intent(getContext(), CountryPickerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(EXTRA_SHOW_FAST_SCROLL, showFastScroller);
                bundle.putInt(EXTRA_SHOW_FAST_SCROLL_BUBBLE_COLOR, fastScrollerBubbleColor);
                bundle.putInt(EXTRA_SHOW_FAST_SCROLL_HANDLER_COLOR, fastScrollerHandleColor);
                bundle.putInt(EXTRA_SHOW_FAST_SCROLL_BUBBLE_TEXT_APPEARANCE, fastScrollerBubbleTextAppearance);
                bundle.putBoolean(EXTRA_SHOW_COUNTRY_CODE_IN_LIST, isShowCountryCodeInList());
                ((Activity) getContext()).startActivityForResult(intent, PICKER_REQUEST_CODE);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        } else {
            CountryPickerDialog.openPickerDialog(this, isShowCountryCodeInList());
        }
    }

    public void handleActivityResult(Intent data) {
        Country country = data.getParcelableExtra(EXTRA_COUNTRY);
        Log.d(TAG, "Country: " + country);
        setSelectedCountry(country);
        updateSelectedCountry(country);

    }

    /**
     * Saves the last selected Country code into Sharedpref
     * when remember last selection is set
     */
    private void saveLastSelectedCountryCode(String countryCode) {
        //get instance of the shared pref
        SharedPreferences preferences = getContext().getSharedPreferences(CP_PREF_FILE, Context.MODE_PRIVATE);

        //get the Editor
        SharedPreferences.Editor editor = preferences.edit();

        //put the code into the editor
        editor.putString(lastSelectionTag, countryCode);

        //save the code
        editor.apply();

    }

    private void loadLastSelectedCountryCode() {
        //get instance of the shared pref
        SharedPreferences preferences = getContext().getSharedPreferences(CP_PREF_FILE, Context.MODE_PRIVATE);

        //get the last selected country code
        String lastSelectedCode = preferences.getString(lastSelectionTag, "ng");

        //set the country
        setSelectedCountry(getCountryForName(languageToApply, lastSelectedCode));
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, getSelectedCountryCode());

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        String countryCode = savedState.getCountryCode();
        if (countryCode != null) {
            setSelectedCountry(getCountryForName(languageToApply, countryCode));
        }
    }

    //convenient class to save and restore the view state

    protected static class SavedState extends BaseSavedState {

        private String countryCode;

        private SavedState(Parcelable source, String countryCode) {
            super(source);
            this.countryCode = countryCode;
        }

        private SavedState(Parcel source) {
            super(source);
        }

        private String getCountryCode() {
            return countryCode;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.countryCode);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };

    }
}
