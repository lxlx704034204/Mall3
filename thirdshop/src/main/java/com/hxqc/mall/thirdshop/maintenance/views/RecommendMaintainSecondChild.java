package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceReplaceGoods;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

/**
 * @Author : 钟学东
 * @Since : 2016-02-25
 * FIXME
 * Todo 推荐保养的第二层childView
 */
public class RecommendMaintainSecondChild extends LinearLayout {

    private Context context;
    private ImageView mGoodsPhotoView;
    private TextView mGoodsNameView;
    private TextView mGoodsPriceView;
    private RelativeLayout mRelaButtonView;
    private ImageView mImageView;
    private View mDashedView;
    private RelativeLayout mParentView;
    private TextView mGoodsCountView;

    private MaintenanceReplaceGoods replaceGoods;

    private MaintenanceClient maintenanceClient;

    private int position;
    private String shopID;

    public interface onButtonClickListener{
        void  onButtonClick(int position);
    }

    private onButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(onButtonClickListener onButtonClickListener){
        this.onButtonClickListener = onButtonClickListener;
    }

    public RecommendMaintainSecondChild(Context context) {
        super(context);
        this.context = context;
        maintenanceClient= new MaintenanceClient();
    }

    public RecommendMaintainSecondChild(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        maintenanceClient= new MaintenanceClient();
        LayoutInflater.from(context).inflate(R.layout.item_smart_maintain_third_layer_item,this);
        initView();
        initEvent();

    }

    private void initEvent() {
        mRelaButtonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListener.onButtonClick(position);
            }
        });
        mParentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                maintenanceClient.goodsIntroduce(shopID, replaceGoods.goodsID, new DialogResponseHandler(context) {
                    @Override
                    public void onSuccess(String response) {
                        GoodsIntroduce goodsIntroduce = JSONUtils.fromJson(response,new TypeToken<GoodsIntroduce>(){});
                        showAdvertisement(goodsIntroduce);
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

    public void initDate(MaintenanceReplaceGoods replaceGoods,int position,String shopID) {
        this.replaceGoods = replaceGoods;
        this.position = position;
        this.shopID = shopID ;
        mGoodsNameView.setText(replaceGoods.name + "*" + replaceGoods.count);
        mGoodsPriceView.setText(OtherUtil.amountFormat(replaceGoods.price,true));

        mGoodsCountView.setText(replaceGoods.count +"");
        ImageUtil.setImage(context, mGoodsPhotoView, replaceGoods.thumb);
    }

    private void initView() {
        mGoodsPhotoView = (ImageView) findViewById(R.id.goods_photo);
        mGoodsNameView = (TextView) findViewById(R.id.goods_name);
        mGoodsPriceView = (TextView) findViewById(R.id.goods_price);
        mRelaButtonView = (RelativeLayout) findViewById(R.id.rl_button);
        mImageView = (ImageView) findViewById(R.id.iv_button);

        mDashedView = findViewById(R.id.dashed);
        //虚线的显示
        mDashedView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mParentView = (RelativeLayout) findViewById(R.id.parent_view);

        mGoodsCountView = (TextView) findViewById(R.id.goods_count);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        this.measure(w,h);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.getMeasuredWidth()/5,this.getMeasuredHeight());
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        DebugLog.i("TAG", this.getMeasuredWidth() + "  " + this.getMeasuredHeight());
        mRelaButtonView.setLayoutParams(layoutParams);
    }


    public void hideDashedView(){
        mDashedView.setVisibility(View.GONE);
    }

    public void setBackground(){
        mParentView.setBackgroundResource(R.color.white);
    }

    public void setImageViewSelected(){
        mImageView.setImageResource(R.drawable.radiobutton_selected);
    }

    public void setImageViewNormal(){
        mImageView.setImageResource(R.drawable.radiobutton_normal);
    }


    //显示广告的dialog
    private void showAdvertisement(GoodsIntroduce goodsIntroduce) {
        AdvertisementDialog advertisementDialog = new AdvertisementDialog(context,goodsIntroduce);
        advertisementDialog.show();
    }
}
