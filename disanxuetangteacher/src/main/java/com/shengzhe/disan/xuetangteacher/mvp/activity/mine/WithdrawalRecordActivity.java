package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.adapter.MineDollarAdapter;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.PresentRecordBean;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 提现记录
 */
public class WithdrawalRecordActivity extends BaseActivity implements RefreshCommonView.RefreshLoadMoreListener {
    @BindView(R.id.tv_dollars_can)
    TextView tvDollarsCan;
    @BindView(R.id.tv_dollars_account)
    TextView tvDollarsAccount;
    @BindView(R.id.tv_dollars_flag)
    TextView tvDollarsFlag;
    @BindView(R.id.rcv_withdrawalrecord)
    RefreshCommonView mCommonView;
    private List<PresentRecordBean> listWithdrawal = new ArrayList<>();
    private MineDollarAdapter adapter;

    @Override
    public void initData() {
        Long totalAmount = getIntent().getLongExtra("totalAmount", 0L);
        tvDollarsAccount.setText(ArithUtils.round(totalAmount).equals("0.00") == true ? "0.00" : ArithUtils.round(totalAmount));
        adapter = new MineDollarAdapter(this, listWithdrawal);
        initWithdrawalRecord();
    }

    /**
     * 请求网络得到提现记录
     */
    private void postPresentRecord() {
        HttpService httpService = Http.getHttpService();
        ConstantUrl.CLIEN_Info=2;
        httpService.presentRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<PresentRecordBean>>(mContext, true) {
                    @Override
                    protected void onDone(List<PresentRecordBean> recordBeanList) {
                        if (recordBeanList.isEmpty()) {
                            listWithdrawal.clear();
                            mCommonView.setIsEmpty(true);
                        } else {
                            mCommonView.setIsEmpty(false);
                            mCommonView.finishRefresh();
                            listWithdrawal.clear();
                            listWithdrawal.addAll(recordBeanList);
                            adapter.notifyDataSetChanged();
                            openAllGroup();
                        }

                    }

                    @Override
                    public void onResultError(ResultException ex) {

                    }
                });

    }

    public void initWithdrawalRecord() {
        mCommonView.setExpandableListViewAdapter(adapter);
        mCommonView.setExpandableListArrows();
        mCommonView.setRefreshLoadMoreListener(this);
        mCommonView.setIsLoadMore(false);

    }


    @Override
    public int setLayout() {
        return R.layout.activity_withdrawal_record;
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
    public void startRefresh() {
        postPresentRecord();
    }

    @Override
    public void startLoadMore() {

    }

    /**
     * 打开全部
     */
    private void openAllGroup() {
        //默认展开
        for (int i = 0; i < listWithdrawal.size(); i++) {
            mCommonView.<ExpandableListView>getView().expandGroup(i);
        }

        mCommonView.<ExpandableListView>getView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//返回true,表示不可点击
            }
        });
    }

}
