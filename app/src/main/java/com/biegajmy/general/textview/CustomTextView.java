package com.biegajmy.general.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

    private static final String FONTS_ISTOK_REGULAR_TTF = "fonts/OpenSans-CondLight.ttf";

    public CustomTextView(Context context) {
        super(context);
        setTypeFace(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace(context);
    }

    protected void setTypeFace(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), FONTS_ISTOK_REGULAR_TTF));
    }
}
