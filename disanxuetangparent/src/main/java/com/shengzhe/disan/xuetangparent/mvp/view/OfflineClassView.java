package com.shengzhe.disan.xuetangparent.mvp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.SquadRecommendInformation;
import com.shengzhe.disan.xuetangparent.fragment.ScreenConditionFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.CourseDetailActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.OfflineClassFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/2.
 */

public class OfflineClassView extends BaseView implements ScreenConditionFragment.SelectListener{
    private List<Fragment> popupFragment = new ArrayList<>();//下拉view 列表
    private List<SquadRecommendInformation> courseList = new ArrayList<>();
    private SimpleAdapter adapter;
    private Map<String, Object> param = new HashMap<>();
    private int teacherId;

    public OfflineClassView(Context context, int teacherId) {
        super(context);
        this.teacherId = teacherId;
    }

    private IOfflineClassView iView;
    public void setIOfflineClassView(IOfflineClassView iView){
        this.iView = iView;
    }

    @Override
    public void tagOnClickListener(View v, String tag, CourseType type, int mFatherId) {
        iView.getDropDownMenuView().setTabText(type.name);
        iView.getDropDownMenuView().closeMenu();
        if (tag.equals(StringUtils.select_grade)) {
            //年级
            if (type.id != -1) {
                param.put("gradeId", mFatherId);
            } else {
                param.remove("gradeId");
            }
        } else if (tag.equals("course")) {
            if (type.id == -1) {
                param.remove("subjectId");
            } else {
                param.put("subjectId", type.id);
            }
        }
        iView.getRefreshCommonView().notifyData();
    }

    @Override
    public void tagMoreListener(View v, String tag, Map<Integer, CourseType> selectOrderMap) {
        if (tag.equals(StringUtils.select_order)) {
            //排序
            iView.getDropDownMenuView().closeMenu();
            if (selectOrderMap.isEmpty())
                return;
            //	性别（1：男，0：女）
            CourseType sex = selectOrderMap.get(10);
            if (sex != null && sex.id != -1) {
                param.put("sex", sex.id);
            } else {
                param.remove("sex");
            }
            //	教龄（字典属性propValue值。获取值传”seniority”）
            CourseType seniority = selectOrderMap.get(11);
            if (seniority != null && seniority.id != -1) {
                param.put("seniority", seniority.id);
            } else {
                param.remove("seniority");
            }
            //教师身份 0自由老师，1平台教师
            CourseType identity = selectOrderMap.get(12);
            if (identity != null && identity.id != -1) {
                param.put("identity", identity.id);
            } else {
                param.remove("identity");
            }

            iView.getRefreshCommonView().notifyData();
        }

    }

    public void initDatas(final Activity activity, DropDownMenu.DropMenuFragmentManage fragmentManage, RefreshCommonView.RefreshLoadMoreListener listener) {
        if (teacherId == -1) {
            for (int i = 0; i < ContentUtil.selectXXYDY2.length; i++) {
                if (i == 0) {//年级
                    ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_grade);
                    fragment.setSelectListener(this);
                    popupFragment.add(fragment);
                } else if (i == 1) {//科目
                    ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_course);
                    fragment.setSelectListener(this);
                    popupFragment.add(fragment);
                } else {//筛选
                    ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_order, OfflineClassFragment.class.getName());
                    fragment.setSelectListener(this);
                    popupFragment.add(fragment);
                }
            }

            iView.getDropDownMenuView().setRequestFragmentManage(fragmentManage);
            float[] weight = {1, 1, 1};
            iView.getDropDownMenuView().setItwmWeight(weight);
            iView.getDropDownMenuView().setDropDownFragment(Arrays.asList(ContentUtil.selectXXYDY2), popupFragment);
        }else {
            iView.getDropDownMenuView().setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,0,0);
            iView.getRefreshCommonView().setLayoutParams(layoutParams);
        }
        courseList.clear();
        adapter = new SimpleAdapter<SquadRecommendInformation>(mContext, courseList, R.layout.item_classcourse) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final SquadRecommendInformation data) {
                holder.setText(R.id.tv_class_name, data.courseName)
                        .setText(R.id.iv_teacher_mesg, "")
                        .setText(R.id.tv_class_time, data.singleClassTime + "小时/次，共" + data.classTime + "次")
                        .setText(R.id.tv_class_number, StringUtils.textFormatHtml("<font color='#1d97ea'>" + data.salesVolume + "</font>" + "/" + data.maxUser + "人"))
                        .setText(R.id.tv_class_details, data.subjectName + " " + data.gradeName)
                        .setText(R.id.iv_teacher_mesg, data.nickName)
                        .setVisible(R.id.view_line, courseList.indexOf(data) == 0)
                        //.setVisible(R.id.ll_teacher_header, !(activity instanceof TeacherNewPagerActivity))
                        .setVisible(R.id.ll_teacher_header, !(activity instanceof OfflineTeacherActivity))
                        .setOnClickListener(R.id.ll_teacher_header, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //老师主页
                                //Intent intent = new Intent(mContext, TeacherNewPagerActivity.class);
                                Intent intent = new Intent(mContext, OfflineTeacherActivity.class);
                                intent.putExtra(StringUtils.TEACHER_ID, data.teacherId);
                                mContext.startActivity(intent);
                            }
                        })
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CourseDetailActivity.class);
                                intent.putExtra(StringUtils.COURSE_ID, data.courseId);
                                mContext.startActivity(intent);
                            }
                        });
                //原价和折扣
                if(data.discountPrice!=0){
                    //原价大于折扣价
                    holder .setText(R.id.tv_class_price,"¥"+ ArithUtils.round(data.discountPrice));
                    //  holder .setText(R.id.iv_oneone_priprice,"¥"+ArithUtils.round(data.getCoursePrice()));
                    //   .setText(R.id.tv_class_price, "¥" + ArithUtils.round(data.singlePrice))
                    //  holder.setVisible(R.id.iv_oneone_priprice,true);
                } else {
                    //原价小于等于折扣价
                    holder .setText(R.id.tv_class_price,"¥"+ArithUtils.round(data.getCourseTotalPrice()));
                    //    holder.setVisible(R.id.iv_oneone_priprice,false);
                }
                ImageUtil.loadCircleImageView(mContext,data.photoUrl , holder.<ImageView>getView(R.id.iv_teacher_header), R.mipmap.ic_personal_avatar);
                ImageUtil.loadImageViewLoding(mContext,data.pictureUrl, holder.<ImageView>getView(R.id.iv_class_photo), R.mipmap.default_error, R.mipmap.default_error);
            }
        };
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        //iView.getRefreshCommonView().setIsRefresh(!(activity instanceof TeacherNewPagerActivity));
        iView.getRefreshCommonView().setIsRefresh(!(activity instanceof OfflineTeacherActivity));
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    public void clearDatas() {
        courseList.clear();
    }

    public Map<String,Object> getParameter() {
        return param;
    }

    public void setResultDatas(BasePageBean<SquadRecommendInformation> teacher) {
        finishLoad();
        courseList.addAll(teacher.getList());
        if (teacher.getList().isEmpty()) {
            courseList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(teacher.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }


    public interface IOfflineClassView{
        DropDownMenu getDropDownMenuView();
        RefreshCommonView getRefreshCommonView();
    }

}
