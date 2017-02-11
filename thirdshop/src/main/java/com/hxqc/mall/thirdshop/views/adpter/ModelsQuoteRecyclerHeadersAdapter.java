package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ModelsQuote;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:liaoguilong
 * Date 2015-12-08
 * FIXME
 * Todo
 */
public class ModelsQuoteRecyclerHeadersAdapter extends RecyclerView.Adapter implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private ArrayList<ModelsQuote> modelsQuotes;
    private Context mContext;
    private List<String> groupArray = new ArrayList<>();//品牌组
    private List<ModelsQuote.Series> childArray = new ArrayList<>();//子车系
    protected static final int TYPE_HEAD = 1;
    protected static final int TYPE_NORMAL = 2;
    private ShopInfo mShopInfo;
 

    public ModelsQuoteRecyclerHeadersAdapter(ArrayList<ModelsQuote> modelsQuotes,ShopInfo shopInfo, Context context) {
        this.mContext = context;
        this.modelsQuotes = modelsQuotes;
        this.mShopInfo=shopInfo;
        InitData(modelsQuotes);
    }

    public void InitData(ArrayList<ModelsQuote > mModelsQuotes) {
        if (mModelsQuotes.size() > 0)
            for (ModelsQuote modelsQuote : mModelsQuotes) {
                if (modelsQuote.series.size() > 0)
                    for (ModelsQuote.Series mSeries : modelsQuote.series) {
                        groupArray.add(modelsQuote.brandName);
                        childArray.add(mSeries);
                    }
            }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return TYPE_HEAD;
        else
            return TYPE_NORMAL;
    }

    /**
     * 获取车系
     * @param Position 下标位置
     * @return
     */
    public ModelsQuote.Series getSeriesByPosition(int Position){
        return childArray.get(Position);
    }

    /**
     * 返回Group高度
     * @return
     */
    public int getOffSet(){
        if(modelsQuotes.size()==1) return 0;
         return  mContext.getResources().getDimensionPixelOffset(R.dimen.group_recycler_height_32);
    }
    
    
    /**
     * 根据车系ID获取 Position
     * @param seriesID
     * @return
     */
    public int getIndexBySeriesID(String seriesID){
        for (int i = 0; i < childArray.size(); i++) {
            String mSeriesID = childArray.get(i).seriesID;
            if(seriesID.equals(mSeriesID))
                return  i;
        }
        return -1;
    }

    /**
     * 根据品牌名称获取 Position
     * @param brandName
     * @return
     */
    public int getPositionBySeriesName(String brandName){
        for (int i = 0; i < modelsQuotes.size(); i++) {
            String mBrandName = modelsQuotes.get(i).brandName;
            if(brandName.equals(mBrandName))
                return  i;
        }
        return -1;
    }
    

    @Override
    public long getHeaderId(int position) {
        if(position==0)
            return -1;
        if(modelsQuotes.size()==1)
            return -1;
        for (int i = 0; i < modelsQuotes.size(); i++) {
            String brandName = modelsQuotes.get(i).brandName;
            if (brandName.equals(groupArray.get(position-1)))
                return i;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.t_modelsquote_item_group, null);
        return new ViewHolderGroup(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((ViewHolderGroup) viewHolder).mName.setText(groupArray.get(position-1));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(mContext);
        switch (viewType)
        {
            case TYPE_HEAD:
                return new HeadViewHolder(from.inflate(R.layout.t_fragment_modelsquote_item_head, null));
            case TYPE_NORMAL:
                return new ViewHolderChild(from.inflate(R.layout.t_fragment_modelsquote_item, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position))
        {
            case TYPE_HEAD:
                ((HeadViewHolder) holder).mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_CXBJ);
                ((HeadViewHolder) holder).mShopDetailsHeadView.bindData(mShopInfo);
                break;
            case TYPE_NORMAL:
                ModelsQuote.Series msSeries = childArray.get(position-1);
                ((ViewHolderChild) holder).mName.setText(msSeries.getSeriesName());
                ((ViewHolderChild) holder).mPrice.setText(OtherUtil.formatPriceRange(msSeries.priceRange));
                ImageUtil.setImage(mContext, ((ViewHolderChild) holder).mImage, msSeries.seriesThumb);
                holder.itemView.setTag(msSeries);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return childArray.size()+1;
    }

    class ViewHolderChild extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView mImage;
        TextView mPrice;

        public ViewHolderChild(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.modelsquote_item_name);
            mImage = (ImageView) itemView.findViewById(R.id.modelsquote_item_image);
            mPrice = (TextView) itemView.findViewById(R.id.modelsquote_item_price);
        }
    }


    class ViewHolderGroup extends RecyclerView.ViewHolder {
        TextView mName;


        public ViewHolderGroup(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.modelsquote_item_group_label);

        }
    }
    class  HeadViewHolder  extends RecyclerView.ViewHolder{
        ShopDetailsHeadView mShopDetailsHeadView;
        public HeadViewHolder(View itemView) {
            super(itemView);
            mShopDetailsHeadView= (ShopDetailsHeadView) itemView.findViewById(R.id.modelsquote_item_headView);
        }
    }
}
