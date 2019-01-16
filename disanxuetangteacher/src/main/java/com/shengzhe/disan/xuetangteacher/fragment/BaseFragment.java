package com.shengzhe.disan.xuetangteacher.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.main.disanxuelib.app.BaseApplication;
import com.main.disanxuelib.util.LogUtils;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import com.shengzhe.disan.xuetangteacher.utils.UmengUtils;
import com.squareup.leakcanary.RefWatcher;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*****
 * 基础
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    protected FragmentTransaction transaction = null;
    protected Context mContext;
    public Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(setLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        unbinder = ButterKnife.bind(this, view);
        if (StringUtils.EventBusFragmentNames.contains(this.getClass().getName())&&!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        lazyLoad();
    }


    /**
     * 数据初始化
     * 默认在onStart中调用
     * 建议增加数据恢复判断，配合onSaveInstanceState使用
     */
    public abstract void initData();

    /*****
     * 绑定布局
     * @return
     */
    public abstract int setLayout();

    /**
     * 显示生命周期日志
     *
     * @param showLog
     */
    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    private boolean showLog;

    public String getClassName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (showLog) {
            Log.e(getClassName(), "--onResume");
            Log.e(getClassName(), "--getActivity：" + getActivity() == null ? "null" : getActivity().toString());
        }
        UmengUtils.onResumeToFragment(mContext);
       /* if (pagePath) {
            MobclickAgent.onPageStart(getClassName());
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        if (showLog) {
            Log.e(getClassName(), "--onPause");
            Log.e(getClassName(), "--getActivity：" + getActivity() == null ? "null" : getActivity().toString());
        }
        UmengUtils.onPauseToFragment(mContext);
        /*if (pagePath) {
            MobclickAgent.onPageEnd(getClassName());
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (StringUtils.EventBusFragmentNames.contains(this.getClass().getName())) {
            EventBus.getDefault().unregister(this);
        }
        //检测内存泄漏使用
        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    //EventBus的回调方法。
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Bundle bundle) {
        LogUtils.d("");
    }

    //Fragment的View加载完毕的标记
    private boolean isViewCreated = false;
    //Fragment对用户可见的标记
    private boolean isUIVisible = false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        if (this.lazyLoadingListener==null)
            return;
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            //加载数据
            this.lazyLoadingListener.loadLazyDatas(isViewCreated && isUIVisible);
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    /****
     * 设置懒加载数据
     * @param lazyLoadingListener
     */
    protected void setLazyLoadingListener(LazyLoadingListener lazyLoadingListener){
        this.lazyLoadingListener = lazyLoadingListener;
    }

    private LazyLoadingListener lazyLoadingListener;

    /*****
     * 懒加载接口
     */
    public interface LazyLoadingListener{
        void loadLazyDatas(boolean bool);
    }

}
