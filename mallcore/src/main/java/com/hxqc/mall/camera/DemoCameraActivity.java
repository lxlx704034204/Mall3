package com.hxqc.mall.camera;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ImageUtil;

/**
 * 说明:证件照demo
 *
 * @author: 吕飞
 * @since: 2016-10-31
 * Copyright:恒信汽车电子商务有限公司
 */

public class DemoCameraActivity extends GetIDPhotoActivity implements View.OnClickListener {
    TextView mAddDriver1View, mAddDriver2View, mAddDriving1View, mAddDriving2View, mAddId1View, mAddId2View;
    ImageView mDriverLicenceView, mDrivingLicenceView, mIdCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_camera);
        mAddDriver1View = (TextView) findViewById(R.id.add_driver1);
        mAddDriver2View = (TextView) findViewById(R.id.add_driver2);
        mAddDriving1View = (TextView) findViewById(R.id.add_driving1);
        mAddDriving2View = (TextView) findViewById(R.id.add_driving2);
        mAddId1View = (TextView) findViewById(R.id.add_id1);
        mAddId2View = (TextView) findViewById(R.id.add_id2);
        mDriverLicenceView = (ImageView) findViewById(R.id.driver_licence);
        mDrivingLicenceView = (ImageView) findViewById(R.id.driving_licence);
        mIdCardView = (ImageView) findViewById(R.id.id_card);
        mAddDriver1View.setOnClickListener(this);
        mAddDriver2View.setOnClickListener(this);
        mAddDriving1View.setOnClickListener(this);
        mAddDriving2View.setOnClickListener(this);
        mAddId1View.setOnClickListener(this);
        mAddId2View.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.add_driver1) {
            toTakePhoto(CAMERA_DRIVER);
        } else if (i == R.id.add_driver2) {
            getPicFromContent(GALLERY_DRIVER);
        } else if (i == R.id.add_driving1) {
            toTakePhoto(CAMERA_DRIVING);
        } else if (i == R.id.add_driving2) {
            getPicFromContent(GALLERY_DRIVING);
        } else if (i == R.id.add_id1) {
            toTakePhoto(CAMERA_ID);
        } else if (i == R.id.add_id2) {
            getPicFromContent(GALLERY_ID);
        }
    }

    protected void chooseSuccess(String filePath, int requestCode) {
        String mPhoto = "file://" + filePath;
        switch (requestCode) {
            case CAMERA_DRIVER:
            case GALLERY_DRIVER:
            case CROP_DRIVER:
                ImageUtil.setImage(this, mDriverLicenceView, mPhoto);
                break;
            case CAMERA_DRIVING:
            case GALLERY_DRIVING:
            case CROP_DRIVING:
                ImageUtil.setImage(this, mDrivingLicenceView, mPhoto);
                break;
            case CAMERA_ID:
            case GALLERY_ID:
            case CROP_ID:
                ImageUtil.setImage(this, mIdCardView, mPhoto);
                break;
        }
    }
}
