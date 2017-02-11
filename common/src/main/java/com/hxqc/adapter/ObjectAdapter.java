/**
 * 2014年10月20日
 * ObjectAdapter.java
 * TODO
 *
 * @version 1.0
 * author 胡俊杰
 */
package com.hxqc.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 * author 胡俊杰
 */
public class ObjectAdapter< T > extends BaseAdapter {
    protected Context ctx;
    protected LayoutInflater layoutInflater;
    protected List< T > mData;
    private int[] mTo;
    private int mResource;
    private LayoutInflater mInflater;
    private ViewBinder mViewBinder;
    private List< Field > mfields;

    /**
     * 类似SimpleAdapter，
     *
     * @param context
     * @param data
     * @param mObject
     *         Object的Class
     * @param resource
     *         item，layout
     * @param from
     *         object字段String
     * @param to
     *         item，id
     */
    public ObjectAdapter(Context context, List< T > data, Class mObject,
                         int resource, String[] from, int[] to) {
        super();

        ctx = context;
        mData = data;
        mResource = resource;
        mTo = to;
        mInflater = LayoutInflater.from(context);
        mfields = new ArrayList<>();
        Field field;
        for (String s : from) {
            try {
                field = mObject.getField(s);
                mfields.add(field);
            } catch (NoSuchFieldException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void addList(ArrayList< T > list) {
        if (list == null) {
            return;
        }
        if (this.mData != null) {
            this.mData.addAll(list);
        } else {
            this.mData = list;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mData == null || mData.size() == 0) {
            return 0;
        } else {
            return mData.size();
        }

    }

    @Override
    public T getItem(int arg0) {
        // TODO Auto-generated method stub
        return mData.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView,
                                        ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }

        try {
            bindView(position, v);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return v;
    }

    private void bindView(int position, View view)
            throws IllegalAccessException, IllegalArgumentException {
        final T dataSet = mData.get(position);
        if (dataSet == null) {
            return;
        }

        final ViewBinder binder = mViewBinder;
        final int[] to = mTo;
        final int count = to.length;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {

                final Object data = mfields.get(i).get(dataSet);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }

                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }
                if (!bound) {
                    if (v instanceof Checkable) {
                        if (v instanceof TextView) {
                            setViewText((TextView) v, text, position);
                        } else {
                            throw new IllegalStateException(v.getClass()
                                    .getName()
                                    + " should be bound to a Boolean, not a "
                                    + (data == null ? "<unknown type>"
                                    : data.getClass()));
                        }
                        if (data instanceof Boolean) {
                            ((Checkable) v).setChecked((Boolean) data);

                        } else {
                            setViewCheck(v, data, position);
                        }

                    } else if (v instanceof TextView) {
                        setViewText((TextView) v, text, position);
                    } else if (v instanceof ImageView) {
                        if (data instanceof Integer) {
                            setViewImage((ImageView) v, (Integer) data,
                                    position);
                        } else {
                            setViewImage((ImageView) v, text, position);
                        }
                    } else {
                        throw new IllegalStateException(
                                v.getClass().getName()
                                        + " is not a "
                                        + " view that can be bounds by this SimpleAdapter");
                    }
                }
            }
        }
    }

    /**
     * Returns the {@link android.widget.SimpleAdapter.ViewBinder} used to bind data to views.
     *
     * @return a ViewBinder or null if the binder does not exist
     * @see #setViewBinder(android.widget.SimpleAdapter.ViewBinder)
     */
    public ViewBinder getViewBinder() {
        return mViewBinder;
    }

    /**
     * Sets the binder used to bind data to views.
     *
     * @param viewBinder
     *         the binder used to bind data to views, can be null
     *         to remove the existing binder
     * @see #getViewBinder()
     */
    public void setViewBinder(ViewBinder viewBinder) {
        mViewBinder = viewBinder;
    }

    public void setViewCheck(View v, Object data, int position) {

    }

    public void setViewImage(ImageView v, int value, int position) {
        v.setImageResource(value);
    }

    public void setViewImage(ImageView v, String value, int position) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    /**
     * Called by bindView() to set the text for a TextView but only if
     * there is no existing ViewBinder or if the existing ViewBinder
     * cannot handle binding to a TextView.
     *
     * @param v
     *         TextView to receive text
     * @param text
     *         the text to be set for the TextView
     */
    public void setViewText(TextView v, String text, int position) {
        v.setText(text);
    }

}
