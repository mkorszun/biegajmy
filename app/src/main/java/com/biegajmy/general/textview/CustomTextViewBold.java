package com.biegajmy.general.textview;

import android.content.Context;
import android.util.AttributeSet;

public class CustomTextViewBold extends CustomTextView {

    private static final String FONT = "";

    public CustomTextViewBold(Context context) {
        super(context);
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void setTypeFace(Context context) {
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(), FONT));
    }
}
