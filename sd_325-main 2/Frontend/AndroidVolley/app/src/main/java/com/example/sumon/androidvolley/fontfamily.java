package com.example.sumon.androidvolley;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class fontfamily extends android.support.v7.widget.AppCompatTextView {
    public fontfamily(Context context) {
        super(context);
    }
    public fontfamily(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public fontfamily(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/FontName.ttf");
        setTypeface(tf);
    }
}
