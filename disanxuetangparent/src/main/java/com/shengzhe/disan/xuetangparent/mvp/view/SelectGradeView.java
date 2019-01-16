package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.util.AppManager;
import com.shengzhe.disan.xuetangparent.adapter.CourseExpandableAdapter;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 年级选择 on 2018/4/25.
 */

public class SelectGradeView extends BaseView {
    private CourseExpandableAdapter courseExpandableAdapter;
    private List<CourseType> typeList = new ArrayList<>();

    public SelectGradeView(Context context) {
        super(context);
    }

    private ISelectGradeView iView;
    public void setISelectGradeView(ISelectGradeView iView){
        this.iView = iView;
    }

    public void initDatas(int fatherId, CourseType type, CourseExpandableAdapter.SelectTagListener listener) {
        typeList.clear();
        typeList = SharedPreferencesManager.getGrade();
        if(typeList==null || typeList.isEmpty()){
            typeList = new ArrayList<>();
        }else{
            typeList.remove(0);
        }
        courseExpandableAdapter = new CourseExpandableAdapter(mContext, typeList, StringUtils.select_grade);
        courseExpandableAdapter.selectMap.put(fatherId,type);
        iView.getCourseClazzView().setAdapter(courseExpandableAdapter);
        //group不可点击
        iView.getCourseClazzView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        courseExpandableAdapter.setTagListener(listener);
        openAllGroup();
    }

    /**
     * 打开全部
     */
    private void openAllGroup() {
        if(typeList==null || typeList.isEmpty()){
           return;
        }
        //默认展开
        for (int i = 0; i < typeList.size(); i++) {
            iView.getCourseClazzView().expandGroup(i);
        }
    }

    public void showEvbus() {
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11012);
        bundle.putParcelable(StringUtils.EVENT_DATA,type);
        bundle.putInt(StringUtils.EVENT_DATA2,fatherId);
        EventBus.getDefault().post(bundle);
        AppManager.getAppManager().currentActivity().onBackPressed();
    }

    public void setResultDatas(String str) {
        SharedPreferencesManager.saveGrade(str);
        List<CourseType> gradeList = SharedPreferencesManager.getGrade();
        if(typeList.isEmpty()&&typeList.size()!=gradeList.size()){
            typeList.clear();
            typeList.addAll(gradeList);
            typeList.remove(0);
            courseExpandableAdapter.notifyDataSetChanged();
            openAllGroup();
            return;
        }
    }

    private CourseType type;
    private int fatherId;
    public void setGradePersonalData(CourseType type, int fatherId) {
        this.type = type;
        this.fatherId = fatherId;
    }

    public interface ISelectGradeView{
        ExpandableListView getCourseClazzView();
    }

}
