package com.hxqc.mall.activity.me.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.NewestMessage;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.views.MarkableImageView;
import com.hxqc.mall.views.MyMessageItem;
import com.hxqc.util.DateUtil;
import com.hxqc.util.JSONUtils;
import com.hxqc.xiaoneng.ChatManager;
import com.hxqc.xiaoneng.UnReadMessageListener;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-01-21
 * FIXME
 * Todo 我的消息界面
 */
public class MyMessageActivity extends BackActivity implements View.OnClickListener {
    private MyMessageItem serviceItem, orderItem, activityItem;
    private ApiClient apiClient;
    private ArrayList<NewestMessage> newestMessages;
    private RequestFailView requestFailView;
    private UnReadMessageListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);

        serviceItem = (MyMessageItem) findViewById(R.id.service_item);
        orderItem = (MyMessageItem) findViewById(R.id.order_item);
        activityItem = (MyMessageItem) findViewById(R.id.activity_item);

        requestFailView = (RequestFailView) findViewById(R.id.request_view);
        requestFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivitySwitcher.toMain(MyMessageActivity.this, 0);
            }
        });
        requestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });
        apiClient = new ApiClient();
        serviceItem.setOnClickListener(this);
        orderItem.setOnClickListener(this);
        activityItem.setOnClickListener(this);

        listener = new UnReadMessageListener() {
            @Override
            public void unReadMessage(String unmessage, long time, int count, boolean isRead) {
                String millon = DateUtil.getDay(time);
                serviceItem.infData(unmessage, millon, isRead ?
                        MarkableImageView.IconState.READ : MarkableImageView.IconState.UNREAD);
            }
        };

        addUnReadMessageListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = this.getSharedPreferences("HxqcChatMessage", Context.MODE_PRIVATE);

        int count = sharedPreferences.getInt("count", 0);
        long time = sharedPreferences.getLong("time", 0);
        boolean isRead = sharedPreferences.getBoolean("is_read", false);
        String unmessage = sharedPreferences.getString("unmessage", null);

        if (!TextUtils.isEmpty(unmessage) && 0 != time) {
            String millon = DateUtil.getDay(time);
            serviceItem.infData(unmessage, millon, isRead ?
                    MarkableImageView.IconState.READ : MarkableImageView.IconState.UNREAD);
        }
        if (UserInfoHelper.getInstance().isLogin(this)) {
            requestData();
        }
    }

    private void requestData() {
        apiClient.newest(
                new LoadingAnimResponseHandler(this) {
                    @Override
                    public void onSuccess(String response) {
                        requestFailView.setVisibility(View.GONE);
                        newestMessages = new ArrayList<>();
                        newestMessages = JSONUtils.fromJson(response, new TypeToken<ArrayList<NewestMessage>>() {
                        });
                        if (newestMessages.size() == 0) {
//                            requestFailView.setVisibility(View.VISIBLE);
//                            requestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                        }
                        infData(newestMessages);
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                        requestFailView.setVisibility(View.VISIBLE);
//                        requestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });


    }

    private void addUnReadMessageListener() {
        ChatManager.getInstance().addUnReadMessageListener(listener);
    }

    private void infData(ArrayList<NewestMessage> newestMessages) {
        for (int i = 0; i < newestMessages.size(); i++) {
            NewestMessage newestMessage = newestMessages.get(i);
            if (newestMessage.messageType == 0) {
                activityItem.infData(newestMessage.title, newestMessage.date, newestMessage.getMessageState());
            } else if (newestMessage.messageType == 1) {
                orderItem.infData(newestMessage.title, newestMessage.date, newestMessage.getMessageState());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listener != null)
            ChatManager.getInstance().removeUnReadMessageListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.service_item:
                ChatManager.getInstance().startHistoryGoods("消息", "null");
                ChatManager.getInstance().markRead();
                break;
            case R.id.order_item:
                UserInfoHelper.getInstance().loginAction(this, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitcher.toMessage(MyMessageActivity.this, MessageListActivity.MessageListTypeEnum.order);
                    }
                });
//                if (UserInfoHelper.getInstance().isLogin(this)) {
//                    ActivitySwitcher.toMessage(MyMessageActivity.this, MessageListActivity.MessageListTypeEnum.order);
//                } else {
//                    ActivitySwitcher.toLogin(MyMessageActivity.this);
//                }

                break;
            case R.id.activity_item:
                UserInfoHelper.getInstance().loginAction(this, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitcher.toMessage(MyMessageActivity.this, MessageListActivity.MessageListTypeEnum
                                .promotion);
                    }
                });
//                if (UserInfoHelper.getInstance().isLogin(this)) {
//                    ActivitySwitcher.toMessage(MyMessageActivity.this, MessageListActivity.MessageListTypeEnum
//                            .promotion);
//                } else {
//                    ActivitySwitchAuthenticate.toLogin(MyMessageActivity.this);
//                }
                break;
        }
    }
}
