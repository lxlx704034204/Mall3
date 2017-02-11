package com.hxqc.mall.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;

/**
 * Author:李烽
 * Date:2016-03-26
 * FIXME
 * Todo 标题内容横条
 */
public class BaseMapView extends LinearLayout {
    private static final int INPUT = 0, CLICKABLE = 1, SHOW = 2, VALUE_ICON = 3, VALUE_CLICK = 4, VALUE_CLICK_MORE = 5;
    private static final int LEFT = 0, RIGHT = 1;
    private TextView keyTextView;
    private TextView valueTextView;
    private LinearLayout valueContainer;

    public EditText getInputEditText() {
        return inputEditText;
    }

    public TextView getValueTextView() {
        return valueTextView;
    }

    public TextView getKeyTextView() {
        return keyTextView;
    }

    private EditText inputEditText;
    private ImageView value_icon, more;
    private String key, value, hint;
    private Type type;
    private Gravity gravity;
    private Drawable valueIcon;
    protected OnValueClickListener onValueClickListener;

    private BaseMapView rootView;

    public BaseMapView(Context context) {
        this(context, null);
    }

    public BaseMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_normal_map, this);
        rootView = this;
        keyTextView = (TextView) findViewById(R.id.key_tv_1);
        valueContainer = (LinearLayout) findViewById(R.id.value_container_1);
        valueTextView = (TextView) findViewById(R.id.default_value_tv_1);
        inputEditText = (EditText) findViewById(R.id.map_item_input);
        value_icon = (ImageView) findViewById(R.id.value_icon);
        more = (ImageView) findViewById(R.id.more);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseMapView);
        key = typedArray.getString(R.styleable.BaseMapView_keyText);
        value = typedArray.getString(R.styleable.BaseMapView_valueText);
        hint = typedArray.getString(R.styleable.BaseMapView_inputHint);
        type = getType(typedArray.getInt(R.styleable.BaseMapView_itemType, SHOW));
        gravity = getGravity(typedArray.getInt(R.styleable.BaseMapView_valueGravity, RIGHT));
        valueIcon = typedArray.getDrawable(R.styleable.BaseMapView_valueIcon);
        typedArray.recycle();
        updateView();
    }

    public String getValue() {
        if (type == Type.INPUT)
            value = inputEditText.getText().toString();
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        updateView();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        updateView();
    }

    /**
     * 设置valueicon
     *
     * @param thumb
     */
    public void setValueIcon(String thumb) {
        ImageUtil.setImage(getContext(), value_icon, thumb);
    }

    protected void updateView() {
        switch (gravity) {
            case LEFT:
                valueContainer.setGravity(android.view.Gravity.START | android.view.Gravity.CENTER_VERTICAL);
                break;
            case RIGHT:
                valueContainer.setGravity(android.view.Gravity.END | android.view.Gravity.CENTER_VERTICAL);
                break;
        }
        switch (type) {
            case SHOW:
                show();
                break;
            case CLICKABLE:
                clickable();
                break;
            case INPUT:
                input();
                break;
            case VALUE_ICON:
                valueIcon();
                break;
            case VALUE_CLICK:
                valueClick();
            case VALUE_CLICK_MORE:
                valueClickMore();
                break;
        }
        keyTextView.setText(key);
    }

    private void valueClickMore() {
        valueTextView.setTextColor(Color.parseColor("#2196f3"));
        valueTextView.setVisibility(VISIBLE);
        more.setVisibility(VISIBLE);
        valueTextView.setText(value);
        valueTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onValueClickListener)
                    onValueClickListener.onValueClick(rootView, value);
            }
        });
    }

    private void valueClick() {
        valueTextView.setTextColor(Color.parseColor("#2196f3"));
        valueTextView.setVisibility(VISIBLE);
        valueTextView.setText(value);
        valueTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onValueClickListener)
                    onValueClickListener.onValueClick(rootView, value);
            }
        });
    }

    private void valueIcon() {
        valueTextView.setVisibility(VISIBLE);
        value_icon.setVisibility(VISIBLE);
        value_icon.setImageDrawable(valueIcon);
        valueTextView.setText(value);
    }

    private void input() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherUtil.openSoftKeyBoard(getContext(), inputEditText);
            }
        });
        inputEditText.setVisibility(VISIBLE);
        inputEditText.setHint(hint);
        if (!TextUtils.isEmpty(value))
            inputEditText.setText(value);
    }

    private void clickable() {
        valueTextView.setVisibility(VISIBLE);
        more.setVisibility(VISIBLE);
        valueTextView.setText(value);
    }

    private void show() {
        more.setVisibility(GONE);
        valueTextView.setVisibility(VISIBLE);
        valueTextView.setText(value);
    }

    private Type getType(int typeValue) {
        switch (typeValue) {
            case INPUT:
                return Type.INPUT;
            case CLICKABLE:
                return Type.CLICKABLE;
            case SHOW:
                return Type.SHOW;
            case VALUE_ICON:
                return Type.VALUE_ICON;
            case VALUE_CLICK:
                return Type.VALUE_CLICK;
            case VALUE_CLICK_MORE:
                return Type.VALUE_CLICK_MORE;
            default:
                return Type.SHOW;
        }
    }

    private Gravity getGravity(int gravity) {
        switch (gravity) {
            case LEFT:
                return Gravity.LEFT;
            case RIGHT:
                return Gravity.RIGHT;
            default:
                return Gravity.RIGHT;
        }
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
        if (gravity == Gravity.LEFT) {
            LinearLayout.LayoutParams layoutParams = (LayoutParams) valueTextView.getLayoutParams();
            layoutParams.weight = 1;
            valueTextView.setLayoutParams(layoutParams);
        }
        updateView();
    }

    /**
     * 设置数据
     *
     * @param key
     * @param value
     */
    public void setKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
        updateView();
    }

    public String getKey() {
        return key;
    }

    public enum Type {
        INPUT, CLICKABLE, SHOW, VALUE_ICON, VALUE_CLICK, VALUE_CLICK_MORE
    }

    public enum Gravity {
        LEFT, RIGHT
    }

    public interface OnValueClickListener {
        void onValueClick(BaseMapView view, String value);
    }
}
