package com.thanksbingo.bingo.Entities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Hagyut on 2015. 2. 25..
 */
public class FoodImageView extends ImageView {

    public FoodImageView(Context context) {
        super(context);
    }

    public FoodImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFivId(int _val) {
        fiv_id = _val;
    }

    public int getFivId() {
        return fiv_id;
    }

    private int fiv_id;
}
