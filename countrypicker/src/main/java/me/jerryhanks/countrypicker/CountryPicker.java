package me.jerryhanks.countrypicker;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class CountryPicker extends TextInputEditText {
    private final static int EXTRA_TAPPABLE_AREA = 50;
    private BitmapDrawable chip;
    private boolean isRTL;
    private boolean searchAllowed = true;
    private boolean dialogKeyboardAutoPopup = true;

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

        chip = createClusterBitmap("+234");
        setCompoundDrawablesWithIntrinsicBounds(chip, null, null, null);
        setCompoundDrawablePadding(10);
    }


    private BitmapDrawable createClusterBitmap(CharSequence code) {
        View wrapper = LayoutInflater.from(getContext()).inflate(R.layout.picker_view,
                null);

        TextView clusterSizeText = wrapper.findViewById(R.id.tvShortCode);
        clusterSizeText.setText(code);

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
        int iconXRect = isRTL ? getLeft() + bounds.width() + EXTRA_TAPPABLE_AREA :
                getRight() - bounds.width() - EXTRA_TAPPABLE_AREA;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(getContext(), "CLicked:DOWN", Toast.LENGTH_LONG).show();
                launchCountrySelectionDialog();
                break;
//            case MotionEvent.ACTION_UP:
//                Toast.makeText(getContext(), "CLicked:UP", Toast.LENGTH_LONG).show();
//                launchCountrySelectionDialog();
//                break;

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
