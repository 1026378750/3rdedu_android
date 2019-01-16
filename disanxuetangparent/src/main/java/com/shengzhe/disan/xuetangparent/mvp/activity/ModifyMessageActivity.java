package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.BasePresenter;
import com.shengzhe.disan.xuetangparent.mvp.presenter.UserPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.ModifyMessageView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 修改用户信息 on 2017/11/22.
 */

public class ModifyMessageActivity extends BaseActivity implements ModifyMessageView.IModifyMessageView,BasePresenter.ResultDate {
    @BindView(R.id.ccb_modify_title)
    CommonCrosswiseBar title;
    @BindView(R.id.et_modify_content)
    EditText content;
    private Intent intent;

    private UserPresenter presenter;

    @Override
    public void initData() {
        intent = getIntent();
        if (presenter==null)
            presenter = new UserPresenter(mContext,this);
        presenter.initModifyUi();
        presenter.setResultDatePresenter(this);
        presenter.setModifyDatas(intent.getStringExtra("title"),intent.getStringExtra("hintText"),intent.getStringExtra("text"));
    }

    @Override
    public int setLayout() {
        return R.layout.activity_modifymessage;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.common_bar_rightBtn:
                presenter.savePersonalData(title.getTitleText(),content.getText().toString().trim());
                break;
        }
    }

    @Override
    public CommonCrosswiseBar getTitleView() {
        return title;
    }

    @Override
    public EditText getContentView() {
        return content;
    }

    @Override
    public void resultDate(int tag, Object obj) {
        Intent intent = new Intent();
        intent.putExtra("result", content.getText().toString().trim());
        setResult(Activity.RESULT_OK, intent);
        onBackPressed();
    }
}
