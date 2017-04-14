package com.xiaoyezi.midicore.factorytool.data;

import android.media.Image;

import java.util.List;

/**
 * Created by jim on 2017/4/14.
 */
public interface FileModel {
    /**
     * File name
     *
     * @return
     */
    String getName();

    /**
     * File path
     *
     * @return
     */
    String getPath();

    /**
     * File size
     *
     * @return
     */
    long getSize();

    /**
     * File created time
     *
     * @return
     */
    String getCreatedTime();

    /**
     * Get file icon
     *
     * @return
     */
    Image getFileIcon();
}
