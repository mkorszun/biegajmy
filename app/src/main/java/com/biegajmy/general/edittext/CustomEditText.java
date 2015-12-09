package com.biegajmy.general.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText {

    private static final String FONTS_ISTOK_REGULAR_TTF = "fonts/Lato-Regular.ttf";

    public CustomEditText(Context context) {
        super(context);
        setTypeFace(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace(context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeFace(context);
    }

    protected void setTypeFace(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), FONTS_ISTOK_REGULAR_TTF));
    }
}
