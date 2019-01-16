package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.text.TextUtils;

import com.main.disanxuelib.util.CityDaoUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.Personal;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

/**
 * Created by Administrator on 2018/4/17.
 */

public class UserMessageView extends BaseView {

    public UserMessageView(Context context) {
        super(context);
    }

    private IUserMessageView iView;
    public void setIUserMessageView(IUserMessageView view){
        this.iView = view;
    }

    public void setDatas(Personal personal) {
        if (!TextUtils.isEmpty(personal.getPhotoUrl())) {
            ImageUtil.setItemRoundImageViewOnlyDisplay(iView.getHeadView().getRightImage(), personal.getPhotoUrl());
        }
        //请选择性别
        if(personal.getSex()!=2){
            iView.getStudentSexView().setRightText(StringUtils.getSex(personal.getSex()));
        }
        if (!TextUtils.isEmpty(personal.getSchool())) {
            iView.getStudentSchoolView().setRightText(personal.getSchool());
        }
        if (!TextUtils.isEmpty(personal.getGradeName())) {
            iView.getStudentClassView().setRightText(personal.getGradeName());
        }
        if (!TextUtils.isEmpty(personal.getNickName())) {
            iView.getUserNiceNameView().setRightText(personal.getNickName());
        }
        if (!TextUtils.isEmpty(personal.getStudenName())) {
            iView.getStudentNameView().setRightText(personal.getStudenName());
        }
        if (!TextUtils.isEmpty(personal.getCity())&&!TextUtils.isEmpty(personal.getArea())) {
            if(CityDaoUtil.getCityById(personal.getCity())!=null && !TextUtils.isEmpty(CityDaoUtil.getCityById(personal.getCity()).getArea_name())){
                if(!(CityDaoUtil.getCityById(personal.getArea())==null)){
                    iView.getAddressView().setRightText(CityDaoUtil.getCityById(personal.getCity()).getArea_name()+" "+CityDaoUtil.getCityById(personal.getArea()).getArea_name()+" "+personal.getAddress());
                }else {
                    iView.getAddressView().setRightText(CityDaoUtil.getCityById(personal.getCity()).getArea_name()+""+personal.getAddress());
                }
            }
        }
    }

    public interface IUserMessageView{
        CommonCrosswiseBar getStudentSexView();
        CommonCrosswiseBar getAddressView();
        CommonCrosswiseBar getHeadView();
        CommonCrosswiseBar getUserNiceNameView();
        CommonCrosswiseBar getStudentNameView();
        CommonCrosswiseBar getStudentSchoolView();
        CommonCrosswiseBar getStudentClassView();
    }

}
