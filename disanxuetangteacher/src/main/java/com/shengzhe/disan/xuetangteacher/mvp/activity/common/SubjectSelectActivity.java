package com.shengzhe.disan.xuetangteacher.mvp.activity.common;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.gson.Gson;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.PersonalDataBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 科目选择 on 2018/1/15.
 */

public class SubjectSelectActivity extends BaseActivity {
    @BindView(R.id.rv_screen_courseclazz)
    RecyclerView mRVCourseClazz;

    private List<Subject> typeList = new ArrayList<>();
    private SimpleAdapter adapter;
    private HttpService httpService;
    private PersonalDataBean userData;

    @Override
    public void initData() {
        userData = getIntent().getParcelableExtra(StringUtils.ACTIVITY_DATA);
        httpService = Http.getHttpService();
        mRVCourseClazz.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.VERTICAL));

        adapter = new SimpleAdapter<Subject>(mContext, typeList, R.layout.item_common_text) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Subject data) {
                holder.setText(R.id.rb_item_text, data.getSubjectName())
                        .setTextColor(R.id.rb_item_text,((userData==null || StringUtils.textIsEmpty(userData.getSubjectName())) && typeList.indexOf(data)==0)|| (userData!=null&&userData.getSubjectName().equals(data.getSubjectName()))?UiUtils.getColor(R.color.color_ff1d97ea):UiUtils.getColor(R.color.color_666666))
                        .setVisible(R.id.rb_item_line, typeList.indexOf(data) != typeList.size())
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11017);
                                bundle.putParcelable(StringUtils.EVENT_DATA, data);
                                EventBus.getDefault().post(bundle);
                                onBackPressed();
                            }
                        });
            }
        };
        mRVCourseClazz.setAdapter(adapter);
        postSubject();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_subjectselect;
    }

    @OnClick({R.id.common_bar_leftBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
        }
    }

    //获取首页科目集合
    private void postSubject() {
        httpService.subject()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<Subject>>(mContext, true) {
                    @Override
                    protected void onDone(List<Subject> subjectList) {
                        SharedPreferencesManager.clearSubject();
                        SharedPreferencesManager.saveSubject(new Gson().toJson(subjectList));
                        typeList.clear();
                        typeList.addAll(subjectList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                    }
                });

    }

}
