package com.hxqc.fastreqair.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.fastreqair.adapter.WashCarShopAdapter;
import com.hxqc.fastreqair.adapter.WashCarShopPhotoAdapter;
import com.hxqc.fastreqair.api.CarWashApiClient;
import com.hxqc.fastreqair.model.CarWashComment;
import com.hxqc.fastreqair.model.CarWashShopDetail;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.views.MonitorScrollView;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import hxqc.mall.R;

/**
 * @Author : 钟学东
 * @Since : 2016-05-13
 * FIXME
 * Todo 洗车店铺
 */
public class WashCarShopActivity extends NoBackActivity implements MonitorScrollView.onImageZoomListener, MonitorScrollView.ScrollViewListener {

    private Context context;
    private Toolbar mToolbar;
    private MonitorScrollView monitorScrollView;
    private ImageView mShopPhotoView;
    private TextView mShopNameView;
    private TextView mShopHoursView;
    private TextView mShopAddressView;
    private ListViewNoSlide listViewNoSlide;
    private ImageView mShopCallView;
    private MyRatingBarView myRatingBarView;
    private RelativeLayout mHouresView;
    private RelativeLayout mAddressView;

    private CircleImageView circleImageView;
    private TextView UsernameCommentView;
    private MyRatingBarView commentStarView;
    private TagFlowLayout mTagFlowView;
    private TextView mCommentView;
    private RecyclerView recyclerView;
    private TextView commentTimeView;
    private RelativeLayout mLookCommentView;
    private WashCarShopPhotoAdapter washCarShopPhotoAdapter;

    private LinearLayout mLlCommentView;
    private TextView mTextScoreView;

    private DisplayMetrics metric;

    private WashCarShopAdapter washCarShopAdapter;

    private CarWashApiClient carWashApiClient;
    private CarWashShopDetail carWashShopDetails;
    private ArrayList< CarWashComment > carWashComments;
    private String shopID;
    private ArrayList<ImageModel> imageModels = new ArrayList<>(); //用于显示大图的list

    protected static int count = 15;
    protected static int firstPage = 1;
    private CarWashShopDetail carWashShopListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_car_shop);
        context = this;
        // 获取屏幕宽高
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        carWashApiClient = new CarWashApiClient();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ActivitySwitchBase.KEY_DATA);
        shopID = bundle.getString("shopID");
        initView();
        getDate();
        initEvent();
    }

    private void getDate() {

        carWashApiClient.ShopDetail(shopID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                carWashShopDetails = JSONUtils.fromJson(response,new TypeToken<CarWashShopDetail>(){});
                if(carWashShopDetails != null){
                    initDate();
                }
            }
        });


        carWashApiClient.ShopCommentList(firstPage, count, shopID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                carWashComments = JSONUtils.fromJson(response, new TypeToken< ArrayList< CarWashComment > >() {
                });
                if (carWashComments == null || carWashComments.size() == 0) {
                    mLlCommentView.setVisibility(View.GONE);
                } else {
                    mLlCommentView.setVisibility(View.VISIBLE);
                    CarWashComment carWashComment = carWashComments.get(0);
                    if (carWashComment.userAvatar != null) {
                        ImageUtil.setImage(context, circleImageView, carWashComment.userAvatar);
                    }
                    UsernameCommentView.setText(carWashComment.userNickname);

                    if(carWashComment.images == null || carWashComment.images.size() == 0){
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        recyclerView.setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                        layoutParams.height = (int) ((metric.widthPixels - DisplayTools.dip2px(context,5*16))/3.5);
                        recyclerView.setLayoutParams(layoutParams);

                        washCarShopPhotoAdapter = new WashCarShopPhotoAdapter(context,carWashComment.images);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(washCarShopPhotoAdapter);
                    }

                    if (carWashComment.content == null || carWashComment.content.equals("")) {
                        mCommentView.setVisibility(View.GONE);
                    } else {
                        mCommentView.setText(carWashComment.content);
                    }
                    if (carWashComment.tags == null || carWashComment.tags.size() == 0) {
                        mTagFlowView.setVisibility(View.GONE);
                    } else {
                        mTagFlowView.setEnabled(false);
                        mTagFlowView.setAdapter(new TagAdapter< String >(carWashComment.tags) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                View view = LayoutInflater.from(context).inflate(R.layout.layout_carwash_flowlayout_view, parent, false);
                                TextView textView = (TextView) view.findViewById(R.id.carwash_flowlayout_textview);
                                textView.setText(s);
                                return view;
                            }
                        });
                    }
                    commentStarView.setStar(carWashComment.score);
                    commentTimeView.setText(carWashComment.createTime);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mLlCommentView.setVisibility(View.GONE);
            }
        });
    }


    private void initDate() {
        carWashShopListBean = carWashShopDetails;

        mShopNameView.setText(carWashShopListBean.shopTitle);
        if (TextUtils.isEmpty(carWashShopListBean.shopHours)) {
            mHouresView.setVisibility(View.GONE);
        } else {
            mHouresView.setVisibility(View.VISIBLE);
            mShopHoursView.setText(carWashShopListBean.shopHours);
        }
        if (TextUtils.isEmpty(carWashShopListBean.address)) {
            mAddressView.setVisibility(View.GONE);
        } else {
            mAddressView.setVisibility(View.VISIBLE);
            mShopAddressView.setText(carWashShopListBean.address);
        }
        if (!TextUtils.isEmpty(carWashShopListBean.shopTel)) {
            mShopCallView.setVisibility(View.VISIBLE);
        }
        mTextScoreView.setText(carWashShopListBean.score +"");
        mToolbar.setTitle(carWashShopListBean.shopName);
        washCarShopAdapter = new WashCarShopAdapter(this, carWashShopListBean.charge);
        listViewNoSlide.setAdapter(washCarShopAdapter);
        if (carWashShopListBean.shopPhoto != null && carWashShopListBean.shopPhoto.size() != 0) {
            ImageUtil.setImage(this, mShopPhotoView, carWashShopListBean.shopPhoto.get(0), R.drawable.sliderimage_pic_normal_slider);
        } else {
            ImageUtil.setImage(this, mShopPhotoView, R.drawable.sliderimage_pic_normal_slider);
        }

        for(String url : carWashShopListBean.shopPhoto){
            ImageModel imageModel = new ImageModel();
            imageModel.thumbImage = url;
            imageModel.largeImage = url;
            imageModels.add(imageModel);
        }
        mShopPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int location[] = new int[2];
                Bundle bundle = new Bundle();
                bundle.putInt("locationX", location[0]);
                bundle.putInt("locationY", location[1]);
                bundle.putInt("width", metric.widthPixels);
                bundle.putInt("height",metric.widthPixels * 9 / 16);
                ActivitySwitchBase.toViewLagerPic(0, imageModels, context, bundle);
            }
        });

        myRatingBarView.setStar((int) carWashShopListBean.score);
    }

    private void initEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLookCommentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarWashActivitySwitcher.toWashCarCommentList(context, carWashShopListBean.shopID);
            }
        });

        mShopCallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WashCarShopActivity.this, com.hxqc.mall.thirdshop.R.style.MaterialDialog);
                builder.setTitle("拨打电话").setMessage(carWashShopListBean.shopTel).setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri uri = Uri.parse("tel:" + carWashShopListBean.shopTel);
                        intent.setData(uri);
                        WashCarShopActivity.this.startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        });
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(Color.parseColor("#00ffffff"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        monitorScrollView = (MonitorScrollView) findViewById(R.id.scroll_view);
        monitorScrollView.smoothScrollBy(0,20);
        monitorScrollView.setOnImageZoomListener(this);
        monitorScrollView.setScrollViewListener(mToolbar,this);
        monitorScrollView.setHeight(metric.widthPixels * 9 / 16);
        mShopPhotoView = (ImageView) findViewById(R.id.shop_photo);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mShopPhotoView.getLayoutParams();
        lp.width = metric.widthPixels;
        lp.height = metric.widthPixels * 9 / 16;
        mShopPhotoView.setLayoutParams(lp);

        mShopNameView = (TextView) findViewById(R.id.shop_name);
        mShopHoursView = (TextView) findViewById(R.id.shop_hours);
        mShopAddressView = (TextView) findViewById(R.id.shop_address);
        listViewNoSlide = (ListViewNoSlide) findViewById(R.id.list_view);
        mShopCallView = (ImageView) findViewById(R.id.shop_call);
        myRatingBarView = (MyRatingBarView) findViewById(R.id.rating_bar);
        mHouresView = (RelativeLayout) findViewById(R.id.rl_1);
        mAddressView = (RelativeLayout) findViewById(R.id.rl_2);

        circleImageView = (CircleImageView) findViewById(R.id.me_avatar);
        UsernameCommentView = (TextView) findViewById(R.id.tv_username_comment);
        commentStarView = (MyRatingBarView) findViewById(R.id.starView);
        mTagFlowView = (TagFlowLayout) findViewById(R.id.grid_view);
        mCommentView = (TextView) findViewById(R.id.comment);
        recyclerView = (RecyclerView) findViewById(R.id.rlv_comment_images);
        commentTimeView = (TextView) findViewById(R.id.tv_comment_post_time);
        mLookCommentView = (RelativeLayout) findViewById(R.id.look_comment);

        mLlCommentView = (LinearLayout) findViewById(R.id.ll_comment);
        mTextScoreView = (TextView) findViewById(R.id.text_score);
    }

    @Override
    public void magnifyImage(int distance) {
        DebugLog.i("Tag", " WashCarShopActivity :  " + distance);
        ViewGroup.LayoutParams lp = mShopPhotoView.getLayoutParams();
        lp.width = metric.widthPixels + distance;
        lp.height = (metric.widthPixels + distance) * 9 / 16;
        mShopPhotoView.setLayoutParams(lp);
    }

    @Override
    public void replyImage() {
        final ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mShopPhotoView.getLayoutParams();
        final float w = mShopPhotoView.getLayoutParams().width;// 图片当前宽度
        final float h = mShopPhotoView.getLayoutParams().height;// 图片当前高度
        final float newW = metric.widthPixels;// 图片原宽度
        final float newH = metric.widthPixels * 9 / 16;// 图片原高度
        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                mShopPhotoView.setLayoutParams(lp);
            }
        });
        //开启动画
        anim.start();
    }

    @Override
    public void onScrollChange(float f1) {
        DebugLog.i("Tag", " WashCarShopActivity f1:  " + f1);
        mToolbar.getBackground().setAlpha((int) (f1 * 255));
        String alpha=Integer.toHexString((int)(f1*255));
        if(alpha.length()==1){
            alpha="0"+alpha;
        }
        mToolbar.setTitleTextColor(Color.parseColor("#" + alpha + "ffffff"));
    }

    @Override
    public void moveDown() {

    }

    @Override
    public void moveUp() {

    }
}
