package com.hxqc.mall.amap.control;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;

import com.autonavi.tbt.TrafficFacilityInfo;
import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 语音播报组件
 */
public class TTSController implements AMapNaviListener {

    public static TTSController ttsManager;
    boolean isfinish = true;
    private Context mContext;
    // 合成对象.
    private SpeechSynthesizer mTts;

    /**
     * 初始化监听
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            DebugLog.d("SHIXIN", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                DebugLog.d("SHIXIN", "初始化失败,错误码：" + code);
            } else {
                DebugLog.d("SHIXIN", "初始化成功");
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    private TTSController(Context context) {
        DebugLog.d("SHIXIN", "语音初始化");
        mContext = context.getApplicationContext();
    }

    public static TTSController getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TTSController(context);
        }
        return ttsManager;
    }

    public void init() {
        SpeechUtility.createUtility(mContext, "appid=" + mContext.getString(R.string.app_id));
        // 初始化合成对象.
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
        initSpeechSynthesizer();
    }

    /**
     * 使用SpeechSynthesizer合成语音，不弹出合成Dialog.
     *
     * @param
     */
    public void startSpeaking(final String playText) {
        DebugLog.d("SHIXIN", "startSpeaking: "+playText);
        // 进行语音合成.
        if (mTts != null)
            mTts.startSpeaking(playText, new SynthesizerListener() {

                @Override
                public void onSpeakResumed() {
                    // TODO Auto-generated method stub
                    DebugLog.i("SHIXIN", "onSpeakResumed: "+playText);
                }

                @Override
                public void onSpeakProgress(int arg0, int arg1, int arg2) {
                    // TODO Auto-generated method stub
//                    DebugLog.i("SHIXIN", "onSpeakProgress: "+playText);
                }

                @Override
                public void onSpeakPaused() {
                    // TODO Auto-generated method stub
                    DebugLog.i("SHIXIN", "onSpeakPaused: "+playText);
                }

                @Override
                public void onSpeakBegin() {
                    // TODO Auto-generated method stub
                    DebugLog.i("SHIXIN", "onSpeakBegin: "+playText);
                }

                @Override
                public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                    // TODO Auto-generated method stub
                    DebugLog.i("SHIXIN", "onEvent: arg0: "+arg0+" arg1: "+arg1+" arg2: "+arg2+"  ");
                }

                @Override
                public void onCompleted(SpeechError arg0) {
                    // TODO Auto-generated method stub
                    DebugLog.i("SHIXIN", "onCompleted message: "+arg0.getErrorDescription()+" error code:  "+arg0.getErrorCode());
                }

                @Override
                public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                    // TODO Auto-generated method stub
//                    DebugLog.i("SHIXIN", "onBufferProgress: arg0: "+arg0+" arg1: "+arg1+" arg2: "+arg2+" arg3:  "+arg3);
                }
            });

    }

    public void stopSpeaking() {
        DebugLog.d("SHIXIN", "stopSpeaking");
        if (mTts != null)
            mTts.stopSpeaking();
    }

    private void initSpeechSynthesizer() {

        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "vixy");
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mContext.getString(R.string.preference_key_tts_speed));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mContext.getString(R.string.preference_default_tts_pitch));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, mContext.getString(R.string.preference_default_tts_volume));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");

    }


    public void destroy() {
        DebugLog.d("SHIXIN", "destroy");
        if (mTts != null) {
            DebugLog.d("SHIXIN", "destroy inner");
            mTts.stopSpeaking();
            mTts.destroy();
            ttsManager=null;
        }
    }

    @Override
    public void onArriveDestination() {
        // TODO Auto-generated method stub
        this.startSpeaking("到达目的地");
    }

    @Override
    public void onArrivedWayPoint(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        this.startSpeaking("路径计算失败，请检查网络");
    }

    @Override
    public void onCalculateRouteSuccess() {
        this.startSpeaking("路径计算就绪");
    }

    @Override
    public void onEndEmulatorNavi() {
        this.startSpeaking("导航结束");
    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {
        // TODO Auto-generated method stub
        DebugLog.d("SHIXIN", "onGetNavigationText text: "+arg1);
        startSpeaking(arg1);
    }

    @Override
    public void onInitNaviFailure() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onInitNaviSuccess() {
//	    this.playText("导航初始化成功");
    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        // TODO Auto-generated method stub
        this.startSpeaking("前方路线拥堵，路线重新规划");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        this.startSpeaking("您已偏航");
    }

    @Override
    public void onStartNavi(int arg0) {
//        this.startSpeaking("导航开始");
    }

    @Override
    public void onTrafficStatusUpdate() {
        // TODO Auto-generated method stub
//	    this.playText("交通状态更新");
    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
    }

    @Override
    public void hideCross() {
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }
}
