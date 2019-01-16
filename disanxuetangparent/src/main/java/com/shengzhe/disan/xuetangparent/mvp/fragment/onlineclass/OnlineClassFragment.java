package com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;

/****
 * 在线课堂
 */
public class OnlineClassFragment extends BaseFragment{
    @BindView(R.id.fragment_online_class)
    LinearLayout onlineClass;
    @BindView(R.id.rg_minecourse_title)
    RadioGroup mTitle;
    private String currentFragmentTag;
    private int currentIndex = 0;

    @Override
    public void initData() {
        setParameter();
        //添加到数组
        mTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rg_minecourse_minecourse:
                        //直播课
                        if(currentIndex==0)
                            return;
                        currentIndex = 0;
                        switchFragment("直播课");
                        break;

                    case R.id.rg_minecourse_mineschedule:
                        //视频课
                        if(currentIndex==1)
                            return;
                        currentIndex = 1;
                        switchFragment("品牌课");
                        closePopupWin();
                        break;
                }
            }
        });
        setCurrentFragment(currentIndex);
    }

    public void setCurrentFragment(int index){
        switch (index){
            case 0:
                currentIndex = 0;
                switchFragment("直播课");
                mTitle.check(R.id.rg_minecourse_minecourse);
                break;

            case 1:
                currentIndex = 1;
                switchFragment("品牌课");
                mTitle.check(R.id.rg_minecourse_mineschedule);
                break;
        }
    }

    private void switchFragment(String name) {
        if (currentFragmentTag != null && currentFragmentTag.equals(name))
            return;
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        Fragment foundFragment = fragmentManager.findFragmentByTag(name);
        if (foundFragment == null) {
            switch (name){
                case "直播课":
                    foundFragment = new OnLiveFragment();
                    break;
                case "品牌课":
                    foundFragment = new VideoFragment();
                    break;
            }
        }

        if (foundFragment.isAdded()) {
            ft.show(foundFragment);
        } else {
            ft.add(R.id.fl_content, foundFragment, name);
        }
        ft.commitAllowingStateLoss();
        currentFragmentTag = name;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_online_class;
    }


    @Override
    public void onClick(View v) {

    }

    /*****
     * 设置偏移量
     */
    private void setParameter() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(UiUtils.getDimension(R.dimen.title_bar_height) + SystemInfoUtil.getStatusBarHeight()));
        mTitle.setPadding(0,mTitle.getPaddingTop()+SystemInfoUtil.getStatusBarHeight(),0,0);
        mTitle.setLayoutParams(params);
    }

    private void closePopupWin(){
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.CONDITION_VIDEOPOPUP_ClOSE);
        EventBus.getDefault().post(bundle);
    }

}
