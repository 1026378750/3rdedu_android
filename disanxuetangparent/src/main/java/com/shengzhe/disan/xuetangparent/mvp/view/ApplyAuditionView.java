package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import com.main.disanxuelib.bean.CourseSubject;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.City;
import com.shengzhe.disan.xuetangparent.bean.User;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 申请试听 on 2018/4/24.
 */

public class ApplyAuditionView extends BaseView {
    private NiceDialog niceDialog;
    private List<City> cityList;
    private List<CourseType> gradeList;
    private  List<Subject> subjectList;
    private List<String> cityStrList = new ArrayList<>();
    private List<String> gradeStrList = new ArrayList<>();
    private  List<String> subjectStrList = new ArrayList<>();
    private Map<String,List<String>> gradeStrMap = new HashMap<>();
    private City selectCity = null;
    private Subject selectSubject = null;
    private FragmentManager fragmentManager;

    public ApplyAuditionView(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
    }

    private IApplyAuditionView iView;
    public void setIApplyAuditionView(IApplyAuditionView iView){
        this.iView = iView;
    }

    public void initDatas(List<City> cityList, List<CourseType> gradeList, List<Subject> subjectList) {
        this.cityList = cityList;
        this.gradeList = gradeList;
        this.subjectList = subjectList;
        iView.getSubmitView().setEnabled(false);

        if (cityList==null||cityList.isEmpty()){
            iView.getCityView().setEnabled(false);
            this.cityList = new ArrayList<>();
        }else{
            getCityStrList();
        }
        if (gradeList==null||gradeList.isEmpty()||gradeList.size()==1){
            iView.getGradeView().setEnabled(false);
            this.gradeList = new ArrayList<>();
        }else{
            getGradeStrList();
        }
        if (subjectList==null||subjectList.isEmpty()){
            iView.getSubjectView().setEnabled(false);
            this.subjectList = new ArrayList<>();
        }else{
            getSubjectStrList();
        }

    }

    private void getCityStrList(){
        if (cityList==null||cityList.isEmpty())
            return;
        cityStrList.clear();
        for(City city : cityList){
            cityStrList.add(city.cityName);
        }
    }

    private void getGradeStrList(){
        if (gradeList==null||gradeList.isEmpty())
            return;
        gradeStrList.clear();
        gradeStrMap.clear();
        gradeList.remove(0);
        for(CourseType courseType : gradeList){
            if (courseType.id==-1)
                continue;
            gradeStrList.add(courseType.name);
            gradeStrMap.put(courseType.name,courseType.getChildStrList());
        }
    }

    private void getSubjectStrList(){
        if (subjectList==null||subjectList.isEmpty())
            return;
        subjectStrList.clear();
        for(Subject subject : subjectList){
            subjectStrList.add(subject.getSubjectName());
        }
    }

    public void setResultAddressDatas(String str) {
        SharedPreferencesManager.saveCity(str);
        cityList.clear();
        cityList.addAll(SharedPreferencesManager.getCity());
        getCityStrList();
        iView.getCityView().setEnabled(true);
    }

    public void setResultGradeDatas(String str) {
        SharedPreferencesManager.saveGrade(str);
        gradeList.clear();
        gradeList.addAll(getGrade(str));
        getGradeStrList();
        iView.getGradeView().setEnabled(true);
    }

    /*****
     * 获取年级
     * @return
     */
    private List<CourseType> getGrade(String subject) {
        List<CourseType> list = new ArrayList<>();
        try {
            list.clear();
            CourseType courseType = new CourseType();
            List<CourseSubject> childList = new ArrayList<>();
            CourseSubject subject1 = new CourseSubject();
            courseType.id = -1;
            courseType.name = "";

            subject1.id = -1;
            subject1.name ="不限年级";
            childList.add(subject1);
            courseType.childList.addAll(childList);
            list.add(courseType);

            JSONArray array = new JSONArray(subject);
            for (int i=0;i<array.length();i++){
                CourseType type = new CourseType();
                type.formatJson(array.optJSONObject(i));
                list.add(type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void setResultSubjectDatas(String str) {
        SharedPreferencesManager.saveSubject(str);
        subjectList.clear();
        subjectList.addAll(SharedPreferencesManager.getSubject());
        getGradeStrList();
        iView.getSubmitView().setEnabled(true);
    }

    public int getGradeId() {
        String[] grade = iView.getGradeView().getRightText().split(" ");
        CourseSubject subject = gradeList.get(gradeStrList.indexOf(grade[0])).childList.get(gradeStrMap.get(grade[0]).indexOf(grade[1]));
        return subject.id;
    }

    public void setResultTryListen() {
        ConfirmDialog dialog = ConfirmDialog.newInstance("", "申请成功，稍后助教会与您联系,请您耐心等待", "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(fragmentManager);
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        User user = SharedPreferencesManager.getUserInfo();
                        user.setIsApplyCourseListen(2);
                        SharedPreferencesManager.setUserInfo(user);
                        AppManager.getAppManager().currentActivity().onBackPressed();
                        break;
                }
            }
        });
    }

    public void selectCityPopup() {
        if(niceDialog==null){
            niceDialog = NiceDialog.init();
        }
        niceDialog.setLayoutId(R.layout.common_popup_string)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                        SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                        pickerView.setTitle("您的城市");
                        pickerView.setStringList(cityStrList,null);
                        pickerView.setShowStringPicker(iView.getCityView().getRightText(),"");
                        holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                colseDialog();
                            }
                        });
                        pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                            @Override
                            public void onResultPicker(Object obj) {
                                selectCity = cityList.get(cityStrList.indexOf(obj));
                                iView.getCityView().setRightText(String.valueOf(obj));
                                colseDialog();
                                if (judgeCondition(false)){
                                    iView.getSubmitView().setEnabled(true);
                                }
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(fragmentManager);
    }

    public void selectGradePopup() {
        if(niceDialog==null){
            niceDialog = NiceDialog.init();
        }
        niceDialog.setLayoutId(R.layout.common_popup_grade)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                        SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                        pickerView.setTitle("孩子年级");
                        pickerView.setStringList(gradeStrList,gradeStrMap);
                        if (StringUtils.textIsEmpty(iView.getGradeView().getRightText())){
                            pickerView.setShowStringPicker("","");
                        }else {
                            String[] grade = iView.getGradeView().getRightText().split(" ");
                            pickerView.setShowStringPicker(grade[0],grade[1]);
                        }
                        holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                colseDialog();
                            }
                        });
                        pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                            @Override
                            public void onResultPicker(Object obj) {
                                iView.getGradeView().setRightText(String.valueOf(obj).replace("&str&"," "));
                                colseDialog();
                                if (judgeCondition(false)){
                                    iView.getSubmitView().setEnabled(true);
                                }
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(fragmentManager);
    }

    public void selectSubjectPopup() {
        if(niceDialog==null){
            niceDialog = NiceDialog.init();
        }
        niceDialog.setLayoutId(R.layout.common_popup_string)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                        SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                        pickerView.setTitle("意向科目");
                        pickerView.setStringList(subjectStrList,null);
                        pickerView.setShowStringPicker(iView.getSubjectView().getRightText(),"");
                        holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                colseDialog();
                            }
                        });
                        pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener() {

                            @Override
                            public void onResultPicker(Object obj) {
                                selectSubject = subjectList.get(subjectStrList.indexOf(obj));
                                iView.getSubjectView().setRightText(String.valueOf(obj));
                                colseDialog();
                                if (judgeCondition(false)){
                                    iView.getSubmitView().setEnabled(true);
                                }
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(fragmentManager);
    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    public  boolean judgeCondition(boolean bool){
        if (selectCity==null) {
            if (bool){
                nofifyShowMesg("请选择城市...");
            }
            return false;
        }

        if (StringUtils.textIsEmpty(iView.getGradeView().getRightText())) {
            if (bool){
                nofifyShowMesg("请选孩子年级...");
            }
            return false;
        }

        if (selectSubject==null) {
            if (bool){
                nofifyShowMesg("请选择意向科目...");
            }
            return false;
        }
        return true;
    }

    /****
     * 显示提示信息
     * @param mesg
     */
    private void nofifyShowMesg(String mesg){
        ConfirmDialog.newInstance("", mesg, "", "确定").setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(fragmentManager);
    }

    public String cityCode() {
        return  selectCity.cityCode;
    }

    public int getSubjectId() {
        return selectSubject.getSubjectId();
    }

    public interface IApplyAuditionView {
        CommonCrosswiseBar getCityView();
        CommonCrosswiseBar getGradeView();
        CommonCrosswiseBar getSubjectView();
        Button getSubmitView();
    }

}
