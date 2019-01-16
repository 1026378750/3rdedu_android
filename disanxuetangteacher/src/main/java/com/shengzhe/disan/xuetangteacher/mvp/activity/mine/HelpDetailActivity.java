package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.HelpBean;
import com.main.disanxuelib.bean.HelpSearcher;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 帮助详情 on 2018/5/8.
 */

public class HelpDetailActivity extends BaseActivity {
    @BindView(R.id.tv_helpitem_title)
    TextView mTitle;
    @BindView(R.id.rv_helpitem_content)
    RecyclerView mContent;

    private HelpSearcher helpBean;
    private SimpleAdapter adapter;
    private List<HelpBean> helpList = new ArrayList<>();


    @Override
    public void initData() {
        helpList.clear();
        helpBean = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        mTitle.setText(helpBean.title);
        helpList.addAll(helpBean.helpList);
        adapter = new SimpleAdapter<HelpBean>(mContext, helpList, R.layout.item_help_detail) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final HelpBean data) {
                holder.setText(R.id.tv_help_title, data.title)
                        .setText(R.id.tv_help_content, data.context);
            }
        };
        mContent.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.VERTICAL));
        mContent.setAdapter(adapter);
    }


    @Override
    public int setLayout() {
        return R.layout.activity_helpitem;
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
}
