package com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OnlivePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OnLiveView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;

/**
 * Created by liukui on 2017/11/27.
 * <p>
 * 直播课
 */

public class OnLiveFragment extends BaseFragment implements OnLiveView.IOnLiveView, DropDownMenu.DropMenuFragmentManage, RefreshCommonView.RefreshLoadMoreListener {
    @BindView(R.id.ddm_common_dropmenu)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private OnlivePresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new OnlivePresenter(mContext,this);
        presenter.initOnLiveUi();
        presenter.initOnLiveDatas(this,this);
    }

    @Override
    public int setLayout() {
        return R.layout.common_drop_refresh_layout;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public FragmentTransaction requestFragmentManage() {
        return getChildFragmentManager().beginTransaction();
    }

    private int pageNum = 1;

    @Override
    public void startRefresh() {
        pageNum = 1;
        presenter.clearOnLiveDatas();
        presenter.loadOnLiveList(pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadOnLiveList(pageNum);
    }


    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.CONDITION_VIDEOPOPUP_ClOSE:
                //接受父级通知是否关闭筛选框
                if (mDropDownMenu.isShowing()) {
                    mDropDownMenu.closeMenu();
                    bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.CONDITION_POPUP_ClOSE);
                    EventBus.getDefault().post(bundle);
                }
                break;

            case IntegerUtil.EVENT_ID_11005:
                refreshCommonView.notifyData();
                break;
        }
    }

    @Override
    public DropDownMenu getDropDownMenuView() {
        return mDropDownMenu;
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }
}
