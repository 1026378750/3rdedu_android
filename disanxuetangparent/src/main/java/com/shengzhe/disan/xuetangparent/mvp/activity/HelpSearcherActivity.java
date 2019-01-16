package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.BasePageBean;
import com.main.disanxuelib.bean.HelpBean;
import com.main.disanxuelib.util.SoftKeyboard;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 搜索结果展示 on 2018/5/8.
 */

public class HelpSearcherActivity extends BaseActivity implements RefreshCommonView.RefreshLoadMoreListener {
    @BindView(R.id.ll_helpsearcher_head)
    LinearLayout mLlhead;
    @BindView(R.id.et_helpsearcher_searchtext)
    EditText mSearchText;
    @BindView(R.id.tv_helpsearcher_head)
    TextView mHeadText;
    @BindView(R.id.rcv_helpsearcher_searcher)
    RefreshCommonView mRefreshCommonView;

    private SimpleAdapter adapter;
    private List<HelpBean> helpList = new ArrayList<>();
    private HttpService httpService;
    private Set<Integer> openSet = new HashSet<>();

    @Override
    public void initData() {
        helpList.clear();
        openSet.clear();
        mSearchText.setText(getIntent().getStringExtra(StringUtils.ACTIVITY_DATA));
        mSearchText.setSelection(mSearchText.getText().toString().length());
        if (httpService == null) {
            httpService = Http.getHttpService();
        }
        mLlhead.setPadding(mLlhead.getPaddingLeft(), mLlhead.getPaddingTop() + SystemInfoUtil.getStatusBarHeight(), mLlhead.getPaddingRight(), mLlhead.getPaddingBottom());
        mHeadText.setText("请输入您需要查询关键字...");
        //监听软键盘搜索点击
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    seacherDatas();
                    return true;
                }
                return false;
            }
        });

        adapter = new SimpleAdapter<HelpBean>(mContext, helpList, R.layout.item_minehelp_searcher) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final HelpBean data) {
                CommonCrosswiseBar crosswiseBar = holder.getView(R.id.ccb_item_searcher);
                crosswiseBar.setLeftText(data.title);
                boolean isOpen = openSet.contains(data.id);
                if (isOpen)
                    crosswiseBar.setRightButton(R.mipmap.ic_black_down_arrow);
                else
                    crosswiseBar.setRightButton(R.mipmap.ic_black_right_arrow);

                holder.setText(R.id.tv_item_content, data.context)
                        .setVisible(R.id.tv_item_content, isOpen);
                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (openSet.contains(data.id))
                            openSet.remove(data.id);
                        else{
                            openSet.clear();
                            openSet.add(data.id);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        mRefreshCommonView.setRecyclerViewAdapter(adapter);
        mRefreshCommonView.setRefreshLoadMoreListener(this);
    }


    @OnClick({R.id.tv_helpsearcher_back})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_helpsearcher_back:
                onBackPressed();
                break;

        }
    }

    private void seacherDatas() {
        if (StringUtils.textIsEmpty(mSearchText.getText().toString())) {
            ToastUtil.showShort("请输入您需要查询的关键字");
            return;
        }
        //关闭键盘
        SoftKeyboard.hide(mSearchText);
        //点击键盘【搜索】，进行搜索
        mRefreshCommonView.notifyData();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_minehelp_searcher;
    }

    private int pageNum = 1;

    @Override
    public void startRefresh() {
        pageNum = 1;
        helpList.clear();
        openSet.clear();
        getSearcherDatas();
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        getSearcherDatas();
    }

    /****
     * 发送验证码请求
     */
    public void getSearcherDatas() {
        final Map<String, Object> map = new HashMap<>();
        map.put("keyWord", mSearchText.getText().toString().trim());
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        httpService.queryHelp(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<HelpBean>>(mContext, true) {
                    @Override
                    protected void onDone(BasePageBean<HelpBean> commonBean) {
                        finishLoad();
                        mHeadText.setText(StringUtils.textFormatHtml("关于“<font color='#ffae12'>" + mSearchText.getText().toString().trim() + "</font>”" + commonBean.getSize() +"条搜索结果"));
                        helpList.addAll(commonBean.getList());
                        if (helpList.isEmpty()) {
                            helpList.clear();
                            mRefreshCommonView.setIsEmpty(true);
                        } else {
                            mRefreshCommonView.setIsEmpty(false);
                            mRefreshCommonView.setIsLoadMore(commonBean.isHasNextPage());
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        finishLoad();
                    }
                });

    }

    public void finishLoad() {
        mRefreshCommonView.finishRefresh();
        mRefreshCommonView.finishLoadMore();
    }

}