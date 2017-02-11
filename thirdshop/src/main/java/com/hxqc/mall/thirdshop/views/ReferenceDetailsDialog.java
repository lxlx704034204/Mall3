package com.hxqc.mall.thirdshop.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.PriceInfo;

/**
 * Author:liaoguilong
 * Date 2015-12-02
 * FIXME
 * Todo 参考总价dialog
 */
public class ReferenceDetailsDialog extends Dialog implements View.OnClickListener {

    private PriceInfo mPriceInfo;
    private RelativeLayout mContentView;
    private TextView tv_itemPrice, tv_purchaseTax, tv_travelTax, tv_compulsoryInsurance, tv_plateFare, tv_totalPrice;
    private TextView tv_price_info;
    private boolean fromNewCar = false;

    public ReferenceDetailsDialog(Context context) {
        super(context, R.style.MaterialDialog);
        InitView();
    }

    public ReferenceDetailsDialog(Context context, PriceInfo mPriceInfo) {
        super(context, R.style.MaterialDialog);
        this.mPriceInfo = mPriceInfo;
        InitView();
        BindData(mPriceInfo);
    }

    public ReferenceDetailsDialog(Context context, PriceInfo mPriceInfo, boolean fromNewCar) {
        super(context, R.style.MaterialDialog);
        this.mPriceInfo = mPriceInfo;
        this.fromNewCar = fromNewCar;
        InitView();
        BindData(mPriceInfo);
    }


    public void InitView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.t_dialog_reference_details);
        mContentView = (RelativeLayout) findViewById(R.id.content);
        mContentView.setOnClickListener(this);
        tv_itemPrice = (TextView) findViewById(R.id.dialog_reference_details_barecarprice); //裸车
        tv_price_info = (TextView) findViewById(R.id.price_info);
        tv_purchaseTax = (TextView) findViewById(R.id.dialog_reference_details_traveltax); //购置
        tv_travelTax = (TextView) findViewById(R.id.dialog_reference_details_travelTax); //车船
//        tv_commercialInsurance= (TextView) findViewById(R.id.dialog_reference_details_businessrisks); //商业
        tv_compulsoryInsurance = (TextView) findViewById(R.id.dialog_reference_details_strongrisk); //交强
//        tv_insuranceCoupon=(TextView) findViewById(R.id.dialog_reference_details_insurancebenefits); //保险优惠
        tv_plateFare = (TextView) findViewById(R.id.dialog_reference_details_plateFare); //上牌费
        tv_totalPrice = (TextView) findViewById(R.id.dialog_reference_details_allprice); //总价

    }

    private void BindData(PriceInfo mPriceInfo) {
        tv_itemPrice.setText(OtherUtil.amountFormat(mPriceInfo.itemPrice, true));
        tv_purchaseTax.setText(OtherUtil.amountFormat(mPriceInfo.purchaseTax, true));
        tv_travelTax.setText(OtherUtil.amountFormat(mPriceInfo.travelTax, true));
       /* tv_commercialInsurance.setText(OtherUtil.amountFormat(mPriceInfo.commercialInsurance));*/
        tv_compulsoryInsurance.setText(OtherUtil.amountFormat(mPriceInfo.compulsoryInsurance, true));
    /*    tv_insuranceCoupon.setText(OtherUtil.amountFormat(mPriceInfo.insuranceCoupon));*/
        tv_plateFare.setText(OtherUtil.amountFormat(mPriceInfo.plateFare, true));
        tv_totalPrice.setText(OtherUtil.amountFormat(mPriceInfo.getTotalPrice(), true));
        if (fromNewCar) {
            tv_price_info.setText("经销商参考价");
            tv_itemPrice.setText(OtherUtil.amountFormat(mPriceInfo.itemPrice, true) + "起");
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
