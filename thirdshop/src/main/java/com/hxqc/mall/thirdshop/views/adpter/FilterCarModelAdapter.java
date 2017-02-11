package com.hxqc.mall.thirdshop.views.adpter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.views.sticklistviewbyzf.StickyListHeadersAdapter;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.ShopBrandActivity;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 车型筛选列表adapter
 * Created by zhaofan on 2016/10/21.
 */
public class FilterCarModelAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Context context;
    private List<ModelInfo> data = new ArrayList<>();


    public FilterCarModelAdapter(Context context) {
        this.context = context;
    }


    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }


    public void setData(List<ModelInfo> dataSource) {
        setData(dataSource, true);
    }

    /**
     * @param isClear true刷新 false加载
     */
    public void setData(List<ModelInfo> data, boolean isClear) {
        if (isClear) {
            this.data.clear();
            this.data = data;
        } else {
            addDate(data);
        }
        notifyDataSetChanged();
    }

    // 拼接list
    public void addDate(List<ModelInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            this.data.add(list.get(i));
        }
    }


    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHodler viewHodler = convertView == null ? new ViewHodler() : (ViewHodler) convertView.getTag();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_filter_carmodel, parent, false);

            viewHodler.title = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(viewHodler);
        }

        final ModelInfo entity = (ModelInfo) getItem(position);

        viewHodler.title.setText(entity.getModelName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(entity);
                EventBus.getDefault().post(ShopBrandActivity.FINISH);
                EventBus.getDefault().post("CarChooseListActivity_finish");
                ((Activity) context).finish();
     /*   if (status == 0)
                    ActivitySwitcherThirdPartShop.toCarCompare(context, "com.hxqc.carcompare.ui.addcar.CarCompareListActivity");
          else if (status == 1)
                    ActivitySwitcherThirdPartShop.toCarCompare(context, "com.hxqc.carcompare.ui.compare.CarCompareDetailActivity");*/

            }
        });

        return convertView;
    }


    class ViewHodler {
        private TextView title;
    }


    public View getHeaderView(final int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_filter_model_tag, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);


            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        holder.text.setText(data.get(position).tag);

        return convertView;
    }

    public String getHeaderId(int position) {
        return data.get(position).tag;
    }

    class HeaderViewHolder {
        TextView text;
    }

    public Object[] getSections() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getPositionForSection(int section) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getSectionForPosition(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


}