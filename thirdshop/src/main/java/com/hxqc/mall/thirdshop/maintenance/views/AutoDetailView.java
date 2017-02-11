package com.hxqc.mall.thirdshop.maintenance.views;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-16
 * FIXME
 * Todo 顶部汽车的详情
 */
public class AutoDetailView extends LinearLayout implements View.OnClickListener {
    /*完善了车辆信息的view*/
    private LinearLayout perfectView;
    private ImageView autoBrandIcon;//车辆的品牌图标
    private TextView modelsTv;//车型
    private TextView autoDrivenDistance;//车辆行驶里程
    private TextView nextMaintenance,//下次保养
            nextInsurance,//下次保险
            warranty,//质保期
            lastTime;//上次保养信息
    private EditText input_auto_driven_distance;
    private MyAuto myAuto = null;
    private ImageView line1, line2;
    public String shopID = "";
    public ArrayList<Brand> mBrands;

    public AutoDetailView(Context context) {
        this(context, null);
    }

    public AutoDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_auto_detail, this);
        initView();
        initEvent();
        addData(myAuto);
    }

    private void initView() {
        line1 = (ImageView) findViewById(R.id.line_one);
        line2 = (ImageView) findViewById(R.id.line_two);
        autoBrandIcon = (ImageView) findViewById(R.id.auto_brand_icon);
        modelsTv = (TextView) findViewById(R.id.models_tv);
        autoDrivenDistance = (TextView) findViewById(R.id.auto_driven_distance);
        nextMaintenance = (TextView) findViewById(R.id.next_maintenance);
        nextInsurance = (TextView) findViewById(R.id.next_insurance);
        lastTime = (TextView) findViewById(R.id.last_time_data);
        warranty = (TextView) findViewById(R.id.warranty);
        input_auto_driven_distance = (EditText) findViewById(R.id.input_driven_distance);
    }

    long time = 0;

    private void initEvent() {
        findViewById(R.id.to_my_auto).setOnClickListener(this);
        nextInsurance.setOnClickListener(this);
        nextMaintenance.setOnClickListener(this);


        input_auto_driven_distance.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    toCommit(input_auto_driven_distance.getText());
                    OtherUtil.closeSoftKeyBoard(getContext(), AutoDetailView.this);
                    input_auto_driven_distance.clearFocus();
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 隐藏光标
     */
    public void hideFocus() {
        OtherUtil.closeSoftKeyBoard(getContext(), AutoDetailView.this);
        input_auto_driven_distance.clearFocus();
    }

    private void toCommit(Editable s) {
        long l = System.currentTimeMillis();
        if (l - time > 500) {
            resetDistance(s);
            time = 0;
        } else {
            time = l;
        }
    }

    private void resetDistance(Editable s) {
        if (!TextUtils.isEmpty(myAuto.isDefault + ""))
            myAuto.isDefault = "20";
        myAuto.drivingDistance = s.toString();
        //提交修改后的数据
//        new AutoInfoClient().editAutoInfo(myAuto,
//                new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers,
//                                          String responseString, Throwable throwable) {
////                        ToastHelper.showRedToast(getContext(), "修改里程数失败");
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
////                        ToastHelper.showRedToast(getContext(), "成功修改里程数");
//                    }
//                });
    }

    public void onDestory(Context context) {
        if (UserInfoHelper.getInstance().isLogin(context)) {
            AutoHelper.getInstance().editAutoDataLocal(context, myAuto, AutoHelper.AUTO_LOCAL_INFO);
            AutoHelper.getInstance().editAutoDataLocal(context, myAuto, AutoHelper.SWITCH_AUTO);
        } else {
            AutoHelper.getInstance().editAutoDataLocal(context, myAuto, AutoHelper.AUTO_DETAIL_INFO);
            AutoHelper.getInstance().editAutoDataLocal(context, myAuto, AutoHelper.SWITCH_AUTO);
        }
    }

    /**
     * 填充数据
     *
     * @param myAuto
     */
    public void addData(MyAuto myAuto) {
        this.myAuto = myAuto;
        if (myAuto == null) {
            setVisibility(GONE);
            return;
        } else setVisibility(VISIBLE);

        if (!TextUtils.isEmpty(myAuto.brandThumb))
            ImageUtil.setImageSquare(getContext(), autoBrandIcon, myAuto.brandThumb);
//        Picasso.with(getContext()).load(myAuto.brandThumb)
//                .placeholder(R.drawable.pic_icon)
//                .error(R.drawable.pic_icon)
//                .into(autoBrandIcon);
        modelsTv.setText(myAuto.autoModel);
//        setAutoDrivenDistance(myAuto.drivingDistance);
        input_auto_driven_distance.setText(myAuto.drivingDistance + "");
        input_auto_driven_distance.removeTextChangedListener(drivenDistanceChangeListener);
        input_auto_driven_distance.addTextChangedListener(drivenDistanceChangeListener);
        if (myAuto.nextInsurance != 0) {
            nextInsurance.setVisibility(VISIBLE);
            setNextInsurance(myAuto.nextInsurance);
        } else nextInsurance.setVisibility(GONE);
        if (myAuto.nextMaintenanceDistance != 0) {
            nextMaintenance.setVisibility(VISIBLE);
            setNextMaintenance(myAuto.nextMaintenanceDistance);
        } else nextMaintenance.setVisibility(GONE);
        setLastTime(myAuto.lastMaintenanceDate, myAuto.lastMaintenanceDistance);
//        if (!TextUtils.isEmpty(myAuto.guaranteePeriod)) {
//            warranty.setVisibility(VISIBLE);
//            warranty.setText(getContext().getString(R.string.warranty) + myAuto.guaranteePeriod);
//        } else warranty.setVisibility(GONE);
//处理虚线
        if (warranty.getVisibility() == GONE && lastTime.getVisibility() == GONE)
            line1.setVisibility(GONE);
        else line1.setVisibility(VISIBLE);

        if (nextInsurance.getVisibility() == GONE && nextMaintenance.getVisibility() == GONE)
            line2.setVisibility(GONE);
        else line2.setVisibility(VISIBLE);
    }

    /**
     * 设置行驶里程
     *
     * @param distance
     */
    private void setAutoDrivenDistance(int distance) {
        autoDrivenDistance.setText(Html.fromHtml("<font color='#8a000000'>行驶里程</font>"
                + "<font color='#ff7043'>" + distance + "</font>" + "<font color='#8a000000'>公里</font>"));

    }

    /**
     * 设置上次检查的信息
     */
    private void setLastTime(String lastMaintenanceDate, long lastDistance) {
        lastTime.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(lastMaintenanceDate) && lastDistance != 0/*lastDistance > 0*/) {
            lastTime.setText("您上次保养时间" + lastMaintenanceDate + "," + "行驶里程" + lastDistance + "公里");
        } else if (TextUtils.isEmpty(lastMaintenanceDate) && lastDistance != 0 /*lastDistance > 0*/) {
            lastTime.setText("您上次保养行驶里程" + lastDistance + "公里");
        } else if (!TextUtils.isEmpty(lastMaintenanceDate) && lastDistance == 0 /*lastDistance == 0*/) {
            lastTime.setText("您上次保养时间" + lastMaintenanceDate);
        } else {
            lastTime.setVisibility(GONE);
        }
    }

    /**
     * 设置下次保养
     *
     * @param nextMaintenance
     */
    private void setNextMaintenance(long nextMaintenance) {
        this.nextMaintenance.setText(Html.fromHtml("<font color='#de000000'>距离下次保养</font>"
                + "<font color='#ff7043'>" + nextMaintenance + "</font>" + "<font color='#de000000'>公里</font>"));

    }

    /**
     * 下次保险
     *
     * @param nextInsurance
     */
    private void setNextInsurance(long nextInsurance) {
        this.nextInsurance.setText(Html.fromHtml("<font color='#de000000'>距离下次保险</font>"
                + "<font color='#ff7043'>" + nextInsurance + "</font>" + "<font color='#de000000'>天</font>"));

    }


    @Override
    public void onClick(View v) {
//        if (!UserInfoHelper.getInstance().isLogin(getContext())) {
//            ActivitySwitchAuthenticate.toLogin(getContext(), null, ActivitySwitchBase.ENTRANCE_ACCESSORYMAINTENANE);
//        } else {
        int id = v.getId();
        if (id == R.id.to_my_auto) {
            UserInfoHelper.getInstance().loginAction(getContext(), new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
//                    ActivitySwitchBase.toMaintainAutoInfo((Activity) getContext(), shopID, AutoInfoContants.FOURS_SHOP, mBrands);
                    AutoInfoControl.getInstance().chooseAuto(getContext(), myAuto, shopID, AutoInfoContants.FOURS_SHOP, mBrands);
                }
            });
        } else if (id == R.id.next_insurance) {
            ActivitySwitchBase.toH5Activity(getContext(), "保险详情", ApiUtil.AccountHostURL + "/App/Insurance/insurance.html");
        } else if (id == R.id.next_maintenance) {
            UserInfoHelper.getInstance().loginAction(getContext(), new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    ActivitySwitchAutoInfo.toRepairRecord(getContext(), myAuto);
                }
            });
//            ActivitySwitcherMaintenance.toMaintenanceRecord(getContext(), myAuto.myAutoID, myAuto.VIN);
            ActivitySwitchAutoInfo.toRepairRecord(getContext(), myAuto);
        }
//        }
    }

    private DrivenDistanceChangeCallbck drivenDistanceChangeCallbck;

    public interface DrivenDistanceChangeCallbck {
        void drivenDistanceChange(Editable s);
    }

    public void setdrivenDistanceChangeListener(DrivenDistanceChangeCallbck l) {
        this.drivenDistanceChangeCallbck = l;
    }

    private String driverDis = "";
    private TextWatcher drivenDistanceChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            driverDis = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (drivenDistanceChangeCallbck != null) {
                if (s != null && !driverDis.equals(s.toString()))
                    drivenDistanceChangeCallbck.drivenDistanceChange(s);
            }
        }
    };


}
