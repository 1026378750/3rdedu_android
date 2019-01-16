package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.shengzhe.disan.xuetangparent.mvp.presenter.VideoPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.VideoDeatilView;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.LoginOpentionUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 老师视频课详情
 * Created by liukui on 2017/11/29.
 */

public class VideoDeatilActivity extends BaseActivity implements VideoDeatilView.IVideoDeatilView {
    @BindView(R.id.iv_videodetail_image)
    ImageView mImage;
    @BindView(R.id.tv_videodetail_name)
    TextView mName;
    @BindView(R.id.tv_videodetail_price)
    TextView mPrice;
    @BindView(R.id.tv_videodetail_preprice)
    TextView mPrePrice;
    @BindView(R.id.tv_videodetail_classify)
    TextView mClassify;
    @BindView(R.id.tv_videodetail_teacher)
    TextView mTeacher;
    @BindView(R.id.tv_videodetail_notice)
    TextView mNotice;
    @BindView(R.id.tv_videodetail_introduction)
    TextView mIntroduction;
    @BindView(R.id.tv_teacherdetail_introduction)
    TextView tvTeacherdetailIntroduction;
    @BindView(R.id.tv_liveorder_delte)
    TextView tvLiveorderDelte;
    @BindView(R.id.btn_videodetail_submit)
    Button btnVideodetailSubmit;

    private VideoPresenter presenter;

    private int courseId = -1;

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.COURSE_ID,-1);
        if (presenter==null)
            presenter = new VideoPresenter(mContext,this);
        presenter.initVideoDeatilUi();
        presenter.getVideoDeatil(courseId);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_videodetail;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.btn_videodetail_submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.btn_videodetail_submit:
                //提交
                if (TextUtils.isEmpty(ConstantUrl.TOKN)) {
                    //尚未登录
                    LoginOpentionUtil.getInstance().LoginRequest(this);
                    return;
                }
                Intent intent = new Intent(this, VideoOrderActivity.class);
                intent.putExtra(StringUtils.COURSE_ID,courseId);
                startActivity(intent);
                break;
        }
    }


    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11010:
                Intent intent = new Intent(this,VideoOrderActivity.class);
                intent.putExtra(StringUtils.COURSE_ID,courseId);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public ImageView getImageView() {
        return mImage;
    }

    @Override
    public TextView getNameView() {
        return mName;
    }

    @Override
    public TextView getPriceView() {
        return mPrePrice;
    }

    @Override
    public TextView getPrePriceView() {
        return mPrePrice;
    }

    @Override
    public TextView getClassifyView() {
        return mClassify;
    }

    @Override
    public TextView getTeacherView() {
        return mTeacher;
    }

    @Override
    public TextView getNoticeView() {
        return mNotice;
    }

    @Override
    public TextView getIntroductionView() {
        return mIntroduction;
    }

    @Override
    public TextView getTeacherdetailIntroductionView() {
        return tvTeacherdetailIntroduction;
    }

    @Override
    public TextView getLiveorderDelteView() {
        return tvLiveorderDelte;
    }

    @Override
    public Button getVideodetailSubmitView() {
        return btnVideodetailSubmit;
    }
}
