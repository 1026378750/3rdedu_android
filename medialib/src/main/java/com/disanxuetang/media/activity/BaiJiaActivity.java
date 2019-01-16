package com.disanxuetang.media.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import com.baijiahulian.common.networkv2.HttpException;
import com.baijiahulian.player.BJPlayerView;
import com.baijiahulian.player.OnPlayerViewListener;
import com.baijiahulian.player.bean.SectionItem;
import com.baijiahulian.player.bean.VideoItem;
import com.baijiahulian.player.playerview.BJBottomViewPresenter;
import com.baijiahulian.player.playerview.BJCenterViewPresenter;
import com.baijiahulian.player.playerview.BJTopViewPresenter;
import com.disanxuetang.media.BaseActivity;
import com.disanxuetang.media.R;
import com.disanxuetang.media.util.MediaUtil;
import com.main.disanxuelib.util.LogUtils;
import com.main.disanxuelib.util.SystemBarTintManager;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;

import butterknife.BindView;

/*****
 * 百家云
 */
public class BaiJiaActivity extends BaseActivity implements OnPlayerViewListener {
    private BJPlayerView bjpvBase;
    private CommonCrosswiseBar ccbTitle;
    private int videoBjyId = 0;
    private String videoBjyToken = "";
    private SystemBarTintManager tintManager;

    @Override
    public void initData() {
        initUi();
        videoBjyId = getIntent().getIntExtra("videoBjyId", -1);
        videoBjyToken = getIntent().getStringExtra("videoBjyToken");
        bjpvBase.setBottomPresenter(new BJBottomViewPresenter(bjpvBase.getBottomView()));
        bjpvBase.setTopPresenter(new BJTopViewPresenter(bjpvBase.getTopView()));
        bjpvBase.setCenterPresenter(new BJCenterViewPresenter(bjpvBase.getCenterView()));
        // 使用前要初始化PartnerId
        bjpvBase.initPartner(MediaUtil.PartnerId, BJPlayerView.PLAYER_DEPLOY_ONLINE);
        bjpvBase.setHeadTailPlayMethod(BJPlayerView.HEAD_TAIL_PLAY_NONE);
        int color = Color.parseColor("#080808");
        bjpvBase.setVideoEdgePaddingColor(color);
        //8772053
        //第一个参数为百家云后台配置的视频id，第二个参数为视频token
        bjpvBase.setVideoId(Long.valueOf(videoBjyId), videoBjyToken);
        //  bjpvBase.setVideoId(Long.valueOf(8772053), "5ePT5vnXhyxdak05Ni9msigBWROQ7kXhPO2UzjmF7SWX17lALJNKMA");
        setDatas();
        bjpvBase.playVideo();
    }

    private void initUi() {
        ccbTitle = (CommonCrosswiseBar) findViewById(R.id.ccb_title);
        bjpvBase = (BJPlayerView) findViewById(R.id.bjpv_base);
    }

    private void setDatas() {
        bjpvBase.getTopView().findViewById(R.id.bjplayer_top_back_vertical_btn).setVisibility(View.GONE);
        bjpvBase.getTopView().findViewById(R.id.bjplayer_top_back_vertical_btn).setOnClickListener(this);
        bjpvBase.getTopView().findViewById(R.id.bjplayer_top_title_tv).setVisibility(View.GONE);
        bjpvBase.getTopView().findViewById(R.id.bjplayer_top_back_horizontal_btn).setVisibility(View.GONE);
        bjpvBase.getTopView().findViewById(R.id.bjplayer_top_back_horizontal_btn).setOnClickListener(this);
        findViewById(R.id.common_bar_leftBtn).setOnClickListener(this);
        bjpvBase.setOnPlayerViewListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.common_bar_leftBtn || i == R.id.bjplayer_top_back_vertical_btn || i == R.id.bjplayer_top_back_horizontal_btn) {
            onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (bjpvBase != null) {
            bjpvBase.onConfigurationChanged(newConfig);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ccbTitle.setVisibility(View.GONE);
            bjpvBase.getTopView().findViewById(R.id.bjplayer_top_title_tv).setVisibility(View.INVISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                tintManager = new SystemBarTintManager();
                tintManager.setStatusBarTintColor(UiUtils.getColor(this instanceof BaiJiaActivity ? R.color.color_00000000 : R.color.color_ffffff));
                tintManager.setStatusBarTintEnabled(true);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                tintManager = new SystemBarTintManager();
                tintManager.setStatusBarTintColor(UiUtils.getColor(this instanceof BaiJiaActivity ? R.color.color_00000000 : R.color.color_ffffff));
                tintManager.setStatusBarTintEnabled(true);
            }
            ccbTitle.setVisibility(View.VISIBLE);
            ccbTitle.setTitleText("品牌课");
            ccbTitle.setLeftButton(R.mipmap.ic_black_left_arrow);
            bjpvBase.getTopView().findViewById(R.id.bjplayer_top_back_vertical_btn).setVisibility(View.GONE);
            bjpvBase.getTopView().findViewById(R.id.bjplayer_top_title_tv).setVisibility(View.GONE);
            bjpvBase.getTopView().findViewById(R.id.bjplayer_top_back_horizontal_btn).setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (!bjpvBase.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bjpvBase != null) {
            bjpvBase.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bjpvBase != null) {
            bjpvBase.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bjpvBase != null) {
            bjpvBase.onDestroy();
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_baijia;
    }

    @Override
    public void onVideoInfoInitialized(BJPlayerView bjPlayerView, HttpException exception) {
        //TODO: 视频信息初始化结束
        if (exception != null) {
            // 视频信息初始化成功
            VideoItem videoItem = bjPlayerView.getVideoItem();
        }
    }

    @Override
    public void onPause(BJPlayerView bjPlayerView) {
        //TODO: 暂停
    }

    @Override
    public void onPlay(BJPlayerView bjPlayerView) {
        //TODO: 开始播放
    }

    @Override
    public void onError(BJPlayerView bjPlayerView, int code){
        //TODO: 播放出错
        LogUtils.e("******code**********=" + code);
    }

    @Override
    public void onUpdatePosition(BJPlayerView bjPlayerView, int i) {
        //TODO: 播放过程中更新播放位置
    }

    @Override
    public void onSeekComplete(BJPlayerView bjPlayerView, int i) {
        //TODO: 拖动进度条

    }

    @Override
    public void onSpeedUp(BJPlayerView bjPlayerView, float v) {
        //TODO: 设置倍速播放

    }

    @Override
    public void onVideoDefinition(BJPlayerView bjPlayerView, int i) {
        //TODO: 设置清晰度完成
    }

    @Override
    public void onPlayCompleted(BJPlayerView bjPlayerView, VideoItem videoItem, SectionItem sectionItem) {
        //TODO: 当前视频播放完成 [nextSection已被废弃，请勿使用]

    }

    @Override
    public void onVideoPrepared(BJPlayerView bjPlayerView) {
        //TODO: 准备好了，马上要播放
        // 可以在这时获取视频时长
        bjPlayerView.getDuration();
    }
}