//package com.hxqc.pay.fragment;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AnimationUtils;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.hxqc.mall.core.api.DialogResponseHandler;
//import com.hxqc.mall.core.controler.UserInfoHelper;
//import com.hxqc.mall.core.model.User;
//import com.hxqc.mall.core.model.loan.InstallmentData;
//import com.hxqc.mall.core.model.loan.InstallmentDataKeyValue;
//import com.hxqc.mall.core.model.loan.InstallmentInfo;
//import com.hxqc.mall.core.model.loan.LoanItemFinanceModel;
//import com.hxqc.mall.core.model.loan.LoanKeyValueModel;
//import com.hxqc.mall.core.util.ToastHelper;
//import com.hxqc.mall.photolibrary.util.ImageUtils;
//import com.hxqc.pay.adapter.UploadInfoSpinnerAdapter;
//import com.hxqc.pay.api.PayApiClient;
//import com.hxqc.pay.event.EventUpLoadFile;
//import com.hxqc.pay.util.ConstantValue;
//import com.hxqc.pay.views.ImageUploadView;
//import com.hxqc.util.BitmapCompress;
//import com.hxqc.util.DebugLog;
//import com.hxqc.util.ScreenUtil;
//import com.hxqc.widget.PhotoChooseFragment;
//
//import cz.msebera.android.httpclient.Header;
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Random;
//
//import hxqc.mall.R;
//
///**
// * Author: liukechong
// * Date: 2015-10-17
// * FIXME 提交分期资料Fragment
// * Todo
// */
//public class UploadInfoFragment extends PhotoChooseFragment implements View.OnClickListener, ImageUploadView.ImageUploadCallBack {
//
//    private static final String TAG = "UploadInfoFragment";
//    private View view;
//
//    private String orderID;
//
//    //性别选择
//    private RadioGroup sex_radio_group_upload_info;
//    private RadioButton men_radio;
//    private RadioButton women_radio;
//    private RadioButton secret_radio;
//
//    //收入EditText
//    private EditText month_salary_upload_info;
//
//    //对应显示的text
//    private TextView buyer_name_upload_info;
//    private TextView phone_number_upload_info;
//    private TextView bank_upload_info;
//    private TextView city_upload_info;
//    private TextView brand_upload_info;
//    private TextView price_upload_info;
//    private TextView date_upload_info;
//
//    //需要上传的图片
//    private ImageUploadView image1;
//    private ImageUploadView image2;
//    private ImageUploadView image3;
//    private ImageUploadView image4;
//    private ImageUploadView image5;
//    Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            String imagePath = msg.getData().getString("imagePath");
//
//            if (msg.what == 210) {
//                image1.setImage("file://" + imagePath);
//                image1.uploadImage(imagePath);
//            } else if (msg.what == 220) {
//                image2.setImage("file://" + imagePath);
//                image2.uploadImage(imagePath);
//            } else if (msg.what == 230) {
//                image3.setImage("file://" + imagePath);
//                image3.uploadImage(imagePath);
//            } else if (msg.what == 240) {
//                image4.setImage("file://" + imagePath);
//                image4.uploadImage(imagePath);
//            } else if (msg.what == 250) {
//                image5.setImage("file://" + imagePath);
//                image5.uploadImage(imagePath);
//            }
//            return true;
//        }
//    });
//    private int imageViewFlag = 0;
//    //保存按钮
//    private Button save;
//    //对应下拉框
//    private Spinner houseProperty_upload_info;
//    private Spinner industry_upload_info;
//    private Spinner marriageStatus_upload_info;
//    //经融机构容器
//    private LinearLayout container_upload_info;
//    private String orderStatus;//10,26,27不可点击
//    private InstallmentInfo installmentInfo;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        if (view == null) {
//            view = inflater.inflate(R.layout.fragment_upload_info, container, false);
//            initView();
//        }
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            orderID = arguments.getString(ConstantValue.PAY_MAIN_ORDER_ID);
//            installmentInfo = arguments.getParcelable("InstallmentInfo");
//            DebugLog.d(TAG, "onActivityCreated() returned: " + installmentInfo);
//            setInfo(installmentInfo);
//            orderStatus = arguments.getString(ConstantValue.ORDER_STATUS);
//            DebugLog.d(TAG, "onCreateView() returned: " + orderStatus);
//            if (!TextUtils.isEmpty(orderStatus)) {
//                if (!"10".equals(orderStatus) && !"26".equals(orderStatus) && !"27".equals(orderStatus)) {
//                    setEnable();
//                }
//            }
//        }
//    }
//
//    private void initView() {
//
//        bank_upload_info = (TextView) view.findViewById(R.id.bank_upload_info);
//        city_upload_info = (TextView) view.findViewById(R.id.city_upload_info);
//        brand_upload_info = (TextView) view.findViewById(R.id.brand_upload_info);
//        price_upload_info = (TextView) view.findViewById(R.id.price_upload_info);
//        date_upload_info = (TextView) view.findViewById(R.id.date_upload_info);
//
//        save = (Button) view.findViewById(R.id.save_upload_info);
//
//        image1 = (ImageUploadView) view.findViewById(R.id.image1);
//        image2 = (ImageUploadView) view.findViewById(R.id.image2);
//        image3 = (ImageUploadView) view.findViewById(R.id.image3);
//        image4 = (ImageUploadView) view.findViewById(R.id.image4);
//        image5 = (ImageUploadView) view.findViewById(R.id.image5);
//
//        image1.setCallBack(this);
//        image2.setCallBack(this);
//        image3.setCallBack(this);
//        image4.setCallBack(this);
//        image5.setCallBack(this);
//
//        buyer_name_upload_info = (TextView) view.findViewById(R.id.buyer_name_upload_info);
//        phone_number_upload_info = (TextView) view.findViewById(R.id.phone_number_upload_info);
//        sex_radio_group_upload_info = (RadioGroup) view.findViewById(R.id.sex_radio_group_upload_info);
//
//        men_radio = (RadioButton) view.findViewById(R.id.men_radio);
//        women_radio = (RadioButton) view.findViewById(R.id.women_radio);
//        secret_radio = (RadioButton) view.findViewById(R.id.secret_radio);
//        month_salary_upload_info = (EditText) view.findViewById(R.id.month_salary_upload_info);
//        setEditText();
//
//        houseProperty_upload_info = (Spinner) view.findViewById(R.id.houseProperty_upload_info);
//        marriageStatus_upload_info = (Spinner) view.findViewById(R.id.marriageStatus_upload_info);
//        industry_upload_info = (Spinner) view.findViewById(R.id.industry_upload_info);
//
//        container_upload_info = (LinearLayout) view.findViewById(R.id.container_upload_info);
//
//        save.setOnClickListener(this);
//
//
//    }
//
//    /**
//     * 设置控件不可操作
//     */
//    private void setEnable() {
//        image1.setEnabled(false);
//        image2.setEnabled(false);
//        image3.setEnabled(false);
//        image4.setEnabled(false);
//        image5.setEnabled(false);
//
//        //保存按钮
//        save.setVisibility(View.GONE);
//
//        //对应下拉框
//        houseProperty_upload_info.setEnabled(false);
//        industry_upload_info.setEnabled(false);
//        marriageStatus_upload_info.setEnabled(false);
//
//        month_salary_upload_info.setEnabled(false);
//        month_salary_upload_info.setTextColor(getResources().getColor(R.color.text_gray));
//        men_radio.setEnabled(false);
//        women_radio.setEnabled(false);
//    }
//
//    String gender = null;
//    /**
//     * 设置性别Radio button
//     */
//    private void setSexRadio(final ArrayList<InstallmentDataKeyValue> dataList) {
//        if (dataList == null || dataList.isEmpty()) {
//            return;
//        }
//        for (int i = 0; i < sex_radio_group_upload_info.getChildCount(); i++) {
//            sex_radio_group_upload_info.getChildAt(i).setVisibility(View.INVISIBLE);
//        }
//
//        UserInfoHelper.getInstance().getUserInfo(getActivity(), new UserInfoHelper.UserInfoAction() {
//            @Override
//            public void showUserInfo(User meData) {
//                gender = meData.gender;
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }, false);
//
//        for (int i = 0; i < dataList.size(); i++) {
//            RadioButton childAt = (RadioButton) sex_radio_group_upload_info.getChildAt(i);
//            childAt.setVisibility(View.VISIBLE);
//            childAt.setText(dataList.get(i).value);
//            if (!TextUtils.isEmpty(gender) && Integer.parseInt(gender) * 10 == dataList.get(i).key) {
//                childAt.setChecked(true);
//            } else {
//                childAt.setChecked(false);
//            }
//
//            if (!TextUtils.isEmpty(installmentInfo.sex) && Integer.parseInt(installmentInfo.sex) != 0) {
//                if (Integer.parseInt(installmentInfo.sex) == dataList.get(i).key) {
//                    childAt.setChecked(true);
//                } else {
//                    childAt.setChecked(false);
//                }
//            }
//        }
//
//
//        sex_radio_group_upload_info.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                if (checkedId == R.id.men_radio) {
//                    installmentInfo.sex = String.valueOf(dataList.get(0).key);
//                } else if (checkedId == R.id.women_radio) {
//                    installmentInfo.sex = String.valueOf(dataList.get(1).key);
//                } else if (checkedId == R.id.secret_radio) {
//                    installmentInfo.sex = String.valueOf(dataList.get(2).key);
//                }
//                DebugLog.d(TAG, "onCheckedChanged() returned: " + installmentInfo.sex);
//            }
//        });
//    }
//
//    /**
//     * 设置产业spinner
//     */
//    private void setIndustrySpinner(ArrayList<InstallmentDataKeyValue> dataList) {
//        if (dataList == null) {
//            return;
//        }
//        InstallmentDataKeyValue demo = new InstallmentDataKeyValue();
//        demo.key = 1000;
//        demo.value = "请选择";
//        dataList.add(0, demo);
//        industry_upload_info.setAdapter(new UploadInfoSpinnerAdapter(dataList));
//        industry_upload_info.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                installmentInfo.industry = (String) adapterView.getItemAtPosition(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        setSpinnerCurrentItem(industry_upload_info, dataList, installmentInfo.industry);
//    }
//
//    /**
//     * 设置婚姻状况spinner
//     */
//    private void setMarriageStatusSpinner(ArrayList<InstallmentDataKeyValue> dataList) {
//        if (dataList == null) {
//            return;
//        }
//        InstallmentDataKeyValue demo = new InstallmentDataKeyValue();
//        demo.key = 1000;
//        demo.value = "请选择";
//        dataList.add(0, demo);
//        marriageStatus_upload_info.setAdapter(new UploadInfoSpinnerAdapter(dataList));
//        marriageStatus_upload_info.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                installmentInfo.marriageStatus = (String) adapterView.getItemAtPosition(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
//        setSpinnerCurrentItem(marriageStatus_upload_info, dataList, installmentInfo.marriageStatus);
//    }
//
//    /**
//     * 设置房产spinner
//     */
//    private void setHousePropertySpinner(ArrayList<InstallmentDataKeyValue> dataList) {
//        if (dataList == null) {
//            return;
//        }
//        InstallmentDataKeyValue demo = new InstallmentDataKeyValue();
//        demo.key = 1000;
//        demo.value = "请选择";
//        dataList.add(0, demo);
//        houseProperty_upload_info.setAdapter(new UploadInfoSpinnerAdapter(dataList));
//        houseProperty_upload_info.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                installmentInfo.houseProperty = (String) adapterView.getItemAtPosition(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        setSpinnerCurrentItem(houseProperty_upload_info, dataList, installmentInfo.houseProperty);
//    }
//
//    /**
//     * 设置Spinner选择的选项
//     */
//    public void setSpinnerCurrentItem(Spinner spinner, ArrayList<InstallmentDataKeyValue> dataList, String currentItem) {
//        if (TextUtils.isEmpty(currentItem)) {
//            spinner.setSelection(0);
//        } else {
//            if (dataList != null && !dataList.isEmpty()) {
//                for (int i = 0; i < dataList.size(); i++) {
//                    if (dataList.get(i).key == Integer.parseInt(currentItem)) {
//                        DebugLog.d(TAG, "setSpinnerCurrentItem() returned: " + currentItem + "----" + dataList.get(i).key);
//                        spinner.setSelection(i);
//                        break;
//                    }
//
//                }
//            }
//        }
//    }
//
//    private void setEditText() {
//        month_salary_upload_info.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                EditText editText = (EditText) v;
//                if (hasFocus) {
//                    editText.selectAll();
//                    editText.setCursorVisible(true);
//                }
//            }
//        });
//    }
//
//    /**
//     * 进入该fragment时获取字段
//     */
//    private void setInfo(InstallmentInfo installmentInfo) {
//
////        installmentInfo = JSONUtils.fromJson(response, new TypeToken<InstallmentInfo>() {
////        });
//        if (installmentInfo != null) {
//            setView(installmentInfo);
//            InstallmentData fields = installmentInfo.fields;
//            if (fields != null) {
//                setHousePropertySpinner(fields.houseProperty);
//                setMarriageStatusSpinner(fields.marriageStatus);
//                setIndustrySpinner(fields.industry);
//                setSexRadio(fields.sex);
//            }
//        }
//    }
//
//    /**
//     * 根据金融机构字段,动态添加描述条目
//     *
//     * @param finance
//     */
//    private void addFinanceView(LoanItemFinanceModel finance) {
//        bank_upload_info.setText(finance.title);
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        View view;
//        TextView item_text_left;
//        TextView item_text_right;
//        ArrayList<LoanKeyValueModel> infomation = finance.infomation;
//        for (LoanKeyValueModel model : infomation) {
//            view = inflater.inflate(R.layout.item_infomation_upload_info, null);
//            item_text_left = (TextView) view.findViewById(R.id.item_text_left);
//            item_text_right = (TextView) view.findViewById(R.id.item_text_right);
//            StringBuffer buffer = new StringBuffer(model.title);
//            if (buffer.length() > 4) {
//                buffer.insert(4, "\n");
//            }
//            item_text_left.setText(buffer.append(":"));
//            item_text_right.setText(model.value);
//            container_upload_info.addView(view);
//        }
//    }
//
//    private void setView(InstallmentInfo installmentInfo) {
//        city_upload_info.setText(installmentInfo.city);
//        brand_upload_info.setText(installmentInfo.brand);
//        price_upload_info.setText("¥  " + installmentInfo.price);
//        date_upload_info.setText(installmentInfo.orderTime);
//        buyer_name_upload_info.setText(installmentInfo.buyerName);
//        phone_number_upload_info.setText(installmentInfo.phoneNumber);
//        if (!TextUtils.isEmpty(installmentInfo.monthSalary) && !"0".equals(installmentInfo.monthSalary)) {
//            month_salary_upload_info.setText(installmentInfo.monthSalary);
//            month_salary_upload_info.selectAll();
//
//        }
//        LoanItemFinanceModel finance = installmentInfo.finance;
//        if (finance != null) {
//            addFinanceView(finance);
//        }
//        image1.setImageInfoURL(installmentInfo.info1, installmentInfo.info1Large);
//        image2.setImageInfoURL(installmentInfo.info2, installmentInfo.info2Large);
//        image3.setImageInfoURL(installmentInfo.info3, installmentInfo.info3Large);
//        image4.setImageInfoURL(installmentInfo.info4, installmentInfo.info4Large);
//        image5.setImageInfoURL(installmentInfo.info5, installmentInfo.info5Large);
//    }
//
//    @Override
//    public void chooseSuccess(final String filePath) {
//
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                String path1 = compressPhoto(filePath);
//                Message msg = Message.obtain();
//                msg.what = 200 + imageViewFlag;
//                Bundle bundle = new Bundle();
//                bundle.putString("imagePath", path1);
//                msg.setData(bundle);
//                mHandler.sendMessage(msg);
//            }
//        }.start();
//
//    }
//
//    /**
//     * 压缩图片
//     *
//     * @param path
//     * @return
//     */
//    private String compressPhoto(String path) {
//        try {
//            String path1 = "";
//            if (getActivity().getExternalCacheDir() != null) {
//                path1 = getActivity().getExternalCacheDir().getPath() + "/temp" + new Random().nextInt(100) + ".jpg";
//            }
//
////            Bitmap bitmap = ImageUtils.getBitmapByDecodeFile(path);
//            Bitmap bitmap = ImageUtils.compressAndGetImgBitmap(
//                    path,
//                    (int) (ScreenUtil.getScreenWidth(getActivity()) * 0.8),
//                    (int) (ScreenUtil.getScreenHeight(getActivity()) * 0.8)
//            );
////            DebugLog.i("text_bitmap", "DebugLog size: " + bitmap.getByteCount());
//            BitmapCompress.compressImageToSpecifySizeAndSaveFile(bitmap, 800, 800, path1, 86);
//            bitmap.recycle();
//            return path1;
//        } catch (Exception e) {
//            ToastHelper.showRedToast(getActivity(), "图片上传错误,请重新上传");
//            return null;
//        }
//    }
//
//
//    @Override
//    public boolean toCropPhoto() {
//        return false;
//    }
//
//    @Override
//    public String getCameraPath() {
//        Context context = getActivity().getApplicationContext();
//        return context.getExternalCacheDir().toString() + "/camera.png";
//    }
//
//    @Override
//    protected String getCropCacheFilePath() {
//        Context context = getActivity();
//        cropCachePath = String.valueOf(context.getExternalCacheDir()) + File.separator + "crop_" + System.currentTimeMillis() + ".png";
//        return cropCachePath;
//    }
//
//    /**
//     * 获取用户输入的信息
//     *
//     * @return
//     */
//    private boolean checkInfo() {
//        installmentInfo.info1 = image1.getInfoLargeURL();
//        installmentInfo.info2 = image2.getInfoLargeURL();
//        installmentInfo.info3 = image3.getInfoLargeURL();
//        installmentInfo.info4 = image4.getInfoLargeURL();
//        installmentInfo.info5 = image5.getInfoLargeURL();
//
//
//        installmentInfo.monthSalary = month_salary_upload_info.getText().toString();
//        if (image1.getIsUploading() || image2.getIsUploading() || image3.getIsUploading() || image4.getIsUploading() || image5.getIsUploading()) {
//            ToastHelper.showRedToast(getActivity(), "请等待图片上传");
//            return false;
//        }
//        if ("0".equals(installmentInfo.sex)) {
////            Toast.makeText(getActivity(), "请选择性别", Toast.LENGTH_SHORT).show();
//            ToastHelper.showRedToast(getActivity(), "请选择性别");
//            return false;
//        }
//        if (TextUtils.isEmpty(installmentInfo.monthSalary)) {
////            Toast.makeText(getActivity(), "月收入不能为空", Toast.LENGTH_SHORT).show();
//            ToastHelper.showRedToast(getActivity(), "月收入不能为空");
//
//            return false;
//        }
//        if (TextUtils.isEmpty(image1.getInfoLargeURL()) || TextUtils.isEmpty(image2.getInfoLargeURL())) {
////            Toast.makeText(getActivity(), "身份证必须上传", Toast.LENGTH_SHORT).show();
//            ToastHelper.showRedToast(getActivity(), "身份证必须上传");
//
//            return false;
//        }
//        if ("1000".equals(installmentInfo.houseProperty)) {
////            Toast.makeText(getActivity(), "请选择房产性质", Toast.LENGTH_SHORT).show();
//            ToastHelper.showRedToast(getActivity(), "请选择房产性质");
//
//            return false;
//        }
//        if ("1000".equals(installmentInfo.marriageStatus)) {
////            Toast.makeText(getActivity(), "请选择婚姻状况", Toast.LENGTH_SHORT).show();
//            ToastHelper.showRedToast(getActivity(), "请选择婚姻状况");
//
//            return false;
//        }
//        if ("1000".equals(installmentInfo.industry)) {
////            Toast.makeText(getActivity(), "请选择经营行业", Toast.LENGTH_SHORT).show();
//            ToastHelper.showRedToast(getActivity(), "请选择经营行业");
//
//            return false;
//        }
//
//
//        return true;
//    }
//
//    /**
//     * 上传填写的资料
//     */
//    private void uploadInfo() {
//        PayApiClient mPayApiClient = new PayApiClient();
//        mPayApiClient.uploadInstallmentInfo(orderID, installmentInfo, new DialogResponseHandler(getActivity()) {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
////                Toast.makeText(getActivity(), "联网失败,请检查网络再保存", Toast.LENGTH_SHORT).show();
//                ToastHelper.showRedToast(getActivity(), "联网失败,请检查网络再保存");
//            }
//
//            @Override
//            public void onSuccess(String response) {
//                EventUpLoadFile eventUpLoadFile = new EventUpLoadFile();
//                eventUpLoadFile.operateStatus = EventUpLoadFile.has_saved;
//                EventBus.getDefault().post(eventUpLoadFile);
//                getActivity().finish();
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.save_upload_info) {
//            if (checkInfo()) {
//                uploadInfo();
//            }
//        }
//    }
//
//    @Override
//    public void clickImageEdit(ImageUploadView view) {
//        if (view.getId() == R.id.image1) {
//            imageViewFlag = 10;
//        } else if (view.getId() == R.id.image2) {
//            imageViewFlag = 20;
//        } else if (view.getId() == R.id.image3) {
//            imageViewFlag = 30;
//        } else if (view.getId() == R.id.image4) {
//            imageViewFlag = 40;
//        } else if (view.getId() == R.id.image5) {
//            imageViewFlag = 50;
//        }
//
//        new PopupWindows(getActivity(), view);
//    }
//
//    @Override
//    public void clickImage(ImageUploadView view) {
//
//        if (view.getId() == R.id.image1) {
//            imageViewFlag = 10;
//        } else if (view.getId() == R.id.image2) {
//            imageViewFlag = 20;
//        } else if (view.getId() == R.id.image3) {
//            imageViewFlag = 30;
//        } else if (view.getId() == R.id.image4) {
//            imageViewFlag = 40;
//        } else if (view.getId() == R.id.image5) {
//            imageViewFlag = 50;
//        }
//
//        if ((view).getPhotoPreviewPrepared()) {
//            (view).openPhotoView(getActivity());
//        } else {
//            new PopupWindows(getActivity(), view);
//        }
//    }
//
//
//    public class PopupWindows extends PopupWindow {
//
//        public PopupWindows(Context mContext, View parent) {
//
//            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
//            view.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.fade_ins));
//            LinearLayout ll_popup = (LinearLayout) view
//                    .findViewById(R.id.ll_popup);
//            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.push_bottom_in_2));
//
//            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//            setFocusable(true);
//            setOutsideTouchable(true);
//            setContentView(view);
//            setBackgroundDrawable(new BitmapDrawable());
//            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//            update();
//
//            Button bt1 = (Button) view
//                    .findViewById(R.id.item_popupwindows_camera);
//            Button bt2 = (Button) view
//                    .findViewById(R.id.item_popupwindows_Photo);
//            Button bt3 = (Button) view
//                    .findViewById(R.id.item_popupwindows_cancel);
//
//            View space = view.findViewById(R.id.space_click);
//            space.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
//
//            bt1.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    getPicFromCamera();
//                    dismiss();
//                }
//            });
//            bt2.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//
//                    getPicFromContent();
//                    dismiss();
//                }
//            });
//            bt3.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
//
//        }
//    }
//
//}
