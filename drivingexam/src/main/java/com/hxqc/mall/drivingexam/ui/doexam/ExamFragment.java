package com.hxqc.mall.drivingexam.ui.doexam;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initFragment;
import com.hxqc.mall.drivingexam.db.model.ExamRecord;
import com.hxqc.mall.drivingexam.db.model.ExamRecord_Table;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.drivingexam.model.Options;
import com.hxqc.mall.drivingexam.model.QItems;
import com.hxqc.mall.drivingexam.ui.doexam.adapter.MultiChoiceAdapter;
import com.hxqc.mall.drivingexam.ui.doexam.adapter.SingleChoiceAdapter;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 答题界面
 * Created by zhaofan on 2016/8/1.
 */
public class ExamFragment extends initFragment implements View.OnClickListener {
    private long dateTag;
    private ImageView mImageView;
    private VideoView videoView;
    private ListView lv;
    private SingleChoiceAdapter mSingleChoiceAdapter;
    private MultiChoiceAdapter mMultiChoiceAdapter;
    private RelativeLayout mKonwledgeLay;
    private TextView mKonwledgeDetail;
    int index;
    QItems data;
    View mTitleView;
    private TextView mQuestionTv, type;
    String mAnswerMulti = "", mAnswerSingle = "", mChooseStr = "";
    //  View mSubmitBtn;
    TextView mSubmitBtnTv;
    String questionId;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_doexam;
    }

    @Override
    public void bindView(View view) {
        mTitleView = View.inflate(getActivity(), R.layout.view_driveexam, null);
        mKonwledgeLay = (RelativeLayout) view.findViewById(R.id.konwledge_lay);
        mKonwledgeDetail = (TextView) view.findViewById(R.id.konwledge_detail);
        mSubmitBtnTv = (TextView) view.findViewById(R.id.submit);
        lv = (ListView) view.findViewById(R.id.lv);
        mQuestionTv = (TextView) mTitleView.findViewById(R.id.title);
        mImageView = (ImageView) mTitleView.findViewById(R.id.img);
        videoView = (VideoView) mTitleView.findViewById(R.id.vidio_view);
        type = (TextView) mTitleView.findViewById(R.id.type);

    }

    @Override
    public void init() {
        initDataResource();
        lv.setEnabled(false);
        lv.addHeaderView(mTitleView);

        //获取答案
        StringBuilder answer = new StringBuilder();
        int i = 0, answerCount = 0;
        for (Options entity : data.getOptions()) {
            if (entity.choose.equals("1")) {
                answerCount++;
                mAnswerSingle = i + "";
            }
            answer.append(entity.choose).append(",");
            i++;
        }
        if (answer.length() > 1) {
            mAnswerMulti = answer.substring(0, answer.length() - 1);
        }

        //单选
        if (answerCount == 1) {
            type.setText(TextUtils.isEmpty(data.getOptions().get(3).content) ? "判断题" : "单选题");
            mSingleChoiceAdapter = new SingleChoiceAdapter(getActivity());
            lv.setAdapter(mSingleChoiceAdapter);
            mSingleChoiceAdapter.setAnswer(mAnswerSingle);
            mSingleChoiceAdapter.setIndex(index);
            mSingleChoiceAdapter.setQuestionId(questionId);
            List<Options> mOptionses = data.getOptions();
            if (mOptionses.size() == 4 && TextUtils.isEmpty(mOptionses.get(3).content)) {
                mOptionses = mOptionses.subList(0, 2);
            }
            mSingleChoiceAdapter.setData(mOptionses);
            mSingleChoiceAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showKnowledge();
                    nextPage();
                }
            });
        }
        //多选
        else if (answerCount > 1) {
            type.setText("多选题");
            //   mSubmitBtn = View.inflate(getActivity(), R.layout.view_exam_btn, null);
            //    lv.addFooterView(mSubmitBtn);
            mSubmitBtnTv.setVisibility(View.VISIBLE);
            mMultiChoiceAdapter = new MultiChoiceAdapter(getActivity());
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setAdapter(mMultiChoiceAdapter);
            mMultiChoiceAdapter.setListView(lv);
            mMultiChoiceAdapter.setAnswer(mAnswerMulti);
            mMultiChoiceAdapter.setIndex(index);
            mMultiChoiceAdapter.setData(data.getOptions());
            //    mSubmitBtn.findViewById(R.id.submit).setOnClickListener(this);
            mSubmitBtnTv.setOnClickListener(this);
        }

        completeSetting();
    }

    private void initDataResource() {
        dateTag = getArguments().getLong("dateTag");
        index = getArguments().getInt("index");
        data = getArguments().getParcelable("data");
        questionId = getArguments().getString("id");
        mQuestionTv.setText("             " + data.getQuestion());
        mImageView.setOnClickListener(this);
        mImageView.setVisibility(data.getMediaType().equals("1") ? View.VISIBLE : View.GONE);
        videoView.setVisibility(data.getMediaType().equals("2") ? View.VISIBLE : View.GONE);
        if (data.getMediaType().equals("1")) {
            ImageUtil.setImageNormalSize(getActivity(), mImageView, data.getMediaUrl());
            // ImageUtil.load(data.getMediaUrl(),mImageView);
        }
        if (data.getMediaType().equals("2")) {
            // videoView.setMediaController(new MediaController(this));
            Uri uri = Uri.parse(data.getMediaUrl());
            videoView.setVideoURI(uri);
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.seekTo(0);
                    videoView.start();
                }
            });
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * 已做题目的配置
     */
    private void completeSetting() {
        ExamRecord entity = DbHelper.queryEntity(ExamRecord.class, ExamRecord_Table.num.eq(index));
        if (entity != null && entity.isFinish.equals("1")) {
            showKnowledge();
            //隐藏按钮
            mSubmitBtnTv.setVisibility(View.GONE);
        }
    }


    /**
     * 显示题目解析
     */
    private void showKnowledge() {
        DebugLog.e("Konwledge", " show Konwledge");
        mKonwledgeLay.setVisibility(View.VISIBLE);
        StringBuilder answer = new StringBuilder();
        String[] answerArray = mAnswerMulti.split(",");
        for (int i = 0; i < answerArray.length; i++) {
            if (answerArray[i].equals("1")) {
                answer.append(i).append(",");
            }
        }
        String realAnswer = answer.substring(0, answer.length() - 1)
                .replace("0", "A").replace("1", "B").replace("2", "C").replace("3", "D");
        DebugLog.e("答案", realAnswer);
        mKonwledgeDetail.setText("答案：" + realAnswer + "\n" + "试题分析：" + data.getKonwledge());
    }


    private void nextPage() {
        //下一题
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new Event(questionId, ExamActivity.NEXT_PAGE));
            }
        }, 800);
    }


    public void upDataMultiRecord() {
        DbHelper.delete(ExamRecord.class, ExamRecord_Table.num.eq(index));
        new ExamRecord(index, "1", mChooseStr + "", mAnswerMulti,
                mAnswerMulti.equals(mChooseStr) ? "1" : "0")
                .save();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img) {
            EventBus.getDefault().post(new Event(data.getMediaUrl(), ExamActivity.SHOW_PHOTO_VIEW));
        }
        //多选题提交
        else if (v.getId() == R.id.submit) {
            if (lv.getCheckedItemCount() <= 1) {
                ToastHelper.showGreenToast(getActivity(), "请至少选择两项答案");
                return;
            }
            StringBuilder choose = new StringBuilder();
            for (int i = 0; i < data.getOptions().size(); i++) {
                choose.append(lv.isItemChecked(i) ? "1" : "0").append(",");
            }
            if (choose.length() > 1)
                mChooseStr = choose.substring(0, choose.length() - 1);
            DebugLog.i("mAnswerStr", mAnswerMulti);
            DebugLog.i("mChooseStr", mChooseStr);
            if (DbHelper.queryEntity(ExamRecord.class, ExamRecord_Table.num.eq(index)).isFinish.equals("0")) {
                upDataMultiRecord();
            }
            mSubmitBtnTv.setVisibility(View.GONE);
            mMultiChoiceAdapter.notifyDataSetChanged();
            showKnowledge();
            nextPage();
        }
    }
}
