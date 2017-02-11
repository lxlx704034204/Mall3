package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.base.BaseAdapter;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.newcar.UserDiscussDetail;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.ScreenUtil;

import java.util.List;


/**
 * 口碑
 * Created by zhaofan on 2016/8/4.
 */
@Deprecated
public class UserGradeAdapter extends BaseAdapter<UserDiscussDetail, BaseAdapter.BaseViewHolder> {
    private ImageAdapter[] mAdapter;

    public UserGradeAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_discuss, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, int position, final UserDiscussDetail data) {
        final ViewHolder vh = (ViewHolder) holder;

        vh.userName.setText(data.userInfo.nickName);
        vh.content.setText(data.content);
        vh.grade.setText(String.format("%s分", data.grade.average));
        try {
            String date1 = data.time.split(" ")[0];
            String date2 = data.time.split(" ")[1].replace(".", ":").substring(0, 5);
            vh.date.setText(date1 + " " + date2);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

//        vh.content.setText(data.content);
//        ImageUtil.build(context, data.userInfo.userAvatar, R.drawable.umeng_socialize_default_avatar)
//                .bitmapTransform(new GlideCircleTransform(context))
//                .into(vh.userImg);
        ImageUtil.setImage(context, vh.userImg, data.userInfo.userAvatar, R.drawable.umeng_socialize_default_avatar);
        android.view.ViewGroup.LayoutParams pp = vh.divide.getLayoutParams();
        pp.width = (ScreenUtil.getScreenWidth(context) - dip2px(20)) / 5 * 3 + dip2px(20);
        vh.divide.setLayoutParams(pp);

        if (data.images.isEmpty()) {
            vh.gv.setVisibility(View.GONE);
        } else {
            vh.gv.setVisibility(View.VISIBLE);
            vh.gv.setAdapter(mAdapter[position]);
        }
    }


    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void initData() {
        mAdapter = new ImageAdapter[getCount()];
        for (int i = 0; i < getCount(); i++) {
            mAdapter[i] = new ImageAdapter(context);
            mAdapter[i].setData(dataSource.get(i).images);
        }
    }

    private class ViewHolder extends BaseViewHolder {
        private TextView userName, grade, date, content;
        private ImageView userImg;
        private GridView gv;
        private View divide;

        public ViewHolder(View itemView) {
            super(itemView);

            userName = getView(R.id.user_name);
            grade = getView(R.id.grade_value);
            date = getView(R.id.time);
            content = getView(R.id.content);
            userImg = getView(R.id.user_img);
            gv = getView(R.id.user_grade_gv);
            divide = getView(R.id.divide);
        }
    }


    private class ImageAdapter extends BaseAdapter<String, BaseAdapter.BaseViewHolder> {


        public ImageAdapter(Context context) {
            super(context);
        }

        @Override
        protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_imageview, parent, false));
        }

        @Override
        protected void bindViewHolder(BaseViewHolder holder, View convertView, final int position, final String data) {
            final ViewHolder vh = (ViewHolder) holder;
            android.view.ViewGroup.LayoutParams pp = vh.Img.getLayoutParams();
            int w = (ScreenUtil.getScreenWidth(context) - dip2px(20)) / 5;
            pp.width = w;
            pp.height = w;
            vh.Img.setLayoutParams(pp);

            ImageUtil.setImageNormalSize(context, vh.Img,data);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> mList = dataSource;
                    ActivitySwitcherThirdPartShop.toPhotoView(context, mList.toArray(new String[mList.size()]), position);
                }
            });

        }

        private class ViewHolder extends BaseViewHolder {
            private ImageView Img;

            public ViewHolder(View itemView) {
                super(itemView);

                Img = getView(R.id.img);
            }
        }
    }
}

