package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import com.main.disanxuelib.view.CommonEditView;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CommonPresenter;
import com.shengzhe.disan.xuetangparent.mvp.presenter.UserPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.LoginView;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.LoginOpentionUtil;
import com.shengzhe.disan.xuetangparent.utils.HtmlJumpUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

/******
 * 用户登录
 */
public class LoginActivity extends BaseActivity implements LoginView.ILoginView {
    @BindView(R.id.et_login_mobile)
    CommonEditView etLoginMobile;
    @BindView(R.id.et_login_psw)
    CommonEditView etLoginPsw;
    @BindView(R.id.bt_login_submit)
    Button btLoginSubmit;

    private UserPresenter presenter;

    @Override
    public void initData() {
        if(presenter==null)
            presenter = new UserPresenter(mContext,this);
        presenter.setLoginUi();
        presenter.setLoginDatas();
        etLoginPsw.setViewOnClick(R.id.tv_edittext_rightText,this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    @OnClick({R.id.common_bar_rightBtn, R.id.bt_login_submit, R.id.tv_login_agreement})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.common_bar_rightBtn:
                //关闭
                LoginOpentionUtil.getInstance().LoginCancel();
                onBackPressed();
                break;

            case R.id.tv_edittext_rightText:
                //发送验证码
                presenter.sendVerifyLogin();
                break;

            case R.id.bt_login_submit:
                //提交
                presenter.sendUserLogin();
                break;

            case R.id.tv_login_agreement:
                //协议
                HtmlJumpUtil.yhxyActivity();
                break;
        }
    }

    @Override
    public CommonEditView getLoginMobileView() {
        return etLoginMobile;
    }

    @Override
    public CommonEditView getLoginPswView() {
        return etLoginPsw;
    }

    @Override
    public Button getLoginSubmitView() {
        return btLoginSubmit;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (presenter.isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Bundle bundle = new Bundle();
            bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11017);
            EventBus.getDefault().post(bundle);
            onBackPressed();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
