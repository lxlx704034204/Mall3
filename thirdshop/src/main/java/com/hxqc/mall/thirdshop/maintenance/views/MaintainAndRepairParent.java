//package com.hxqc.mall.thirdshop.maintenance.views;
//
//import android.animation.Animator;
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
//import com.hxqc.mall.core.util.OtherUtil;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
//import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.GoodsIntroduce;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
//import com.hxqc.util.JSONUtils;
//
//import java.util.ArrayList;
//
//
///**
// * @Author : 钟学东
// * @Since : 2016-03-24
// * FIXME
// * Todo
// */
//public class MaintainAndRepairParent extends LinearLayout {
//
//    private Context context;
//    private ImageView mCheckView;
//    private TextView mItemNameView;
//    private ImageView mQuestionView;
//    private ImageView mArrowsView;
//    private LinearLayout mParentView;
//    private LinearLayout mRlCheckView;
//    private RelativeLayout mRlQuestionView;
//    private RelativeLayout mRlArrowView;
//
//    private boolean isDown = false;
//    private int AnimeTime = 300;
//
//    private String itemGroupID;
//    private String itemName;
//
//    private ValueAnimator animator;
//    //测量view的宽高
//    private int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
//            View.MeasureSpec.UNSPECIFIED);
//    private int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
//            View.MeasureSpec.UNSPECIFIED);
//
////    private MaintenanceItem maintenanceItem; //正常流程
//    private MaintenanceItemN maintenanceItemN; //4s店 快修店流程
//    private MaintenanceClient maintenanceClient;
//    private FourSAndQuickHelper fourSAndQuickHelper;
//
//    public interface onCheckClick{
//        void onCheckClick(ArrayList<MaintenanceItemN> maintenanceItemNs,int position);
//    }
//
//    private onCheckClick onCheckClick ;
//
//    public void setOnCheckClick(onCheckClick onCheckClick){
//        this.onCheckClick = onCheckClick;
//    }
//
//    public MaintainAndRepairParent(Context context) {
//        super(context);
//        this.context = context;
//        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
//        maintenanceClient = new MaintenanceClient();
//
//    }
//
//    public MaintainAndRepairParent(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.context = context;
//        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
//        maintenanceClient = new MaintenanceClient();
//        LayoutInflater.from(context).inflate(R.layout.item_maintain_repair, this);
//        initView();
//        initEvent();
//    }
//
//    private void initEvent() {
//        mRlArrowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isDown) {
//                    isDown = false;
//                    mArrowsView.setImageResource(R.drawable.ic_arrow_down);
//                    hideChildView(mParentView);
//                } else {
//                    isDown = true;
//                    mArrowsView.setImageResource(R.drawable.ic_arrow_up);
//                    showChildView(mParentView);
//                }
//            }
//        });
//
//        mRlQuestionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                maintenanceClient.itemIntroduce(itemGroupID, new LoadingAnimResponseHandler(context) {
//                    @Override
//                    public void onSuccess(String response) {
//                        GoodsIntroduce goodsIntroduce = JSONUtils.fromJson(response, new TypeToken<GoodsIntroduce>() {
//                        });
//                        ItemIntroduceDialog dialog = new ItemIntroduceDialog(context, R.style.FullWidthDialog, goodsIntroduce, itemName);
//                        Window window = dialog.getWindow();
//                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        window.setGravity(Gravity.BOTTOM);
//                        dialog.show();
//                    }
//                });
//            }
//        });
//    }
//
//    private void calculateMoney(final LinearLayout ll_laborHour) {
//        TextView mLaborHour = (TextView) ll_laborHour.findViewById(R.id.labor_price);
//        mLaborHour.setText(OtherUtil.CalculateRangeMoney(maintenanceItemN.workCost,true));
//
//        fourSAndQuickHelper.CalculateItemMoney(maintenanceItemN.goods, maintenanceItemN.workCost, new FourSAndQuickHelper.CalculateItemMoneyHandle() {
//            @Override
//            public void onSuccess(float goodsAmount, String amount) {
//                TextView mGoodsPriceView = (TextView) ll_laborHour.findViewById(R.id.accessory);
//                TextView mAmountView = (TextView) ll_laborHour.findViewById(R.id.payable);
//                mGoodsPriceView.setText(OtherUtil.amountFormat(goodsAmount,true));
//                mAmountView.setText(OtherUtil.CalculateRangeMoney(amount,true));
//            }
//        });
//    }
//
//
//    //4s店 快修店流程
//    public void initDate(final ArrayList<MaintenanceItemN> maintenanceItemNs , final int position, String shopID, final LinearLayout ll_parent_layer) {
//        this.maintenanceItemN = maintenanceItemNs.get(position);
//        itemGroupID = maintenanceItemN.itemGroupID;
//        itemName = maintenanceItemN.name;
//
//        mItemNameView.setText(maintenanceItemN.name);
//        if (maintenanceItemNs.get(position).isCheck) {
//            mCheckView.setImageResource(R.drawable.ic_check_maintain);
//        }
//
//
//        mRlCheckView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (maintenanceItemNs.get(position).isCheck) {
//                    maintenanceItemNs.get(position).isCheck = false;
//                    mCheckView.setImageResource(R.drawable.ic_check_dis_maintain);
//                } else {
//                    maintenanceItemNs.get(position).isCheck = true;
//                    mCheckView.setImageResource(R.drawable.ic_check_maintain);
//                    for(int i = 0 ; i<maintenanceItemNs.size() ; i++){
//                        if( i != position && maintenanceItemNs.get(position).mutualExclusionGroup.equals(maintenanceItemNs.get(i).mutualExclusionGroup)){
//                            maintenanceItemNs.get(i).isCheck = false;
//                            MaintainAndRepairParent childView  = (MaintainAndRepairParent) ll_parent_layer.getChildAt(i);
//                            childView.setCheckImageResource();
//                        }
//                    }
////                    onCheckClick.onCheckClick(maintenanceItemNs, position);
//                }
//            }
//        });
//
//
//        //添加子布局
//        for (int i = 0; i < maintenanceItemN.goods.size(); i++) {
//            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_maintain_min_child, mParentView);
//            MaintainMinChild maintainMinChild = (MaintainMinChild) linearLayout.getChildAt(i);
//            maintainMinChild.initDate(maintenanceItemN.goods.get(i), shopID);
//
//        }
//        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_item_labor_hour, mParentView);
//        calculateMoney(layout);
//    }
//
////    public MaintenanceItem returnItem() {
////        return maintenanceItem;
////    }
//
//    public MaintenanceItemN returnItemN(){
//        return maintenanceItemN;
//    }
//
//    //显示子布局
//    private void showChildView(final LinearLayout mParentView) {
//        mParentView.setVisibility(View.VISIBLE);
//
//        mParentView.measure(widthSpec, heightSpec);
//        animator = slideAnimator(0, mParentView.getMeasuredHeight(), mParentView);
//
//        animator.start();
//    }
//
//    //隐藏子布局
//    private void hideChildView(final LinearLayout mParentView) {
//
//
//        int finalHeight = mParentView.getHeight();
//        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, mParentView);
//        mAnimator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mParentView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        mAnimator.start();
//    }
//
//    private ValueAnimator slideAnimator(int start, int end, final LinearLayout contentLayout) {
//        ValueAnimator animator = ValueAnimator.ofInt(start, end);
//        animator.setDuration(AnimeTime);
//
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                int value = (Integer) valueAnimator.getAnimatedValue();
//
//                ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
//                layoutParams.height = value;
//
//                contentLayout.setLayoutParams(layoutParams);
//                contentLayout.invalidate();
//            }
//        });
//        return animator;
//    }
//
//    private void initView() {
//        mCheckView = (ImageView) findViewById(R.id.iv_check);
//        mItemNameView = (TextView) findViewById(R.id.item_name);
//        mQuestionView = (ImageView) findViewById(R.id.question);
//        mArrowsView = (ImageView) findViewById(R.id.arrows);
//        mParentView = (LinearLayout) findViewById(R.id.ll_parent);
//        mRlCheckView = (LinearLayout) findViewById(R.id.rl_check);
//        mRlQuestionView = (RelativeLayout) findViewById(R.id.rl_question);
//        mRlArrowView = (RelativeLayout) findViewById(R.id.rl_arrows);
//    }
//
//    public void setCheckImageResource(){
//        mCheckView.setImageResource(R.drawable.ic_check_dis_maintain);
//    }
//
//
//}
