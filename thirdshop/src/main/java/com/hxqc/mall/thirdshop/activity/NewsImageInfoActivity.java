package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.fragment.NewsImagesViewFragment;


/**
 * 各种图片资讯大图
 */
public class NewsImageInfoActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_news_image_info);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.thread_info_photo_content_layout, new NewsImagesViewFragment())
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out).commit();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
