package com.xiaoyezi.midicore.factorytool.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class AlertUtil {
    /**
     *
     *
     * @param context
     *
     * @param title
     *
     * @param message
     *
     * @param okListener
     *
     * @param cancleListener
     *
     * @return
     */
    public static AlertDialog judgeAlertDialog(Context context, String title,
                                               String message, DialogInterface.OnClickListener okListener,
                                               DialogInterface.OnClickListener cancleListener) {
        AlertDialog  aDialog= new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setNegativeButton("确定", okListener)
                .setPositiveButton("取消", cancleListener).show();
        return aDialog;
    }
}
