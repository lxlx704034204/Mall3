package com.hxqc.autonews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.hxqc.autonews.model.AutoInfoDetailModel;
import com.hxqc.autonews.model.pojos.AutoImage;
import com.hxqc.autonews.model.pojos.AutoInfoDetail;
import com.hxqc.autonews.presenter.Presenter;
import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.autonews.view.RequestDataWithCacheHandler;
import com.hxqc.autonews.widget.EvaluationBar;
import com.hxqc.autonews.widget.Gallery;
import com.hxqc.autonews.widget.WriteEvaluationPopWindow;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.socialshare.manager.ShareController;
import com.hxqc.socialshare.pojo.ShareContent;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-09-05
 * FIXME
 * Todo 汽车资讯图片详情(图集类型的汽车资讯和图文资讯中的点击图片浏览图片的共用)
 */
public class AutoGalleryActivity extends AppCompatActivity implements RequestDataWithCacheHandler<AutoInfoDetail>, View.OnClickListener, EvaluationBar.OnShareClickListener, EvaluationBar.OnMessageClickListener {
    public static final String INFO_ID = "infoID";
    public static final String INFO_TITLE = "infoTitle";
    public static final String IMAGES = "images";
    public static final String POSITION = "position";
    private Gallery gallery;
    private String infoID = "";
    private Presenter mPresenter;
    private ShareController shareController;
    private ShareContent shareContent;

    private boolean isWebRequestData = true;//传infoID过来请求数据
    private ArrayList<AutoImage> images = new ArrayList<>();
    private int position;
    private EvaluationBar evaluationBar;
    private WriteEvaluationPopWindow popWindow;
    private long commentCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_gallery);
        evaluationBar = (EvaluationBar) findViewById(R.id.evaluation_bar);
        evaluationBar.setOnClickListener(this);
        evaluationBar.setOnShareClickListener(this);
        evaluationBar.setOnMessageClickListener(this);

        gallery = (Gallery) findViewById(R.id.gallery);
        if (shareController == null) {
            shareController = new ShareController(this);
        }
        infoID = getInfoID();
        images = getImages();
        isWebRequestData = images == null;

        mPresenter = new Presenter();
        ArrayList<Gallery.Data> datas = new ArrayList<>();

        gallery.setShareListener(this);
        loadData();
    }

    private void loadData() {
        if (isWebRequestData)
            mPresenter.getAutoInfoDetail(infoID, this, new AutoInfoDetailModel(this));
        else {

            position = getPosition();
            ArrayList<Gallery.Data> galleryData = new ArrayList<>();
            for (AutoImage image : images) {
                Gallery.Data data = image.toGalleryData(this, "");
                galleryData.add(data);
            }
            gallery.bindData(galleryData);
            gallery.toItemAtIndex(position);

        }
    }

    private String getInfoID() {
        if (getIntent() != null) {
            if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
                infoID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(INFO_ID);
            }
        }

        return infoID;
    }


    private ArrayList<AutoImage> getImages() {
        if (getIntent() != null) {
            if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
                images = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(IMAGES);
            }
        }
        return images;
    }

    private int getPosition() {
        if (getIntent() != null) {
            if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
                position = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(POSITION);
            }
        }
        return position;
    }


    private ArrayList<Gallery.Data> toGalleryData(AutoInfoDetail data) {
        if (data.share != null) {
            shareContent = data.share;
        }
        ArrayList<Gallery.Data> galleryDatas = new ArrayList<>();
        ArrayList<AutoImage> images = data.images;
        if (images != null) {
            for (AutoImage image : images) {
                Gallery.Data data1 = gallery.new Data();
                data1.info = image.description;
                data1.picUrl = image.largeURL;
                data1.title = data.title;
                galleryDatas.add(data1);
            }
        }
        return galleryDatas;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn:
                share();
                break;
            case R.id.evaluation_bar:
                UserInfoHelper.getInstance().loginAction(this, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        showEvaluationEditTextWindow();
                    }
                });
                break;
        }
    }

    private void showEvaluationEditTextWindow() {
        if (popWindow == null) {
            popWindow = new WriteEvaluationPopWindow(this, infoID);
        }
        popWindow.showAtLocation(findViewById(R.id.root_layout),
                Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareController != null) {
            shareController.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    /**
     * 分享
     */
    private void share() {
        if (shareContent != null)
            shareController.showSharePopupWindow(shareContent);
    }

    @Override
    public void onDataNull(String message) {
        //获取数据失败
    }

    @Override
    public void onDataResponse(AutoInfoDetail data) {
        bindData(data);
    }

    @Override
    public void onCacheDataBack(AutoInfoDetail detail) {
        bindData(detail);
    }

    @Override
    public void onCacheDataNull() {

    }

    @Override
    public void onShareClick() {
        share();
    }

    private void bindData(AutoInfoDetail data) {
        commentCount = data.commentCount;
        ArrayList<Gallery.Data> galleryDatas = toGalleryData(data);
        gallery.bindData(galleryDatas);
        evaluationBar.setCount((int) data.commentCount);
    }

    @Override
    public void onMessageClick() {
        //去评论页面
//        if (commentCount > 0)
        ActivitySwitchAutoInformation.toMessageCommentList(this, infoID, (int) commentCount);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (popWindow != null && popWindow.isShowing())
            popWindow.dismiss();
        else
            finish();
    }
}
