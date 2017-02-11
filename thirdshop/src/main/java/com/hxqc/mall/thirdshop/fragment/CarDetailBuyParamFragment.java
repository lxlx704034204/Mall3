package com.hxqc.mall.thirdshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.order.BaseOrder;
import com.hxqc.mall.core.views.Order.OrderDescription;
import com.hxqc.mall.core.views.autotext.AutofitTextView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.PurchaseArg;
import com.hxqc.mall.thirdshop.model.PurchaseArgDetail;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.CarDetailGroupView;
import com.hxqc.util.DebugLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Function: 特卖车辆详情页Fragment
 *
 * @author 袁秉勇
 * @since 2016年05月04日
 */
public class CarDetailBuyParamFragment extends FunctionFragment {
    private final static String TAG = CarDetailBuyParamFragment.class.getSimpleName();
    private Context mContext;

    private OrderDescription mOrderDescriptionView;
    private CarDetailGroupView mBuyTypeView;
    private CarDetailGroupView mShopInsuranceView;
    private CarDetailGroupView mShopDecorationView;

    private AutofitTextView mAdvancePriceView;
    private Button mToBuyView;

    private HashMap<String, String> hashMap = new HashMap<>();


    public void setHashMap(HashMap< String, String > hashMap) {
        this.hashMap = hashMap;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_detail, container);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mOrderDescriptionView = (OrderDescription) view.findViewById(R.id.order_description);
        mBuyTypeView = (CarDetailGroupView) view.findViewById(R.id.buy_type);
        mShopInsuranceView = (CarDetailGroupView) view.findViewById(R.id.shop_insurance);
        mShopDecorationView = (CarDetailGroupView) view.findViewById(R.id.shop_decoration);

        mAdvancePriceView = (AutofitTextView) view.findViewById(R.id.auto_cost);
        mToBuyView = (Button) view.findViewById(R.id.to_pay_order);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void initData(BaseOrder baseOrder, String price, final String shopTel) {
        mOrderDescriptionView.initOrderDescription(mContext, baseOrder, false);

        mAdvancePriceView.setText(formatPrice(price));

        mToBuyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toConfirmSpecialCarOrder(mContext, hashMap, shopTel);
            }
        });
    }


    private String formatPrice(String price) {
        try {
            double num = Float.valueOf(price);
            if (num > 10000) {
                num = num / 10000.00;
                DecimalFormat df = new DecimalFormat("#.00");
                return "￥" + df.format(num) + "万";
            } else {
                return "￥" + price + "元";
            }
        } catch (Exception e) {
            return price;
        }
    }


    /** 初始化Fragment数据 **/
    public void initFragmentData(final PurchaseArg purchaseArg) {

        assemblyData(mBuyTypeView, purchaseArg.method);

        assemblyData(mShopInsuranceView, purchaseArg.insurance);

        assemblyData(mShopDecorationView, purchaseArg.decorate);

//        mBuyTypeView.post(new Runnable() {
//            @Override
//            public void run() {
//                mBuyTypeView.setCallBack(new CarDetailGroupView.CallBack() {
//                    @Override
//                    public void callBack(String string) {
//                        DebugLog.e(TAG, "111111111111111111    >>>>>>>>>>>>>>>>" + string);
//                    }
//                });
//                purchaseArg.method.toArray();
//                mBuyTypeView.initData(2, 0, new String[]{"分期+置换", "分期", "置换", "全额"});
//            }
//        });
//
//        mShopInsuranceView.post(new Runnable() {
//            @Override
//            public void run() {
//                mShopInsuranceView.setCallBack(new CarDetailGroupView.CallBack() {
//                    @Override
//                    public void callBack(String string) {
//                        DebugLog.e(TAG, "222222222222222222222    >>>>>>>>>>>>>>>>" + string);
//                    }
//                });
//                mShopInsuranceView.initData(2, 0, new String[]{"是", "否"});
//            }
//        });
//
//        mShopDecorationView.post(new Runnable() {
//            @Override
//            public void run() {
//                mShopDecorationView.setCallBack(new CarDetailGroupView.CallBack() {
//                    @Override
//                    public void callBack(String string) {
//                        DebugLog.e(TAG, "33333333333333333333333   >>>>>>>>>>>>>>>>" + string);
//                    }
//                });
//                mShopDecorationView.initData(2, 0, new String[]{"是", "否"});
//            }
//        });
    }


    private void assemblyData(final CarDetailGroupView carDetailGroupView, final ArrayList< PurchaseArgDetail > purchaseArgDetails) {
        carDetailGroupView.post(new Runnable() {
            @Override
            public void run() {
                carDetailGroupView.setCallBack(new CarDetailGroupView.CallBack() {
                    @Override
                    public void callBack(String string) {
                        int i = carDetailGroupView.getId();
                        if (i == R.id.buy_type) {
                            DebugLog.e(TAG, " method ========= " + string);
                            hashMap.put("method", string);
                        } else if (i == R.id.shop_insurance) {
                            DebugLog.e(TAG, " insurance ========= " + string);
                            hashMap.put("insurance", string);
                        } else if (i == R.id.shop_decoration) {
                            DebugLog.e(TAG, " decorate ========= " + string);
                            hashMap.put("decorate", string);
                        }
                    }
                });

                int defSelectIndex = 0;
                boolean firstDefSelectIndex = true;
                ArrayList< String > params = new ArrayList<>();
                ArrayList< String > keys = new ArrayList<>();

                for (int i = 0; i < purchaseArgDetails.size(); i++) {
                    PurchaseArgDetail purchaseArgDetail = purchaseArgDetails.get(i);
                    if (firstDefSelectIndex && purchaseArgDetail.isDefault) {
                        defSelectIndex = i;
                        firstDefSelectIndex = false;
                    }
                    params.add(purchaseArgDetail.key);// 用于显示
                    keys.add(purchaseArgDetail.value); // 用于上传
                }

                String[] strings = new String[params.size()];
                strings = params.toArray(strings);// 用于显示

                String[] values = new String[keys.size()];
                values = keys.toArray(values);// 用于上传

                carDetailGroupView.initData(2, defSelectIndex, strings, values);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdvancePriceView = null;
    }


    @Override
    public String fragmentDescription() {
        return "4S店车辆详情Fragment";
    }
}
