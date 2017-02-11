package com.hxqc.autonews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.adapter.SendCommentAdapter;
import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.photolibrary.activity.ImageZoomActivity;
import com.hxqc.mall.photolibrary.model.EventImages;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.CustomConstants;
import com.hxqc.mall.photolibrary.util.IntentConstants;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.GridViewNoSlide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hxqc.mall.R;
import me.nereo.multi_image_selector.MultiImageSelector;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

/**
 * 口碑 发表评价
 * Created by huangyi on 16/10/14.
 */
public class SendCommentActivity extends BackActivity {

    public static List<ImageItem> mDataList = new ArrayList<>();
    String mExtID, mBrand, mSeries, mImage;
    int mSpace = 5, mPower = 5, mGasoline = 5, mComfort = 5, mFacade = 5, mInterior = 5;
    TextView mSpaceView, mPowerView, mGasolineView, mComfortView, mFacadeView, mInteriorView;
    EditText mEditView;
    GridViewNoSlide mGridView;
    SendCommentAdapter mAdapter;
    InputMethodManager mManager;

    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment_hy);

        mExtID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(FilterResultKey.EXT_ID);
        mBrand = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(FilterResultKey.BRAND_KEY);
        mSeries = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(FilterResultKey.SERIES_KEY);
        mImage = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(FilterResultKey.IMG_KEY);
        EventBus.getDefault().register(this);
        mManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getTempFromPref();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        saveAvailableSize(SendCommentAdapter.MAX_IMAGE_SIZE - mDataList.size());
    }

    @Override
    public void onPause() {
        super.onPause();
        saveTempToPref();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        removeTempFromPref();
        mDataList.clear();
    }

    //接收 图片列表
    @Subscribe
    public void onEventMainThread(EventImages event) {
        if (event._type == IntentConstants.SELECT_TYPE) {
            if (mDataList != null) mDataList.addAll(event.imageItems);

        } else if (event._type == IntentConstants.PREVIEW_TYPE) {
            mDataList.clear();
            mDataList.addAll(event.imageItems);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null)
                mManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), HIDE_NOT_ALWAYS);
        }
        return super.onTouchEvent(event);
    }

    private void initView() {
        mSpaceView = (TextView) findViewById(R.id.comment_space);
        mPowerView = (TextView) findViewById(R.id.comment_power);
        mGasolineView = (TextView) findViewById(R.id.comment_gasoline);
        mComfortView = (TextView) findViewById(R.id.comment_comfort);
        mFacadeView = (TextView) findViewById(R.id.comment_facade);
        mInteriorView = (TextView) findViewById(R.id.comment_interior);

        MyRatingBarView spaceBarView = (MyRatingBarView) findViewById(R.id.comment_star_space);
        spaceBarView.setStar(5);
        spaceBarView.setmClickable(true);
        spaceBarView.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                mSpace = RatingScore;
                mSpaceView.setText(RatingScore + ".0分");
            }
        });
        MyRatingBarView powerBarView = (MyRatingBarView) findViewById(R.id.comment_star_power);
        powerBarView.setStar(5);
        powerBarView.setmClickable(true);
        powerBarView.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                mPower = RatingScore;
                mPowerView.setText(RatingScore + ".0分");
            }
        });
        MyRatingBarView gasolineBarView = (MyRatingBarView) findViewById(R.id.comment_star_gasoline);
        gasolineBarView.setStar(5);
        gasolineBarView.setmClickable(true);
        gasolineBarView.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                mGasoline = RatingScore;
                mGasolineView.setText(RatingScore + ".0分");
            }
        });
        MyRatingBarView comfortBarView = (MyRatingBarView) findViewById(R.id.comment_star_comfort);
        comfortBarView.setStar(5);
        comfortBarView.setmClickable(true);
        comfortBarView.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                mComfort = RatingScore;
                mComfortView.setText(RatingScore + ".0分");
            }
        });
        MyRatingBarView facadeBarView = (MyRatingBarView) findViewById(R.id.comment_star_facade);
        facadeBarView.setStar(5);
        facadeBarView.setmClickable(true);
        facadeBarView.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                mFacade = RatingScore;
                mFacadeView.setText(RatingScore + ".0分");
            }
        });
        MyRatingBarView interiorBarView = (MyRatingBarView) findViewById(R.id.comment_star_interior);
        interiorBarView.setStar(5);
        interiorBarView.setmClickable(true);
        interiorBarView.setOnRatingListener(new MyRatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                mInterior = RatingScore;
                mInteriorView.setText(RatingScore + ".0分");
            }
        });

        ImageView photoView = (ImageView) findViewById(R.id.comment_photo);
        ImageUtil.setImageSquare(this, photoView, mImage);
        mEditView = (EditText) findViewById(R.id.comment_edit);
        mEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!resetText) {
                    cursorPos = mEditView.getSelectionEnd();
                    inputAfterText = s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
                    try {
                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                        if (isEmoji(input.toString())) {
                            resetText = true;
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            mEditView.setText(inputAfterText);
                            CharSequence text = mEditView.getText();
                            if (text instanceof Spannable) {
                                Spannable spanText = (Spannable) text;
                                Selection.setSelection(spanText, spanText.length());
                            }
                        }
                    } catch (Exception e) {
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mGridView = (GridViewNoSlide) findViewById(R.id.comment_grid);
        mAdapter = new SendCommentAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == getDataSize()) {
                    ArrayList<String> list = new ArrayList<>();
                    for (ImageItem item : mDataList) {
                        list.add(item.sourcePath);
                    }

                    MultiImageSelector selector = new MultiImageSelector(SendCommentActivity.this);
                    selector.showCamera(true);
                    selector.count(6);
                    selector.origin(list);
                    selector.start(SendCommentActivity.this, new MultiImageSelector.MultiImageCallBack() {
                        @Override
                        public void multiSelectorImages(Collection<String> result) {
                            // if max=6 选完6张再使用相机拍一张会出现Bug 会出现7张的情况 so...
                            if (result.size() > 6) result.remove(6);
                            mDataList.clear();
                            for (String s : result) {
                                ImageItem item = new ImageItem("");
                                item.sourcePath = s;
                                mDataList.add(item);
                            }
                        }
                    });

                    if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null)
                        mManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                } else {
                    Intent intent = new Intent(SendCommentActivity.this, ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST, (Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.comment_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = mEditView.getText().toString();
                if (content.length() != 0) {
                    if (content.length() < 10) {
                        //字数少了
                        ToastHelper.showYellowToast(SendCommentActivity.this, "评价长度不符");

                    } else if (content.length() < 200) {
                        UserInfoHelper.getInstance().loginAction(SendCommentActivity.this, new UserInfoHelper.OnLoginListener() {
                            @Override
                            public void onLoginSuccess() {
                                send(content);
                            }
                        });

                    } else {
                        //字数多了
                        ToastHelper.showYellowToast(SendCommentActivity.this, "评价长度不符");
                    }

                } else {
                    //空的
                    ToastHelper.showYellowToast(SendCommentActivity.this, "评价长度不符");
                }
            }
        });
    }

    private void send(String content) {
        String image1 = "", image2 = "", image3 = "", image4 = "", image5 = "", image6 = "";
        for (int i = 0; i < mDataList.size(); i++) {
            switch (i) {
                case 0:
                    image1 = mDataList.get(0).sourcePath;
                    break;

                case 1:
                    image2 = mDataList.get(1).sourcePath;
                    break;

                case 2:
                    image3 = mDataList.get(2).sourcePath;
                    break;

                case 3:
                    image4 = mDataList.get(3).sourcePath;
                    break;

                case 4:
                    image5 = mDataList.get(4).sourcePath;
                    break;

                case 5:
                    image6 = mDataList.get(5).sourcePath;
                    break;
            }
        }

        new AutoInformationApiClient().sendAutoGrade(this, mExtID, mBrand, mSeries, content, image1, image2, image3, image4, image5, image6,
                mComfort, mSpace, mPower, mGasoline, mFacade, mInterior, new LoadingAnimResponseHandler(this, true) {
                    @Override
                    public void onSuccess(String response) {
                        ToastHelper.showGreenToast(SendCommentActivity.this, "评价成功");
                        finish();
                    }
                });
    }

    private void saveAvailableSize(int dSize) {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().putInt(CustomConstants.AVAILABLE_IMAGE_SIZE, dSize).apply();
    }

    private void saveTempToPref() {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String prefStr = JSONUtils.toJson(mDataList);
        sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).apply();
    }

    private void removeTempFromPref() {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).remove(CustomConstants.AVAILABLE_IMAGE_SIZE).apply();
    }

    private void getTempFromPref() {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String prefStr = sp.getString(CustomConstants.PREF_TEMP_IMAGES, null);

        if (!TextUtils.isEmpty(prefStr))
            mDataList = JSONUtils.fromJson(prefStr, new TypeToken<List<ImageItem>>() {
            });
    }

    private int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * 判断是否有输入法表情
     *
     * @param string
     * @return
     */
    public boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

}
