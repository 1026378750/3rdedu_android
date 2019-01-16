package com.shengzhe.disan.xuetangparent.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.CourseSubject;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.util.ToastUtil;
import com.shengzhe.disan.xuetangparent.mvp.activity.HomeSubjectActivity;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.OfflineClassFragment;
import com.shengzhe.disan.xuetangparent.mvp.fragment.offline.OfflineOneOnOneFragment;
import com.shengzhe.disan.xuetangparent.http.AbsAPICallback;
import com.shengzhe.disan.xuetangparent.http.Http;
import com.shengzhe.disan.xuetangparent.http.exception.ResultException;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.adapter.CourseExpandableAdapter;
import com.main.disanxuelib.bean.Subject;
import com.shengzhe.disan.xuetangparent.mvp.presenter.CommonPresenter;
import com.shengzhe.disan.xuetangparent.mvp.view.ScreenConditionView;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukui on 2017/11/27.
 */

public class ScreenConditionFragment extends BaseFragment implements ScreenConditionView.IScreenConditionView {
    @BindView(R.id.ll_popup_selectlay)
    View mSelectView;
    @BindView(R.id.rv_screen_courseclazz)
    RecyclerView mRVCourseClazz;
    @BindView(R.id.elv_screen_courseclazz)
    ExpandableListView mELVCourseClazz;
    @BindView(R.id.rv_screen_leftbtn)
    TextView leftBtn;
    @BindView(R.id.rv_screen_rightbtn)
    TextView rightBtn;

    private CommonPresenter presenter;
    private String tag = "";

    //构造fragment
    public static ScreenConditionFragment newInstance(String tag) {
        ScreenConditionFragment fragment = new ScreenConditionFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        fragment.setArguments(args);
        return fragment;
    }

    public static ScreenConditionFragment newInstance(String tag, String from) {
        ScreenConditionFragment fragment = new ScreenConditionFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        args.putString("from", from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        tag = getArguments().getString("tag");
        if (presenter==null)
            presenter = new CommonPresenter(mContext,this);
        presenter.initScreenConditionUi();
        presenter.initScreenConditionDatas(getArguments().getString("from"),tag,listener);

        switch (tag) {
            case StringUtils.select_order:
                mELVCourseClazz.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.getAreaCityByCode();
                    }
                }, 100);
                break;
            case StringUtils.select_grade:
                //年级
                mELVCourseClazz.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.getScreenConditionGradeListAll();
                    }
                }, 100);
                break;
        }
    }


    @Override
    public int setLayout() {
        return R.layout.fragment_screencondition;
    }

    @OnClick({R.id.rv_screen_leftbtn, R.id.rv_screen_rightbtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_screen_leftbtn:
                //左边按钮
                listener.tagMoreListener(v, tag, new HashMap<Integer, CourseType>());
                break;

            case R.id.rv_screen_rightbtn:
                //右边按钮
                listener.tagMoreListener(v, tag, presenter.getSelectMapDatas());
                break;
        }
    }

    public void setSelectListener(SelectListener listener) {
        this.listener = listener;
    }

    private SelectListener listener;

    @Override
    public View getSelectView() {
        return mSelectView;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRVCourseClazz;
    }

    @Override
    public ExpandableListView getExpandableListView() {
        return mELVCourseClazz;
    }

    @Override
    public TextView getLeftBtnView() {
        return leftBtn;
    }

    @Override
    public TextView getRightBtnView() {
        return rightBtn;
    }

    public interface SelectListener {
        void tagOnClickListener(View v, String tag, CourseType type, int post);

        void tagMoreListener(View v, String tag, Map<Integer, CourseType> selectOrderMap);
    }

    //接受event事件
    @Override
    public void onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11005:
                if(tag ==StringUtils.select_order)
                    presenter.getAreaCityByCode();
                break;
        }
    }

}
