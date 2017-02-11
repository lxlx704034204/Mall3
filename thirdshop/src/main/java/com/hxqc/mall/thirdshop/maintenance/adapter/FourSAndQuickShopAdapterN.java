package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ShopQuote;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ShopQuoteItem;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-06-21
 * FIXME
 * Todo
 */
public class FourSAndQuickShopAdapterN extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ShopQuote> shopQuotes;
    private FourSAndQuickShopItemAdapter itemAdapter;
    private FourSAndQuickHelper fourSAndQuickHelper;

    private RecyclerView mRecyclerView;

    private View VIEW_FOOTER;
    private View VIEW_HEADER;

    //Type
    private int TYPE_NORMAL = 1000;
    private int TYPE_HEADER = 1001;
    private int TYPE_FOOTER = 1002;


    public interface onButtonSelected{
        void buttonEnabled();

        void buttonNoEnabled();
    }

    private  onButtonSelected onButtonSelected;

    public void setOnButtonSelected(onButtonSelected onButtonSelected){
        this.onButtonSelected = onButtonSelected;
    }


    public FourSAndQuickShopAdapterN(Context context){
        this.context = context;
        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
        shopQuotes = fourSAndQuickHelper.getShopQuotes();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new ViewHolder(VIEW_FOOTER);
        } else if (viewType == TYPE_HEADER) {
            return new ViewHolder(VIEW_HEADER);
        } else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_4s_quick_shop_adpter_n,null));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (!isHeaderView(position) && !isFooterView(position)) {
            if (haveHeaderView()) position--;
            final ShopQuote shopQuote = shopQuotes.get(position);

            ((ViewHolder)holder).mGroupTagView.setText(shopQuote.groupTag);
            itemAdapter = new FourSAndQuickShopItemAdapter(context,shopQuote.items);
            ((ViewHolder)holder).mGridView.setAdapter(itemAdapter);
            ((ViewHolder)holder).mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

            itemAdapter.setOnMutexItemListener(new FourSAndQuickShopItemAdapter.onMutexItemListener() {
                @Override
                public void returnMutexItem() {
                    shopQuotes = fourSAndQuickHelper.getShopQuotes();

                    notifyDataSetChanged();
                }
            });

            itemAdapter.setOnButtonSelectedListener(new FourSAndQuickShopItemAdapter.onButtonSelectedListener() {
                @Override
                public void onButtonSelected() {
                    boolean isNoCheck = true;
                    for(ShopQuote shopQuote : shopQuotes){
                        for(ShopQuoteItem shopQuoteItem : shopQuote.items){
                            if(shopQuoteItem.choose == 1){
                                isNoCheck = false;
                            }
                        }
                    }
                    if(isNoCheck){
                        onButtonSelected.buttonNoEnabled();
                    }else {
                        onButtonSelected.buttonEnabled();
                    }
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        int count = (shopQuotes == null ? 0 : shopQuotes.size());
        if (VIEW_FOOTER != null) {
            count++;
        }

        if (VIEW_HEADER != null) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        } else if (isFooterView(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        try {
            if (mRecyclerView == null && mRecyclerView != recyclerView) {
                mRecyclerView = recyclerView;
            }
            ifGridLayoutManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View getLayout(int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            ifGridLayoutManager();
            notifyItemInserted(0);
        }

    }

    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            throw new IllegalStateException("footerView has already exists!");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            ifGridLayoutManager();
            notifyItemInserted(getItemCount() - 1);
        }
    }

    private void ifGridLayoutManager() {
        if (mRecyclerView == null) return;
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                    ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() :
                            1;
                }
            });
        }
    }

    private boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    private boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }

    public ArrayList<ShopQuote> returnShopQuotes(){
        return  shopQuotes;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mGroupTagView;
        private ListViewNoSlide mGridView;
        public ViewHolder(View itemView) {
            super(itemView);
            mGridView = (ListViewNoSlide) itemView.findViewById(R.id.grid_view);
            mGroupTagView = (TextView) itemView.findViewById(R.id.groupTag);
        }
    }
}
