package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.LiveBean;
import com.shengzhe.disan.xuetangparent.fragment.ScreenConditionFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.LiveCourseActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/26.
 */

public class OnLiveView extends BaseView implements ScreenConditionFragment.SelectListener{
    private List<Fragment> popupFragment = new ArrayList<>();//下拉view 列表
    private List<LiveBean> courseList = new ArrayList<>();
    private SimpleAdapter adapter;
    private Map<String, Object> param = new HashMap<>();

    public OnLiveView(Context context) {
        super(context);
    }

    private IOnLiveView iView;
    public void setIOnLiveView(IOnLiveView iView){
        this.iView = iView;
    }

    public void initDatas(DropDownMenu.DropMenuFragmentManage fragmentManage, RefreshCommonView.RefreshLoadMoreListener listener) {
        popupFragment.clear();

        for (int i = 0; i < ContentUtil.selectZXKCXXYDY.length; i++) {
            if (i == 0) {
                ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_grade);
                fragment.setSelectListener(this);
                popupFragment.add(fragment);
            } else if (i == 1) {
                ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_course);
                fragment.setSelectListener(this);
                popupFragment.add(fragment);

            } else if (i == 2) {
                ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_live);
                fragment.setSelectListener(this);
                popupFragment.add(fragment);
            } else {
                ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_order);
                fragment.setSelectListener(this);
                popupFragment.add(fragment);
            }
        }
        //内容区
        iView.getDropDownMenuView().setRequestFragmentManage(fragmentManage);
        float[] weight = {3, 3, 3, 2};
        iView.getDropDownMenuView().setItwmWeight(weight);
        iView.getDropDownMenuView().setDropDownFragment(Arrays.asList(ContentUtil.selectZXKCXXYDY), popupFragment);

        //设置适配器，封装后的适配器只需要实现一个函数
        adapter = new SimpleAdapter<LiveBean>(mContext, courseList, R.layout.item_hot_course) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final LiveBean data) {
                holder.setText(R.id.tv_hot_name, data.getCourseName())
                        .setText(R.id.tv_hot_clazz, data.getGradeName() + " " + data.getSubjectName() + " | " + data.getDirectTypeName())
                        .setText(R.id.tv_hot_teachername, data.getNickName())
                        .setText(R.id.tv_hot_num, data.getSalesVolume() + "人")
                        .setText(R.id.tv_hot_priprice, "¥" + ArithUtils.round(data.getCourseTotalPrice()))
                        .setText(R.id.tv_hot_priprice, data.getCourseTotalPrice() == 0 ? "免费" : "¥" + ArithUtils.round(data.getCourseTotalPrice()))
                        .setVisible(R.id.v_hot_line, courseList.indexOf(data) != courseList.size() - 1)
                        .setVisible(R.id.tv_hot_isplant, data.getIdentity() > 0)
                        .setTextColor(R.id.tv_hot_status, data.getCourseStatus() == 2 ? UiUtils.getColor(R.color.color_ffffff) : data.getCourseStatus() == 1 ? UiUtils.getColor(R.color.color_ffae12) : UiUtils.getColor(R.color.myred))
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //老师详情
                                Intent intent = new Intent(mContext, LiveCourseActivity.class);
                                intent.putExtra(StringUtils.COURSE_ID, data.getCourseId());
                                mContext.startActivity(intent);
                            }
                        });
                ImageUtil.setCompoundDrawable(holder.<TextView>getView(R.id.tv_hot_num), 10, R.mipmap.class_man, Gravity.LEFT, 0);
                if(data.getStartTime()!=0){
                    holder.setText(R.id.tv_hot_time, DateUtil.timeStamp(data.getStartTime(), "yyyy-MM-dd HH:mm"));
                }else {
                    holder.setText(R.id.tv_hot_time, DateUtil.timeStamp(data.getStartTime(), "2017-11-07 15:00"));
                }
                if (data.getCourseStatus() == 2) {
                    holder.setText(R.id.tv_hot_status, "待上课");
                } else if (data.getCourseStatus() == 1) {
                    holder.setText(R.id.tv_hot_status, "直播中");
                } else if (data.getCourseStatus() == 3) {
                     holder .setText(R.id.tv_hot_status,"可回放");
                   // holder.setText(R.id.tv_hot_status, "待上课");
                    holder.setText(R.id.tv_hot_time,"可回放");
                    if(data.getStartTime()!=0){
                        holder.setText(R.id.tv_hot_time, DateUtil.timeStamp(data.getStartTime(), "yyyy-MM-dd HH:mm"));
                    }else {
                        holder.setText(R.id.tv_hot_time, DateUtil.timeStamp(data.getStartTime(), "2017-11-07 15:00"));
                    }

                }else {
                    holder.setText(R.id.tv_hot_status, "待上课");
                }


                //原价和折扣
                if (data.getCourseTotalPrice() == 0) {
                    //原价为0 免费
                    holder.setText(R.id.tv_hot_priprice, "免费");
                    holder.setVisible(R.id.tv_hot_price, false);
                } else if (data.getCourseTotalPrice() > data.getDiscountPrice()) {
                    //原价大于折扣价
                    holder.setText(R.id.tv_hot_priprice, "¥" + ArithUtils.round(data.getDiscountPrice()));
                    holder.setText(R.id.tv_hot_price, "¥" + ArithUtils.round(data.getCourseTotalPrice()));
                    holder.setVisible(R.id.tv_hot_price, true);
                } else {
                    //原价小于等于折扣价
                    holder.setText(R.id.tv_hot_priprice, "¥" + ArithUtils.round(data.getCourseTotalPrice()));
                    holder.setVisible(R.id.tv_hot_price, false);
                }

                holder.<TextView>getView(R.id.tv_hot_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageUtil.loadImageViewLoding(mContext, data.getPictureUrl(), holder.<ImageView>getView(R.id.iv_hot_photo), R.mipmap.default_iamge, R.mipmap.default_iamge);
                ImageUtil.loadCircleImageView(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_hot_teacherimg), R.mipmap.ic_personal_avatar);
            }
        };
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    @Override
    public void tagOnClickListener(View v, String tag, CourseType type, int fatherId) {
        iView.getDropDownMenuView().setTabText(type.name);
        iView.getDropDownMenuView().closeMenu();
        if (tag.equals(StringUtils.select_grade)) {
            //年级
            if (type.id != -1) {
                param.put("gradeId", fatherId);
            } else {
                param.remove("gradeId");
            }
        } else if (tag.equals(StringUtils.select_course)) {
            if (type.id == -1) {
                param.remove("subjectId");
            } else {
                param.put("subjectId", type.id);
            }
        } else if (tag.equals(StringUtils.select_live)) {
            if (type.id == -1) {
                param.remove("directType");
            } else {
                param.put("directType", type.id);
            }
        }
        iView.getRefreshCommonView().notifyData();
    }

    @Override
    public void tagMoreListener(View v, String tag, Map<Integer, CourseType> selectOrderMap) {
        if (tag.equals(StringUtils.select_order)) {
            //排序
            iView.getDropDownMenuView().closeMenu();
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

            //城市code
            CourseType cityCode = selectOrderMap.get(13);
            if (cityCode != null && cityCode.id != -1) {
                param.put("cityCode", cityCode.id);
            } else {
                param.remove("cityCode");
            }
            if (selectOrderMap == null || selectOrderMap.isEmpty())
                return;
            iView.getRefreshCommonView().notifyData();
        }
    }

    public Map<String,Object> getParameter(){
        return param;
    }

    public void clearDatas() {
        courseList.clear();
    }

    public void setResultDatas(BasePageBean<LiveBean> resultDatas) {
        finishLoad();
        courseList.addAll(resultDatas.getList());
        if (courseList.isEmpty()) {
            courseList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        } else {
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(resultDatas.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }

    public interface IOnLiveView{
        DropDownMenu getDropDownMenuView();
        RefreshCommonView getRefreshCommonView();
    }

}
