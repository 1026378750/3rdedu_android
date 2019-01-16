package com.shengzhe.disan.xuetangparent.mvp.model;

import android.content.Context;
import com.main.disanxuelib.bean.Wallet;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.bean.MyBalance;
import com.shengzhe.disan.xuetangparent.bean.MyBankCardBean;
import com.shengzhe.disan.xuetangparent.bean.Personal;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.bean.UserAssistant;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import java.io.File;
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

    public UserModelImpl(Context context, MVPRequestListener listener,String from) {
        super(context, listener,from);
    }

    /**
     * 个人资料页
     */
    public void getMessage() {
        getHttpService().personalData(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<Personal>(getContext(), true) {
                    @Override
                    protected void onDone(Personal personal) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyMessage, personal,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyMessage, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyMessage, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /****
     * 获取我的老师
     */
    public void getMyTeacherList() {
        getHttpService().myTeacher(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<TeacherInformation>>(getContext(), true) {
                    @Override
                    protected void onDone(List<TeacherInformation> dataList) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyTeacherList, dataList,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyTeacherList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyTeacherList, ex.getMessage(),getFrom());
                        }
                    }
                });
    }


    /******
     * 账户余额信息
     */
    public void getMyBalance() {
        getHttpService().myBalance(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<MyBalance>(getContext(), true) {
                    @Override
                    protected void onDone(MyBalance myBalance) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyBalance, myBalance,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyBalance, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyBalance, ex.getMessage(),getFrom());
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
        map.put("bankMan", bankBean.getOpenMan());

        getHttpService().bindingBankInfo(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String methodCode) {
                        getListener().onSuccess(IntegerUtil.WEB_API_BindingBank, methodCode,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_BindingBank, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_BindingBank, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /**
     * 我的银行卡
     */
    public void getMyBankCard() {
        getHttpService().myBankCard(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<MyBankCardBean>(getContext(), true) {
                    @Override
                    protected void onDone(MyBankCardBean myBankCardBean) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MyBankCard, myBankCardBean,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MyBankCard, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MyBankCard, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /*****
     * 获取我的消息数量
     */
    public void getMessageNum() {
        getHttpService().myMsgNum(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<Integer>(getContext(), false) {
                    @Override
                    protected void onDone(Integer messages) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MessageNum, messages==null?0:messages,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MessageNum, 0,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MessageNum, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

    /******
     * 我的钱包账户记录信息
     * @param time
     */
    public void getWalletList(long time) {
        Map<String, Object> map = getParameterMap();
        map.put("billTime", time);
       // ConstantUrl.CLIEN_Info = 2;
        getHttpService().myWallet(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<Wallet>>(getContext(), true) {
                    @Override
                    protected void onDone(List<Wallet> myWallet) {
                  //      ConstantUrl.CLIEN_Info = 1;
                        getListener().onSuccess(IntegerUtil.WEB_API_WalletList, myWallet,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_WalletList, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_WalletList, ex.getMessage(),getFrom());
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
        getHttpService().userWithdrawals(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String strTeacher) {
                        getListener().onSuccess(IntegerUtil.WEB_API_ExtractCash, strTeacher,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_ExtractCash, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_ExtractCash, ex.getMessage(),getFrom());
                        }
                    }
                });


    }

    /****
     * 更新个人数据
     * @param imageUrl
     */
    public void upDateUserPhoto(String imageUrl) {
        File file = new File(imageUrl);//filePath 图片地址
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("uploadFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        getHttpService().uploadOnePhoto(ConstantUrl.ApiVerCode1,parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String jsonObject) {
                        getListener().onSuccess(IntegerUtil.WEB_API_UpDateUserPhoto, "上传成功",getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_UpDateUserPhoto, "上传成功",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_UpDateUserPhoto, ex.getMessage(),getFrom());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getListener().onFailed(IntegerUtil.WEB_API_UpDateUserPhoto, e.getMessage(),getFrom());
                    }
                });
    }

    /*****
     * 更新用户信息
     * @param map
     */
    public void upDateUser(Map<String, Object> map) {
        getHttpService().savePersonalData(ConstantUrl.ApiVerCode1,RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(getContext(), true) {
                    @Override
                    protected void onDone(String str) {
                        getListener().onSuccess(IntegerUtil.WEB_API_UpDateUser, str,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_UpDateUser, "",getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_UpDateUser, ex.getMessage(),getFrom());
                        }
                    }
                });
    }


    /*****
     * 获取我的助教
     */
    public void getMinesSitant() {
        getHttpService().userAssistant(ConstantUrl.ApiVerCode1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<UserAssistant>(getContext(), true) {
                    @Override
                    protected void onDone(UserAssistant assistantMode) {
                        getListener().onSuccess(IntegerUtil.WEB_API_MinesSitant, assistantMode,getFrom());
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if (ex.getErrCode() == ConstantUrl.error_code2 || ex.getErrCode() == ConstantUrl.error_code1) {
                            getListener().onSuccess(IntegerUtil.WEB_API_MinesSitant, null,getFrom());
                        } else {
                            getListener().onFailed(IntegerUtil.WEB_API_MinesSitant, ex.getMessage(),getFrom());
                        }
                    }
                });
    }

}
