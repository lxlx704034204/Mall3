package com.hxqc.mall.fragment.me;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.SubjoinInfo;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.util.DebugLog;
import com.hxqc.xiaoneng.ChatManager;
import com.umeng.analytics.MobclickAgent;

import hxqc.mall.R;

/**
 * 说明:个人中心
 * <p/>
 * author:李烽
 * since: 2015-03-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class MeFragment extends FunctionFragment implements View.OnClickListener {
    //    private RecyclerView mRecyclerView;
    Context mContext;
    boolean showAnim = true;
    int i = 0;
    private ImageView mUserImageView;
    private TextView mUserNameView;
    private ImageView mMessageImageView;
    private ImageView cardIcon;//会员卡的图标
    private TextView cardNumber;//会员卡卡号
    private TextView cardLevel;//会员卡级别
    private LinearLayout level_layout;
    private ImageView settingIcon;
    private TableLayout buttonContainer;
    private LinearLayout info_container;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        DebugLog.i("Tag", " onAttach------------ ");
        mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout headView = (LinearLayout) view.findViewById(R.id.layout_me_head);
        mUserImageView = (ImageView) headView.findViewById(R.id.user_avatar);
        mMessageImageView = (ImageView) headView.findViewById(R.id.my_message);
        settingIcon = (ImageView) headView.findViewById(R.id.setting_icon);
        info_container = (LinearLayout) headView.findViewById(R.id.info_container);
        mUserNameView = (TextView) headView.findViewById(R.id.user_name);
        cardIcon = (ImageView) headView.findViewById(R.id.card_icon);
        cardLevel = (TextView) headView.findViewById(R.id.card_level);
        cardNumber = (TextView) headView.findViewById(R.id.card_number);
        level_layout = (LinearLayout) headView.findViewById(R.id.level_layout);
        buttonContainer = (TableLayout) view.findViewById(R.id.layout_me_buttons);
        initEvent();

    }

    private void initEvent() {
        mUserImageView.setOnClickListener(this);
//        mUserNameView.setOnClickListener(this);
        settingIcon.setOnClickListener(this);
        mMessageImageView.setOnClickListener(this);
        info_container.setOnClickListener(this);

/*下面的按钮*/
        buttonContainer.findViewById(R.id.btn_my_order).setOnClickListener(this);
//        buttonContainer.findViewById(R.id.btn_my_attention).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_my_comment).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_my_service).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_my_wallet).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_second_hand_auto).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_brand_activity).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_car_info).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_shopping_cart).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_normal_maintenance).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_emergency_rescue).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_repair_appointment).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_menber_rule).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_menber_right).setOnClickListener(this);
        buttonContainer.findViewById(R.id.btn_complaint).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("个人中心");
        DebugLog.i("Tag", " onResume------------ ");
        boolean isLogin = UserInfoHelper.getInstance().isLogin(getActivity());
        if (isLogin) {
//            refreshAssistantInfo(showAnim);
        } else {
            updateMessage(null);
        }
        updateHeadView();
        if (showAnim)
            showAnim = false;
    }

    @Override
    public String fragmentDescription() {
        return null;
    }

    /**
     * 更新消息
     */
    private void updateMessage(SubjoinInfo mSubjoinInfo) {
        if (mSubjoinInfo != null && !mSubjoinInfo.messageCount.equals("0")) {
            mMessageImageView.setBackgroundResource(R.drawable.ic_news_unread);
        } else {
            mMessageImageView.setBackgroundResource(R.drawable.ic_news);
        }
    }

//    private void refreshAssistantInfo(boolean showAnim) {
//        new UserApiClient().assistantInfo(new LoadingAnimResponseHandler(getActivity(), showAnim) {
//            @Override
//            public void onSuccess(String response) {
//                SubjoinInfo mSubjoinInfo = JSONUtils.fromJson(response, SubjoinInfo.class);
//                updateMessage(mSubjoinInfo);
//            }
//        });
//    }

    /**
     * 更新头部View
     */
    private void updateHeadView() {
        if (UserInfoHelper.getInstance().isLogin(getActivity())) {
            mUserNameView.setVisibility(View.VISIBLE);
            /**
             * 获取个人信息
             */
            UserInfoHelper.getInstance().getUserInfo(getContext(), new UserInfoHelper.UserInfoAction() {
                @Override
                public void showUserInfo(User meData) {
                    updateHeadValue(meData);
                }

                @Override
                public void onFinish() {

                }
            }, false);
        } else {
            mUserNameView.setText(getString(R.string.login_or_register));
            level_layout.setVisibility(View.GONE);
            mMessageImageView.setVisibility(View.GONE);
            mUserImageView.setImageResource(R.drawable.me_userphoto);
        }
    }

    @Override
    public void onClick(View v) {
        boolean isLogin = UserInfoHelper.getInstance().isLogin(getActivity());
        switch (v.getId()) {
            case R.id.setting_icon:
                ActivitySwitcher.toSettings(getActivity());
                break;
            case R.id.my_message:
                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitcher.toMyMessageActivity(getContext());
                    }
                });
                break;
            case R.id.info_container:
//                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
//                    @Override
//                    public void onLoginSuccess() {
//                        ActivitySwitcher.toResetUserInfo(getActivity());
//                    }
//                });
                if (!isLogin)
                    toLoginMe();
                else ActivitySwitcher.toResetUserInfo(getActivity());
                break;
            case R.id.user_avatar:
//                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
//                    @Override
//                    public void onLoginSuccess() {
//                        ActivitySwitcher.toResetUserInfo(getActivity());
//                    }
//                });
                if (!isLogin)
                    toLoginMe();
                else ActivitySwitcher.toResetUserInfo(getActivity());
                break;
            case R.id.btn_my_order:
                //我的订单
                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitcher.toMain(getActivity(), 2);
                    }
                });
                break;
//            case R.id.btn_my_attention:
            //我的关注
//                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
//                    @Override
//                    public void onLoginSuccess() {
//                        ActivitySwitcher.toWishList(getActivity());
//                    }
//                });
//                break;
            case R.id.btn_my_comment:
//                我的评论
                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
//                        ActivitySwitcher.toUserComment(getActivity());
//                        CommentActivitySwitch.toMyCommentList(getActivity());
                        ActivitySwitchAutoInformation.toMyComment(mContext);
                    }
                });
                break;
            case R.id.btn_my_service:
//                我的客服
//                new CallBar.CallPhoneDialog(getActivity(), getActivity().getString(R.string.tv_service_phone)).show();
                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ChatManager.getInstance().startHistoryGoods("消息", "null");
                    }
                });
                break;
            case R.id.btn_my_wallet:
//                我的钱包
                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitcher.toMyWallet(getActivity());
                    }
                });
                break;
            case R.id.btn_second_hand_auto:
                //二手车
                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        UsedCarActivitySwitcher.toSellCarInfo(getActivity());
                    }
                });
                break;
            case R.id.btn_emergency_rescue:
//                紧急救援
                ActivitySwitcher.toEmergencyRescue(getActivity());
                break;
            case R.id.btn_menber_right:
//                会员权益
                ActivitySwitchBase.toH5Activity(getActivity(), getString(R.string.menber_right),
                        new UserApiClient().accountRule());
                break;
            case R.id.btn_menber_rule:
//                会员规则
                ActivitySwitchBase.toH5Activity(getActivity(), getString(R.string.menber_rule),
                        new UserApiClient().accountRule());
                break;
            case R.id.btn_shopping_cart:
//                购物车
                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitcherAccessory4S.toShoppingCart(getActivity());
                    }
                });
                break;
            case R.id.btn_repair_appointment:
//                预约维修
                ActivitySwitcherMaintenance.toAppointmentMaintenance(getActivity());
                break;
            case R.id.btn_normal_maintenance:
//                常规保养
                ActivitySwitcherMaintenance.toNormalMaintenance(getActivity());
                break;
            case R.id.btn_car_info:
////                车辆信息
                UserInfoHelper.getInstance().loginAction(getActivity(), new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitchBase.toCenterAutoInfo(getActivity(), "");
                    }
                });

                break;
            case R.id.btn_brand_activity:
//                品牌活动
                UserApiClient client = new UserApiClient();
                ActivitySwitchBase.toH5Activity(getActivity(),
                        getString(R.string.brand_activity), client.brandActivities());
                break;
            case R.id.btn_complaint:
                //客户投诉
//                UserInfoHelper.getInstance().loginAction(mContext, new UserInfoHelper.OnLoginListener() {
//                    @Override
//                    public void onLoginSuccess() {
//                ActivitySwitcher.toComplaints(getActivity());
                ActivitySwitcher.toComplaints2(getActivity());
//                    }
//                });
                break;
        }
    }

    //登陆后回到个人中心
    private void toLoginMe() {
//        ActivitySwitchAuthenticate.toLogin(getActivity(), "");
        ActivitySwitchAuthenticate.toLogin(this);
    }


    /**
     * 更新头部数据
     */
    private void updateHeadValue(User mUser) {
        if (mUser != null) {
            DebugLog.i("Tag", mUser.toString());
            mUserNameView.setText(String.format("欢迎您 %s", mUser.getNickName(false)));
            if (!TextUtils.isEmpty(mUser.levelIcon)) {
                ImageUtil.setImage(getActivity(), cardIcon, mUser.levelIcon, R.drawable.me_userphoto);
                cardIcon.setVisibility(View.VISIBLE);
            } else cardIcon.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mUser.level)) {
                cardLevel.setText(mUser.level);
                cardLevel.setVisibility(View.VISIBLE);
            } else cardLevel.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mUser.levelID))
                cardNumber.setText(mUser.levelID);
            else cardNumber.setText(mUser.phoneNumber);

//            ImageUtil.setImage(getActivity(), mUserImageView, mUser.avatar, R.drawable.me_userphoto);
            ImageUtil.setImage(getActivity(),mUserImageView,mUser.avatar,R.drawable.me_userphoto);
            level_layout.setVisibility(View.VISIBLE);
            mMessageImageView.setVisibility(View.VISIBLE);
        } else {
            mUserImageView.setImageResource(R.drawable.me_userphoto);
        }
    }


}
