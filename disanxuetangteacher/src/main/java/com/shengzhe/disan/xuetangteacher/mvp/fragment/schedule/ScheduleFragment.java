package com.shengzhe.disan.xuetangteacher.mvp.fragment.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.ldf.calendar.view.MonthPager;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.bean.CourseBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.ParticipationActivity;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.SchedulePresenter;
import com.shengzhe.disan.xuetangteacher.mvp.view.IScheduleView;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.text.ParseException;
import butterknife.BindView;

/*****
 * 课程表
 */
public class ScheduleFragment extends BaseFragment implements IScheduleView,SchedulePresenter.OnClickPresenter,SchedulePresenter.OnManClickPresenter,SchedulePresenter.OnItemClickPresenter{
    @BindView(R.id.rv_mine_class)
    RecyclerView rvMineClass;
    @BindView(R.id.tv_mine_course_date)
    TextView tvMineCourseDate;
    @BindView(R.id.calendar_view)
    MonthPager calendarView;
    private SchedulePresenter presenter;

    @Override
    public void initData() {
        presenter = new SchedulePresenter(mContext,this);
        presenter.setOnClickPresenter(this);
        presenter.setOnManClickPresenter(this);
        presenter.setOnItemClickPresenter(this);
        presenter.initDate();
        presenter.getMonthStatus();
        presenter.getMySchedule();
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_schedule;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.claerAllDate();
    }

    @Override
    public void onClick(View v) {

    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11001:
            case IntegerUtil.EVENT_ID_11004:
            case IntegerUtil.EVENT_ID_11039:
                presenter.notifyMarkDataAsyn();
                break;

            case IntegerUtil.EVENT_ID_11002:
                presenter.getMonthStatus();
                presenter.getMySchedule();
                break;
        }
    }

    @Override
    public RecyclerView getRvMineClass() {
        return rvMineClass;
    }

    @Override
    public TextView getTvMineCourseDate() {
        return tvMineCourseDate;
    }

    @Override
    public MonthPager getCalendarView() {
        return calendarView;
    }

    @Override
    public void presenterClick(View v, Object obj) {
        if (!(obj instanceof   CourseBean))
            return;
       final CourseBean data = (CourseBean)obj;
        if (data.getCourseType() == 1 && data.getStatus() == 1) {
            try {
                if(DateUtil.compare(DateUtil.createTime(), DateUtil.timeStamp(data.getStartTime(), "yyyy-MM-dd HH:mm"))){
                    ConfirmDialog dialog = ConfirmDialog.newInstance("", "课程已开始，您确定要考勤？", "取消", "确定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                            .setOutCancel(false)
                            .show(getChildFragmentManager());
                    dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

                        @Override
                        public void dialogStatus(int id) {
                            switch (id) {
                                case R.id.tv_dialog_ok:
                                    //确定考勤
                                    presenter.modifyCourseStatus(data);
                                    break;
                            }
                        }
                    });
                }else {
                    ConfirmDialog dialog = ConfirmDialog.newInstance("","未到上课时间", "", "确定");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                            .setOutCancel(false)
                            .show(getChildFragmentManager());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(data.getCourseType()==2){
            //进入
            ConfirmDialog dialog = ConfirmDialog.newInstance("", "暂未开通手机"+(data.getStatus()==1?"观看直播课":"回放") +"，请电脑登陆官网" +
                    "<font color='#1d97ea'>www.3rdedu.com</font>“老师个人中心-我的课表”"+(data.getStatus()==1?"直播":"观看回放"), "", "确定");
            dialog.setMargin(60)
                    .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                    .setOutCancel(false)
                    .show(getChildFragmentManager());
        }
    }

    @Override
    public void presenterManClick(View v, Object obj) {
        if (!(obj instanceof   CourseBean))
            return;
        final CourseBean data = (CourseBean)obj;
        if(data.getCourseType()==4){
            //线下班课
            Intent intent = new Intent(mContext, ParticipationActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA, data.getCourseId());
            startActivity(intent);
        }

    }
    @Override
    public void presenterItemClick(View v, Object obj) {
      /*  if (!(obj instanceof   CourseBean))
            return;
        final CourseBean data = (CourseBean)obj;
        if(data.getCourseType()==4){
            //线下班课
            ConstantUrl.IS_EDIT = false;
            Intent intent =  new Intent(mContext,CourseDetailActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA,data.getCourseId());
            context.startActivity(intent);

        }*/

    }
}
