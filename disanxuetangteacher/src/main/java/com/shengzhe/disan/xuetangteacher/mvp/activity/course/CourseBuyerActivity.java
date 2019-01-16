package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.view.View;
import android.widget.ImageView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.BuyerBean;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 课程购买人数列表 on 2018/4/2.
 */

public class CourseBuyerActivity extends BaseActivity implements  RefreshCommonView.RefreshLoadMoreListener {
    @BindView(R.id.rcv_buyers_commonlayout)
    RefreshCommonView refreshCommonView;

    private List<BuyerBean> messageArrayList =new ArrayList<>();
    private SimpleAdapter adapter;

    private int courseId = -1;

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra(StringUtils.ACTIVITY_DATA,-1);
        if (courseId==-1)
            return;
        adapter = new SimpleAdapter<BuyerBean>(mContext, messageArrayList, R.layout.item_coursebuyer) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final BuyerBean data) {
                holder
                        .setText(R.id.tv_buyer_ordernum,"订单编号："+data.orderCode)
                        .setText(R.id.tv_buyer_time, DateUtil.timeStamp(data.buyerTime, "MM-dd HH:mm"))
                        .setText(R.id.tv_buyer_name,data.studentName)
                        .setText(R.id.tv_buyer_type,data.isJoin==1?"插班":"全程")
                        .setText(R.id.tv_buyer_phone,data.mobile)
                        .setText(R.id.tv_buyer_jointimes,data.areadyNum+"次课");

                ImageUtil.loadImageViewLoding(mContext,data.photoUrl,holder.<ImageView>getView(R.id.tv_buyer_image),R.mipmap.default_error,R.mipmap.default_error);
            }
        };
        refreshCommonView.setRecyclerViewAdapter(adapter);
        refreshCommonView.setRefreshLoadMoreListener(this);
        refreshCommonView.setIsRefresh(false);
        loadDatas();
    }

    private void loadDatas() {
        HttpService httpService = Http.getHttpService();
        Map<String,Object> map= new HashMap<String, Object>();
        map.put("courseId", courseId);
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);

        httpService.courseSquadStudentSchedule(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BuyerBean>>(mContext,true) {
                    @Override
                    protected void onDone(List<BuyerBean> beanList) {
                        messageArrayList.clear();
                        messageArrayList.addAll(beanList);
                        refreshCommonView.finishRefresh();
                        refreshCommonView.finishLoadMore();
                        if (messageArrayList.isEmpty()){
                            messageArrayList.clear();
                            refreshCommonView.setIsEmpty(true);
                        }else{
                            refreshCommonView.setIsEmpty(false);
                            refreshCommonView.setIsLoadMore(false);
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
        return R.layout.activity_coursebuyer;
    }

    private int pageNum = 1;
    @Override
    public void startRefresh() {
        messageArrayList.clear();
        pageNum = 1;
        //loadDatas();
    }

    @Override
    public void startLoadMore() {
        pageNum++;
        //loadDatas();
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
