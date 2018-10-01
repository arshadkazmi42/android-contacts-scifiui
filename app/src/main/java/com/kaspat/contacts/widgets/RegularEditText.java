package com.kaspat.contacts.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.kaspat.contacts.utils.Constants;

/**
 * Created by Arshad on 03-06-2016.
 */
public class RegularEditText extends android.support.v7.widget.AppCompatEditText {
    /*
     * Caches typefaces based on their file path and name,
     * so that they don't have to be created every time when they are referenced.
     */
    private static Typeface mTypeface;

    public RegularEditText(Context context) {
        super(context);
        init(context);
    }

    public RegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void init(Context context) {
        try {
            if (mTypeface == null) {
                mTypeface = Typeface.createFromAsset(context.getAssets(), Constants.FONT_REGULAR);
            }
            setTypeface(mTypeface);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
