package com.hxqc.mall.core.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * liaoguilong
 * Created by CPR113 on 2016/3/1.
 * 筛选的Adapter
 */
public class ScreenPopWindowAdapter  extends BaseAdapter {

    private Context mContext;
    private String chooseStr="";
    private ArrayList<Screen> mListData;
    public ScreenPopWindowAdapter(Context context) {
            this.mContext=context;
            initData();
    }
    public void initData(){
        mListData=new ArrayList<>();
        mListData.add(new Screen("全部",""));
//        mListData.add(new Screen("洗车","50"));
        mListData.add(new Screen("买车","10"));
        mListData.add(new Screen("修车","20"));
        mListData.add(new Screen("车服务","30"));
//        mListData.add(new Screen("用品备件","40"));
    }


    public void setCheckColorChange(String str) {
        this.chooseStr = str;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Screen getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView ==null)
        {
            convertView=LayoutInflater.from(mContext).inflate(R.layout.activity_myorder_simple_list_item, parent,false);
            viewHolder=new ViewHolder();
            viewHolder.conditionName= (TextView) convertView.findViewById(R.id.myorder_pop_orderType);
            viewHolder.conditionState= (ImageView) convertView.findViewById(R.id.myorder_pop_check);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.conditionName.setText(getItem(position).Name);
        if (chooseStr.equals(getItem(position).toString())) {
            viewHolder.conditionName.setTextColor(mContext.getResources().getColor(com.hxqc.mall.core.R.color.cursor_orange));
            viewHolder.conditionState.setVisibility(View.VISIBLE);
        } else {
            viewHolder.conditionName.setTextColor(mContext.getResources().getColor(com.hxqc.mall.core.R.color.straight_matter_and_secondary_text));
            viewHolder.conditionState.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        public TextView conditionName;
        public ImageView conditionState;
    }
   public class Screen{
        String Name;
        String Type;

        public Screen(String name, String type) {
            Name = name;
            Type = type;
        }

        @Override
        public String toString() {
            return Type;
        }
    }


}
