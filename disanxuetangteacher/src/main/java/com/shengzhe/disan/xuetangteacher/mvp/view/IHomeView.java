package com.shengzhe.disan.xuetangteacher.mvp.view;

import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.banner.MyBanner;

/**
 * Created by Administrator on 2017/11/27.
 */

public interface IHomeView {
    RefreshCommonView getRefreshCommonView();
    TextView getNotify();
    TextView getNiceName();
    TextView getMessage();
    TextView getOrderNum();
    TextView getMineClass();
    TextView getTimes();
    ImageView getHeadImage();
    LinearLayout getLayout();
    MyBanner getHomeBanner();
    FragmentManager getFragmentManagers();
}
