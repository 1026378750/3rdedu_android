package com.shengzhe.disan.xuetangteacher.mvp.activity.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.TeachingExperienceBean;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.ExperienceItemActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 教学经历 on 2018/1/15.
 */

public class TeacherExperienceActivity extends BaseActivity{
    @BindView(R.id.rcv_teacherexprience)
    RefreshCommonView refreshCommonView;

    private List<TeachingExperienceBean> oneOneExPerience = new ArrayList<>();
    private SimpleAdapter adapter;
    private HttpService httpService;

    @Override
    public void initData() {
        refreshCommonView.setIsLoadMore(false);
        httpService= Http.getHttpService();
        adapter = new SimpleAdapter<TeachingExperienceBean>(mContext, oneOneExPerience, R.layout.item_teacher_experience) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final TeachingExperienceBean data) {

                holder.setText(R.id.ccb_experience_times,DateUtil.timeStamp(data.getStartTime(), "yyyy-MM-dd")+" 至 "+DateUtil.timeStamp(data.getEndTime(), "yyyy-MM-dd"))
                        .setText(R.id.tv_experience_title,data.getSchool())
                        .setText(R.id.tv_experience_content,data.getRemark())
                        .setVisible(R.id.tv_experience_line,oneOneExPerience.indexOf(data)==0)
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                index = oneOneExPerience.indexOf(data);
                                Intent intent = getIntent();
                                intent.putExtra(StringUtils.ACTIVITY_DATA2,data);
                                intent.setClass(mContext,ExperienceItemActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setOnClickListener(R.id.ccb_experience_delte, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ConfirmDialog dialog = ConfirmDialog.newInstance("", "您确定要删除此经历？", "取消", "确定");
                                dialog.setMargin(60)
                                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                                        .setOutCancel(false)
                                        .show(getSupportFragmentManager());
                                dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

                                    @Override
                                    public void dialogStatus(int id) {
                                        switch (id){
                                            case R.id.tv_dialog_ok:
                                                index = oneOneExPerience.indexOf(data);
                                                postDeleteTeachingExperience(data);
                                                break;
                                        }
                                    }
                                });
                            }
                        });
            }
        };
        refreshCommonView.setRecyclerViewAdapter(adapter);
        postQueryExperience();
    }

    /**
     * 查询老师教学经历
     */
    private void postQueryExperience() {
        httpService.queryTeachingExperience()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<TeachingExperienceBean>>(mContext, true) {
                    @Override
                    protected void onDone(List<TeachingExperienceBean> listTeacher) {
                        oneOneExPerience.clear();
                        oneOneExPerience.addAll(listTeacher);
                        refreshCommonView.finishRefresh();
                        refreshCommonView.finishLoadMore();
                        if (oneOneExPerience.isEmpty()){
                            refreshCommonView.setContentBgColor(UiUtils.getColor(R.color.background_color));
                            refreshCommonView.setIsEmpty(true);
                        }else{
                            refreshCommonView.setContentBgColor(UiUtils.getColor(R.color.color_ffffff));
                            refreshCommonView.setIsEmpty(false);
                        }
                        adapter.notifyDataSetChanged();
                        Bundle bundle = new Bundle();
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11036);
                        bundle.putInt(StringUtils.EVENT_DATA,oneOneExPerience.size());
                        EventBus.getDefault().post(bundle);
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                        if (oneOneExPerience.isEmpty()){
                            refreshCommonView.setIsEmpty(true);
                        }else{
                            refreshCommonView.setIsEmpty(false);
                        }
                    }
                });
    }

    /**
     * 删除教学经历
     */
    private void postDeleteTeachingExperience(final TeachingExperienceBean teacheing){
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        map.put("teachingExperienceId", teacheing.getId());
        httpService.deleteTeachingExperience(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String string) {
                        postQueryExperience();
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                    }
                });
    }

    private int index = -1;

    @Override
    public int setLayout() {
        return R.layout.activity_teacherexperience;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                //添加
                index = -1;
                Intent intent = getIntent();
                intent.setClass(this,ExperienceItemActivity.class);
                startActivity(intent);
                break;
        }
    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11018:
                TeachingExperienceBean bean =  bundle.getParcelable(StringUtils.EVENT_DATA);
                if(index==-1){
                    oneOneExPerience.add(0,bean);
                    adapter.notifyDataSetChanged();

                    Bundle newBundle = new Bundle();
                    newBundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11036);
                    newBundle.putInt(StringUtils.EVENT_DATA,oneOneExPerience.size());
                    EventBus.getDefault().post(newBundle);
                }else{//修改
                    oneOneExPerience.get(index).setRemark(bean.getRemark());
                    oneOneExPerience.get(index).setSchool(bean.getSchool());
                    oneOneExPerience.get(index).setStartTime(bean.getStartTime());
                    oneOneExPerience.get(index).setEndTime(bean.getEndTime());
                    adapter.notifyItemChanged(index);
                }
                refreshCommonView.setIsEmpty(false);
                break;
        }
        return false;
    }

}
