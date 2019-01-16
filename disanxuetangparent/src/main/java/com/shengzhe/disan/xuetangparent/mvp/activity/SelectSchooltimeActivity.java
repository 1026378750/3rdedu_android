package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import com.main.disanxuelib.bean.Calendar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OneToOnePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.SelectSchooltimeView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 选择上课时间 on 2017/12/4.
 */

public class SelectSchooltimeActivity extends BaseActivity implements SelectSchooltimeView.ISelectSchooltimeView {
    @BindView(R.id.cb_schooltime_monday)
    CheckBox mMonday;
    @BindView(R.id.cb_schooltime_tuesday)
    CheckBox mTuesday;
    @BindView(R.id.cb_schooltime_wednesday)
    CheckBox mWednesday;
    @BindView(R.id.cb_schooltime_thursday)
    CheckBox mThursday;
    @BindView(R.id.cb_schooltime_friday)
    CheckBox mFriday;
    @BindView(R.id.cb_schooltime_saturday)
    CheckBox mSaturday;
    @BindView(R.id.cb_schooltime_sunday)
    CheckBox mSunday;
    @BindView(R.id.rv_schooltime_content)
    RecyclerView mContent;
    @BindView(R.id.tv_schooltime_number)
    TextView mMumber;
    @BindView(R.id.tv_schooltime_message)
    TextView mMessage;
    @BindView(R.id.tv_schooltime_confirm)
    RadioButton mConfirm;

    private OneToOnePresenter presenter;
    private int courseId = -1;

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.COURSE_ID,-1);
        if (presenter==null)
            presenter = new OneToOnePresenter(mContext,this);
        presenter.initSelectSchooltimeUi(getSupportFragmentManager());
        presenter.initSelectSchooltimeDatas(courseId,getIntent().getExtras(),Integer.parseInt(TextUtils.isEmpty(getIntent().getStringExtra(StringUtils.num))?"0":getIntent().getStringExtra(StringUtils.num)));
        presenter.getCanTimer(courseId);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_schooltime;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.tv_schooltime_confirm})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_schooltime_confirm:
                ArrayList<Calendar> calList = presenter.getCalList();
                if(calList.isEmpty()){
                    return;
                }
                //循环生成课程表
                Bundle bundle = getIntent().getExtras();
                Intent intent = new Intent(mContext,CreateScheduleActivity.class);
                bundle.putParcelableArrayList(StringUtils.data,calList);
                intent.putExtra(StringUtils.COURSE_ID,courseId);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.clearSelectSchoolDatas();
        finish();
    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11001:
                presenter.selectSchooltime(bundle.getString(StringUtils.EVENT_DATA).split(" "));
                break;
            case IntegerUtil.EVENT_ID_11003:
                //清空
                presenter.clearSelectSchoolDatas();
                break;
        }
        return false;
    }

    @Override
    public CheckBox getMondayView() {
        return mMonday;
    }

    @Override
    public CheckBox getTuesdayView() {
        return mTuesday;
    }

    @Override
    public CheckBox getWednesdayView() {
        return mWednesday;
    }

    @Override
    public CheckBox getThursdayView() {
        return mThursday;
    }

    @Override
    public CheckBox getFridayView() {
        return mFriday;
    }

    @Override
    public CheckBox getSaturdayView() {
        return mSaturday;
    }

    @Override
    public CheckBox getSundayView() {
        return mSunday;
    }

    @Override
    public RecyclerView getContentView() {
        return mContent;
    }

    @Override
    public TextView getMumberView() {
        return mMumber;
    }

    @Override
    public TextView getMessageView() {
        return mMessage;
    }

    @Override
    public RadioButton getConfirmView() {
        return mConfirm;
    }
}
