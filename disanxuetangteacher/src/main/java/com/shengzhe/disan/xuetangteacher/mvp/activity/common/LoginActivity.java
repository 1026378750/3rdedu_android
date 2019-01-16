package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.mvp.presenter.LoginPresenter;
import com.shengzhe.disan.xuetangteacher.mvp.view.ILoginView;
import com.shengzhe.disan.xuetangteacher.utils.HtmlJumpUtil;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.LoginOpentionUtil;
import butterknife.BindView;
import butterknife.OnClick;

/******
 * 用户登录
 */
public class LoginActivity extends BaseActivity implements ILoginView,LoginPresenter.LoginNotifyView{
    @BindView(R.id.child_login_shade)
    View mShade;
    @BindView(R.id.child_login_one)
    View mOneLayout;
    @BindView(R.id.child_login_two)
    View mTwoLayout;
    @BindView(R.id.et_login_username)
    EditText mUserName;
    @BindView(R.id.et_login_userpwd)
    EditText mUserPwd;
    @BindView(R.id.tv_login_sendcode)
    TextView tvSendcode;
    @BindView(R.id.tv_loginone_notify)
    TextView mOneNotify;
    @BindView(R.id.et_login_confirm)
    Button btLoginSubmit;

    @BindView(R.id.iv_login_headimage)
    ImageView mHeadImage;
    @BindView(R.id.et_login_nicname)
    EditText mNicName;
    @BindView(R.id.rg_login_sex)
    RadioGroup mSex;
    @BindView(R.id.btn_login_confirm)
    Button mConfirm;
    @BindView(R.id.tv_login_jump)
    TextView mJump;
    @BindView(R.id.tv_logintwo_notify)
    TextView mTwoNotify;

    private LoginPresenter presenter;

    @Override
    public void initData() {
        presenter = new LoginPresenter(mContext, this);
        presenter.setLoginNotifyView(this);
        presenter.initDate();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.tv_login_sendcode, R.id.et_login_confirm, R.id.tv_login_fwxy, R.id.iv_login_headimage, R.id.btn_login_confirm, R.id.tv_login_jump})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_login_sendcode:
                //发生短信验证码
                isBack = false;
                presenter.sendVerifyLogin();
                break;

            case R.id.et_login_confirm:
                //提交
                isBack = false;
                presenter.sendLogin();
                break;

            case R.id.tv_login_fwxy:
                //协议
                HtmlJumpUtil.yhxyActivity();
                break;

            case R.id.iv_login_headimage:
                //头像
                presenter.operatorUserPhoto();
                break;

            case R.id.btn_login_confirm:
                //完成
                isBack = false;
                presenter.comfirmUser();
                break;

            case R.id.tv_login_jump:
                //跳过
                goToActivity();
                break;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IntegerUtil.PERMISSION_REQUEST_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                presenter.updateUser();
            } else {
                showPremission();
            }
        }
    }

    private void showPremission() {
        ConfirmDialog dialog =  ConfirmDialog.newInstance("提示", "您已禁止了访问设备存储空间 <br/>设置路径：设置 ->应用管理 ->" + SystemInfoUtil.getApplicationName() + " ->授权管理<br/><br/>特别提示：<br/><font color='#3a7bd5'>“取消”后您提交的数据将不会被保存！</font>", "取消", "设置");
        dialog.setMessageGravity(Gravity.LEFT);
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener(){

            @Override
            public void dialogStatus(int id) {
                if (id == com.main.disanxuelib.R.id.tv_dialog_ok) {//设置界面
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    ActivityCompat.startActivityForResult((Activity) mContext,intent,IntegerUtil.PERMISSION_REQUEST_SETTING,null);
                }else{
                    goToActivity();
                }
            }
        });
    }

    private boolean isBack = true;
    private long startTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isBack) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                if (System.currentTimeMillis() - startTime < 2000) {
                    AppManager.getAppManager().AppExit();
                } else {
                    startTime = System.currentTimeMillis();
                    Toast.makeText(this, "再次点击退出应用程序", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    @Override
    public View getShade() {
        return mShade;
    }

    @Override
    public View getOneLayout() {
        return mOneLayout;
    }

    @Override
    public View getTwoLayout() {
        return mTwoLayout;
    }

    @Override
    public EditText getUserName() {
        return mUserName;
    }

    @Override
    public EditText getUserPwd() {
        return mUserPwd;
    }

    @Override
    public TextView getSendcode() {
        return tvSendcode;
    }

    @Override
    public TextView getOneNotify() {
        return mOneNotify;
    }

    @Override
    public Button getLoginSubmit() {
        return btLoginSubmit;
    }

    @Override
    public ImageView getHeadImage() {
        return mHeadImage;
    }

    @Override
    public EditText getNicName() {
        return mNicName;
    }

    @Override
    public RadioGroup getSex() {
        return mSex;
    }

    @Override
    public Button getConfirm() {
        return mConfirm;
    }

    @Override
    public TextView getJump() {
        return mJump;
    }

    @Override
    public TextView getTwoNotify() {
        return mTwoNotify;
    }

    @Override
    public FragmentManager getFragmentManagers() {
        return getSupportFragmentManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.claerAllDate();
    }

    @Override
    public void isBack(boolean isCan) {
        isBack = isCan;
    }

    @Override
    public void goToActivity() {
        LoginOpentionUtil.getInstance().LoginResult(getUserName().getText().toString().trim());
    }
}
