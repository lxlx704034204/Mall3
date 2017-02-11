package com.hxqc.mall.drivingexam.ui.helpyoupass.lightandvoice;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.base.BaseActivity;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initFragment;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.drivingexam.model.LightQuestion;
import com.hxqc.mall.drivingexam.model.LightTime;
import com.hxqc.mall.drivingexam.utils.ResourceUtils;
import com.hxqc.util.DebugLog;
import com.hxqc.util.OtherUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 灯光
 * Created by zhaofan on 2016/8/22.
 */
public class LightFragment extends initFragment {
    private GridView gv;
    private RelativeLayout tipLayout;
    private LightButtonAdapter mLightSimulateAdapter;
    private VideoView videoView;
    List<Integer> timeList;
    List<LightTime> mTimeList;
    private ListView lv;
    private View divide;
    private LightQuestionAdapter mLightQuestionAdapter;
    private List<LightQuestion> mLightQuetions;
    int postion = 0;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_light;
    }

    @Override
    public void bindView(View view) {
        gv = (GridView) view.findViewById(R.id.gv);
        videoView = (VideoView) view.findViewById(R.id.video_view);
        lv = (ListView) view.findViewById(R.id.lv);
        divide = view.findViewById(R.id.divide2);
        tipLayout = (RelativeLayout) view.findViewById(R.id.tip);

    }

    @Override
    public void init() {
        mEventBus.register(this);
        showTip();
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            mList.add("模拟" + (i + 1));
        }
        gv.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        mLightSimulateAdapter = new LightButtonAdapter(getActivity(), gv);
        mLightSimulateAdapter.setData(mList);
        gv.setAdapter(mLightSimulateAdapter);

        handler.postDelayed(run_scroll_up, 0);

        mLightQuestionAdapter = new LightQuestionAdapter(mContext);
        lv.setAdapter(mLightQuestionAdapter);
        //  lv.addFooterView(View.inflate(mContext, R.layout.view_light_tip, null));

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                gv.clearChoices();
                mLightSimulateAdapter.notifyDataSetChanged();
            }
        });

        mTimeList = new Gson().fromJson(getFromAssets("light_time"), new TypeToken<List<LightTime>>() {
        }.getType());
    }

    private void showTip() {
        tipLayout.setVisibility((boolean) ((BaseActivity) mContext).mSpUtils.get("showtip", false) ?
                View.GONE : View.VISIBLE);
        tipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipLayout.setVisibility(View.GONE);
                ((BaseActivity) mContext).mSpUtils.put("showtip", true);
            }
        });
    }


    Handler handler = new Handler();

    Runnable run_scroll_up = new Runnable() {
        public void run() {
            if (timeList != null && !timeList.isEmpty())
                if (videoView.getCurrentPosition() > timeList.get(0)) {
                    DebugLog.i("getDuration：", videoView.getCurrentPosition() + "");
                    DebugLog.e("移动到：", timeList.get(0) + " " + postion);
                    timeList.remove(0);
                    lv.smoothScrollToPositionFromTop(postion, 0);
                    postion++;
                }

            handler.postDelayed(this, 1000);

        }
    };


    @Subscribe
    public void getEvent(Event msg) {
        //灯光
        if (msg.what.equals(LightButtonAdapter.LIGHT_MODE)) {
            int position = (int) msg.obj;
            if (gv.isItemChecked(position)) {
                lv.setVisibility(View.VISIBLE);
                divide.setVisibility(View.VISIBLE);

                int musicResId = ResourceUtils.getResourceId(mContext, "hx_exam_light" + (position + 1), "raw");
                videoView.setVideoURI(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + musicResId));
                videoView.start();
                timeList = new ArrayList<>(mTimeList.get(position).getTime());
                upDataListView(position);
                postion = 0;
                lv.setSelection(postion);

            } else {
                videoView.pause();
            }
        }

        //翻页
        else if (msg.what.equals(LightMainActivity.CHANGE_PAGE)) {
            reset();
        }

    }

    private void upDataListView(int pos) {
        mLightQuetions = new Gson().fromJson(getFromAssets("light_question" + (pos + 1)),
                new TypeToken<List<LightQuestion>>() {
                }.getType());
        mLightQuestionAdapter.setData(mLightQuetions);
    }

    private String getFromAssets(String file) {
        String a = OtherUtil.getFromAssets(mContext, file)
                .replace(" ", "").replace("\r", "").replace("\n", "");
        DebugLog.i("getFromAssets", a);
        return a;
    }

    private void reset() {
        videoView.stopPlayback();
        //lv.setVisibility(View.GONE);
        //divide.setVisibility(View.GONE);
        gv.clearChoices();
        mLightSimulateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        reset();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


}
