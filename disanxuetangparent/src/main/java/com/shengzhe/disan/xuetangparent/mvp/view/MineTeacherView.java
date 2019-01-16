package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 我的老师 on 2018/4/20.
 */

public class MineTeacherView extends BaseView {
    private List<TeacherInformation> oneOneList = new ArrayList<>();
    private SimpleAdapter adapter;

    public MineTeacherView(Context context) {
        super(context);
    }

    private IMineTeacherView iView;
    public void setIMineTeacherView(IMineTeacherView iView){
        this.iView = iView;
    }

    public void initDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        adapter = new SimpleAdapter<TeacherInformation>(mContext, oneOneList, R.layout.item_oneone_teacher) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final TeacherInformation data) {
                holder.setText(R.id.iv_oneone_name,data.getTeacherName())
                        .setText(R.id.iv_oneone_message, StringUtils.getSex(data.getSex())+" | "+data.getGradeName()+" "+data.getSubjectName()+" | "+data.getTeachingAge()+"年教龄")
                        .setText(R.id.iv_oneone_priprice,"¥"+ ArithUtils.round(data.getDiscountPrice()))
                        .setText(R.id.tv_oneone_address,data.getCityName()+"-"+data.getTeacherArea())
                        .setText(R.id.iv_oneone_latelycourse,data.getMaxCourseName())
                        .setVisible(R.id.iv_quality_certification,data.getIpmpStatus()!=2)
                        .setVisible(R.id.iv_realname_certification,data.getCardApprStatus()!=0)
                        .setVisible(R.id.iv_teacher_certification,data.getQtsStatus()!=2)
                        .setVisible(R.id.iv_education_certification,data.getQuaStatus()!=2)
                        .setVisible(R.id.v_oneone_line,oneOneList.indexOf(data)!=oneOneList.size()-1)
                        .setVisible(R.id.iv_oneone_price,false)
                        .setVisible(R.id.tv_oneone_isplant,data.getIdentity()>0)
                        .setVisible(R.id.tv_original_price,false)
                        .setVisible(R.id.iv_oneone_priprice,false)
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                //intent.setClass(mContext, TeacherNewPagerActivity.class);
                                intent.setClass(mContext, OfflineTeacherActivity.class);
                                intent.putExtra(StringUtils.TEACHER_ID,data.getTeacherId());
                                mContext.startActivity(intent);
                            }
                        });
                if(data.getCourseType()==1){
                    ImageUtil.setCompoundDrawable(holder.<TextView>getView(R.id.iv_oneone_latelycourse),16,R.mipmap.one_lines, Gravity.LEFT,0);
                }else if(data.getCourseType()==2){
                    ImageUtil.setCompoundDrawable(holder.<TextView>getView(R.id.iv_oneone_latelycourse),16,R.mipmap.ic_course_min,Gravity.LEFT,0);
                }

                if(!(TextUtils.isEmpty(data.getCityName()))){
                    holder.setText(R.id.tv_oneone_address,data.getAreaName()==null?"":data.getCityName()+"-"+data.getAreaName());
                }
                ImageUtil.loadCircleImageView(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_oneone_image),R.mipmap.teacher);
            }
        };
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    public void setResultdatas(List<TeacherInformation> dataList) {
        oneOneList.clear();
        oneOneList.addAll(dataList);
        if (oneOneList.isEmpty()){
            iView.getRefreshCommonView().setIsEmpty(true);
        }else{
            iView.getRefreshCommonView().setIsEmpty(false);
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }


    public interface IMineTeacherView{
        RefreshCommonView getRefreshCommonView();
    }

}
