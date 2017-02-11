package com.hxqc.mall.core.views;

import android.app.Activity;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.WindowDecorActionBar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.ScreenUtil;

import java.lang.ref.WeakReference;


/**
 * Created by zhaofan
 */
public class ErrorView {
    public static LruCache<String, WeakReference<View>> map = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 10));
    public static final int HIGHT = -1;
    public String TAG_DEF = "default_tag";
    private int marginTop;
    private RelativeLayout lay, error_view;
    private Activity context;
    private View v;
    private TextView info, btn;
    private ImageView img;
    LinearLayout.LayoutParams mParams;

    private ErrorView(Activity context, View v) {
        this.context = context;
        v.setLayoutParams(setDefaultParams());
        //set tag
        TAG_DEF = context.toString();
        this.v = v;
    }

    private ViewGroup.LayoutParams setDefaultParams() {
        mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mParams.gravity = Gravity.BOTTOM;
        //default margin top
        marginTop = ScreenUtil.dip2px(context, 55);
        if (context instanceof AppCompatActivity) {
            ActionBar actionbar = ((AppCompatActivity) context).getSupportActionBar();
            if (actionbar != null) {
                if (actionbar instanceof WindowDecorActionBar) {
                    marginTop = 0;
                } else {
                    TypedValue tv = new TypedValue();
                    if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)) {
                        int h = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
                        marginTop = (h);
                    }
                }
            }
        }
        mParams.topMargin = marginTop;

        return mParams;
    }

    private void bindView(View v) {
        error_view = (RelativeLayout) v.findViewById(R.id.error_view);
        info = (TextView) v.findViewById(R.id.info_tv);
        btn = (TextView) v.findViewById(R.id.btn);
        img = (ImageView) v.findViewById(R.id.ImageView);
        lay = (RelativeLayout) v.findViewById(R.id.main);
        lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    /**
     * @return
     */
    public static ErrorView builder(Activity context) {
        View v = View.inflate(context.getApplicationContext(), R.layout.view_error, null);
        return new ErrorView(context, v);
    }


    public View getView() {
        return map.get(TAG_DEF).get();
    }


    public View getView(String tag) {
        return map.get(tag).get();
    }

    /**
     * @param tag
     * @return
     */
    public ErrorView setTag(String tag) {
        TAG_DEF = tag;
        map.put(tag, new WeakReference<>(v));
        return this;
    }

    public static void remove(String tag) {
        if (map.get(tag) != null) {
            View v = map.get(tag).get();
            if (v != null && v.getParent() != null)
                ((ViewGroup) v.getParent()).removeView(v);
        }
    }

    /**
     * @param margin (px)
     * @return
     */
    public ErrorView topMargin(int margin) {
        margin = margin == HIGHT ? marginTop : margin + marginTop;
        mParams.topMargin = margin;
        return this;
    }

    private void display() {
        if (map.get(TAG_DEF) == null || map.get(TAG_DEF).get() == null)
            setTag(TAG_DEF);

        View v = getView();
        bindView(v);
        if (v != null && v.getParent() != null)
            ((ViewGroup) v.getParent()).removeView(v);

        LinearLayout lay = new LinearLayout(context);
        lay.addView(v, mParams);

        LinearLayout.LayoutParams mParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mParams2.width = LinearLayout.LayoutParams.MATCH_PARENT;//窗口的宽和高
        mParams2.height = LinearLayout.LayoutParams.MATCH_PARENT;
        mParams2.gravity = Gravity.BOTTOM;
        if (lay.getParent() != null) {
            ((ViewGroup) lay.getParent()).removeView(lay);
        }
        context.addContentView(lay, mParams2);
    }


    public void showBlank() {
        display();
        error_view.setVisibility(View.GONE);
    }


    public void showEmpty() {
        display();
        info.setText("暂无数据");
        btn.setText("返回");
        img.setImageResource(R.drawable.no_date);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                context.finish();
            }
        });
    }


    public void showFailed(View.OnClickListener listener) {
        display();
        failedCtrl(listener);

    }

    public void failedCtrl(final View.OnClickListener listener) {
        info.setText("网络连接失败");
        btn.setText("重试");
        img.setImageResource(R.drawable.no_line);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    dismiss();
                    listener.onClick(v);
                }
            }
        });
    }


    public void showCustom(String content, String btnStr, int imgRes, final boolean isHidden, final View.OnClickListener listener) {
        display();
        info.setText(content);
        if (TextUtils.isEmpty(btnStr))
            btn.setVisibility(View.GONE);
        else
            btn.setText(btnStr);
        img.setImageResource(imgRes);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isHidden) {
                    dismiss();
                }
                if (listener != null) {
                    listener.onClick(v);

                } else
                    context.finish();
            }
        });
    }


    private void dismiss() {
        View v = getView();
        if (v != null && v.getParent() != null)
            ((ViewGroup) v.getParent()).removeView(v);
    }

}
