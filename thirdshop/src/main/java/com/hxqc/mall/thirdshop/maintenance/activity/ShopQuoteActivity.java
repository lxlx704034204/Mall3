package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.FourSAndQuickShopAdapterN;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ShopQuote;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ShopQuoteItem;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;

import java.util.ArrayList;


/**
 * @Author : 钟学东
 * @Since : 2016-04-19
 * FIXME
 * Todo 店铺报价
 */
public class ShopQuoteActivity extends NoBackActivity implements View.OnClickListener {


    private RecyclerView recyclerView;
    private Button m4SShopView; //4s报价
    //    private Button mQuickView; //快修店报价
    private ImageView mBrandThumbView;
    private TextView mBrandNameView;
    private TextView mSeriesNameView;
    private TextView mChangeCarView;
    private EditText mCarMileView;

    private FourSAndQuickShopAdapterN adapterN;
    private ActivitySwitcherMaintenance activitySwitcherMaintenance;
    private FourSAndQuickHelper fourSAndQuickHelper;

    private MyAuto myAuto;

    private RequestFailView mFailView;

    protected Toolbar toolbar;
    protected LinearLayout mServiceLayoutView;


    private static final String HintKey = "Hint";
    private static final int HintMessage = 1;
    private static final int DelayedSearchTime = 1500;//延迟提示时间

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == HintMessage) {
                String keyValue = msg.getData().getString(HintKey);
                if (!TextUtils.isEmpty(keyValue)) {
                    myAuto.drivingDistance = keyValue;
                    DebugLog.i(AutoInfoContants.LOG_J, myAuto.toString());
                } else {
                    myAuto.drivingDistance = "";
                }
                fourSAndQuickHelper.setMyAuto(myAuto);
                getDate();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_quote);
        activitySwitcherMaintenance = new ActivitySwitcherMaintenance();
        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();

        initView();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            if (bundle != null) {
                myAuto = bundle.getParcelable("myAuto");
                initMyAuto(myAuto);
                getDate();
            }
        }
        initHeaderAndFooter();
        initEvent();

    }

    private void initHeaderAndFooter() {
        TextView t1 = new TextView(this);
        t1.setTextColor(this.getResources().getColor(R.color.text_color_orange));
        t1.setTextSize(14);
        t1.setText("根据您选择的车型和累计行驶里程，系统自动为您推荐基础保养方案，您也可以根据车辆的实际情况自行选择其他保养项目。");
        t1.setPadding(DisplayTools.dip2px(this, 16), DisplayTools.dip2px(this, 10), DisplayTools.dip2px(this, 16), DisplayTools.dip2px(this, 10));
        adapterN.addHeaderView(t1);
    }

    private void initMyAuto(MyAuto myAuto) {
        if (myAuto != null) {
            fourSAndQuickHelper.setMyAuto(myAuto);
            mBrandNameView.setText(myAuto.series);
            mSeriesNameView.setText(myAuto.autoModel);
            DebugLog.i("TAG", "myAuto.drivingDistance  " + myAuto.drivingDistance);
            mCarMileView.setText(myAuto.drivingDistance);
            ImageUtil.setImage(this, mBrandThumbView, myAuto.brandThumb);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == AutoInfoContants.REQUEST_ADD_OR_EDIT && data != null){
//            myAutos = autoHelper.getAutoLocal(this, AutoHelper.AUTO_DETAIL_INFO);
//            myAuto = myAutos.get(0);
//
//            if(!T[ ]extUtils.isEmpty(myAuto.drivingDistance)) {
//                mMileView.setText(myAuto.drivingDistance);
//            }
//            getDate();
//            getSupportActionBar().setTitle(myAuto.series);
//        }else
        if (requestCode == AutoInfoContants.GET_AUTO_DATA) {
            if (data != null) {
                Bundle bundleExtra = data.getBundleExtra(ActivitySwitchBase.KEY_DATA);
                if (bundleExtra != null) {
                    myAuto = bundleExtra.getParcelable("myAuto");
                    initMyAuto(myAuto);
                }

            }
        }
    }

    private void getDate() {
        fourSAndQuickHelper.getItemsAllOverviewN(this, new FourSAndQuickHelper.ItemsAllOverviewNHandle() {
            @Override
            public void onSuccess(ArrayList<ShopQuote> shopQuotes) {
                boolean isNoCheck = true;
                recyclerView.setVisibility(View.VISIBLE);
                mFailView.setVisibility(View.GONE);

                for (ShopQuote shopQuote : shopQuotes) {
                    for (ShopQuoteItem shopQuoteItem : shopQuote.items) {
                        if (shopQuoteItem.choose == 1) {
                            isNoCheck = false;
                        }
                    }
                }
                if (isNoCheck) {
                    m4SShopView.setEnabled(false);
//                    mQuickView.setEnabled(false);
                } else {
                    m4SShopView.setEnabled(true);
//                    mQuickView.setEnabled(true);
                }


                recyclerView.setAdapter(adapterN);

                adapterN.setOnButtonSelected(new FourSAndQuickShopAdapterN.onButtonSelected() {
                    @Override
                    public void buttonEnabled() {
                        m4SShopView.setEnabled(true);
//                        mQuickView.setEnabled(true);
                    }

                    @Override
                    public void buttonNoEnabled() {
                        m4SShopView.setEnabled(false);
//                        mQuickView.setEnabled(false);
                    }
                });
            }

            @Override
            public void onFailure() {
                showFailView();
                m4SShopView.setEnabled(false);
//                mQuickView.setEnabled(false);
            }

            @Override
            public void onEmpty() {
                showEmpty();
                m4SShopView.setEnabled(false);
//                mQuickView.setEnabled(false);
            }

        });
    }

    private void initEvent() {

        m4SShopView.setOnClickListener(this);

        mChangeCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoHelper.getInstance().loginAction(ShopQuoteActivity.this, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
//                        ActivitySwitchBase.toAutoInfo(ShopQuoteActivity.this, "", AutoInfoContants.AUTO_DETAIL);
                        AutoInfoControl.getInstance().chooseAuto(ShopQuoteActivity.this, myAuto, "", AutoInfoContants.FLAG_ACTIVITY_MAINTAIN_SHOP_QUOTE, null);
                    }
                });
            }
        });

        mCarMileView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                toHintMessage(s.toString());
            }
        });

        mServiceLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopQuoteActivity.this, R.style.MaterialDialog);
                builder.setTitle("拨打电话").setMessage("400-1868-555").setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri uri = Uri.parse("tel:" + "400-1868-555");
                        intent.setData(uri);
                        ShopQuoteActivity.this.startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        });


    }

    /**
     * 延迟获取数据
     *
     * @param s
     */
    private void toHintMessage(String s) {
        handler.removeMessages(HintMessage);
        Message message = new Message();
        message.what = HintMessage;
        message.getData().putString(HintKey, s);
        handler.sendMessageDelayed(message, DelayedSearchTime);
    }

    /**
     * 格式化 上牌时间
     *
     * @param registerTime
     * @return
     */
    private String getRegisterTime(String registerTime) {

        if (TextUtils.isEmpty(registerTime)) {
            return "";
        } else {
            if (registerTime.substring(0, 4).equals("0000")) {
                return "";
            } else {
                if (registerTime.length() > 7) {
                    return registerTime.substring(0, registerTime.indexOf("-", registerTime.indexOf("-") + 1));
                } else {
                    return registerTime;
                }
            }
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBrandThumbView = (ImageView) findViewById(R.id.brandThumb);
        mBrandNameView = (TextView) findViewById(R.id.brandName);
        mSeriesNameView = (TextView) findViewById(R.id.seriesName);
        mChangeCarView = (TextView) findViewById(R.id.change_car);
        mChangeCarView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mCarMileView = (EditText) findViewById(R.id.et_car_mile);
        mServiceLayoutView = (LinearLayout) findViewById(R.id.service);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShopQuoteActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapterN = new FourSAndQuickShopAdapterN(ShopQuoteActivity.this);


        m4SShopView = (Button) findViewById(R.id.bt_4s);
//        mQuickView = (Button) findViewById(R.id.bt_quick_repair);
        m4SShopView.setEnabled(false);
//        mQuickView.setEnabled(false);

        mFailView = (RequestFailView) findViewById(R.id.fail_view);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_4s) {
            fourSAndQuickHelper.setShopQuotes(adapterN.returnShopQuotes());
            if (fourSAndQuickHelper.mustSelect4S()) {
                fourSAndQuickHelper.setFlag("1");
                activitySwitcherMaintenance.toMaintenanceList(this, myAuto.brandID, myAuto.seriesID, myAuto.autoModelID, myAuto.myAutoID, fourSAndQuickHelper.getGroupItem(), 0);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopQuoteActivity.this, R.style.MaterialDialog);
                builder.setMessage(fourSAndQuickHelper.get4SmustSelectName() + "为4S店保养必选项目").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
//                ToastHelper.showYellowToast(this,fourSAndQuickHelper.get4SmustSelectName()+"为必选");
            }

        }
//        else if (i == R.id.bt_quick_repair) {
//
//            fourSAndQuickHelper.setShopQuotes(adapterN.returnShopQuotes());
//
//            fourSAndQuickHelper.setFlag("2");
//            activitySwitcherMaintenance.toFourSAndQuickShopQuote(this);
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myAuto != null) {
            if (UserInfoHelper.getInstance().isLogin(this)) {
                AutoHelper.getInstance().editAutoDataLocal(ShopQuoteActivity.this, myAuto, AutoHelper.SWITCH_AUTO);
                AutoHelper.getInstance().editAutoDataLocal(ShopQuoteActivity.this, myAuto, AutoHelper.AUTO_LOCAL_INFO);
            } else {
                AutoHelper.getInstance().editAutoDataLocal(ShopQuoteActivity.this, myAuto, AutoHelper.AUTO_LOCAL_INFO);
//                AutoHelper.getInstance().editAutoDataLocal(ShopQuoteActivity.this, myAuto, AutoHelper.SWITCH_AUTO);
            }
            /*if (!TextUtils.isEmpty(myAuto.localAuto) && myAuto.localAuto.equals("1")) {
                AutoHelper.getInstance().editAutoDataLocal(ShopQuoteActivity.this, myAuto, AutoHelper.AUTO_LOCAL_INFO);
            }*/
        }
        fourSAndQuickHelper.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        fourSAndQuickHelper.setShopID("");
    }

//    @Override
//    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        int month = monthOfYear + 1;
//        String date = year + "-" + month;
//        myAuto.registerTime = date;
//        fourSAndQuickHelper.setMyAuto(myAuto);
//        getDate();
//    }

    //错误界面的显示
    private void showFailView() {
        recyclerView.setVisibility(View.GONE);
        mFailView.setVisibility(View.VISIBLE);
        mFailView.setEmptyDescription("网络连接失败");
        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    private void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        mFailView.setVisibility(View.VISIBLE);
        mFailView.setEmptyDescription("暂无数据");
        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
    }
}
