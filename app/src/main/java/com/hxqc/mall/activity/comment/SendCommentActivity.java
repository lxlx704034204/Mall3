package com.hxqc.mall.activity.comment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.api.CommentApiClient;
import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.adapter.comment.ImagePublishAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.photolibrary.activity.ImageBucketChooseActivity;
import com.hxqc.mall.photolibrary.activity.ImageZoomActivity;
import com.hxqc.mall.photolibrary.model.EventImages;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.CustomConstants;
import com.hxqc.mall.photolibrary.util.IntentConstants;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;




/**
 * 新车发表评论
 */
public class SendCommentActivity extends BackActivity {

    final private static int MAX_TEXT_LENGTH = 200;//最大数字限制
    InputMethodManager inputMethodManager;

    private GridView mGridView;
    private ImagePublishAdapter mAdapter;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();



    EditText mMessageView;
    MyRatingBarView mScore;
    CommentApiClient apiClient;//
    //    判断当前是否发表
    boolean flag = false;
    String itemID = "+";
    String orderID = "+";

    //分数
    int score = 0;
    //评论字数
    int mCommentNum = 0;
    //评论
    String mComment = "";

    /**
     * 防止横竖屏销毁    与manifest对应 复写此方法无作为
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        DebugLog.i("qr_c","onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment);
        EventBus.getDefault().register(this);
        DebugLog.d("comments", "   SendCommentActivity   --   onCreate      ");
        getSupportActionBar().setTitle("发表评论");
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        apiClient = new CommentApiClient();
        ScrollView scrollView = (ScrollView) findViewById(R.id.sv_send_comment);
        scrollView.setVerticalScrollBarEnabled(false);
        itemID = getIntent().getStringExtra("item_id");
        orderID = getIntent().getStringExtra("order_id");
        mScore = (MyRatingBarView) findViewById(R.id.mrbv_press_star);
        mMessageView = (EditText) findViewById(R.id.met_write_comment);
        mScore.setStar(5);
        score = 5;
        //评分星星
        mScore.setmClickable(true);
        mScore.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                score = RatingScore;

            }
        });

        //edit 评论数
        mMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mComment = mMessageView.getText().toString().trim();
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

        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImagePublishAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == getDataSize()) {
                    new PopupWindows(SendCommentActivity.this, mGridView);
                    if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                } else {
                    Intent intent = new Intent(SendCommentActivity.this,
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
                    Intent intent = new Intent(SendCommentActivity.this,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_send_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_settings_send_comment) {
            if (TextUtils.isEmpty(mComment)) {
                ToastHelper.showYellowToast(getApplicationContext(), "请输入评论内容");
            } else {
                if (score <= 0) {
                    ToastHelper.showYellowToast(getApplicationContext(), "请选择评分");
                } else {
                    if (mCommentNum > MAX_TEXT_LENGTH) {
                        ToastHelper.showYellowToast(getApplicationContext(), "字数超过限制");
                    } else {//TODO 发布功能
//                        ToastHelper.showRedToast(getApplicationContext(), "++" + mCommentNum + "==" + mComment + "()_" + score);
                        if (TextUtils.isEmpty(itemID) || TextUtils.isEmpty(orderID)) {
                            ToastHelper.showYellowToast(getApplicationContext(), "商品有误");
                        } else {
                            try {
                                apiClient.sendComment(this,itemID, orderID, score, mComment, mDataList, new LoadingAnimResponseHandler(SendCommentActivity.this) {

                                    @Override
                                    public void onSuccess(String response) {
                                        new BaseSharedPreferencesHelper(SendCommentActivity.this).setCommentChange(true);
                                        new BaseSharedPreferencesHelper(SendCommentActivity.this).setOrderChange(true);
                                        ToastHelper.showGreenToast(getApplicationContext(), "已发表成功，请等待审核");
                                        DebugLog.i("comments", "+@@**+= send comment =++" + response);
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                SendCommentActivity.this.finish();
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
        }
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
            SendCommentActivity.this.finish();
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






