package com.shengzhe.disan.xuetangteacher.mvp.activity.teacher;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.bean.ShareBean;
import com.main.disanxuelib.util.ShareUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangteacher.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangteacher.http.UrlHelper;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.CourseDetailActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OfflineDetailActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.course.OnLiveDetailActivity;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import com.shengzhe.disan.xuetangteacher.view.X5WebView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/14.
 */

public class TeacherNewPagerActivity extends BaseActivity implements ShareUtil.ShareSelectListener{
    @BindView(R.id.web_newteacherpager)
    X5WebView x5WebView;
    @BindView(R.id.ccb_teacher_title)
    CommonCrosswiseBar mCcbTeacherTitle;

    private SystemPersimManage manage = null;
    private Gson gson = new Gson();
    private boolean showList = false;

    private ShareUtil shareUtil;

    @Override
    public void initData() {
        String url = UrlHelper.PUBLIC_URL + "&token=" + SharedPreferencesManager.getUserToken() + "&userDifferentiation=1";
        x5WebView.loadUrl("file:///android_asset/teacherDetail.html?linkUrl=" + url);
        x5WebView.addJavascriptInterface(this, "android");

        if (SharedPreferencesManager.getPersonaInfo() != null) {
            mCcbTeacherTitle.setTitleText(SharedPreferencesManager.getPersonaInfo().getNickName());
        }

    }

    @JavascriptInterface
    public void setLineClassMessage(String json) {
        CourseLiveBean person = gson.fromJson(json, CourseLiveBean.class);
        Intent intent = new Intent(mContext, CourseDetailActivity.class);
        intent.putExtra(StringUtils.ACTIVITY_DATA, person.courseId);
        startActivity(intent);
    }

    @JavascriptInterface
    public void setOnLineMessage(String json) {
        CourseLiveBean person = gson.fromJson(json, CourseLiveBean.class);
        Intent intent = new Intent(mContext, OnLiveDetailActivity.class);
        intent.putExtra(StringUtils.ACTIVITY_DATA, person);
        startActivity(intent);
    }

    @JavascriptInterface
    public void setTeacherMessage() {
        showList = true;
    }

    @JavascriptInterface
    public void setMessage(String json) {
        CourseOflineBean person = gson.fromJson(json, CourseOflineBean.class);
        Intent intent = new Intent(mContext, OfflineDetailActivity.class);
        intent.putExtra(StringUtils.ACTIVITY_DATA, person);
        startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_teacher_new_pager;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.common_bar_rightBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                if (showList) {
                    showList = false;
                    if (x5WebView.canGoBack()) {
                        x5WebView.goBack();
                        return;
                    }
                } else {
                    onBackPressed();
                }
                break;

            case R.id.common_bar_rightBtn:
                //分享
                shareUtil = ShareUtil.getInsatnce();
                shareUtil.setShareLogo(R.mipmap.ic_launcher_logo)
                        .initDatas(mContext, getSupportFragmentManager())
                        .setExtoryBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.share_parent))
                        .show(new ShareBean(0, "第三学堂老师分享", "", "", "", 0))
                        .setShareSelectListener(TeacherNewPagerActivity.this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (x5WebView.canGoBack()) {
            x5WebView.goBack();
            return;
        }
        finish();
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IntegerUtil.PERMISSION_REQUEST_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                shareUtil.shareImage(mesg);
            } else {
                showPremissionDialog("访问设备存储空间");
            }
        }
    }


    @Override
    public void selectShareItem(String mesg) {
        ToastUtil.showShort(mesg);
    }

    private String mesg = "";
    @Override
    public void selectLoggPicture(String mMesg) {
        this.mesg = mMesg;
        if (manage == null)
            manage = new SystemPersimManage(mContext) {
                @Override
                public void resultPerm(boolean isCan, int requestCode) {
                    if (isCan)
                        shareUtil.shareImage(mesg);
                }
            };
        manage.CheckedFile();
    }
}
