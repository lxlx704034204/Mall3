//package com.hxqc.mall.thirdshop.maintenance.adapter;
//
//import android.animation.Animator;
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.hxqc.mall.core.util.ActivitySwitchBase;
//import com.hxqc.mall.core.util.ImageUtil;
//import com.hxqc.mall.core.util.OtherUtil;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceGoods;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-03-02
// * FIXME
// * Todo 维修保养adapter
// */
//public class MaintainAndRepairAdapter extends RecyclerView.Adapter {
//
//    private Context context;
//    private boolean[] isDown;
//    private int AnimeTime = 300;
//
//    private ActivitySwitchBase activitySwitchBase;
//
//    //服务器传来的
//    private ArrayList<MaintenanceItem> maintenanceItems = new ArrayList<>();
//    //上个页面传来的 用来对比
//    private ArrayList<MaintenanceItem> tempMaintenanceItems = new ArrayList<>();
//
//
//    private ArrayList<MaintenanceGoods> maintenanceGoodses = new ArrayList<>();
//
//    //测量view的宽高
//    private int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
//            View.MeasureSpec.UNSPECIFIED);
//    private int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
//            View.MeasureSpec.UNSPECIFIED);
//
//    private ValueAnimator animator;
//
//    public MaintainAndRepairAdapter(Context context, ArrayList<MaintenanceItem> maintenanceItems,ArrayList<MaintenanceItem> tempMaintenanceItems) {
//        this.context = context;
//        this.maintenanceItems = maintenanceItems;
//        this.tempMaintenanceItems = tempMaintenanceItems;
//        activitySwitchBase = new ActivitySwitchBase();
//        isDown = new boolean[maintenanceItems.size()];
//
//        for( int i = 0 ;i < maintenanceItems.size() ; i++ ){
//            for( int j = 0 ; j < tempMaintenanceItems.size() ; j++){
//                if(maintenanceItems.get(i).itemId.equals(tempMaintenanceItems.get(j).itemId)){
//                    maintenanceItems.get(i).isCheck = true;
//                    maintenanceItems.get(i).goods.clear();
//                    maintenanceItems.get(i).goods.addAll(tempMaintenanceItems.get(j).goods);
//                }
//            }
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintain_repair, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//
//
////        for(int i =0 ; i< tempMaintenanceItems.size() ; i++){
////            if(tempMaintenanceItems.get(i).itemId .equals(maintenanceItems.get(position).itemId)){
////                maintenanceItems.get(position).isCheck = true;
////                ((ViewHolder) holder).mCheckView.setImageResource(R.drawable.ic_check_maintain);
////                maintenanceItems.get(position).goods.clear();
////
////                maintenanceItems.get(position).goods.addAll(tempMaintenanceItems.get(i).goods);
////            }
////        }
//
//        maintenanceGoodses = maintenanceItems.get(position).goods;
//
//        ((ViewHolder) holder).mItemNameView.setText(maintenanceItems.get(position).name);
//
//
//        if(maintenanceItems.get(position).isCheck){
//            ((ViewHolder) holder).mCheckView.setImageResource(R.drawable.ic_check_maintain);
//        }
//
//        //判断是不是套餐
//        if (maintenanceItems.get(position).isPackage) {
//            ((ViewHolder) holder).mCheckView.setImageResource(R.drawable.ic_check_maintain);
//            ((ViewHolder) holder).mCheckView.setFocusable(false);
//            ((ViewHolder) holder).mCheckView.setFocusableInTouchMode(false);
//        }else {
//            ((ViewHolder) holder).mRlCheckView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (maintenanceItems.get(position).isCheck) {
//                        maintenanceItems.get(position).isCheck = false;
//                        ((ViewHolder) holder).mCheckView.setImageResource(R.drawable.ic_check_dis_maintain);
//                    } else {
//                        maintenanceItems.get(position).isCheck = true;
//                        ((ViewHolder) holder).mCheckView.setImageResource(R.drawable.ic_check_maintain);
//                    }
//                }
//            });
//        }
//
//        if(maintenanceGoodses.size() != 0){
//            //添加子布局
//            for (int i = 0; i < maintenanceGoodses.size(); i++) {
//                LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_item_maintain_goods, ((ViewHolder) holder).mParentView);
//                TextView mNameView = (TextView) linearLayout.findViewById(R.id.goods_name);
//                ImageView mPhotoView = (ImageView) linearLayout.findViewById(R.id.goods_photo);
//                TextView mGoodsPriceView = (TextView) linearLayout.findViewById(R.id.goods_price);
//
//                mNameView.setText(maintenanceGoodses.get(i).name+ "*" +maintenanceGoodses.get(i).count);
//                mGoodsPriceView.setText(OtherUtil.amountFormat((maintenanceGoodses.get(i).price * maintenanceGoodses.get(i).count),true));
//                ImageUtil.setImage(context, mPhotoView, maintenanceGoodses.get(i).thumb);
//            }
//
//        }
//
//        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_item_labor_hour, ((ViewHolder) holder).mParentView);
//        TextView mLaborView = (TextView) layout.findViewById(R.id.labor_price);
//        mLaborView.setText(OtherUtil.amountFormat(maintenanceItems.get(position).workCost,true));
//
//
////        if (position == 0) {
////            ((ViewHolder) holder).mParentView.setVisibility(View.VISIBLE);
////            ((ViewHolder) holder).mArrowsView.setImageResource(R.drawable.ic_arrow_up);
////            isDown[position] = true;
////            LinearLayout linearLayout = (LinearLayout) ((ViewHolder) holder).mParentView.getChildAt(0);
////            linearLayout.measure(w, h);
////            GoodsHeight = linearLayout.getMeasuredHeight();
////        }
//
//
//
//        ((ViewHolder) holder).mRlArrowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isDown[position]) {
//                    isDown[position] = false;
//                    ((ViewHolder) holder).mArrowsView.setImageResource(R.drawable.ic_arrow_down);
//                    hideChildView(((ViewHolder) holder).mParentView);
//                } else {
//                    isDown[position] = true;
//                    ((ViewHolder) holder).mArrowsView.setImageResource(R.drawable.ic_arrow_up);
//                    showChildView(((ViewHolder) holder).mParentView);
//                }
//            }
//        });
//
//        ((ViewHolder) holder).mRlQuestionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ActivitySwitchBase.toH5Activity(context, maintenanceItems.get(position).name, "https://www.baidu.com/index.php?tn=39042058_1_oem_dg&ch=1");
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return maintenanceItems.size();
//    }
//
//    public ArrayList<MaintenanceItem> returnItems(){
//        ArrayList<MaintenanceItem> returnMaintenanceItems = new ArrayList<>();
//        for(int i = 0 ; i < maintenanceItems.size() ; i++){
//            if(maintenanceItems.get(i).isCheck){
//                returnMaintenanceItems.add(maintenanceItems.get(i));
//            }
//        }
//        return  returnMaintenanceItems;
//    }
//
//    //显示子布局
//    private void showChildView(final LinearLayout mParentView) {
//        mParentView.setVisibility(View.VISIBLE);
//
//        mParentView.measure(widthSpec,heightSpec);
//        animator = slideAnimator(0,mParentView.getMeasuredHeight(),mParentView);
//
//        animator.start();
//    }
//
//    //隐藏子布局
//    private void hideChildView(final LinearLayout mParentView) {
//
//
//        int finalHeight = mParentView.getHeight();
//        ValueAnimator mAnimator = slideAnimator(finalHeight, 0 ,mParentView);
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
//
//
//    private ValueAnimator slideAnimator(int start, int end , final LinearLayout contentLayout) {
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
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView mCheckView;
//        private TextView mItemNameView;
//        private ImageView mQuestionView;
//        private ImageView mArrowsView;
//        private LinearLayout mParentView;
//        private RelativeLayout mRlCheckView;
//        private RelativeLayout mRlQuestionView;
//        private RelativeLayout mRlArrowView;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            mCheckView = (ImageView) itemView.findViewById(R.id.iv_check);
//            mItemNameView = (TextView) itemView.findViewById(R.id.item_name);
//            mQuestionView = (ImageView) itemView.findViewById(R.id.question);
//            mArrowsView = (ImageView) itemView.findViewById(R.id.arrows);
//            mParentView = (LinearLayout) itemView.findViewById(R.id.ll_parent);
//            mRlCheckView = (RelativeLayout) itemView.findViewById(R.id.rl_check);
//            mRlQuestionView = (RelativeLayout) itemView.findViewById(R.id.rl_question);
//            mRlArrowView = (RelativeLayout) itemView.findViewById(R.id.rl_arrows);
//        }
//    }
//}
