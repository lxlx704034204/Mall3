package com.hxqc.xiaoneng;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Author :liukechong
 * Date : 2016-01-21
 * FIXME
 * Todo
 */
public class ChatNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ChatManager.getInstance().startChatWithNothing();
    }
}
