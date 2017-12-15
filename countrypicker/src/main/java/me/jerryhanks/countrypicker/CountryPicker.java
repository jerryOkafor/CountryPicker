package me.jerryhanks.countrypicker;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class CountryPicker extends TextInputEditText {
    private final static int EXTRA_TAPPABLE_AREA = 50;
    private boolean isRTL;
    private boolean searchAllowed = true;
    private boolean dialogKeyboardAutoPopup = true;
    private boolean showFastScroller = true;
    private int fastScrollerBubbleColor = 0;
    private int fastScrollerHandleColor = 0;
    private int fastScrollerBubbleTextAppearance = 0;
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
//            TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CountryPicker, defStyleAttr, 0);
//            try {
//
//            } finally {
//                styledAttributes.recycle();
//            }
        }

        isRTL = isRTLLanguage();
        setInputType(InputType.TYPE_CLASS_NUMBER);

        invalidCountryCode(selectedCountry);
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
        final int y = (int) event.getY();
        Log.d("TAG", "x:" + x + " y " + y);
        Log.d("BOUND TAG", "b X:" + bounds.centerX() + " b y " + bounds.centerY());
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
        //TODO investigate why ViewUtils.isLayoutRtl(this) not working as intended
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

    public void launchCountrySelectionDialog() {
        CountryCodePickerDialog.openPickerDialog(this);
    }

}
