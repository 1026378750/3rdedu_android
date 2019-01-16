package com.shengzhe.disan.xuetangparent.http;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.gson.JsonParseException;
import com.main.disanxuelib.util.LogUtils;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.ProgressBarDialog;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.activity.MainActivity;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.bean.User;
import com.main.disanxuelib.util.AppManager;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.LoginOpentionUtil;
import org.json.JSONException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by hy on 2017/10/19.
 */
public abstract class AbsAPICallback<T> extends Subscriber<T> {
    protected  Context mContext;
    private ProgressBarDialog dialog = null;
    public  AbsAPICallback(Context context,boolean isLoadProgress) {
        this.mContext = context;
        if(isLoadProgress && dialog == null){
            dialog = new ProgressBarDialog(context);
        }
        if(isLoadProgress && dialog!=null){
            dialog.showProgress();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(dialog!=null&&dialog.isShowing()){
            dialog.closeProgress();
        }
        Log.e("e","httpException = " +e);
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        if (e instanceof HttpException) {//HTTP错误
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case IntegerUtil.ABS_UNAUTHORIZED:
                    break;
                case IntegerUtil.ABS_FORBIDDEN:
                    break;
                case IntegerUtil.ABS_NOT_FOUND:
                    ToastUtil.showShort(UiUtils.getString(R.string.noNetWork));
                    break;
                case IntegerUtil.ABS_REQUEST_TIMEOUT:
                    ToastUtil.showShort(UiUtils.getString(R.string.timeout));
                    break;
                case IntegerUtil.ABS_GATEWAY_TIMEOUT:
                    ToastUtil.showShort(UiUtils.getString(R.string.timeout));
                    break;
                case IntegerUtil.ABS_INTERNAL_SERVER_ERROR:
                    break;
                case IntegerUtil.ABS_BAD_GATEWAY:
                    break;
                case IntegerUtil.ABS_SERVICE_UNAVAILABLE:
                    ToastUtil.showShort(UiUtils.getString(R.string.unavailable));
                    break;
                default:
                    LogUtils.d(UiUtils.getString(R.string.InternalServerError));
                    break;
            }
        } else if (e instanceof SocketTimeoutException) {
            String message = UiUtils.getString(R.string.timeout);
            ToastUtil.showShort(message);
            ResultException resultException = new ResultException(0,message);
            onResultError(resultException);
        } else if (e instanceof ResultException) {//服务器返回的错误
            ResultException resultException = (ResultException) e;
            switchError(resultException);
            Log.e("e","resultException = " +resultException.getMessage()+"----erroer = " +resultException.getErrCode());
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            LogUtils.d(UiUtils.getString(R.string.JsonSyntaxException));
        } else if(e instanceof ConnectException){
            String message = UiUtils.getString(R.string.noNetWork);
            ToastUtil.showShort(message);
            ResultException resultException = new ResultException(0,message);
            onResultError(resultException);
        }
    }
    /**
     * 服务器返回的错误
     */
    protected abstract void onResultError(ResultException ex);

    protected abstract void onDone(T t);

    @Override
    public void onCompleted() {

    }
    private ConfirmDialog comfirmDialog;
    private void switchError(ResultException resultException){
        switch (resultException.getErrCode()){
            case IntegerUtil.ABS_TOKEN_FAIL://您的账号登录异常，请重新登录！
                ToastUtil.showShort(UiUtils.getString(R.string.tokenVerifyFail));
                SharedPreferencesManager.setUserInfo(new User());
                ConstantUrl.TOKN = "";
                LoginOpentionUtil.getInstance().LoginRequest(mContext);
                break;

            case IntegerUtil.ABS_TOKEN_EXPIRE://您的账号已过期，为确保信息安全，请重新登录！
                FragmentManager mamage = ((AppCompatActivity) mContext).getSupportFragmentManager();
                if(comfirmDialog==null)
                    comfirmDialog = ConfirmDialog.newInstance("", UiUtils.getString( R.string.tokenVerifyExpire) , "稍后再说", "立即登录");
                if(comfirmDialog.isVisible())
                    return;
                comfirmDialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                        .setOutCancel(false)
                        .show(mamage);
                comfirmDialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){
                    @Override
                    public void dialogStatus(int id) {
                        switch (id){
                            case R.id.tv_dialog_cancel:
                                //取消 到首页
                                SharedPreferencesManager.setUserInfo(new User());
                                ConstantUrl.TOKN = "";
                                AppManager.getAppManager().goToActivityForName(MainActivity.class.getName());
                                break;

                            case R.id.tv_dialog_ok:
                                SharedPreferencesManager.setUserInfo(new User());
                                ConstantUrl.TOKN = "";
                                LoginOpentionUtil.getInstance().LoginRequest(mContext);
                                break;
                        }
                    }
                });
                break;

            case IntegerUtil.ABS_TOKEN_POST_NULL://请求token不能为空
                ToastUtil.showShort(UiUtils.getString(R.string.tokenisnull));
                SharedPreferencesManager.setUserInfo(new User());
                ConstantUrl.TOKN = "";
                LoginOpentionUtil.getInstance().LoginRequest(mContext);
                break;

            default:
                onResultError(resultException);
                break;

        }
    }

    @Override
    public void onNext(T t) {
        onDone(t);
        if(dialog!=null&&dialog.isShowing()){
            dialog.closeProgress();
        }
    }
}
