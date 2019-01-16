package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/22.
 */

public class ModifyMessageActivity extends BaseActivity {
    @BindView(R.id.ccb_modify_title)
    CommonCrosswiseBar title;
    @BindView(R.id.tv_maxeducation)
    TextView mMaxEducation;
    @BindView(R.id.et_modify_content)
    EditText content;
    @BindView(R.id.tv_major)
    EditText mMajor;
    @BindView(R.id.ll_modify_introlayout)
    LinearLayout mIntroLayout;
    @BindView(R.id.et_modify_intro)
    EditText mIntro;
    @BindView(R.id.tv_modify_intro)
    TextView mIntroNum;
    @BindView(R.id.ll_modify_agelayout)
    LinearLayout mAgeLayout;
    @BindView(R.id.ll_modify_age)
    EditText mAge;

    @BindView(R.id.et_modify_reminder)
    TextView reminder;

    private Intent intent;
    private String mTitle;

    @Override
    public void initData() {
        intent = getIntent();
        mTitle = intent.getStringExtra("title");
        title.setTitleText(mTitle);
        content.setHint(intent.getStringExtra("hintText"));
        content.setText(intent.getStringExtra("text"));
        //设置光标
        Selection.setSelection(content.getText(), content.getText().toString().length());

        if (mTitle.equals("毕业院校")) {
            mMaxEducation.setVisibility(View.VISIBLE);
            mMajor.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(intent.getStringExtra("profession"))) {
                mMajor.setText(intent.getStringExtra("profession"));
                mMajor.setSelection(intent.getStringExtra("profession").length());
            }
            if (intent.getIntExtra("edu", 5) != 5) {
                //"博士","硕士","本科","专科"
                switch (intent.getIntExtra("edu", 5)) {
                    case 4:
                        mMaxEducation.setText("博士");
                        postion = 4;
                        break;
                    case 3:
                        mMaxEducation.setText("硕士");
                        postion = 3;
                        break;
                    case 2:
                        mMaxEducation.setText("本科");
                        postion = 2;
                        break;
                    case 1:
                        mMaxEducation.setText("专科");
                        postion = 1;
                        break;
                }
            }
            reminder.setVisibility(View.GONE);
        } else if (mTitle.equals("个人简介")) {
            mIntroLayout.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            reminder.setVisibility(View.GONE);
            mIntro.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //50-200
                    count += start;
                    mIntroNum.setText(count == 0 ? "限50-200字" : count > 200 ? "-" + (200 - count) + "字" : count + "字");
                    mIntroNum.setTextColor(UiUtils.getColor(count > 0 && (count < 50 || count > 200) ? R.color.color_ca4341 : R.color.color_999999));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            if (!TextUtils.isEmpty(intent.getStringExtra("text"))) {
                mIntro.setText(intent.getStringExtra("text"));
                mIntro.setSelection(intent.getStringExtra("text").length());
            }
        } else if (mTitle.equals("教龄")) {
            if (!TextUtils.isEmpty(intent.getStringExtra("text"))) {
                mAge.setText(intent.getStringExtra("text"));
                mAge.setSelection(intent.getStringExtra("text").length());
            }
            mAgeLayout.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            reminder.setVisibility(View.GONE);
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_modifymessage;
    }

    private int postion;
    private NiceDialog niceDialog;

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_maxeducation, R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.tv_maxeducation:
                //选择最高学历
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
                    @Override
                    public void onItemListener(int index, String item) {
                        mMaxEducation.setText(item);
                        switch (item) {
                            case "博士":
                                postion = 4;
                                break;
                            case "硕士":
                                postion = 3;
                                break;
                            case "本科":
                                postion = 2;
                                break;
                            case "专科":
                                postion = 1;
                                break;
                        }
                    }
                });
                niceDialog.setCommonLayout(new String[]{"博士", "硕士", "本科", "专科"}, true, getSupportFragmentManager());
                break;


            case R.id.common_bar_rightBtn:
                //保存
                Bundle bundle = new Bundle();
                switch (intent.getStringExtra("title")) {
                    case "昵称":
                        if (!RegUtil.isUsername(content.getText().toString().trim())) {
                            nofifyShowMesg("昵称仅支持15个汉字或32个字母", content);
                            return;
                        }
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11031);
                        bundle.putString("result", content.getText().toString().trim());
                        EventBus.getDefault().post(bundle);
                        onBackPressed();
                        break;

                    case "毕业院校":
                        if(TextUtils.isEmpty(mMaxEducation.getText().toString().trim())){
                            //毕业院校
                            nofifyShowMesg("请选择最高学历", null);
                            return;
                        }
                        if(TextUtils.isEmpty(content.getText().toString().trim())){
                            nofifyShowMesg("请填写毕业院校", content);
                            return;
                        }
                        if(TextUtils.isEmpty(mMajor.getText().toString().trim())){
                            nofifyShowMesg("请填写专业", mMajor);
                            return;
                        }

                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11033);
                        bundle.putString("geaduateSchool", content.getText().toString().trim());
                        bundle.putString("profession", mMajor.getText().toString().trim());
                        bundle.putString("edu", mMaxEducation.getText().toString().trim());
                        bundle.putInt("postion", postion);
                        EventBus.getDefault().post(bundle);
                        onBackPressed();
                        break;

                    case "教龄":
                        if (StringUtils.textIsEmpty(mAge.getText().toString())) {
                            nofifyShowMesg("请输入教龄1~99", mIntro);
                            return;
                        }
                        if (mAge.getText().toString().length() < 0 || mAge.getText().toString().length() > 99||mAge.getText().toString().trim().equals("0")) {
                            nofifyShowMesg("请输入教龄1~99", mIntro);
                            return;
                        }
                        int mAges=Integer.parseInt(mAge.getText().toString().trim());
                        if(mAges < 1 || mAges > 99){
                            nofifyShowMesg("请输入教龄1~99", mIntro);
                            return;
                        }
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11034);
                        bundle.putString("teachingAge", mAge.getText().toString().trim());
                        EventBus.getDefault().post(bundle);
                        onBackPressed();
                        break;

                    case "个人简介":
                        if (StringUtils.textIsEmpty(mIntro.getText().toString())) {
                            nofifyShowMesg("请输入个人简介", mIntro);
                            return;
                        }
                        if (mIntro.getText().toString().length() < 50 || mIntro.getText().toString().length() > 200) {
                            nofifyShowMesg("个人简介字数范围为50~200字", mIntro);
                            return;
                        }

                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11032);
                        bundle.putString("personalResume", mIntro.getText().toString().trim());
                        EventBus.getDefault().post(bundle);
                        onBackPressed();
                        break;
                }
                break;
        }
    }

    /****
     * 显示提示信息
     * @param mesg
     */
    private void nofifyShowMesg(String mesg, final EditText currentEdit) {
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
                        if (currentEdit == null)
                            return;
                        currentEdit.requestFocus();
                        break;
                }
            }
        });
    }

}
