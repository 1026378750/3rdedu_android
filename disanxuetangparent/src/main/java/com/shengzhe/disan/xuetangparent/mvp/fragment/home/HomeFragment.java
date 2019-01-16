package com.shengzhe.disan.xuetangparent.mvp.fragment.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.MyFloatingActionButton;
import com.main.disanxuelib.view.banner.BannerBean;
import com.main.disanxuelib.view.banner.MyBanner;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.mbg.library.IRefreshListener;
import com.mbg.library.RefreshRelativeLayout;
import com.shengzhe.disan.xuetangparent.mvp.view.MainView;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.mvp.activity.OpenCityActivity;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.mvp.activity.HeadSearchActivity;
import com.shengzhe.disan.xuetangparent.fragment.BaseFragment;
import com.shengzhe.disan.xuetangparent.bean.City;
import com.shengzhe.disan.xuetangparent.mvp.activity.ApplyAuditionActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.MineCourseActivity;
import com.shengzhe.disan.xuetangparent.mvp.presenter.MainPresenter;
import com.shengzhe.disan.xuetangparent.utils.HtmlJumpUtil;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

/*****
 * 首页
 * liukui
 */
public class HomeFragment extends BaseFragment implements MainView.IMainView, MyBanner.BannerItemOnClickListener, IRefreshListener {
    @BindView(R.id.rrl_refresh_layout)
    RefreshRelativeLayout mRefreshLayout;
    @BindView(R.id.rbl_top_layout)
    AppBarLayout mBarLayout;
    @BindView(R.id.top_line)
    View topLine;
    @BindView(R.id.tx_main_choosecity)
    TextView mChooseCity;
    @BindView(R.id.tx_main_searchlayout)
    RelativeLayout mSsearchlayout;
    @BindView(R.id.tx_main_search)
    TextView mSearch;
    @BindView(R.id.top_layout)
    RelativeLayout topLayout;
    @BindView(R.id.mb_home_banner)
    MyBanner myBanner;
    @BindView(R.id.rv_home_courseclazz)
    RecyclerView rvCourseClazz;
    @BindView(R.id.rl_home_notify_layout)
    View notifyLayout;
    @BindView(R.id.tv_home_notify)
    TextView tvNotify;
    @BindView(R.id.tv_home_recommend)
    TextView tvRecommend;
    @BindView(R.id.rv_home_recommend)
    RecyclerView rvRecommend;
    @BindView(R.id.tv_home_oneone)
    TextView tvOneToOne;
    @BindView(R.id.rc_home_oneone)
    RecyclerView rvOneToOne;
    @BindView(R.id.iv_apply_audition)
    MyFloatingActionButton mFloatingButton;
    @BindView(R.id.common_refresh_none)
    View mNoneView;
    @BindView(R.id.nsv_scroll_layout)
    NestedScrollView mMainView;

    private MainPresenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initData() {
        if (presenter == null)
            presenter = new MainPresenter(mContext, this);
        presenter.initMainUi();
        presenter.initMainDatas(this, this);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_home;
    }

    @Override
    @OnClick({R.id.tx_main_choosecity, R.id.tv_home_notify, R.id.tv_home_recommend, R.id.iv_home_brand, R.id.iv_home_onlive, R.id.tx_main_searchlayout, R.id.iv_apply_audition, R.id.iv_home_notify_close})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_home_notify_close:
                presenter.setNotifyFloat();
                break;

            case R.id.tv_home_notify:
                //弹出消息点击入口
                notifyLayout.setVisibility(View.GONE);
                Intent mineCourseIntent = new Intent(mContext, MineCourseActivity.class);
                mineCourseIntent.putExtra(StringUtils.FRAGMENT_INDEX, 1);
                startActivity(mineCourseIntent);
                break;

            case R.id.tx_main_choosecity:
                //选择地址
                Intent intent = new Intent(mContext, OpenCityActivity.class);
                intent.putExtra(StringUtils.activcity_from, HomeFragment.class.getName());
                startActivity(intent);
                break;

            case R.id.tx_main_searchlayout:
                //搜索
                startActivity(new Intent(mContext, HeadSearchActivity.class));
                break;

            case R.id.tv_home_recommend:
                //推荐班课更多
                Bundle recommendBundle = new Bundle();
                recommendBundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11020);
                EventBus.getDefault().post(recommendBundle);
                break;

            case R.id.iv_home_brand:
                //品牌课
                Bundle brandBundle = new Bundle();
                brandBundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11018);
                EventBus.getDefault().post(brandBundle);
                break;

            case R.id.iv_home_onlive:
                //直播课
                Bundle onliveBundle = new Bundle();
                onliveBundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11019);
                EventBus.getDefault().post(onliveBundle);
                break;

            case R.id.iv_apply_audition:
                //申请试听入口
                if (SharedPreferencesManager.getUserInfo() != null && SharedPreferencesManager.getUserInfo().getIsApplyCourseListen() == 2) {
                    ConfirmDialog dialog = ConfirmDialog.newInstance("", "您已经申请过了，助教稍后会为您处理，<br/>" +
                            "请您耐心等待", "", "确定");
                    dialog.setMessageGravity(Gravity.LEFT);
                    dialog.setMargin(60)
                            .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                            .setOutCancel(false)
                            .show(getChildFragmentManager());
                } else {
                    if (TextUtils.isEmpty(ConstantUrl.TOKN)) {
                        ToastUtil.showShort("请登录");
                    }
                    startActivity(new Intent(mContext, ApplyAuditionActivity.class));
                }
                break;
        }
    }

    @Override
    public RefreshRelativeLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public AppBarLayout getBarLayout() {
        return mBarLayout;
    }

    @Override
    public MyBanner getMyBanner() {
        return myBanner;
    }

    @Override
    public TextView getChooseCity() {
        return mChooseCity;
    }

    @Override
    public RelativeLayout getTopLayout() {
        return topLayout;
    }

    @Override
    public RecyclerView getCourseClazz() {
        return rvCourseClazz;
    }

    @Override
    public View getNotify() {
        return notifyLayout;
    }

    @Override
    public TextView getNotifyText() {
        return tvNotify;
    }

    @Override
    public TextView getTvRecommend() {
        return tvRecommend;
    }

    @Override
    public RecyclerView getRVRecommend() {
        return rvRecommend;
    }

    @Override
    public TextView getTvOneToOne() {
        return tvOneToOne;
    }

    @Override
    public RecyclerView getRVOneToOne() {
        return rvOneToOne;
    }

    @Override
    public TextView getSearchView() {
        return mSearch;
    }

    @Override
    public RelativeLayout getSsearchLayout() {
        return mSsearchlayout;
    }

    @Override
    public View getTopLinerView() {
        return topLine;
    }

    @Override
    public MyFloatingActionButton getFloatButton() {
        return mFloatingButton;
    }

    @Override
    public View getNoneView() {
        return mNoneView;
    }

    @Override
    public NestedScrollView getMainView() {
        return mMainView;
    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11005:
                //地址选择
                City city = bundle.getParcelable(StringUtils.EVENT_DATA);
                mChooseCity.setText(city.cityName);
                pageNum = 1;
                presenter.loadMainRefreshDatas(pageNum);
                break;

            case IntegerUtil.EVENT_ID_11017:
                //申请试听
                if (TextUtils.isEmpty(ConstantUrl.TOKN)) {
                    return;
                }
                startActivity(new Intent(getActivity(), ApplyAuditionActivity.class));
                break;

        }
    }

    @Override
    public void bannerOnItemClick(View view, BannerBean banner) {
        if (banner != null && (!TextUtils.isEmpty(banner.getPicLink()))) {
            HtmlJumpUtil.bannerActivity(banner);
        }
    }

    private int pageNum = 1;

    @Override
    public void onPositiveRefresh() {
        pageNum = 1;
        presenter.loadMainRefreshDatas(pageNum);
    }

    @Override
    public void onNegativeRefresh() {
        pageNum++;
        presenter.loadMainLoadMoreDatas(pageNum);
    }
}
