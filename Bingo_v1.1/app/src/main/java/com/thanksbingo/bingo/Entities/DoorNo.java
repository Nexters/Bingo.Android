package com.thanksbingo.bingo.Entities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.thanksbingo.CONST_STRINGS;

/**
 * Created by yoonsun on 2015. 2. 22..
 */
public class DoorNo {

    SharedPreferences door_no;

    static Context mContext;

    public DoorNo(Context c){
        mContext = c;
    }

    public static boolean checkYo(SharedPreferences pref) {
        if (!pref.contains(CONST_STRINGS.FREEZER_DOOR_ROW_CNT)
            || !pref.contains(CONST_STRINGS.FREEZER_IN_ROW_CNT)
            || !pref.contains(CONST_STRINGS.FRIDGE_DOOR_ROW_CNT)
            || !pref.contains(CONST_STRINGS.FRIDGE_IN_ROW_CNT)) {

            return false;
        }
        else {
            return true;
        }
    }

    public void put(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public int getValue(String key, int dftValue) {
        try {
            SharedPreferences pref = mContext.getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Activity.MODE_PRIVATE);
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }
}
