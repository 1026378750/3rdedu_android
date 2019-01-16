package com.shengzhe.disan.xuetangparent.activity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.disanxuetang.shortcutbadger.ShortcutBadger;
import com.main.disanxuelib.util.AppManager;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.mvp.fragment.home.HomeFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.OfflineCourseFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.mine.MineFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass.OnlineClassFragment;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.LoginOpentionUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/****
 * 主页
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.ll_radiolist)
    LinearLayout radioList;
    @BindView(R.id.rb_home)
    RadioButton rb_home;
    @BindView(R.id.rb_oneone)
    RadioButton rb_oneone;
    @BindView(R.id.rb_live)
    RadioButton rb_live;
    @BindView(R.id.rb_mine)
    RadioButton rb_mine;
    @BindView(R.id.main_ovlayout)
    View mOvLayout;
    private FragmentManager fragmentManager;
    private String currentFragmentTag;

    @Override
    public void initData() {
        SharedPreferencesManager.setBadgeCount(0);
        boolean success = ShortcutBadger.removeCount(mContext);
        // LogUtils.d("**************extra***************onReceivedMessage:"+success);
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

        fragmentManager = getSupportFragmentManager();
        lastBtn = rb_home;
        switchBtn();
    }

    private Fragment foundFragment;
    @Override
    public int setLayout() {
        return R.layout.activity_main;
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
        foundFragment = fragmentManager.findFragmentByTag(name);
        if (foundFragment == null) {
            switch (name) {
                case "首页":
                    foundFragment = new HomeFragment();
                    break;
                case "线下课程":
                    foundFragment = new OfflineCourseFragment();
                    break;
                case "在线直播":
                    foundFragment = new OnlineClassFragment();
                    break;
                case "我的":
                    foundFragment = new MineFragment();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      /*  if (keyCode == KeyEvent.KEYCODE_BACK) {
            //仅当activity为task根（即首个启动activity）时才生效,这个方法不会改变task中的activity状态，
            // 按下返回键的作用跟按下HOME效果一样；重新点击应用还是回到应用退出前的状态；
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);*/
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_BACK) {

            if (System.currentTimeMillis() - startTime < 2000) {
                //onExit();
                AppManager.getAppManager().AppExit();

            } else {
                startTime = System.currentTimeMillis();
                Toast.makeText(this, "再次点击退出应用程序", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private RadioButton lastBtn = rb_home;

    @Override
    @OnClick({R.id.rb_home, R.id.rb_oneone, R.id.rb_live, R.id.rb_mine})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_home:
                if (lastBtn == rb_home) {
                    return;
                }
                lastBtn = rb_home;
                switchBtn();
                break;

            case R.id.rb_oneone:
                if (lastBtn == rb_oneone) {
                    return;
                }
                lastBtn = rb_oneone;
                switchBtn();
                break;

            case R.id.rb_live:
                if (lastBtn == rb_live) {
                    return;
                }
                lastBtn = rb_live;
                switchBtn();
                break;

            case R.id.rb_mine:
                if (lastBtn == rb_mine) {
                    return;
                }
                if (TextUtils.isEmpty(ConstantUrl.TOKN)) {
                    //尚未登录
                    rb_mine.setChecked(false);
                    LoginOpentionUtil.getInstance().LoginRequest(MainActivity.this);
                    return;
                }
                lastBtn = rb_mine;
                switchBtn();
                break;
        }
    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11006:
                if (bundle.getBoolean(StringUtils.EVENT_DATA)) {
                    //登录成功
                    lastBtn = rb_mine;
                    switchBtn();
                    return false;
                }
                if (lastBtn == rb_mine) {
                    lastBtn = rb_home;
                }
                switchBtn();
                break;

            case IntegerUtil.EVENT_ID_11007:
                //退出登录
                lastBtn = rb_home;
                switchBtn();
                break;

            case IntegerUtil.CONDITION_POPUP_OPEN:
                //打开筛选框
                if (mOvLayout.getVisibility() == View.GONE)
                    mOvLayout.setVisibility(View.VISIBLE);
                break;

            case IntegerUtil.CONDITION_POPUP_ClOSE:
                //关闭筛选框
                if (mOvLayout.getVisibility() == View.VISIBLE)
                    mOvLayout.setVisibility(View.GONE);
                break;

            case IntegerUtil.EVENT_ID_11020:
                //推荐班课
                lastBtn = rb_oneone;
                switchBtn();
                ((OfflineCourseFragment)foundFragment).setCurrentFragment(1);
                break;

            case IntegerUtil.EVENT_ID_11019:
                //直播课
                lastBtn = rb_live;
                switchBtn();
                ((OnlineClassFragment)foundFragment).setCurrentFragment(0);
                break;

            case IntegerUtil.EVENT_ID_11018:
                //品牌课
                lastBtn = rb_live;
                switchBtn();
                ((OnlineClassFragment)foundFragment).setCurrentFragment(1);
                break;

        }
        return false;
    }

    long startTime;

    private void switchBtn() {
        if (lastBtn == rb_home) {
            rb_home.setChecked(true);
            rb_oneone.setChecked(false);
            rb_live.setChecked(false);
            rb_mine.setChecked(false);
            switchFragment("首页");
        } else if (lastBtn == rb_oneone) {
            rb_home.setChecked(false);
            rb_oneone.setChecked(true);
            rb_live.setChecked(false);
            rb_mine.setChecked(false);
            switchFragment("线下课程");
        } else if (lastBtn == rb_live) {
            rb_home.setChecked(false);
            rb_oneone.setChecked(false);
            rb_live.setChecked(true);
            rb_mine.setChecked(false);
            switchFragment("在线直播");
        } else if (lastBtn == rb_mine) {
            rb_home.setChecked(false);
            rb_oneone.setChecked(false);
            rb_live.setChecked(false);
            rb_mine.setChecked(true);
            switchFragment("我的");
        }
    }

}
