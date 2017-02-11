//package com.hxqc.mall.thirdshop.maintenance.adapter;
//
//import android.content.Context;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.GoodsIntroduce;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ShopQuote;
//import com.hxqc.mall.thirdshop.maintenance.views.ItemIntroduceDialog;
//import com.hxqc.util.JSONUtils;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-04-20
// * FIXME
// * Todo
// */
//public class FourSAndQuickShopAdapter extends BaseAdapter {
//
//    private Context context;
//    private ArrayList<ShopQuote> shopQuotes;
//    private MaintenanceClient maintenanceClient;
//
//
//    public interface onButtonSelected{
//        void buttonEnabled();
//
//        void buttonNoEnabled();
//    }
//
//    private  onButtonSelected onButtonSelected;
//
//    public void setOnButtonSelected(onButtonSelected onButtonSelected){
//        this.onButtonSelected = onButtonSelected;
//    }
//
//    public FourSAndQuickShopAdapter(Context context, ArrayList<ShopQuote> shopQuotes) {
//        this.context = context;
//        this.shopQuotes = shopQuotes;
//        maintenanceClient = new MaintenanceClient();
//    }
//
//    @Override
//    public int getCount() {
//        return shopQuotes == null ? 0 : shopQuotes.size();
//    }
//
//    @Override
//    public ShopQuote getItem(int position) {
//        return shopQuotes.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_four_s_quick_shop, null);
//            holder = new ViewHolder();
//            holder.mCheckView = (ImageView) convertView.findViewById(R.id.iv_check);
//            holder.mRlCheckView = (RelativeLayout) convertView.findViewById(R.id.rl_check);
//            holder.mRlQuestionView = (RelativeLayout) convertView.findViewById(R.id.rl_question);
//            holder.mItemNameView = (TextView) convertView.findViewById(R.id.item_name);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        holder.mItemNameView.setText(shopQuotes.get(position).name);
//        if (shopQuotes.get(position).choose ==1) {
//            holder.mCheckView.setImageResource(R.drawable.ic_check_maintain);
//        } else {
//            holder.mCheckView.setImageResource(R.drawable.ic_check_dis_maintain);
//        }
//
//        final ViewHolder finalHolder = holder;
//        holder.mRlCheckView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (shopQuotes.get(position).choose==1) {
//                    shopQuotes.get(position).choose = 0;
//                    finalHolder.mCheckView.setImageResource(R.drawable.ic_check_dis_maintain);
//                } else {
//                    shopQuotes.get(position).choose = 1;
//                    finalHolder.mCheckView.setImageResource(R.drawable.ic_check_maintain);
//                    for (int i = 0; i < shopQuotes.size(); i++) {
//                        if (i != position && shopQuotes.get(position).mutualExclusionGroup.equals(shopQuotes.get(i).mutualExclusionGroup)) {
//                            shopQuotes.get(i).choose = 0;
//                        }
//                    }
//                    notifyDataSetChanged();
//                }
//                boolean isNoCheck = true;
//                for (ShopQuote shopQuote : shopQuotes) {
//                    if (shopQuote.choose ==1) {
//                        isNoCheck = false;
//                    }
//                }
//                if(isNoCheck){
//                    onButtonSelected.buttonNoEnabled();
//                }else {
//                    onButtonSelected.buttonEnabled();
//                }
//            }
//        });
//
//        holder.mRlQuestionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                maintenanceClient.itemIntroduce(shopQuotes.get(position).itemGroupID, new LoadingAnimResponseHandler(context) {
//                    @Override
//                    public void onSuccess(String response) {
//                        GoodsIntroduce goodsIntroduce = JSONUtils.fromJson(response, new TypeToken<GoodsIntroduce>() {
//                        });
//                        ItemIntroduceDialog dialog = new ItemIntroduceDialog(context, R.style.FullWidthDialog, goodsIntroduce, shopQuotes.get(position).name);
//                        Window window = dialog.getWindow();
//                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        window.setGravity(Gravity.BOTTOM);
//                        dialog.show();
//                    }
//                });
//            }
//        });
//
//        return convertView;
//    }
//
//
//    public ArrayList<ShopQuote> returnShopQuote() {
//        return this.shopQuotes;
//    }
//
//    class ViewHolder {
//        RelativeLayout mRlCheckView;
//        RelativeLayout mRlQuestionView;
//        ImageView mCheckView;
//        TextView mItemNameView;
//    }
//}
