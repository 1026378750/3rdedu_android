package com.shengzhe.disan.xuetangparent.mvp.activity;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.main.disanxuelib.app.SystemPersimManage;
import com.main.disanxuelib.bean.ShareBean;
import com.main.disanxuelib.util.ShareUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.activity.BaseActivity;
import com.shengzhe.disan.xuetangparent.bean.CourseLiveBean;
import com.shengzhe.disan.xuetangparent.bean.CourseOflineBean;
import com.shengzhe.disan.xuetangparent.http.UrlHelper;
import com.shengzhe.disan.xuetangparent.mvp.activity.CourseDetailActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.LiveCourseActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineOneonOneDetailsActivity;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.utils.IntegerUtil;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import com.shengzhe.disan.xuetangparent.view.X5WebView;

import butterknife.BindView;
import butterknife.OnClick;

public class TeacherNewPagerActivity extends BaseActivity implements ShareUtil.ShareSelectListener{
    @BindView(R.id.web_newteacherpager)
    X5WebView x5WebView;
    @BindView(R.id.ccb_teacher_title)
    CommonCrosswiseBar mCcbTeacherTitle;

    private SystemPersimManage manage = null;
    private Gson gson = new Gson();
    private boolean showList = false;
    String url="";
    private ShareUtil shareUtil;


    @Override
    public void initData() {
        int teacherId = getIntent().getIntExtra(StringUtils.TEACHER_ID, 0);
        if(TextUtils.isEmpty( ConstantUrl.TOKN )){
            url = UrlHelper.PUBLIC_URL + "&teacherId="+teacherId+ "&userDifferentiation=2";
        }else{
            url = UrlHelper.PUBLIC_URL + "&token=" + ConstantUrl.TOKN +"&teacherId="+teacherId+ "&userDifferentiation=2";
        }
        //x5WebView.loadDataWithBaseURL("file:///android_asset/", HfFileUtil.readAssetsByName(this, "teacherDetail.html?linkUrl="+url, "utf-8"),"text/html","utf-8","");
        x5WebView.loadUrl("file:///android_asset/teacherDetail.html?linkUrl=" + url);
        x5WebView.addJavascriptInterface(this, "android");


    }
    @JavascriptInterface
    public void setLineClassMessage(String json) {
        CourseLiveBean person = gson.fromJson(json, CourseLiveBean.class);
        Intent intent = new Intent(mContext, CourseDetailActivity.class);
        intent.putExtra(StringUtils.COURSE_ID, person.courseId);
        mContext.startActivity(intent);

    }
    @JavascriptInterface
    public void setOnLineMessage(String json) {
        CourseLiveBean person = gson.fromJson(json, CourseLiveBean.class);
        Intent intent = new Intent(mContext,LiveCourseActivity.class);
        intent.putExtra(StringUtils.COURSE_ID,person.getCourseId());
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void setTeacherMessage() {
        showList = true;
    }

    @JavascriptInterface
    public void setMessage(String json) {
        CourseOflineBean person = gson.fromJson(json, CourseOflineBean.class);
        Intent intent = new Intent(mContext, OfflineOneonOneDetailsActivity.class);
        intent.putExtra(StringUtils.COURSE_ID, person.courseId);
        mContext.startActivity(intent);

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
                onBackPressed();
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
    public void onBackPressed() {
        if (showList) {
            showList=false;
            if (x5WebView.canGoBack()) {
                x5WebView.goBack();
                return;
            }
        }else {
           finish();
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
