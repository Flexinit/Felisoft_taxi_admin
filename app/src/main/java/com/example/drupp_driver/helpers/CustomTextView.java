package com.example.drupp_driver.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.CommonUtils;

public class CustomTextView extends AppCompatTextView {

    private Context mContext;

    public CustomTextView(Context context) {
        super(context);
        mContext = context;
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String textToColor = typedArray.getString(R.styleable.CustomTextView_subText);


        int color = typedArray.getColor(R.styleable.CustomTextView_colorToApply, Color.BLACK);
        String tf = typedArray.getString(R.styleable.CustomTextView_styleToApply);
        if (tf == null) {
            switch (getTypeface().getStyle()) {
                case Typeface.NORMAL:
                    tf = "normal";
                    break;
                case Typeface.BOLD:
                    tf = "bold";
                    break;
                case Typeface.ITALIC:
                    tf = "italic";
                    break;
            }
        }
        int newTf = Typeface.NORMAL;
        if (tf.equalsIgnoreCase("BOLD")) {
            newTf = Typeface.BOLD;
        } else if (tf.equalsIgnoreCase("ITALIC")) {
            newTf = Typeface.ITALIC;
        }
        float ts = typedArray.getFloat(R.styleable.CustomTextView_textSizeToApply, getTextSize());
        boolean applyToAll = typedArray.getBoolean(R.styleable.CustomTextView_applyToAll, false);

        setCustomTextStyle(textToColor, color, newTf, ts, applyToAll);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setCustomTextStyle(String textToStyle, int color, int typeFace, float textSize, boolean applyToAll) {
        String fullText = getText().toString().trim();
        String fullTextLower = fullText.toLowerCase();
        if (textToStyle == null) {
            textToStyle = "";
        }
        textToStyle = textToStyle.trim();
        try {
            if (fullTextLower.contains(textToStyle.toLowerCase()) && !applyToAll) {
                int i = fullTextLower.indexOf(textToStyle.toLowerCase());
                SpannableString abc = new SpannableString(fullText);
                abc.setSpan(new ForegroundColorSpan(color), i, i + textToStyle.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                abc.setSpan(new AbsoluteSizeSpan((int) textSize, true), i, i + textToStyle.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                abc.setSpan(new StyleSpan(typeFace), i, i + textToStyle.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                setText(abc);
            } else if (applyToAll) {
                setTextColor(color);
                setTypeface(getTypeface(), typeFace);
                setTextSize(textSize);
            }
        } catch (Exception e) {
            return;
        }

    }

    public void setCustomTextStyle(int color) {
        setCustomTextStyle(getText().toString(), color, 0, 0, true);
    }


    public class CustomTypefaceSpan extends TypefaceSpan {

        private final Typeface newType;

        public CustomTypefaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }

        private void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }
    }


}

