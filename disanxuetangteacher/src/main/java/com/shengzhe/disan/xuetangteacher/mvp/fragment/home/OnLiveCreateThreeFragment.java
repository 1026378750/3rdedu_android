package com.shengzhe.disan.xuetangteacher.mvp.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Schedule;
import com.main.disanxuelib.util.BaseStringUtils;
import com.main.disanxuelib.util.ClassScheduleUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.OnliveCourseBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.schedule.ModifyCalendarActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.Arrays;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by  在线直播了第三步 on 2018/1/17.
 */

public class OnLiveCreateThreeFragment extends BaseFragment {
    @BindView(R.id.tv_onlive_title)
    TextView mTitle;
    @BindView(R.id.rv_recycle_review)
    RecyclerView mRecycleView;
    @BindView(R.id.et_onliveoperator_next)
    Button mNext;

    private OnliveCourseBean onliveCourseBean;
    private SimpleAdapter adapt;
    private ArrayList<Schedule> scheduleList = new ArrayList<>();

    public static OnLiveCreateThreeFragment newInstance(OnliveCourseBean data) {
        OnLiveCreateThreeFragment fragment = new OnLiveCreateThreeFragment();
        Bundle args = new Bundle();
        args.putParcelable(StringUtils.FRAGMENT_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    private int index = 0;
    @Override
    public void initData() {
        disWeekList.clear();
        if (onliveCourseBean==null)
            onliveCourseBean = getArguments().getParcelable(StringUtils.FRAGMENT_DATA);
        if (onliveCourseBean==null){
            onliveCourseBean = new OnliveCourseBean();
        }
        adapt = new SimpleAdapter<Schedule>(mContext, scheduleList, R.layout.item_calendar) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Schedule data) {
                holder.setText(R.id.tv_calender_week,data.week)
                        .setText(R.id.tv_calender_starttime,data.date+" "+data.time);

                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        index = scheduleList.indexOf(data);
                        Intent intent =  new Intent(mContext,ModifyCalendarActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(StringUtils.scheduleList,scheduleList);
                        bundle.putParcelable(StringUtils.schedule,data);
                        bundle.putStringArrayList(StringUtils.WeekList,disWeekList);
                        bundle.putInt(StringUtils.singleTime,onliveCourseBean.singleTime);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecycleView.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.VERTICAL));
        mRecycleView.setAdapter(adapt);
        mTitle.setText("(共"+onliveCourseBean.courseTimes+"次课)");
        ClassScheduleUtil.createSchedule(onliveCourseBean.courseTimes,onliveCourseBean.calList,handler);
    }

    private ArrayList<String> disWeekList = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case IntegerUtil.MESSAGE_ID_60001:
                    scheduleList.clear();
                    Schedule[] array = (Schedule[]) msg.getData().getParcelableArray(BaseStringUtils.HANDLER_DATA);
                    scheduleList.addAll(Arrays.asList(array));
                    disWeekList.addAll(msg.getData().getStringArrayList(BaseStringUtils.HANDLER_DATA2));
                    adapt.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public int setLayout() {
        return  R.layout.fragment_onlivethree;
    }

    @OnClick({R.id.et_onliveoperator_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_onliveoperator_next:
                //保存
                onliveCourseBean.scheduleList.clear();
                onliveCourseBean.scheduleList.addAll(scheduleList);
                postSaveDatas();
                break;
        }
    }

    private void postSaveDatas(){
        HttpService httpService = Http.getHttpService();
        httpService.addAndModifyDirect(onliveCourseBean.getAddParameter())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String str) {
                        startPage();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if(ex.getErrCode()==222222){
                       startPage();
                        }
                    }
                });
    }
    private void startPage(){
        ToastUtil.showShort("提交成功");
        Bundle newBundle = new Bundle();
        newBundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11023);
        EventBus.getDefault().post(newBundle);
        getActivity().finish();


    }

    public void setOnliveCourseBean(OnliveCourseBean data){
        this.onliveCourseBean = data;
        mTitle.setText("(共"+data.courseTimes+"次课)");
        ClassScheduleUtil.createSchedule(data.courseTimes,data.calList,handler);
    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11004:
                Schedule schedule = bundle.getParcelable(StringUtils.EVENT_DATA);
                scheduleList.get(index).date = schedule.date;
                scheduleList.get(index).time = schedule.time;
                scheduleList.get(index).week = schedule.week;
                adapt.notifyItemChanged(index);
                break;

        }
    }

}
