package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoSPControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.NoCancelDialog;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.ShopProFileWebActivity;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;

/**
 * liaoguilong
 * Created by CPR113 on 2016/4/1.
 * 店铺头部栏目View
 */
public class ShopDetailsHeadView extends LinearLayout implements View.OnClickListener, ShopDetailsTabView.TabChick {

    /**
     * //0,首页
     * //1,车型报价
     * //2,维修保养
     * //3,修车预约
     * //4,用品销售
     * /5,促销信息
     * //6,紧急救援
     * //7,整车质保
     * //8,限时特价车
     * //7,分期购车
     */
    public final static int TAB_SY = 0;
    public final static int TAB_CXBJ = 1;
    public final static int TAB_WXBY = 2;
    public final static int TAB_XCYY = 3;
    public final static int TAB_YPXS = 4;
    public final static int TAB_CXXX = 5;
    public final static int TAB_JJJY = 6;
    public final static int TAB_ZCZB = 7;
    public final static int TAB_XSTJC = 8;
    public final static int TAB_FQGC = 9;
    private RelativeLayout mColumnView; //栏目项
    private TextView mShopNameView, //名称
            mShopAddressView, //地址
            mShopTel,//电话
            mServiceHotline,//售后电话
            mColumnTextView,  //栏目view
            mCompanyProfileView,  //公司介绍栏
            mCompanyAddressView; //地址栏
    private ImageView mShopLogoView; //logo
    private ShopDetailsTabView mPopShopDetailsTabView, mHomeShopDetailsTabView; //tab菜单
    private PopupWindow mPopupWindow;
    private int TAB_CURRENT = -1; //当前tab 位置
    // 0，显示栏目项，隐藏TAB,
    // 1，隐藏栏目项，显示TAB
    private int MODE_TYPE = 0; //默认0，
    private ShopInfo mShopInfo;//

    private MyAuto mMyAuto;

    public ShopDetailsHeadView(Context context) {
        this(context, null);
    }

    public ShopDetailsHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShopDetailsHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_activity_shop_details_head, this);
        initView();
    }

    public void initView() {
        mShopLogoView = (ImageView) findViewById(R.id.activity_shopdetails_image); //logo
        mShopNameView = (TextView) findViewById(R.id.activity_shopdetails_shopName); //店铺name
        mShopAddressView = (TextView) findViewById(R.id.activity_shopdetails_shopAddress); //店铺详细地址
        mShopTel = (TextView) findViewById(R.id.activity_shopdetails_shopTel);
        mServiceHotline = (TextView) findViewById(R.id.activity_shopdetails_serviceHotline);

        mColumnView = (RelativeLayout) findViewById(R.id.shop_details_company_column); //栏目项
        mColumnTextView = (TextView) findViewById(R.id.shop_details_company_column_textview); //栏目View
        mCompanyProfileView = (TextView) findViewById(R.id.shop_details_company_profile); //公司介绍栏
        mCompanyAddressView = (TextView) findViewById(R.id.shop_details_company_address); //店铺地址栏

        mCompanyProfileView.setOnClickListener(this);
        mCompanyAddressView.setOnClickListener(this);
        mColumnView.setOnClickListener(this);


        mPopShopDetailsTabView = new ShopDetailsTabView(getContext());
        mPopShopDetailsTabView.setTabChick(this);
        mHomeShopDetailsTabView = (ShopDetailsTabView) findViewById(R.id.t_activity_shop_details_tabview);
        mHomeShopDetailsTabView.setTabChick(this);
        setMODE_TYPE(MODE_TYPE);
    }

    public void setMODE_TYPE(int MODE_TYPE) {
        if (MODE_TYPE == 0) {
            mColumnView.setVisibility(View.VISIBLE);
            mHomeShopDetailsTabView.setVisibility(View.GONE);
        } else {
            mColumnView.setVisibility(View.GONE);
            mHomeShopDetailsTabView.setVisibility(View.VISIBLE);
        }
        this.MODE_TYPE = MODE_TYPE;
    }

    /**
     * 设置TAB选中
     *
     * @param tabIndex TAB_SY,首页
     *                 TAB_CXBJ,车型报价
     *                 TAB_WXBY,维修保养
     *                 TAB_XCYY,修车预约
     *                 TAB_YPXS,用品销售
     *                 TAB_CXXX,促销信息
     *                 TAB_JJJY,紧急救援
     *                 TAB_ZCZB,整车质保
     */
    public void setTabCheck(int tabIndex) {
        TAB_CURRENT = tabIndex;
        if (tabIndex == TAB_SY) {
            mHomeShopDetailsTabView.check(mHomeShopDetailsTabView.grp1);
            mPopShopDetailsTabView.check(mPopShopDetailsTabView.grp1);
        } else if (tabIndex == TAB_CXBJ) {
            mHomeShopDetailsTabView.check(mHomeShopDetailsTabView.grp2);
            mPopShopDetailsTabView.check(mPopShopDetailsTabView.grp2);
        } else if (tabIndex == TAB_WXBY) {
            mHomeShopDetailsTabView.check(mHomeShopDetailsTabView.grp3);
            mPopShopDetailsTabView.check(mPopShopDetailsTabView.grp3);
        } else if (tabIndex == TAB_XCYY) {
            mHomeShopDetailsTabView.check(mHomeShopDetailsTabView.grp4);
            mPopShopDetailsTabView.check(mPopShopDetailsTabView.grp4);
        } else if (tabIndex == TAB_YPXS) {
            mHomeShopDetailsTabView.check(mHomeShopDetailsTabView.grp8);
            mPopShopDetailsTabView.check(mPopShopDetailsTabView.grp8);
        } else if (tabIndex == TAB_CXXX) {
            mHomeShopDetailsTabView.check(mHomeShopDetailsTabView.grp6);
            mPopShopDetailsTabView.check(mPopShopDetailsTabView.grp6);
        } else if (tabIndex == TAB_JJJY) {
            mHomeShopDetailsTabView.check(mHomeShopDetailsTabView.grp7);
            mPopShopDetailsTabView.check(mPopShopDetailsTabView.grp7);
        } else if (tabIndex == TAB_XSTJC) {
            mHomeShopDetailsTabView.check(mHomeShopDetailsTabView.grp5);
            mPopShopDetailsTabView.check(mPopShopDetailsTabView.grp5);
        } else if (tabIndex == TAB_FQGC) {
            mHomeShopDetailsTabView.check(mHomeShopDetailsTabView.grp9);
            mPopShopDetailsTabView.check(mPopShopDetailsTabView.grp9);
        }
    }

    /**
     * 清理选中TAB
     */
    public void cleanTabCheck() {
        mHomeShopDetailsTabView.cleanTabCheck();
        mPopShopDetailsTabView.cleanTabCheck();
    }

    /**
     * 绑定头部数据
     *
     * @param mShopInfo
     */
    public void bindData(ShopInfo mShopInfo) {
        if (mShopInfo == null)
            return;
        this.mShopInfo = mShopInfo;
        mShopNameView.setText(mShopInfo.shopName);
        mShopAddressView.setText(String.format("地址： %s", mShopInfo.getShopLocation().address));
        mServiceHotline.setText(String.format("售后电话： %s", mShopInfo.serviceHotline));
        mShopTel.setText(String.format("咨询电话： %s", mShopInfo.shopTel));

        mCompanyAddressView.setTag(mShopInfo.getShopLocation());
        Bundle bundle = new Bundle();
        bundle.putString(ShopProFileWebActivity.TITLE, "公司介绍");
        bundle.putString(ShopProFileWebActivity.URL, mShopInfo.shopInstruction);
        bundle.putString(ShopProFileWebActivity.TEL, mShopInfo.shopTel);
        mCompanyProfileView.setTag(bundle);
        ImageUtil.setImage(getContext(), mShopLogoView, mShopInfo.shopLogoThumb);
    }

    public void showTab() {
        mPopupWindow = new PopupWindow(mPopShopDetailsTabView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.argb(0, 0, 0, 0));
        mPopupWindow.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        mPopupWindow.setAnimationStyle(R.style.my_tab_popwindow_anim_style);
        // 在底部显示
        mPopupWindow.showAsDropDown(findViewById(R.id.shop_details_company_column), 0, 0);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.shop_details_company_profile) {
            if (v.getTag() == null) return;
            ActivitySwitcherThirdPartShop.toShopProFileH5(v.getContext(), (Bundle) v.getTag());
        } else if (v.getId() == R.id.shop_details_company_address) {
            if (v.getTag() == null) return;
            PickupPointT msShopLocation = (PickupPointT) v.getTag();
            ActivitySwitcherThirdPartShop.toAMapNvai(v.getContext(), 0, msShopLocation);
        } else if (v.getId() == R.id.shop_details_company_column) {
            showTab();
        }
    }


    /**
     * 打电话的应用
     *
     * @param sellerTel
     */
    private void showCallDialog(final String sellerTel) {
        new AlertDialog.Builder(getContext(), R.style.MaterialDialog)
                .setTitle(getContext().getString(R.string.call_phone))
                .setMessage(sellerTel)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OtherUtil.callPhone(getContext(), sellerTel);
                            }
                        }
                )
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create().show();
    }


    //首页
    @Override
    public void toHomePage(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        ActivitySwitcherThirdPartShop.toShopHome(mShopInfo.shopID, v.getContext());
    }

    // 车型报价
    @Override
    public void toModelsQuote(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        ActivitySwitcherThirdPartShop.toModelsOffer(mShopInfo.shopID, v.getContext());
    }

    //用品销售
    @Override
    public void toAccessorySale(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        if (TAB_CURRENT != TAB_YPXS)
            // ActivitySwitcherAccessory4S.toAccessorySaleFrom4S(v.getContext(), mShopInfo.shopID);
            new NoCancelDialog(v.getContext(), "该板块正在筹备中，敬请期待") {
                @Override
                protected void doNext() {

                }
            }.show();
    }

    //促销信息
    @Override
    public void toSalesP(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        ActivitySwitcherThirdPartShop.toShopPromotionList(mShopInfo.shopID, v.getContext());
    }

    //维修保养
    @Override
    public void toMaintenance(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        ActivitySwitcherThirdPartShop.toMaintenanHome(mShopInfo.shopID, v.getContext());
    }

    //紧急救援
    @Override
    public void toRescue(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        if (mShopInfo.rescueTel != null && !TextUtils.isEmpty(mShopInfo.rescueTel))
            showCallDialog(mShopInfo.rescueTel);
        else
            ToastHelper.showYellowToast(getContext(), "门店无紧急救援号码！");
    }

//    //整车自保
//    @Override
//    public void toQualityGuarantee(View v) {
//        if (mPopupWindow != null && mPopupWindow.isShowing())
//            mPopupWindow.dismiss();
//        ActivitySwitcherThirdPartShop.toQualityGuaranteeActivity(getContext());
//    }

    //修车预约
    @Override
    public void toReserveMaintain(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
//        ActivitySwitchBase.toReserveMaintain(getContext(), null, null);
        if (mShopInfo != null) {
            int appointmentFlag = AutoSPControl.getAppointmentFlag(getContext());
            DebugLog.i(AutoInfoContants.LOG_J, "appointmentFlag:" + appointmentFlag);
            AutoSPControl.saveAppointmentInfo(getContext(), mShopInfo.shopID, mShopInfo.shopTitle, "10");
            if (appointmentFlag == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S) {
                AutoInfoControl.getInstance().toActivityInter(getContext(), AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S, null);
            } else {
                if (mMyAuto != null) {
                    ActivitySwitchAutoInfo.toReserveMaintainAndHeadActivity(getContext(), mMyAuto, mShopInfo.shopID, mShopInfo.shopTitle);
                } else {
                    ActivitySwitchAutoInfo.toReserveMaintainAndHeadActivity(getContext(), null, mShopInfo.shopID, mShopInfo.shopTitle);
                }
            }
        }
    }

    //限时特价车
    @Override
    public void toSeckill(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        ActivitySwitcherThirdPartShop.toShopFlashSaleList(mShopInfo.shopID, v.getContext());
    }

    @Override
    public void toInstallmentBuyCar(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        ActivitySwitcherThirdPartShop.toInstallmentBuyCar(mShopInfo.shopID, v.getContext());
    }

    /**
     * 设置车辆信息
     *
     * @param mMyAuto
     */
    public void setMyAuto(MyAuto mMyAuto) {
        this.mMyAuto = mMyAuto;
    }
}
