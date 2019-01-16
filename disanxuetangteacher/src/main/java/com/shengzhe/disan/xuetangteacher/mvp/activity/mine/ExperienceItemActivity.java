package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.main.disanxuelib.bean.TeachingExperienceBean;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 添加、修改老师经历 on 2018/1/16.
 */

public class ExperienceItemActivity extends BaseActivity {
    @BindView(R.id.et_expenience_starttime)
    TextView mStartTime;
    @BindView(R.id.et_expenience_endtime)
    TextView mEndTime;
    @BindView(R.id.et_expenience_monad)
    EditText mMonad;
    @BindView(R.id.et_expenience_content)
    EditText mContent;
    @BindView(R.id.tv_expenience_content)
    TextView mContentNum;

    private TeachingExperienceBean bean;
    private Calendar currentTime,startTime,endTime;
    private String startTimeStr ="", currentTimeStr = "";
    private int TeachingAge = 0;

    @Override
    public void initData() {
        TeachingAge = getIntent().getIntExtra(StringUtils.EVENT_DATA,0);
        bean = getIntent().getParcelableExtra(StringUtils.EVENT_DATA2);
        currentTime = Calendar.getInstance();
        startTimeStr = (currentTime.get(Calendar.YEAR)-TeachingAge)+"-"+(currentTime.get(Calendar.MONTH)+1<10?"0"+(currentTime.get(Calendar.MONTH)+1)
                :currentTime.get(Calendar.MONTH)+1)+"-"+(currentTime.get(Calendar.DAY_OF_MONTH)<10?"0"+currentTime.get(Calendar.DAY_OF_MONTH):currentTime.get(Calendar.DAY_OF_MONTH));
        currentTimeStr = currentTime.get(Calendar.YEAR)+"-"+(currentTime.get(Calendar.MONTH)+1<10?"0"+(currentTime.get(Calendar.MONTH)+1)
                :currentTime.get(Calendar.MONTH)+1)+"-"+(currentTime.get(Calendar.DAY_OF_MONTH)<10?"0"+currentTime.get(Calendar.DAY_OF_MONTH):currentTime.get(Calendar.DAY_OF_MONTH));

        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //30~400
                count += start;
                mContentNum.setText(count==0?"限30-400字":count>400?"-"+(400-count)+"字": count+"字");
                mContentNum.setTextColor(UiUtils.getColor(count>0&&(count<30||count>400)?R.color.color_ca4341:R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(bean==null){
            bean = new TeachingExperienceBean();
            startTime = Calendar.getInstance();
            endTime = Calendar.getInstance();
            return;
        }
        startTime = DateUtil.timeStamp2Calendar(bean.getStartTime());
        endTime = DateUtil.timeStamp2Calendar(bean.getEndTime());
        setStartTime();
        setEndTime();
        mMonad.setText(bean.getSchool());
        mContent.setText(bean.getRemark());
    }

    private NiceDialog niceDialog;
    @Override
    public int setLayout() {
        return R.layout.item_experience;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.et_expenience_starttime,R.id.et_expenience_endtime, R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.et_expenience_starttime:
                //开始时间
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setLayoutId(R.layout.common_popup_yymmss)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                                SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                                //范围 ：开始时间 ~  结束时间
                                pickerView.setDateRange(startTimeStr,
                                        StringUtils.textIsEmpty(mEndTime.getText().toString())?currentTimeStr:mEndTime.getText().toString().trim())
                                        .setShowDatePicker(startTime.get(Calendar.YEAR) + "-" +
                                        (startTime.get(Calendar.MONTH)+1<10?"0"+(startTime.get(Calendar.MONTH)+1):startTime.get(Calendar.MONTH)+1)+"-"+(startTime.get(Calendar.DAY_OF_MONTH)<10?"0"+
                                        startTime.get(Calendar.DAY_OF_MONTH):startTime.get(Calendar.DAY_OF_MONTH)));
                                holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        colseDialog();
                                    }
                                });
                                pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                                    @Override
                                    public void onResultPicker(Object obj) {
                                        //如果是当年的不显示年份
                                        String selectData = String.valueOf(obj);
                                        if (TextUtils.isEmpty(selectData) || !selectData.contains("-")) {
                                            colseDialog();
                                            return;
                                        }
                                        String[] select = selectData.split("-");
                                        startTime.set(Integer.parseInt(select[0]),Integer.parseInt(select[1])-1,Integer.parseInt(select[2]));
                                        setStartTime();
                                        colseDialog();
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;

            case R.id.et_expenience_endtime:
                //结束
                if (StringUtils.textIsEmpty(mStartTime.getText().toString().trim())){
                    nofifyShowMesg("您先选择的开始日期",null);
                    return;
                }
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setLayoutId(R.layout.common_popup_yymmss)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                                SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                                //范围 ：开始时间 ~  当前时间
                                pickerView.setDateRange(mStartTime.getText().toString().trim(),currentTimeStr).setShowDatePicker(
                                        endTime.get(Calendar.YEAR) + "-" + (endTime.get(Calendar.MONTH)+1<10?"0"+(endTime.get(Calendar.MONTH)+1):endTime.get(Calendar.MONTH)+1)+"-"+(endTime.get(Calendar.DAY_OF_MONTH)<10?"0"+endTime.get(Calendar.DAY_OF_MONTH):endTime.get(Calendar.DAY_OF_MONTH)));
                                holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        colseDialog();
                                    }
                                });
                                pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                                    @Override
                                    public void onResultPicker(Object obj) {
                                        //如果是当年的不显示年份
                                        String selectData = String.valueOf(obj);
                                        if (TextUtils.isEmpty(selectData) || !selectData.contains("-")) {
                                            colseDialog();
                                            return;
                                        }
                                        String[] select = selectData.split("-");
                                        endTime.set(Integer.parseInt(select[0]),Integer.parseInt(select[1])-1,Integer.parseInt(select[2]));
                                        setEndTime();
                                        colseDialog();
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;

            case R.id.common_bar_rightBtn:
                //保存
                if (StringUtils.textIsEmpty(mStartTime.getText().toString().trim())){
                    nofifyShowMesg("您选择的开始日期",null);
                    return;
                }
                if (StringUtils.textIsEmpty(mEndTime.getText().toString().trim())){
                    nofifyShowMesg("您选择的结束日期",null);
                    return;
                }

                bean.setStartTime(startTime.getTime().getTime());
                bean.setEndTime(endTime.getTime().getTime());
                if(bean.getStartTime()>=bean.getEndTime()){
                    nofifyShowMesg("您选择的日期有误",null);
                    return;
                }
                if(StringUtils.textIsEmpty(mMonad.getText().toString())){
                    nofifyShowMesg("请输入您所在单位",mMonad);
                    return;
                }

                bean.setSchool(mMonad.getText().toString());

                if(StringUtils.textIsEmpty(mContent.getText().toString())){
                    nofifyShowMesg("请输入您的教学经历",mContent);
                    return;
                }
                if (mContent.getText().toString().length()<30 || mContent.getText().toString().length()>400) {
                    nofifyShowMesg("教学经历字数范围为30~400字",mContent);
                    return;
                }

                bean.setRemark(mContent.getText().toString());
                postSaveTeachingExperience(bean);
                break;
        }
    }

    private void postSaveTeachingExperience(TeachingExperienceBean teachingExperienceBean){
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        if (teachingExperienceBean.getId()!=-1){
            map.put("teachingExperienceId", teachingExperienceBean.getId());
        }
        map.put("startTime", teachingExperienceBean.getStartTime());
        map.put("endTime",  teachingExperienceBean.getEndTime());
        map.put("school", teachingExperienceBean.getSchool());
        map.put("remark", teachingExperienceBean.getRemark());
        httpService.saveTeachingExperience(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<TeachingExperienceBean>(mContext, true) {
                    @Override
                    protected void onDone(TeachingExperienceBean bean) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11018);
                        bundle.putParcelable(StringUtils.EVENT_DATA,bean);
                        EventBus.getDefault().post(bundle);
                        onBackPressed();
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                    }
                });

    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    private void setStartTime(){
        mStartTime.setText(startTime.get(Calendar.YEAR)+"-"+(startTime.get(Calendar.MONTH)+1<10?"0"+(startTime.get(Calendar.MONTH)+1):startTime.get(Calendar.MONTH)+1)+"-"+(startTime.get(Calendar.DAY_OF_MONTH)<10?"0"+startTime.get(Calendar.DAY_OF_MONTH):startTime.get(Calendar.DAY_OF_MONTH)));
    }

    private void setEndTime(){
        mEndTime.setText(endTime.get(Calendar.YEAR)+"-"+(endTime.get(Calendar.MONTH)+1<10?"0"+(endTime.get(Calendar.MONTH)+1):endTime.get(Calendar.MONTH)+1)+"-"+(endTime.get(Calendar.DAY_OF_MONTH)<10?"0"+endTime.get(Calendar.DAY_OF_MONTH):endTime.get(Calendar.DAY_OF_MONTH)));
    }

    /****
     * 显示提示信息
     * @param mesg
     */
    private void nofifyShowMesg(String mesg ,final EditText currentEdit){
        ConfirmDialog dialog = ConfirmDialog.newInstance("", mesg, "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        if(currentEdit==null)
                            return;
                        currentEdit.requestFocus();
                        break;
                }
            }
        });
    }

}
