package com.hxqc.mall.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hxqc.mall.core.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Function: 下拉菜单控件
 *
 * @author 袁秉勇
 * @since 2016年3月14日
 */
public class DropDownMenu extends LinearLayout {
    private FragmentActivity mRelatedActivity;

    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //分割线颜色
    private int dividerColor = 0xffcccccc;
    private float dividerPadding = 0;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x88000000;
    //tab字体大小
    private int menuTextSize = 14;

    private int menuHeight;

    //tab选中图标
    private int menuSelectedIcon = R.drawable.drop_down_selected_icon;
    //tab未选中图标
    private int menuUnselectedIcon = R.drawable.drop_down_unselected_icon;

    private int drawableMarginLeft = 4;

    private boolean inflateFragment = false;

    private ArrayList< Fragment > fragments = new ArrayList<>();

    private ArrayList< Fragment > attachedFragments = new ArrayList<>();

    private Fragment lastFragment;

    private ArrayList< LinearLayout > tabViews;


    public void setInflateFragment(boolean inflateFragment) {
        this.inflateFragment = inflateFragment;
    }


    public DropDownMenu(Context context) {
        super(context, null);
        mRelatedActivity = (FragmentActivity) context;
    }


    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mRelatedActivity = (FragmentActivity) context;
    }


    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRelatedActivity = (FragmentActivity) context;

        setOrientation(VERTICAL);

        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        int underlineHeight;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_underlineColor, underlineColor);
        underlineHeight = a.getDimensionPixelOffset(R.styleable.DropDownMenu_underlineHeight, 1);
        dividerColor = a.getColor(R.styleable.DropDownMenu_dividerColor, dividerColor);
        dividerPadding = a.getDimension(R.styleable.DropDownMenu_dividerPadding, 0);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_textSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_textUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_menuBackgroundColor, menuBackgroundColor);
        menuHeight = a.getDimensionPixelOffset(R.styleable.DropDownMenu_menuHeight, 0);
        maskColor = a.getColor(R.styleable.DropDownMenu_maskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_menuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuUnselectedIcon, menuUnselectedIcon);
        drawableMarginLeft = a.getDimensionPixelOffset(R.styleable.DropDownMenu_drawableMarginLeft, 4);
        a.recycle();

        //初始化tabMenuView并添加到tabMenuView
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuHeight > 0 ? menuHeight : ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(context);
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, underlineHeight));
        underLine.setBackgroundColor(underlineColor);
        addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(containerView, 2);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //初始化内容区域
        View view = findViewById(R.id.drop_down_content);
        if (view != null) {
            if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
            containerView.addView(view);
        }
    }


    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param inflateObjects
     */
    public void setDropDownMenu(@NonNull List< String > tabTexts, @NonNull List< Object > inflateObjects) {
        if (tabTexts.size() != inflateObjects.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        tabViews = new ArrayList<>();

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        maskView.setVisibility(GONE);
        containerView.addView(maskView);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setId(R.id.fragment);
        popupMenuViews.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popupMenuViews.setPadding(0, 0, 0, getContext().getResources().getDisplayMetrics().heightPixels * 3 / 23);
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews);

        if (!inflateFragment) {
            for (int i = 0; i < inflateObjects.size(); i++) {
                if (inflateObjects.get(i) instanceof View == false) throw new IllegalArgumentException("which you inflate is not View");
                View view = (View) inflateObjects.get(i);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                popupMenuViews.addView(view, i);
            }
        } else {
            for (int i = 0; i < inflateObjects.size(); i++) {
                if (inflateObjects.get(i) instanceof Fragment == false) throw new IllegalArgumentException("which you inflate is not Fragment");
                Fragment fragment = (Fragment) inflateObjects.get(i);
                fragments.add(fragment);
            }
        }
    }


    private void addTab(@NonNull List< String > tabTexts, int i) {
        final LinearLayout tabView = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.dropdownmenu_inflate_view, null);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tabView.findViewById(R.id.text_right_arrow).getLayoutParams();
        lp.setMargins(drawableMarginLeft, 0, 0, 0);
        tabView.setLayoutParams(layoutParams);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tabView);
            }
        });

        tabMenuView.addView(tabView);
        tabViews.add(tabView);
        setTabText(i, tabTexts.get(i));
        setImageResource(i, menuUnselectedIcon);


//        final TextView tab = new TextView(getContext());
//        tab.setSingleLine();
//        tab.setEllipsize(TextUtils.TruncateAt.END);
//        tab.setGravity(Gravity.CENTER);
//        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
//        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
//        layoutParams.setMargins(dpTpPx(5), dpTpPx(12), dpTpPx(5), dpTpPx(12));
//        tab.setLayoutParams(layoutParams);
//        tab.setTextColor(textUnselectedColor);
//        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
//        tab.setText(tabTexts.get(i));
////        tab.setPadding(dpTpPx(5), dpTpPx(12), dpTpPx(5), dpTpPx(12));
//        //添加点击事件
//        tab.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchMenu(tab);
//            }
//        });
//        tabMenuView.addView(tab);


        //添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            LinearLayout.LayoutParams layoutParams1 = new LayoutParams(dpTpPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams1.setMargins(0, dpTpPx(dividerPadding), 0, dpTpPx(dividerPadding));
            view.setLayoutParams(layoutParams1);
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }


    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != 1) {
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setText(text);
        }
    }


    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    public void setTabClickable(int position, boolean clickable) {
        tabViews.get(position).setClickable(clickable);
    }


    public void setTabText(int position, String text) {
        if (tabViews != null && tabViews.size() > position) {
            ((TextView)tabViews.get(position).findViewById(R.id.menu_top_text)).setText(text);
        }
    }

    public void setTabTextColor(int position, int colorID) {
        if (tabViews != null && tabViews.size() > position) {
            ((TextView)tabViews.get(position).findViewById(R.id.menu_top_text)).setTextColor(colorID);
        }
    }

    public void setImageResource(int positon, int ImgID) {
        if (tabViews != null && tabViews.size() > positon) {
            ((ImageView)tabViews.get(positon).findViewById(R.id.text_right_arrow)).setImageResource(ImgID);
        }
    }


    /**
     * 关闭菜单
     */
    public void closeMenu() {
        setImageResource(current_tab_position / 2, menuUnselectedIcon);
//        ((TextView) tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
//        ((TextView) tabMenuView.getChildAt(current_tab_position)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
        popupMenuViews.setVisibility(View.GONE);
//        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
        maskView.setVisibility(GONE);
//        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
        current_tab_position = -1;

    }


    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }


    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            if (target == tabMenuView.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));

                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
//                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                        showFragmentOrView(i / 2);
                    } else {
//                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                        showFragmentOrView(i / 2);
                    }

                    current_tab_position = i;
//                    ((TextView) tabMenuView.getChildAt(i)).setTextColor(textSelectedColor);
//                    ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuSelectedIcon), null);
                    setImageResource(i / 2, menuSelectedIcon);
                }
            } else {
//                ((TextView) tabMenuView.getChildAt(i)).setTextColor(textUnselectedColor);
//                ((TextView) tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
                setImageResource(i / 2, menuUnselectedIcon);
                if (!inflateFragment) popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }


    /** 根据传入参数的展示不同的布局 **/
    private void showFragmentOrView(int i) {
        if (inflateFragment) {  //传入的参数为Fragment时
            Fragment newFragment = fragments.get(i);
            FragmentTransaction fragmentTransaction = mRelatedActivity.getSupportFragmentManager().beginTransaction();
            if (!attachedFragments.contains(newFragment)) {
                fragmentTransaction.add(R.id.fragment, newFragment);
                if (lastFragment != null) fragmentTransaction.hide(lastFragment);
                attachedFragments.add(newFragment);
            } else {
                if (lastFragment != null && lastFragment != newFragment) fragmentTransaction.hide(lastFragment).show(newFragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
            lastFragment = newFragment;
        } else {    // 传入的参数为View时
            popupMenuViews.getChildAt(i).setVisibility(VISIBLE);
        }
    }


    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
