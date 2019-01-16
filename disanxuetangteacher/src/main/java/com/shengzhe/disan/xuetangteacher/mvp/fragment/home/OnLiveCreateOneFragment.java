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
import com.common.camera.utils.VanCropType;
import com.main.disanxuelib.bean.GradeParentBean;
import com.main.disanxuelib.util.ArithUtils;
import com.common.camera.utils.CameraAlbumUtils;
import com.main.disanxuelib.util.ContentUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.ToastUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.fragment.BaseFragment;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;
import com.shengzhe.disan.xuetangteacher.bean.OnliveCourseBean;
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
 * Created by 在线直播了第一步 on 2018/1/17.
 */

public class OnLiveCreateOneFragment extends BaseFragment implements IPhotoCall {
    @BindView(R.id.iv_onliveoperator_face)
    ImageView mFace;
    @BindView(R.id.et_onliveoperator_coursename)
    EditText mCourseName;
    @BindView(R.id.tv_onliveoperator_prename)
    TextView mPreName;
    @BindView(R.id.tv_onliveoperator_stage)
    TextView mStage;
    @BindView(R.id.tv_onliveoperator_type)
    TextView mType;
    @BindView(R.id.et_onliveoperator_number)
    EditText mNumber;
    @BindView(R.id.et_onliveoperator_times)
    EditText mTimes;
    @BindView(R.id.et_onliveoperator_singletime)
    EditText mSingleTime;
    @BindView(R.id.et_onliveoperator_singleprice)
    EditText mSinglePrice;
    @BindView(R.id.et_onliveoperator_introduction)
    EditText mIntroduction;
    @BindView(R.id.et_onliveoperator_target)
    EditText mTarget;
    @BindView(R.id.et_onliveoperator_fitcrowd)
    EditText mFitcrowd;
    @BindView(R.id.tv_onliveoperator_introduction)
    TextView mIntroductionNum;
    @BindView(R.id.tv_onliveoperator_target)
    TextView mTargetNum;
    @BindView(R.id.tv_onliveoperator_fitcrowd)
    TextView mFitcrowdNum;
    @BindView(R.id.et_onliveoperator_next)
    Button mNext;

    private OnliveCourseBean data;
    private NiceDialog niceDialog;

    public static OnLiveCreateOneFragment newInstance(OnliveCourseBean data) {
        OnLiveCreateOneFragment fragment = new OnLiveCreateOneFragment();
        Bundle args = new Bundle();
        args.putParcelable(StringUtils.FRAGMENT_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        data = getArguments().getParcelable(StringUtils.FRAGMENT_DATA);
        if(data==null){
            data = new OnliveCourseBean();
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
        GetGradeParent();
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_onliveone;
    }
    private int gradeId = -1;
    @OnClick({R.id.iv_onliveoperator_face,R.id.tv_onliveoperator_stage,R.id.tv_onliveoperator_type,R.id.et_onliveoperator_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_onliveoperator_face:
                //照片
                int width = SystemInfoUtil.getScreenWidth()-40;
                CameraAlbumUtils.getInstance(mContext).setIPhotoCall(this).getPhoto(getChildFragmentManager()).setTailorType(VanCropType.CROP_TYPE_RECTANGLE)
                        .setParam(width,(int)(width*0.7));
                break;

            case R.id.tv_onliveoperator_stage:
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

            case R.id.tv_onliveoperator_type:
                //课程类型
                showType();
                break;

            case R.id.et_onliveoperator_next:
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

                if(StringUtils.textIsEmpty(mType.getText().toString())){
                    ToastUtil.showShort("请选择直播类型");
                    showType();
                    return;
                }
                data.directTypeName = mType.getText().toString();

                if(StringUtils.textIsEmpty(mNumber.getText().toString())){
                    nofifyShowMesg("请输入人数",mNumber);
                    return;
                }
                if(!StringUtils.textIsEmpty(mNumber.getText().toString())&&(Long.parseLong(mNumber.getText().toString().trim())<1||Long.parseLong(mNumber.getText().toString().trim())>maxNumber)){
                    nofifyShowMesg("人数上限范围为1"+(maxNumber>1?"~"+maxNumber:""),mNumber);
                    return;
                }
                data.salesVolume = Integer.parseInt(mNumber.getText().toString());

                if(StringUtils.textIsEmpty(mTimes.getText().toString())){
                    nofifyShowMesg("请输入次数",mTimes);
                    return;
                }
                if(!StringUtils.textIsEmpty(mTimes.getText().toString())&&(Long.parseLong(mTimes.getText().toString().trim())<1||Long.parseLong(mTimes.getText().toString().trim())>365)){
                    nofifyShowMesg("课程次数范围为1~365",mTimes);
                    return;
                }
                data.courseTimes = Integer.parseInt(mTimes.getText().toString());

                if(StringUtils.textIsEmpty(mSingleTime.getText().toString())){
                    nofifyShowMesg("请输入单次时长",mSingleTime);
                    return;
                }
                if (Long.parseLong(mSingleTime.getText().toString().trim())<1||Long.parseLong(mSingleTime.getText().toString().trim())>6) {
                    nofifyShowMesg("单次课时长范围1~6小时",mSingleTime);
                    return;
                }
                data.singleTime = Integer.parseInt(mSingleTime.getText().toString());

                if(StringUtils.textIsEmpty(mSinglePrice.getText().toString())){
                    nofifyShowMesg("请输入单价",mSinglePrice);
                    return;
                }
                if(!StringUtils.textIsEmpty(mSinglePrice.getText().toString())&&(Double.parseDouble(mSinglePrice.getText().toString().trim())<0||Double.parseDouble(mSinglePrice.getText().toString().trim())>9999)){
                    nofifyShowMesg("单次课价格范围为0.0~9999元/小时",mSinglePrice);
                    return;
                }
                data.singlePrice = ArithUtils.roundLong(mSinglePrice.getText().toString());

              /*  if(StringUtils.textIsEmpty(mIntroduction.getText().toString())){
                    nofifyShowMesg("请输入课程简介",mIntroduction);
                    return;
                }****/
                if (!StringUtils.textIsEmpty(mIntroduction.getText().toString())&&(mIntroduction.getText().toString().length()<50 || mIntroduction.getText().toString().length()>500)) {
                    nofifyShowMesg("课程简介字数范围为50~500字",mIntroduction);
                    return;
                }
                if(RegUtil.containsEmoji(mIntroduction.getText().toString())){
                    nofifyShowMesg("禁止输入表情",mIntroduction);
                    return;
                }
                data.introduction = mIntroduction.getText().toString();
                /*
                if(StringUtils.textIsEmpty(mTarget.getText().toString())){
                    nofifyShowMesg("请输入教学目标",mTarget);
                    return;
                }****/
                if (!StringUtils.textIsEmpty(mTarget.getText().toString())&&(mTarget.getText().toString().length()<40 || mTarget.getText().toString().length()>400)) {
                    nofifyShowMesg("教学目标字数范围为40~400字",mTarget);
                    return;
                }
                if(RegUtil.containsEmoji(mTarget.getText().toString())){
                    nofifyShowMesg("禁止输入表情",mTarget);
                    return;
                }
                data.target = mTarget.getText().toString();

              /*  if(StringUtils.textIsEmpty(mFitcrowd.getText().toString())){
                    nofifyShowMesg("请输入适用人群",mFitcrowd);
                    return;
                }****/

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
                bundle.putInt(StringUtils.EVENT_ID, IntegerUtil.EVENT_ID_11021);
                bundle.putParcelable(StringUtils.EVENT_DATA,data);
                bundle.putInt(StringUtils.EVENT_DATA2,1);
                EventBus.getDefault().post(bundle);
                break;
        }
    }

    private int maxNumber = 1;
    private  void  showType(){
        if (niceDialog == null) {
            niceDialog = NiceDialog.init();
        }
        niceDialog.setOnNiceDialogListener(new NiceDialog.NiceDialogListener() {
            @Override
            public void onItemListener(int index, String item) {
                data.directType = ContentUtil.selectLive().get(index+1).id;
                mType.setText(item);
                mNumber.setEnabled(true);
                mNumber.setEnabled(data.directType!=1);
                if(data.directType!=1) {
                    mNumber.requestFocus();
                    mNumber.setSelection(mNumber.getText().toString().length());
                }
                if(data.directType==1){
                    mNumber.setText("1");
                    mTimes.requestFocus();
                    mTimes.setSelection(mTimes.getText().toString().length());
                } else if (data.directType==2) {
                    maxNumber = 9;
                    mNumber.setText("");
                    mNumber.setHint("请输入（1-"+maxNumber+"）");
                } else if (data.directType==3) {
                    maxNumber = 100;
                    mNumber.setText("");
                    mNumber.setHint("请输入（1-"+maxNumber+"）");
                }
            }
        });
        niceDialog.setCommonLayout(new String[]{"一对一授课","互动小班课","普通大班课"},true,getChildFragmentManager());
    }

    @Override
    public void onPhotoResult(String photoUrl) {
        data.pictureUrl = photoUrl;
        ImageUtil.setItemRoundImageViewOnlyDisplay(mFace, photoUrl);
        ImageUtil.loadImageViewLoding(mContext, photoUrl, mFace,R.mipmap.default_error,R.mipmap.default_error);
    }

    private String[] beanArray;
    private List<GradeParentBean> beanList;
    private void GetGradeParent(){
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

}
