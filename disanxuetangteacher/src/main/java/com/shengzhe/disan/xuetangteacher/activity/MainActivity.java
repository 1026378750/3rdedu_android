package com.shengzhe.disan.xuetangteacher.activity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import com.disanxuetang.shortcutbadger.ShortcutBadger;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.LogUtils;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.home.HomeFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.mine.MineFragment;
import com.shengzhe.disan.xuetangteacher.mvp.fragment.schedule.ScheduleFragment;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import butterknife.BindView;
import butterknife.OnClick;

/****
 * 首页
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.ll_radiolist)
    LinearLayout radioList;
    @BindView(R.id.rb_home)
    RadioButton rb_home;
    @BindView(R.id.rb_course)
    RadioButton rb_course;
    @BindView(R.id.rb_mine)
    RadioButton rb_mine;

    private FragmentManager fragmentManager;
    private String currentFragmentTag;

    @Override
    public void initData() {
        SharedPreferencesManager.setBadgeCount(0);
        boolean success = ShortcutBadger.removeCount(mContext);
        LogUtils.d("**************extra***************onReceivedMessage:"+success);
        fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < radioList.getChildCount(); i++) {
            RadioButton child = (RadioButton) radioList.getChildAt(i);
            //挨着给每个RadioButton加入drawable限制边距以控制显示大小
            Drawable[] drs = child.getCompoundDrawables();
            //获取drawables
            Rect r = new Rect(0, 0, drs[1].getMinimumWidth() * 2 / 3, drs[1].getMinimumHeight() * 2 / 3);
            //定义一个Rect边界
            drs[1].setBounds(r);
            child.setCompoundDrawables(null, drs[1], null, null);
        }
        switchFragment();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    public void switchFragment() {
        if (currentFragmentTag != null && currentFragmentTag.equals(lastFragment))
            return;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        Fragment foundFragment = fragmentManager.findFragmentByTag(lastFragment);
        if (foundFragment == null) {
            switch (lastFragment) {
                case "首页":
                    foundFragment = new HomeFragment();
                    break;
                case "课表":
                    foundFragment = new ScheduleFragment();
                    break;
                case "我的":
                    foundFragment = new MineFragment();
                    break;
            }
        }
        if (foundFragment.isAdded()) {
            ft.show(foundFragment);
        } else {
            ft.add(R.id.fl_content, foundFragment, lastFragment);
        }
        ft.commitAllowingStateLoss();
        currentFragmentTag = lastFragment;
    }

    private long startTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - startTime < 2000) {
                AppManager.getAppManager().AppExit();
            } else {
                startTime = System.currentTimeMillis();
                Toast.makeText(this, "再次点击退出应用程序", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private String lastFragment = "首页";

    @Override
    @OnClick({R.id.rb_home, R.id.rb_course , R.id.rb_mine})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_home:
                if (lastFragment.equals("首页")) {
                    return;
                }
                lastFragment = "首页";

                rb_home.setChecked(true);
                rb_course.setChecked(false);
                rb_mine.setChecked(false);
                switchFragment();
                break;

            case R.id.rb_course:
                if (lastFragment.equals("课表")) {
                    return;
                }
                lastFragment = "课表";
                rb_home.setChecked(false);
                rb_course.setChecked(true);
                rb_mine.setChecked(false);
                switchFragment();
                break;

            case R.id.rb_mine:
                if (lastFragment.equals("我的")) {
                    return;
                }
                lastFragment = "我的";
                rb_home.setChecked(false);
                rb_course.setChecked(false);
                rb_mine.setChecked(true);
                switchFragment();
                break;
        }
    }
}

