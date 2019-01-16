package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.R;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/8.
 */

public class UpLoadPayActivity extends BaseActivity {
    @BindView(R.id.et_uploadpay_ordernum)
    TextView mOrderNum;
    @BindView(R.id.iv_uploadpay_image)
    ImageView mImage;
    @BindView(R.id.iv_uploadpay_reupload)
    TextView mReUpload;
    @BindView(R.id.iv_uploadpay_confirm)
    RadioButton mComfirm;

    @Override
    public void initData() {
        mComfirm.setChecked(false);
        mComfirm.setEnabled(false);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_uploadpay;
    }

    @OnClick({R.id.common_bar_leftBtn,R.id.iv_uploadpay_reupload,R.id.iv_uploadpay_confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.iv_uploadpay_reupload:
                //拍照片或相机相册

                break;

            case R.id.iv_uploadpay_confirm:

                break;
        }
    }
}
