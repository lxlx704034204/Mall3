package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.GoodsIntroduce;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceGoods;
import com.hxqc.util.JSONUtils;

/**
 * @Author : 钟学东
 * @Since : 2016-03-24
 * FIXME
 * Todo
 */
public class MaintainMinChild extends LinearLayout {

    private Context context;
    private TextView mGoodsNameView ;
    private TextView mPriceView ;
    private TextView mGoodsCountView;
    private ImageView mGoodsPhotoView;
    private RelativeLayout mRlGoodsView;
    private MaintenanceClient maintenanceClient;
    private MaintenanceGoods maintenanceGoods;
    private String shopID;

    public MaintainMinChild(Context context) {
        super(context);
        this.context = context;
        maintenanceClient = new MaintenanceClient();
    }

    public MaintainMinChild(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        maintenanceClient = new MaintenanceClient();

        LayoutInflater.from(context).inflate(R.layout.layout_item_maintain_goods,this);
        mGoodsNameView = (TextView)findViewById(R.id.goods_name);
        mPriceView = (TextView)findViewById(R.id.goods_price);
        mRlGoodsView = (RelativeLayout) findViewById(R.id.rl_goods);
        mGoodsPhotoView = (ImageView) findViewById(R.id.goods_photo);
        mGoodsCountView = (TextView) findViewById(R.id.goods_count);

        mRlGoodsView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                maintenanceClient.goodsIntroduce(shopID, maintenanceGoods.goodsID, new DialogResponseHandler(context) {
                    @Override
                    public void onSuccess(String response) {
                        GoodsIntroduce goodsIntroduce = JSONUtils.fromJson(response,new TypeToken<GoodsIntroduce>(){});
//                        showAdvertisement(goodsIntroduce);

                        ItemIntroduceDialog dialog = new ItemIntroduceDialog(context, R.style.FullWidthDialog, goodsIntroduce,maintenanceGoods.name);
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.BOTTOM);
                        dialog.show();
                    }
                });
//                GoodsIntroduce goodsIntroduce = new GoodsIntroduce();
//                goodsIntroduce.img = new ArrayList<String>();
//                goodsIntroduce.img.add("http://ww1.sinaimg.cn/large/7a8aed7bjw1f20ruz456sj20go0p0wi3.jpg");
//                goodsIntroduce.img.add("http://ww4.sinaimg.cn/large/7a8aed7bjw1f1yjc38i9oj20hs0qoq6k.jpg");
//                goodsIntroduce.content = "https://www.baidu.com/index.php?tn=39042058_1_oem_dg&ch=1";
//                showAdvertisement(goodsIntroduce);
            }
        });


    }

    public void initDate(MaintenanceGoods maintenanceGoods,String shopID){
        this.maintenanceGoods = maintenanceGoods;
        this.shopID = shopID;
        mGoodsNameView.setText(maintenanceGoods.name);
        mGoodsCountView.setText(maintenanceGoods.count+"");
        mPriceView.setText(OtherUtil.amountFormat(maintenanceGoods.price,true));
        ImageUtil.setImage(context, mGoodsPhotoView, maintenanceGoods.thumb);
    }

    //显示广告的dialog
    private void showAdvertisement(GoodsIntroduce goodsIntroduce) {
        AdvertisementDialog advertisementDialog = new AdvertisementDialog(context,goodsIntroduce);
        advertisementDialog.show();
    }
}
