package com.shengzhe.disan.xuetangteacher.mvp.activity.course;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.main.disanxuelib.bean.Address;
import com.main.disanxuelib.util.AppManager;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.view.CommonCrosswiseBar;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.shengzhe.disan.xuetangteacher.mvp.activity.schedule.TimeSelectActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.AddressSelectActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.AreaSelectActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.IndustrySchoolActivity;
import com.shengzhe.disan.xuetangteacher.mvp.activity.common.OpenCityActivity;
import com.shengzhe.disan.xuetangteacher.utils.ConstantUrl;
import com.shengzhe.disan.xuetangteacher.activity.MainActivity;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.activity.BaseActivity;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.main.disanxuelib.bean.CityBean;
import com.shengzhe.disan.xuetangteacher.bean.StartCourseSetupBean;
import com.shengzhe.disan.xuetangteacher.bean.TeachingAreasBean;
import com.shengzhe.disan.xuetangteacher.bean.TeachingTimeBean;
import com.shengzhe.disan.xuetangteacher.bean.User;
import com.shengzhe.disan.xuetangteacher.bean.CampusBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.RequestBodyUtils;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liukui on 2018/1/13.
 * <p>
 * 开课设置
 */

public class StartClassActivity extends BaseActivity {
    @BindView(R.id.ccb_startclass_localaddress)
    CommonCrosswiseBar mLocalAddress;

    @BindView(R.id.rl_campus_layout)
    RelativeLayout mCampusLayout;
    @BindView(R.id.rb_campus_checked)
    RadioButton mCampusChecked;
    @BindView(R.id.rb_campus_campus)
    TextView mCampusCampus;
    @BindView(R.id.rb_campus_address)
    TextView mCampusAddress;

    @BindView(R.id.rb_address_checked)
    RadioButton mAddressChecked;
    @BindView(R.id.rb_address_campus)
    TextView mAddressCampus;
    @BindView(R.id.rb_address_address)
    TextView mAddressAddress;

    @BindView(R.id.rb_area_checked)
    RadioButton mAreaChecked;
    @BindView(R.id.rb_area_campus)
    TextView mAreaCampus;

    @BindView(R.id.ccb_startclass_time)
    CommonCrosswiseBar mStartclassTime;
    private HttpService httpService;

    private StartCourseSetupBean mCourseBean;
    private String postData="";

    @Override
    public void initData() {
        isChangeDate = false;

        mCity = new CityBean();
       /* mCity.cityName = "上海市";
        mCity.cityCode = "3101";
        mLocalAddress.setLeftText("上海市");*/

        httpService = Http.getHttpService();
     //   postData
        postData=getIntent().getStringExtra("postData");
        if(TextUtils.isEmpty(postData)){
            postStartCourseSetupData();
        }else {
            Gson  gson=new Gson();
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(postData);
                if (mCourseBean == null) {
                    mCourseBean = new StartCourseSetupBean();
                }
                mLocalAddress.setLeftText(jsonObject.optString("cityName"));
                mCourseBean.setCity(jsonObject.optString("city"));
                mCity.cityName =jsonObject.optString("cityName");
                mCity.cityCode = jsonObject.optString("city");
                mLocalAddress.setEnabled(false);
                mLocalAddress.setClickable(false);
                mCampusCampus.setText("等待分配校区");
                mCampusLayout.setEnabled(false);
                mCampusLayout.setClickable(false);
            }catch (JSONException e){
                    e.printStackTrace();
            }

        }

    }

    /**
     * 开课设置数据
     */
    private void postStartCourseSetupData() {
        httpService.startCourseSetupData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<StartCourseSetupBean>(mContext, true) {
                    @Override
                    protected void onDone(StartCourseSetupBean courseBean) {
                        mCourseBean = courseBean;
                        if (mCourseBean == null) {
                            mCourseBean = new StartCourseSetupBean();
                        }
                        if (courseBean != null) {
                            if (!TextUtils.isEmpty(courseBean.getCity())) {
                                //城市
                                mLocalAddress.setLeftText(courseBean.getCity());
                                //授课信息
                                mAddressChecked.setChecked(!TextUtils.isEmpty(courseBean.getAreaCode())||!TextUtils.isEmpty(courseBean.getCity()));
                                mAddressCampus.setText(TextUtils.isEmpty( courseBean.getCity())==true?  " ":courseBean.getCity() + " " + courseBean.getArea());
                                mAddressAddress.setText(TextUtils.isEmpty(courseBean.getAddress())==true?courseBean.getAddress():courseBean.getAddress());
                                //城市
                                mCity.cityName = courseBean.getCity();
                                mCity.cityCode = courseBean.getCityCode();
                                //授课地址
                                address = new Address();
                                address.district = courseBean.getArea();

                                address.districtId =StringUtils.textIsEmpty(courseBean.getAreaCode())==true?0: Integer.parseInt(courseBean.getAreaCode());
                                address.address = courseBean.getAddress();
                            }
                            //校区信息
                            if (courseBean.getCampus() != null) {
                                mCampusChecked.setChecked(!StringUtils.textIsEmpty(courseBean.getCampus().campusName));
                                mCampusCampus.setText(TextUtils.isEmpty(courseBean.getCampus().campusName)==true?"等待分配校区":courseBean.getCampus().campusName);

                                courseBean.getCampus().setAddress(TextUtils.isEmpty(courseBean.getCampus().address)==true?"":courseBean.getCampus().address);
                                courseBean.getCampus().setCampusCity(TextUtils.isEmpty(courseBean.getCampus().campusCity)==true?"":courseBean.getCampus().campusCity);
                                courseBean.getCampus().setCampusArea(TextUtils.isEmpty(courseBean.getCampus().campusArea)==true?"":courseBean.getCampus().campusArea);

                                mCampusAddress.setText(courseBean.getCampus().campusCity+courseBean.getCampus().campusArea+courseBean.getCampus().address);
                                //赋值
                                campusBean = mCourseBean.getCampus();
                            }
                            //所属校区
                           mLocalAddress.setEnabled(false);
                            mLocalAddress.setClickable(false);
                            mCampusLayout.setEnabled(false);
                            mCampusLayout.setClickable(false);

                         /*   if (courseBean.getIdentity() != 1) {
                                //自由老师
                                mLocalAddress.setRightButton(R.mipmap.ic_black_right_arrow);
                                ImageUtil.setCompoundDrawable(mCampusCampus, 17, R.mipmap.ic_black_right_arrow, Gravity.RIGHT, 0);
                            } else {
                                mLocalAddress.setEnabled(false);
                                mLocalAddress.setClickable(false);
                                mCampusLayout.setEnabled(false);
                                mCampusLayout.setClickable(false);
                            }*/
                            //授课区域
                            if (courseBean.getTeachingAreas() != null && !courseBean.getTeachingAreas().isEmpty()) {
                                mAreaList = new ArrayList<>();
                                mAreaList.addAll(courseBean.getTeachingAreas());
                                mAreaChecked.setChecked(true);
                                mAreaCampus.setText(getAreaStr(mAreaList));
                            }
                            //授课时间
                            if (courseBean.getTeachingTime() != null && !courseBean.getTeachingTime().isEmpty()) {
                                classHourses = new ArrayList<>();
                                classHourses.addAll(courseBean.getTeachingTime());
                                mStartclassTime.setLeftButton(R.drawable.ic_tick_checked);
                            }
                        }
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                    }
                });

    }

    @Override
    public int setLayout() {
        return R.layout.activity_startclass;
    }

    @OnClick({R.id.common_bar_leftBtn, R.id.ccb_startclass_localaddress, R.id.rl_campus_layout, R.id.rl_address_layout, R.id.rl_area_layout, R.id.ccb_startclass_time, R.id.tv_startclass_comfirm})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.common_bar_leftBtn:
                onBackPressed();
                break;

            case R.id.ccb_startclass_localaddress:
                //地址选择
                intent.setClass(mContext, OpenCityActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, mCity);
                startActivity(intent);
                break;

            case R.id.rl_campus_layout:
                //所属校区
                if (mCity == null) {
                    showToast("请先选择城市");
                    return;
                }
                intent.setClass(mContext, IndustrySchoolActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, mCity);
                intent.putExtra(StringUtils.ACTIVITY_DATA2, campusBean);
                startActivity(intent);
                break;

            case R.id.rl_address_layout:
                //授课地址
              /*  if (campusBean == null) {
                    showToast("请先选择所属校区");
                    return;
                }*/
                intent.setClass(mContext, AddressSelectActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, mCity);
                intent.putExtra(StringUtils.ACTIVITY_DATA2, address);
                startActivity(intent);
                break;

            case R.id.rl_area_layout:
                //授课区域
                if (mCity == null) {
                    showToast("请先选择城市");
                    return;
                }
                intent.setClass(mContext, AreaSelectActivity.class);
                intent.putExtra(StringUtils.ACTIVITY_DATA, mCity);
                intent.putExtra(StringUtils.ACTIVITY_DATA2, mAreaList);
                startActivity(intent);
                break;

            case R.id.ccb_startclass_time:
                //授课时间
                if (mAreaList == null || mAreaList.isEmpty()) {
                    showToast("请先选择授课区域");
                    return;
                }
                intent.setClass(mContext, TimeSelectActivity.class);
                intent.putParcelableArrayListExtra(StringUtils.ACTIVITY_DATA, classHourses);
                startActivity(intent);
                break;

            case R.id.tv_startclass_comfirm:
                //提交
                if (mCity == null) {
                    showToast("请先选择城市");
                    return;
                }
               /* if (campusBean == null) {
                    showToast("请先绑定校区");
                    return;
                }*/
                if (address == null) {
                    showToast("请先选择授课地址");
                    return;
                }
                if (TextUtils.isEmpty(address.address)) {
                    showToast("请先选择详细地址");
                    return;
                }
                if (mAreaList == null || mAreaList.isEmpty()) {
                    showToast("请先选择授课区域");
                    return;
                }
                if (classHourses == null || classHourses.isEmpty()) {
                    showToast("请先选择授课授课时间");
                    return;
                }
                ConstantUrl.CLIEN_Info=2;
                if(TextUtils.isEmpty(postData)){
                    SubmitData();
                }else {
                    SubmitComplexData();
                }

                break;
        }
    }

    /**
     * 个人资料+开课设置提交数据
     */
    private void SubmitComplexData() {
        HttpService httpService = Http.getHttpService();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        Map<String, Object> map =getStartData();

        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(postData);
            map.put("nickName",jsonObject.optString("nickName"));//用户昵称
            map.put("sex", jsonObject.optInt(("nickName")));//老师性别
            map.put("subjectId", jsonObject.optInt(("subjectId")));//	科目id
            map.put("teachingAge", jsonObject.optInt(("teachingAge")));//	教龄
            map.put("geaduateSchool", jsonObject.optString(("geaduateSchool")));//毕业学校
            map.put("personalResume", jsonObject.optString(("personalResume")));//个人简历
            map.put("profession",jsonObject.optString(("profession")));//专业
            map.put("edu", jsonObject.optInt(("edu")));//最高学历
            File file;
            String  PhotoUrl=jsonObject.optString("uploadFile");
            if (StringUtils.textIsEmpty(PhotoUrl) || PhotoUrl.contains("http")) {
                //file = FileUtil.getNewFile("");//filePath 图片地址
                byte[] data = new byte[0];
                RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), data);
                builder.addFormDataPart("uploadFile", "uploadFile", imageBody);//imgfile 后台接收图片流的参数名
               //map.put("uploadFile","");
            } else {
                file = new File(PhotoUrl);//filePath 图片地址
                RequestBody imageBody = RequestBody.create(MediaType.parse("img/jpeg"), file);
                builder.addFormDataPart("uploadFile", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                //map.put("uploadFile",PhotoUrl);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        builder.addFormDataPart("sign", RequestBodyUtils.setEncrypt(map));
        List<MultipartBody.Part> parts = builder.build().parts();
        httpService.savePersonalData(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String str) {
                        ConstantUrl.CLIEN_Info=1;
                        ToastUtil.showShort("开课设置保存成功！");
                        AppManager.getAppManager().goToActivityExcludeName(MainActivity.class.getName());
                        SharedPreferencesManager.setOpenCity(2);
                        Bundle bundle = new Bundle();
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11038);
                        EventBus.getDefault().post(bundle);
                        finish();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        ToastUtil.showShort("提交出错，请稍后再试！");
                        if(ex.getErrCode()==222222){
                            SharedPreferencesManager.setOpenCity(2);
                        }
                    }
                });


    }

    private void showToast(String str) {
        ConfirmDialog.newInstance("", str, "", "确定").setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    private   Map<String, Object> getStartData(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("city", mCity.getCityCode());
        map.put("area", address.districtId==0?"":address.districtId);
        map.put("address", address.address);
        //map.put("campusId", campusBean.id);
        ArrayList<TeachingAreasBean>  areasBeanArrayList=new ArrayList<>();
        for (TeachingAreasBean teachingAreasBean : mAreaList) {
            teachingAreasBean.areaCode=teachingAreasBean.areaId;
            teachingAreasBean.areaName=teachingAreasBean.getAreaName();
            areasBeanArrayList.add(teachingAreasBean);
        }
        map.put("teachingAreas", new Gson().toJson(areasBeanArrayList));//授课区域json数组

        ArrayList<TeachingTimeBean>  teachingTimeBeen=new ArrayList<>();
        for(int i =1;i<=7;i++){
            TeachingTimeBean  teachingTimeBean=new TeachingTimeBean();
            teachingTimeBean.setWeek(i);
            teachingTimeBean.setTimes("");
            teachingTimeBeen.add(teachingTimeBean);
        }

        for (int i=1; i<=7; i++) {
                for(int j=0;j<classHourses.size();j++){
                    TeachingTimeBean  teachingTimeBean=classHourses.get(j);
                    if(i==teachingTimeBean.getWeek()&&(!TextUtils.isEmpty(teachingTimeBean.getTimes()))){
                        teachingTimeBeen.set(i-1,teachingTimeBean);
                        break;
                    }
                }
        }
        map.put("teachingTime", new Gson().toJson(teachingTimeBeen));//	授课时间json数组
        return map;
    }
    //提交数据
    private void SubmitData() {
        HttpService httpService = Http.getHttpService();
        Map<String, Object> map =getStartData();
       /* if(areasBeanArrayList.size()==1){
            TeachingAreasBean  teachingAreasBean=areasBeanArrayList.get(0);
            if(teachingAreasBean.getAreaName().equals("不限")){
                areasBeanArrayList.clear();
                teachingAreasBean.setAreaCode("--");
                teachingAreasBean.setAreaName("");
                areasBeanArrayList.add(teachingAreasBean);
                map.put("teachingAreas", new Gson().toJson(areasBeanArrayList));//授课区域json数组
            }
        }*/

        httpService.addStartCourseData(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<String>(mContext, true) {
                    @Override
                    protected void onDone(String str) {
                        ToastUtil.showShort("开课设置保存成功！");
                       /* Bundle bundle = new Bundle();
                        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11038);
                        EventBus.getDefault().post(bundle);
                        finish();*/
                        setEvbus();
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        if(ex.getErrCode()==222222){
                            setEvbus();
                        }else {
                            ToastUtil.showShort("提交出错，请稍后再试！");
                        }

                    }
                });

    }
    private void setEvbus(){
        ConstantUrl.CLIEN_Info=1;
        ToastUtil.showShort("开课设置保存成功！");
        Bundle bundle = new Bundle();
        bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11038);
        EventBus.getDefault().post(bundle);
        User  user=SharedPreferencesManager.getUserInfo();
        user.setIsStartCourse(2);
        SharedPreferencesManager.setUserInfo(user);
        finish();

    }

    private CityBean mCity;
    private CampusBean campusBean;
    private Address address;
    private ArrayList<TeachingAreasBean> mAreaList;
    private ArrayList<TeachingTimeBean> classHourses = new ArrayList<>();

    //接受event事件
    @Override
    public boolean onEventMainThread(Bundle bundle) {
        switch (bundle.getInt(StringUtils.EVENT_ID)) {
            case IntegerUtil.EVENT_ID_11025://城市修改
                isChangeDate = true;
                CityBean city = bundle.getParcelable(StringUtils.EVENT_DATA);
                mLocalAddress.setLeftText(city.cityName);
                if (!mCity.cityCode.equals(city.cityCode)) {
                    campusBean = null;
                    address = null;
                    if (mAreaList != null) {
                        mAreaList.clear();
                    }
                    if (classHourses != null) {
                        classHourses.clear();
                    }

                    mCampusChecked.setChecked(false);
                    mAddressChecked.setChecked(false);
                    mAreaChecked.setChecked(false);
                    mCampusCampus.setText("");
                    mCampusAddress.setText("");
                    mAddressCampus.setText("");
                    mAddressAddress.setText("");
                    mAreaCampus.setText("");
                    mStartclassTime.setLeftButton(R.drawable.ic_tick_unchecked);
                }
                mCity = city;
                break;

            case IntegerUtil.EVENT_ID_11026://所属校区修改
                isChangeDate = true;
                campusBean = bundle.getParcelable(StringUtils.EVENT_DATA);
                mCampusChecked.setChecked(!StringUtils.textIsEmpty(campusBean.campusName));

                campusBean.address=TextUtils.isEmpty(campusBean.address)==true?"":campusBean.address;
                mCity.cityName=TextUtils.isEmpty(mCity.cityName)==true?"":mCity.cityName;
                campusBean.campusArea=TextUtils.isEmpty(campusBean.campusArea)==true?"":campusBean.campusArea;

                mCampusCampus.setText(campusBean.campusName);
                mCampusAddress.setText(mCity.cityName + campusBean.campusArea + campusBean.address);
                break;

            case IntegerUtil.EVENT_ID_11027://所授课地址修改
                isChangeDate = true;
                address = bundle.getParcelable(StringUtils.EVENT_DATA);
                mAddressCampus.setText(mCity.cityName + " " + (StringUtils.textIsEmpty(address.district)?"":address.district));
                mAddressAddress.setText(address.address);
                mAddressChecked.setChecked(true);
                break;

            case IntegerUtil.EVENT_ID_11028://授课区域修改
                isChangeDate = true;
                mAreaList = bundle.getParcelableArrayList(StringUtils.EVENT_DATA);
                mAreaChecked.setChecked(mAreaList != null);
                mAreaCampus.setText(getAreaStr(mAreaList));
                break;

            case IntegerUtil.EVENT_ID_11029://授课时间修改
                isChangeDate = true;
                classHourses = bundle.getParcelableArrayList(StringUtils.EVENT_DATA);
                mStartclassTime.setLeftButton(classHourses != null && !classHourses.isEmpty() ? R.drawable.ic_tick_checked : R.drawable.ic_tick_unchecked);
                break;
        }
        return false;
    }

    private ConfirmDialog dialog;
    private boolean isChangeDate = false;
    @Override
    public void onBackPressed() {
        if (!isChangeDate) {
            finish();
            return;
        }
        if (dialog == null) {
            dialog = ConfirmDialog.newInstance("", "您还没有保存，确定要退出？", "取消", "确定");
        }
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        colseDialog();
                        finish();
                        break;
                }
            }
        });
    }

    private void colseDialog() {
        if (dialog != null && dialog.isVisible()) {
            dialog.dismiss();
        }
    }

    private String getAreaStr(ArrayList<TeachingAreasBean> mAreaList) {
        String strMesg = "";
        for (TeachingAreasBean area : mAreaList) {
            strMesg += area.getAreaName() + "  ";
        }
        return strMesg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isChangeDate = false;
    }

}
