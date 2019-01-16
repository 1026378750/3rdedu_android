package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.bean.UserAssistant;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

/**
 * Created by 助教 on 2018/4/20.
 */

public class MinesSistantView extends BaseView {
    private UserAssistant userAssistant;
    private ConfirmDialog dialog;
    private FragmentManager fragmentManager;

    public MinesSistantView(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
    }

    private IMinesSistantView iView;

    public void setIMinesSistantView(IMinesSistantView iView) {
        this.iView = iView;
    }

    public void setDatas() {
        iView.getSisantNameView().setText("您还未绑定助教");
        iView.getMineMobileView().setText("联系客服");
    }

    public void setResultDatas(UserAssistant userAssistant) {
        this.userAssistant= userAssistant;
        if (userAssistant==null||(StringUtils.textIsEmpty(userAssistant.getMobile())&&StringUtils.textIsEmpty(userAssistant.getName())&&StringUtils.textIsEmpty(userAssistant.getPhotoUrl())))
            return;
        ImageUtil.loadCircleImageView(mContext, userAssistant.getPhotoUrl(),iView.getSisantHeadView(), R.mipmap.ic_personal_avatar);
        if(!StringUtils.textIsEmpty(userAssistant.getName())){
            iView.getSisantNameView().setText(userAssistant.getName());
        }

        if(!StringUtils.textIsEmpty(userAssistant.getMobile())){
            iView.getMineMobileView().setText("联系助教");
        }
    }

    public void callPhone() {
        String mesg = "您确定要拨打";
        if(userAssistant==null || StringUtils.textIsEmpty(userAssistant.getMobile())) {
            mesg += "客服电话\n<font color='#FFAE12'>"+StringUtils.System_Service_Phone+"</font>？";
            userAssistant.setMobile(StringUtils.System_Service_Phone);
        }else{
            mesg += "助教电话\n<font color='#FFAE12'>"+userAssistant.getMobile()+"</font>？";
        }
        if(dialog==null) {
            dialog = ConfirmDialog.newInstance("",mesg, "取消", "立即拨打");
        }
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                .setOutCancel(false)
                .show(fragmentManager);
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
            @Override
            public void dialogStatus(int id) {
                switch (id){
                    case R.id.tv_dialog_ok:
                        //确定
                        SystemInfoUtil.callDialing(userAssistant.getMobile());
                        break;
                }
            }
        });
    }

    public interface IMinesSistantView {
        ImageView getSisantHeadView();

        TextView getSisantNameView();

        TextView getMineMobileView();
    }

}
