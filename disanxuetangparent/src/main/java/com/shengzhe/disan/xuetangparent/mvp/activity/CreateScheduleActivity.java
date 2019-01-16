package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Calendar;
import com.main.disanxuelib.bean.Discounts;
import com.main.disanxuelib.bean.Schedule;
import com.main.disanxuelib.bean.TeacherMethod;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.BaseStringUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.bean.CourseOneInfo;
import com.shengzhe.disan.xuetangparent.bean.TeachingMethod;
import com.main.disanxuelib.util.ClassScheduleUtil;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.Arrays;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 循环生成课程表
 * Created by liukui on 2017/12/6.
 */

public class CreateScheduleActivity extends BaseActivity {
    @BindView(R.id.iv_createschedule_image)
    ImageView mImage;
    @BindView(R.id.tv_oneone_isplant)
    ImageView isplant;
    @BindView(R.id.tv_createschedule_name)
    TextView mName;
    @BindView(R.id.tv_createschedule_price)
    TextView mPrice;
    @BindView(R.id.tv_createschedule_time)
    TextView mTime;
    @BindView(R.id.tv_createschedule_message)
    TextView mMessage;
    @BindView(R.id.tv_createschedule_content)
    TextView mContent;
    @BindView(R.id.rv_createschedule_recview)
    RecyclerView mRecView;
    @BindView(R.id.tv_createschedule_payprice)
    TextView mPayPrice;
    @BindView(R.id.tv_createschedule_preprice)
    TextView preprice;
    @BindView(R.id.btn_createschedule_confirm)
    Button mConfirm;

    private ArrayList<Schedule> scheduleList = new ArrayList<>();
    private CourseOneInfo teacher;
    private TeachingMethod teachingMethod;
    private TeacherMethod selectMet;
    private Discounts selectDis;
    private Bundle bundle = null;
    private int courseId = 0;

    @Override
    public void initData() {
        disWeekList.clear();
        courseId = getIntent().getIntExtra(StringUtils.COURSE_ID,0);
        bundle = getIntent().getExtras();
        teacher = bundle.getParcelable(StringUtils.teacher);
        teachingMethod =  bundle.getParcelable(StringUtils.teachingMethod);
        selectMet  =  bundle.getParcelable(StringUtils.selectMet);
        selectDis =  bundle.getParcelable(StringUtils.selectDis);

        ImageUtil.loadCircleImageView(this, teacher.getPhotoUrl(), mImage,R.mipmap.teacher);
        mName.setText(teacher.getTeacherName());
        isplant.setVisibility(teacher.getIdentity()==0?View.GONE:View.VISIBLE);
        preprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        long price =0;
        if(selectMet.campusDiscountSignPrice==0){
            price = selectMet.signPrice;
        }else{
            price = selectMet.campusDiscountSignPrice;
        }
        mPrice.setText("¥ " + ArithUtils.round(price));

        mMessage.setText(StringUtils.getSex(teacher.getSex())+" | "+teacher.getGradeName()+" "+teacher.getSubjectName()+" | "+teacher.getTeachingAge()+"年教龄");
        int num = Integer.parseInt(bundle.getString(StringUtils.num))+(selectDis==null?0:selectDis.giveNum);
        int hours = teachingMethod.classTime*num;

        mPayPrice.setText("¥"+ArithUtils.round(price*(selectDis==null?num:selectDis.buyNum)*teachingMethod.classTime));
        mContent.setText(String.format("课次 %d，单次课时长 %s小时，共%d小时",num,teachingMethod.classTime,hours));
        /*if(selectMet.campusDiscountSignPrice < selectMet.signPrice){
            preprice.setText("¥ " + ArithUtils.round(selectMet.signPrice*hours));
            preprice.setVisibility(View.VISIBLE);
        }*/
        if (selectMet.campusDiscountSignPrice >= selectMet.signPrice&&(selectDis == null||selectDis.giveNum==0)) {//不打折扣,并且没有满赠数量
            preprice.setVisibility(View.GONE);
        } else {
            preprice.setText("¥ " + ArithUtils.round(selectMet.signPrice * hours));
            preprice.setVisibility(View.VISIBLE);
        }
        mRecView.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.VERTICAL));
        mRecView.setAdapter(adapt);

        ArrayList<Calendar> calenterList = bundle.getParcelableArrayList(StringUtils.data);
        ClassScheduleUtil.createSchedule(Integer.parseInt(bundle.getString(StringUtils.num))+(selectDis==null?0:selectDis.giveNum),calenterList,handler);
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
                    mConfirm.setBackgroundResource(R.drawable.btn_rule_default_ok);
                    mConfirm.setEnabled(true);
                    break;
            }
        }
    };

    private int index = 0;
    private SimpleAdapter adapt = new SimpleAdapter<Schedule>(this, scheduleList, R.layout.item_common_bar) {
        @Override
        protected void onBindViewHolder(TrdViewHolder holder, final Schedule data) {
            CommonCrosswiseBar ccb =  holder.getView(R.id.ccb_common_title);
            ccb.setLeftText((scheduleList.indexOf(data)+1)+". " + data.week +" "+data.date+" "+data.time);
            holder.setOnItemListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = scheduleList.indexOf(data);
                    Intent intent =  new Intent(mContext,ModifyCalendarActivity.class);
                    intent.putExtra(StringUtils.COURSE_ID,courseId);
                    bundle.putParcelableArrayList(StringUtils.scheduleList,scheduleList);
                    bundle.putParcelable(StringUtils.schedule,data);
                    bundle.putStringArrayList(StringUtils.WeekList,disWeekList);
                    bundle.putInt(StringUtils.duration,teachingMethod.classTime);
                    bundle.putString(StringUtils.canTime,data.canTime);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    };

    @Override
    public int setLayout() {
        return R.layout.activity_createschedule;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.tv_createschedule_clearall,R.id.btn_createschedule_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_createschedule_clearall:
                //清空
                ConfirmDialog dialog = ConfirmDialog.newInstance("", "清空列表将会清空当前全部选择", "取消", "确定清空");
                dialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

                    @Override
                    public void dialogStatus(int id) {
                        switch (id){
                            case R.id.tv_dialog_ok:
                                //确定
                                scheduleList.clear();
                                adapt.notifyDataSetChanged();
                                Bundle bundle = new Bundle();
                                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11003);
                                EventBus.getDefault().post(bundle);
                                onBackPressed();
                                break;
                        }
                    }
                });
                break;

            case R.id.btn_createschedule_confirm:
                //立即支付
                Intent intent = new Intent(this,OfflineOrderActivity.class);
                bundle.putParcelableArrayList(StringUtils.scheduleList,scheduleList);
                intent.putExtra(StringUtils.COURSE_ID,courseId);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11004:
                Schedule schedule = bundle.getParcelable(StringUtils.EVENT_DATA);
                scheduleList.get(index).date = schedule.date;
                scheduleList.get(index).time = schedule.time;
                scheduleList.get(index).week = schedule.week;
                adapt.notifyItemChanged(index);
                break;

        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduleList!=null)
            scheduleList.clear();
    }

}
