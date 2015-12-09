package com.biegajmy.general.button;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {

    private static final String FONT = "fonts/Lato-Regular.ttf";

    public CustomButton(Context context) {
        super(context);
        setTypeFace(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace(context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace(context);
    }

    protected void setTypeFace(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), FONT));
    }
}
