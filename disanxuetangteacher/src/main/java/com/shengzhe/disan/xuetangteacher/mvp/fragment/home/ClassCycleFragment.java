package com.shengzhe.disan.xuetangteacher.mvp.fragment.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.DateUtil;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.CourseItemBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 课程信息 on 2018/1/17.
 */

public class ClassCycleFragment extends BaseFragment {
    @BindView(R.id.rv_recycle_review)
    RecyclerView refreshCommonView;

    private SimpleAdapter adapter;
    private List<CourseItemBean> scheduleList = new ArrayList<>();
    private CourseLiveBean bean;

    public static ClassCycleFragment newInstance(CourseLiveBean data) {
        ClassCycleFragment fragment = new ClassCycleFragment();
        Bundle args = new Bundle();
        args.putParcelable(StringUtils.FRAGMENT_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        scheduleList.clear();
        bean = getArguments().getParcelable(StringUtils.FRAGMENT_DATA);
        if (bean == null) {
            return;
        }
        loadDatas();
    }

    private void setAdapter() {
        refreshCommonView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SimpleAdapter<CourseItemBean>(mContext, scheduleList, R.layout.item_classcycle) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final CourseItemBean data) {
                holder.setText(R.id.item_classcycle_times, "第" + data.getNumber() + "次")
                        .setText(R.id.item_classcycle_date, DateUtil.timeStamp(data.getStartTime(), "yyyy-MM-dd") + "  " + DateUtil.weekTimeStamp(data.getStartTime(), "yyyy-MM-dd HH:mm") + "    " + DateUtil.timeStamp(data.getStartTime(), "HH:mm"))
                        .setVisible(R.id.item_classcycle_headline, scheduleList.indexOf(data) == 0)
                        .setVisible(R.id.item_classcycle_line, scheduleList.indexOf(data) != scheduleList.size() - 1);
            }
        };
        refreshCommonView.setAdapter(adapter);
    }

    public void loadDatas() {
        HttpService httpService = Http.getHttpService();
        final Map<String, Object> map = new HashMap<>();
        map.put("courseId", bean.courseId);
        httpService.courseItem(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<CourseItemBean>>(mContext, true) {
                    @Override
                    protected void onDone(List<CourseItemBean> courseItemBeanList) {
                        scheduleList = courseItemBeanList;
                        setAdapter();
                    }

                    @Override
                    public void onResultError(ResultException ex) {

                    }
                });
    }

    @Override
    public int setLayout() {
        return R.layout.common_recycle_notitle;
    }

    @Override
    public void onClick(View v) {

    }
}
