package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SoftKeyboard;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 搜索 on 2018/4/25.
 */

public class HeadSearchView extends BaseView {
    private SimpleAdapter adapter;
    private List<TeacherInformation> oneOneList = new ArrayList<>();

    public HeadSearchView(Context context) {
        super(context);
    }

    private IHeadSearchView iView;
    public void setIHeadSearchView(IHeadSearchView iView){
        this.iView = iView;
    }

    public void initDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        setParameter();
        oneOneList.clear();
        adapter = new SimpleAdapter<TeacherInformation>(mContext, oneOneList, R.layout.item_oneone_teacher) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final TeacherInformation data) {
                holder.setText(R.id.iv_oneone_name, data.getTeacherName())
                        .setVisible(R.id.tv_oneone_isplant,data.getIdentity()>0)
                        .setText(R.id.iv_oneone_message,StringUtils.getSex(data.getSex())+" | "+data.getGradeName()+" "+data.getSubjectName()+" | "+data.getTeachingAge()+"年教龄")
                        .setText(R.id.iv_oneone_price, data.getCoursePrice() / 10000 == 0 ? "免费" : "¥" + ArithUtils.round(data.getCoursePrice()))
                        .setText(R.id.iv_oneone_priprice, "¥" + ArithUtils.round(data.getDiscountPrice()))
                        .setText(R.id.tv_oneone_address, data.getCityName() + "-" + data.getTeacherArea())
                        .setText(R.id.iv_oneone_latelycourse, data.getMaxCourseName())
                        .setVisible(R.id.iv_quality_certification, data.getIpmpStatus() == 2)
                        .setVisible(R.id.iv_realname_certification, data.getCardApprStatus() != 0)
                        .setVisible(R.id.iv_teacher_certification, data.getQtsStatus() == 2)
                        .setVisible(R.id.iv_education_certification, data.getQuaStatus() == 2)
                        .setVisible(R.id.v_oneone_line, oneOneList.indexOf(data) != oneOneList.size() - 1)
                        .setVisible(R.id.iv_oneone_price,false)
                        .setVisible(R.id.iv_oneone_priprice,false)
                        .setVisible(R.id.tv_original_price,false)
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
                if(!(TextUtils.isEmpty(data.getCityName()))){
                    holder.setText(R.id.tv_oneone_address,data.getAreaName()==null?"":data.getCityName()+"-"+data.getAreaName());
                }
                ImageUtil.loadCircleImageView(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_oneone_image), R.mipmap.teacher);
            }
        };
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setIsAutoLoad(false);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
        iView.getRefreshCommonView().setIsEmpty(true);
    }

    /*****
     * 设置偏移量
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void setParameter() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(UiUtils.getDimension(R.dimen.title_bar_height) + SystemInfoUtil.getStatusBarHeight()));
        iView.getHeadView().setPadding(0, iView.getHeadView().getPaddingTop()+SystemInfoUtil.getStatusBarHeight(),0,0);
        iView.getHeadView().setLayoutParams(params);

        //监听软键盘搜索点击
        iView.getHeadSearchView().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (StringUtils.textIsEmpty(iView.getHeadSearchView().getText().toString())) {
                        ToastUtil.showShort("请输入要搜索的关键字");
                        return true;
                    }
                    //关闭键盘
                    SoftKeyboard.hide(iView.getHeadSearchView());
                    //点击键盘【搜索】，进行搜索
                    iView.getRefreshCommonView().notifyData();
                    return true;
                }
                return false;
            }
        });
    }

    public List<TeacherInformation> getDatasList(){
        return oneOneList;
    }

    public String getInputText() {
        return iView.getHeadSearchView().getText().toString().trim();
    }

    public void setResultDatas(BasePageBean<TeacherInformation> teacher) {
        oneOneList.addAll(teacher.getList());
        finishLoad();
        if (oneOneList == null || oneOneList.isEmpty()) {
            oneOneList.clear();
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

    public void seacherDatas() {
        if (StringUtils.textIsEmpty(iView.getHeadSearchView().getText().toString())) {
            ToastUtil.showShort("请输入要搜索的关键字");
            return;
        }
        iView.getRefreshCommonView().notifyData();
        SoftKeyboard.hide(iView.getHeadSearchView());
    }


    public interface IHeadSearchView{
        LinearLayout getHeadView();
        RefreshCommonView getRefreshCommonView();
        EditText getHeadSearchView();
    }

}
