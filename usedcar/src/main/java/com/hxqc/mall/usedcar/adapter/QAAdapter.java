package com.hxqc.mall.usedcar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.QA;
import com.hxqc.mall.usedcar.model.QADetail;
import com.hxqc.widget.ListViewNoSlide;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 恒信质保ListViewNoSlide adapter
 * Created by huangyi on 16/1/8.
 */
public class QAAdapter extends BaseAdapter {

    OnItemClickListener mOnItemClickListener;
    Context mContext;
    ArrayList<QA> mQA;
    LayoutInflater mLayoutInflater;

    public QAAdapter(OnItemClickListener mOnItemClickListener, Context mContext, ArrayList<QA> mQA) {
        this.mOnItemClickListener = mOnItemClickListener;
        this.mContext = mContext;
        this.mQA = mQA;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null == mQA ? 0 : mQA.size();
    }

    @Override
    public QA getItem(int position) {
        return mQA.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_qa, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mParentView = (LinearLayout) convertView.findViewById(R.id.qa_parent);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.qa_name);
            mViewHolder.mListView = (ListViewNoSlide) convertView.findViewById(R.id.qa_list_single);
            mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.qa_price);
            mViewHolder.mStatusView = (ImageView) convertView.findViewById(R.id.qa_status);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        QA model = mQA.get(position);

        mViewHolder.mNameView.setText(model.item_name);
        QuickAdapter<QADetail> adapter = new QuickAdapter<QADetail>(mContext, R.layout.item_qa_detail, model.item_detail) {
            @Override
            protected void convert(BaseAdapterHelper helper, QADetail item) {
                helper.setText(R.id.qa_detail_value, item.key + ": " + item.value);
            }
        };
        mViewHolder.mListView.setAdapter(adapter);
        mViewHolder.mPriceView.setText(" " + OtherUtil.amountFormat(model.item_price, false));
        if (model.isChecked) {
            mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#f9fafb"));
            mViewHolder.mStatusView.setImageResource(R.drawable.ic_repair_record_sel);
        } else {
            mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#ffffff"));
            mViewHolder.mStatusView.setImageResource(R.drawable.radiobutton_normal);
        }
        mViewHolder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mOnItemClickListener) mOnItemClickListener.onItemClick(position);
            }
        });
        mViewHolder.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != mOnItemClickListener) mOnItemClickListener.onItemClick(position);
            }
        });
        return convertView;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class ViewHolder {
        LinearLayout mParentView;
        TextView mNameView;
        ListViewNoSlide mListView;
        TextView mPriceView;
        ImageView mStatusView;
    }

}
