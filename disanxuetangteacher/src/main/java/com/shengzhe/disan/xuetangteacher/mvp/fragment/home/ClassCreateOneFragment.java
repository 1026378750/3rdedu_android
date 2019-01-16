package com.shengzhe.disan.xuetangteacher.mvp.fragment.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.common.camera.callback.IPhotoCall;
import com.common.camera.utils.CameraAlbumUtils;
import com.common.camera.utils.VanCropType;
import com.main.disanxuelib.bean.GradeParentBean;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.main.disanxuelib.view.popup.SelectorPickerView;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.main.disanxuelib.bean.ClassCourseBean;
import com.shengzhe.disan.xuetangteacher.utils.IntegerUtil;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 线下班课 第一步 on 2018/3/30.
 */

public class ClassCreateOneFragment extends BaseFragment implements IPhotoCall {
    @BindView(R.id.iv_calssone_photo)
    ImageView mFace;
    @BindView(R.id.et_calssone_name)
    EditText mCourseName;
    @BindView(R.id.tv_calssone_prename)
    TextView mPreName;
    @BindView(R.id.tv_calssone_stage)
    TextView mStage;
    @BindView(R.id.et_calssone_number)
    EditText mNumber;
    @BindView(R.id.tv_calssone_address)
    TextView mAddress;
    @BindView(R.id.et_calssone_address)
    EditText mAddressDetail;
    @BindView(R.id.et_calssone_times)
    EditText mTimes;
    @BindView(R.id.tv_calssone_singletime)
    TextView mSingleTime;
    @BindView(R.id.et_calssone_singleprice)
    EditText mSinglePrice;
    @BindView(R.id.tv_calssone_experience)
    TextView mExperience;
    @BindView(R.id.tv_calssone_inlimit)
    TextView mInlimit;
    @BindView(R.id.et_calssone_introduction)
    EditText mIntroduction;
    @BindView(R.id.et_calssone_target)
    EditText mTarget;
    @BindView(R.id.et_calssone_fitcrowd)
    EditText mFitcrowd;
    @BindView(R.id.tv_calssone_introduction)
    TextView mIntroductionNum;
    @BindView(R.id.tv_calssone_target)
    TextView mTargetNum;
    @BindView(R.id.tv_calssone_fitcrowd)
    TextView mFitcrowdNum;
    @BindView(R.id.btn_calssone_next)
    Button mNext;

    private ClassCourseBean data;
    private NiceDialog niceDialog;

    public static ClassCreateOneFragment newInstance(ClassCourseBean data) {
        ClassCreateOneFragment fragment = new ClassCreateOneFragment();
        Bundle args = new Bundle();
        args.putParcelable(StringUtils.FRAGMENT_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        data = getArguments().getParcelable(StringUtils.FRAGMENT_DATA);
        if(data==null){
            data = new ClassCourseBean();
        }
        data.subjectName =  SharedPreferencesManager.getSubjectName();
        mCourseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.textIsEmpty(mStage.getText().toString())){
                    mPreName.setText("请选择授课阶段");
                    return;
                }
                mPreName.setText(mStage.getText().toString()+"-"+mCourseName.getText().toString());
            }
        });

        mIntroduction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //50~500
                count += start;
                mIntroductionNum.setText(count==0?"限50-500字":count>500?"-"+(500-count)+"字": count+"字");
                mIntroductionNum.setTextColor(UiUtils.getColor(count>0&&(count<50||count>500)?R.color.color_ca4341:R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTarget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //40~400
                count += start;
                mTargetNum.setText(count==0?"限40-400字":count>400?"-"+(400-count)+"字": count+"字");
                mTargetNum.setTextColor(UiUtils.getColor(count>0&&(count<40||count>400)?R.color.color_ca4341:R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mFitcrowd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //30~300
                count += start;
                mFitcrowdNum.setText(count==0?"限30-300字":count>300?"-"+(300-count)+"字": count+"字");
                mFitcrowdNum.setTextColor(UiUtils.getColor(count>0&&(count<30||count>300)?R.color.color_ca4341:R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTimes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String exNum = mExperience.getText().toString().trim();
                String inpNum = mTimes.getText().toString();
                if( SharedPreferencesManager.getUserInfo()!=null&&SharedPreferencesManager.getUserInfo().getIdentity()==0){
                    if(StringUtils.textIsEmpty(inpNum)&& !StringUtils.textIsEmpty(exNum)&&Integer.parseInt(exNum)>0){
                        mExperience.setText("0");
                        return;
                    }
                    if(!StringUtils.textIsEmpty(exNum)&&Integer.parseInt(exNum)>0&&Long.parseLong(inpNum)<Integer.parseInt(exNum)){
                        mExperience.setText(String.valueOf(Integer.parseInt(inpNum)-1));
                    }
                } else {
                    if((!StringUtils.textIsEmpty(inpNum)&& !StringUtils.textIsEmpty(exNum)) &&(Integer.parseInt(inpNum)<=Integer.parseInt(exNum))) {
                        nofifyShowMesg("课程次数必须大于体验次数",mTimes);
                    }
                }
            }
        });

        mAddress.setText(SharedPreferencesManager.getUserCity()==null?"请选择已开通城市":SharedPreferencesManager.getUserCity().getCityName());

        getGradeParent();
        if( SharedPreferencesManager.getUserInfo()!=null&&SharedPreferencesManager.getUserInfo().getIdentity()==1){
            mExperience.setClickable(false);
            mExperience.setEnabled(false);
            getCampushExperienctNum();
        }
    }

    /**
     * 查询校区体验课次数
     */
    private void getCampushExperienctNum() {
        HttpService httpService = Http.getHttpService();
        httpService.campushExperienctNum()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<Integer>(mContext, true) {
                    @Override
                    protected void onDone(Integer num) {
                        mExperience.setText(String.valueOf(num));
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                        mExperience.setText("0");
                    }
                });
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_classone;
    }
    private int gradeId = -1;
    @OnClick({R.id.iv_calssone_photo,R.id.tv_calssone_stage,R.id.tv_calssone_singletime,R.id.tv_calssone_inlimit,R.id.tv_calssone_experience,R.id.btn_calssone_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_calssone_photo:
                //照片
                int width = SystemInfoUtil.getScreenWidth()-40;
                CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(getChildFragmentManager()).setTailorType(VanCropType.CROP_TYPE_RECTANGLE)
                        .setParam(width,(int)(width*0.7));
                break;

            case R.id.tv_calssone_stage:
                //授课阶段
                if(beanArray==null)
                    return;
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
                    @Override
                    public void onItemListener(int index, String item) {
                        gradeId = beanList.get(index).getGradeId();
                        mStage.setText(item+data.subjectName);
                        mStage.setHint(data.subjectName);
                        if (!StringUtils.textIsEmpty(mCourseName.getText().toString())){
                            mPreName.setText(mStage.getText().toString()+"-"+mCourseName.getText().toString());
                        }
                    }
                });
                niceDialog.setCommonLayout(beanArray, true, getFragmentManager());
                break;

            case R.id.tv_calssone_singletime:
                //单次课时长
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
                    @Override
                    public void onItemListener(int index, String item) {
                        mSingleTime.setText(item);
                    }
                });
                niceDialog.setCommonLayout(new String[]{"1","2","3","4","5","6"},true,getFragmentManager());
                break;

            case R.id.tv_calssone_experience:
                //体验次数
                final int times = getExperience();
                if (times==-1)
                    return;
                if(niceDialog==null){
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setLayoutId(R.layout.common_popup_num)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                                SelectorPickerView pickerView = (SelectorPickerView) holder.getConvertView();
                                pickerView.setNumberPickerView(0,times);
                                pickerView.setShowNumPicker(StringUtils.textIsEmpty(mExperience.getText().toString().trim())?"0":mExperience.getText().toString().trim());
                                holder.setOnClickListener(R.id.customer_picker_leftbtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        colseDialog();
                                    }
                                });
                                pickerView.setSelectPicker(new SelectorPickerView.SelectPickerListener(){

                                    @Override
                                    public void onResultPicker(Object obj) {
                                        mExperience.setText(String.valueOf(obj));
                                        colseDialog();
                                    }
                                });
                            }
                        })
                        .setDimAmount(0.3f)
                        .setShowBottom(true)
                        .show(getFragmentManager());

                break;

            case R.id.tv_calssone_inlimit:
                //插班人数
                if (niceDialog == null) {
                    niceDialog = NiceDialog.init();
                }
                niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
                    @Override
                    public void onItemListener(int index, String item) {
                        mInlimit.setText(item);
                    }
                });
                niceDialog.setCommonLayout(ContentUtil.selectCANJOIN,true,getFragmentManager());
                break;

            case R.id.btn_calssone_next:
                //下一页
                if(StringUtils.textIsEmpty(data.pictureUrl)){
                    nofifyShowMesg("请上传封面",null);
                    return;
                }

                if (StringUtils.textIsEmpty(mStage.getText().toString())) {
                    nofifyShowMesg("请选择授课阶段", null);
                    return;
                }
                data.gradeName = mStage.getHint().toString();
                data.gradeId = gradeId;

                if(StringUtils.textIsEmpty(mCourseName.getText().toString())){
                    nofifyShowMesg("请填写课程名称",mCourseName);
                    return;
                }
                if (mCourseName.getText().toString().length()>15) {
                    nofifyShowMesg("课程名称最多15个字",mCourseName);
                    return;
                }
                if(RegUtil.containsEmoji(mCourseName.getText().toString())){
                    nofifyShowMesg("禁止输入表情",mCourseName);
                    return;
                }
                data.courseName = mPreName.getText().toString();
                data.courseName2 = mCourseName.getText().toString();

                if(StringUtils.textIsEmpty(mNumber.getText().toString())){
                    nofifyShowMesg("请输入班课人数",mNumber);
                    return;
                }
                if(!StringUtils.textIsEmpty(mNumber.getText().toString())&&(Long.parseLong(mNumber.getText().toString().trim())<1||Long.parseLong(mNumber.getText().toString().trim())>1000)){
                    nofifyShowMesg("班课人数上限范围为1~1000",mNumber);
                    return;
                }
                data.salesVolume = Integer.parseInt(mNumber.getText().toString());

                if(StringUtils.textIsEmpty(mAddressDetail.getText().toString())){
                    nofifyShowMesg("请输入详细地址",mAddressDetail);
                    return;
                }
                data.address =SharedPreferencesManager.getUserCity();
                data.addressStr = mAddressDetail.getText().toString();

                if(StringUtils.textIsEmpty(mTimes.getText().toString())){
                    nofifyShowMesg("请输入次数",mTimes);
                    return;
                }
                if(!StringUtils.textIsEmpty(mTimes.getText().toString())&&(Long.parseLong(mTimes.getText().toString().trim())<1||Long.parseLong(mTimes.getText().toString().trim())>500)){
                    nofifyShowMesg("课程次数范围为1~500",mTimes);
                    return;
                }
                data.courseTimes = Integer.parseInt(mTimes.getText().toString());

                if(StringUtils.textIsEmpty(mSingleTime.getText().toString())){
                    nofifyShowMesg("请选择单次时长",null);
                    return;
                }
                data.singleTime = Integer.parseInt(mSingleTime.getText().toString());

                if(StringUtils.textIsEmpty(mSinglePrice.getText().toString())){
                    nofifyShowMesg("请输入单价",mSinglePrice);
                    return;
                }
                if(!StringUtils.textIsEmpty(mSinglePrice.getText().toString())&&(Double.parseDouble(mSinglePrice.getText().toString().trim())<0.1||Double.parseDouble(mSinglePrice.getText().toString().trim())>9999)){
                    nofifyShowMesg("单次课价格范围为0.1~9999元/小时",mSinglePrice);
                    return;
                }
                data.singlePrice = ArithUtils.roundLong(mSinglePrice.getText().toString());

                if(StringUtils.textIsEmpty(mExperience.getText().toString())){
                    nofifyShowMesg("请选择体验次数",null);
                    return;
                }
                String exNum = mExperience.getText().toString().trim();
                String inpNum = mTimes.getText().toString();
                if((!StringUtils.textIsEmpty(inpNum)&& !StringUtils.textIsEmpty(exNum)) &&(Integer.parseInt(inpNum)<=Integer.parseInt(exNum))) {
                    nofifyShowMesg("课程次数必须大于体验次数",mTimes);
                }

                data.mExperience = Integer.parseInt(mExperience.getText().toString());

                if(StringUtils.textIsEmpty(mInlimit.getText().toString())){
                    nofifyShowMesg("请选择插班限制",null);
                    return;
                }
                data.mInlimit = StringUtils.canJoinToInt(mInlimit.getText().toString());

               if (!StringUtils.textIsEmpty(mIntroduction.getText().toString())&&(mIntroduction.getText().toString().length()<50 || mIntroduction.getText().toString().length()>500)) {
                    nofifyShowMesg("课程简介字数范围为50~500字",mIntroduction);
                    return;
                }
                if(RegUtil.containsEmoji(mIntroduction.getText().toString())){
                    nofifyShowMesg("禁止输入表情",mIntroduction);
                    return;
                }
                data.introduction = mIntroduction.getText().toString();

               if (!StringUtils.textIsEmpty(mTarget.getText().toString())&&(mTarget.getText().toString().length()<40 || mTarget.getText().toString().length()>400)) {
                    nofifyShowMesg("教学目标字数范围为40~400字",mTarget);
                    return;
                }
                if(RegUtil.containsEmoji(mTarget.getText().toString())){
                    nofifyShowMesg("禁止输入表情",mTarget);
                    return;
                }
                data.target = mTarget.getText().toString();

                if (!StringUtils.textIsEmpty(mFitcrowd.getText().toString())&&(mFitcrowd.getText().toString().length()<30 || mFitcrowd.getText().toString().length()>300)) {
                    nofifyShowMesg("适用人群字数范围为30~300字",mFitcrowd);
                    return;
                }
                if(RegUtil.containsEmoji(mFitcrowd.getText().toString())){
                    nofifyShowMesg("禁止输入表情",mFitcrowd);
                    return;
                }

                data.fitCrowd = mFitcrowd.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11040);
                bundle.putParcelable(StringUtils.EVENT_DATA,data);
                bundle.putInt(StringUtils.EVENT_DATA2,1);
                EventBus.getDefault().post(bundle);
                break;
        }
    }

    /*****
     * 获取体验次数
     * @return
     */
    private int getExperience(){
        String timesStr = mTimes.getText().toString().trim();
        if(StringUtils.textIsEmpty(timesStr)||Long.parseLong(timesStr)<1){
            nofifyShowMesg("请先填写课程次数",mTimes);
            return -1;
        }
        return Long.parseLong(timesStr)<=10?Integer.parseInt(timesStr)-1:10;
    }

    private String[] beanArray;
    private List<GradeParentBean> beanList;
    private void getGradeParent(){
        beanList = SharedPreferencesManager.getGradePhase();
        if(beanList!=null&&!beanList.isEmpty()){
            beanArray =new String[beanList.size()];
            for(int i=0;i<beanList.size();i++){
                beanArray[i]=beanList.get(i).getGradeName();
            }
        }

        HttpService httpService = Http.getHttpService();
        httpService.gradeParent()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<List<GradeParentBean>>(mContext, true) {
                    @Override
                    protected void onDone(List<GradeParentBean> mBeanList) {
                        SharedPreferencesManager.saveGradePhase(beanList);
                        beanList = mBeanList;
                        if(beanList!=null&&!beanList.isEmpty()){
                            beanArray = new String[beanList.size()];
                            for(int i=0;i<beanList.size();i++){
                                beanArray[i]=beanList.get(i).getGradeName();
                            }
                        }
                    }
                    @Override
                    public void onResultError(ResultException ex) {
                    }
                });
    }

    /****
     * 显示提示信息
     * @param mesg
     */
    private void nofifyShowMesg(String mesg ,final EditText currentEdit){
        ConfirmDialog dialog = ConfirmDialog.newInstance("", mesg, "", "确定");
        dialog.setMargin(60)
                .setWidth(SystemInfoUtil.getScreenWidth() * 2 / 3)
                .setOutCancel(false)
                .show(getFragmentManager());
        dialog.setConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {

            @Override
            public void dialogStatus(int id) {
                switch (id) {
                    case R.id.tv_dialog_ok:
                        if(currentEdit==null)
                            return;
                        currentEdit.requestFocus();
                        break;
                }
            }
        });
    }

    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    @Override
    public void onPhotoResult(String photoUrl) {
        data.pictureUrl = photoUrl;
        ImageUtil.setItemRoundImageViewOnlyDisplay(mFace, photoUrl);
        ImageUtil.loadImageViewLoding(mContext, photoUrl, mFace,R.mipmap.default_error,R.mipmap.default_error);
    }
}
