package com.shengzhe.disan.xuetangteacher.mvp.model;

import android.content.Context;

import com.main.disanxuelib.bean.BasePageBean;
import com.main.disanxuelib.bean.GradeParentBean;
import com.main.disanxuelib.bean.Message;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.bean.VersionBean;
import com.main.disanxuelib.util.ToastUtil;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.bean.CampusBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukui on 2017/11/27.
 * <p>
 * 公共业务处理
 */

public class CommonModelImpl extends BaseModelImpl {

    public CommonModelImpl(Context context, MVPRequestListener listener) {
        super(context, listener);
    }

    /****
     * 发送验证码请求
     * @param mPhone
     */
    public void getSendVerifyLogin(String mPhone) {
        final Map<String, Object> map = new HashMap<>();
        map.put("mobile", mPhone);
        getHttpService().getMethodCode(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String methodCode) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SendVerifyLogin, methodCode);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SendVerifyLogin, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SendVerifyLogin, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 获取授课阶段
     */
    public void getCommonGradeList() {
        getHttpService().gradeParent()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<GradeParentBean>>(getContext(), true) {
                    @Override
                    protected void onDone(List<GradeParentBean> mBeanList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_ConnomGrade, mBeanList);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_ConnomGrade, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_ConnomGrade, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 获取开放城市
     */
    public void getOpenCityList() {
        getHttpService().findCityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_OpenCity, str);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_OpenCity, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_OpenCity, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 获取城市下面的校区
     * @param cityCode
     */
    public void getCampusByCity(String cityCode) {
        Map<String, Object> map = getParameterMap();
        map.put("city", cityCode);
        getHttpService().findCampusByCity(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<CampusBean>>(getContext(), false) {
                    @Override
                    protected void onDone(List<CampusBean> campusBeanList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CampusByCity, campusBeanList);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CampusByCity, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CampusByCity, ex.getMessage());
                        }
                    }
                });

    }

    /*****
     * 获取首页科目集合
     */
    public void getCommonSubject() {
        getHttpService().subject()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<Subject>>(getContext(), true) {
                    @Override
                    protected void onDone(List<Subject> subjectList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CommonSubject, subjectList);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CommonSubject, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CommonSubject, ex.getMessage());
                        }
                    }
                });

    }

    /*****
     * 消息列表
     * @param pageNum
     */
    public void getCommonMessage(int pageNum) {
        Map<String, Object> map = getParameterMap();
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        getHttpService().myMessage(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<Message>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<Message> myMessage) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CommonMessage, myMessage);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CommonMessage, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CommonMessage, ex.getMessage());
                        }
                    }
                });

    }

    /******
     * 考情状态
     */
    private void getSigninStatus() {
        getHttpService().userSetUpStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String messages) {
                        try {
                            getListener().onSuccess(IntegerUtil.WEB_API_SigninStatus, new JSONObject(messages).optInt("isEnableSign")==1);
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SigninStatus, false);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SigninStatus, ex.getMessage());
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
        getHttpService().userSetUp(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        ToastUtil.showToast(str);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showToast(ex.getMessage());
                    }
                });
    }

    /**
     * 检查版本更新，保存更新的数据
     */
    public void getAppVersion() {
        getHttpService().appVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<VersionBean>(getContext(), true) {
                    @Override
                    protected void onDone(VersionBean mVersionBean) {
                        getListener().onSuccess(IntegerUtil.WEB_API_AppVersion, mVersionBean);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_AppVersion, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_AppVersion, ex.getMessage());
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
        getHttpService().updateVersion(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


}
