package com.shengzhe.disan.xuetangteacher.mvp.view;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.main.disanxuelib.view.CommonCrosswiseBar;

/**
 * Created by Administrator on 2017/11/27.
 */

public interface IOpenCityView {
    RecyclerView getRecViw();
    CommonCrosswiseBar getCenterOpencity();
    TextView getOpencity();

}
