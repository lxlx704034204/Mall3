package com.hxqc.mall.thirdshop.accessory.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.adapter.PhotoAdapter;

import java.util.ArrayList;

/**
 * 相册
 * Created by huangyi on 16/2/29.
 */
public class PhotoActivity extends AccessoryBackActivity {

    GridView mGridView;
    ArrayList<ImageModel> mImageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mGridView = (GridView) findViewById(R.id.photo_gv);
        mImageModel = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(ActivitySwitchBase.viewLargePics);

        getSupportActionBar().setTitle(mImageModel.size() + " 张图片");

        mGridView.setAdapter(new PhotoAdapter(this, mImageModel));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int location[] = new int[2];
                mGridView.getChildAt(position - mGridView.getFirstVisiblePosition()).getLocationOnScreen(location);
                Bundle bundle = new Bundle();
                bundle.putInt("locationX", location[0]);
                bundle.putInt("locationY", location[1]);
                bundle.putInt("width", mGridView.getChildAt(position - mGridView.getFirstVisiblePosition()).getWidth());
                bundle.putInt("height", mGridView.getChildAt(position - mGridView.getFirstVisiblePosition()).getHeight());
                ActivitySwitchBase.toViewLagerPic(position, mImageModel, PhotoActivity.this, bundle);
            }
        });
    }

}
