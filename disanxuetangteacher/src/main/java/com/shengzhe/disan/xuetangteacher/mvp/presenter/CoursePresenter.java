package com.shengzhe.disan.xuetangteacher.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.RadioButton;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.CityBean;
import com.main.disanxuelib.bean.GradeParentBean;
import com.main.disanxuelib.util.ToastUtil;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangteacher.mvp.model.CourseModelImpl;
import com.shengzhe.disan.xuetangteacher.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangteacher.mvp.view.ClassCreateOneView;
import com.shengzhe.disan.xuetangteacher.mvp.view.IOpenCityView;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 课表业务处理 on 2017/11/27.
 */
public class CoursePresenter extends BasePresenter implements MVPRequestListener {
    private CommonModelImpl commonModel;
    private CourseModelImpl courseModel;
    private String[] beanArray;
    private List<GradeParentBean> beanList;

    public CoursePresenter(Context context) {
        super(context);
        if (commonModel == null)
            commonModel = new CommonModelImpl(context, this);
    }

    public void getGradeParent() {
        beanList = SharedPreferencesManager.getGradePhase();
        if(beanList!=null&&!beanList.isEmpty()){
            beanArray =new String[beanList.size()];
            for(int i=0;i<beanList.size();i++){
                beanArray[i]=beanList.get(i).getGradeName();
            }
        }
        commonModel.getCommonGradeList();
    }

    @Override
    public void onSuccess(int tager, Object objects) {
        switch (tager){
            case IntegerUtil.WEB_API_ConnomGrade:
                if (objects==null)
                    return;
                beanList = (List<GradeParentBean>)objects;
                SharedPreferencesManager.saveGradePhase(beanList);
                if(beanList!=null&&!beanList.isEmpty()){
                    beanArray = new String[beanList.size()];
                    for(int i=0;i<beanList.size();i++){
                        beanArray[i]=beanList.get(i).getGradeName();
                    }
                }
                break;

        }
    }

    @Override
    public void onFailed(int tager, String mesg) {
        ToastUtil.showToast(mesg);
        switch (tager){
            case IntegerUtil.WEB_API_ConnomGrade:

                break;

        }
    }

    public String[] getBeanArray() {
        return beanArray;
    }

    public List<GradeParentBean> getBeanList() {
        return beanList;
    }
}
