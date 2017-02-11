package com.hxqc.autonews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.hxqc.autonews.model.pojos.AutoInfoHomeData;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.model.pojos.InfoTag;
import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.autonews.util.ToAutoInfoDetailUtil;
import com.hxqc.mall.auto.util.ScreenUtil;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;


/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯列表适配器（已不适用）
 */
@Deprecated
public class AutoInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BaseSliderView.OnSliderClickListener {

    private static final long SLIDER_DURATION = 2000;
    private Context mContext;

    private ArrayList<AutoInformation> mBanner;
    private ArrayList<AutoInformation> mInfoList;

    private static final int TYPE_BANNER = 10;//头部广告轮播
    private static final int TYPE_LIST_TEXT = 20;//普通资讯
    private static final int TYPE_LIST_IMAGES = 30;//图集item
    private SliderLayout mSliderLayout;
    private TextView bannerTitle;
    private ArrayList<RadioButton> radioButtons;
    private SparseBooleanArray indicData;//记录小白（indicator）的的状态

    public AutoInfoListAdapter(AutoInfoHomeData autoInfoHomeData, Context context) {
        mContext = context;
        if (autoInfoHomeData != null) {
            mBanner = autoInfoHomeData.banner;
            mInfoList = autoInfoHomeData.infoList;
        } else {
            mBanner = new ArrayList<>();
            mInfoList = new ArrayList<>();
        }
        radioButtons = new ArrayList<>();

        indicData = new SparseBooleanArray();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else {
            if (mInfoList.get(position - 1).getType() == AutoInformation.Type.Images)
                return TYPE_LIST_IMAGES;
            else return TYPE_LIST_TEXT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderTxt holderTxt = new ViewHolderTxt(LayoutInflater.from(mContext)
                .inflate(R.layout.item_auto_information_text, null));
        ViewHolderImages holderImages = new ViewHolderImages(LayoutInflater.from(mContext)
                .inflate(R.layout.item_auto_information_images, null));
        mSliderLayout = new SliderLayout(mContext);
//        mSliderLayout.setViewPagerParent(parent);
        ViewHolderBanner holderBanner = new ViewHolderBanner(mSliderLayout);

        switch (viewType) {
            case TYPE_BANNER:
                return holderBanner;
            case TYPE_LIST_IMAGES:
                return holderImages;
            case TYPE_LIST_TEXT:
                return holderTxt;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        final int index = position - 1;
        switch (itemViewType) {
            case TYPE_BANNER:
                setSlider((ViewHolderBanner) holder, mBanner);
                break;
            case TYPE_LIST_IMAGES:
                setImagesItem((ViewHolderImages) holder, index);
                break;
            case TYPE_LIST_TEXT:
                setTextItem((ViewHolderTxt) holder, index);
                break;
        }
    }

    private void setTextItem(ViewHolderTxt holder, int index) {
        ViewHolderTxt holderTxt = holder;
        final AutoInformation information = mInfoList.get(index);
        ArrayList<String> thumbImages = information.thumbImage;
        holderTxt.infoTitle.setText(information.title);
        holderTxt.infoDate.setText(DateUtil.fullDate2Day(information.date));
//        holderTxt.infoType.setText(information.kind);
        if (information.tags != null) {
            setTypeTags(holderTxt.infoType, information.tags);
        }
        if (thumbImages != null && thumbImages.size() > 0) {
            ImageUtil.setImage(mContext, holderTxt.autoIcon, thumbImages.get(0));
        } else ImageUtil.setImage(mContext, holderTxt.autoIcon, "");
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toAutoInfoDetail(information.infoID + "");
                ToAutoInfoDetailUtil
                        .onToNextPage(information.infoID, information.getType(),mContext);
            }
        });
    }

    private void setImagesItem(ViewHolderImages holder, final int index) {
        ViewHolderImages holderImages = holder;
        final AutoInformation information = mInfoList.get(index);
        ArrayList<String> thumbImages = information.thumbImage;
//        holderImages.type.setText(information.kind);
        if (information.tags != null) {
            setTypeTags(holderImages.type, information.tags);
        }
//        holderImages.date.setText(information.date);
        holderImages.date.setText(DateUtil.fullDate2Day(information.date));
        holderImages.title.setText(information.title);
        if (thumbImages != null && thumbImages.size() > 2) {
            ImageUtil.setImage(mContext, holderImages.img1, thumbImages.get(0));
            ImageUtil.setImage(mContext, holderImages.img2, thumbImages.get(1));
            ImageUtil.setImage(mContext, holderImages.img3, thumbImages.get(2));
        } else {
            ImageUtil.setImage(mContext, holderImages.img1, "");
            ImageUtil.setImage(mContext, holderImages.img2, "");
            ImageUtil.setImage(mContext, holderImages.img3, "");
        }

        holderImages.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toAutoImagesDetail(information.infoID + "", information.title);
                ToAutoInfoDetailUtil
                        .onToNextPage(information.infoID, information.getType(),mContext);
            }
        });
    }

    /**
     * 设置标签
     *
     * @param textView
     * @param tags
     */
    private void setTypeTags(TextView textView, List<InfoTag> tags) {
        StringBuilder stringBuilder = new StringBuilder();
        for (InfoTag tag : tags) {
            stringBuilder.append(tag.tagName).append(",");
        }
        String s = stringBuilder.toString();
        if (!TextUtils.isEmpty(s)) {
            s = s.substring(0, s.length() - 1);
            textView.setText(s);
        }
    }

    /**
     * 设置广告轮播
     *
     * @param holder
     * @param mBanner
     */
    private void setSlider(final ViewHolderBanner holder, final ArrayList<AutoInformation> mBanner) {
        this.bannerTitle = holder.title;
        holder.mSlider.removeAllSliders();
        if (mBanner == null) {
            holder.mSlider.setSliderOnlyOneView(null);
            holder.mSlider.setEnabled(false);
            holder.mSlider.setFocusableInTouchMode(false);
            holder.mSlider.setFilterTouchesWhenObscured(false);
            holder.mSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        } else {
            if (mBanner.size() > 1) {
                for (int i = 0; i < (mBanner.size() > 5 ? 5 : mBanner.size()); i++) {
                    AutoInformation autoInformation = mBanner.get(i);
                    ArrayList<String> thumbImages = autoInformation.thumbImage;
                    if (thumbImages != null && thumbImages.size() > 0) {
                        DefaultSliderView sliderView = new DefaultSliderView(mContext);
                        sliderView.empty(R.drawable.sliderimage_pic_normal_slider)
                                .error(R.drawable.sliderimage_pic_normal_slider);
                        sliderView.description(autoInformation.title).image(thumbImages.get(0))
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                .setOnSliderClickListener(this);
                        sliderView.bundle(new Bundle());
                        sliderView.getBundle().putParcelable("extra", autoInformation);
                        holder.mSlider.addSlider(sliderView);
                        addIndicator(i, holder);
                    }
                }

                holder.mSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                holder.mSlider.setCustomAnimation(new DescriptionAnimation());
                holder.mSlider.setDuration(SLIDER_DURATION);
                if (holder.mSlider.getRealAdapter().getCount() > 0) {
                    int currentPosition = holder.mSlider.getCurrentPosition();
                    setChoosed(currentPosition);
                }
                holder.mSlider.startAutoCycle();
                holder.mSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
//                        DebugLog.i("onPageSelected", "radioButtons.size()=" + radioButtons.size());
//                        DebugLog.i("onPageSelected", "radioGroup.getChildCount()=" + holder.indicatorGroup.getChildCount());
                        int i = indicData.indexOfValue(true);
                        indicData.append(i, false);
                        indicData.append(position, true);
                        setChoosed(position);
//                        DebugLog.i("onPageSelected", "indicData=" + indicData.toString());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            } else if (mBanner.size() == 1) {
                ArrayList<String> thumbImage = mBanner.get(0).thumbImage;
                if (thumbImage != null && thumbImage.size() > 0) {
                    holder.mSlider.setSliderOnlyOneView(thumbImage.get(0));
                    holder.mSlider.setEnabled(false);
                    holder.mSlider.setFocusableInTouchMode(false);
                    holder.mSlider.setFilterTouchesWhenObscured(false);
                    holder.mSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                    addIndicator(0, holder);
                    holder.mSlider.sliderOnlyOneView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击唯一的一个按钮
                            AutoInformation information = mBanner.get(0);
//                            if (autoInformation.getType() == AutoInformation.Type.Images)
//                                toAutoImagesDetail(autoInformation.infoID + "", autoInformation.title);
//                            else toAutoInfoDetail(autoInformation.infoID + "");
                            ToAutoInfoDetailUtil
                                    .onToNextPage(information.infoID,   information.getType(),mContext);
                        }
                    });
                }
                if (indicData.indexOfValue(true) == -1) {
                    indicData.put(0, true);
                }
                setChoosed(indicData.indexOfValue(true));
            }
        }
    }




    private void setChoosed(int i) {
        allFalse();
        radioButtons.get(i).setChecked(true);
        bannerTitle.setText(mBanner.get(i).title);
    }

    private void allFalse() {
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isChecked())
                radioButtons.get(i).setChecked(false);
        }
    }

    private void addIndicator(int i, ViewHolderBanner holder) {
        int count = mBanner.size() > 5 ? 5 : mBanner.size();//最多为5
        if (holder.indicatorGroup.getChildCount() < count) {
            RadioButton rb = new RadioButton(mContext);
            rb.setBackgroundResource(R.drawable.bg_indicator);
            rb.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            int size = ScreenUtil.dip2px(mContext, 10);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size
                    , size);
            int margin = ScreenUtil.dip2px(mContext, 5);
            lp.leftMargin = margin;
            rb.setLayoutParams(lp);
            holder.indicatorGroup.addView(rb);
            if (radioButtons.size() < count) {
                radioButtons.add(rb);
            }
        }
        if (indicData.size() < count) {
            indicData.put(i, false);
        }
    }

    public void notifyData(AutoInfoHomeData autoInfoHomeData) {
        mBanner.clear();
        mBanner.addAll(autoInfoHomeData.banner);
        mInfoList.clear();
        mInfoList.addAll(autoInfoHomeData.infoList);
        notifyDataSetChanged();
    }

    /**
     * 更新列表数据
     *
     * @param autoInformation
     */
    public void notifyListData(ArrayList<AutoInformation> autoInformation) {
        mInfoList.addAll(autoInformation);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mInfoList == null ? 0 : mInfoList.size() + 1;
    }

    /**
     * 在activity销毁的时候调用
     */
    public void onDestroy() {
        if (mSliderLayout != null) {
            mSliderLayout.destroy();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //点击banner跳转到指定的页面
        AutoInformation extra = slider.getBundle().getParcelable("extra");
        String infoID = "";
        String infoTitle = "";
        if (extra != null) {
            infoID = extra.infoID + "";
            infoTitle = extra.title;
        }
        ToAutoInfoDetailUtil
                .onToNextPage(infoID,   extra.getType(),mContext);
//        if (extra.getType() == AutoInformation.Type.Images)
//            toAutoImagesDetail(infoID, infoTitle);
//        else toAutoInfoDetail(infoID);
    }

    /**
     * 去图文详情界面
     */
    private void toAutoInfoDetail(String infoID) {
        ActivitySwitchAutoInformation.toAutoInfoDetail(mContext, infoID);
    }

    /**
     * 去图集界面
     */
    private void toAutoImagesDetail(String infoID, String title) {
        ActivitySwitchAutoInformation.toAutoGallery(mContext, infoID );
    }

    class ViewHolderTxt extends RecyclerView.ViewHolder {
        private final TextView gTitle;
        private ImageView autoIcon;
        private TextView infoTitle;
        private TextView infoType;
        private TextView infoDate;
        private View rootView;

        public ViewHolderTxt(View itemView) {
            super(itemView);
            rootView = itemView;
            gTitle = (TextView) itemView.findViewById(R.id.group_title);
            autoIcon = (ImageView) itemView.findViewById(R.id.auto_icon);
            infoDate = (TextView) itemView.findViewById(R.id.info_date);
            infoType = (TextView) itemView.findViewById(R.id.info_type);
            infoTitle = (TextView) itemView.findViewById(R.id.info_title);
        }
    }

    class ViewHolderImages extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private TextView type;
        private ImageView img1, img2, img3;
        private TextView gTitle;
        private View rootView;

        public ViewHolderImages(View itemView) {
            super(itemView);
            rootView = itemView;
            title = (TextView) itemView.findViewById(R.id.item_title);
            gTitle = (TextView) itemView.findViewById(R.id.group_title);
            date = (TextView) itemView.findViewById(R.id.item_date);
            type = (TextView) itemView.findViewById(R.id.item_type);
            img1 = (ImageView) itemView.findViewById(R.id.img1);
            img2 = (ImageView) itemView.findViewById(R.id.img2);
            img3 = (ImageView) itemView.findViewById(R.id.img3);
        }
    }

    class ViewHolderBanner extends RecyclerView.ViewHolder {
        private SliderLayout mSlider;
        private TextView title;
        private LinearLayout indicatorGroup;

        public ViewHolderBanner(View itemView) {
            super(itemView);

            mSlider = (SliderLayout) itemView;
            View bottomContainer = LayoutInflater.from(mContext).inflate(R.layout.layout_slider_bottom, null);
            title = (TextView) bottomContainer.findViewById(R.id.title);
            indicatorGroup = (LinearLayout) bottomContainer.findViewById(R.id.indicator_group);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            bottomContainer.setLayoutParams(lp);
            mSlider.addView(bottomContainer);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            mSlider.setLayoutParams(layoutParams);
        }
    }
}
