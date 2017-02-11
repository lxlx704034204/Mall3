package com.hxqc.mall.activity.me.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.fragment.me.OrderMessageFragment;
import com.hxqc.mall.fragment.me.PromotionMessageFragment;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-01-22
 * FIXME
 * Todo 消息列表
 */
public class MessageListActivity extends BackActivity {
    public static final String MESSAGE_TYPE = "message_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        MessageListTypeEnum messageListTypeEnum = MessageListTypeEnum.valueOf(
                getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(MESSAGE_TYPE, 0));
//                getIntent().getIntExtra(MESSAGE_TYPE, 0));

        ActionBar actionBar = getSupportActionBar();
        String title = null;
        Fragment fragment = null;
        switch (messageListTypeEnum) {
            case order:
                title = getString(R.string.title_activity_message_list);
                fragment = new OrderMessageFragment();
                break;
            case promotion:
                title = getString(R.string.newsitem_promotion);
                fragment = new PromotionMessageFragment();
                break;
        }
        assert actionBar != null;
        actionBar.setTitle(title);
        getSupportFragmentManager().beginTransaction().add(R.id.message_content, fragment).commit();
    }


    public enum MessageListTypeEnum {
        order(0), promotion(1);

        private int value = 0;

        MessageListTypeEnum(int value) {    //    必须是private的，否则编译错误
            this.value = value;
        }

        public static MessageListTypeEnum valueOf(int value) {
            switch (value) {
                case 0:
                    return order;
                case 1:
                    return promotion;
                default:

                    return order;
            }
        }

        public int value() {
            return this.value;
        }
    }
}
