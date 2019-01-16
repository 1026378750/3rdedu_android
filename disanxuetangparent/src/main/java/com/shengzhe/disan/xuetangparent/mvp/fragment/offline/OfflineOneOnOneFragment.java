package com.shengzhe.disan.xuetangparent.mvp.fragment.offline;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.OneToOnePresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.OneOnOneView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/******
 * 一对一
 */
public class OfflineOneOnOneFragment extends BaseFragment implements OneOnOneView.IOneOnOneView,DropDownMenu.DropMenuFragmentManage,RefreshCommonView.RefreshLoadMoreListener,BaseFragment.LazyLoadingListener {
    @BindView(R.id.ddm_common_dropmenu)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private OneToOnePresenter presenter;

    @Override
    public void initData() {
        if (presenter==null){
            presenter = new OneToOnePresenter(mContext,this);
        }
        presenter.initOneOnOneUi();
        presenter.initOneOnOneDatas(this,this);
        setLazyLoadingListener(this);
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

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11005:
                refreshCommonView.notifyData();
                break;
        }
    }

    private int pageNum = 1;
    @Override
    public void startRefresh() {
        pageNum = 1;
        presenter.clearOneOnOneListDatas();
        presenter.loadOneOnOneList(pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadOneOnOneList(pageNum);
    }


    @Override
    public void loadLazyDatas(boolean bool) {
        refreshCommonView.notifyData();
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
