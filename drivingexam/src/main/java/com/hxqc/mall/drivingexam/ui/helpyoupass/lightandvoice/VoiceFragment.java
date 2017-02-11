package com.hxqc.mall.drivingexam.ui.helpyoupass.lightandvoice;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.GridView;
import android.widget.VideoView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initFragment;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.drivingexam.utils.ResourceUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;

/**
 * 语音
 * Created by zhaofan on 2016/8/23.
 */
public class VoiceFragment extends initFragment {
    private GridView gv;
    private LightButtonAdapter mLightSimulateAdapter;
    int[] resId;
    private VideoView videoView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_voice;
    }

    @Override
    public void bindView(View view) {
        gv = (GridView) view.findViewById(R.id.gv);
        videoView = (VideoView) view.findViewById(R.id.video_view);
    }

    @Override
    public void init() {
        mEventBus.register(this);
        String[] str = new String[]{"考试准备", "起步", "路口直行", "变更车道",
                "公共汽车站", "学校", "直线行驶", "左转",
                "右转", "加减档", "会车", "超车",
                "减速", "限速", "人行横道", "人行横道\n-有行人",
                "隧道", "调头", "靠边停车", ""};

        resId = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            String tag = i + 1 < 10 ? "0" + (i + 1) + "" : (i + 1) + "";
            resId[i] = ResourceUtils.getResourceId(mContext, "hx_exam_yy_icon" + tag, "drawable");
        }
        resId[str.length - 1] = 0;
        mLightSimulateAdapter = new LightButtonAdapter(mContext, gv);
        mLightSimulateAdapter.setVoiceMode(true, resId);
        mLightSimulateAdapter.setData(Arrays.asList(str));
        gv.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        gv.setAdapter(mLightSimulateAdapter);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                reset();
            }
        });
    }


    @Subscribe
    public void getEvent(Event msg) {
        //语音
        if (msg.what.equals(LightButtonAdapter.VOICE_MODE)) {
            int position = (int) msg.obj;
            if (gv.isItemChecked(position)) {
                int musicResId = ResourceUtils.getResourceId(mContext, "hx_exam_voice" + (position), "raw");
                videoView.setVideoURI(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + musicResId));
                videoView.start();
            } else
                videoView.pause();
        }
        //翻页
        else if (msg.what.equals(LightMainActivity.CHANGE_PAGE)) {
            reset();
        }
    }

    private void reset() {
        videoView.stopPlayback();
        gv.clearChoices();
        mLightSimulateAdapter.notifyDataSetChanged();
    }


}
