
package com.hxqc.mall.drivingexam.utils;

import android.content.Context;
import android.content.Intent;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.drivingexam.ui.ChooseKemuActivity;
import com.hxqc.mall.drivingexam.ui.ScoreActivity;
import com.hxqc.mall.drivingexam.ui.StartExamActivity;
import com.hxqc.mall.drivingexam.ui.WrongSubjectSettingActivity;
import com.hxqc.mall.drivingexam.ui.doexam.ExamActivity;
import com.hxqc.mall.drivingexam.ui.helpyoupass.HelpYouPassActivity;
import com.hxqc.mall.drivingexam.ui.helpyoupass.drivingskill.DrivingExperienceActivity;
import com.hxqc.mall.drivingexam.ui.helpyoupass.lightandvoice.LightMainActivity;
import com.hxqc.mall.drivingexam.ui.helpyoupass.picturedescribedetail.MediaManageActivity;
import com.hxqc.mall.drivingexam.ui.helpyoupass.picturedescribedetail.PictureDescribeActivity;
import com.hxqc.mall.drivingexam.ui.helpyoupass.picturedescribedetail.VideoviewFullscreenActivity;
import com.hxqc.mall.drivingexam.ui.homepage.ExamHomePageActivity;
import com.hxqc.mall.drivingexam.ui.recordhistory.RecordHistoryActivity;
import com.hxqc.mall.photolibrary.activity.ImagePagerActivity;

/**
 * Created by zhaofan on 2016/8/4.
 */

public class ActivitySwitcherExam {

    public static void toExamHomeActivity(Context context) {
        Intent intent = new Intent(context, ExamHomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void toHelpPassActivity(Context context) {
        Intent intent = new Intent(context, HelpYouPassActivity.class);
        context.startActivity(intent);
    }


    public static void toChooseKemuActivity(Context context) {
        Intent intent = new Intent(context, ChooseKemuActivity.class);
        context.startActivity(intent);
    }


    /**
     * 做题界面
     */
    public static void toExamActivity(Context context, int kemu) {
        Intent intent = new Intent(context, ExamActivity.class);
        intent.putExtra("kemu", kemu);
        context.startActivity(intent);
    }


    /**
     * 做新题
     */
    public static void toExamActivity_NewSubject(Context context, int kemu) {
        Intent intent = new Intent(context, ExamActivity.class);
        intent.putExtra(ExamActivity.ONLY_NEW_SUBJECT, true);
        intent.putExtra("kemu", kemu);
        context.startActivity(intent);
    }


    /**
     * 只看错题
     */
    public static void toExamActivity(Context context, int kemu,
                                      long dateTag, boolean isShowWrong) {
        Intent intent = new Intent(context, ExamActivity.class);
        intent.putExtra(ExamActivity.SHOW_WRONG_SUBJECT, isShowWrong);
        intent.putExtra("dateTag", dateTag);
        intent.putExtra("kemu", kemu);
        context.startActivity(intent);
    }

    /**
     * 我的错题
     */
    public static void toExamActivity_MyWrongSub(Context context, int kemu) {
        Intent intent = new Intent(context, ExamActivity.class);
        intent.putExtra(ExamActivity.MY_WRONG_SUBJECT, true);
        intent.putExtra("kemu", kemu);
        context.startActivity(intent);
    }

    public static void toStartExamActivity(Context context, int kemu) {
        Intent intent = new Intent(context, StartExamActivity.class);
        intent.putExtra("kemu", kemu);
        context.startActivity(intent);
    }

    /**
     * 得分界面
     */
    public static void toScoreActivity(Context context, long dateTag, long Time,
                                       int rightCount, int wrongCount, int kemu) {
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra("dateTag", dateTag);
        intent.putExtra("saveTime", Time);
        intent.putExtra("rightCount", rightCount);
        intent.putExtra("wrongCount", wrongCount);
        intent.putExtra("kemu", kemu);
        context.startActivity(intent);

    }

    /**
     * 我的错题设置
     */
    @Deprecated
    public static void toWrongSubjectSettingActivity(Context context) {
        Intent intent = new Intent(context, WrongSubjectSettingActivity.class);
        context.startActivity(intent);
    }

    /**
     * 成绩记录
     */
    public static void toRecordHistoryActivity(Context context, int kemu) {
        Intent intent = new Intent(context, RecordHistoryActivity.class);
        intent.putExtra("kemu", kemu);
        context.startActivity(intent);
    }


    /**
     * 图文详情
     */
    public static void toPictureDescribe(Context context) {
        context.startActivity(new Intent(context, PictureDescribeActivity.class));
    }


    /**
     * 灯光
     */
    public static void toLightAndVoice(Context context) {
        context.startActivity(new Intent(context, LightMainActivity.class));
    }


    /**
     * 学车技巧
     */
    public static void toDrivingExperience(Context context, int kemu) {
        Intent intent = new Intent(context, DrivingExperienceActivity.class);
        intent.putExtra("kemu", kemu);
        context.startActivity(intent);
    }

    /**
     * 视频管理
     */
    public static void toMediaManage(Context context, String fullPath, long totalSize) {
        Intent intent = new Intent(context, MediaManageActivity.class);
        intent.putExtra("path", fullPath);
        intent.putExtra("totalSize", totalSize);
        context.startActivity(intent);
    }

    /**
     * 视频全屏
     */
    public static void toVideoviewFullscreen(Context context, String fullPath, int CurrentPosition) {
        Intent intent = new Intent(context, VideoviewFullscreenActivity.class);
        intent.putExtra("path", fullPath);
        intent.putExtra("CurrentPosition", CurrentPosition);
        context.startActivity(intent);
    }


    public static void toPhotoView(Context context, String[] url, int pos) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, pos);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, url);
        context.startActivity(intent);
    }


    public static String completeUrl(String control) {
        return ApiUtil.getAroundURL(control);
    }

    public static void toKemu2Html(Context context, int position) {
        String title = "", url = "";
        if (position == 0) {
            title = "安全带";
            url = completeUrl("/Cardriving/safebelt");
        } else if (position == 1) {
            title = "点火开关";
            url =completeUrl("/Cardriving/ignitionlock");
        } else if (position == 2) {
            title = "方向盘";
            url = completeUrl("/Cardriving/aimingcircle");
        } else if (position == 3) {
            title = "离合器";
            url = completeUrl("/Cardriving/clutch");
        } else if (position == 4) {
            title = "加速踏板";
            url = completeUrl("/Cardriving/acceleratepale");
        } else if (position == 5) {
            title = "制动踏板";
            url = completeUrl("/Cardriving/brakepedal");
        } else if (position == 6) {
            title = "驻车制动";
            url = completeUrl("/Cardriving/parkingbraking");
        } else if (position == 7) {
            title = "座椅调整";
            url = completeUrl("/Cardriving/seatadjustment");
        } else if (position == 8) {
            title = "后视镜";
            url = completeUrl("/Cardriving/backmirror");
        } else if (position == 9) {
            title = "经验技巧";
            url = completeUrl("/Cardriving/experskill");
        }

        ActivitySwitchBase.toH5Activity(context, title, url);
    }

    public static void toKemu3Html(Context context, int position) {
        String title = "", url = "";
        if (position == 0) {
            title = "车距判断";
            url = completeUrl("/Cardriving/carjudge");
        } else if (position == 1) {
            title = "档位操作";
            url = completeUrl("/Cardriving/positionopera");
        } else if (position == 2) {
            title = "灯光";
            url = completeUrl("/Cardriving/lamplight");
        } else if (position == 3) {
            title = "直行";
            url = completeUrl("/Cardriving/rectigrade");
        } else if (position == 4) {
            title = "经验技巧";
            url = completeUrl("/Cardriving/expskill");
        }

        ActivitySwitchBase.toH5Activity(context, title, url);
    }


    public static void toKemu2PictureDetailHtml(Context context, int position) {
        String title = "", url = "";
        if (position == 0) {
            title = "倒车入库";
            url = completeUrl("/Cardriving/reverseparking");
        } else if (position == 1) {
            title = "坡道定点停车和起步";
            url = completeUrl("/Cardriving/designatedparking");
        } else if (position == 2) {
            title = "侧方停车";
            url = completeUrl("/Cardriving/sideparking");
        } else if (position == 3) {
            title = "曲线行驶";
            url = completeUrl("/Cardriving/routedriving");
        } else if (position == 4) {
            title = "直角转弯";
            url = completeUrl("/Cardriving/quarterturn");
        }
        ActivitySwitchBase.toH5Activity(context, title, url);
    }

    public static void toKemu3PassExperience(Context context) {
        String title = "科目三 考试过关全解析", url = completeUrl("/Cardriving/testpass");
        ActivitySwitchBase.toH5Activity(context, title, url);
    }

    public static void practiceLaw(Context context) {
        String title = "实习法律规定", url = completeUrl("/Cardriving/practicelaw");
        ActivitySwitchBase.toH5Activity(context, title, url);
    }


    public static void rookieSkill(Context context) {
        String title = "新手上路小技巧", url = completeUrl("/Cardriving/invoceing");
        ActivitySwitchBase.toH5Activity(context, title, url);
    }

}

