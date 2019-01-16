package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.common.camera.callback.IPhotoCall;
import com.common.camera.utils.CameraAlbumUtils;
import com.common.camera.utils.VanCropType;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.bean.PersonalDataBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 基本信息 on 2018/1/15.
 */

public class UserBaseActivity extends BaseActivity implements IPhotoCall{
    @BindView(R.id.ccb_center_head)
    CommonCrosswiseBar mHead;
    @BindView(R.id.ccb_center_nicename)
    CommonCrosswiseBar mNiceName;
    @BindView(R.id.ccb_center_sex)
    CommonCrosswiseBar mSex;

    private CommonCrosswiseBar commonText;
    private PersonalDataBean  personalDataBean;

    @Override
    public void initData() {
        personalDataBean = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);

        if(personalDataBean==null)
            return;

        if(!TextUtils.isEmpty( personalDataBean.getPhotoUrl())){
            ImageUtil.setItemRoundImageViewOnlyDisplay(mHead.getRightImage(), personalDataBean.getPhotoUrl());
        }

       if(!TextUtils.isEmpty(personalDataBean.getNickName())){
           mNiceName.setRightText(personalDataBean.getNickName());
       }

       mSex.setRightText(StringUtils.getSex(personalDataBean.getSex()));
    }

    @Override
    public int setLayout() {
        return R.layout.activity_userbase;
    }

    private NiceDialog niceDialog;
    @OnClick({R.id.common_bar_leftBtn,R.id.ccb_center_head,R.id.ccb_center_nicename,R.id.ccb_center_sex})
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ccb_center_head:
                //头像
                CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(getSupportFragmentManager()).setTailorType(VanCropType.CROP_TYPE_CIRCLE).setParam(SystemInfoUtil.getScreenWidth()-10,SystemInfoUtil.getScreenWidth()-10);
                break;

            case R.id.ccb_center_nicename:
                //昵称
                commonText = (CommonCrosswiseBar) v;
                intent = intent == null ? new Intent() : intent;
                intent.setClass(this, ModifyMessageActivity.class);
                intent.putExtra("title", "昵称");
                intent.putExtra("hintText", "请输入昵称");
                intent.putExtra("text", commonText.getRightText());
                 startActivity(intent);
                break;

            case R.id.ccb_center_sex:
                //性别
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
                    @Override
                    public void onItemListener(int index, String item) {
                        mSex.setRightText(item);
                        if(personalDataBean==null)
                            personalDataBean = new PersonalDataBean();
                        personalDataBean.setSex(StringUtils.getSexInt(item));
                    }
                });
                niceDialog.setCommonLayout(new String[]{"男","女"},true,getSupportFragmentManager());
                break;
        }
    }

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11031:
                String  title=bundle.getString("result");
                mNiceName.setRightText(title);
                if(personalDataBean==null)
                    personalDataBean = new PersonalDataBean();
                personalDataBean.setNickName(title);
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Bundle bundles = new Bundle();
        bundles.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11030);
        bundles.putParcelable(StringUtils.EVENT_DATA,personalDataBean);
        EventBus.getDefault().post(bundles);
        finish();
    }

    @Override
    public void onPhotoResult(String photoUrl) {
        if(personalDataBean==null)
            personalDataBean = new PersonalDataBean();
        personalDataBean.setPhotoUrl(photoUrl);
        ImageUtil.setItemRoundImageViewOnlyDisplay(mHead.getRightImage(), photoUrl);
    }
}
