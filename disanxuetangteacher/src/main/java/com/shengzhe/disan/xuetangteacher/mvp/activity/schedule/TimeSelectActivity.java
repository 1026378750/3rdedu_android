package com.shengzhe.disan.xuetangteacher.mvp.activity.schedule;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.MyCheckBox;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.bean.TeachingTimeBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 授课时间 on 2018/1/16.
 */

public class TimeSelectActivity extends BaseActivity {
    @BindView(R.id.rg_timeselect_title)
    CommonCrosswiseBar mTitle;
    @BindView(R.id.rv_timeselect_review)
    RecyclerView mRecView;

    private SimpleAdapter adapter;
    private ArrayList<ClassHours> classList = new ArrayList<>();
    private Set<String> selectSet = new HashSet<>();

    @Override
    public void initData() {
        classList.clear();
        selectSet.clear();
        ArrayList<TeachingTimeBean> mSeleteAddress = getIntent().getParcelableArrayListExtra(StringUtils.ACTIVITY_DATA);

        for(TeachingTimeBean timeBean : mSeleteAddress){
            if(TextUtils.isEmpty(timeBean.getTimes())){
                continue;
            }
            String[] timeArr = timeBean.getTimes().split(",");
            if(timeArr==null)
                continue;
            for(String time : timeArr){
                selectSet.add(timeBean.getWeek()+"-"+time);
            }
        }

        for (int i = 0; i < 7; i++) {
            int week = i + 1;
            ClassHours classHours = new ClassHours();
            classHours.week = week;
            classHours.time = "06:00-12:00";
            classHours.times=1;
            classHours.timeTag = "上午";

            classList.add(classHours);

            classHours = new ClassHours();
            classHours.week = week;
            classHours.times=2;
            classHours.time = "12:00-18:00";
            classHours.timeTag = "下午";
            classList.add(classHours);

            classHours = new ClassHours();
            classHours.week = week;
            classHours.times=3;
            classHours.time = "18:00-24:00";
            classHours.timeTag = "晚上";
            classList.add(classHours);
        }


        mRecView.setLayoutManager(UiUtils.getGridLayoutManager(3));

        adapter = new SimpleAdapter<ClassHours>(mContext, classList, R.layout.item_timeselect) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final ClassHours data) {
                MyCheckBox myCheckBox = holder.getView(R.id.rg_timeselect);
                myCheckBox.setChecked(selectSet.contains(data.week+"-"+data.times));
                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str = data.week+"-"+data.times;
                        if (selectSet.contains(str)) {//反选
                            selectSet.remove(str);
                        } else {
                            selectSet.add(str);
                        }
                        mTitle.setRightText(selectSet.size() == classList.size() ? "取消" : "全选");
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        mRecView.setAdapter(adapter);
        mTitle.setRightText(selectSet.size() == classList.size() ? "取消" : "全选");
    }

    @Override
    public int setLayout() {
        return R.layout.activity_timeselect;
    }
    private ConfirmDialog dialog;
    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn,R.id.tv_timeselect_comfirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                //全选
                if (mTitle.getRightText().equals("全选")) {
                    mTitle.setRightText("取消");
                    for (ClassHours hour : classList) {
                        selectSet.add(hour.week+"-"+hour.times);
                    }
                } else {
                    mTitle.setRightText("全选");
                    selectSet.clear();
                }
                adapter.notifyDataSetChanged();
                break;

            case R.id.tv_timeselect_comfirm:
                //提交
                if (selectSet.isEmpty()){
                    if(dialog==null) {
                        dialog = ConfirmDialog.newInstance("", "请选择授课时间", "", "确定");
                    }
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                    return;
                }
                Map<Integer,String> newTimeMap = new HashMap<>();
                for(String select : selectSet){
                    String[] selectArr = select.split("-");
                    int key = Integer.parseInt(selectArr[0]);
                    if(newTimeMap.containsKey(key)){
                        String time = newTimeMap.get(key);
                        time += ","+selectArr[1];
                        newTimeMap.put(key,time);
                    }else{
                        newTimeMap.put(key,selectArr[1]);
                    }
                }
                ArrayList<TeachingTimeBean> timeList = new ArrayList<>();
                for(Map.Entry<Integer,String> time : newTimeMap.entrySet()){
                    TeachingTimeBean timeBean = new TeachingTimeBean();
                    timeBean.setWeek(time.getKey());
                    timeBean.setTimes(time.getValue());
                    timeList.add(timeBean);
                }
                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11029);
                bundle.putParcelableArrayList(StringUtils.EVENT_DATA,timeList);
                EventBus.getDefault().post(bundle);
                onBackPressed();
                break;
        }
    }

    class ClassHours{
        //星期
        public int week;
        //具体时间 上午（06:00-12:00）、下午（12:00-18:00）、晚上（18:00-24:00）
        public String time="";
        //表示
        public String timeTag="";

        //是否选中 1表示选中，0表示未选中
        //时间区分，1代表，上，2代表中，3代表下
        public int times;
    }

}
