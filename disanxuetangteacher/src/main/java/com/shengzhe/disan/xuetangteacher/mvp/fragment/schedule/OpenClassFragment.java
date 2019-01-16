package com.shengzhe.disan.xuetangteacher.mvp.fragment.schedule;

import android.os.Bundle;
import android.view.View;

import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;


public class OpenClassFragment extends BaseFragment {


    public static OpenClassFragment newInstance(String from) {
        OpenClassFragment fragment = new OpenClassFragment();
        Bundle args = new Bundle();
        args.putString(StringUtils.FRAGMENT_DATA, from);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void initData() {


    }

    @Override
    public int setLayout() {

        return  R.layout.fragment_open_class;
    }

    @Override
    public void onClick(View v) {

    }
}
