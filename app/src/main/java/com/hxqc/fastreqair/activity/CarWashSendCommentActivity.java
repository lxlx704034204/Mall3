package com.hxqc.fastreqair.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.fastreqair.api.CarWashApiClient;
import com.hxqc.fastreqair.model.CarWashTagBean;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.adapter.comment.ImagePublishAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.photolibrary.activity.ImageBucketChooseActivity;
import com.hxqc.mall.photolibrary.activity.ImageZoomActivity;
import com.hxqc.mall.photolibrary.model.EventImages;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.CustomConstants;
import com.hxqc.mall.photolibrary.util.IntentConstants;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.GridViewNoSlide;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import hxqc.mall.R;

/**
 * 洗车发表评论
 */
public class CarWashSendCommentActivity extends BackActivity implements OnClickListener,View.OnTouchListener{
    private static final String TAG = "CarWashSendCommentActivity";
    InputMethodManager inputMethodManager;
    private GridViewNoSlide mGridView;
    private ImagePublishAdapter mAdapter;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
    MyRatingBarView mScore;
    CarWashApiClient mCarWashApiClient;
    Button mButton;
    TagFlowLayout mTagFlowLayout; //标签

    int score = 0;  //分数

    String mShopPhoto=""; //店铺图片
    String mShopID=""; //店铺ID
    String orderID = ""; //订单号
    String mShopName = ""; //店铺名称
    String mAmuount = ""; //支付金额
    LayoutInflater mInflater;
    ArrayList<CarWashTagBean> mCarWashTagBeans;
    LinearLayout mContentlly,mPaymentAmountlly;
    private boolean isHide=false;
    private ScrollView scrollView;
    private float lastY; // 手指 Y点
    private boolean isUpSlide; //是否向上滑动
    private int PaymentAmountllyHeight; //支付金额布局的原始高度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwash_send_comment);
        EventBus.getDefault().register(this);
        getSupportActionBar().setTitle("发表评论");
        mCarWashApiClient=new CarWashApiClient();
        mInflater=LayoutInflater.from(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        scrollView = (ScrollView) findViewById(R.id.sv_send_comment);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setOnTouchListener(this);
        mTagFlowLayout= (TagFlowLayout) findViewById(R.id.send_comment_flowlayout);
        orderID = getIntent().getStringExtra(CarWashActivitySwitcher.ORDER_ID);
        mShopPhoto=getIntent().getStringExtra(CarWashActivitySwitcher.SHOP_PHOTO);
        mShopID=getIntent().getStringExtra(CarWashActivitySwitcher.SHOP_ID);
        mShopName=getIntent().getStringExtra(CarWashActivitySwitcher.SHOP_NAME);
        mAmuount=getIntent().getStringExtra(CarWashActivitySwitcher.PAYMENT_AMOUNT);
        CircleImageView welcome = (CircleImageView) findViewById(R.id.send_comment_shopPhoto);
        ImageUtil.setImage(this, welcome, mShopPhoto);
        TextView mShopNameView= (TextView) findViewById(R.id.send_comment_shopName);
        mShopNameView.setText(mShopName);
        TextView mAmountView= (TextView) findViewById(R.id.send_comment_payment_amount);
        mAmountView.setText(OtherUtil.amountFormat(mAmuount,false));
        mContentlly= (LinearLayout) findViewById(R.id.send_comment_content_lly);
        mPaymentAmountlly= (LinearLayout) findViewById(R.id.send_comment_payment_lly);
        PaymentAmountllyHeight=mPaymentAmountlly.getLayoutParams().height;


        mScore = (MyRatingBarView) findViewById(R.id.mrbv_press_star);
        mScore.setmClickable(true);
        mButton= (Button) findViewById(R.id.but_send_comment);
        mButton.setOnClickListener(this);
        mScore.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                score = RatingScore;
                initTagFlow();
                if(!isHide)
                  hideAmount();
                mContentlly.setVisibility(View.VISIBLE);
            }
        });
        loadTag();
        initData();
        initView();
    }

    /**
     * 显示支付金额
     */

    private void showAmount() {
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                LayoutParams lp = mPaymentAmountlly.getLayoutParams();
                lp.height = (int) (PaymentAmountllyHeight * cVal);
                mPaymentAmountlly.setLayoutParams(lp);
            }
        });
        anim.start();
        isHide = false;
    }

    /**
     * 隐藏支付金额
     */
    private void hideAmount() {
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                LayoutParams lp = mPaymentAmountlly.getLayoutParams();
                lp.height = (int) (PaymentAmountllyHeight * (1-cVal));
                mPaymentAmountlly.setLayoutParams(lp);
            }
        });
        anim.start();
        isHide = true;

    }



    private void loadTag() {
        mCarWashApiClient.getCarWashTags(new LoadingAnimResponseHandler(CarWashSendCommentActivity.this, true) {
            @Override
            public void onSuccess(String response) {
                mCarWashTagBeans = JSONUtils.fromJson(response, new TypeToken<ArrayList<CarWashTagBean>>() {});
                initTagFlow();
            }
        });
    }

    private void initTagFlow(){
        mTagFlowLayout.setAdapter(new TagAdapter<CarWashTagBean.Tags>(getTagsByScore(score)) {
            @Override
            public View getView(FlowLayout parent, int position, CarWashTagBean.Tags tags) {
                View view = mInflater.inflate(R.layout.layout_carwash_flowlayout_view, parent, false);
                TextView tv = (TextView) view.findViewById(R.id.carwash_flowlayout_textview);
                tv.setText(tags.tagTitle);
                return view;
            }
        });
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if(view.isSelected())
                    view.setSelected(false);
                else
                    view.setSelected(true);
                return false;
            }
        });
    }


    public  List<CarWashTagBean.Tags> getTagsByScore(int score){
        if(mCarWashTagBeans !=null)
        {
            for (CarWashTagBean carWashTagBean:mCarWashTagBeans) {
                if(score==carWashTagBean.score)
                    return carWashTagBean.tags;
            }
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugLog.d(TAG, "   SendCommentActivity   --   onResume      ");
        if(getDataSize()>0)  //很关键，提示拍照部分正常显示
         mGridView.setNumColumns(4);
        else
         mGridView.setNumColumns(1);

        mAdapter.notifyDataSetChanged();
        saveAvailableSize(CustomConstants.MAX_IMAGE_SIZE - mDataList.size());
    }

    private void saveAvailableSize(int dSize) {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        DebugLog.d("newimage", dSize + "");
        sp.edit().putInt(CustomConstants.AVAILABLE_IMAGE_SIZE, dSize).apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveTempToPref();
        DebugLog.i("life", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeTempFromPref();
        EventBus.getDefault().unregister(this);
        DebugLog.i("life", "onDestroy");
        mDataList.clear();
    }

    private void saveTempToPref() {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String prefStr = JSONUtils.toJson(mDataList);
        DebugLog.d("newimage", prefStr);
        sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).apply();
    }

    private void getTempFromPref() {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String prefStr = sp.getString(CustomConstants.PREF_TEMP_IMAGES, null);
        DebugLog.d("newimage", "  getTempFromPref-----  " + prefStr);
        if (!TextUtils.isEmpty(prefStr)) {
            DebugLog.d("newimage", "  getTempFromPref  " + prefStr);
            mDataList = JSONUtils.fromJson(prefStr, new TypeToken<List<ImageItem>>() {
            });
        }
    }

    private void removeTempFromPref() {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        DebugLog.d("newimage", "  removeTempFromPref  ");
        sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).remove(CustomConstants.AVAILABLE_IMAGE_SIZE).apply();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        getTempFromPref();
    }

    public void initView() {
        mGridView = (GridViewNoSlide) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImagePublishAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == getDataSize()) {
                    new PopupWindows(CarWashSendCommentActivity.this, mGridView);
                    if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                } else {
                    Intent intent = new Intent(CarWashSendCommentActivity.this,
                            ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
                            (Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
                    startActivity(intent);
                }
            }
        });
    }

    private int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private int getAvailableSize() {
        int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
        if (availSize >= 0) {
            return availSize;
        }
        return 0;
    }

    /**
     * 发布评论
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (score <= 0) {
            ToastHelper.showYellowToast(getApplicationContext(), "请给店铺评分");
        } else{
            if (mTagFlowLayout.getSelectedList().size()==0) {
                ToastHelper.showYellowToast(getApplicationContext(), "请选择评论标签");
            }else {
                //发布
                    if (TextUtils.isEmpty(orderID)) {
                        ToastHelper.showYellowToast(getApplicationContext(), "订单有误");
                    } else {
                        try {
                            //todo 调发布评论接口
                            mCarWashApiClient.sendComment(v.getContext(),orderID, mShopID, score,"",getTagsID(mTagFlowLayout.getSelectedList()), mDataList, new LoadingAnimResponseHandler(CarWashSendCommentActivity.this,true) {
                                @Override
                                public void onSuccess(String response) {
                                    new BaseSharedPreferencesHelper(CarWashSendCommentActivity.this).setCommentChange(true);
                                    new BaseSharedPreferencesHelper(CarWashSendCommentActivity.this).setOrderChange(true);
                                    ToastHelper.showGreenToast(getApplicationContext(), "已发表成功，请等待审核");
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            CarWashSendCommentActivity.this.finish();
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        }
                                    }, 2300);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastHelper.showGreenToast(getApplicationContext(), "发表失败，请确认信息是否正确");
                        }
                    }
            }
        }


    }

    private String getTagsID(Set<Integer> selectedList) {
        String mTagsID="";
        try {
            Iterator<Integer> keyIte = selectedList.iterator();
            while (keyIte.hasNext())
            {
                Integer keyIndex = keyIte.next();
                mTagsID +=getTagsByScore(score).get(keyIndex).tagID+"|";
//            mTagsID +=keyIndex+"|";
            }
            DebugLog.d(TAG,"tagsID-->:"+mTagsID.substring(0,mTagsID.length()-1));
            return mTagsID.substring(0,mTagsID.length()-1);
        }catch (Exception e) {}
      return mTagsID;
    }
    
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float disY = event.getY() - lastY;
                if (Math.abs(disY) > 40) { // 垂直方向滑动设置一个偏差值，如果滑动距离不够，不做操作
                    //是否向上滑动
                    isUpSlide = disY < 0;
                    //实现显示与隐藏
                    if (isUpSlide) {
                        if (!isHide)
                            hideAmount();
                    } else {
                        if (isHide)
                            showAmount();
                    }
                }

                break;
        }
        return false;
    }


    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(LayoutParams.MATCH_PARENT);
            setHeight(LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            setBackgroundDrawable(new BitmapDrawable());
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

//            View backView = view.findViewById(R.id.v_popup_back);

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);

//            backView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });

            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    takePhoto();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(CarWashSendCommentActivity.this,
                            ImageBucketChooseActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                            getAvailableSize());
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void takePhoto() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(Environment.getExternalStorageDirectory()
                + "/myimage/", String.valueOf(System.currentTimeMillis())
                + ".jpg");
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PICTURE:
                if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE
                        && resultCode == -1 && !TextUtils.isEmpty(path)) {
                    ImageItem item = new ImageItem("");
                    item.sourcePath = path;
                    mDataList.add(item);
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        removeTempFromPref();
        this.finish();
        return false;
    }

    //接收 图片列表
    @Subscribe
    public void onEventMainThread(EventImages event) {
        if (event._type == IntentConstants.SELECT_TYPE) {
            if (mDataList != null)
                mDataList.addAll(event.imageItems);
        } else if (event._type == IntentConstants.PREVIEW_TYPE) {
            mDataList.clear();
            mDataList.addAll(event.imageItems);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DebugLog.i("life", "back-----");
            removeTempFromPref();
            CarWashSendCommentActivity.this.finish();
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}






