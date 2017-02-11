package com.hxqc.mall.activity.order;

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
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.adapter.comment.ImagePublishAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.photolibrary.activity.ImageBucketChooseActivity;
import com.hxqc.mall.photolibrary.activity.ImageZoomActivity;
import com.hxqc.mall.photolibrary.model.EventImages;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.CustomConstants;
import com.hxqc.mall.photolibrary.util.IntentConstants;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.GridViewNoSlide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hxqc.mall.R;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * 保养发表评论
 */
public class MaintainSendCommentActivity extends BackActivity implements View.OnClickListener{
    final private static int MAX_TEXT_LENGTH = 200;//最大数字限制
    InputMethodManager inputMethodManager;

    private GridViewNoSlide mGridView;
    private ImagePublishAdapter mAdapter;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();

    EditText mMessageView;
    MyRatingBarView mScore1,mScore2,mScore3;
    UserApiClient mUserApiClient;
    Button mButton;
    //    判断当前是否发表
    boolean flag = false;
    String orderID = "+";



    int default_score=5;
    //评论字数
    int mCommentNum = 0;
    //评论
    String mComment = "";
    //店铺图片
    String mShopPhoto="";
    String mShopID="";
    //分数
    int score1,score2,score3;


    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_send_comment);
        EventBus.getDefault().register(this);
        getSupportActionBar().setTitle("发表评价");
        mUserApiClient=new UserApiClient();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ScrollView scrollView = (ScrollView) findViewById(R.id.sv_send_comment);
        scrollView.setVerticalScrollBarEnabled(false);
        orderID = getIntent().getStringExtra(ActivitySwitcher.ORDER_ID);
        mShopPhoto=getIntent().getStringExtra(ActivitySwitcher.SHOP_PHOTO);
        mShopID=getIntent().getStringExtra(ActivitySwitcher.SHOP_ID);
        ImageView welcome = (ImageView) findViewById(R.id.send_comment_userPic);
        ImageUtil.setImage(this, welcome, mShopPhoto);
        mScore1 = (MyRatingBarView) findViewById(R.id.mrbv_press_star_01);
        mScore1.setmClickable(true);
        mScore1.setStar(default_score);
        mScore2 = (MyRatingBarView) findViewById(R.id.mrbv_press_star_02);
        mScore2.setmClickable(true);
        mScore2.setStar(default_score);
        mScore3 = (MyRatingBarView) findViewById(R.id.mrbv_press_star_03);
        mScore3.setmClickable(true);
        mScore3.setStar(default_score);
        score1=score2=score3=default_score;
        mMessageView = (EditText) findViewById(R.id.met_write_comment);
        mButton= (Button) findViewById(R.id.but_send_comment);
        mButton.setOnClickListener(this);
        mScore1.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                score1 = RatingScore;

            }
        });
        mScore2.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                score2 = RatingScore;

            }
        });
        mScore3.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                score3 = RatingScore;

            }
        });
        //edit 评论数
        mMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
                    cursorPos = mMessageView.getSelectionEnd();
                    inputAfterText= s.toString();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
//                    DebugLog.d("TextWatcher","onTextChanged->s="+s.toString());
                    try {
                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                        if (isEmoji(input.toString())) {
                            resetText = true;
                          //  ToastHelper.showYellowToast(MaintainSendCommentActivity.this, "不支持输入法表情");
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            mMessageView.setText(inputAfterText);
                            CharSequence text = mMessageView.getText();
                            if (text instanceof Spannable) {
                                Spannable spanText = (Spannable) text;
                                Selection.setSelection(spanText, spanText.length());
                            }
                        }
                    }catch (Exception e){}
                } else {
                    resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mComment=mMessageView.getText().toString().trim();
                mCommentNum = mComment.length();
            }
        });
        initData();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugLog.d("comments", "   SendCommentActivity   --   onResume      ");
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
//                    new PopupWindows(MaintainSendCommentActivity.this, mGridView);
//                    if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
//                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
                    getPhotos();
                } else {
                    Intent intent = new Intent(MaintainSendCommentActivity.this,
                            ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
                            (Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 类似微信 选取照片方式
     */
    public  void  getPhotos(){
        new MultiImageSelector(this)
                .showCamera(true)
                .count(getAvailableSize())
                .start(this, new MultiImageSelector.MultiImageCallBack() {
                    @Override
                    public void multiSelectorImages(Collection<String> result) {
                        if (result != null && result.size() > 0) {
                            String[] paths = new String[result.size()];
                            for (int i=0;i<result.size();i++)
                            {
                                String imgPath = result.toArray(paths)[i];
                                ImageItem item = new ImageItem(imgPath);
                                mDataList.add(item);
                            }
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

//    public RequestParams getRequestParams(List<ImageItem> pics){
//        RequestParams requestParams = new RequestParams();
//        if (pics != null && pics.size() > 0) {
//            int size = pics.size();
//            for (int i = 0; i < size; i++) {
//                try {
//                    Bitmap bitmap = ImageUtils.compressAndGetImgBitmap(
//                            pics.get(i).sourcePath,
//                            ScreenUtil.getScreenWidth(this),
//                            ScreenUtil.getScreenHeight(this));
//
//                    @SuppressWarnings("ConstantConditions")
//                    String path = this.getExternalCacheDir().getPath() + "/temp" + i + ".jpg";
//                    BitmapCompress.compressImageToSpecifySizeAndSaveFile(bitmap, 640, 640, path, 96);
//                    File file = new File(path);
//                    requestParams.put("image" + (i + 1), file);
//                    bitmap.recycle();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        return requestParams;
//    }

    private List<com.hxqc.mall.core.model.ImageItem> getListImageItem(){
        List<com.hxqc.mall.core.model.ImageItem> imageItems=new ArrayList<>();
        for (int i=0;i<mDataList.size();i++)
        {
             imageItems.add(new com.hxqc.mall.core.model.ImageItem(mDataList.get(i).sourcePath));
        }
        return imageItems;
    }

    /**
     * 判断是否有输入法表情
     * @param string
     * @return
     */
    public boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    /**
     * 发布评论
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mComment)) {
            ToastHelper.showYellowToast(MaintainSendCommentActivity.this, "请输入评论内容");
        } else{
            if (score1 <= 0) {
                ToastHelper.showYellowToast(MaintainSendCommentActivity.this, "请选择技术评分");
            } else if(score2<= 0){
                ToastHelper.showYellowToast(MaintainSendCommentActivity.this, "请选择服务评分");
            } else if(score3<= 0){
                ToastHelper.showYellowToast(MaintainSendCommentActivity.this, "请选择环境评分");
            }else {
                //发布
                if (mCommentNum > MAX_TEXT_LENGTH) {
                    ToastHelper.showYellowToast(MaintainSendCommentActivity.this, "字数超过限制");
                } else {
                    if (TextUtils.isEmpty(orderID)) {
                        ToastHelper.showYellowToast(MaintainSendCommentActivity.this, "订单有误");
                    } else {
                        //todo 调发布评论接口
                        try {
                            mUserApiClient.sendComment(v.getContext(),mShopID,orderID, score1, score2, score3, mComment, getListImageItem(), new LoadingAnimResponseHandler(MaintainSendCommentActivity.this) {
                                @Override
                                public void onSuccess(String response) {
                                    new BaseSharedPreferencesHelper(MaintainSendCommentActivity.this).setCommentChange(true);
                                    new BaseSharedPreferencesHelper(MaintainSendCommentActivity.this).setOrderChange(true);
                                    ToastHelper.showGreenToast(MaintainSendCommentActivity.this, "已发表成功");
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            MaintainSendCommentActivity.this.finish();
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        }
                                    }, 2300);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastHelper.showGreenToast(MaintainSendCommentActivity.this, "发表失败，请确认信息是否正确");
                        }
                    }
                }
            }
        }


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
                    Intent intent = new Intent(MaintainSendCommentActivity.this,
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
            MaintainSendCommentActivity.this.finish();
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






