package com.shengzhe.disan.xuetangteacher.mvp.activity.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.RefreshCommonView;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.HomeBean;
import com.shengzhe.disan.xuetangteacher.bean.SearchAssistantMode;
import com.shengzhe.disan.xuetangteacher.bean.User;
import com.shengzhe.disan.xuetangteacher.bean.UserAssistantMode;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/******
 * 绑定助教
 */
public class BindingAssistantActivity extends BaseActivity {

    @BindView(R.id.ccb_dollar_title)
    CommonCrosswiseBar ccbDollarTitle;
    @BindView(R.id.et_oneclazzoperator_school)
    EditText etOneclazzoperatorSchool;
    @BindView(R.id.tv_bindingassintant_select)
    TextView tvBindingassintantSelect;
    @BindView(R.id.rcv_mine_commonlayout)
    RefreshCommonView rcvMineCommonlayout;

    private SimpleAdapter adapter;
    private List<SearchAssistantMode> listSeachAssistant = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        adapter= new SimpleAdapter<SearchAssistantMode>(mContext, listSeachAssistant, R.layout.item_binding_assistant) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final SearchAssistantMode data) {
                holder.setText(R.id.tv_binding_name, TextUtils.isEmpty(data.getName()) == true ? "" : data.getName())
                        .setText(R.id.tv_binding_phone, TextUtils.isEmpty(data.getMobile()) == true ? "" : data.getMobile());
                ImageUtil.loadCircleImageView(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_binding_photoUrl), R.mipmap.ic_personal_avatar);
                holder.setOnClickListener(R.id.tv_binding, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = ConfirmDialog.newInstance("", "您确定要绑定 " + data.getName() + " 助教？", "取消", "绑定");
                        dialog.setMargin(60)
                                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                                .setOutCancel(false)
                                .show(getSupportFragmentManager());
                        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
                            @Override
                            public void dialogStatus(int id) {
                                switch (id) {
                                    case R.id.tv_dialog_ok:
                                        postBindingAssistant(data.getId());
                                        break;
                                }
                            }
                        });
                    }
                });
            }
        };
        rcvMineCommonlayout.setRecyclerViewAdapter(adapter);
        rcvMineCommonlayout.setIsLoadMore(false);
        rcvMineCommonlayout.setIsRefresh(false);
        rcvMineCommonlayout.setIsAutoLoad(false);
        rcvMineCommonlayout.setIsEmpty(true);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_binding_assistant;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.tv_bindingassintant_select})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;
            case R.id.tv_bindingassintant_select:
                if (TextUtils.isEmpty(etOneclazzoperatorSchool.getText().toString())) {
                    ToastUtil.showShort("请输入助教号码");
                    return;
                }
                if (!RegUtil.checkPhone(etOneclazzoperatorSchool.getText().toString())) {
                    ToastUtil.showShort("请输入正确的手机号码");
                    return;
                }
                postSearchAssistant();
                break;
        }

    }

    /**
     * 网络请求得到助教列表
     */
    private void postSearchAssistant() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        map.put("assistantMobile", etOneclazzoperatorSchool.getText().toString().trim());
        httpService.searchAssistant(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<SearchAssistantMode>(mContext, true) {
                    @Override
                    protected void onDone(SearchAssistantMode searchAssistantMode) {
                        listSeachAssistant.clear();
                        if (!TextUtils.isEmpty(searchAssistantMode.getName())) {
                            listSeachAssistant.add(searchAssistantMode);
                            rcvMineCommonlayout.setIsEmpty(false);
                            adapter.notifyDataSetChanged();
                        } else {
                            rcvMineCommonlayout.setIsEmpty(true);
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        listSeachAssistant.clear();
                        if(ex.getErrCode()==222222){
                            rcvMineCommonlayout.setIsEmpty(true);
                        }else {
                            rcvMineCommonlayout.setIsEmpty(true);
                        }
                    }
                });
    }

    private ConfirmDialog dialog;


    /**
     * 绑定助教
     */
    private void postBindingAssistant(final int id) {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map = new HashMap<>();
        map.put("assistantId", id);
        httpService.bindingAssistant(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<UserAssistantMode>(mContext, true) {
                    @Override
                    protected void onDone(UserAssistantMode userAssistant) {
                        if (userAssistant != null) {
                            ToastUtil.showShort("绑定成功");
                            setHomeBean(id);
                            Bundle bundle = new Bundle();
                            bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.TEA__BINDING_32011);
                            bundle.putParcelable(StringUtils.EVENT_DATA, userAssistant);
                            EventBus.getDefault().post(bundle);
                            onBackPressed();

                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if(ex.getErrCode()==222222){
                            setHomeBean(id);
                        }
                    }
                });
    }
    private void setHomeBean(long id){

        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11038);
        EventBus.getDefault().post(bundle);

        HomeBean     homeBean = SharedPreferencesManager.getHomeBean();
        homeBean.setAssistantId(id);
        SharedPreferencesManager.setHomeBean(homeBean);
        User  user= SharedPreferencesManager.getUserInfo();
        SharedPreferencesManager.setUserInfo(user);

    }

}
