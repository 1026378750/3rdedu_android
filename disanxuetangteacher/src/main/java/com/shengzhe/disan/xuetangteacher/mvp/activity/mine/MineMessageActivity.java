package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.BasePageBean;
import com.main.disanxuelib.bean.Message;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*****
 * 我的消息
 */
public class MineMessageActivity extends BaseActivity implements  RefreshCommonView.RefreshLoadMoreListener {
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private ArrayList<Message> messageArrayList =new ArrayList<>();
    private  SimpleAdapter adapter;

    @Override
    public void initData() {
        adapter = new SimpleAdapter<Message>(mContext, messageArrayList, R.layout.item_mine_message) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Message data) {
                holder.setText(R.id.tv_meesage_content,data.getContent())
                        .setText(R.id.tv_meesage_time, DateUtil.timeStamp(data.getCreateTime(),"yyyy-MM-dd HH:mm"));
            }
        };
        refreshCommonView.setRecyclerViewAdapter(adapter);
        refreshCommonView.setRefreshLoadMoreListener(this);
    }

    private void loadDatas() {
        HttpService httpService = Http.getHttpService();
        Map<String,Object> map= new HashMap<String, Object>();
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);

        httpService.myMessage(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<Message>>(mContext,true) {
                    @Override
                    protected void onDone(BasePageBean<Message> myMessage) {
                        messageArrayList.addAll(myMessage.getList());

                        refreshCommonView.finishRefresh();
                        refreshCommonView.finishLoadMore();
                        if (messageArrayList.isEmpty()){
                            messageArrayList.clear();
                            refreshCommonView.setIsEmpty(true);
                        }else{
                            refreshCommonView.setIsEmpty(false);
                            refreshCommonView.setIsLoadMore(myMessage.isHasNextPage());
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void  onResultError(ResultException ex){
                        refreshCommonView.finishRefresh();
                        refreshCommonView.finishLoadMore();
                    }
                });

    }

    @Override
    public int setLayout() {
        return R.layout.activity_mine_message;
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
    protected void onDestroy() {
        super.onDestroy();
    }

    private int pageNum = 1;
    @Override
    public void startRefresh() {
        messageArrayList.clear();
        pageNum = 1;
        loadDatas();
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        loadDatas();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11013);
        EventBus.getDefault().post(bundle);
        finish();
    }
}
