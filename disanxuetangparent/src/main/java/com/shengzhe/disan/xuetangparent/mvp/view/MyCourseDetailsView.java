package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.MyRecyclerView;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.OrderCourse;
import com.shengzhe.disan.xuetangparent.bean.MyCourseInfo;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/2.
 */

public class MyCourseDetailsView extends BaseView {
    private FragmentManager fragmentManager;
    private MyCourseInfo myCourse;
    private List<MyCourseInfo.CourseImtemBean> dataList = new ArrayList<>();
    private final int noOpen=21;
    private final int opengIng=22;
    private final int finshClass=80;
    private SimpleAdapter adapter;
    private OrderCourse orderCourse;

    public MyCourseDetailsView(Context context) {
        super(context);
        dataList.clear();
    }

    private IMyCourseDetailsView iView;
    public void setIMyCourseDetailsView(IMyCourseDetailsView iView){
        this.iView = iView;
    }

    public void initDatas(OrderCourse mCourse, FragmentManager mFragmentManager) {
        this.orderCourse = mCourse;
        this.fragmentManager = mFragmentManager;
        iView.getRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SimpleAdapter<MyCourseInfo.CourseImtemBean>(mContext,dataList, R.layout.item_class_plan) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final MyCourseInfo.CourseImtemBean data) {
                holder.setText(R.id.tv_class_videoclass,(dataList.indexOf(data)+1)+"." + DateUtil.weekTimeStamp(data.getStartTime(),"yyyy-MM-dd")+" "+
                        DateUtil.timeStamp(data.getStartTime(),"yyyy-MM-dd HH:mm")+"-"+DateUtil.timeStamp(data.getEndTime(),"HH:mm"));
                if(orderCourse.getCourseType()==1){
                    //1待授课 2已授课 3退款审核 4已完成 5已退款
                    if(data.getCourseStatus()==1||data.getCourseStatus()==3){
                        holder.setText(R.id.tv_class_status,"待授课");
                    }else if(data.getCourseStatus()==2){
                        holder .setText(R.id.tv_class_status,"已授课");
                    }else if(data.getCourseStatus()==4) {
                        holder .setText(R.id.tv_class_status,"已完成");
                    }else if(data.getCourseStatus()==5) {
                        holder .setText(R.id.tv_class_status,"已退款");
                    }
                    holder.setTextColor(R.id.tv_class_status, UiUtils.getColor(R.color.color_999999));
                }else{
                    if(data.getCourseStatus()==1){
                        holder.setText(R.id.tv_class_status,"待上课");
                        holder.setTextColor(R.id.tv_class_status, UiUtils.getColor(R.color.color_ffae12));
                    }else if(data.getCourseStatus()==2){
                        holder .setText(R.id.tv_class_status,"直播中");
                        holder.setTextColor(R.id.tv_class_status,UiUtils.getColor(R.color.color_999999));
                    }else if(data.getCourseStatus()==3) {
                        holder .setText(R.id.tv_class_status,"已结束");
                        holder.setTextColor(R.id.tv_class_status,UiUtils.getColor(R.color.color_0d3662));
                    }else if(data.getCourseStatus()==4){
                        holder .setText(R.id.tv_class_status,"可回放");
                    }
                    ImageUtil.setCompoundDrawable(holder.getView(R.id.tv_class_videoclass), 16, R.mipmap.video_class, Gravity.LEFT,0);
                }

                if(orderCourse.getCourseType()== 2){
                    //直播课
                    holder.setOnItemListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ConfirmDialog dialog = ConfirmDialog.newInstance("", "暂未开通手机观看直播课，请电脑登陆官网www.3rdedu.com" +
                                    "<font color='#ffae12'>“家长个人中心-我的课表”</font>观看", "", "确定");
                            dialog.setMargin(60)
                                    .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                                    .setOutCancel(false)
                                    .show(fragmentManager);
                        }
                    });
                }
            }
        };
        iView.getRecyclerView().setAdapter(adapter);
    }

    //设置所有返回的值
    public void setResultDatas(MyCourseInfo strTeacher){
        this.myCourse = strTeacher;
        iView.getTypeView().setText(StringUtils.textStr(myCourse.getCourseTypeName()));
        if (orderCourse.getSubStatus() == noOpen) {
            iView.getStatusView().setText("未开课");
        } else if (orderCourse.getSubStatus() == opengIng) {
            iView.getStatusView().setText( "开课中");
        } else if (orderCourse.getSubStatus() == finshClass) {
            iView.getStatusView().setText("已完成");
        }
        ImageUtil.loadImageViewLoding(mContext, myCourse.getPhotoUrl(), iView.getImageView(), R.mipmap.default_iamge, R.mipmap.default_iamge);
        iView.getNameView().setText(StringUtils.textStr(myCourse.getCourseName()));
        iView.getMessageView().setText(StringUtils.textStr(myCourse.getTeacherNickName()));
        iView.getCityView().setText(StringUtils.textStr(myCourse.getAddress()));
        iView.getTimeView().setText(DateUtil.timeStamp(myCourse.getFirstTime(), "MM-dd")+"开课");
        iView.getClassView().setText("已上"+myCourse.getOrderNum()+"次，剩余"+((myCourse.getGiveNum()+myCourse.getBuyNum())-myCourse.getOrderNum())+"次");
        if(orderCourse.getCourseType()==1){
            iView.getProblemView().setText(StringUtils.textFormatHtml("课程有问题，想要调课/退课？<font color='#ffae12'>立即联系助教</font>"));
        }
        dataList.addAll(myCourse.getCourseItem());
        adapter.notifyDataSetChanged();
        if(orderCourse.getCourseType()==1){
            iView.getMethodView().setText("· " + myCourse.getTeachingMethodName()==null?"":"· "+myCourse.getTeachingMethodName());
        }else {
            iView.getMethodView().setText("· " + myCourse.getDirectTypeName()==null?"":"· "+myCourse.getDirectTypeName());
        }
    }
    private ConfirmDialog dialog;
    public void courseProblem() {
        String mesg = "您确定要拨打";
        if(myCourse==null || StringUtils.textIsEmpty(myCourse.getAssistantPhone())) {
            mesg += "客服电话\n<font color='#FFAE12'>"+StringUtils.System_Service_Phone+"</font>？";
            myCourse.setAssistantPhone(StringUtils.System_Service_Phone);
        }else{
            mesg += "助教电话\n<font color='#FFAE12'>"+myCourse.getAssistantPhone()+"</font>？";
        }
        if(dialog==null) {
            dialog = ConfirmDialog.newInstance("",mesg, "取消", "立即拨打");
        }
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                .setOutCancel(false)
                .show(fragmentManager);
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){
            @Override
            public void dialogStatus(int id) {
                switch (id){
                    case R.id.tv_dialog_ok:
                        //确定
                        SystemInfoUtil.callDialing(myCourse.getAssistantPhone());
                        break;
                }
            }
        });
    }

    public interface IMyCourseDetailsView {
        TextView getTypeView();
        TextView getMethodView();
        TextView getStatusView();
        ImageView getImageView();
        TextView getNameView();
        TextView getMessageView();
        TextView getCityView();
        TextView getTimeView();
        TextView getClassView();
        TextView getProblemView();
        MyRecyclerView getRecyclerView();
    }

}
