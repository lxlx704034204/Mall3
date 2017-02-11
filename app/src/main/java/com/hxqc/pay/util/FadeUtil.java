package com.hxqc.pay.util;//package com.hxqc.hxqcmall.pay.util;
//
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//
///**
// * 淡入淡出的动画处理
// *
// * author wh
// */
//public class FadeUtil {
//    // 淡出
//    // 1、那个界面要淡出（当前正在展示）
//    // 2、动画执行时间
//    // 3、何时删除当前正在展示的界面——动画执行"完成后"删除
//
//    // 淡入
//    // 1、那个界面要淡入（目标界面）
//    // 2、等待一段时间（淡出界面的动画执行时间）
//    // 3、淡入的动画执行时间
//
//    private static Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            View currentView = (View) msg.obj;
//            ViewGroup middle = (ViewGroup) currentView.getParent();
//            middle.removeView(currentView);
//            return false;
//        }
//    }) ;
//
////	{
////		public void handleMessage(Message msg) {
////			View currentView=(View) msg.obj;
////			ViewGroup middle = (ViewGroup) currentView.getParent();
////			middle.removeView(currentView);
////
////		}
////	}
//
//
//    /**
//     * 淡出
//     */
//    public static void fadeOut(final View currentView, long duration) {
//        // where 1.0 means fully opaque and 0.0 means fully transparent.
//        AlphaAnimation animation = new AlphaAnimation(1, 0);
//        animation.setDuration(duration);
//
//        animation.setAnimationListener(new AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // middle.removeAllViews();
//                // 删除指定的界面
//                // middle.removeView(view);
//
//                // 如果能够获取到中间容器的话:middle父容器，里面有一个孩子currentView
////				ViewGroup middle = (ViewGroup) currentView.getParent();
////				middle.removeView(currentView);
//
//                // 在老版本（2.3）的模拟器和真机上会存在问题
//
//                Message msg = Message.obtain();
//                msg.obj = currentView;
//                handler.sendMessageDelayed(msg, 1);
//
//            }//动画完成了，不会在onAnimationEnd调用的时候去删除currentView
//        });
//
//        currentView.startAnimation(animation);
//    }
//
//    /**
//     * 淡入
//     */
//    public static void fadeIn(View targetView, long duration, long delay) {
//        // where 1.0 means fully opaque and 0.0 means fully transparent.
//        AlphaAnimation animation = new AlphaAnimation(0, 1);
//        animation.setDuration(duration);
//        // 等待一段时间处理动画的开始
//        animation.setStartOffset(delay);
//
//        targetView.startAnimation(animation);
//    }
//}
