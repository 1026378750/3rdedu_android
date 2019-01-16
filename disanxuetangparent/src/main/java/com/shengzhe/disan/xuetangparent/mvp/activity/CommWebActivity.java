package com.shengzhe.disan.xuetangparent.mvp.activity;

import android.view.View;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.utils.HfFileUtil;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.shengzhe.disan.xuetangparent.view.X5WebView;
import butterknife.BindView;
import butterknife.OnClick;

/*****
 * 公共网页加载
 */
public class CommWebActivity extends BaseActivity {
    @BindView(R.id.ccb_title)
    CommonCrosswiseBar mTitle;
    @BindView(R.id.web_filechooser)
    X5WebView x5WebView;

    private String title;
    private String url = StringUtils.WEB_TYPE_url;

    @Override
    public void initData() {
        title = getIntent().getStringExtra(StringUtils.ACTIVITY_title);
        url = getIntent().getStringExtra(StringUtils.url);

        mTitle.setTitleText(title);
        switch (getIntent().getStringExtra(StringUtils.WEB_TYPE)){

            case StringUtils.WEB_TYPE_fileHtml5:
                x5WebView.loadDataWithBaseURL("file:///android_asset/", HfFileUtil.readAssetsByName(this, url, "utf-8"),"text/html","utf-8","");
                break;

            case StringUtils.WEB_TYPE_url:
                x5WebView.loadUrl(url);
                break;

            case StringUtils.WEB_TYPE_html5:
                x5WebView.loadData(url,"text/html","utf-8");
                break;

        }
    }

    @Override
    public int setLayout() {
        return R.layout.common_weblay;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (x5WebView != null) {
            x5WebView.destroy();
        }
        x5WebView.removeAllViews();
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

}
