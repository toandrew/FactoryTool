package com.xiaoyezi.midicore.factorytool.utils;

import java.util.Date;

/**
 * Created by jim on 2017/4/13.
 */
public class Utils {
    public static long getRandom(int start, int end) {
        int w = end - start;
        return Math.round(Math.random() * w + start);
    }

    public static String getTime(long lastModified) {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date(lastModified));
    }
}
