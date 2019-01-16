package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import android.widget.ExpandableListView;
import com.main.disanxuelib.bean.CourseType;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.adapter.CourseExpandableAdapter;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CommonPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.SelectGradeView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liukui on 2017/12/18.
 *
 * 年级选择
 *
 */

public class SelectGradeActivity extends BaseActivity implements CourseExpandableAdapter.SelectTagListener,SelectGradeView.ISelectGradeView {
    @BindView(R.id.ccb_courseclazz)
    ExpandableListView courseClazz;

    private CommonPresenter presenter;

    @Override
    public void initData() {
        CourseType type = getIntent().getParcelableExtra("courseType");
        int fatherId = getIntent().getIntExtra("fatherId",-1);
        if (presenter==null)
            presenter = new CommonPresenter(mContext,this);
        presenter.initSelectGradeUi();
        presenter.initSelectGradeDatas(type,fatherId,this);
        presenter.getGradeListAll();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_studentgrade;
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

    @Override
    public ExpandableListView getCourseClazzView() {
        return courseClazz;
    }

    @Override
    public void tagOnClickListener(View v,final CourseType type,final int fatherId) {
        presenter.saveGradePersonalData(type,fatherId);
    }

}
