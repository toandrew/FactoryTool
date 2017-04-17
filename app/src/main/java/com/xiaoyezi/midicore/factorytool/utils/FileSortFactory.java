package com.xiaoyezi.midicore.factorytool.utils;

import com.xiaoyezi.midicore.factorytool.data.FileModel;

import java.util.Comparator;

public class FileSortFactory {
    public static final int SORT_BY_TIME = 0;

    public static Comparator<FileModel> getFileQueryMethod(
            int method) {
        switch (method) {
            case SORT_BY_TIME:
                return new SortByTime();
            default:
                break;
        }
        return null;

    }
}
