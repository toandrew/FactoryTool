package com.xiaoyezi.midicore.factorytool.data;

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
     * Get lastModified time
     *
     * @return
     */
    long getLastModified();
}
