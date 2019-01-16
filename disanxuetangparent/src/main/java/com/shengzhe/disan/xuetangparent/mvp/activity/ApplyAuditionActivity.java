package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CoursePresenter;
import com.shengzhe.disan.xuetangparent.mvp.presenter.MainPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.ApplyAuditionView;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.activity.MainActivity;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.bean.City;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.LoginOpentionUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 申请试听 on 2018/3/16.
 */

public class ApplyAuditionActivity extends BaseActivity implements ApplyAuditionView.IApplyAuditionView {
    @BindView(R.id.ccb_apply_city)
    CommonCrosswiseBar ccbCity;
    @BindView(R.id.ccb_apply_grade)
    CommonCrosswiseBar ccbGrade;
    @BindView(R.id.ccb_apply_subject)
    CommonCrosswiseBar ccbSubject;
    @BindView(R.id.bt_apply_submit)
    Button mSubmit;

    private MainPresenter presenter;

    @Override
    public void initData() {
        if (TextUtils.isEmpty(ConstantUrl.TOKN)) {
            //尚未登录
            LoginOpentionUtil.getInstance().LoginRequest(mContext);
            onBackPressed();
            return;
        }
        List<City> cityList = SharedPreferencesManager.getCity();
        List<CourseType> gradeList = SharedPreferencesManager.getGrade();
        List<Subject> subjectList = SharedPreferencesManager.getSubject();

        if (presenter==null)
            presenter = new MainPresenter(mContext,this);
        presenter.initApplyAuditionUi(getSupportFragmentManager());
        presenter.setApplyAuditionDatas(cityList,gradeList,subjectList);

        if (cityList==null||cityList.isEmpty()){
            presenter.getCityList();
        }
        if (gradeList==null||gradeList.isEmpty()||gradeList.size()==1){
            presenter.getGradeList();
        }
        if (subjectList==null||subjectList.isEmpty()){
            presenter.getSubjectList();
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_applyaudition;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.ccb_apply_city,R.id.ccb_apply_grade,R.id.ccb_apply_subject,R.id.bt_apply_submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
                break;

            case R.id.ccb_apply_city:
                //选择城市
                presenter.selectApplyAuditionCity();
                break;

            case R.id.ccb_apply_grade:
                //孩子年级
                presenter.selectApplyAuditionGrade();
                break;

            case R.id.ccb_apply_subject:
                //意向科目
                presenter.selectApplyAuditionSubject();
                break;

            case R.id.bt_apply_submit:
                //提交
                presenter.appCourseListen();
                break;
        }
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {

        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11017:
                if (TextUtils.isEmpty(ConstantUrl.TOKN)||(SharedPreferencesManager.getUserInfo()!=null && SharedPreferencesManager.getUserInfo().getIsApplyCourseListen()==2)) {
                    AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
                }
                initData();
        }
        return false;
    }

    @Override
    public CommonCrosswiseBar getCityView() {
        return ccbCity;
    }

    @Override
    public CommonCrosswiseBar getGradeView() {
        return ccbGrade;
    }

    @Override
    public CommonCrosswiseBar getSubjectView() {
        return ccbSubject;
    }

    @Override
    public Button getSubmitView() {
        return mSubmit;
    }
}
