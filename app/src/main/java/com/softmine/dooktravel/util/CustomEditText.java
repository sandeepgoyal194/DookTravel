package com.softmine.dooktravel.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.softmine.dooktravel.R;

/**
 * Created by GAURAV on 9/12/2017.
 */

public class CustomEditText extends EditText{
    public String delimiter;
    public boolean fancyText;
    private Context context;
    public CustomEditText(Context context) {
        super(context);
        this.context=context;
        setTypeFace();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Typeface );
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.Typeface_typeface:
                    delimiter = a.getString(attr);
                    setTypeFace();
                    break;

                case R.styleable.Typeface_fancyText:
                    fancyText = a.getBoolean(attr, false);
                    // fancyText();
                    break;
            }
        }
        a.recycle();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;

        setTypeFace();
    }

    private void setTypeFace() {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "font/"+delimiter));
    }

   /* private void fancyText() {
        if( this.fancyText){
            setShadowLayer(9, 1, 1, Color.rgb(44, 44, 40));
        }
    }*/
}

