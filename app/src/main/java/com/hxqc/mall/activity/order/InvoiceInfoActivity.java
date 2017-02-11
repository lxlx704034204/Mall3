package com.hxqc.mall.activity.order;

import android.os.Bundle;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.model.InvoiceModel;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import hxqc.mall.R;

/**
 * liaoguilong
 * 发票信息（订单详情）
 * 2016年7月20日 15:24:51
 */
public class InvoiceInfoActivity extends BackActivity {

    private InvoiceModel mInvoiceModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_background);
        mInvoiceModel=getIntent().getBundleExtra(ActivitySwitcher.KEY_DATA).getParcelable(ActivitySwitcher.INVOICEINFO);
        initView();
    }

    private void initView() {
        if(mInvoiceModel ==null)
            return;
        if(mInvoiceModel.invoiceType.equals("0")) //普通发票
        {
            setContentView(R.layout.activity_invoice_info_normal);
            TextView mInvoiceTitleTextView= (TextView) findViewById(R.id.invoice_info_normal_invoiceTitleText);
            mInvoiceTitleTextView.setText(mInvoiceModel.invoiceTitleText); //抬头
            TextView mInvoiceContentTextView= (TextView) findViewById(R.id.invoice_info_normal_invoiceContentText);
            mInvoiceContentTextView.setText(mInvoiceModel.invoiceContentText); //内容
            TextView mReceivableserView= (TextView) findViewById(R.id.invoice_info_normal_receivableser);
            mReceivableserView.setText(mInvoiceModel.receivableser); //收票人
            TextView mReceivablesPhoneView= (TextView) findViewById(R.id.invoice_info_normal_receivablesPhone);
            mReceivablesPhoneView.setText(mInvoiceModel.receivablesPhone); //收票人电话
            TextView mReceivablesAddressView= (TextView) findViewById(R.id.invoice_info_normal_receivablesAddress);
            mReceivablesAddressView.setText(mInvoiceModel.receivablesAddress); //收票人地址
            TextView mInvoiceCodeView= (TextView) findViewById(R.id.invoice_info_normal_invoiceCode);
            mInvoiceCodeView.setText(mInvoiceModel.invoiceCode); //票号
            TextView mExpressNameView= (TextView) findViewById(R.id.invoice_info_normal_expressName);
            mExpressNameView.setText(mInvoiceModel.expressName);//快递公司;
            TextView mExpressCodeView= (TextView) findViewById(R.id.invoice_info_normal_expressCode);
            mExpressCodeView.setText(mInvoiceModel.expressCode); //快递单号

        }else if(mInvoiceModel.invoiceType.equals("1")) //增值税发票
        {
            setContentView(R.layout.activity_invoice_info_appreciation);
            TextView mCompanyNameView= (TextView) findViewById(R.id.invoice_info_appreciation_companyName);
            mCompanyNameView.setText(mInvoiceModel.invoiceContentText);
            TextView mIdentityCodeView= (TextView) findViewById(R.id.invoice_info_appreciation_identityCode);
            mIdentityCodeView.setText(mInvoiceModel.identityCode);
            TextView mRegistAddressView= (TextView) findViewById(R.id.invoice_info_appreciation_registAddress);
            mRegistAddressView.setText(mInvoiceModel.registAddress);
            TextView mRegistPhoneView= (TextView) findViewById(R.id.invoice_info_appreciation_registPhone);
            mRegistPhoneView.setText(mInvoiceModel.registPhone);
            TextView mRegistBankView= (TextView) findViewById(R.id.invoice_info_appreciation_registBank);
            mRegistBankView.setText(mInvoiceModel.registBank);
            TextView mRegistBankNoView= (TextView) findViewById(R.id.invoice_info_appreciation_registBankNo);
            mRegistBankNoView.setText(mInvoiceModel.registBankNo);
            TextView mInvoiceContentTextView= (TextView) findViewById(R.id.invoice_info_appreciation_invoiceContentText);
            mInvoiceContentTextView.setText(mInvoiceModel.invoiceContentText);
            TextView mReceivableserView= (TextView) findViewById(R.id.invoice_info_appreciation_receivableser);
            mReceivableserView.setText(mInvoiceModel.receivableser); //收票人
            TextView mReceivablesPhoneView= (TextView) findViewById(R.id.invoice_info_appreciation_receivablesPhone);
            mReceivablesPhoneView.setText(mInvoiceModel.receivablesPhone); //收票人电话
            TextView mReceivablesAddressView= (TextView) findViewById(R.id.invoice_info_appreciation_receivablesAddress);
            mReceivablesAddressView.setText(mInvoiceModel.receivablesAddress); //收票人地址
            TextView mInvoiceCodeView= (TextView) findViewById(R.id.invoice_info_appreciation_invoiceCode);
            mInvoiceCodeView.setText(mInvoiceModel.invoiceCode); //票号
            TextView mExpressNameView= (TextView) findViewById(R.id.invoice_info_appreciation_expressName);
            mExpressNameView.setText(mInvoiceModel.expressName);//快递公司;
            TextView mExpressCodeView= (TextView) findViewById(R.id.invoice_info_appreciation_expressCode);
            mExpressCodeView.setText(mInvoiceModel.expressCode); //快递单号
        }
    }

}
