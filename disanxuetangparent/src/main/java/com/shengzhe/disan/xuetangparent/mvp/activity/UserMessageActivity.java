package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.common.camera.callback.IPhotoCall;
import com.common.camera.utils.CameraAlbumUtils;
import com.common.camera.utils.VanCropType;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.bean.Personal;
import com.shengzhe.disan.xuetangparent.mvp.presenter.UserPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.UserMessageView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liukui on 2017/11/22.
 * <p>
 * 个人资料
 */

public class UserMessageActivity extends BaseActivity implements IPhotoCall,UserMessageView.IUserMessageView {
    @BindView(R.id.ccb_studentsex)
    CommonCrosswiseBar ccb_studentsex;

    @BindView(R.id.ccb_studentaddress)
    CommonCrosswiseBar mAddress;

    @BindView(R.id.ccb_userhead)
    CommonCrosswiseBar mHead;
    @BindView(R.id.ccb_usernicename)
    CommonCrosswiseBar ccbUsernicename;
    @BindView(R.id.ccb_studentname)
    CommonCrosswiseBar ccbStudentname;
    @BindView(R.id.ccb_studentschool)
    CommonCrosswiseBar ccbStudentschool;

    @BindView(R.id.ccb_studentclass)
    CommonCrosswiseBar ccbStudentClass;

    private NiceDialog niceDialog;
    private CommonCrosswiseBar commonText;

    private Personal personal;
    private  Address address;

    private UserPresenter presenter;

    @Override
    public void initData() {
        if (presenter==null)
            presenter = new UserPresenter(mContext,this);
        presenter.initUserUi();
        personal = SharedPreferencesManager.getPersonaInfo();
        if(personal==null)
            return;
        presenter.setUserDatas(personal);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_usermessage;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.ccb_userhead, R.id.ccb_usernicename, R.id.ccb_studentname, R.id.ccb_studentclass , R.id.ccb_studentsex, R.id.ccb_studentschool, R.id.ccb_studentaddress})
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ccb_userhead:
                //头像选择
                CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(getSupportFragmentManager()).setTailorType(VanCropType.CROP_TYPE_CIRCLE).setParam(SystemInfoUtil.getScreenWidth()-10,SystemInfoUtil.getScreenWidth()-10);
                break;

            case R.id.ccb_usernicename:
                //用户昵称选择
                commonText = (CommonCrosswiseBar) v;
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, ModifyMessageActivity.class);
                intent.putExtra("title", "昵称");
                intent.putExtra("hintText", "请输入昵称");
                intent.putExtra("text", commonText.getRightText());
                startActivityForResult(intent, IntegerUtil.Activity_Modify);
                break;

            case R.id.ccb_studentname:
                //学生姓名选择
                commonText = (CommonCrosswiseBar) v;
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, ModifyMessageActivity.class);
                intent.putExtra("title", "学生姓名");
                intent.putExtra("hintText", "请输入学生姓名");
                intent.putExtra("text", commonText.getRightText());
                startActivityForResult(intent, IntegerUtil.Activity_Modify);
                break;

            case R.id.ccb_studentclass:
                //年级选择
                Intent grade = new Intent(this, SelectGradeActivity.class);
                grade.putExtra("fatherId", fatherId);
                grade.putExtra("courseType", courseType==null?new CourseType():courseType);
                startActivity(grade);
                break;

            case R.id.ccb_studentsex:
                //性别选择
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setLayoutId(R.layout.select_sex)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                                holder.setOnClickListener(R.id.tv_select_male, UserMessageActivity.this);
                                holder.setOnClickListener(R.id.tv_select_female, UserMessageActivity.this);
                                holder.setOnClickListener(R.id.tv_select_cancel, UserMessageActivity.this);
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;

            case R.id.ccb_studentschool:
                //学校选择
                commonText = (CommonCrosswiseBar) v;
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, ModifyMessageActivity.class);
                intent.putExtra("title", "目前就读学校");
                intent.putExtra("hintText", "请输入学生目前就读学校");
                intent.putExtra("text", commonText.getRightText());
                startActivityForResult(intent, IntegerUtil.Activity_Modify);
                break;

            case R.id.ccb_studentaddress:
                //地址选择
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, AddressCommonActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, address);
                startActivity(intent);
                break;

            case R.id.tv_select_male:
                //男
                colseDialog();
                ccb_studentsex.setRightText("男");
                presenter.savePersonalSex(personal,StringUtils.getSexInt(ccb_studentsex.getRightText()));
                break;

            case R.id.tv_select_female:
                //女
                colseDialog();
                ccb_studentsex.setRightText("女");
                presenter.savePersonalSex(personal,StringUtils.getSexInt(ccb_studentsex.getRightText()));
                break;

            case R.id.tv_select_cancel:
                //相机选择取消
                colseDialog();
                break;
        }
    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntegerUtil.Activity_Modify:
                if (data == null)
                    return;
                commonText.setRightText(data.getStringExtra("result"));
                break;
        }
    }

    private CourseType courseType;
    private int fatherId = -1;

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        Object data = bundle.getParcelable(StringUtils.EVENT_DATA);
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11011:
                //地址返回
                if (data == null)
                    return false;
                 address = (Address) data;
                if (address == null)
                    return false;
                mAddress.setRightText(address.province + " " + address.city + " " + address.district + "  " + address.address);
                break;

            case IntegerUtil.EVENT_ID_11012:
                //选择年级
                if (data == null)
                    return false;
                courseType = (CourseType) data;
                fatherId = bundle.getInt(StringUtils.EVENT_DATA2);
                ccbStudentClass.setRightText(courseType.name);
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11013);
        EventBus.getDefault().post(bundle);
        finish();
    }

    @Override
    public void onPhotoResult(String photoUrl) {
        ImageUtil.setItemRoundImageViewOnlyDisplay(mHead.getRightImage(), photoUrl);
        presenter.upDateUserPhoto(photoUrl);
    }

    @Override
    public CommonCrosswiseBar getStudentSexView() {
        return ccb_studentsex;
    }

    @Override
    public CommonCrosswiseBar getAddressView() {
        return mAddress;
    }

    @Override
    public CommonCrosswiseBar getHeadView() {
        return mHead;
    }

    @Override
    public CommonCrosswiseBar getUserNiceNameView() {
        return ccbUsernicename;
    }

    @Override
    public CommonCrosswiseBar getStudentNameView() {
        return ccbStudentname;
    }

    @Override
    public CommonCrosswiseBar getStudentSchoolView() {
        return ccbStudentschool;
    }

    @Override
    public CommonCrosswiseBar getStudentClassView() {
        return ccbStudentClass;
    }
}
