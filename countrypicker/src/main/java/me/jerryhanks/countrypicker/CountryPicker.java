package me.jerryhanks.countrypicker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class CountryPicker extends TextInputEditText {
    private final static int EXTRA_TAPPABLE_AREA = 5;
    private static final String TAG = CountryPicker.class.getSimpleName();
    private boolean isRTL;
    private boolean searchAllowed = true;
    private boolean dialogKeyboardAutoPopup = true;
    private boolean showFastScroller = true;
    private int fastScrollerBubbleColor = 0;
    private int fastScrollerHandleColor = 0;
    private int fastScrollerBubbleTextAppearance = 0;
    private Country selectedCountry = new Country("Nigeria", "+234", "NG");
    private BitmapDrawable chip;
    private String defaultCountryName;
    private boolean autoDetectCountryEnabled = true;
    private Language languageToApply = Language.ENGLISH;
    private AutoDetectionPref selectedAutoDetectionPref;

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

                //default Country
                defaultCountryName = a.getString(R.styleable.CountryPicker_cp_defaultCountryName);


                //country auto detection pref
                int autoDetectionPrefValue = a.getInt(R.styleable.CountryPicker_cp_countryAutoDetectionPref, 123);
                selectedAutoDetectionPref = AutoDetectionPref.getPrefForValue(String.valueOf(autoDetectionPrefValue));


                //auto detect country
                autoDetectCountryEnabled = a.getBoolean(R.styleable.CountryPicker_ccp_autoDetectCountry, true);

            } finally {
                a.recycle();
            }
        }


        //implement auto Country detection if it is set
        if (isAutoDetectCountryEnabled() && !isInEditMode()) {
            startAutoCountryDetection(true);
        }

        isRTL = isRTLLanguage();
        setInputType(InputType.TYPE_CLASS_NUMBER);

        invalidCountryCode(selectedCountry);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addTextChangedListener(new PhoneNumberFormattingTextWatcher(selectedCountry.getCode()));
        } else {
            addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        }
    }

    private void startAutoCountryDetection(boolean loadDefaultWhenFails) {
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

            if (!successfullyDetected && loadDefaultWhenFails) {
                resetToDefaultCountry();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "setAutoDetectCountry: Exception" + e.getMessage());
            if (loadDefaultWhenFails) {
                resetToDefaultCountry();
            }
        }

    }

    private String getSelectedCountryName() {
        return this.selectedCountry.getName();
    }

    private void invalidCountryCode(Country country) {
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
            drawableId = Country.getFlagResID(country);
        }

        chip = createClusterBitmap(dialCode, code, drawableId);
        setCompoundDrawablesWithIntrinsicBounds(chip, null, null, null);
        setCompoundDrawablePadding(10);

    }


    private BitmapDrawable createClusterBitmap(CharSequence dialCode, CharSequence code, int drawableId) {
        View wrapper = LayoutInflater.from(getContext()).inflate(R.layout.picker_view,
                null);

        TextView tvCode = wrapper.findViewById(R.id.tvShortCode);
        tvCode.setTypeface(getTypeface());
        tvCode.setTextSize(getTextSize());
        tvCode.setTextColor(getTextColors());
        tvCode.setText(getContext().getString(R.string.fmt_code_and_dial_code, code, dialCode));

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
        int iconXRect = isRTL ? getRight() - bounds.width() - EXTRA_TAPPABLE_AREA :
                getLeft() + bounds.width() + EXTRA_TAPPABLE_AREA;

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
        invalidCountryCode(this.selectedCountry);

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
     * @param loadDefaultWhenFails true if want to reset to default country when sim country cannot be detected. if false, then it
     *                             will not change currently selected country
     * @return true if it successfully sets country, false otherwise
     */
    public boolean detectSIMCountry(boolean loadDefaultWhenFails) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String simCountryISO = telephonyManager.getSimCountryIso();
            if (simCountryISO == null || simCountryISO.isEmpty()) {
                if (loadDefaultWhenFails) {
                    resetToDefaultCountry();
                }
                return false;
            }
            setSelectedCountry(getCountryForName(getLanguageToApply(), simCountryISO));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (loadDefaultWhenFails) {
                resetToDefaultCountry();
            }
            return false;
        }
    }

    /**
     * This will detect country from NETWORK info and then load it into CCP.
     *
     * @param loadDefaultWhenFails true if want to reset to default country when network country cannot be detected. if false, then it
     *                             will not change currently selected country
     * @return true if it successfully sets country, false otherwise
     */
    public boolean detectNetworkCountry(boolean loadDefaultWhenFails) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String networkCountryISO = telephonyManager.getNetworkCountryIso();
            if (networkCountryISO == null || networkCountryISO.isEmpty()) {
                if (loadDefaultWhenFails) {
                    resetToDefaultCountry();
                }
                return false;
            }
            setSelectedCountry(getCountryForName(getLanguageToApply(), networkCountryISO));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (loadDefaultWhenFails) {
                resetToDefaultCountry();
            }
            return false;
        }
    }

    /**
     * This will detect country from LOCALE info and then load it into CCP.
     *
     * @param loadDefaultWhenFails true if want to reset to default country when locale country cannot be detected. if false, then it
     *                             will not change currently selected country
     * @return true if it successfully sets country, false otherwise
     */
    public boolean detectLocaleCountry(boolean loadDefaultWhenFails) {
        try {
            String localeCountryISO = getContext().getResources().getConfiguration().locale.getCountry();
            if (localeCountryISO == null || localeCountryISO.isEmpty()) {
                if (loadDefaultWhenFails) {
                    resetToDefaultCountry();
                }
                return false;
            }
            setSelectedCountry(getCountryForName(getLanguageToApply(), localeCountryISO));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (loadDefaultWhenFails) {
                resetToDefaultCountry();
            }
            return false;
        }
    }

    private Country getCountryForName(Language languageToApply, String simCountryISO) {
        List<Country> countries = CountryCodePickerDialog.loadDataFromJson(getContext());
        for (Country country : countries) {
            if (simCountryISO.equals(country.getCode()))
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
        CountryCodePickerDialog.openPickerDialog(this);
    }


}
