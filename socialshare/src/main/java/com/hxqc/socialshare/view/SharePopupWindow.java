package com.hxqc.socialshare.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;

import com.hxqc.socialshare.R;
import com.hxqc.socialshare.adapter.ShareAdapter;
import com.hxqc.socialshare.manager.ShareManager;
import com.hxqc.socialshare.pojo.ShareContent;


/**
 * Author:胡俊杰
 * Date: 2015/9/11
 * FIXME
 * Todo
 */
public class SharePopupWindow extends Dialog implements AdapterView.OnItemClickListener {
    private static final String TAG = "SharePopupWindow";
    ShareManager shareManager;
Context context;
    public SharePopupWindow(Context context) {
        super(context);
    }

    public SharePopupWindow(Context context, int theme) {
        super(context, theme);
        this.context=context;
//        Log.i("Tag","window   "+context.toString());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        super.onCreate(savedInstanceState);
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.view_popupwindow, null);
        setContentView(mView);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        GridViewNoSlide mGridView = (GridViewNoSlide) findViewById(R.id.share_grid);
        mGridView.setVelocityScale(20);
        ShareAdapter mAdapter = new ShareAdapter(getContext());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.ShareMenuAnimationStyle);  //添加动画
        findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    ShareContent shareContent;

    public void showShare(ShareManager shareManager, ShareContent shareContent) {
        this.shareManager = shareManager;
        this.shareContent = shareContent;
        show();
    }

    /**
     * 跳转
     */
    @Override
    public void onItemClick(AdapterView< ? > adapterView, View view, int i, long l) {
        if (shareManager == null)
            return;
        try {
            switch (i) {
                case 0://微信
                    shareManager.toWX(this.context, shareContent);
                    cancel();
                    break;
                case 1://朋友圈
                    shareManager.toWXCircle(this.context, shareContent);
                    cancel();
                    break;
                case 2://微博
                    shareManager.toWeibo(this.context, shareContent);
                    cancel();

                    break;
                case 3://QQ
                    shareManager.toQQ(this.context, shareContent);
                    cancel();
                    break;
                case 4://QQ空间
                    shareManager.toQQZone(this.context, shareContent);
                    cancel();
                    break;
                case 5://短信
                    shareManager.toSms(this.context, shareContent);
                    cancel();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cancel();
        }

    }


}
