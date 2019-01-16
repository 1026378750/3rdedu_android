package com.shengzhe.disan.xuetangteacher.mvp.model;

import android.content.Context;
import android.text.TextUtils;
import com.main.disanxuelib.util.FileUtil;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.User;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.UmengEventUtils;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/27.
 */

public class LoginModelImpl extends BaseModelImpl {

    public LoginModelImpl(Context context, MVPRequestListener listener) {
        super(context, listener);
    }

    /****
     * 登录
     */
    public void sendLogin(String mPhone, String mSecurity) {
        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mPhone);
        param.put("code", mSecurity);
        param.put("registrationId", JPushInterface.getRegistrationID(getContext()));

        getHttpService().login(RequestBodyUtils.setRequestBody(param))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<User>(getContext(), true) {
                    @Override
                    protected void onDone(User mUser) {
                        UmengEventUtils.toLoginClick(mContext,mUser.getMobile());
                        getListener().onSuccess(IntegerUtil.WEB_API_UserLogin, mUser);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        getListener().onFailed(IntegerUtil.WEB_API_UserLogin, ex.getMessage());
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


                });
    }

}
