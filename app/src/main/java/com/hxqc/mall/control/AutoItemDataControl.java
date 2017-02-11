package com.hxqc.mall.control;

import android.content.Context;

import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.Error;
import com.hxqc.mall.core.model.OrderPayRequest;
import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.model.loan.LoanItemFinanceModel;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * Author: HuJunJie
 * Date: 2015-04-20
 * FIXME
 * Todo
 */
public class AutoItemDataControl {
    protected static volatile AutoItemDataControl ourInstance;
    public OrderPayRequest mOrderPayRequest;//生成订单
    ItemDetailListenerConfig mConfig;//配置
    FactorChangeListener mFactorChangeListener;//条件改变
    AutoDetail autoDetail;
    NewAutoClient newAutoClient;
    AutoPackage mChooseAutoPackage;//选择的套餐
    LoanItemFinanceModel mChooseLoanFinance;//选择的分期金融
    ArrayList< LoanItemFinanceModel > financeModels;//分期银行
    ItemDetailListener itemDetailListener;

    {
        newAutoClient = new NewAutoClient();
        mOrderPayRequest = new OrderPayRequest();
    }

    protected AutoItemDataControl() {

    }

    public static AutoItemDataControl getInstance() {
        if (ourInstance == null) {
            synchronized (AutoItemDataControl.class) {
                if (ourInstance == null) {
                    ourInstance = new AutoItemDataControl();
                }
            }
        }
        return ourInstance;
    }

    public synchronized void destroy() {
        newAutoClient = null;
        mOrderPayRequest = null;
        autoDetail = null;
        mConfig = null;
        mFactorChangeListener = null;
        mChooseAutoPackage = null;
        mChooseLoanFinance = null;
        financeModels = null;
        itemDetailListener = null;
        ourInstance = null;
        AutoPackage.clearCustomChooseAccessory();
        AutoPackage.clearTempChooseAccessory();
    }

    public ArrayList< LoanItemFinanceModel > getFinanceModels() {
        return financeModels;
    }

    public void setFinanceModels(ArrayList< LoanItemFinanceModel > financeModels) {
        this.financeModels = financeModels;
    }

    public LoanItemFinanceModel getChooseLoanFinance() {
        return mChooseLoanFinance;
    }

    public void setChooseLoanFinance(LoanItemFinanceModel chooseLoanFinance) {
        this.mChooseLoanFinance = chooseLoanFinance;
        if (chooseLoanFinance != null) {
            mOrderPayRequest.financeID = chooseLoanFinance.financeID;
        } else {
            mOrderPayRequest.financeID = null;
        }

    }

    /**
     * 条件改变
     *
     * @param mFactorChangeListener
     */
    public void setFactorChangeListener(FactorChangeListener mFactorChangeListener) {
        this.mFactorChangeListener = mFactorChangeListener;
    }

    public ItemDetailListenerConfig getConfig() {
        return mConfig;
    }

    public void setConfig(ItemDetailListenerConfig mConfig) {
        this.mConfig = mConfig;
    }

    public void autoDetailAction(final ItemDetailListener listener) {
        this.itemDetailListener = listener;
        if (autoDetail != null) {
            listener.onItemDetailSuccess(autoDetail);
            return;
        }
        requestServer();
    }

    /**
     * 获取车辆详情
     */
    public void requestServer() {
        mOrderPayRequest.setItem(mConfig.getItemID(), mConfig.getItemType());
        newAutoClient.autoItemDetail(mConfig.getContext(), mConfig.getItemID(), mConfig.getItemType(), mConfig.getItemCategory(),
                new LoadingAnimResponseHandler(mConfig.getContext()) {
                    @Override
                    public void onSuccess(String response) {
                        autoDetail = JSONUtils.fromJson(response, AutoDetail.class);
                        if (autoDetail == null) return;
                        itemDetailListener.onItemDetailSuccess(autoDetail);
//                        getInsuranceCompany();
                        if (mFactorChangeListener != null) {
                            mFactorChangeListener.factorChange(getAllCost());
                        }
                    }


                    @Override
                    public void onOtherFailure(int statusCode, Header[] headers, String responseString, Error throwable) {
                        super.onOtherFailure(statusCode, headers, responseString, throwable);
                        itemDetailListener.onItemDetailFailure(throwable);
                    }
                });
    }


    /**
     * 总费用
     */
    public float getAllCost() {
        float allCost = 0;
        if (getOrderPayRequest().isLicense()) {
            //上牌
            allCost += getOtherAllCost();
        }
        if (mChooseAutoPackage != null) {
            //套餐
            allCost += mChooseAutoPackage.getAmount();
        }
        return allCost + autoDetail.getItemPrice();
    }

    /**
     * 上牌总计
     */
    public double getOtherAllCost() {
        return autoDetail.getFare().getAllCost();
    }


    /**
     * 是否上牌
     *
     * @param isAddLicense
     */
    public void setIsAddLicense(boolean isAddLicense) {
        getOrderPayRequest().setIsLicense(isAddLicense);
        if (mFactorChangeListener != null) {
            mFactorChangeListener.factorChange(getAllCost());
        }
    }

    public AutoDetail getAutoDetail() {
        return autoDetail;
    }

    /**
     * 已选择套餐
     */
    public AutoPackage getCheckAutoPackage() {
        return mChooseAutoPackage;
    }

    /**
     * 设置选择套餐
     */
    public void setCheckAutoPackage(AutoPackage checkAutoPackage) {
        this.mChooseAutoPackage = checkAutoPackage;
        if (checkAutoPackage != null) {
            getOrderPayRequest().setPackageID(checkAutoPackage.packageID);
            getOrderPayRequest().setAccessoryID(checkAutoPackage.getAccessoryString());
        } else {
            AutoPackage.clearCustomChooseAccessory();
            getOrderPayRequest().setPackageID(null);
            getOrderPayRequest().setAccessoryID(null);
        }
        if (mFactorChangeListener != null) {
            mFactorChangeListener.factorChange(getAllCost());
        }
    }

    public ArrayList< AutoPackage > getAutoPackages() {
        return autoDetail.getAutoPackages();
    }

    /**
     * 付款类型
     */
    public void setPaymentType(String paymentType) {
        getOrderPayRequest().setPaymentType(paymentType);
    }

    public synchronized OrderPayRequest getOrderPayRequest() {
        if (mOrderPayRequest == null) {
            mOrderPayRequest = new OrderPayRequest();
        }
        return mOrderPayRequest;
    }

    //汽车详情配置
    public interface ItemDetailListenerConfig {
        Context getContext();

        String getItemID();

        String getItemType();

        int getItemCategory();
    }

    //商品详情
    public interface ItemDetailListener {
        void onItemDetailSuccess(AutoDetail autoDetail);

        void onItemDetailFailure(Error error);
    }

    //条件改变
    public interface FactorChangeListener {
        void factorChange(double cost);
    }


}
