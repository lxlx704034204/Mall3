package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zf
 */
public abstract class BaseListViewAdapter<E, V extends BaseListViewAdapter.BaseViewHolder> extends android.widget.BaseAdapter {

    private Context context;
    private List<E> dataSource = new ArrayList<>();

    public BaseListViewAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }


    public void setData(List<E> dataSource) {
        setData(dataSource, true);
    }

    /**
     * @param isClear true刷新 false加载
     */
    public void setData(List<E> data, boolean isClear) {
        if (isClear) {
            this.dataSource.clear();
            this.dataSource = data;
        } else {
            addDate(data);
        }
        notifyDataSetChanged();
    }

    // 拼接list
    public void addDate(List<E> list) {
        for (int i = 0; i < list.size(); i++) {
            this.dataSource.add(list.get(i));
        }
    }

    //只加一个数据
    public void addData(E data) {
        this.dataSource.add(data);
        notifyDataSetChanged();
    }

    //通过下标移除一条数据
    public void removeData(int position) {
        this.dataSource.remove(position);
        notifyDataSetChanged();
    }

    //通过对象移除一条数据
    public void removeData(E data) {
        this.dataSource.remove(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.dataSource.size();
    }


    @Override
    public E getItem(int position) {
        return this.dataSource.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V viewHolder = null;
        if (convertView == null) {
            viewHolder = createViewHolder(position, parent);
            if (viewHolder == null || viewHolder.getRootView() == null) {
                throw new NullPointerException("createViewHolder不能返回null或view为null的实例");
            }
            convertView = viewHolder.getRootView();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (V) convertView.getTag();
        }
        //给当前复用的holder一个正确的position
        viewHolder.setPosition(position);
        bindViewHolder(viewHolder, convertView, position, getItem(position));
        return viewHolder.getRootView();
    }

    protected abstract V createViewHolder(int position, ViewGroup parent);

    protected abstract void bindViewHolder(V holder, View convertView, int position, E data);

    public static class BaseViewHolder {
        private View rootView;
        private SparseArray<View> viewCache = new SparseArray<>();
        private int position = -1;

        public View getRootView() {
            return rootView;
        }

        void setPosition(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public BaseViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public <R> R getView(@IdRes int viewID) {
            View cachedView = viewCache.get(viewID);
            if (null == cachedView) {
                cachedView = rootView.findViewById(viewID);
                viewCache.put(viewID, cachedView);
            }
            return (R) cachedView;
        }
    }
}