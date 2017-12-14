package me.jerryhanks.countrypicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * @author Jerry Hanks on 12/14/17.
 */

public class CountryPicker extends TextInputEditText {
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


        Drawable chip = createClusterBitmap("+234");
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

}
