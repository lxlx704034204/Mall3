package com.hxqc.mall.drivingexam.ui.helpyoupass.picturedescribedetail;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.CustomToolBar;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.drivingexam.api.ExamApiClient;
import com.hxqc.mall.drivingexam.api.callback.FileDownloadCallBack;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.drivingexam.utils.FileUtils;
import com.hxqc.mall.drivingexam.utils.StringUtils;
import com.hxqc.mall.drivingexam.view.CustomMediaPlayer;
import com.hxqc.util.DebugLog;
import com.hxqc.util.FileUtil;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 图文详情
 * Created by zhaofan on 2016/8/19.
 */
public class PictureDescribeActivity extends initActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    CustomToolBar mToolBar;
    CustomMediaPlayer mediaPlayer;
    public String filePath, fileName;
    private String fullPath;
    private GridView gv;
    int saveCurrentPosition;
    long totalSize = 1;
    private TextView mDetailTv;
    private ExamApiClient mApiClient;


    @Override
    public int getLayoutId() {
        return R.layout.activity_kemu2detail;
    }

    @Override
    public void bindView() {
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        mediaPlayer = (CustomMediaPlayer) findViewById(R.id.media_player);
        mDetailTv = (TextView) findViewById(R.id.to_detail);
        gv = (GridView) findViewById(R.id.gv);
        bindListener();

    }

    private void bindListener() {
        gv.setOnItemClickListener(this);
        mDetailTv.setOnClickListener(this);
    }


    @Override
    public void init() {
        mEventBus.register(this);
        mApiClient = new ExamApiClient();
        filePath = this.getExternalCacheDir() + "/Media";
        fileName = StringUtils.hashKeyForDisk("倒车入库") + ".mp4";
        fullPath = filePath + "/" + fileName;
        initToolbar();
        mediaPlayer.mediaStop();
        if (FileUtil.isFileExist(fullPath)) {
            mediaPlayer.setMediaPlayer(filePath, fileName);
        }
        setGridView();

    }


    private void setGridView() {
        String[] str = new String[]{"坡道定点停车和起步", "侧方停车", "曲线行驶", "直角转弯"};
        int[] img = new int[]{R.drawable.pic_detail0, R.drawable.pic_detail1, R.drawable.pic_detail2, R.drawable.pic_detail3};

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("item", str[i]);
            map.put("img", img[i]);
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, list,
                R.layout.item_kemu2_picture_detail, new String[]{"item", "img"},
                new int[]{R.id.tv1, R.id.img});
        gv.setAdapter(adapter);
    }

    private void initToolbar() {
        mToolBar.setTitle("图文详情");
        mToolBar.setRightTitle("视频管理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherExam.toMediaManage(mContext, fullPath, totalSize);
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.to_detail) {
            ActivitySwitcherExam.toKemu2PictureDetailHtml(this, 0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ActivitySwitcherExam.toKemu2PictureDetailHtml(this, position + 1);
    }

    @Subscribe
    public void getEvent(Event msg) {
        //从全屏还原
        switch (msg.what) {
            case VideoviewFullscreenActivity.FULL_SCREEN_RESET:
                mediaPlayer.seekTo((int) msg.obj);
                if ((int) msg.obj == 0) {
                    mediaPlayer.mediaStop();
                } else
                    mediaPlayer.start();
                break;
            //下载视频
            case CustomMediaPlayer.DOWNLOAD_MEDIA:
                download();
                break;

            //删除
            case MediaManageActivity.DELETE_MEDIA:
                recreate();
                break;
        }

    }

    public void download() {
        mApiClient.downloadMedia(new FileDownloadCallBack(filePath, fileName) {
            @Override
            public void onProgress(float progress, long total) {
                totalSize = total < 0 ? 1 : total;
                String currentStr = FileUtils.generateFileSize((long) (total * progress));
                String totalStr = FileUtils.generateFileSize(total);
                DebugLog.i("onProgress", currentStr + "/" + totalStr);
                mediaPlayer.setProgress((int) (progress * 100), currentStr, totalStr);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                ToastHelper.showRedToast(mContext, "网络连接失败，请检查网络");
            }

            @Override
            public void onResponse(File response, int id) {
                DebugLog.e("onResponse", response.getName());
                mediaPlayer.setMediaPlayer(filePath, fileName);
                mediaPlayer.start();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer.seekTo(saveCurrentPosition);
      /*  mediaPlayer.start();
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mediaPlayer.pause();
                    }
                });*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCurrentPosition = mediaPlayer.getCurrentPosition();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApiClient.cancelDownload();
    }


}
