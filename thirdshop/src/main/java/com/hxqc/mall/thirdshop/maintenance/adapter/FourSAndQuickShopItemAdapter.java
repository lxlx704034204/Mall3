package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.GoodsIntroduce;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ShopQuoteItem;
import com.hxqc.mall.thirdshop.maintenance.views.ItemIntroduceDialog;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-06-21
 * FIXME
 * Todo
 */
public class FourSAndQuickShopItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ShopQuoteItem> shopQuoteItems;
    private FourSAndQuickHelper fourSAndQuickHelper;
    private MaintenanceClient maintenanceClient;

    public FourSAndQuickShopItemAdapter(Context context , ArrayList<ShopQuoteItem> shopQuoteItems){
        this.context = context;
        maintenanceClient = new MaintenanceClient();
        this.shopQuoteItems = shopQuoteItems;
        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
    }

    public interface onMutexItemListener{
        void returnMutexItem();
    }

    public void setOnMutexItemListener(onMutexItemListener onMutexItemListener){
        this.onMutexItemListener = onMutexItemListener;
    }

    private onMutexItemListener onMutexItemListener;

    public interface onButtonSelectedListener{
        void onButtonSelected();
    }

    public void setOnButtonSelectedListener(onButtonSelectedListener onButtonSelectedListener){
        this.onButtonSelectedListener = onButtonSelectedListener;
    }

    private onButtonSelectedListener onButtonSelectedListener;

    @Override
    public int getCount() {
        return shopQuoteItems.size();
    }

    @Override
    public ShopQuoteItem getItem(int position) {
        return shopQuoteItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = null;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.layout_4s_quick_shop_item_adpter,null);
            viewHolder = new ViewHolder();
            viewHolder.mCheckView = (ImageView) view.findViewById(R.id.iv_check);
            viewHolder.mItemNameView = (TextView) view.findViewById(R.id.item_name);
            viewHolder.mHelpView = (ImageView) view.findViewById(R.id.iv_help);
            viewHolder.mItemNumView = (TextView) view.findViewById(R.id.item_num);
            viewHolder.mRlView = (RelativeLayout) view.findViewById(R.id.rl);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mItemNumView.setText(position+1 +".");
        viewHolder.mItemNameView.setText(shopQuoteItems.get(position).name);

        if(shopQuoteItems.get(position).choose == 1){
            viewHolder.mCheckView.setVisibility(View.VISIBLE);
//            if(shopQuoteItems.get(position).isPlatformRecommend){
//                viewHolder.mCheckView.setImageResource(R.drawable.maintain_recomment_n);
//            }else {
//                viewHolder.mCheckView.setImageResource(R.drawable.maintain_check_n);
//            }
            viewHolder.mCheckView.setImageResource(R.drawable.maintain_check_n);
            viewHolder.mRlView.setBackgroundResource(R.drawable.shape_maintance_4s_shop_check);
        }else {
            viewHolder.mCheckView.setVisibility(View.INVISIBLE);
            viewHolder.mRlView.setBackgroundResource(R.drawable.shape_maintance_4s_shop_no_check);
        }


        viewHolder.mRlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.i("TAG", "position:  " + position);
                if(shopQuoteItems.get(position).choose == 1){
                    shopQuoteItems.get(position).choose = 0;
                    viewHolder.mCheckView.setVisibility(View.INVISIBLE);
                    viewHolder.mRlView.setBackgroundResource(R.drawable.shape_maintance_4s_shop_no_check);
                }else {
                    shopQuoteItems.get(position).choose = 1;
                    viewHolder.mCheckView.setVisibility(View.VISIBLE);
                    if(shopQuoteItems.get(position).isPlatformRecommend){
                        viewHolder.mCheckView.setImageResource(R.drawable.maintain_recomment_n);
                    }else {
                        viewHolder.mCheckView.setImageResource(R.drawable.maintain_check_n);
                    }
                    viewHolder.mRlView.setBackgroundResource(R.drawable.shape_maintance_4s_shop_check);
                    fourSAndQuickHelper.getMutexItems(shopQuoteItems.get(position));
                }
                fourSAndQuickHelper.getBothItems(shopQuoteItems.get(position),shopQuoteItems.get(position).choose);
                onMutexItemListener.returnMutexItem();
                onButtonSelectedListener.onButtonSelected();
            }
        });

        viewHolder.mHelpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maintenanceClient.itemIntroduce(shopQuoteItems.get(position).itemGroupID, new LoadingAnimResponseHandler(context) {
                    @Override
                    public void onSuccess(String response) {
                        GoodsIntroduce goodsIntroduce = JSONUtils.fromJson(response, new TypeToken<GoodsIntroduce>() {
                        });
                        ItemIntroduceDialog dialog = new ItemIntroduceDialog(context, R.style.FullWidthDialog, goodsIntroduce, shopQuoteItems.get(position).name);
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.BOTTOM);
                        dialog.show();

                    }
                });
            }
        });
        return view;
    }


    class ViewHolder{
        private TextView mItemNameView;
        private TextView mItemNumView;
        private ImageView mHelpView;
        private ImageView mCheckView;
        private RelativeLayout mRlView;
    }
}
