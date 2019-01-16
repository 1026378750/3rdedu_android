package com.shengzhe.disan.xuetangparent.mvp.model;

import android.content.Context;

import com.main.disanxuelib.bean.Message;
import com.main.disanxuelib.bean.VersionBean;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.bean.TeaHomePageBean;
import com.shengzhe.disan.xuetangparent.bean.User;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukui on 2017/11/27.
 * <p>
 * 公共业务处理
 */

public class CommonModelImpl extends BaseModelImpl {

    public CommonModelImpl(Context context, MVPRequestListener listener,String from) {
        super(context, listener,from);
    }

    /****
     * 发送验证码请求
     * @param mPhone
     */
    public void getSendVerifyLogin(String mPhone) {
        final Map<String, Object> map = new HashMap<>();
        map.put("mobile", mPhone);
        getHttpService().getMethodCode(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String methodCode) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SendVerifyLogin, methodCode,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SendVerifyLogin, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SendVerifyLogin, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /*****
     * 登录
     * @param mPhone
     * @param mSecurity
     */
    public void sendLogin(String mPhone,String mSecurity) {
        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mPhone);
        param.put("code", mSecurity);
        param.put("registrationId", JPushInterface.getRegistrationID(getContext()));
        getHttpService().login(ConstantUrl.ApiVerCode2,RequestBodyUtils.setRequestBody(param))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<User>(getContext(), true) {
                    @Override
                    protected void onDone(User user) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SendLogin, user,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SendLogin, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SendLogin, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /****
     * 获取开头城市
     */
    public void getOpenCityList(){
        getHttpService().findCityList(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(),true) {
                    @Override
                    protected void onDone(String addressList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OpenCity, addressList,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_OpenCity, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_OpenCity, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /******
     * 阶段和年级集合
     */
    public void getGradeList() {
        getHttpService().genGradeListAll(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), false) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_GradeList, str,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_GradeList, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_GradeList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*******
     * 获取城市下的区县信息
     * @param cityCode
     */
    public void getCityByCode(String cityCode) {
        Map<String, Object> map = getParameterMap();
        map.put(StringUtils.cityCode, cityCode);
        getHttpService().findAreaCityByCode(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), false) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CityByCode, str,getFrom());
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CityByCode, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CityByCode, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /*****
     * 获取首页科目集合
     */
    public void getCommonSubject() {
        getHttpService().subject(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CommonSubject, str,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CommonSubject, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CommonSubject, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /*****
     * 退出
     */
    public void getCommonLogout() {
        getHttpService().logout(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CommonLogout, str,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CommonLogout, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CommonLogout, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /******
     * 考情状态
     */
    public void getSigninStatus() {
        getHttpService().userSetUpStatus(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String messages) {
                        try {
                            getListener().onSuccess(IntegerUtil.WEB_API_SigninStatus, new JSONObject(messages).optInt("isEnableSign")==1,getFrom());
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SigninStatus, false,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SigninStatus, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /******
     * 考情设置
     * @param isEnableSign
     */
    public void upDateSigninStatus(int isEnableSign) {
        Map<String, Object> map = getParameterMap();
        //个人设置是否开启签到通知 1：是 2：否
        map.put("isEnableSign", isEnableSign);
        getHttpService().userSetUp(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_UpDateSigninStatus, "设置成功",getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_UpDateSigninStatus, "设置成功",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_UpDateSigninStatus, ex.getMessage(),getFrom());
                        }
                    }
                });
    }


    /**
     * 检查版本更新，保存更新的数据
     */
    public void getAppVersion() {
        getHttpService().appVersion(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<VersionBean>(getContext(), true) {
                    @Override
                    protected void onDone(VersionBean mVersionBean) {
                        getListener().onSuccess(IntegerUtil.WEB_API_AppVersion, mVersionBean,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_AppVersion, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_AppVersion, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /******
     * 更新下载次数
     */
    public void updateVersion(long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appVersionId", id);
        getHttpService().updateVersion(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /*****
     * 消息列表
     * @param pageNum
     */
    public void getCommonMessage(int pageNum) {
        Map<String, Object> map = getParameterMap();
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        getHttpService().myMessage(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<Message>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<Message> myMessage) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CommonMessage, myMessage,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CommonMessage, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CommonMessage, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /*****
     * 搜索列表
     * @param search
     * @param cityCode
     * @param pageNum
     */
    public void getSearchList(String search,String cityCode,int pageNum) {
        Map<String, Object> map = getParameterMap();
        map.put("teacherOrCourseName", search);
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        map.put("cityCode", cityCode);
        getHttpService().overallSituationSearch(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<TeacherInformation>>(getContext(),true) {
                    @Override
                    protected void onDone(BasePageBean<TeacherInformation> teacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SearchList, teacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SearchList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SearchList, ex.getMessage(),getFrom());
                        }
                    }
                });

    }

    /******
     * 得到老师课程信息
     * @param teacherId
     */
    public void getTeacherPage(int teacherId) {
        Map<String, Object> map = getParameterMap();
        map.put("teacherId", teacherId);
        getHttpService().teaHomePage(ConstantUrl.ApiVerCode2,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<TeaHomePageBean>(getContext(), true) {
                    @Override
                    protected void onDone(TeaHomePageBean strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_TeacherPage, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_TeacherPage, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_TeacherPage, ex.getMessage(),getFrom());
                        }
                    }
                });

    }


}
