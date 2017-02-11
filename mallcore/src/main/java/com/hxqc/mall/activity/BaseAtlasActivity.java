package com.hxqc.mall.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.adapter.AtlasGridAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailViewUtil;
import com.hxqc.mall.core.model.AtlasModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * Function:
 * 图集
 *
 * @author 袁秉勇
 * @since 2015年12月12日
 */
public abstract class BaseAtlasActivity extends BackActivity {
    final int GET_ATLAS = 0;
    TextView mNoDataNotice;
    GridView mAtlas;
    AtlasGridAdapter adapter;
    protected boolean isExtID;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == GET_ATLAS) {
                DebugLog.i("atlas", (String) msg.obj);
                ArrayList< AtlasModel > atlasModels = JSONUtils.fromJson((String) msg.obj, new TypeToken< ArrayList<
                        AtlasModel > >() {
                });
                if (atlasModels == null || atlasModels.size() == 0) {
                    mAtlas.setVisibility(View.INVISIBLE);
                    mNoDataNotice.setVisibility(View.VISIBLE);
//                    DebugLog.i("comments", "--++  " + atlasModels.size());di
                    setContentView(new RequestFailViewUtil().getEmptyView(BaseAtlasActivity.this, "暂无图集"));
                } else {
                    getSupportActionBar().setTitle(atlasModels.size() + " 张图片");
                    mAtlas.setVisibility(View.VISIBLE);
                    mNoDataNotice.setVisibility(View.INVISIBLE);
                    adapter = new AtlasGridAdapter(atlasModels, BaseAtlasActivity.this);
                    mAtlas.setAdapter(adapter);
                    mAtlas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {

                            int location[] = new int[2];
                            view.getLocationOnScreen(location);
                            Bundle bundle = new Bundle();
                            bundle.putInt("locationX", location[0]);
                            bundle.putInt("locationY", location[1]);
                            bundle.putInt("width", view.getWidth());
                            bundle.putInt("height", view.getHeight());
                            ActivitySwitchBase.toViewLagerPic(position, adapter.getImageModels(), BaseAtlasActivity
                                    .this, bundle);
                        }


                    });
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atlas);
        //手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.toast_yellow))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);

        mNoDataNotice = (TextView) findViewById(R.id.tv_atlas_no_data);
        mAtlas = (GridView) findViewById(R.id.gv_atlas);
        mAtlas.setVerticalScrollBarEnabled(false);
        isExtID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getBoolean("isExtID", false);
        String itemID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString("item_id");

        if (!TextUtils.isEmpty(itemID)) {
//            newAutoClient.autoItemAtlas(getIntent().getStringExtra("item_id"),
//            });

            getData(itemID, getLoadingAnimResponseHandler());
        }
    }

    protected abstract void getData(String itemID, LoadingAnimResponseHandler loadingAnimResponseHandler);


    protected LoadingAnimResponseHandler getLoadingAnimResponseHandler() {
        return new LoadingAnimResponseHandler(BaseAtlasActivity.this) {
            @Override
            public void onSuccess(String response) {
                Message msg = Message.obtain();
                msg.what = GET_ATLAS;
                msg.obj = response;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                    , "未获取到图片数据"
                setContentView(new RequestFailViewUtil().getFailView(BaseAtlasActivity.this));
            }
        };
    }
}
