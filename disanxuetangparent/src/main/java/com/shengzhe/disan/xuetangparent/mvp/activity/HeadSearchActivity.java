package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CommonPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.HeadSearchView;
import butterknife.BindView;
import butterknife.OnClick;

/*****
 * 搜索
 *
 * liukui
 *
 */
public class HeadSearchActivity extends BaseActivity implements RefreshCommonView.RefreshLoadMoreListener,HeadSearchView.IHeadSearchView {
    @BindView(R.id.ll_head_search_head)
    LinearLayout mHeadView;
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;
    @BindView(R.id.et_head_search)
    EditText etHeadSearch;

    private CommonPresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new CommonPresenter(mContext,this);
        presenter.initHeadSearchUi();
        presenter.initHeadSearchDatas(this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_head_search;
    }

    @Override
    @OnClick({R.id.img_black_left_arrow, R.id.txt_serch})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_black_left_arrow:
                onBackPressed();
                break;

            case R.id.txt_serch:
                //得到查询的值
                presenter.searcherDatas();
                break;

        }
    }

    private int pageNum = 1;

    @Override
    public void startRefresh() {
        pageNum = 1;
        presenter.getHeadsearchDatas().clear();
        presenter.loadHeadSearcherDatas(pageNum);
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        presenter.loadHeadSearcherDatas(pageNum);
    }

    @Override
    public LinearLayout getHeadView() {
        return mHeadView;
    }

    @Override
    public RefreshCommonView getRefreshCommonView() {
        return refreshCommonView;
    }

    @Override
    public EditText getHeadSearchView() {
        return etHeadSearch;
    }
}
