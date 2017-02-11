package com.hxqc.mall.views.loan;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hxqc.mall.core.model.loan.LoanDepartmentType;
import com.hxqc.mall.core.model.loan.LoanItemFinanceModel;
import com.hxqc.mall.core.views.stickylist.StickyListHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-11-02
 * FIXME 金融机构列表
 * Todo
 */
public class LoanAdapter extends BaseAdapter
        implements StickyListHeadersAdapter, SectionIndexer {


    Context context;
    LayoutInflater layoutInflater;
    ArrayList< LoanDepartmentType > financeModels;
    List< LoanItemFinanceModel > itemData;
    LoanItemFinanceModel checkedModel;

    private int[] mSectionIndices;
    private String[] mSectionLetters;

    public LoanAdapter(Context context, ArrayList< LoanDepartmentType > financeModels, String fID) {
        this.context = context;
        this.financeModels = financeModels;

        layoutInflater = LayoutInflater.from(context);
        initData();
        if (!TextUtils.isEmpty(fID)) {
            for (LoanItemFinanceModel m : itemData) {
                if (m.financeID.equals(fID))
                    this.checkedModel = m;
            }
        }
        mSectionLetters = getSectionLetters();
    }

    public void initData() {
        itemData = new ArrayList<>();
        mSectionIndices = new int[financeModels.size()];
        for (int i = 0; i < financeModels.size(); i++) {
            LoanDepartmentType loanDepartmentType = financeModels.get(i);
            for (int a = 0; a < loanDepartmentType.finance.size(); a++) {
                LoanItemFinanceModel itemFinanceModel = loanDepartmentType.finance.get(a);
                itemFinanceModel.typeHeadID = i;
                itemData.add(itemFinanceModel);
                if (a == 0) {
                    mSectionIndices[i] = itemData.indexOf(itemFinanceModel);
                }
            }
        }
    }

    private String[] getSectionLetters() {
        String[] letters = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = financeModels.get(i).getTitle();
        }
        return letters;
    }

    public void notifyData(ArrayList< LoanDepartmentType > financeModels) {
        this.financeModels = financeModels;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemData.size();
    }

    @Override
    public LoanItemFinanceModel getItem(int position) {
        return itemData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHeadHolder holder;
//
//        if (convertView == null) {
//            holder = new ViewHeadHolder();
//            convertView = layoutInflater.inflate(R.layout.test_list_item_layout, parent, false);
//            holder.text = (TextView) convertView.findViewById(R.id.text);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHeadHolder) convertView.getTag();
//        }

//        DebugLog.i("loan_list", "getView进来了");
        final LoanFileItemView loanFileItemView = new LoanFileItemView(context);

        if (position + 1 == itemData.size()) {
            loanFileItemView.setDividerViewVisible(View.GONE);
        }

        loanFileItemView.setFinanceModel(itemData.get(position));
        loanFileItemView.setTag(getItem(position));
        loanFileItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedModel == null) {
                    checkedModel = (LoanItemFinanceModel) loanFileItemView.getTag();
                } else {
                    if (getItem(position).equals(checkedModel)) {
                        loanFileItemView.setChecked(false);
                        checkedModel = null;
                    } else {
                        checkedModel = (LoanItemFinanceModel) loanFileItemView.getTag();
                    }
                }
                notifyDataSetChanged();
            }
        });


        if (checkedModel != null) {
            if (checkedModel.title.equals(getItem(position).title)) {
                loanFileItemView.setChecked(true);
            } else {
                loanFileItemView.setChecked(false);
            }
        }

        if (getItem(position).equals(checkedModel)) {
            loanFileItemView.setChecked(true);
        } else {
            loanFileItemView.setChecked(false);
        }

//        holder.text.setText(mCountries[position]);
//        DebugLog.i("loan_list", "设置值了:" + itemData.get(position).toString());
        return loanFileItemView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = layoutInflater.inflate(R.layout.view_loan_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text_head_loan);
            holder.textChooseView = (TextView) convertView.findViewById(R.id.text_head_choose);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        String headerStr = mSectionLetters[((int) itemData.get(position).typeHeadID)];
        holder.text.setText(headerStr);
        if (checkedModel != null) {
//            DebugLog.i("test_bug", "headID:" + getHeaderId(position) + " modelid:" + checkedModel.typeHeadID);

            if (getHeaderId(position) == checkedModel.typeHeadID) {
                holder.textChooseView.setText(checkedModel.title);
            } else {
                holder.textChooseView.setText("");
            }
        } else {
            holder.textChooseView.setText("");
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return itemData.get(position).typeHeadID;
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public String[] getSections() {
        return mSectionLetters;
    }

    public LoanItemFinanceModel getCheckedModel() {
        return checkedModel;
    }

    class HeaderViewHolder {
        TextView text;
        TextView textChooseView;
    }

}
