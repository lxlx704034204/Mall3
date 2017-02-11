package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ModelsQuote;

import java.util.ArrayList;

/**
 *  
 * 车型报价列表
 * @author liaoguilong
 * @since 2015年12月1日
 */
public class ModelsQuoteAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    ArrayList<ModelsQuote > modelsQuotes = new ArrayList<>();
    Context mContext;

    public void setmOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener) {
        this.mOnRecyclerViewItemClickListener = mOnRecyclerViewItemClickListener;
    }

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener=null;
    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
    }

    @Override
    public void onClick(View v) {
            if(mOnRecyclerViewItemClickListener !=null)
                mOnRecyclerViewItemClickListener.onItemClick(v);
    }
    
    public ModelsQuoteAdapter(ArrayList<ModelsQuote> modelsQuotes, Context context) {
        this.modelsQuotes = modelsQuotes;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.t_fragment_modelsquote_item, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ModelsQuote modelsQuote = modelsQuotes.get(position);
//        ((ViewHolder)holder).mName.setText(modelsQuote.getSeriesName());
//        ((ViewHolder)holder).mPrice.setText(modelsQuote.getPriceRange());      
//        if (!TextUtils.isEmpty(modelsQuote.getSeriesThumb())) {
//            Picasso.with(mContext).load(modelsQuote.getSeriesThumb()).placeholder(R.drawable.pic_normal).error(R.drawable.pic_normal).into(((ViewHolder) holder).mImage);
//        }
        holder.itemView.setTag(modelsQuote);
    }

    @Override
    public int getItemCount() {
        return modelsQuotes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView mImage;
        TextView mPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.modelsquote_item_name);
            mImage = (ImageView) itemView.findViewById(R.id.modelsquote_item_image);
            mPrice = (TextView) itemView.findViewById(R.id.modelsquote_item_price);
        }
    }
}
