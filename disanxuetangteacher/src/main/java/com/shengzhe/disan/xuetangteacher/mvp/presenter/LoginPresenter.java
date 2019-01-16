package com.shengzhe.disan.xuetangteacher.mvp.presenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.common.camera.callback.IPhotoCall;
import com.common.camera.utils.CameraAlbumUtils;
import com.common.camera.utils.VanCropType;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.bean.CityBean;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.bean.User;
import com.shengzhe.disan.xuetangteacher.mvp.model.CommonModelImpl;
import com.shengzhe.disan.xuetangteacher.mvp.model.LoginModelImpl;
import com.shengzhe.disan.xuetangteacher.mvp.model.MVPRequestListener;
import com.shengzhe.disan.xuetangteacher.mvp.view.ILoginView;
import com.shengzhe.disan.xuetangteacher.utils.APKVersionUtil;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

/**
 * Created by 课表业务处理 on 2017/11/27.
 */
public class LoginPresenter extends BasePresenter implements MVPRequestListener,IPhotoCall {
    private ILoginView view;
    private LoginModelImpl modelImpl;
    private CommonModelImpl commonModelImpl;
    private User user;
    private int sexId = -1;
    //定时秒数
    private TimeCount time;
    private SystemPersimManage manage = null;

    public LoginPresenter(Context context, ILoginView view) {
        super(context);
        this.view = view;
        if (modelImpl == null)
            modelImpl = new LoginModelImpl(context, this);
        if (commonModelImpl == null)
            commonModelImpl = new CommonModelImpl(context, this);
    }

    public void initDate() {
        view.getShade().setVisibility(View.VISIBLE);
        view.getOneNotify().setVisibility(View.INVISIBLE);
        view.getTwoNotify().setVisibility(View.INVISIBLE);
        time = new TimeCount(60000, 1000);
        view.getLoginSubmit().setEnabled(false);
        view.getConfirm().setEnabled(false);
        view.getJump().setEnabled(false);
        view.getHeadImage().setEnabled(false);

        view.getUserName().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(view.getUserName().getText().toString().trim()) || TextUtils.isEmpty(view.getUserPwd().getText().toString().trim()))
                    view.getLoginSubmit().setEnabled(false);
                else
                    view.getLoginSubmit().setEnabled(true);
                view.getOneNotify().setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        view.getUserPwd().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(view.getUserName().getText().toString().trim()) || TextUtils.isEmpty(view.getUserPwd().getText().toString().trim()))
                    view.getLoginSubmit().setEnabled(false);
                else
                    view.getLoginSubmit().setEnabled(true);
                view.getOneNotify().setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        view.getNicName().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(user==null || (StringUtils.textIsEmpty(user.getPhotoUrl())&&StringUtils.textIsEmpty(imageUrl))||StringUtils.textIsEmpty(view.getNicName().getText().toString().trim()))
                    view.getConfirm().setEnabled(false);
                else
                    view.getConfirm().setEnabled(true);
                view.getTwoNotify().setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        view.getSex().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                sexId = StringUtils.getSexInt(((RadioButton)group.findViewById(checkedId)).getText().toString());
            }
        });
        twoOutAnim();
    }

    public void claerAllDate() {
        if (view.getOneLayout()!=null)
            view.getOneLayout().clearAnimation();
        if (view.getTwoLayout()!=null)
            view.getTwoLayout().clearAnimation();
    }

    /**
     * 检查是不是正确
     *
     * @return
     */
    private String mSecurity;

    private boolean isCheck() {
        mPhone = view.getUserName().getText().toString().trim();
        mSecurity = view.getUserPwd().getText().toString().trim();
        if (TextUtils.isEmpty(mPhone)) {
            setOneNotifyText("请输入手机号");
            setEtLoginMobile(view.getUserName());
            return false;
        }
        if (!RegUtil.checkPhone(mPhone)) {
            setOneNotifyText("手机号有误，请重新输入");
            setEtLoginMobile(view.getUserName());
            return false;
        }
        if (TextUtils.isEmpty(mSecurity)) {
            setOneNotifyText("请输入验证码");
            setEtLoginMobile(view.getUserPwd());
            return false;
        }
        if (!RegUtil.isSecurity(mSecurity)) {
            setOneNotifyText("验证码输入有误");
            setEtLoginMobile(view.getUserPwd());
            return false;
        }
        return true;
    }

    public void sendVerifyLogin() {
        if (!isCheckVerify())
            return;
        commonModelImpl.getSendVerifyLogin(mPhone);
    }

    public void sendLogin() {
        if (!isCheck())
            return;
        modelImpl.sendLogin(mPhone,mSecurity);
    }

    private boolean isCheckedUser(){
        if ((user == null || StringUtils.textIsEmpty(user.getPhotoUrl())) && StringUtils.textIsEmpty(imageUrl)) {
            setTwoNotifyText("请上传头像");
            return false;
        }

        if (StringUtils.textIsEmpty(view.getNicName().getText().toString())) {
            setTwoNotifyText("请输入昵称");
            setEtLoginMobile(view.getNicName());
            return false;
        }

        if (!RegUtil.isUsername(view.getNicName().getText().toString().trim())) {
            setTwoNotifyText("昵称仅支持15个汉字或32个字母");
            setEtLoginMobile(view.getNicName());
            return false;
        }

        if (sexId==-1) {
            setTwoNotifyText("请选择性别");
            return false;
        }
        return true;
    }

    private boolean isModify() {
        if (!StringUtils.textIsEmpty(imageUrl)||!view.getNicName().getText().toString().equals(user.getNickName())||sexId != user.getSex()){
            //有改变
            return true;
        }
        return false;
    }

    public void comfirmUser() {
        if (!isCheckedUser())
            return;
        if (!isModify()){
            viewImpl.goToActivity();
            return;
        }
        if(manage==null)
            manage = new SystemPersimManage(mContext){
                @Override
                public void resultPerm(boolean isCan, int requestCode) {
                    if (isCan){
                        updateUser();
                    }
                }
            };
        manage.CheckedFile();
        updateUser();
    }

    public void updateUser(){
        modelImpl.upDateUser(imageUrl,view.getNicName().getText().toString(),String.valueOf(sexId));
    }

    @Override
    public void onPhotoResult(String photoUrl) {
        if(TextUtils.isEmpty(photoUrl)){
            return;
        }
        imageUrl = photoUrl;
        ImageUtil.setItemRoundImageViewOnlyDisplay(view.getHeadImage(), imageUrl);
        if (StringUtils.textIsEmpty(imageUrl) || StringUtils.textIsEmpty(view.getNicName().getText().toString().trim()))
            view.getConfirm().setEnabled(false);
        else
            view.getConfirm().setEnabled(true);
    }

    public void operatorUserPhoto() {
        CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(view.getFragmentManagers()).setTailorType(VanCropType.CROP_TYPE_CIRCLE).setParam(SystemInfoUtil.getScreenWidth()-10,SystemInfoUtil.getScreenWidth()-10);
    }

    /**
     * 倒计时的内部类
     *
     * @author Administrator 只要继承CountDownTimer 自会重写继承构造，方法
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (view.getSendcode()==null)
                return;
            view.getSendcode().setClickable(false);// 设置不可点击
            view.getSendcode().setText(millisUntilFinished / 1000 + "s");
            if (millisUntilFinished / 1000 <= 1) {
                view.getSendcode().setTextColor(UiUtils.getColor(R.color.color_ff1d97ea));
                view.getSendcode().setClickable(true);// 倒数一秒的时候 获得点击
                view.getSendcode().setText("获取验证码");
            }
        }

        @Override
        public void onFinish() {
            if (view.getSendcode()==null)
                return;
            view.getSendcode().setEnabled(true);
        }
    }

    private void twoOutAnim() {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.8f);//x轴上的缩放比例，也就是横向缩放比例
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.8f);//y轴上的缩放比例，也就是纵向缩放比例
        PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", 200f);//y轴上的缩放比例，也就是纵向缩放比例
        ObjectAnimator movingFragmentAnimator = ObjectAnimator.ofPropertyValuesHolder(view.getTwoLayout(), scaleX, scaleY, translateY);

        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(view.getTwoLayout(), "rotationX", 0);
        movingFragmentRotator.setDuration(400);
        movingFragmentRotator.setStartDelay(300);
        AnimatorSet s = new AnimatorSet();
        s.playTogether(movingFragmentAnimator, movingFragmentRotator);
        s.start();
    }

    private void twoInAnim() {
        PropertyValuesHolder rotateX = PropertyValuesHolder.ofFloat("rotationX", 40f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f);
        PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", 0);//y轴上的缩放比例，也就是纵向缩放比例
        ObjectAnimator movingFragmentAnimator = ObjectAnimator.ofPropertyValuesHolder(view.getTwoLayout(), rotateX, scaleX, scaleY, translateY);
        ObjectAnimator movingFragmentRotator = ObjectAnimator.ofFloat(view.getTwoLayout(), "rotationX", 0);

        //movingFragmentRotator.setStartDelay(200);
        AnimatorSet s = new AnimatorSet();
        s.playTogether(movingFragmentAnimator, movingFragmentRotator);
        s.start();
    }

    private void oneOutAnim() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(300);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator()); //横向动画设为匀速运动
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.3f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                view.getOneLayout().setX(-point.x);
                view.getOneLayout().setY(point.y);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator arg0) {
                view.getShade().setVisibility(View.GONE);
                view.getOneLayout().setVisibility(View.GONE);
            }
        });
    }


    private String mPhone;
    private boolean isCheckVerify() {
        mPhone =  view.getUserName().getText().toString().trim();
        if (TextUtils.isEmpty(mPhone)) {
            setOneNotifyText("请输入手机号");
            setEtLoginMobile(view.getUserName());
            return false;
        }

        if (!RegUtil.checkPhone(mPhone)){
            setOneNotifyText("手机号有误，请重新输入");
            setEtLoginMobile(view.getUserName());
            return false;
        }
        return true;
    }

    /**
     * 聚焦
     */
    private void setEtLoginMobile(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private String imageUrl = "";
    @Override
    public void onSuccess(int tager, Object objects) {
        switch (tager){
            case IntegerUtil.WEB_API_SendVerifyLogin:
                if (viewImpl!=null)
                    viewImpl.isBack(true);
                ToastUtil.showToast(objects+"");
                view.getSendcode().setTextColor(UiUtils.getColor(R.color.color_333333));
                //倒计时
                time.start();
                break;

            case IntegerUtil.WEB_API_UserLogin:
                user = (User)objects;
                SharedPreferencesManager.setOpenCity(user.getIsOpenCity());
                SharedPreferencesManager.setUserInfo(user);
                CityBean cityBean=new CityBean();
                cityBean.setCityName(user.getCityName());
                cityBean.setCityCode(user.getCity());
                SharedPreferencesManager.setUserCity(cityBean);
                if (user != null) {
                    ImageUtil.loadCircleImageView(mContext, user.getPhotoUrl(), view.getHeadImage(), R.mipmap.ic_head_3rd_normal);
                    view.getNicName().setText(StringUtils.textIsEmpty(user.getNickName()) ? "" : user.getNickName());
                    if (user.getSex()!=null) {
                        sexId = user.getSex();
                        //1、男 0、女
                        view.getSex().check(sexId == 0 ? R.id.rb_login_woman : R.id.rb_login_man);
                        view.getConfirm().setEnabled(true);
                    }else
                        view.getConfirm().setEnabled(false);

                    if (viewImpl!=null)
                        viewImpl.isBack(true);
                }
                view.getHeadImage().setEnabled(true);
                view.getJump().setEnabled(true);
                view.getOneNotify().setVisibility(View.INVISIBLE);
                time.onFinish();
                oneOutAnim();
                twoInAnim();
                break;

            case IntegerUtil.WEB_API_UpDateUser:
                if (viewImpl!=null)
                    viewImpl.goToActivity();
                break;
        }

    }

    @Override
    public void onFailed(int tager, String mesg) {
        if (tager==IntegerUtil.WEB_API_UpDateUser){
            setTwoNotifyText(mesg);
        }else
            ToastUtil.showShort(mesg);
    }

    private void setOneNotifyText(String mesg) {
        view.getOneNotify().setText(mesg);
        view.getOneNotify().setVisibility(View.VISIBLE);
    }

    private void setTwoNotifyText(String mesg) {
        view.getTwoNotify().setText(mesg);
        view.getTwoNotify().setVisibility(View.VISIBLE);
    }

    public void setLoginNotifyView(LoginNotifyView viewImpl){
        this.viewImpl = viewImpl;
    }

    private LoginNotifyView viewImpl;
    public interface LoginNotifyView{
        void isBack(boolean isCan);
        void goToActivity();
    }

}
