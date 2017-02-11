package com.hxqc.mall.usedcar.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.ImageAdapter;
import com.hxqc.mall.usedcar.model.Image;

import java.util.ArrayList;

/**
 * 车辆详情页图集
 * Created by huangyi on 15/10/23.
 */
public class ImageActivity extends BackActivity implements AdapterView.OnItemClickListener {

    public static final String IMAGE = "image";

    ArrayList<Image> mImage;
    ListView mListView;
    ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mImage = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(IMAGE);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.toolbar_background)));
        getSupportActionBar().setTitle(mImage.size() + "张图片");
        mListView = (ListView) findViewById(R.id.image_list);
        mAdapter = new ImageAdapter(this, mImage);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int location[] = new int[2];
        mListView.getChildAt(position - mListView.getFirstVisiblePosition()).getLocationOnScreen(location);
        Bundle bundle = new Bundle();
        bundle.putInt("locationX", location[0]);
        bundle.putInt("locationY", location[1]);
        bundle.putInt("width", mListView.getChildAt(position - mListView.getFirstVisiblePosition()).getWidth());
        bundle.putInt("height", mListView.getChildAt(position - mListView.getFirstVisiblePosition()).getHeight());

        ArrayList<ImageModel> temp = new ArrayList<>();
        for (Image i : mImage) {
            temp.add(new ImageModel(i.path, i.small_path));
        }

        ActivitySwitchBase.toViewLagerPic(position, temp, this, bundle);
    }

}
