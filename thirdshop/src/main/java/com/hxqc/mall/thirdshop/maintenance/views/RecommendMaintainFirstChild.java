package com.hxqc.mall.thirdshop.maintenance.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
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
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.GoodsIntroduce;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceReplaceGoods;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-02-25
 * FIXME
 * Todo 推荐保养的第一层childView  这个View是精华之中的精华
 */
public class RecommendMaintainFirstChild extends RelativeLayout implements RecommendMaintainSecondChild.onButtonClickListener {

    private ImageView mGoodsPhotoView;
    private TextView mGoodsNameView;
    private TextView mGoodsPriceView;
    private TextView mGoodsCountView;
    private RelativeLayout mChangeView;
    private View mDashedView;
    private RelativeLayout mOneLitreView;
    private TextView mOneLiterTextView;
    private ImageView mOneLiterImageView;
    private LinearLayout rlAddGoodsView;
    private ImageView mAddGoodsPhotoView;
    private TextView mAddGoodsNameView;
    private TextView mAddGoodsPriceView;
    private TextView mAddGoodsCountView;

    private LinearLayout ll_first;
    private LinearLayout ll_second;

    private Context context;

    //显示更改View动画执行时间
    private int AnimeTime = 500;
    //显示第二层动画执行时间
    private int AnimateTime = 500;

    private ValueAnimator animator;
    //测量view的宽高
    private int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
            View.MeasureSpec.UNSPECIFIED);
    private int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
            View.MeasureSpec.UNSPECIFIED);

    //第二层child选中的position
    private int tempPosition = 0;

    private boolean isButtonClick = false;

    private ArrayList<MaintenanceReplaceGoods> replaceGoodses ;

    //测量view的宽高
    private int w = View.MeasureSpec.makeMeasureSpec(0,
            View.MeasureSpec.UNSPECIFIED);
    private int h = View.MeasureSpec.makeMeasureSpec(0,
            View.MeasureSpec.UNSPECIFIED);

    private LinearLayout linearLayoutChild;

    private MaintenanceClient maintenanceClient;

    private String shopID;

    //是位于商品组的第几个
    private int position;
    //是否含有 4L + 1L 的情况
    private boolean isInclude;


    //是否显示添加的商品
    private boolean isShowAddGoods;

    public interface onAddGoods{
        void addGoods(ArrayList<MaintenanceReplaceGoods> replaceGoodses,int goodsPosition , boolean isShowAddGoods);
    }

    private onAddGoods onAddGoods;

    public void setOnAddGoods(onAddGoods onAddGoods){
        this.onAddGoods = onAddGoods;
    }


    public RecommendMaintainFirstChild(Context context) {
        super(context);
        this.context = context;
        maintenanceClient= new MaintenanceClient();
    }

    public RecommendMaintainFirstChild(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        maintenanceClient= new MaintenanceClient();
        LayoutInflater.from(context).inflate(R.layout.item_smart_maintain_second_laye_item, this);
        initView();
        initEvent();
    }

    private void initEvent() {
        mChangeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showSecondLayer();
            }
        });

        //联网获取配件介绍
        ll_first.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                maintenanceClient.goodsIntroduce(shopID, replaceGoodses.get(0).goodsID, new BaseMallJsonHttpResponseHandler(context) {
                    @Override
                    public void onSuccess(String response) {
                        GoodsIntroduce goodsIntroduce = JSONUtils.fromJson(response,new TypeToken<GoodsIntroduce>(){});
//                        showAdvertisement(goodsIntroduce);
                        ItemIntroduceDialog dialog = new ItemIntroduceDialog(context, R.style.FullWidthDialog, goodsIntroduce,replaceGoodses.get(0).name);
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.BOTTOM);
                        dialog.show();
                    }
                });
            }
        });

        mOneLitreView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowAddGoods){
                    isShowAddGoods = false;
                    mOneLiterTextView.setText("添加1L装");
                    mOneLiterImageView.setImageResource(R.drawable.maintain_down);
                    if(!isInclude){
                        hideChildView(rlAddGoodsView,"2");
                        replaceGoodses.get(0).isAddAddGoods = false;
                    }
                }else {
                    isShowAddGoods = true;
                    mOneLiterTextView.setText("移除1L装");
                    mOneLiterImageView.setImageResource(R.drawable.maintain_up);
                    if(!isInclude){
                        showChildView(rlAddGoodsView,"2");
                        replaceGoodses.get(0).isAddAddGoods = true;
                    }
                }
                if(onAddGoods != null){
                    onAddGoods.addGoods(replaceGoodses,position,isShowAddGoods);
                }
            }
        });
    }


    public void initDate(ArrayList<MaintenanceReplaceGoods> replaceGoodses,String shopID,int position,boolean isInclude,boolean isShowAddGoods) {
        this.replaceGoodses = replaceGoodses;
        this.shopID =shopID;
        this.position = position;
        this.isInclude = isInclude;
        this.isShowAddGoods = isShowAddGoods;
        //第一个 是显示出来的商品
        MaintenanceReplaceGoods showGoods = replaceGoodses.get(0);
        mGoodsNameView.setText(showGoods.name );
        mGoodsCountView.setText(showGoods.count+"");
        mGoodsPriceView.setText(OtherUtil.amountFormat(showGoods.price,true));

        ImageUtil.setImage(context, mGoodsPhotoView, showGoods.thumb);
        if( showGoods.addition == null || (TextUtils.isEmpty(showGoods.addition.goodsID)&& TextUtils.isEmpty(showGoods.addition.name))|| showGoods.equals("")){
            mOneLitreView.setVisibility(View.GONE);
            replaceGoodses.get(0).isAddAddGoods =false;
            rlAddGoodsView.setVisibility(View.GONE);
        }else {
            mOneLitreView.setVisibility(View.VISIBLE);
            mAddGoodsNameView.setText(showGoods.addition.name);
            mAddGoodsPriceView.setText(OtherUtil.amountFormat((showGoods.addition.price),true));
            mAddGoodsCountView.setText(showGoods.addition.count+"");
            ImageUtil.setImage(context, mAddGoodsPhotoView, showGoods.addition.thumb);
            if(isShowAddGoods){
                if(!isInclude){
                    rlAddGoodsView.setVisibility(View.VISIBLE);
                }
                mOneLiterTextView.setText("移除1L装");
                mOneLiterImageView.setImageResource(R.drawable.maintain_up);
            }else {
                mOneLiterTextView.setText("添加1L装");
                mOneLiterImageView.setImageResource(R.drawable.maintain_down);
            }
        }

        for(int i= 0 ; i< replaceGoodses.size() ; i++){
            linearLayoutChild = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_smart_maintain_third_layer,ll_second);
            RecommendMaintainSecondChild secondChild = (RecommendMaintainSecondChild) linearLayoutChild.getChildAt(i);
            secondChild.initDate(replaceGoodses.get(i),i,shopID);
            if(i == 0){
                secondChild.setBackground();
                secondChild.setImageViewSelected();
            }
            if(i == 0 || i == replaceGoodses.size()-1){
                secondChild.hideDashedView();
            }
            secondChild.setOnButtonClickListener(this);
        }

    }

    private void initView() {
        mGoodsPhotoView = (ImageView) findViewById(R.id.goods_photo);
        mGoodsNameView = (TextView) findViewById(R.id.goods_name);
        mGoodsPriceView = (TextView) findViewById(R.id.goods_price);
        mGoodsCountView = (TextView) findViewById(R.id.goods_count);
        mChangeView = (RelativeLayout) findViewById(R.id.change);
        mDashedView = findViewById(R.id.dashed);
        //虚线的显示
        mDashedView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mOneLitreView = (RelativeLayout) findViewById(R.id.rl_one_litre);
        mOneLiterTextView = (TextView) findViewById(R.id.litre_goods);
        mOneLiterImageView = (ImageView) findViewById(R.id.litre_photo);
        rlAddGoodsView = (LinearLayout) findViewById(R.id.rl_add);
        mAddGoodsNameView = (TextView) findViewById(R.id.add_goods_name);
        mAddGoodsPhotoView = (ImageView) findViewById(R.id.add_goods_photo);
        mAddGoodsPriceView = (TextView) findViewById(R.id.add_goods_price);
        mAddGoodsCountView = (TextView) findViewById(R.id.add_goods_count);
        ll_first = (LinearLayout) findViewById(R.id.ll_one);
        ll_second = (LinearLayout) findViewById(R.id.ll_two);

    }

    //显示 更换view 的动画
    public void showChangeView() {
        ll_first.measure(w,h);
        DebugLog.i("TAG","ll_first.getWidth()" + ll_first.getWidth());
        DebugLog.i("TAG","ll_first.getHeight()" + ll_first.getHeight());
        LayoutParams params = new LayoutParams(ll_first.getWidth()/5,ll_first.getHeight());
        mChangeView.setLayoutParams(params);

        mChangeView.setVisibility(View.VISIBLE);
        mChangeView.measure(w, h);
        ObjectAnimator.ofFloat(mChangeView, "translationX", getWidth(), getWidth() - getWidth()/5).setDuration(AnimeTime).start();
    }

    //隐藏 更换view 的动画
    public void hideChangeView() {
        ll_first.measure(w,h);
        DebugLog.i("TAG","ll_first.getWidth()" + ll_first.getWidth());
        DebugLog.i("TAG","ll_first.getHeight()" + ll_first.getHeight());
        LayoutParams params = new LayoutParams(ll_first.getWidth()/5,ll_first.getHeight());
        mChangeView.setLayoutParams(params);

        mChangeView.measure(w, h);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mChangeView,"translationX", getWidth() - getWidth()/5,getWidth());
        animator.setDuration(AnimeTime);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mChangeView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    //显示第二层
    private void showSecondLayer(){
        ll_first.setVisibility(View.GONE);

        showChildView(ll_second,"1");
//        ll_second.measure(w,h);
//        ll_second.setVisibility(View.VISIBLE);
//
//        for(int j=0 ; j<replaceGoodses.size() ; j++){
//            RecommendMaintainSecondChild secondChild = (RecommendMaintainSecondChild) ll_second.getChildAt(j);
//            ObjectAnimator.ofFloat(secondChild,"translationY",-DisplayTools.dip2px(context,90*j),0).setDuration(AnimeTime).start();
//        }
    }

    //隐藏第二层
    public void hideSecondLayer(){
//        for(int j=0 ; j<replaceGoodses.size() ; j++){
//            RecommendMaintainSecondChild secondChild = (RecommendMaintainSecondChild) ll_second.getChildAt(j);
//            ObjectAnimator.ofFloat(secondChild,"translationY",0,-DisplayTools.dip2px(context,90*j)).setDuration(AnimeTime).start();
//        }
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ll_second.setVisibility(View.GONE);
//            }
//        }, AnimeTime);
        hideChildView(ll_second,"1");

    }

    //显示子布局
    private void showChildView(final LinearLayout mParentView,String flag) {
        mParentView.setVisibility(View.VISIBLE);

        mParentView.measure(widthSpec, heightSpec);
        if(flag.equals("1")){
            animator = slideAnimator(ll_first.getHeight(), mParentView.getMeasuredHeight(), mParentView);
        }else {
            animator = slideAnimator(0, mParentView.getMeasuredHeight(), mParentView);
        }


        animator.start();
    }

    //隐藏子布局
    private void hideChildView(final LinearLayout mParentView, final String flag) {


        int finalHeight = mParentView.getHeight();
        ValueAnimator mAnimator;
        if(flag.equals("1")){
            mAnimator = slideAnimator(finalHeight, ll_first.getHeight(), mParentView);
        }else {
            mAnimator = slideAnimator(finalHeight, 0, mParentView);
        }
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (flag.equals("1")){
                    ll_first.setVisibility(View.VISIBLE);
                }
                mParentView.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end, final LinearLayout contentLayout) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(AnimateTime);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
                layoutParams.height = value;

                contentLayout.setLayoutParams(layoutParams);
                contentLayout.invalidate();
            }
        });
        return animator;
    }

    //重新排序代替商品的顺序
    public ArrayList<MaintenanceReplaceGoods> rankGoods(){
        if(isButtonClick){
            MaintenanceReplaceGoods tempReplaceGoods = replaceGoodses.get(tempPosition);
            MaintenanceReplaceGoods firstReplaceGoods = replaceGoodses.get(0);
            ArrayList<MaintenanceReplaceGoods> tempReplaceGoodses = new ArrayList<>();
            for(int i=0; i< replaceGoodses.size();i++){
                if(i == 0){
                    tempReplaceGoodses.add(tempReplaceGoods);
                }else if(i == tempPosition){
                    tempReplaceGoodses.add(firstReplaceGoods);
                }else {
                    tempReplaceGoodses.add(replaceGoodses.get(i));
                }
            }
            replaceGoodses.clear();
            linearLayoutChild.removeAllViews();
            //初始化list中 所有商品 是否添加商品字段
            for(int i = 0 ; i < tempReplaceGoodses.size() ; i++){
                tempReplaceGoodses.get(i).isAddAddGoods = false;
                //如果替换的商品没有添加的商品  即使之前添加过也要干掉
                if(i == 0 && tempReplaceGoodses.get(0).addition == null){
                    isShowAddGoods = false;
                }
            }
            if(isShowAddGoods && !isInclude){
                tempReplaceGoodses.get(0).isAddAddGoods = true;
            }


            initDate(tempReplaceGoodses,shopID,position,isInclude,isShowAddGoods);
            isButtonClick = false;
            return tempReplaceGoodses;
        }else {
            return replaceGoodses;
        }

    }

    //显示广告的dialog
    private void showAdvertisement(GoodsIntroduce goodsIntroduce) {
        AdvertisementDialog advertisementDialog = new AdvertisementDialog(context,goodsIntroduce);
        advertisementDialog.show();
    }

    @Override
    public void onButtonClick(int position) {
        tempPosition = position;
        isButtonClick = true;
        for(int i = 0 ;i<replaceGoodses.size(); i++){
            RecommendMaintainSecondChild secondChild = (RecommendMaintainSecondChild) linearLayoutChild.getChildAt(i);
            if (i ==  position){
                secondChild.setImageViewSelected();
            }else {
                secondChild.setImageViewNormal();
            }
        }
    }
}
