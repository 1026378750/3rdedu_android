package com.shengzhe.disan.xuetangteacher.mvp.model;

import android.content.Context;
import com.main.disanxuelib.view.banner.BannerBean;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.bean.HomeBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;

import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/27.
 */

public class HomeModelImpl extends BaseModelImpl {

    public HomeModelImpl(Context context, MVPRequestListener listener) {
        super(context, listener);
    }

    /**
     * 获取首页上的数据
     */
    public void postHomeData() {
        getHttpService().homeData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<HomeBean>(getContext(), false) {
                    @Override
                    protected void onDone(HomeBean homeBeans) {
                        getListener().onSuccess(IntegerUtil.WEB_API_HomeData, homeBeans);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        getListener().onFailed(IntegerUtil.WEB_API_HomeData, ex.getMessage());
                    }
                });
    }


    /**
     * 获取首页banner
     */
    public void postBanner() {
        getHttpService().banner()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<BannerBean>>(getContext(), false) {
                    @Override
                    protected void onDone(List<BannerBean> banner) {
                        getListener().onSuccess(IntegerUtil.WEB_API_TeacherBanner, banner);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        getListener().onFailed(IntegerUtil.WEB_API_TeacherBanner, ex.getMessage());
                    }
                });
    }
}
