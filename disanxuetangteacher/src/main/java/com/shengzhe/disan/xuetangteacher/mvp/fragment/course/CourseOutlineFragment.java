package com.shengzhe.disan.xuetangteacher.mvp.fragment.course;

import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.courseSquadScheduleBean;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.shengzhe.disan.xuetangteacher.utils.SharePrefUtils.context;

/**
 * Created by 大纲 on 2018/4/2.
 */

public class CourseOutlineFragment extends BaseFragment implements BaseFragment.LazyLoadingListener {
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;

    private SimpleAdapter adapter;
    private List<courseSquadScheduleBean> catalogList = new ArrayList<>();

    private int courseId = 0;

    //构造fragment
    public static CourseOutlineFragment newInstance(int orderId) {
        CourseOutlineFragment fragment = new CourseOutlineFragment();
        Bundle args = new Bundle();
        args.putInt(StringUtils.COURSE_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        courseId = getArguments().getInt(StringUtils.COURSE_ID);
        catalogList.clear();
        adapter = new SimpleAdapter<courseSquadScheduleBean>(context, catalogList, R.layout.item_coursecatalog) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final courseSquadScheduleBean data) {

                int index = catalogList.indexOf(data)+1;
                holder.setText(R.id.tv_catalog_number,index+".");
                holder.setText(R.id.tv_catalog_title,"第"+ ArithUtils.munberToChinese(String.valueOf(index)) +"次  "+data.getTitle());
                //课程状态 1：直播中 2：未开始 3：已结束
              switch(data.getCourseStatus()){
                  case 1:

                      holder.setText(R.id.tv_catalog_status, DateUtil.timeStamp(data.getStartTime() , "MM") + "月" +
                              DateUtil.timeStamp(data.getStartTime(), "dd") + " " + DateUtil.timeStamp(data.getStartTime(), "HH:mm")+"-"+DateUtil.timeStamp(data.getEndTime(), "HH:mm"));
                      holder.setTextColorRes(R.id.tv_catalog_status,R.color.color_ff1d97ea);
                      holder.setTextColorRes(R.id.tv_catalog_title,R.color.color_ff1d97ea);
                      holder.setTextColorRes(R.id.tv_catalog_number,R.color.color_ff1d97ea);
                      break;
                  case 2:
                      //4月17 13:00-15:00
                      holder.setText(R.id.tv_catalog_status, DateUtil.timeStamp(data.getStartTime() , "MM") + "月" +
                              DateUtil.timeStamp(data.getStartTime(), "dd") + " " + DateUtil.timeStamp(data.getStartTime(), "HH:mm")+"-"+DateUtil.timeStamp(data.getEndTime(), "HH:mm"));
                      holder.setTextColorRes(R.id.tv_catalog_status,R.color.color_333333);
                      holder.setTextColorRes(R.id.tv_catalog_title,R.color.color_333333);
                      holder.setTextColorRes(R.id.tv_catalog_number,R.color.color_333333);
                      break;
                  case 3:
                      holder.setText(R.id.tv_catalog_status,"已完成");
                      holder.setTextColorRes(R.id.tv_catalog_status,R.color.color_999999);
                      holder.setTextColorRes(R.id.tv_catalog_title,R.color.color_999999);
                      holder.setTextColorRes(R.id.tv_catalog_number,R.color.color_999999);
                      break;
              }
            }
        };
        refreshCommonView.setIsEmpty(false);
        refreshCommonView.setIsLoadMore(false);
        refreshCommonView.setIsRefresh(false);
        refreshCommonView.setIsAutoLoad(false);
        refreshCommonView.setRecyclerViewAdapter(adapter);
        setLazyLoadingListener(this);
    }


    /**
     * 提交网络请求
     */
    private void postCourseSquadInfo() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId",  courseId);
        ConstantUrl.CLIEN_Info=2;
        httpService.courseSquadSchedule(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<courseSquadScheduleBean>>(mContext, true) {
                    @Override
                    protected void onDone(List<courseSquadScheduleBean>  mCourseSquadScheduleBean) {
                        catalogList.clear();
                        catalogList.addAll(mCourseSquadScheduleBean);
                        if (mCourseSquadScheduleBean.isEmpty()) {
                            mCourseSquadScheduleBean.clear();
                            refreshCommonView.setIsEmpty(true);
                        } else {
                            refreshCommonView.setIsEmpty(false);
                            refreshCommonView.setIsLoadMore(false);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onResultError(ResultException ex) {

                        refreshCommonView.setIsEmpty(false);
                        refreshCommonView.setIsLoadMore(false);
                    }
                });
    }


    @Override
    public int setLayout() {
        return R.layout.common_refresh_notitle;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadLazyDatas(boolean bool) {
        postCourseSquadInfo();
    }
}
