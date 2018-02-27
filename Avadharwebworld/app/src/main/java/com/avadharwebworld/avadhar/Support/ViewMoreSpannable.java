package com.avadharwebworld.avadhar.Support;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by user-03 on 30-Nov-16.
 */

public class ViewMoreSpannable extends ClickableSpan {
    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public ViewMoreSpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#1b76d3"));
    }

    @Override
    public void onClick(View widget) {


    }
}
