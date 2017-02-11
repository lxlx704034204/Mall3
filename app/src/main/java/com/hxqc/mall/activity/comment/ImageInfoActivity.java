package com.hxqc.mall.activity.comment;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.hxqc.mall.core.base.BaseActivity;
import com.hxqc.mall.fragment.comment.ImageInfoFragment;
import com.hxqc.util.DebugLog;
import com.umeng.analytics.MobclickAgent;

import hxqc.mall.R;


public class ImageInfoActivity extends BaseActivity {

    ImageInfoFragment imageInfoFragment;

    FrameLayout bg;
	private int mLeft;
	private int mTop;
	private float mScaleX;
	private float mScaleY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_info);
        bg = (FrameLayout) findViewById(R.id.thread_info_photo_content_layout);

        Bundle bundle = getIntent().getExtras();
        imageInfoFragment = new ImageInfoFragment();
        imageInfoFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.thread_info_photo_content_layout,
                imageInfoFragment).setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out).commit();
        initView();
    }

//    int clickCount = 0;
//    float downX = 0;
//    float upX = 0;
//    float upY = 0;
//    float downY = 0;
//
//    //TODO  单双击事件
//    @Override
//    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
//
//        Display display = getWindowManager().getDefaultDisplay();
//        int height = display.getHeight();
//        DebugLog.i("_p", ev.getY() + " downy " + height);
//        if (height - ev.getY() > 120) {
//            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//                downX = ev.getX();
//                downY = ev.getY();
//                DebugLog.i("_time", downX + " downX ");
//            }
//
//            if (ev.getAction() == MotionEvent.ACTION_UP) {
//                upX = ev.getX();
//                upY = ev.getY();
//                DebugLog.i("_time", upX + " upX ");
//                if ((upX - downX) == 0 && (upY - downY) == 0)
//                    isSingleClick();
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    private void isSingleClick() {
//        clickCount++;
//        Timer tExit = new Timer();
//        DebugLog.i("_time", clickCount + "");
//        tExit.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (clickCount == 1) {
//                    ImageInfoActivity.this.finish();
//                } else {
//                    clickCount = 0;
//                }
//            }
//        }, 550);
//    }

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void initView() {
		final int left = getIntent().getIntExtra("locationX", 0);
		final int top = getIntent().getIntExtra("locationY", 0);
		final int width = getIntent().getIntExtra("width", 0);
		final int height = getIntent().getIntExtra("height", 0);
		bg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				bg.getViewTreeObserver().removeOnPreDrawListener(this);
				int location[] = new int[2];
				bg.getLocationOnScreen(location);
				mLeft = left - location[0];
				mTop = top - location[1];
				mScaleX = width * 1.0f / bg.getWidth();
				mScaleY = height * 1.0f / bg.getHeight();
				DebugLog.v("zgy", "========resId========" + bg.getWidth());
				DebugLog.v("zgy", "========resId=====mScaleY===" + mScaleY);
				DebugLog.v("zgy", "========resId=====mLeft===" + mLeft);
				DebugLog.v("zgy", "========resId=====mTop===" + mTop);
				activityEnterAnim();
				return true;
			}
		});

	}

	private void activityEnterAnim() {

		DebugLog.v("zgy", "========resId======858==" + mScaleY + " -- " + mScaleX + " -- " + mLeft + " -- " + mTop);
		bg.setPivotX(0);
		bg.setPivotY(0);
		bg.setScaleX(mScaleX);
		bg.setScaleY(mScaleY);
		bg.setTranslationX(mLeft);
		bg.setTranslationY(mTop);
		bg.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).alphaBy(0f).
				setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
	}

    @Override
    public void onBackPressed() {
//        activityExitAnim();
        finish();
    }

//    void activityExitAnim() {
//
//        YoYo.with(Techniques.Hinge).onEnd(new YoYo.AnimatorCallback() {
//            @Override
//            public void call(com.nineoldandroids.animation.Animator animator) {
//                finish();
//            }
//        }).playOn(bg);
//
//    }
}
