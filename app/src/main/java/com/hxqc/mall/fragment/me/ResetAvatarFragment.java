//package com.hxqc.mall.fragment.me;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//
//import com.hxqc.mall.config.application.SampleApplicationContext;
//import com.hxqc.mall.core.api.DialogResponseHandler;
//import com.hxqc.mall.core.api.UserApiClient;
//import com.hxqc.mall.core.controler.UserInfoHelper;
//import com.hxqc.mall.core.model.User;
//import com.hxqc.mall.core.util.ImageUtil;
//import com.hxqc.mall.core.views.dialog.ListDialog;
//import com.hxqc.util.DebugLog;
//import com.hxqc.util.JSONUtils;
//import com.hxqc.widget.PhotoChooseFragment;
//
//import java.io.File;
//import java.util.HashMap;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import hxqc.mall.R;
//
///**
// * Author:李烽
// * Date:2016-04-01
// * FIXME
// * Todo 修改头像的fragment
// */
//@Deprecated
//public class ResetAvatarFragment extends PhotoChooseFragment implements View.OnClickListener {
//    private LinearLayout rootLayout;
//    private CircleImageView user_avatar;
//    private Context mContext;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_reset_avatar, container, false);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mContext = context;
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        rootLayout = (LinearLayout) view.findViewById(R.id.root_layout);
//        user_avatar = (CircleImageView) view.findViewById(R.id.user_avatar);
//        rootLayout.setOnClickListener(this);
////        User user = UserInfoHelper.getInstance().getUser(mContext);
//        UserInfoHelper.getInstance().getUserInfo(mContext, new UserInfoHelper.UserInfoAction() {
//            @Override
//            public void showUserInfo(User meData) {
//                User user = meData;
//                if (user != null) {
//                    ImageUtil.setImage(getActivity(), user_avatar, user.avatar, R.drawable.me_userphoto);
//                }
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }, false);
//
//    }
//
//    @Override
//    public void chooseSuccess(final String filePath) {
//        DebugLog.i("Tag", "修改头像   " + filePath);
//        final String mPhoto = "file://" + filePath;
//        ImageUtil.setImage(getActivity(), user_avatar, mPhoto, R.drawable.me_userphoto);
////        User mUser = UserInfoHelper.getInstance().getUser(mContext);
//        UserInfoHelper.getInstance().getUserInfo(mContext, new UserInfoHelper.UserInfoAction() {
//            @Override
//            public void showUserInfo(User meData) {
//                meData.avatar = mPhoto;
//                UserInfoHelper.getInstance().saveUser(mContext, meData);
//                new UserApiClient().updateAvatar(UserInfoHelper.getInstance().getToken(mContext), filePath, new
//                        DialogResponseHandler(getActivity(), getResources().getString(R.string.me_submitting)) {
//                            @Override
//                            public void onSuccess(String response) {
//                                HashMap< String, Object > stringObjectHashMap = JSONUtils.fromJson(response);
//                                if (onAvatarChangeListener != null)
//                                    onAvatarChangeListener.onAvatarChanged(stringObjectHashMap.get("url").toString());
//                                //头像上传完后更新内存中的user
//                                UserInfoHelper.getInstance().refreshUserInfo(mContext, new UserInfoHelper.UserInfoAction() {
//                                    @Override
//                                    public void showUserInfo(User meData) {
//
//                                    }
//
//                                    @Override
//                                    public void onFinish() {
//
//                                    }
//                                }, false);
//                            }
//                        });
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }, false);
//
//    }
//
//    public void setOnAvatarChangeListener(OnAvatarChangeListener onAvatarChangeListener) {
//        this.onAvatarChangeListener = onAvatarChangeListener;
//    }
//
//    private OnAvatarChangeListener onAvatarChangeListener;
//
//    public interface OnAvatarChangeListener {
//        void onAvatarChanged(String url);
//    }
//
//    @Override
//    public boolean toCropPhoto() {
//        return true ;
//    }
//
//    @Override
//    public String getCameraPath() {
//        return SampleApplicationContext.application.getExternalCacheDir().toString() + "/camera.png";
//    }
//
//    @Override
//    protected String getCropCacheFilePath() {
//        Context context = getActivity();
//        cropCachePath = context.getExternalCacheDir() + File.separator + "crop_" + System.currentTimeMillis() + ".png";
//        return cropCachePath;
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.root_layout) {
////            changeUserImage();
//        }
//    }
//
//    private void changeUserImage() {
////        String[] permissions = new String[0];
////        boolean permission = true;
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
////            permissions = new String[]{Manifest.permission.MANAGE_DOCUMENTS};
////            permission = PermissionUtil.checkPermission(getActivity(), permissions[0]);
////        }
////        if (!permission)
////            PermissionUtil.requestPermission(getActivity(), permissions[0]);
////        else {
//        try {
//            new ListDialog(getActivity(), getActivity().getResources().getString(R.string.me_upload_photo),
//                    new String[]{getResources().getString(R.string.me_take_picture_upload), getResources().getString
//                            (R.string.me_local_upload)},
//                    new int[]{R.drawable.ic_uploadpictures, R.drawable.ic_detail_image1}) {
//                @Override
//                protected void doNext(int position) {
//                    switch (position) {
//                        case 0:
//                            // 相机
//                            getPicFromCamera();
//                            break;
//                        case 1:
//                            // 图库
//                            getPicFromContent();
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
////    }
//
//}
