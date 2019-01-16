package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OneToOnePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OneToOneDetailsView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 线下1对1课程详情页
 */
public class OfflineOneonOneDetailsActivity extends BaseActivity implements OneToOneDetailsView.IOneToOneDetailsView {
    @BindView(R.id.iv_offline_plat)
    ImageView mPlat;
    @BindView(R.id.tv_offline_title)
    TextView mTitle;
    @BindView(R.id.tv_offline_price)
    TextView mPrice;
    @BindView(R.id.tv_offline_preprice)
    TextView mPrePrice;
    @BindView(R.id.iv_offline_image)
    ImageView mImage;
    @BindView(R.id.tv_offline_name)
    TextView mName;
    @BindView(R.id.tv_offline_message)
    TextView mMessage;
    @BindView(R.id.iv_quality_certification)
    ImageView mQuality;
    @BindView(R.id.iv_realname_certification)
    ImageView mRealname;
    @BindView(R.id.iv_teacher_certification)
    ImageView mTeacher;
    @BindView(R.id.iv_education_certification)
    ImageView mEducation;
    @BindView(R.id.tv_offline_kcxz)
    TextView mKcxz;
    @BindView(R.id.tv_offline_kcjj)
    TextView mKcjj;
    @BindView(R.id.rb_offline_apply)
    RadioButton mApply;
    @BindView(R.id.btn_offline_confirm)
    Button btnOfflineConfirm;
    @BindView(R.id.rb_line)
    View rbLine;

    private OneToOnePresenter presenter;

    private int courseId = -1;
    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.COURSE_ID,-1);
        if (presenter==null)
            presenter = new OneToOnePresenter(mContext,this);
        presenter.initOneToOneDetailsUi(getSupportFragmentManager());
        presenter.getCourseOneInfo(courseId);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_offline_oneon_one_details;
    }


    @OnClick({R.id.common_bar_leftBtn, R.id.rb_offline_apply, R.id.btn_offline_confirm, R.id.iv_offline_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                //返回
                onBackPressed();
                break;

            case R.id.rb_offline_apply:
                //申请试听
                presenter.applyDetailDatas(courseId);
                break;

            case R.id.iv_offline_back:
                //点击返回
                presenter.oneDetailBack();
                break;

            case R.id.btn_offline_confirm:
                //立即购买
                presenter.oneDetailConfirm(getIntent());
                break;
        }
    }

    @Override
    public ImageView getPlatView() {
        return mPlat;
    }

    @Override
    public TextView getTitleView() {
        return mTitle;
    }

    @Override
    public TextView getPriceView() {
        return mPrice;
    }

    @Override
    public TextView getPrePriceView() {
        return mPrePrice;
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
    public TextView getMessageView() {
        return mMessage;
    }

    @Override
    public ImageView getQualityView() {
        return mQuality;
    }

    @Override
    public ImageView getRealnameView() {
        return mRealname;
    }

    @Override
    public ImageView getTeacherView() {
        return mTeacher;
    }

    @Override
    public ImageView getEducationView() {
        return mEducation;
    }

    @Override
    public TextView getKcxzView() {
        return mKcxz;
    }

    @Override
    public TextView getKcjjView() {
        return mKcjj;
    }

    @Override
    public RadioButton getApplyView() {
        return mApply;
    }

    @Override
    public Button getOfflineConfirmView() {
        return btnOfflineConfirm;
    }

    @Override
    public View getLineView() {
        return rbLine;
    }
}
