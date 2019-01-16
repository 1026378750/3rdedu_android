package com.shengzhe.disan.xuetangteacher.mvp.view;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/11/27.
 */

public interface ILoginView {
    View getShade();
    View getOneLayout();
    View getTwoLayout();
    EditText getUserName();
    EditText getUserPwd();
    TextView getSendcode();
    TextView getOneNotify();
    Button getLoginSubmit();
    ImageView getHeadImage();
    EditText getNicName();
    RadioGroup getSex();
    Button getConfirm();
    TextView getJump();
    TextView getTwoNotify();
    FragmentManager getFragmentManagers();
}
