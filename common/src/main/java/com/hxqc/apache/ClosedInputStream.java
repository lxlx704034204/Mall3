package com.hxqc.apache;

import java.io.InputStream;

/**
 * Author: wanghao
 * Date: 2015-11-04
 * FIXME
 * Todo
 */
public class ClosedInputStream extends InputStream {
    /**
     * A singleton.
     */
    public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();

    /**
     * Returns -1 to indicate that the stream is closed.
     *
     * @return always -1
     */
    @Override
    public int read() {
        return -1;
    }
}
