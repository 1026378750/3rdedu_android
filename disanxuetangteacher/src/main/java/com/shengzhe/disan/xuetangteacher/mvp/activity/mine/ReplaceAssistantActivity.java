package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ReplaceAssistantActivity extends BaseActivity {

    @BindView(R.id.ccb_dollar_title)
    CommonCrosswiseBar ccbDollarTitle;
    @BindView(R.id.et_replaceassistant_cause)
    EditText etReplaceassistantCause;
    @BindView(R.id.tv_center_comfirm)
    CheckBox tvCenterComfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initData() {
        tvCenterComfirm.setClickable(false);
        etReplaceassistantCause.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())){
                    tvCenterComfirm.setChecked(false);
                    tvCenterComfirm.setClickable(false);
                }else {
                    tvCenterComfirm.setChecked(true);
                    tvCenterComfirm.setClickable(true);
                }

            }
        });

    }

    @Override
    public int setLayout() {
        return R.layout.activity_replace_assistant;
    }
    @OnClick({R.id.common_bar_leftBtn,R.id.tv_center_comfirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
            case R.id.tv_center_comfirm:
                if (StringUtils.textIsEmpty(etReplaceassistantCause.getText().toString())) {
                    nofifyShowMesg("请输入更换原因",null);
                    return;
                }
                if(RegUtil.containsEmoji(etReplaceassistantCause.getText().toString())){
                    nofifyShowMesg("禁止输入表情",etReplaceassistantCause);
                    return;
                }
                 submitData();
                break;
        }

    }

    /****
     * 显示提示信息
     * @param mesg
     */
    private void nofifyShowMesg(String mesg ,final EditText currentEdit){
        ConfirmDialog dialog = ConfirmDialog.newInstance("", mesg, "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        if(currentEdit==null)
                            return;
                        currentEdit.requestFocus();
                        break;
                }
            }
        });
    }

    /**
     * 提交数据
     */
    private void submitData() {
    }
}
