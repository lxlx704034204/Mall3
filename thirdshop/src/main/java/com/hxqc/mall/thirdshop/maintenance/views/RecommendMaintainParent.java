//package com.hxqc.mall.thirdshop.maintenance.views;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
//import com.hxqc.mall.core.util.OtherUtil;
//import com.hxqc.mall.core.views.dialog.NormalDialog;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
//import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.GoodsIntroduce;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceBaseGoods;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceGoods;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceReplaceGoods;
//
//import com.hxqc.util.JSONUtils;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-02-26
// * FIXME
// * Todo 推荐保养的父布局 从这里开始就是整个保养之精华所在
// */
//public class RecommendMaintainParent extends LinearLayout implements RecommendMaintainFirstChild.onAddGoods {
//
//    private Context context;
//
//    //是不是编辑状态
//    private Boolean isChange = false;
//
//    private TextView mNumView;
//    private TextView mNameView;
//    private TextView mChangeView;
//    private TextView mDeleteView;
//    private LinearLayout ll_first_layer;
//    private RelativeLayout mQuestionView;
//    private RelativeLayout mCalculateView;
//    public TextView mTitleView;
//    public TextView mSecondTitleView;
//
//    private MaintenanceItemN maintenanceItemN ;//4s店 快修店报价流程
//
//
//    private ArrayList<MaintenanceGoods> maintenanceGoodses = new ArrayList<>();
//    //用来判断是否显示mChangeView
//    private ArrayList<MaintenanceReplaceGoods> tempGoods = new ArrayList<>();
//
//    private String itemGroupID;
//    private String itemName;
//
//    //用于返回检查套餐
//    private ArrayList<MaintenanceGoods> returnGoods = new ArrayList<>();
//    //如果有 4L 加 1L 的情况 这个就是 4L中的Addition
//    private MaintenanceBaseGoods baseGoods = null;
//    //如果有 4L 加 1L 的情况 这个就是 1L中的商品数量
//    private int oneCount;
//    private int position;
//    private String shopID;
//    private boolean isInclude;
//    private boolean isIncluded = false; //只算一次是不是包含1L
//    private String oneGoodsID; //一升的goodsid
//    private boolean isShowAddGoods = false; //子View 是否显示添加1L装
//
//    private MaintenanceClient maintenanceClient;
//    private FourSAndQuickHelper fourSAndQuickHelper;
//    private LinearLayout ll_laborHour;
//
//    private static String FourLitre = "4L";
//    private static String OneLitre = "1L";
//
//    public interface onDeleteClick {
//        void deleteClick(int position);
//
//        void Complete(ArrayList<MaintenanceGoods> returnGoods, int position);
//    }
//
//    private onDeleteClick onDeleteClick;
//
//    public void setOnDeleteClick(onDeleteClick onDeleteClick) {
//        this.onDeleteClick = onDeleteClick;
//    }
//
//    public interface onAddGoodsCalculate{
//        void addGoodsCalculate(ArrayList<MaintenanceGoods> returnGoods,int position);
//    }
//
//    private onAddGoodsCalculate onAddGoodsCalculate;
//
//    public void setOnAddGoodsCalculate(onAddGoodsCalculate onAddGoodsCalculate){
//        this.onAddGoodsCalculate = onAddGoodsCalculate;
//    }
//
//    public RecommendMaintainParent(Context context) {
//        super(context);
//        this.context = context;
//        maintenanceClient = new MaintenanceClient();
//        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
//    }
//
//
//
//
//    public RecommendMaintainParent(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.context = context;
//        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
//        LayoutInflater.from(context).inflate(R.layout.item_smart_maintain_first_layer_item, this);
//        maintenanceClient = new MaintenanceClient();
//        initView();
//        initEvent();
//    }
//
//    private void initEvent() {
//        mChangeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickChangeView();
//            }
//        });
//
//        mDeleteView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NormalDialog normalDialog = new NormalDialog(context, "您确定要删除该项目吗？") {
//                    @Override
//                    protected void doNext() {
//                        if (onDeleteClick != null)
//                            onDeleteClick.deleteClick(position);
//                    }
//                };
//                normalDialog.show();
//            }
//        });
//
//        mQuestionView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                maintenanceClient.itemIntroduce(itemGroupID, new LoadingAnimResponseHandler(context) {
//                    @Override
//                    public void onSuccess(String response) {
//                        GoodsIntroduce goodsIntroduce = JSONUtils.fromJson(response, new TypeToken<GoodsIntroduce>() {
//                        });
//                        ItemIntroduceDialog dialog = new ItemIntroduceDialog(context, R.style.FullWidthDialog, goodsIntroduce,itemName);
//                        Window window = dialog.getWindow();
//                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        window.setGravity(Gravity.BOTTOM);
//                        dialog.show();
//                    }
//                });
//
//            }
//        });
//    }
//
//
//    //添加了商品后  用于计算价格 和  上传商品id
//    @Override
//    public void addGoods(ArrayList<MaintenanceReplaceGoods> replaceGoodses, int goodsPosition, boolean isShowAddGoods) {
//        this.isShowAddGoods = isShowAddGoods;
//        if(onAddGoodsCalculate != null){
//            for(int n = 0 ; n < maintenanceGoodses.size() ; n++){
//                if(n == goodsPosition){
//                    MaintenanceGoods tempGoods = new MaintenanceGoods();
//                    tempGoods.replaceable = new ArrayList<>();
//                    for (int i = 0; i < replaceGoodses.size(); i++) {
//                        if (i == 0) {
//                            tempGoods.name = replaceGoodses.get(i).name;
//                            tempGoods.price = replaceGoodses.get(i).price;
//                            tempGoods.count = replaceGoodses.get(i).count;
//                            tempGoods.thumb = replaceGoodses.get(i).thumb;
//                            tempGoods.goodsID = replaceGoodses.get(i).goodsID;
//                            tempGoods.addition = replaceGoodses.get(i).addition;
//                            tempGoods.isLinkage = replaceGoodses.get(i).isLinkage;
//                            tempGoods.isAddAddGoods = replaceGoodses.get(i).isAddAddGoods;
//                        } else {
//                            tempGoods.replaceable.add(replaceGoodses.get(i));
//                        }
//                    }
//                    returnGoods.add(tempGoods);
//                }else {
//                    returnGoods.add(maintenanceGoodses.get(n));
//                }
//
//                if(maintenanceGoodses.get(n).goodsID.equals(oneGoodsID)){
//                    if(isShowAddGoods){
//                        maintenanceGoodses.get(n).count = maintenanceGoodses.get(n).count + baseGoods.count;
//                    }else {
//                        maintenanceGoodses.get(n).count = maintenanceGoodses.get(n).count - baseGoods.count;
//                    }
//                }
//            }
//
//            maintenanceItemN.goods.clear();
//            maintenanceGoodses.clear();
//            maintenanceItemN.goods.addAll(returnGoods);
//            ll_first_layer.removeAllViews();
//            initDate(maintenanceItemN,position,shopID);
//            onAddGoodsCalculate.addGoodsCalculate(returnGoods,position);
//            returnGoods.clear();
//        }
//    }
//
//
//    //4s店 快修店报价流程
//    public void initDate(MaintenanceItemN maintenanceItemN, final int position, String shopID) {
//        this.maintenanceItemN = maintenanceItemN;
//        this.position = position;
//        this.shopID  = shopID;
//        itemGroupID = maintenanceItemN.itemGroupID;
//        itemName = maintenanceItemN.name;
//
//        mNumView.setText(position + 1 + ".");
//        mNameView.setText(maintenanceItemN.name);
//
//        //套餐中的item不能修改
//        if (maintenanceItemN.revisability == 0) {
//            mChangeView.setVisibility(View.GONE);
//            mDeleteView.setVisibility(View.GONE);
//        }
//        maintenanceGoodses.addAll(maintenanceItemN.goods);
//        //是否含有 4L + 1L 的情况
//        if(!isIncluded){
//            isIncluded = true;
//            isInclude = isInclude1L();
//        }
//
//
//        //动态加子布局
//        for (int m = 0; m < maintenanceGoodses.size(); m++) {
//
//            tempGoods.addAll(maintenanceGoodses.get(m).replaceable);
//
//            ArrayList<MaintenanceReplaceGoods> replaceGoodses = new ArrayList<>();
//
//            MaintenanceReplaceGoods replaceGoods  = maintenanceGoodses.get(m);
//
//            replaceGoodses.add(replaceGoods);
//
//            replaceGoodses.addAll(maintenanceGoodses.get(m).replaceable);
//
//            LinearLayout ll_second_layer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_smart_maintain_second_layer, ll_first_layer);
//            RecommendMaintainFirstChild firstChild = (RecommendMaintainFirstChild) ll_second_layer.getChildAt(m);
//
//            firstChild.setOnAddGoods(this);
//
//
//            firstChild.initDate(replaceGoodses, shopID,m,isInclude,isShowAddGoods);
//
//        }
//
//        ll_laborHour = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_maintain_labor_hour, ll_first_layer);
//
//        calculateMoney(ll_laborHour);
//
//        //如果所有的商品都没替换物品就没有更换按钮
//        if (tempGoods.size() == 0) {
//            mChangeView.setVisibility(View.GONE);
//        }
//    }
//
//    private void calculateMoney(final LinearLayout ll_laborHour) {
//        TextView mLaborHour = (TextView) ll_laborHour.findViewById(R.id.labor_price);
//        String workCost = OtherUtil.CalculateRangeMoney(maintenanceItemN.workCost,true);
//        mLaborHour.setText(workCost);
//        TextView mLaborName = (TextView) ll_laborHour.findViewById(R.id.labor_name);
//        if(workCost.contains("-")){
//            mLaborName.setText("工时费区间:");
//        }else {
//            mLaborName.setText("工时小计:");
//        }
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
//    /**
//     * 项目中是不是 有4L 加 1L 的组合
//     * @return
//     */
//    private boolean isInclude1L() {
//        boolean isInclude = false;
//        for (int i = 0; i < maintenanceGoodses.size(); i++) {
//            if (maintenanceGoodses.get(i).addition != null) {
//                for (int j = 0; j < maintenanceGoodses.size(); j++) {
//                    if (maintenanceGoodses.get(i).addition != null && maintenanceGoodses.get(i).addition.goodsID != null) {
//                        if (maintenanceGoodses.get(i).addition.goodsID.equals(maintenanceGoodses.get(j).goodsID)) {
//                            isInclude = true;
//                            baseGoods = maintenanceGoodses.get(i).addition;
//                            oneGoodsID = maintenanceGoodses.get(i).addition.goodsID;
//                            maintenanceGoodses.get(i).isLinkage = FourLitre;
//                            maintenanceGoodses.get(j).isLinkage = OneLitre;
//                            oneCount = maintenanceGoodses.get(j).count;
//                        }
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < maintenanceGoodses.size(); i++) {
//            if (maintenanceGoodses.get(i).isLinkage.equals(FourLitre)) {
//                for (int j = 0; j < maintenanceGoodses.get(i).replaceable.size(); j++) {
//                    maintenanceGoodses.get(i).replaceable.get(j).isLinkage = FourLitre;
//                }
//            }
//        }
//        return isInclude;
//    }
//
//    private void clickChangeView() {
//        if (!isChange) {
//            mChangeView.setText("完成");
//            for (int m = 0; m < maintenanceGoodses.size(); m++) {
//                //如果没有可替换的物品就不显示可更换的View
//                if (maintenanceGoodses.get(m).replaceable.size() != 0) {
//                    RecommendMaintainFirstChild ll_second_layer = (RecommendMaintainFirstChild) ll_first_layer.getChildAt(m);
//                    ll_second_layer.showChangeView();
//                }
//            }
//            isChange = true;
//        } else {
//            clickDown();
//        }
//    }
//
//    private void clickDown(){
//        mChangeView.setText("更换");
//        isShowAddGoods = false;
//        for (int m = 0; m < maintenanceGoodses.size(); m++) {
//            RecommendMaintainFirstChild ll_second_layer = (RecommendMaintainFirstChild) ll_first_layer.getChildAt(m);
//            ArrayList<MaintenanceReplaceGoods> tempReplace = ll_second_layer.rankGoods();
//            MaintenanceGoods tempGoods = new MaintenanceGoods();
//            tempGoods.replaceable = new ArrayList<>();
//            for (int i = 0; i < tempReplace.size(); i++) {
//                if (i == 0) {
//                    tempGoods.name = tempReplace.get(i).name;
//                    tempGoods.price = tempReplace.get(i).price;
//                    tempGoods.count = tempReplace.get(i).count;
//                    tempGoods.thumb = tempReplace.get(i).thumb;
//                    tempGoods.goodsID = tempReplace.get(i).goodsID;
//                    tempGoods.addition = tempReplace.get(i).addition;
//                    tempGoods.isAddAddGoods = tempReplace.get(i).isAddAddGoods;
//                    tempGoods.isLinkage = tempReplace.get(i).isLinkage;
//                    tempGoods.additionTag = tempReplace.get(i).additionTag;
//                } else {
//                    tempGoods.replaceable.add(tempReplace.get(i));
//                }
//            }
//            returnGoods.add(tempGoods);
//            ll_second_layer.hideChangeView();
//            ll_second_layer.hideSecondLayer();
//        }
//        isChange = false;
//
//        maintenanceItemN.goods.clear();
//        maintenanceItemN.goods.addAll(returnGoods);
//
//        boolean isDeleteOne = false; // 是否删除一升装
//        int onePosition = 0; //1L 装的位置 用于删除1L装
//        boolean isHaveOne = false ; //是否有一升的 如果更换了一个4L没有addition 就会出现这种情况
//
//        for(int i = 0 ; i< maintenanceItemN.goods.size() ; i++){
//             if(maintenanceItemN.goods.get(i).isLinkage.equals(FourLitre)){
//                 if(maintenanceItemN.goods.get(i).addition != null){
//                     for(int j = 0 ; j< maintenanceItemN.goods.size() ; j++){
//                         if(maintenanceItemN.goods.get(j).isLinkage.equals(OneLitre)){
//                             isHaveOne = true;
//                             baseGoods = maintenanceItemN.goods.get(i).addition;
//                             maintenanceItemN.goods.get(j).goodsID = maintenanceItemN.goods.get(i).addition.goodsID;
//                             oneGoodsID = maintenanceItemN.goods.get(i).addition.goodsID;
//                             maintenanceItemN.goods.get(j).count = oneCount;
//                             maintenanceItemN.goods.get(j).name = maintenanceItemN.goods.get(i).addition.name;
//                             maintenanceItemN.goods.get(j).thumb = maintenanceItemN.goods.get(i).addition.thumb;
//                             maintenanceItemN.goods.get(j).price = maintenanceItemN.goods.get(i).addition.price;
//                         }
//                     }
//                 }else {
//                     for(int j = 0 ; j< maintenanceItemN.goods.size() ; j++){
//                         if(maintenanceItemN.goods.get(j).isLinkage.equals(OneLitre)){
//                             onePosition = j;
//                             isDeleteOne = true;
//                         }
//                     }
//                 }
//             }
//        }
//        int FourPosition = 0; //记录4L装的位置
//        if(isDeleteOne){
//            maintenanceItemN.goods.remove(onePosition);
//        }else {
//            //有4L + 1L 的情况 且 1L被删除 此方法就是加上1L装
//            if(isInclude && !isHaveOne){
//                for(int i = 0 ; i< maintenanceItemN.goods.size() ; i++){
//                    if(maintenanceItemN.goods.get(i).addition != null){
//                        baseGoods = maintenanceItemN.goods.get(i).addition;
//                        FourPosition = i;
//                    }
//                }
//                ArrayList<MaintenanceGoods> tempGoods = new ArrayList<>();
//                for(int j = 0 ; j <= FourPosition ; j++){
//                    tempGoods.add(maintenanceItemN.goods.get(j));
//                }
//
//                MaintenanceGoods tempGood = new MaintenanceGoods();
//                tempGood.isLinkage = OneLitre;
//                tempGood.price = baseGoods.price;
//                tempGood.thumb = baseGoods.thumb;
//                tempGood.goodsID = baseGoods.goodsID;
//                tempGood.name = baseGoods.name;
//                tempGood.count = baseGoods.count;
//                tempGood.replaceable = new ArrayList<>();
//                oneGoodsID = baseGoods.goodsID;
//                tempGoods.add(tempGood);
//
//                for (int n = FourPosition +1 ; n < maintenanceItemN.goods.size() ; n++){
//                    tempGoods.add(maintenanceItemN.goods.get(n));
//                }
//
//                maintenanceItemN.goods.clear();
//                maintenanceItemN.goods.addAll(tempGoods);
//            }
//        }
//
//
//        ll_first_layer.removeAllViews();
//        maintenanceGoodses.clear();
//        initDate(maintenanceItemN,position,shopID);
//        returnGoods.clear();
//        returnGoods.addAll(maintenanceItemN.goods);
//        onDeleteClick.Complete(returnGoods, position);
//        returnGoods.clear();
//    }
//
//    private void initView() {
//        mNumView = (TextView) findViewById(R.id.number);
//        mNameView = (TextView) findViewById(R.id.name);
//        mChangeView = (TextView) findViewById(R.id.change);
//        mDeleteView = (TextView) findViewById(R.id.delete);
//        ll_first_layer = (LinearLayout) findViewById(R.id.ll);
//        mQuestionView = (RelativeLayout) findViewById(R.id.question);
//        mCalculateView = (RelativeLayout) findViewById(R.id.calculate);
//        mTitleView = (TextView) findViewById(R.id.maintenance_title);
//        mSecondTitleView = (TextView) findViewById(R.id.maintain_second_title);
//    }
//
//
//}
