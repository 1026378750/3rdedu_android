package com.shengzhe.disan.xuetangparent.mvp.fragment.onlineclass;

import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.mvp.presenter.VideoPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.ViedoItemView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;

/**
 * Created by 视频课 on 2017/11/27.
 */

public class VideoItemFragment extends BaseFragment implements ViedoItemView.IViedoItemView,BaseFragment.LazyLoadingListener,RefreshCommonView.RefreshLoadMoreListener{
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private VideoPresenter presenter;

    //构造fragment
    public static VideoItemFragment newInstance(String type,int videoType) {
        VideoItemFragment fragment = new VideoItemFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putInt("videotype",videoType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new VideoPresenter(mContext,this);
        presenter.initVideoItemUi();
        presenter.initVideoItemDatas(getArguments().getInt("videotype"),this);
        setLazyLoadingListener(this);
    }

    @Override
    public int setLayout() {
        return R.layout.common_refresh_notitle2;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
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

    @Override
    public void loadLazyDatas(boolean bool) {
        refreshCommonView.notifyData();
    }

    private int pageNum = 1;

    @Override
    public void startRefresh() {
        presenter.clearVideoItemList();
        pageNum = 1;
        presenter.loadVideoItemList(pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadVideoItemList(pageNum);
    }
}
