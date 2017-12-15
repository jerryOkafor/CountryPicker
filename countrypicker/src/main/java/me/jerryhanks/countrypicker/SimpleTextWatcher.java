package me.jerryhanks.countrypicker;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author Jerry Hanks on 12/15/17.
 */

public class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //do nothing

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //do nothing

    }

    @Override
    public void afterTextChanged(Editable s) {
        //do nothing

    }
}
