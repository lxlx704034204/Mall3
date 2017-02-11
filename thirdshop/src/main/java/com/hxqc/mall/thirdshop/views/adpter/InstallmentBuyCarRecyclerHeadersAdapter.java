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
 * Date 2016年11月18日 17:12:54
 * FIXME
 * 分期购车--车型列表adapter
 */
public class InstallmentBuyCarRecyclerHeadersAdapter extends RecyclerView.Adapter implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private ArrayList<ModelsQuote> modelsQuotes;
    private Context mContext;
    private List<String> groupArray = new ArrayList<>();//品牌组
    private List<ModelsQuote.Series> childArray = new ArrayList<>();//子车系
    protected static final int TYPE_HEAD = 1;
    protected static final int TYPE_NORMAL = 2;
    private ShopInfo mShopInfo;
   private boolean[] isDividerLine; //是否显示分割线


    public InstallmentBuyCarRecyclerHeadersAdapter(ArrayList<ModelsQuote> modelsQuotes, ShopInfo shopInfo, Context context) {
        this.mContext = context;
        this.modelsQuotes = modelsQuotes;
        this.mShopInfo=shopInfo;
        InitData(modelsQuotes);
    }

    public void InitData(ArrayList<ModelsQuote > mModelsQuotes) {
        if (mModelsQuotes.size() > 0) {
            for (ModelsQuote modelsQuote : mModelsQuotes) {
                if (modelsQuote.series.size() > 0) {
                    for (ModelsQuote.Series series:modelsQuote.series){
                        groupArray.add(modelsQuote.brandName);
                        childArray.add(series);
                    }

                }
            }
            InitDividerLine(mModelsQuotes);
        }
    }
    public void InitDividerLine(ArrayList<ModelsQuote > mModelsQuotes) {
            isDividerLine=new boolean[childArray.size()];
            int index = 0;
            for (ModelsQuote modelsQuote : mModelsQuotes) {
                if (modelsQuote.series.size() > 0) {
                    for (int i = 0; i < modelsQuote.series.size(); i++) {
                        if (i == 0)
                            isDividerLine[index]=true;
                        index++;
                    }
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


    @Override
    public long getHeaderId(int position) {
        if(position==0)
            return -1;
        if(modelsQuotes.size()==1)
            return -1;
        for (int i = 0; i < modelsQuotes.size(); i++) {
            String brandName = modelsQuotes.get(i).brandName;
            if (brandName.equals(groupArray.get(position-1))) {
                return i;
            }
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
                return new ViewHolderChild(from.inflate(R.layout.t_fragment_installmentbuycar_item, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position))
        {
            case TYPE_HEAD:
                ((HeadViewHolder) holder).mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_FQGC);
                ((HeadViewHolder) holder).mShopDetailsHeadView.bindData(mShopInfo);
                break;
            case TYPE_NORMAL:
                ViewHolderChild mViewHolderChild=(ViewHolderChild) holder;
                ModelsQuote.Series msSeries = childArray.get(position-1);
                mViewHolderChild .mName.setText(msSeries.getSeriesName());
                mViewHolderChild.mShopPrice.setText(OtherUtil.formatPriceRange(msSeries.priceRange));
                mViewHolderChild.mManufactorPrice.setText(OtherUtil.formatPriceRange(msSeries.priceRange));
                ImageUtil.setImage(mContext, mViewHolderChild.mImage, msSeries.seriesThumb);
                if(isDividerLine[position-1])
                    mViewHolderChild.mDividerLine.setVisibility(View.GONE);
                 else
                    mViewHolderChild.mDividerLine.setVisibility(View.VISIBLE);

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
        TextView mShopPrice;
        TextView mManufactorPrice;
        View mDividerLine;


        public ViewHolderChild(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.installmentbuycar_item_name);
            mImage = (ImageView) itemView.findViewById(R.id.installmentbuycar_item_image);
            mShopPrice = (TextView) itemView.findViewById(R.id.installmentbuycar_item_shop_price);
            mManufactorPrice = (TextView) itemView.findViewById(R.id.installmentbuycar_item_manufactor_price);
            mDividerLine =   itemView.findViewById(R.id.installmentbuycar_item_divider);

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
