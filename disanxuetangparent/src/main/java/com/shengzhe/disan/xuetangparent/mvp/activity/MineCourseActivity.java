package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.fragment.mine.MineCourseFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.mine.MineScheduleFragment;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liukui on 2017/11/23.
 */

public class MineCourseActivity extends BaseActivity {
    @BindView(R.id.activity_minecourse)
    RelativeLayout mMineCourse;
    @BindView(R.id.rg_minecourse_title)
    RadioGroup mTitle;
    @BindView(R.id.rg_minecourse_minecourse)
    RadioButton rgMinecourseMinecourse;
    @BindView(R.id.rg_minecourse_mineschedule)
    RadioButton rgMinecourseMineschedule;

    @BindView(R.id.common_bar_leftBtn)
    TextView commonBarLeftBtn;

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    private String currentFragmentTag;
    private FragmentManager fragmentManager;
    private int currentIndex = 0;

    @Override
    public void initData() {
        setParameter();
        fragmentManager = getSupportFragmentManager();
        currentIndex = getIntent().getIntExtra(StringUtils.FRAGMENT_INDEX,0);
        mTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rg_minecourse_minecourse:
                        //我的课程
                        if (currentIndex == 0)
                            return;
                        currentIndex = 0;
                        switchFragment("我的课程");
                        break;

                    case R.id.rg_minecourse_mineschedule:
                        //我的课表
                        if (currentIndex == 1)
                            return;
                        currentIndex = 1;
                        switchFragment("我的课表");
                        break;
                }
            }
        });
        if (currentIndex == 1) {
            rgMinecourseMineschedule.setChecked(true);
            rgMinecourseMinecourse.setChecked(false);
            switchFragment("我的课表");
        } else {
            rgMinecourseMinecourse.setChecked(true);
            rgMinecourseMineschedule.setChecked(false);
            switchFragment("我的课程");
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_minecourse;
    }

    public void switchFragment(String name) {
        if (currentFragmentTag != null && currentFragmentTag.equals(name))
            return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        Fragment foundFragment = fragmentManager.findFragmentByTag(name);
        if (foundFragment == null) {
            switch (name) {
                case "我的课程":
                    foundFragment = new MineCourseFragment();
                    break;
                case "我的课表":
                    foundFragment = new MineScheduleFragment();
                    break;
            }
        }

        if (foundFragment.isAdded()) {
            ft.show(foundFragment);
        } else {
            ft.add(R.id.fl_content, foundFragment, name);
        }
        ft.commitAllowingStateLoss();
        //ft.commit();
        currentFragmentTag = name;
    }

    @OnClick({R.id.common_bar_leftBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*****
     * 设置偏移量
     */
    private void setParameter() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (UiUtils.getDimension(R.dimen.title_bar_height) + SystemInfoUtil.getStatusBarHeight()));
        mMineCourse.setPadding(0, mMineCourse.getPaddingTop() + SystemInfoUtil.getStatusBarHeight(), 0, 0);
        mMineCourse.setLayoutParams(params);
    }


}
