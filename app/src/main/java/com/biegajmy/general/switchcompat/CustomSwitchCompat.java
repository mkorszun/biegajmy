package com.biegajmy.general.switchcompat;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

public class CustomSwitchCompat extends SwitchCompat {

    private static final String FONT = "fonts/Lato-Regular.ttf";

    public CustomSwitchCompat(Context context) {
        super(context);
        setTypeFace(context);
    }

    public CustomSwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace(context);
    }

    public CustomSwitchCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace(context);
    }

    protected void setTypeFace(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), FONT));
    }
}
