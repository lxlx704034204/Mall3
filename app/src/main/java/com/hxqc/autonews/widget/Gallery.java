package com.hxqc.autonews.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;

import java.util.ArrayList;

import hxqc.mall.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Author:李烽
 * Date:2016-09-05
 * FIXME
 * Todo 图片浏览(汽车资讯详情图集模式使用)
 */
public class Gallery extends LinearLayout implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private LinearLayout topBar;
    private TextView index;
    private ImageView back;
    private ImageView share;
    private RelativeLayout detailLayout;
    private View infoContainer;
    private TextView infoTitle;
    private TextView infoDetail;
    private ViewPager galleryViewPager;
    private GalleryAdapter adapter;
    private int count;
    private int indexInt;
    private Activity activity;

    private Context mContext;

    private ArrayList<Data> datas;

    public Gallery(Context context) {
        this(context, null);
    }

    public Gallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Gallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        if (context instanceof Activity)
            activity = (Activity) context;

        initView(context);

        back.setOnClickListener(this);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_gallery, this);
        topBar = (LinearLayout) findViewById(R.id.info_bar);
        index = (TextView) topBar.findViewById(R.id.index);
        back = (ImageView) topBar.findViewById(R.id.back_btn);
        share = (ImageView) topBar.findViewById(R.id.share_btn);

        detailLayout = (RelativeLayout) findViewById(R.id.detail_container);

        infoContainer = findViewById(R.id.info_container);
//        initContanerPosition();
        infoContainer.setOnTouchListener(new ContainerTouchListener());

        infoTitle = (TextView) infoContainer.findViewById(R.id.info_title);
        infoDetail = (TextView) infoContainer.findViewById(R.id.info_detail);
//        infoDetail.setMovementMethod(ScrollingMovementMethod.getInstance());

        maxHeight = infoTitle.getHeight() + infoDetail.getHeight() + ScreenUtil.dip2px(mContext, 30);

        galleryViewPager = (ViewPager) findViewById(R.id.gallery_view_pager);

        galleryViewPager.addOnPageChangeListener(this);
        galleryViewPager.setOnClickListener(this);
    }

    private static float minHeight;
    private static float maxHeight;

    /**
     * 初始化顶部数字显示
     */
    private void initContanerPosition() {
        detailLayout.scrollTo(0, 0);
        minHeight = ScreenUtil.dip2px(mContext, 150);
        maxHeight = infoContainer.getMeasuredHeight();
        DebugLog.i(getClass().getSimpleName(), "maxHeight:" + maxHeight + "minHeight:" + minHeight);
        if (maxHeight >= minHeight)
            detailLayout.scrollTo(0, (int) (minHeight - maxHeight));
//        else {
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) detailLayout.getLayoutParams();
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
//            layoutParams.height = (int) minHeight;
//            detailLayout.setLayoutParams(layoutParams);
//        }
    }


    /**
     * 改变底部文字区域高度（文字高度处理适应文字过多过少情况高度的一致性）
     *
     * @param diffY
     */
    private void setContainerHeight(float diffY) {
        int scrollY = detailLayout.getScrollY();
        if (minHeight > maxHeight)
            return;
        if (diffY > 0) {
            if (scrollY < 0) {
                detailLayout.scrollBy(0, (int) diffY);
            } else {
                detailLayout.scrollTo(0, 0);
            }
        } else if (diffY < 0) {
            if (scrollY >= (minHeight - maxHeight)) {
                detailLayout.scrollBy(0, (int) diffY);
            } else {

                detailLayout.scrollTo(0, (int) (minHeight - maxHeight));
            }
        }
    }

    private class ContainerTouchListener implements OnTouchListener {
        float y = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    y = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float mY = event.getRawY();
                    float diffY = y - mY;
                    DebugLog.i("ContainerTouchListener",
                            "\ndiffY:" + diffY + "\nmY:" + mY + "\ny:" + y);
                    if (Math.abs(diffY) > 1 && Math.abs(diffY) < 50)
                        setContainerHeight(diffY);
                    y = mY;
                    break;
                case MotionEvent.ACTION_UP:
                    y = 0;
                    break;
            }
            return true;
        }
    }

    /**
     * 分享
     * @param shareListener
     */
    public void setShareListener(View.OnClickListener shareListener) {
        share.setOnClickListener(shareListener);
    }

    /**
     * 添加数据
     * @param datas
     */
    public void bindData(ArrayList<Data> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        this.datas = new ArrayList<>();
        this.datas.addAll(datas);
        //添加数据
        count = datas.size();
        adapter = new GalleryAdapter(datas);
        galleryViewPager.setAdapter(adapter);
        infoTitle.setText(datas.get(0).title);
        refrashIndex(0);
    }


    void refrashIndex(int index) {
        this.index.setText((index + 1) + "/" + count);
        this.infoDetail.setText(datas.get(index).info);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initContanerPosition();
            }
        }, 10);
    }

    private boolean isShowBar = true;

    /**
     * 点击图片隐藏顶部数字和底部文字描述
     */
    private void hideBarAndInfoContainer() {
        topBar.setVisibility(GONE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bar_hide);
        topBar.setAnimation(animation);
        animation.start();

        detailLayout.setVisibility(INVISIBLE);
        Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.info_container_hide);
        detailLayout.setAnimation(animation1);
        animation1.start();
    }
    /**
     * 点击图片显示顶部数字和底部文字描述
     */
    private void showBarAndInfoContainer() {
        topBar.setVisibility(VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bar_show);
        topBar.setAnimation(animation);
        animation.start();

        detailLayout.setVisibility(VISIBLE);
        Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.info_container_show);
        detailLayout.setAnimation(animation1);
        animation1.start();
    }

    /**
     * 到指定位置
     *
     * @param index
     */
    public void toItemAtIndex(int index) {
        DebugLog.d(getClass().getSimpleName(), "第" + index + "张图片");
        if (index > -1) {
            galleryViewPager.setCurrentItem(index);
        }
    }

    private void toggle() {
        if (isShowBar) {
            hideBarAndInfoContainer();
            isShowBar = false;
        } else {
            showBarAndInfoContainer();
            isShowBar = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                if (activity != null) {
                    activity.finish();
                }
                break;
            case R.id.gallery:
                //点击隐藏bar
//                toggle();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        DebugLog.d("gallery", position + "");
        refrashIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class Data {
        public String picUrl;
        public String title;
        public String info;
    }

    public class GalleryAdapter extends PagerAdapter {
        public GalleryAdapter(ArrayList<Data> datas) {
            this.datas = datas;
        }

        private ArrayList<Data> datas;

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(getContext());
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            String picUrl = datas.get(position).picUrl;


            ImageUtil.setImageNormalSize(container.getContext(), photoView, picUrl);
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    toggle();
                }

                @Override
                public void onOutsidePhotoTap() {

                }
            });
            return photoView;
        }
    }

}
