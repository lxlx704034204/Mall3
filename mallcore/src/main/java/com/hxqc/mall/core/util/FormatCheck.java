package com.hxqc.mall.core.util;

import android.content.Context;
import android.text.TextUtils;

import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明:格式验证
 * <p/>
 * author: 吕飞
 * since: 2015-03-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class FormatCheck {

    public static final int CHECK_SUCCESS = 0;//验证成功
    public static final int PASSWORD_WEAK = 1;//密码已空格开头或结尾
    public static final int PASSWORD_MEDIUM = 2;//密码已空格开头或结尾
    public static final int PASSWORD_STRONG = 3;//密码已空格开头或结尾
    public static final int NO_PASSWORD = 4;//密码格式为空
    public static final int PASSWORD_LENGTH_ERROR = 5;//密码长度错误
    public static final int PASSWORD_START_END_SPACE = 6;//密码已空格开头或结尾
    public static final int SAME_TO_OLD_PASSWORD = 7;//新密码和旧密码相同
    public static final int NO_PHONE_NUMBER = 8;//号码为空
    public static final int PHONE_NUMBER_HAS_REGISTERED = 9;//号码已注册
    public static final int PHONE_NUMBER_FORMAT_ERROR = 10;//手机号格式错误
    public static final int NO_IDENTIFYING_CODE = 11;//验证码为空
    public static final int IDENTIFYING_CODE_ERROR = 12;//验证码错误
    public static final int NO_REAL_NAME = 13;//真实姓名为空
    public static final int REAL_NAME_NO_CHINESE = 14;//真实姓名不是汉字
    public static final int REAL_NAME_ERROR = 15;//真实姓名不是汉字
    public static final int REAL_NAME_SUCCESS = 16;//真实姓名不是汉字
    public static final int NO_PLATE_NUMBER = 17;
    public static final int PLATE_NUMBER_ERROR = 18;
    public static final int PLATE_NUMBER_SUCCESS = 19;

    /**
     * 检查手机号
     *
     * @param phoneNumber
     * @param context
     */
    public static int checkPhoneNumber(String phoneNumber, Context context) {
        if (TextUtils.isEmpty(phoneNumber)) {
//            ToastHelper.showYellowToast(context, R.string.me_phone_number_hint);
            ToastHelper.showYellowToast(context, "请输入正确的手机号");
            return NO_PHONE_NUMBER;
        } else if (!isPhoneNumber(phoneNumber)) {
//            ToastHelper.showYellowToast(context, R.string.me_phone_number_format_error);
            ToastHelper.showYellowToast(context, "请输入正确的手机号");
            return PHONE_NUMBER_FORMAT_ERROR;
        } else {
            return CHECK_SUCCESS;
        }
    }

    /**
     * 检查收票人手机号
     *
     * @param phoneNumber
     * @param context
     */
    public static int checkPhoneNumberForInvoice(String phoneNumber, Context context) {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastHelper.showYellowToast(context, "请填写收票人电话");
            return NO_PHONE_NUMBER;
        } else if (!isPhoneNumber(phoneNumber)) {
            ToastHelper.showYellowToast(context, R.string.me_phone_number_format_error);
            return PHONE_NUMBER_FORMAT_ERROR;
        } else {
            return CHECK_SUCCESS;
        }
    }

    /**
     * 检查邮箱
     *
     * @param email
     * @param context
     */
    public static boolean checkEmail(String email, Context context) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        if (isMatched || TextUtils.isEmpty(email)) {
            return true;
        } else {
            ToastHelper.showYellowToast(context, R.string.dialog_arrival_email_error);
            return false;
        }
    }

    /**
     * 是否是手机号
     *
     * @param phoneNumber
     */
    public static boolean isPhoneNumber(String phoneNumber) {
//        String telRegex = "[1][34578]\\d{9}";
        final String regexStr_phone = "^0?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$";
//        String telRegex = "[1]\\d{10}";
        return !TextUtils.isEmpty(phoneNumber) && phoneNumber.matches(regexStr_phone);
    }

    /**
     * 检查密码格式
     * 密码强度规则：
     * 首尾不能为空格
     * 长度：6-20个字符串
     * 弱：纯数字，纯字母，纯特殊符号
     * 中：两种组合
     * 强：三种组合
     *
     * @param password
     * @param context
     * @param showToast 验证过程是否弹出Toast
     */
    public static int checkPassword(String password, Context context, boolean showToast) {
        if (TextUtils.isEmpty(password)) {
            if (showToast) {
                ToastHelper.showYellowToast(context, R.string.me_password_hint);
            }
            return NO_PASSWORD;
        } else if (password.length() != password.trim().length()) {
            if (showToast) {
                ToastHelper.showYellowToast(context, R.string.me_password_start_end_space);
            }
            return PASSWORD_START_END_SPACE;
        } else if (password.length() < 6 || password.length() > 20) {
            if (showToast) {
                ToastHelper.showYellowToast(context, R.string.me_password_length_error);
            }
            return PASSWORD_LENGTH_ERROR;
        } else {
            return passwordStrength(password);
        }
    }

    private static int passwordStrength(String password) {
        HashSet<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < password.length(); i++) {
            hashSet.add(OtherUtil.getCharMode(password.charAt(i)));
        }
        return hashSet.size();
    }

    /**
     * 检查验证码
     *
     * @param identifyingCode
     * @param context
     */
    public static int checkIdentifyingCode(String identifyingCode, Context context) {
        if (TextUtils.isEmpty(identifyingCode)) {
            ToastHelper.showYellowToast(context, R.string.me_no_identifying_code);
            return NO_IDENTIFYING_CODE;
        } else {
            return CHECK_SUCCESS;
        }
    }

    /**
     * 验证昵称
     *
     * @param nickname
     * @param context
     */
    public static boolean checkNickname(String nickname, Context context) {
        String nicknameRegex = "^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]{2,12}$";
        if (nickname.matches(nicknameRegex)) {
            return true;
        } else if (TextUtils.isEmpty(nickname)) {
            ToastHelper.showYellowToast(context, R.string.me_nickname_hint);
            return false;
        } else {
            ToastHelper.showYellowToast(context, "请检查昵称格式：" + context.getString(R.string.me_nickname_error));
            return false;
        }
    }

    public static boolean checkName(String nickname, Context context) {
        String nicknameRegex = "^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]{2,30}$";
        if (nickname.matches(nicknameRegex)) {
            return true;
        } else if (TextUtils.isEmpty(nickname)) {
            ToastHelper.showYellowToast(context, R.string.me_name_hint);
            return false;
        } else {
            ToastHelper.showYellowToast(context, R.string.me_name_error);
            return false;
        }
    }

    public static boolean checkNameCanEmpty(String nickname, Context context) {
        String nicknameRegex = "^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]{2,30}$";
        if (nickname.matches(nicknameRegex)) {
            return true;
        } else if (TextUtils.isEmpty(nickname)) {
//            ToastHelper.showYellowToast(context, R.string.me_name_hint);
            return true;
        } else {
            ToastHelper.showYellowToast(context, R.string.me_name_error);
            return false;
        }
    }

    //用品联系人验证
    public static boolean checkNameForAcc(String nickname, Context context) {
        String nicknameRegex = "^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]{2,30}$";
        if (nickname.matches(nicknameRegex)) {
            return true;
        } else if (TextUtils.isEmpty(nickname)) {
            ToastHelper.showYellowToast(context, R.string.me_name_hint);
            return false;
        } else {
            ToastHelper.showYellowToast(context, R.string.me_name_error_for_acc);
            return false;
        }
    }

    //发票验证
    public static boolean checkNameForInvoice(String nickname, Context context) {
        String nicknameRegex = "^[A-Za-z0-9_\\-\\u4e00-\\u9fa5]{2,30}$";
        if (nickname.matches(nicknameRegex)) {
            return true;
        } else if (TextUtils.isEmpty(nickname)) {
            ToastHelper.showYellowToast(context, "请填写单位名称");
            return false;
        } else {
            ToastHelper.showYellowToast(context, R.string.me_name_error_for_invoice);
            return false;
        }
    }

    /**
     * 验证姓名
     *
     * @param nickname
     * @param context
     */
    public static int checkName2(String nickname, Context context) {
        String nicknameRegex = "^[A-Za-z0-9\\-\\u4e00-\\u9fa5]{2,30}$";
        if (nickname.matches(nicknameRegex)) {
            return REAL_NAME_SUCCESS;
        } else if (TextUtils.isEmpty(nickname)) {
            ToastHelper.showYellowToast(context, "请输入姓名");
            return NO_REAL_NAME;
        } else if (nickname.length() < 2 || nickname.length() > 30) {
            ToastHelper.showYellowToast(context, "姓名格式不正确");
            return REAL_NAME_ERROR;
        } else {
            ToastHelper.showYellowToast(context, "姓名格式不正确");
            return REAL_NAME_ERROR;
        }
    }

    /**
     * 验证真名
     *
     * @param realName
     * @param context
     */
    public static boolean checkRealName(String realName, Context context) {
        String telRegex = "^[\\u4e00-\\u9fa5]{2,12}";
        if (realName.matches(telRegex) /*|| TextUtils.isEmpty(realName)*/) {
            return true;
        } else if (TextUtils.isEmpty(realName)) {
            ToastHelper.showYellowToast(context, "真实姓名不能为空");
            return false;
        } else {
//            ToastHelper.showYellowToast(context, R.string.me_real_name_error);
            ToastHelper.showYellowToast(context, "请输入2-12个字，只能由中文组成");
            return false;
        }
    }

    /**
     * 验证收票人姓名
     *
     * @param realName
     * @param context
     */
    public static boolean checkReceiverName(String realName, Context context) {
        String telRegex = "^[\\u4e00-\\u9fa5]{2,12}";
        if (TextUtils.isEmpty(realName)) {
            ToastHelper.showYellowToast(context, "请填写收票人姓名");
            return false;
        }
        if (realName.matches(telRegex)) {
            return true;
        } else {
            ToastHelper.showYellowToast(context, R.string.me_real_name_error);
            return false;
        }
    }

    /**
     * 验证卖车人人姓名
     *
     * @param realName
     * @param context
     */
    public static boolean checkSellerName(String realName, Context context) {
        String telRegex = "^[\\u4e00-\\u9fa5]{2,12}";
        if (TextUtils.isEmpty(realName)) {
            ToastHelper.showYellowToast(context, "请填写您的姓名");
            return false;
        }
        if (realName.matches(telRegex)) {
            return true;
        } else {
            ToastHelper.showYellowToast(context, R.string.me_real_name_error);
            return false;
        }
    }

    /**
     * 验证真实姓名是否是汉字
     *
     * @param realName
     * @param context
     */

    public static int checkRealChinerseName(String realName, Context context) {
        DebugLog.i(FormatCheck.class.getSimpleName(), "realName:" + realName);
        if (TextUtils.isEmpty(realName)) {
            DebugLog.i(FormatCheck.class.getSimpleName(), "为空");
            ToastHelper.showYellowToast(context, "请填写车主姓名");
            DebugLog.i(FormatCheck.class.getSimpleName(), "为空111");
            return NO_REAL_NAME;
        } else {
            int len = 0;
            char[] nickchar = realName.toCharArray();
            for (int i = 0; i < nickchar.length; i++) {
                if (isChinese(nickchar[i])) {
                    len += 2;
                } else {
                    len += 1;
                }
            }
            if (len < 2 || len > 12) {
                DebugLog.i(FormatCheck.class.getSimpleName(), "格式不正确");
                ToastHelper.showYellowToast(context, "车主姓名不正确、2-6个汉字");
                return REAL_NAME_NO_CHINESE;
            }
        }
        return 0;
    }

    /**
     * 汉字判断
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static int checkPlateNumber(Context context, String plateNum) {
        String nicknameRegex = "^[A-Z]";
        if (TextUtils.isEmpty(plateNum)) {
            ToastHelper.showYellowToast(context, "请填写车牌号");
            return NO_PLATE_NUMBER;
        } else if (plateNum.length() < 7) {
            ToastHelper.showYellowToast(context, "车牌号格式不正确");
            if (plateNum.substring(1, 2).matches(nicknameRegex)) {
                return PLATE_NUMBER_SUCCESS;
            } else {
                return PLATE_NUMBER_ERROR;
            }
        }
        return PLATE_NUMBER_SUCCESS;
    }
}
