package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.CourseSubject;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.adapter.CourseExpandableAdapter;
import com.shengzhe.disan.xuetangparent.fragment.ScreenConditionFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.HomeSubjectActivity;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.OfflineClassFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.OfflineOneOnOneFragment;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Administrator on 2018/4/25.
 */

public class ScreenConditionView extends BaseView implements CourseExpandableAdapter.SelectTagListener{
    private String tag = "";
    private String from = "";
    private List<CourseType> typeList = new ArrayList<>();
    private Map<Integer, CourseType> selectMap = new HashMap<>();
    private CourseExpandableAdapter courseExpandableAdapter;
    private ScreenConditionFragment.SelectListener selectListener;

    public ScreenConditionView(Context context) {
        super(context);
    }

    private IScreenConditionView iView;
    public void setIScreenConditionView(IScreenConditionView iView){
        this.iView = iView;
    }

    @Override
    public void tagOnClickListener(View v, CourseType type, int fatherId) {
        if (tag.equals("order")) {
            //分级
            this.selectMap.put(fatherId, type);
            courseExpandableAdapter.notifyDataSetChanged();
        } else {
            //无下标
            this.selectMap.clear();
            this.selectMap.put(fatherId, type);
            selectListener.tagOnClickListener(v, tag, type, fatherId);
            courseExpandableAdapter.notifyDataSetChanged();
        }
    }

    public void initDatas(String from,String tag ,ScreenConditionFragment.SelectListener listener) {
        typeList.clear();
        this.selectListener = listener;
        this.tag = tag;
        this.from = from;
        iView.getLeftBtnView().setVisibility(View.GONE);
        iView.getRightBtnView().setVisibility(View.GONE);
        switch (tag) {
            case StringUtils.select_order:
                iView.getRightBtnView().setVisibility(View.VISIBLE);
                iView.getRightBtnView().setText("确定");
                iView.getLeftBtnView().setVisibility(View.VISIBLE);
                iView.getLeftBtnView().setText("取消");
                typeList.addAll(initOrderData());
                setExpandableAdapter();
                break;
            case StringUtils.select_grade:
                //年级
                typeList.addAll(SharedPreferencesManager.getGrade());
                if (typeList == null || typeList.isEmpty()) {
                    typeList = new ArrayList<>();
                }
                setExpandableAdapter();
                break;

            case StringUtils.select_course:
                //科目
                initCourseData();
                setRecyclerAdapter(4);
                break;

            case StringUtils.select_subject:
                //上课方式
                typeList.addAll(ContentUtil.getTeacherMethod());
                setRecyclerAdapter(1);
                break;

            case StringUtils.select_clazz:
                //课程类型
                typeList.addAll(ContentUtil.selectClazz());
                setRecyclerAdapter(1);
                break;

            case StringUtils.select_courseStatus:
                //开课状态
                typeList.addAll(ContentUtil.selectStatus());
                setRecyclerAdapter(1);
                break;

            case StringUtils.select_payStatus:
                //支付状态
                typeList.addAll(ContentUtil.selectPAyStatus());
                setRecyclerAdapter(1);
                break;
            case StringUtils.select_live:
                //直播方式
                typeList.addAll(ContentUtil.selectLive());
                setRecyclerAdapter(1);
                break;
        }
    }


    private List<CourseType> initOrderData() {
        List<CourseType> courseTypeList = ContentUtil.getOrder();
        List<CourseSubject> childList = new ArrayList<>();
        childList.clear();
        childList = courseTypeList.get(3).childList;
        if (!TextUtils.isEmpty(from) && (from.equals(OfflineOneOnOneFragment.class.getName()) || from.equals(HomeSubjectActivity.class.getName()))) {
            List<CourseSubject> areaList = SharedPreferencesManager.getArea();
            childList.addAll(areaList==null?new ArrayList<CourseSubject>():areaList);
            return courseTypeList;
        } else {
            courseTypeList.remove(courseTypeList.size() - 1);
            if(!TextUtils.isEmpty(from)&&(from.equals(OfflineClassFragment.class.getName()))){
                CourseType courseType = new CourseType();
                courseType.id = 0;
                courseType.name = "不限";
                courseType.childList.clear();
                courseType.childList.addAll(ContentUtil.getCourseNumber());
                courseTypeList.add(courseType);
            }
            return courseTypeList;
        }
    }

    private void initCourseData() {
        typeList = new ArrayList<>();
        CourseType subject = new CourseType();
        subject.id = -1;
        subject.name = "不限";
        typeList.add(subject);
        List<Subject> listSubject = SharedPreferencesManager.getSubject();
        if (listSubject == null)
            return;
        for (Subject item : listSubject) {
            CourseType type = new CourseType();
            type.id = item.getSubjectId();
            type.name = item.getSubjectName();
            typeList.add(type);
        }
    }

    /**
     * 打开全部
     */
    private void openAllGroup() {
        //默认展开
        for (int i = 0; i < typeList.size(); i++) {
            iView.getExpandableListView().expandGroup(i);
        }
    }

    /****
     * 扩展适配器设置
     */
    private void setExpandableAdapter() {
        iView.getExpandableListView().setVisibility(View.VISIBLE);
        iView.getRecyclerView().setVisibility(View.GONE);
        courseExpandableAdapter = new CourseExpandableAdapter(mContext, typeList, tag);
        courseExpandableAdapter.selectMap = selectMap;
        iView.getExpandableListView().setAdapter(courseExpandableAdapter);
        //group不可点击
        iView.getExpandableListView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        //打开全部
        openAllGroup();
        courseExpandableAdapter.setTagListener(this);
        if (typeList.size() > 3 && typeList.get(typeList.size() - 1).childList.size() > 3) {
            iView.getSelectView().setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3 * SystemInfoUtil.getScreenHeight() / 5));
        }
    }

    /****
     * 单级适配器
     */
    private void setRecyclerAdapter(int rowNum) {
        iView.getExpandableListView().setVisibility(View.GONE);
        iView.getRecyclerView().setVisibility(View.VISIBLE);
        iView.getRecyclerView().setLayoutManager(UiUtils.getGridLayoutManager(rowNum));
        if (rowNum == 1) {
            iView.getRecyclerView().setAdapter(new SimpleAdapter<CourseType>(mContext, typeList, R.layout.item_common_text) {
                @Override
                protected void onBindViewHolder(TrdViewHolder holder, final CourseType data) {
                    boolean isChecked = selectMap.containsKey(data.id) || (selectMap.isEmpty() && typeList.indexOf(data) == 0);
                    holder.setText(R.id.rb_item_text, data.name)
                            .setTextColor(R.id.rb_item_text, isChecked ? UiUtils.getColor(R.color.color_ffae12) : UiUtils.getColor(R.color.color_666666))
                            .setVisible(R.id.rb_item_line, typeList.indexOf(data) != typeList.size())
                            .setOnItemListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectMap.clear();
                                    selectMap.put(data.id, data);
                                    notifyDataSetChanged();
                                    selectListener.tagOnClickListener(v, tag, data, data.id);
                                }
                            });
                }
            });
            if (typeList.size() > 10) {
                iView.getSelectView().setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3 * SystemInfoUtil.getScreenHeight() / 5));
            }
        } else {
            iView.getRecyclerView().setAdapter(new SimpleAdapter<CourseType>(mContext, typeList, R.layout.item_select_tag) {
                @Override
                protected void onBindViewHolder(TrdViewHolder holder, final CourseType data) {
                    boolean isChecked = selectMap.containsKey(data.id) || (selectMap.isEmpty() && typeList.indexOf(data) == 0);
                    holder.setText(R.id.history_text, data.name);
                    RadioButton button = holder.getView(R.id.history_text);
                    button.setText(data.name);
                    button.setChecked(isChecked);
                    holder.setOnItemListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectMap.clear();
                            selectMap.put(data.id, data);
                            notifyDataSetChanged();
                            selectListener.tagOnClickListener(v, tag, data, data.id);
                        }
                    });
                }
            });
            if (typeList.size() / rowNum > 5) {
                iView.getSelectView().setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3 * SystemInfoUtil.getScreenHeight() / 5));
            }
        }
    }

    public Map<Integer,CourseType> getSelectMapDatas() {
        return selectMap;
    }

    public void setResultDatas(String str) {
        typeList.clear();
        SharedPreferencesManager.saveArea(str);
        typeList.addAll(initOrderData());
        courseExpandableAdapter.notifyDataSetChanged();
    }

    public void setResultGradeDatas(String str) {
        SharedPreferencesManager.saveGrade(str);
        List<CourseType> gradeList = SharedPreferencesManager.getGrade();
        if (tag.equals(StringUtils.select_grade) && typeList.size() != gradeList.size()) {
            typeList.clear();
            typeList.addAll(gradeList);
            courseExpandableAdapter.notifyDataSetChanged();
            openAllGroup();
            return;
        }
        typeList.clear();
        typeList.addAll(gradeList);
    }


    public interface IScreenConditionView{
        View getSelectView();
        RecyclerView getRecyclerView();
        ExpandableListView getExpandableListView();
        TextView getLeftBtnView();
        TextView getRightBtnView();
    }

}
