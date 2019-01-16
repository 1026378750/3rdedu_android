package com.shengzhe.disan.xuetangteacher.mvp.activity.schedule;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.bean.OrderItemBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 线下一对一课表时间
 */
public class ScheduleTimeActivity extends BaseActivity {

    @BindView(R.id.ccb_schedule)
    CommonCrosswiseBar ccbSchedule;
    @BindView(R.id.rcv_schedule)
    RecyclerView rcvSchedule;
    @BindView(R.id.activity_schedule_time)
    RelativeLayout activityScheduleTime;
    private ArrayList<OrderItemBean> orderItem;
    private SimpleAdapter adapter;

    @Override
    public void initData() {
        orderItem=  (ArrayList<OrderItemBean>) getIntent().getSerializableExtra("orderItem");
        if(orderItem!=null && orderItem.size()>0){
            ccbSchedule.setTitleText("共"+orderItem.size()+"次课");
        }
        setData();
    }

    private void setData() {
        rcvSchedule.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SimpleAdapter<OrderItemBean>(mContext, orderItem, R.layout.item_classcycle) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final OrderItemBean data) {
                holder.setText(R.id.item_classcycle_times, "第"+data.number+"次")
                        .setText(R.id.item_classcycle_date,   DateUtil.timeStamp(data.startTime, "yyyy-MM-dd")+ "  " +   DateUtil.weekTimeStamp(data.startTime, "yyyy-MM-dd HH:mm") + "    " + DateUtil.timeStamp(data.startTime, "HH:mm"))
                        .setVisible(R.id.item_classcycle_headline, orderItem.indexOf(data) == 0)
                        .setVisible(R.id.item_classcycle_line, orderItem.indexOf(data) != orderItem.size() - 1);
            }
        };
        rcvSchedule.setAdapter(adapter);

    }

    @Override
    public int setLayout() {
        return R.layout.activity_schedule_time;
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
