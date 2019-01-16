package com.shengzhe.disan.xuetangteacher.mvp.model;

import android.content.Context;
import android.text.TextUtils;

import com.main.disanxuelib.bean.BasePageBean;
import com.main.disanxuelib.bean.TeachingExperienceBean;
import com.main.disanxuelib.util.FileUtil;
import com.shengzhe.disan.xuetangteacher.bean.WalletDetailsBean;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.AuthenticationCoreBean;
import com.shengzhe.disan.xuetangteacher.bean.MyBalance;
import com.shengzhe.disan.xuetangteacher.bean.MyBankCardBean;
import com.shengzhe.disan.xuetangteacher.bean.PersonalDataBean;
import com.shengzhe.disan.xuetangteacher.bean.PresentRecordBean;
import com.shengzhe.disan.xuetangteacher.bean.RealNameVerify;
import com.shengzhe.disan.xuetangteacher.bean.SearchAssistantMode;
import com.shengzhe.disan.xuetangteacher.bean.StartCourseSetupBean;
import com.shengzhe.disan.xuetangteacher.bean.TeacherInfo;
import com.shengzhe.disan.xuetangteacher.bean.UserAssistantMode;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 用户信息业务处理 on 2018/4/12.
 */

public class UserModelImpl extends BaseModelImpl {

    public UserModelImpl(Context context, MVPRequestListener listener) {
        super(context, listener);
    }

    /**
     * 个人资料页
     */
    private void getMessage() {
        getHttpService().personalData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<PersonalDataBean>(getContext(), true) {
                    @Override
                    protected void onDone(PersonalDataBean personalDatabBeans) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyMessage, personalDatabBeans);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyMessage, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyMessage, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 获取我的消息数量
     */
    public void getMessageNum() {
        getHttpService().myCenter()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), false) {
                    @Override
                    protected void onDone(String messages) {
                        //	我的消息数量，为0表示没有
                        try {
                            getListener().onSuccess(IntegerUtil.WEB_API_MessageNum, new JSONObject(messages).optInt("msgNum"));
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MessageNum, 0);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MessageNum, ex.getMessage());
                        }
                    }
                });
    }

    /****
     * 更新个人数据
     * @param imageUrl
     * @param mNicName
     * @param sexId
     */
    public void upDateUser(String imageUrl, String mNicName, String sexId) {
        HttpService httpService = Http.getHttpService();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        if (TextUtils.isEmpty(imageUrl)) {
            byte[] data = new byte[0];
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
            builder.addFormDataPart("uploadFile", "uploadFile", imageBody);//imgfile 后台接收图片流的参数名
        } else {
            File file = FileUtil.getNewFile(imageUrl);//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
            builder.addFormDataPart("uploadFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        }

        Map<String, Object> map = new HashMap<>();
        map.put("nickName", mNicName);//用户昵称
        map.put("sex", sexId);//用户性别
        builder.addFormDataPart("sign", RequestBodyUtils.setEncrypt(map));
        List<MultipartBody.Part> parts = builder.build().parts();
        httpService.uploadOnePhoto(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String jsonObject) {
                        getListener().onSuccess(IntegerUtil.WEB_API_UpDateUser, jsonObject);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        getListener().onFailed(IntegerUtil.WEB_API_UpDateUser, ex.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getListener().onFailed(IntegerUtil.WEB_API_UpDateUser, e.getMessage());
                    }
                });
    }

    /**
     * 查询老师教学经历
     */
    public void getExperienceList() {
        getHttpService().queryTeachingExperience()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<TeachingExperienceBean>>(getContext(), true) {
                    @Override
                    protected void onDone(List<TeachingExperienceBean> listTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_ExperienceList, listTeacher);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_ExperienceList, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_ExperienceList, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 删除教学经历
     * @param teacherId
     */
    public void deleteTeacherExperience(int teacherId) {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        map.put("teachingExperienceId", teacherId);
        httpService.deleteTeachingExperience(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String string) {
                        getListener().onSuccess(IntegerUtil.WEB_API_DeleteTeacherExperience, string);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_DeleteTeacherExperience, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_DeleteTeacherExperience, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 保存、编辑老师教学经历
     * @param experienceBean
     */
    public void saveTeacherExperience(TeachingExperienceBean experienceBean) {
        Map<String, Object> map = getParameterMap();
        if (experienceBean.getId() != -1) {
            map.put("teachingExperienceId", experienceBean.getId());
        }
        map.put("startTime", experienceBean.getStartTime());
        map.put("endTime", experienceBean.getEndTime());
        map.put("school", experienceBean.getSchool());
        map.put("remark", experienceBean.getRemark());
        getHttpService().saveTeachingExperience(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<TeachingExperienceBean>(getContext(), true) {
                    @Override
                    protected void onDone(TeachingExperienceBean bean) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SaveTeacherExperience, bean);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SaveTeacherExperience, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SaveTeacherExperience, ex.getMessage());
                        }
                    }
                });

    }

    /****
     * 获取认证状态
     */
    public void getAutherStatus() {
        getHttpService().authenticationCore()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<AuthenticationCoreBean>(getContext(), true) {
                    @Override
                    protected void onDone(AuthenticationCoreBean coreBean) {
                        getListener().onSuccess(IntegerUtil.WEB_API_AutherStatus, coreBean);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_AutherStatus, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_AutherStatus, ex.getMessage());
                        }
                    }
                });

    }

    /****
     * 实名认证数据提交
     * @param verify
     * @param name
     * @param cardNo
     */
    public void saveNameAuthentication(RealNameVerify verify, String name, String cardNo) {
        File file;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        byte[] data = new byte[0];
        if (StringUtils.textIsEmpty(verify.getCardFrontUrl()) || verify.getCardFrontUrl().contains("http://")) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
            builder.addFormDataPart("positiveFile", "positiveFile", imageBody);//imgfile 后台接收图片流的参数名
        } else {
            file = new File(verify.getCardFrontUrl());//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
            builder.addFormDataPart("positiveFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        }

        if (StringUtils.textIsEmpty(verify.getCardBackUrl()) || verify.getCardBackUrl().contains("http://")) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
            builder.addFormDataPart("sideFile", "sideFile", imageBody);//imgfile 后台接收图片流的参数名
        } else {
            file = new File(verify.getCardBackUrl());//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
            builder.addFormDataPart("sideFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        }

        if (StringUtils.textIsEmpty(verify.getCardUrl()) || verify.getCardUrl().contains("http://")) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
            builder.addFormDataPart("handFile", "handFile", imageBody);//imgfile 后台接收图片流的参数名
        } else {
            file = new File(verify.getCardUrl());//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
            builder.addFormDataPart("handFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        }
        Map<String, Object> map = getParameterMap();
        map.put("name", name);//真实姓名
        map.put("cardNo", cardNo);//身份证号码
        builder.addFormDataPart("sign", RequestBodyUtils.setEncrypt(map));
        List<MultipartBody.Part> parts = builder.build().parts();
        getHttpService().realNameAuthentication(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String jsonObject) {
                        getListener().onSuccess(IntegerUtil.WEB_API_NameAuthentication, jsonObject);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_NameAuthentication, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_NameAuthentication, ex.getMessage());
                        }
                    }
                });

    }

    /*****
     * 实名认证状态数据
     */
    public void getCardApprData() {
        getHttpService().cardApprData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<RealNameVerify>(getContext(), false) {
                    @Override
                    protected void onDone(RealNameVerify verify) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CardApprData, verify);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CardApprData, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CardApprData, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 保存认证信息（教师资格、学历、专业资格认证）
     * @param photoUrl
     * @param title
     */
    public void saveCardAppr(String photoUrl, String title) {
        HttpService httpService = getHttpService();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        File file = new File(photoUrl);//filePath 图片地址
        RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
        RequestBody getImageBody = RequestBody.create(MediaType.parse("img/jpeg"), new byte[0]);
        switch (title) {
            case "学历认证":
                builder.addFormDataPart("eduCertifyFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                builder.addFormDataPart("teachingCertifyFile", "teachingCertifyFile", getImageBody);//imgfile 后台接收图片流的参数名
                builder.addFormDataPart("professionaCertifyFile", "professionaCertifyFile", getImageBody);//imgfile 后台接收图片流的参数名

                break;
            case "教师资格认证":
                builder.addFormDataPart("teachingCertifyFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                builder.addFormDataPart("eduCertifyFile", "eduCertifyFile", getImageBody);
                builder.addFormDataPart("professionaCertifyFile", "professionaCertifyFile", getImageBody);
                break;
            case "专业资质认证":
                builder.addFormDataPart("professionaCertifyFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                builder.addFormDataPart("eduCertifyFile", "eduCertifyFile", getImageBody);
                builder.addFormDataPart("teachingCertifyFile", "teachingCertifyFile", getImageBody);
                break;
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        httpService.authenticationOther(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String jsonObject) {
                        getListener().onSuccess(IntegerUtil.WEB_API_SaveCardAppr, jsonObject);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SaveCardAppr, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SaveCardAppr, ex.getMessage());
                        }
                    }
                });
    }

    /**
     * 开课设置数据
     */
    private void getCourseSetting() {
        getHttpService().startCourseSetupData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<StartCourseSetupBean>(getContext(), true) {
                    @Override
                    protected void onDone(StartCourseSetupBean courseBean) {
                        getListener().onSuccess(IntegerUtil.WEB_API_CourseSetting, courseBean);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_CourseSetting, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_CourseSetting, ex.getMessage());
                        }
                    }
                });

    }

    /*****
     * 保存开课设置数据
     */
    private void saveCourseSetting(Map<String, Object> map) {
        getHttpService().addStartCourseData(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        ConstantUrl.CLIEN_Info = 1;
                        getListener().onSuccess(IntegerUtil.WEB_API_SaveCourseSetting, str);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_SaveCourseSetting, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_SaveCourseSetting, ex.getMessage());
                        }
                    }
                });

    }

    /*****
     * 获取老师课程信息
     */
    private void getTeacherMessage() {
        getHttpService().myHomePage()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<TeacherInfo>(getContext(), true) {
                    @Override
                    protected void onDone(TeacherInfo strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_TeacherMessage, strTeacher);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_TeacherMessage, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_TeacherMessage, ex.getMessage());
                        }
                    }
                });

    }

    /******
     * 我的钱包账户记录信息
     * @param time
     * @param pageNum
     */
    public void getWalletList(long time, int pageNum) {
        Map<String, Object> map = getParameterMap();
        map.put("pageNum", pageNum);
        map.put("pageSize", 15);
        map.put("billTime", time);
        ConstantUrl.CLIEN_Info = 2;
        getHttpService().myWallet(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<BasePageBean<WalletDetailsBean>>(getContext(), true) {
                    @Override
                    protected void onDone(BasePageBean<WalletDetailsBean> myWallet) {
                        ConstantUrl.CLIEN_Info = 1;
                        getListener().onSuccess(IntegerUtil.WEB_API_WalletList, myWallet);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_WalletList, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_WalletList, ex.getMessage());
                        }
                    }
                });
    }

    /******
     * 账户余额信息
     */
    public void getMyBalance() {
        getHttpService().myBalance()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<MyBalance>(getContext(), true) {
                    @Override
                    protected void onDone(MyBalance myBalance) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyBalance, myBalance);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyBalance, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyBalance, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 申请提现
     * @param availAmount
     */
    public void applyExtractCash(long availAmount) {
        Map<String, Object> map = getParameterMap();
        map.put("withdrawalsAmount", availAmount);
        getHttpService().userWithdrawals(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_ExtractCash, strTeacher);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_ExtractCash, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_ExtractCash, ex.getMessage());
                        }
                    }
                });


    }

    /*****
     * 获取我的助教
     */
    public void getMinesSitant() {
        getHttpService().userAssistant()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<UserAssistantMode>(getContext(), true) {
                    @Override
                    protected void onDone(UserAssistantMode assistantMode) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MinesSitant, assistantMode);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MinesSitant, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MinesSitant, ex.getMessage());
                        }
                    }
                });
    }

    /******
     * 网络请求得到助教列表
     * @param mobile
     */
    public void getAssistantList(String mobile) {
        Map<String, Object> map = getParameterMap();
        map.put("assistantMobile", mobile);
        getHttpService().searchAssistant(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<SearchAssistantMode>(getContext(), true) {
                    @Override
                    protected void onDone(SearchAssistantMode assistantMode) {
                        getListener().onSuccess(IntegerUtil.WEB_API_AssistantList, assistantMode);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_AssistantList, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_AssistantList, ex.getMessage());
                        }
                    }
                });
    }

    /**
     * 绑定助教
     */
    public void bindingAssistant(int assistantId) {
        Map<String, Object> map = getParameterMap();
        map.put("assistantId", assistantId);
        getHttpService().bindingAssistant(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<UserAssistantMode>(getContext(), true) {
                    @Override
                    protected void onDone(UserAssistantMode userAssistant) {
                        getListener().onSuccess(IntegerUtil.WEB_API_BindingAssistant, userAssistant);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_BindingAssistant, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_BindingAssistant, ex.getMessage());
                        }
                    }
                });
    }

    /*****
     * 获取提现记录
     */
    public void getPresentRecordList() {
        getHttpService().presentRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<PresentRecordBean>>(getContext(), true) {
                    @Override
                    protected void onDone(List<PresentRecordBean> recordBeanList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_PresentRecordList, recordBeanList);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_PresentRecordList, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_PresentRecordList, ex.getMessage());
                        }
                    }
                });

    }

    /*****
     * 绑定银行卡
     * @param bankBean
     */
    public void bindingBank(MyBankCardBean bankBean) {
        Map<String, Object> map = getParameterMap();
        map.put("bankType", bankBean.getBankType());
        map.put("bankNum", bankBean.getBankNum());
        map.put("bankSub", bankBean.getBankSub());
        map.put("bankProvince", bankBean.getBankProvince());
        map.put("bankCity", bankBean.getBankCity());
        getHttpService().bindingBankInfo(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String methodCode) {
                        getListener().onSuccess(IntegerUtil.WEB_API_BindingBank, methodCode);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_BindingBank, "");
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_BindingBank, ex.getMessage());
                        }
                    }
                });

    }

    /**
     * 我的银行卡
     */
    private void getMyBankCard() {
        getHttpService().myBankCard()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<MyBankCardBean>(getContext(), true) {
                    @Override
                    protected void onDone(MyBankCardBean myBankCardBean) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyBankCard, myBankCardBean);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyBankCard, null);
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyBankCard, ex.getMessage());
                        }
                    }
                });
    }

}
