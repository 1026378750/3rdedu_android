package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 32人参与
 */
public class ClassSumActivity extends BaseActivity {

    @BindView(R.id.ccb_schedule)
    CommonCrosswiseBar ccbSchedule;
    @BindView(R.id.rcv_classsum)
    RecyclerView rcvClasssum;
    private SimpleAdapter adapter;

    @Override
    public void initData() {


    }

    @Override
    public int setLayout() {
        return R.layout.activity_class_sum;
    }

    @OnClick({R.id.common_bar_leftBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
        }
    }


}
