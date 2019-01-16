package com.shengzhe.disan.xuetangparent.wxapi;

import com.main.disanxuelib.util.ToastUtil;
import com.shengzhe.disan.xuetangparent.app.MyApplication;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import org.greenrobot.eventbus.EventBus;

/*****
 * 微信支付
 *
 * liukui
 *
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    @Override
    public void initData() {
        // IWXAPI实例
        MyApplication.getInstance().getIwxApi().handleIntent(getIntent(), this);
    }

    @Override
    public int setLayout() {
        return 0;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MyApplication.getInstance().getIwxApi().handleIntent(intent, this);
    }



    //微信支付，回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        //微信支付
        Bundle bundle = new Bundle();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_7001);
                EventBus.getDefault().post(bundle);
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtil.showShort("支付已取消");
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_7005);
                EventBus.getDefault().post(bundle);
                break;

            case BaseResp.ErrCode.ERR_COMM:
                ToastUtil.showShort("请确认微信登录是否正常");
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                //发送失败
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                //不支持的错误
                ToastUtil.showShort("支付失败");
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_7006);
                EventBus.getDefault().post(bundle);
                break;

            default:
                ToastUtil.showShort("支付不给力，再给一次机会！");
                break;

        }
        onBackPressed();
    }

    // 微信登录，回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                ToastUtil.showShort("COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
        }
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
        StringBuffer msg = new StringBuffer();
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);

        ToastUtil.showShort(msg.toString());
        onBackPressed();
    }

    @Override
    public void onClick(View v) {

    }
}