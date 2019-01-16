package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SoftKeyboard;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.utils.HelpUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 帮助中心 on 2018/5/7.
 */

public class MineHelpActivity extends BaseActivity implements ConfirmDialog.ConfirmDialogListener{
    @BindView(R.id.ll_help_head)
    LinearLayout mLlhead;

    @BindView(R.id.et_head_searchtext)
    EditText mSearchEdit;

    @BindView(R.id.tv_help_login)
    TextView mLogin;

    @BindView(R.id.tv_help_course)
    TextView mCourse;

    @BindView(R.id.tv_help_wallet)
    TextView mWallet;

    @BindView(R.id.tv_help_teacher)
    TextView mTeacher;

    @BindView(R.id.tv_help_back)
    TextView tvHelpBack;

    @BindView(R.id.ccb_help_buindzj)
    CommonCrosswiseBar ccbHelpBuindzj;

    @BindView(R.id.tv_help_buindzj)
    TextView tvHelpBuindzj;

    @BindView(R.id.ccb_help_search)
    CommonCrosswiseBar ccbHelpSearch;

    @BindView(R.id.tv_help_search)
    TextView tvHelpSearch;

    @BindView(R.id.ccb_help_backcourse)
    CommonCrosswiseBar ccbHelpBackcourse;

    @BindView(R.id.tv_help_backcourse)
    TextView tvHelpBackcourse;

    @BindView(R.id.ccb_help_cash)
    CommonCrosswiseBar ccbHelpCash;

    @BindView(R.id.tv_help_cash)
    TextView tvHelpCash;

    private ConfirmDialog dialog;

    @Override
    public void initData() {
        mLlhead.setPadding(mLlhead.getPaddingLeft(), mLlhead.getPaddingTop() + SystemInfoUtil.getStatusBarHeight(), mLlhead.getPaddingRight(), mLlhead.getPaddingBottom());
        ImageUtil.setCompoundDrawable(mLogin, 30, R.mipmap.btn_help_login, Gravity.TOP, 8);
        ImageUtil.setCompoundDrawable(mCourse, 30, R.mipmap.btn_help_course, Gravity.TOP, 8);
        ImageUtil.setCompoundDrawable(mWallet, 30, R.mipmap.btn_help_wallet, Gravity.TOP, 8);
        ImageUtil.setCompoundDrawable(mTeacher, 30, R.mipmap.btn_help_zujiao, Gravity.TOP, 8);
        tvHelpBuindzj.setText(HelpUtil.getInstance().getHelpZhuJiao().helpList.get(0).context);
        tvHelpSearch.setText(HelpUtil.getInstance().getHelpDaoZhang().helpList.get(0).context);
        tvHelpBackcourse.setText(HelpUtil.getInstance().getHelpTuiKe().helpList.get(0).context);
        tvHelpCash.setText(HelpUtil.getInstance().getHelpCash().helpList.get(0).context);
        //监听软键盘搜索点击
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                    seacherDatas();
                    return true;
                }
                return false;
            }
        });
    }

    private void seacherDatas() {
        if (StringUtils.textIsEmpty(mSearchEdit.getText().toString().trim())) {
            ToastUtil.showShort("请输入您需要查询的关键字");
            return;
        }
        if (AppManager.getAppManager().isTopActivity(HelpSearcherActivity.class))
            return;
        //关闭键盘
        SoftKeyboard.hide(mSearchEdit);
        Intent intent = new Intent(mContext, HelpSearcherActivity.class);
        intent.putExtra(StringUtils.ACTIVITY_DATA,mSearchEdit.getText().toString());
        startActivity(intent);
    }


    @Override
    public int setLayout() {
        return R.layout.activity_minehelp;
    }

    @OnClick({R.id.tv_help_back, R.id.tv_help_login, R.id.tv_help_course, R.id.tv_help_wallet,
            R.id.tv_help_teacher, R.id.ccb_help_buindzj, R.id.ccb_help_search, R.id.ccb_help_backcourse, R.id.ccb_help_cash,R.id.iv_minehelp_tell})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_help_back:
                onBackPressed();
                break;

            case R.id.iv_minehelp_tell:
                //电话
                if(dialog==null) {
                    dialog = ConfirmDialog.newInstance("", "您确定要拨打客服电话<br/><font color='#FFAE12'>"+ StringUtils.System_Service_Phone+"</font>？", "取消", "立即拨打");
                }
                dialog.setMargin(60)
                        .setWidth(SystemInfoUtil.getScreenWidth()*2/3)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                dialog.setConfirmDialogListener(this);
                break;

            case R.id.tv_help_login:
                //注册登录
                Intent intentLogin = new Intent(mContext, HelpDetailActivity.class);
                intentLogin.putExtra(StringUtils.ACTIVITY_DATA, HelpUtil.getInstance().getHelpLogin());
                mContext.startActivity(intentLogin);
                break;

            case R.id.tv_help_course:
                //课程管理
                Intent intentCourse = new Intent(mContext, HelpDetailActivity.class);
                intentCourse.putExtra(StringUtils.ACTIVITY_DATA, HelpUtil.getInstance().getHelpCourse());
                mContext.startActivity(intentCourse);
                break;

            case R.id.tv_help_wallet:
                //我的钱包
                Intent intentWallet = new Intent(mContext, HelpDetailActivity.class);
                intentWallet.putExtra(StringUtils.ACTIVITY_DATA, HelpUtil.getInstance().getHelpWallet());
                mContext.startActivity(intentWallet);
                break;

            case R.id.tv_help_teacher:
                //我的助教
                Intent intentZhuJiao = new Intent(mContext, HelpDetailActivity.class);
                intentZhuJiao.putExtra(StringUtils.ACTIVITY_DATA, HelpUtil.getInstance().getHelpZhuJiao());
                mContext.startActivity(intentZhuJiao);
                break;

            case R.id.ccb_help_buindzj:
                //绑定助教
                tvHelpBuindzj.setVisibility(tvHelpBuindzj.isShown() ? View.GONE : View.VISIBLE);
                tvHelpSearch.setVisibility(View.GONE);
                tvHelpBackcourse.setVisibility(View.GONE);
                tvHelpCash.setVisibility(View.GONE);
                ccbHelpBuindzj.setRightButton(R.mipmap.ic_black_down_arrow);
                ccbHelpSearch.setRightButton(R.mipmap.ic_black_right_arrow);
                ccbHelpCash.setRightButton(R.mipmap.ic_black_right_arrow);
                ccbHelpBackcourse.setRightButton(R.mipmap.ic_black_right_arrow);
                if (!tvHelpBuindzj.isShown()) {
                    ccbHelpBuindzj.setRightButton(R.mipmap.ic_black_right_arrow);
                }
                break;
            case R.id.ccb_help_search:
                //账情况
                tvHelpBuindzj.setVisibility(View.GONE);
                tvHelpSearch.setVisibility(tvHelpSearch.isShown() ? View.GONE : View.VISIBLE);
                tvHelpBackcourse.setVisibility(View.GONE);
                tvHelpCash.setVisibility(View.GONE);
                ccbHelpBuindzj.setRightButton(R.mipmap.ic_black_right_arrow);
                ccbHelpSearch.setRightButton(R.mipmap.ic_black_down_arrow);
                ccbHelpCash.setRightButton(R.mipmap.ic_black_right_arrow);
                ccbHelpBackcourse.setRightButton(R.mipmap.ic_black_right_arrow);
                if (!tvHelpSearch.isShown()) {
                    ccbHelpSearch.setRightButton(R.mipmap.ic_black_right_arrow);
                }
                break;

            case R.id.ccb_help_backcourse:
                //线下1对1和线下班课的调课与退课问题
                tvHelpBuindzj.setVisibility(View.GONE);
                tvHelpSearch.setVisibility(View.GONE);
                tvHelpBackcourse.setVisibility(tvHelpBackcourse.isShown() ? View.GONE : View.VISIBLE);
                tvHelpCash.setVisibility(View.GONE);
                ccbHelpBuindzj.setRightButton(R.mipmap.ic_black_right_arrow);
                ccbHelpSearch.setRightButton(R.mipmap.ic_black_right_arrow);
                ccbHelpCash.setRightButton(R.mipmap.ic_black_right_arrow);
                ccbHelpBackcourse.setRightButton(R.mipmap.ic_black_down_arrow);
                if (!tvHelpBackcourse.isShown()) {
                    ccbHelpBackcourse.setRightButton(R.mipmap.ic_black_right_arrow);
                }
                break;

            case R.id.ccb_help_cash:
                //提现多久能到账
                tvHelpCash.setVisibility(tvHelpCash.isShown() ? View.GONE : View.VISIBLE);
                tvHelpBuindzj.setVisibility(View.GONE);
                tvHelpSearch.setVisibility(View.GONE);
                tvHelpBackcourse.setVisibility(View.GONE);
                ccbHelpBuindzj.setRightButton(R.mipmap.ic_black_right_arrow);
                ccbHelpSearch.setRightButton(R.mipmap.ic_black_right_arrow);
                ccbHelpCash.setRightButton(R.mipmap.ic_black_down_arrow);
                ccbHelpBackcourse.setRightButton(R.mipmap.ic_black_right_arrow);
                if (!tvHelpCash.isShown()) {
                    ccbHelpCash.setRightButton(R.mipmap.ic_black_right_arrow);
                }
                break;
        }
    }

    @Override
    public void dialogStatus(int id) {
        switch (id){
            case R.id.tv_dialog_ok:
                //确定
                SystemInfoUtil.callDialing(StringUtils.System_Service_Phone);
                break;
        }
    }
}
