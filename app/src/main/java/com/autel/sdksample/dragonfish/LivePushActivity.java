package com.autel.sdksample.dragonfish;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.autel.internal.video.core.audio.AutelAudioRecorderUtils;
import com.autel.sdksample.R;
import com.autel.sdksample.util.ThreadUtils;
import com.autel.util.log.AutelLog;
import com.autel.video.AutelPlayer;
import com.autel.video.NetWorkProxyJni;
import com.autel.video.engine.VideoIpConst;

import org.apache.http.impl.cookie.DateUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LivePushActivity extends AppCompatActivity implements AutelPlayer.OnAutelPlayerListener {

    private static final String TAG = "LivePush-ddl";

    private TextView mTvMessage1;
    private TextView mTvMessage2;
    private TextView mTvMessage3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_push);
        mTvMessage1 = findViewById(R.id.tv_message1);
        mTvMessage2 = findViewById(R.id.tv_message2);
        mTvMessage3 = findViewById(R.id.tv_message3);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void startLivePush(View view) {
        latestPushTime = System.currentTimeMillis();
        AutelLog.d(TAG, "===>>>RtspHostAddr: "+ VideoIpConst.getRtspHostAddr());
        mTvMessage3.setText(VideoIpConst.getRtspHostAddr());
        AutelPlayer.init();
        Observable.interval(5, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(aLong -> NetWorkProxyJni.ForceKeyFrame(0));
        ThreadUtils.runOnNonUIthread(() -> {
            int rtmpStart = AutelPlayer.rtmpStart(mRtmpUrl);
            AutelAudioRecorderUtils.instance_().startRecording();
            AutelLog.d(TAG, "===>>>rtmpStart: "+rtmpStart);
            ThreadUtils.runOnUiThread(() -> mTvMessage1.setText("rtmpStart: "+rtmpStart));
        });
        AutelPlayer.setOnAutelPlayerListener(this);
    }

//    private String mRtmpUrl = "rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_652190260_63475914&key=a63ce429871800d968ccc1b5306957e7&schedule=rtmp&pflag=1";

    private String mRtmpUrl = "rtmp://a.rtmp.youtube.com/live2/vkzh-1udz-ak4j-mscv-29s4";
    private boolean isLivePushing = false;
    private long latestPushTime = 0;

    @Override
    public void onPlayerRtmpVideoAttr(int i, int i1, int i2) {
        AutelLog.d(TAG,"===>>>onPlayerRtmpVideoAttr, i: "+i+", i1: "+i1+", i2: "+i2);
        if (i != 0 && i1 != 0 && i2 != 0) {
            if (!isLivePushing) {
                isLivePushing = true;
                ThreadUtils.runOnUiThread(() -> Toast.makeText(LivePushActivity.this, "start live push", Toast.LENGTH_SHORT).show());
            } else {
                latestPushTime = System.currentTimeMillis();
                AutelLog.d(TAG,"===>>>onPlayerRtmpVideoAttr, bit rate: "+i+", FPS: "+i1+", Audio bit rate: "+i2);
            }
        } else if (i == 0 && i1 == 0 && i2 == 0) {
            if (System.currentTimeMillis() - latestPushTime > 60_000) {
                ThreadUtils.runOnUiThread(() -> Toast.makeText(LivePushActivity.this, "live push failure", Toast.LENGTH_SHORT).show());
            }
        }
        ThreadUtils.runOnUiThread(() -> mTvMessage2.setText(DateUtils.formatDate(new Date())+ ", bit rate: "+i+", FPS: "+i1+", Audio bit rate: "+i2));
    }

    public void stopLivePush(View view) {
        ThreadUtils.runOnNonUIthread(new Runnable() {
            @Override
            public void run() {
                AutelAudioRecorderUtils.instance_().stopRecording();
                int rtmpStop = AutelPlayer.rtmpStop();
                AutelLog.d(TAG, "===>>>rtmpStop: "+rtmpStop);
                ThreadUtils.runOnUiThread(() -> mTvMessage1.setText("rtmpStop: "+rtmpStop));
            }
        });
    }

}