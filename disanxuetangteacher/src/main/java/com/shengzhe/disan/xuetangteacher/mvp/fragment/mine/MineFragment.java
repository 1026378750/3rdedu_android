package com.shengzhe.disan.xuetangteacher.mvp.fragment.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MineHelpActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.PersonalDataBean;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.CertifyCenterActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.IdentityCardActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MineCenterActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MineDollarsActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MineMessageActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.order.MineOrderActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.mine.MinesSistantActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.SettingActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.StartClassActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.teacher.TeacherPagerActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的
 */
public class MineFragment extends BaseFragment implements RefreshCommonView.RefreshLoadMoreListener {
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView refreshCommonView;
    private ImageView ivHeadimage;
    private TextView tvNicename;
    private TextView tvPhonenum;
    private CommonCrosswiseBar ccbNews;
    private HttpService httpService;
    private PersonalDataBean userBean;
    private ImageView point;
    private ImageView mMineIsplant;

    @Override
    public void initData() {
        refreshCommonView.addScrollViewChild(R.layout.fragment_mine_child);
        ivHeadimage = (ImageView) refreshCommonView.findViewById(R.id.iv_headimage);
        tvNicename = (TextView) refreshCommonView.findViewById(R.id.tv_nicename);
        tvPhonenum = (TextView) refreshCommonView.findViewById(R.id.tv_phonenum);
        mMineIsplant = (ImageView) refreshCommonView.findViewById(R.id.iv_mine_isplant);
        ccbNews = (CommonCrosswiseBar) refreshCommonView.findViewById(R.id.ccb_news);
        point = ccbNews.getRightImage();
        refreshCommonView.findViewById(R.id.ll_mine_usercenter).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.tv_mineorder).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.tv_dollars).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.ccb_information).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.ccb_open_class).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.ccb_setting).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.ccb_certificate_authority).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.ccb_assistant).setOnClickListener(this);
        refreshCommonView.findViewById(R.id.ccb_help).setOnClickListener(this);
        ccbNews.setOnClickListener(this);

        httpService = Http.getHttpService();
        refreshCommonView.setIsLoadMore(false);
        refreshCommonView.setRefreshLoadMoreListener(this);

        point.setImageResource(R.drawable.point_select);
    }

    /**
     * 个人资料页
     */
    private void postPersonalData() {
        httpService.personalData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<PersonalDataBean>(mContext, true) {
                    @Override
                    protected void onDone(PersonalDataBean personalDatabBeans) {
                        refreshCommonView.finishRefresh();
                        if (personalDatabBeans != null) {
                            userBean = personalDatabBeans;
                            SharedPreferencesManager.setPersonaInfo(new Gson().toJson(personalDatabBeans));
                            setPersonalDatabBean();
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        refreshCommonView.finishRefresh();
                    }
                });
    }

    /***
     * 个人资料页
     */
    private void setPersonalDatabBean() {
        if (TextUtils.isEmpty(userBean.getNickName())) {
            tvPhonenum.setText("未填写");
        } else {
            String teacherAge = "";
            if (userBean.getTeachingAge() != 0) {
                teacherAge += " | " + userBean.getTeachingAge() + "年教龄";
            }
            tvPhonenum.setText(StringUtils.getSex(userBean.getSex()) + " | " + userBean.getSubjectName() + teacherAge);
            tvNicename.setText(userBean.getNickName());
            SharedPreferencesManager.setSubjectName(userBean.getSubjectName());
        }
        tvNicename.setText(StringUtils.textIsEmpty(userBean.getNickName()) ? SharedPreferencesManager.getPhoneNum() : userBean.getNickName());
        mMineIsplant.setVisibility(SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIdentity() == 0 ? View.GONE : View.VISIBLE);
        ImageUtil.loadCircleImageView(mContext, userBean.getPhotoUrl(), ivHeadimage, R.mipmap.ic_personal_avatar);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_mine;
    }

    private ConfirmDialog dialog;

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.ll_mine_usercenter:
                //用户个人中心
                if (!judgeTeacherStatus())
                    return;
               // ConstantUrl.IS_EDIT = true;
                ConstantUrl.IS_EDIT = false;
                //startActivity(new Intent(mContext, TeacherNewPagerActivity.class));
                startActivity(new Intent(mContext, TeacherPagerActivity.class));
                break;
            case R.id.ccb_assistant:
                //我的助教
                ConstantUrl.CLIEN_Center = true;
                if (!judgeStatus()) return;
                startActivity(new Intent(mContext, MinesSistantActivity.class));

                break;
            case R.id.tv_mineorder:
                //我的订单
                startActivity(new Intent(mContext, MineOrderActivity.class));
                break;
            case R.id.tv_dollars:
                //我的钱包
                startActivity(new Intent(mContext, MineDollarsActivity.class));
                break;

            case R.id.ccb_information:
                //我的资料
                ConstantUrl.CLIEN_Center = false;
                Intent intent = new Intent(mContext, MineCenterActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, StringUtils.btn_is_save);
                startActivity(intent);
                break;

            case R.id.ccb_certificate_authority:
                //我认证中心
                startActivity(new Intent(mContext, CertifyCenterActivity.class));
                break;

            case R.id.ccb_open_class:
                if (!judgeTeacherStatus()){
                    return;
                }
                //开课设置
                if ((SharedPreferencesManager.getUserInfo() != null) && SharedPreferencesManager.getOpenCity() <= 1) {
                    dialog = ConfirmDialog.newInstance("", "您当前所选的城市未开通，不能填写开课设置您可以联系客服修改城市", "取消", "联系客服");
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getChildFragmentManager());
                    dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
                        @Override
                        public void dialogStatus(int id) {
                            switch (id) {
                                case R.id.tv_dialog_ok:
                                    SystemInfoUtil.callDialing("400005666");
                                    break;
                            }
                        }
                    });

                    return;
                }
                startActivity(new Intent(mContext, StartClassActivity.class));
                break;

            case R.id.ccb_news:
                //我的消息
                startActivity(new Intent(mContext, MineMessageActivity.class));
                break;

            case R.id.ccb_help:
                //帮助中心
                startActivity(new Intent(mContext, MineHelpActivity.class));
                break;

            case R.id.ccb_setting:
                //设置
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }
    }

    /**
     * 判断老师主页状态
     */
    public boolean judgeTeacherStatus() {
        /*  //消息关闭
        if (ConstantUrl.homebean == null)
            return false;*/
        if (ConstantUrl.homebean.getHomeStatus() == 0) {
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA, StringUtils.btn_is_next);
            startActivity(intent);
            return false;
        }
        return true;
    }

    /**
     * 判断状态
     */
    public boolean judgeStatus() {
    /*    //消息关闭
        if (ConstantUrl.homebean == null)
            return false;*/
        if (ConstantUrl.homebean.getHomeStatus() == 0) {
            //完善资料
            Intent intent = new Intent(mContext, MineCenterActivity.class);
            intent.putExtra(StringUtils.ACTIVITY_DATA, StringUtils.btn_is_next);
            startActivity(intent);
            return false;
        }
        //实名认证状态 0 未认证，1 审核中，2 已认证，3 已驳回
        switch (ConstantUrl.homebean.getRealNameAuthStatus()) {
            case 0:
            case 3:
                //实名认证
                startActivity(new Intent(mContext, IdentityCardActivity.class));
                return false;
        }
        return true;
    }

    //我的消息量
    private void myMsgNum() {
        httpService = Http.getHttpService();
        httpService.myCenter()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, false) {
                    @Override
                    protected void onDone(String messages) {
                        //	我的消息数量，为0表示没有
                        int msgNum = 0;
                        try {
                            msgNum = new JSONObject(messages).optInt("msgNum");
                        } catch (JSONException e) {

                        }
                        point.setVisibility(msgNum > 0 ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onResultError(ResultException ex) {

                    }
                });
    }


    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11006:
                if (!bundle.getBoolean(StringUtils.EVENT_DATA))
                    break;
            case IntegerUtil.EVENT_ID_11002:
            case IntegerUtil.EVENT_ID_11013:
                refreshCommonView.notifyData();
                break;

        }
    }

    @Override
    public void startRefresh() {
        postPersonalData();
        myMsgNum();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void startLoadMore() {

    }
}
