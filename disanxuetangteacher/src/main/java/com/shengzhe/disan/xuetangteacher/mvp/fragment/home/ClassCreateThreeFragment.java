package com.shengzhe.disan.xuetangteacher.mvp.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Calendar;
import com.main.disanxuelib.bean.Schedule;
import com.main.disanxuelib.util.BaseStringUtils;
import com.main.disanxuelib.util.ClassScheduleUtil;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.main.disanxuelib.bean.ClassCourseBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.schedule.ModifyCalendarActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 线下班课 第三步 on 2018/1/17.
 */

public class ClassCreateThreeFragment extends BaseFragment {
    @BindView(R.id.tv_onlive_title)
    TextView mTitle;
    @BindView(R.id.rv_recycle_review)
    RecyclerView mRecycleView;
    @BindView(R.id.et_onliveoperator_next)
    Button mNext;

    private ClassCourseBean onliveCourseBean;
    private SimpleAdapter adapt;
    private ArrayList<Schedule> scheduleList = new ArrayList<>();

    public static ClassCreateThreeFragment newInstance(ClassCourseBean data) {
        ClassCreateThreeFragment fragment = new ClassCreateThreeFragment();
        Bundle args = new Bundle();
        args.putParcelable(StringUtils.FRAGMENT_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    private int index = 0;

    @Override
    public void initData() {
        disWeekList.clear();
        if (onliveCourseBean == null)
            onliveCourseBean = getArguments().getParcelable(StringUtils.FRAGMENT_DATA);
        if (onliveCourseBean == null) {
            onliveCourseBean = new ClassCourseBean();
        }
        adapt = new SimpleAdapter<Schedule>(mContext, scheduleList, R.layout.item_class_calendar) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Schedule data) {
                holder.setText(R.id.tv_calender_week, data.week)
                        .setText(R.id.tv_calender_starttime, data.date + " " + data.time);
                final EditText mTitle = holder.getView(R.id.tv_calender_title);
                mTitle.setText(data.title);

                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        index = scheduleList.indexOf(data);
                        Intent intent = new Intent(mContext, ModifyCalendarActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(StringUtils.scheduleList, scheduleList);
                        bundle.putParcelable(StringUtils.schedule, data);
                        bundle.putStringArrayList(StringUtils.WeekList, disWeekList);
                        bundle.putInt(StringUtils.singleTime, onliveCourseBean.singleTime);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                mTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mTitle.setTextColor(UiUtils.getColor(mTitle.getText().toString().trim().equals(data.title) ? R.color.color_999999 : R.color.color_666666));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        data.title = mTitle.getText().toString().trim();
                    }
                });
            }
        };
        mRecycleView.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.VERTICAL));
        mRecycleView.setAdapter(adapt);
        mTitle.setText("(共" + onliveCourseBean.courseTimes + "次课)");
        ClassScheduleUtil.createSchedule(onliveCourseBean.courseTimes, onliveCourseBean.calList, onliveCourseBean.courseName2, handler);
    }

    private ArrayList<String> disWeekList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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
        return R.layout.fragment_onlivethree;
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

    private void postSaveDatas() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> parameter = onliveCourseBean.getAddParameter();
        parameter.put("classPeriod", getPeriod());
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        File file = new File(onliveCourseBean.pictureUrl);//filePath 图片地址
        RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
        builder.addFormDataPart("pictureUrlFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        builder.addFormDataPart("sign", RequestBodyUtils.setEncrypt(parameter));
        httpService.saveCourseSquad(builder.build().parts())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String str) {
                        ToastUtil.showShort(str);
                        reultData();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showShort(ex.getMessage());
                        if (ex.getErrCode() == 222222) {
                            reultData();
                        }
                    }
                });
    }

    private void reultData() {
        Bundle newBundle = new Bundle();
        newBundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11042);
        EventBus.getDefault().post(newBundle);
        getActivity().finish();
    }

    private String getPeriod() {
        String classPeriod = "";
        for (Calendar bean : onliveCourseBean.calList) {
            String week = DateUtil.getWeek(bean.week);
            if (!classPeriod.contains(week)) {
                classPeriod += week + ",";
            }
        }
        return classPeriod.lastIndexOf(",") == classPeriod.length() - 1 ? classPeriod.substring(0, classPeriod.length() - 1) : classPeriod;
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

    public void setClassCourseBean(ClassCourseBean data) {
        this.onliveCourseBean = data;
        mTitle.setText("(共" + data.courseTimes + "次课)");
        ClassScheduleUtil.createSchedule(data.courseTimes, data.calList, onliveCourseBean.courseName2, handler);
    }
}
