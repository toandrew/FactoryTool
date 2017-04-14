package com.xiaoyezi.midicore.factorytool.utils;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.ConsolePrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy;
import com.elvishew.xlog.printer.file.naming.FileNameGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by jim on 2017/4/13.
 */

public class Tlog {
    private static final String LOG_PATH = "/sdcard/TheOne/";

    private static String sLogFileName = "";

    /**
     * Init log
     */
    public static void initLog() {
        LogConfiguration config = new LogConfiguration.Builder()
                .tag("MIDI_LOG")                                         // Specify TAG, default: "X-LOG"
                .build();

        MyDateFileNameGenerator fileNameneGenerator = new MyDateFileNameGenerator();

        Printer androidPrinter = new AndroidPrinter();             // Printer that print the log using android.util.Log
        Printer consolePrinter = new ConsolePrinter();
        Printer filePrinter = new FilePrinter                      // Printer that print the log to the file system
                .Builder(LOG_PATH)                              // Specify the path to save log file
                .fileNameGenerator(fileNameneGenerator)        // Default: ChangelessFileNameGenerator("log")
                .backupStrategy(new NeverBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
                .build();

        XLog.init(                                                 // Initialize XLog
                config,                                                // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
                androidPrinter,                                        // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
                consolePrinter,
                filePrinter);
    }

    public static String getLogName() {
        return LOG_PATH + sLogFileName;
    }

    /**
     * Debug
     *
     * @param msg
     */
    public static final void d(String msg) {
        XLog.d(msg);
    }

    /**
     * Warning message
     *
     * @param msg
     */
    public static final void w(String msg) {
        XLog.w(msg);
    }

    /**
     * Error message
     * @param msg
     */
    public static final void e(String msg) {
        XLog.w(msg);
    }

    private static class MyDateFileNameGenerator implements FileNameGenerator {
        ThreadLocal<SimpleDateFormat> mLocalDateFormat = new ThreadLocal<SimpleDateFormat>() {

            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            }
        };

        @Override
        public boolean isFileNameChangeable() {
            return false;
        }

        /**
         * Generate a file name which represent a specific date.
         */
        @Override
        public String generateFileName(int logLevel, long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getDefault());

            sLogFileName = sdf.format(new Date(timestamp)) + ".log";

            return sLogFileName;
        }
    }
}
