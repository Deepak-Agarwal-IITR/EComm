package com.example.ecommerce.utils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class LayoutUtils {

    public static final DecimalFormat df = new DecimalFormat("0.00");

    public static TextView getTextView(Context context, String text, int width, int textSize, int gravity){
        TextView t = new TextView(context);
        t.setText(text);
        t.setWidth(width);
        t.setTextSize(textSize);
        t.setGravity(gravity);

        return t;
    }
    public static TextView getTextView(Context context, String text, int textSize, int gravity){
        TextView t = new TextView(context);
        t.setText(text);
        t.setTextSize(textSize);
        t.setGravity(gravity);

        return t;
    }

    public static LinearLayout.LayoutParams getLayoutParams(int width, int height, int ml, int mt, int mr, int mb){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        layoutParams.setMargins(ml,mt,mr,mb);

        return layoutParams;
    }
    public static LinearLayout.LayoutParams getLayoutParams(int width,int height,float weight,int ml, int mt, int mr,int mb){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height,weight);
        layoutParams.setMargins(ml,mt,mr,mb);

        return layoutParams;
    }

    public static LinearLayout getLinearLayout(Context context, int orientation, LinearLayout.LayoutParams layoutParams){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientation);
        linearLayout.setLayoutParams(layoutParams);

        return linearLayout;
    }
}
