package com.hxqc.mall.auto.adapter;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * FIXME
 * Todo 车辆信息列表
 */
  /*public class AutoInfoDataBaseAdapter extends BaseAdapter {

  private static final String TAG = "AutoInfoAdapterV2";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int isDefault;//默认条目
    private List<SqliteMyAuto> mAutoGroups;

    public AutoInfoDataBaseAdapter(Context context, List<SqliteMyAuto> autoGroups) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mAutoGroups = autoGroups;
//        DebugLog.i(TAG, autoGroups.get(0).toString());
    }

    public void notifyData(List<SqliteMyAuto> autoGroups) {
        this.mAutoGroups = autoGroups;
        this.notifyDataSetChanged();
    }

   *//* public ArrayList< MyAuto > dfdj(ArrayList< MyAuto > autoGroups) {
        if(!autoGroups.get(0).getIsDefault().equals("20")) {
            ArrayList<MyAuto> myAutoV2s = new ArrayList<MyAuto>();
            for(int i=0;i<autoGroups.size();i++) {

            }
        }
        return null;
    }*//*

    @Override
    public int getCount() {
        return mAutoGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_auto_info_v2, null);
            viewHolder.mAutoInfoBrandThumb = (ImageView) convertView.findViewById(R.id.item_auto_info_log);
            viewHolder.mAutoInfoType = (TextView) convertView.findViewById(R.id.item_auto_info_type);
            viewHolder.mAutoInfoPlateNum = (TextView) convertView.findViewById(R.id.item_auto_info_plate_num);
            viewHolder.mAutoDrivingDistance = (TextView) convertView.findViewById(R.id.item_auto_info_driving_distance);

            viewHolder.mAutoIsDefaultView = (ImageView) convertView.findViewById(R.id.auto_is_default);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(mAutoGroups.get(position).getIsDefault()!=0) {
            if (mAutoGroups.get(position).getIsDefault()==20) {
                isDefault = position;
                viewHolder.mAutoIsDefaultView.setVisibility(View.GONE);
            } else {
                viewHolder.mAutoIsDefaultView.setVisibility(View.GONE);
            }
        }
        setValue(viewHolder, mAutoGroups.get(position));
        return convertView;
    }

    public void setValue(ViewHolder viewHolder, SqliteMyAuto myAutoV2) {
        viewHolder.mAutoInfoType.setText(myAutoV2.getAutoModel());
        viewHolder.mAutoInfoPlateNum.setText(myAutoV2.getPlateNumber());
        viewHolder.mAutoDrivingDistance.setText(myAutoV2.getDrivingDistance());
        if (TextUtils.isEmpty(myAutoV2.getBrandThumb())) {
            viewHolder.mAutoInfoBrandThumb.setImageResource(R.drawable.pic_normal);
        } else {
            Picasso.with(mContext).load(myAutoV2.getBrandThumb())
                    .placeholder(R.drawable.pic_normal)
                    .error(R.drawable.pic_normal).into(viewHolder.mAutoInfoBrandThumb);
        }
    }

    public int getIsDefault() {
        return isDefault;
    }

    public List<SqliteMyAuto> getmAutoGroups() {
        return mAutoGroups;
    }

    class ViewHolder {
        private ImageView mAutoIsDefaultView;
        private ImageView mAutoInfoBrandThumb;
        private TextView mAutoInfoType;
        private TextView mAutoInfoPlateNum;
        private TextView mAutoDrivingDistance;
    }


}*/
