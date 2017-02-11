package com.hxqc.xiaoneng;

/**
 * Author :liukechong
 * Date : 2016-01-20
 * FIXME
 * Todo
 */
public interface UnReadMessageListener {
    void unReadMessage(String unMessage, long time, int count, boolean isRead);
}
