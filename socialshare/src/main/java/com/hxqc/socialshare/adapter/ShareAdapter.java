package com.hxqc.socialshare.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.socialshare.R;


/**
 * Author:胡俊杰
 * Date: 2015/9/11
 * FIXME
 * Todo
 */

public class ShareAdapter extends BaseAdapter {
    private static final String TAG = "ShareAdapter";
    //图片资源
    private int[] images = new int[]{R.drawable.umeng_socialize_wechat,
            R.drawable.umeng_socialize_wxcircle, R.drawable.umeng_socialize_sina_on,
            R.drawable.umeng_socialize_qq_on, R.drawable.umeng_socialize_qzone_on,
           R.drawable.umeng_socialize_sms_on};
    private  String[] imageStr={"微信好友","微信朋友圈","微博","QQ好友","QQ空间","短信"};
    private Context context;
    private ViewHolder holder;

    public ShareAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.length;
    }

    @Override
    public String getItem(int i) {
        return imageStr[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.item_view, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.item_view_imageview);
            holder.imageName = (TextView) view.findViewById(R.id.item_view_imagename);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.imageView.setImageResource(images[i]);
        holder.imageName.setText(imageStr[i]);
        return view;
    }

    public static class ViewHolder {
        private ImageView imageView;
        private TextView imageName;

    }


}
