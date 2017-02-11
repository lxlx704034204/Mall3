package com.hxqc.pay.views.fragmenttransaction;//package com.hxqc.hxqcmall.pay.views.fragmenttransaction;
//
//
//import android.animation.AnimatorSet;
//import android.animation.PropertyValuesHolder;
//import android.app.Activity;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Context;
//import android.view.View;
//
//import com.hxqc.hxqcmall.pay.R;
//import com.nineoldandroids.animation.Animator;
//import com.nineoldandroids.animation.AnimatorListenerAdapter;
//import com.nineoldandroids.animation.ObjectAnimator;
//
//
//public class FragmentTransactionExtended implements FragmentManager.OnBackStackChangedListener {
//    private boolean mDidSlideOut = false;
//    private boolean mIsAnimating = false;
//    private FragmentTransaction mFragmentTransaction;
//    private Context mContext;
//    private Fragment mFirstFragment, mSecondFragment;
//    private int mContainerID;
//    private int mTransitionType;
//    public static final int SLIDE_HORIZONTAL = 7;
//    public static final int SLIDE_HORIZONTAL_PUSH_TOP = 8;
//    public static final int GLIDE = 10;
//    public static final int SLIDING = 11;
//
//
//
//    public FragmentTransactionExtended(Context context, FragmentTransaction fragmentTransaction, Fragment firstFragment, Fragment secondFragment, int containerID) {
//        this.mFragmentTransaction = fragmentTransaction;
//        this.mContext = context;
//        this.mFirstFragment = firstFragment;
//        this.mSecondFragment = secondFragment;
//        this.mContainerID = containerID;
//    }
//
//    public void addTransition(int transitionType) {
//        this.mTransitionType = transitionType;
//        switch (transitionType) {
//            case SLIDE_HORIZONTAL:
//                transitionSlideHorizontal();
//                break;
//
//            case SLIDING:
//                return;
//
//        }
//        mFragmentTransaction.replace(mContainerID, mSecondFragment);
//    }
//
//
//    private void transitionSlideHorizontal() {
//        mFragmentTransaction.setCustomAnimations(R.animator.slide_fragment_horizontal_right_in, R.animator.slide_fragment_horizontal_left_out, R.animator.slide_fragment_horizontal_left_in, R.animator.slide_fragment_horizontal_right_out);
//}
//
//
//
//    private void switchFragments() {
//        ((Activity) this.mContext).getFragmentManager().addOnBackStackChangedListener(this);
//
//        if (mIsAnimating) {
//            return;
//        }
//        mIsAnimating = true;
//        if (mDidSlideOut) {
//            mDidSlideOut = false;
//            ((Activity) mContext).getFragmentManager().popBackStack();
//        } else {
//            mDidSlideOut = true;
//            Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator arg0) {
//                    mFragmentTransaction.setCustomAnimations(R.animator.slide_fragment_in, 0, 0, R.animator.slide_fragment_out);
//                    mFragmentTransaction.add(mContainerID, mSecondFragment);
//                    mFragmentTransaction.addToBackStack(null);
//                    mFragmentTransaction.commit();
//                }
//            };
//            slideBack(listener);
//        }
//    }
//
////    public void slideBack(Animator.AnimatorListener listener) {
////        View movingFragmentView = mFirstFragment.getView();
////        movingFragmentView.setPivotY(movingFragmentView.getHeight()/2);
////        movingFragmentView.setPivotX(movingFragmentView.getWidth() / 2);
////
////        PropertyValuesHolder rotateX = PropertyValuesHolder.ofFloat("rotationX", 40f);
////        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.8f);
////        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.8f);
////        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.5f);
////        ObjectAnimator movingFragmentAnimator = ObjectAnimator.ofPropertyValuesHolder(movingFragmentView, rotateX, scaleX, scaleY, alpha);
////
////        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(movingFragmentView, "rotationX", 0);
////        movingFragmentRotator.setStartDelay(mContext.getResources().getInteger(R.integer.half_slide_up_down_duration));
////
////        AnimatorSet s = new AnimatorSet();
////        s.playTogether(movingFragmentAnimator, movingFragmentRotator);
////        s.addListener(listener);
////        s.start();
////    }
//
////    public void slideForward() {
////        View movingFragmentView = mFirstFragment.getView();
////        PropertyValuesHolder rotateX = PropertyValuesHolder.ofFloat("rotationX", 40f);
////        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f);
////        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f);
////        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f);
////        ObjectAnimator movingFragmentAnimator = ObjectAnimator.ofPropertyValuesHolder(movingFragmentView, rotateX, scaleX, scaleY, alpha);
////
////        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(movingFragmentView, "rotationX", 0);
////        movingFragmentRotator.setStartDelay(mContext.getResources().getInteger(R.integer.half_slide_up_down_duration));
////
////        AnimatorSet s = new AnimatorSet();
////        s.playTogether(movingFragmentAnimator, movingFragmentRotator);
////        s.setStartDelay(mContext.getResources().getInteger(R.integer.slide_up_down_duration));
////        s.addListener(new AnimatorListenerAdapter() {
////            @Override
////            public void onAnimationEnd(Animator animation) {
////                mIsAnimating = false;
////                mDidSlideOut = true;
////            }
////        });
////        s.start();
////        ((Activity) this.mContext).getFragmentManager().removeOnBackStackChangedListener(this);
////    }
//
//
//    public void commit(){
//        switch (mTransitionType){
//            case SLIDING:
//                switchFragments();
//                break;
//            default:
//                mFragmentTransaction.addToBackStack(null);
//                mFragmentTransaction.commit();
//                break;
//        }
//    }
//
//    @Override
//    public void onBackStackChanged() {
//        switch (mTransitionType){
//            case SLIDING:
//                if (!mDidSlideOut) {
//                        slideForward();
//                }else{
//                    mDidSlideOut= false;
//                }
//                break;
//        }
//    }
//}
