package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.model.AtlasModel;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-04-17
 * FIXME
 * 图集
 */
public class AtlasGridAdapter extends BaseAdapter {

    ArrayList<AtlasModel> atlasModels;
    ArrayList<ImageModel > imageModels;
    Context context;

    public AtlasGridAdapter(ArrayList<AtlasModel> atlasModels, Context context) {
        this.atlasModels = atlasModels;
        this.context = context;
        imageModels = new ArrayList<>();
        for (int i = 0; i < atlasModels.size(); i++) {
            imageModels.add(new ImageModel(atlasModels.get(i).largeURL, atlasModels.get(i).thumbURL));
        }

    }

    public ArrayList<ImageModel> getImageModels() {
        return imageModels;
    }

    @Override
    public int getCount() {
        return atlasModels == null ? 0 : atlasModels.size();
    }

    @Override
    public AtlasModel getItem(int position) {
        return atlasModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        atlasHolder holder;
        if (convertView != null) {
            view = convertView;
            holder = (atlasHolder) view.getTag();
        } else {
            holder = new atlasHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_atlas_grid_image, parent,false);
            holder.imageView = (ImageView) view.findViewById(R.id.siv_atlas_image);
            view.setTag(holder);
        }
        ImageUtil.setImage(context, holder.imageView, atlasModels.get(position).thumbURL);
        return view;
    }

    class atlasHolder {
        ImageView imageView;
    }
}
