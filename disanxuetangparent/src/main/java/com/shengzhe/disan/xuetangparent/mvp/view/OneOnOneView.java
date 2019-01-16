package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.LogUtils;
import com.main.disanxuelib.view.DropDownMenu;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.fragment.ScreenConditionFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.OfflineOneOnOneFragment;
import com.shengzhe.disan.xuetangparent.mvp.activity.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/2.
 */

public class OneOnOneView extends BaseView implements ScreenConditionFragment.SelectListener{
    private List<Fragment> popupFragment = new ArrayList<>();//下拉view 列表
    private List<TeacherInformation> oneOneList = new ArrayList<>();
    private Map<String, Object> param = new HashMap<>();
    private SimpleAdapter adapter;

    public OneOnOneView(Context context) {
        super(context);
    }

    private IOneOnOneView iView;
    public void setIOneOnOneView(IOneOnOneView iView){
        this.iView = iView;
    }

    public void initDatas(DropDownMenu.DropMenuFragmentManage fragmentManage, RefreshCommonView.RefreshLoadMoreListener listener) {
        oneOneList.clear();
        for(int i = 0; i< ContentUtil.selectXXYDY.length; i++){
            if(i==0){
                ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_grade);
                fragment.setSelectListener(this);
                popupFragment.add(fragment);
            }else if(i==1){
                ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_course);
                fragment.setSelectListener(this);
                popupFragment.add(fragment);
            }else if(i==2){
                ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_subject);
                fragment.setSelectListener(this);
                popupFragment.add(fragment);
            }else{
                ScreenConditionFragment fragment = ScreenConditionFragment.newInstance(StringUtils.select_order,OfflineOneOnOneFragment.class.getName());
                fragment.setSelectListener(this);
                popupFragment.add(fragment);
            }
        }
        iView.getDropDownMenuView().setRequestFragmentManage(fragmentManage);
        float[] weight = {3,3,3,2};
        iView.getDropDownMenuView().setItwmWeight(weight);
        iView.getDropDownMenuView().setDropDownFragment(Arrays.asList(ContentUtil.selectXXYDY), popupFragment);

        adapter = new SimpleAdapter<TeacherInformation>(mContext, oneOneList, R.layout.item_oneone_teacher) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final TeacherInformation data) {

                holder.setText(R.id.iv_oneone_name,data.getTeacherName())
                        .setText(R.id.iv_oneone_message,StringUtils.getSex(data.getSex()) +" | "+data.getGradeName()+" "+data.getSubjectName()+" | "+data.getTeachingAge()+"年教龄")
                        .setText(R.id.iv_oneone_latelycourse,"新开课："+data.getMaxCourseName())
                        .setVisible(R.id.tv_oneone_isplant,data.getIdentity()>0)
                        .setVisible(R.id.iv_quality_certification,data.getIpmpStatus()==2)
                        .setVisible(R.id.iv_realname_certification,data.getCardApprStatus()!=0)
                        .setVisible(R.id.iv_teacher_certification,data.getQtsStatus()==2)
                        .setVisible(R.id.iv_education_certification,data.getQuaStatus()==2)
                        .setVisible(R.id.v_oneone_line,oneOneList.indexOf(data)!=oneOneList.size()-1)
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Intent intent = new Intent(mContext, TeacherNewPagerActivity.class);
                                Intent intent = new Intent(mContext, OfflineTeacherActivity.class);
                                intent.putExtra(StringUtils.TEACHER_ID,data.getTeacherId());
                                mContext.startActivity(intent);
                            }
                        });
                //原价和折扣
                if(data.getCoursePrice()>data.getDiscountPrice()){
                    //原价大于折扣价
                    holder .setText(R.id.iv_oneone_price,"¥"+ ArithUtils.round(data.getDiscountPrice()));
                    holder .setText(R.id.iv_oneone_priprice,"¥"+ArithUtils.round(data.getCoursePrice()));
                    holder.setVisible(R.id.iv_oneone_priprice,true);
                } else {
                    //原价小于等于折扣价
                    holder .setText(R.id.iv_oneone_price,"¥"+ArithUtils.round(data.getCoursePrice()));
                    holder.setVisible(R.id.iv_oneone_priprice,false);
                }

                if(data.getCourseType()==1){
                    ImageUtil.setCompoundDrawable(holder.<TextView>getView(R.id.iv_oneone_latelycourse),16,R.mipmap.one_lines, Gravity.LEFT,0);
                }else if(data.getCourseType()==2){
                    ImageUtil.setCompoundDrawable(holder.<TextView>getView(R.id.iv_oneone_latelycourse),16,R.mipmap.ic_course_min, Gravity.LEFT,0);
                }
                String  address="";
                if(!(TextUtils.isEmpty(data.getCityName()))){
                    address=data.getCityName();
                }
                if(!(TextUtils.isEmpty(data.getAreaName()))){
                    address=address+"-"+data.getAreaName();
                }
                holder.setText(R.id.tv_oneone_address,address);
                holder.<TextView>getView(R.id.iv_oneone_priprice).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageUtil.loadCircleImageView(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_oneone_image),R.mipmap.teacher);
            }
        };

        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    @Override
    public void tagOnClickListener(View v, String tag, CourseType type, int mFatherId) {
        iView.getDropDownMenuView().setTabText(type.name);
        iView.getDropDownMenuView().closeMenu();
        if(tag.equals(StringUtils.select_grade)){
            //年级
            if(type.id!=-1){
                param.put("gradeId",mFatherId);
            }else{
                param.remove("gradeId");
            }
        }else if(tag.equals(StringUtils.select_subject)){
            //科目
            if(type.id==0){
                param.remove("teacherMethod1");
                param.remove("teacherMethod2");
                param.remove("teacherMethod3");
            }else{
                LogUtils.e("type.id = " +type.id);
                switch (type.id){
                    case 1:
                        param.put("teacherMethod2",1);//是否老师上门 1是 0否
                        break;
                    case 2:
                        param.put("teacherMethod3",1);//是否校区上课 1是 0否
                        break;
                    case 3:
                        param.put("teacherMethod1",1);//是否学生上门 1是 0否
                        break;
                }
            }
        }else if(tag.equals("course")){
            if(type.id==-1){
                param.remove("subjectId");
            }else{
                param.put("subjectId",type.id);
            }
        }
        iView.getRefreshCommonView().notifyData();
    }

    @Override
    public void tagMoreListener(View v, String tag,Map<Integer,CourseType> selectOrderMap) {
        if(tag.equals(StringUtils.select_order)){
            //排序
            iView.getDropDownMenuView().closeMenu();
            //	性别（1：男，0：女）
            CourseType sex =  selectOrderMap.get(10);
            if(sex!=null&&sex.id!=-1){
                param.put("sex",sex.id);
            }else{
                param.remove("sex");
            }
            //	教龄（字典属性propValue值。获取值传”seniority”）
            CourseType seniority =  selectOrderMap.get(11);
            if(seniority!=null&&seniority.id!=-1){
                param.put("seniority",seniority.id);
            }else{
                param.remove("seniority");
            }
            //教师身份 0自由老师，1平台教师
            CourseType identity =  selectOrderMap.get(12);
            if(identity!=null&&identity.id!=-1){
                param.put("identity",identity.id);
            }else{
                param.remove("identity");
            }

            //城市code
            CourseType cityCode =  selectOrderMap.get(13);
            if(cityCode!=null&&cityCode.id!=-1){
                param.put("areaCode",cityCode.id);
            }else{
                param.remove("areaCode");
            }
            if(selectOrderMap==null|| selectOrderMap.isEmpty())
                return;
            param.put("cityCode",SharedPreferencesManager.getCityId());
            iView.getRefreshCommonView().notifyData();
        }

    }

    public void setResultDatas(BasePageBean<TeacherInformation> teacher) {
        finishLoad();
        oneOneList.addAll(teacher.getList());
        if (oneOneList.isEmpty()){
            oneOneList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        }else{
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(teacher.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }

    public void clearDatas() {
        oneOneList.clear();
    }

    public interface IOneOnOneView{
        DropDownMenu getDropDownMenuView();
        RefreshCommonView getRefreshCommonView();
    }
}
