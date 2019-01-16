package com.shengzhe.disan.xuetangteacher.mvp.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.camera.callback.IPhotoCall;
import com.common.camera.utils.CameraAlbumUtils;
import com.common.camera.utils.VanCropType;
import com.main.disanxuelib.bean.CityBean;
import com.main.disanxuelib.bean.ClassCourseBean;
import com.main.disanxuelib.gen.City;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.RegUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.dialog.ConfirmDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangteacher.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 线下班课View层 on 2018/4/12.
 */

public class ClassCreateOneView implements IPhotoCall {
    private FragmentManager fragmentManager;
    private ClassCourseBean data;
    private IClassCreateOneView oneView;
    private Context context;

    public ClassCreateOneView(FragmentManager fragmentManager,Context context, ClassCourseBean data) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.data = data;
    }

    public void initDatas() {
        if(!TextUtils.isEmpty(SharedPreferencesManager.getUserInfo().getSubjectName())){
            oneView.getSubjectNameView().setText(SharedPreferencesManager.getUserInfo().getSubjectName());
        }
        oneView.getIntroductionView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //50~500
                count += start;
                oneView.getIntroductionNumView().setText(count==0?"限50-500字":count>500?"-"+(500-count)+"字": count+"字");
                oneView.getIntroductionNumView().setTextColor(UiUtils.getColor(count>0&&(count<50||count>500)? R.color.color_ca4341:R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        oneView.getTargetView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //40~400
                count += start;
                oneView.getTargetNumView().setText(count==0?"限40-400字":count>400?"-"+(400-count)+"字": count+"字");
                oneView.getTargetNumView().setTextColor(UiUtils.getColor(count>0&&(count<40||count>400)?R.color.color_ca4341:R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        oneView.getFitcrowdView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //30~300
                count += start;
                oneView.getFitcrowdNumView().setText(count==0?"限30-300字":count>300?"-"+(300-count)+"字": count+"字");
                oneView.getFitcrowdNumView().setTextColor(UiUtils.getColor(count>0&&(count<30||count>300)?R.color.color_ca4341:R.color.color_999999));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*****
     * 获取体验次数
     * @return
     */
    public int getExperience(){
        String timesStr = oneView.getTimesView().getText().toString().trim();
        if(StringUtils.textIsEmpty(timesStr)||Long.parseLong(timesStr)<1){
            nofifyShowMesg("请先填写课程次数",oneView.getTimesView());
            return -1;
        }
        return Long.parseLong(timesStr)<10?(Integer.parseInt(timesStr)<6?Integer.parseInt(timesStr):6):10;
    }

    /*****
     * 提交验证
     */
    public boolean setSubmit() {
        if(StringUtils.textIsEmpty(data.pictureUrl)){
            nofifyShowMesg("请上传封面",null);
            return false;
        }

        if(StringUtils.textIsEmpty(oneView.getCourseNameView().getText().toString())){
            nofifyShowMesg("请填写课程名称",oneView.getCourseNameView());
            return false;
        }
        if (oneView.getCourseNameView().getText().toString().length()>15) {
            nofifyShowMesg("课程名称最多15个字",oneView.getCourseNameView());
            return false;
        }
        if(RegUtil.containsEmoji(oneView.getCourseNameView().getText().toString())){
            nofifyShowMesg("禁止输入表情",oneView.getCourseNameView());
            return false;
        }
        data.courseName = oneView.getCourseNameView().getText().toString();

        if(StringUtils.textIsEmpty(oneView.getStageView().getText().toString())){
            nofifyShowMesg("请选择授课阶段",null);
            return false;
        }
        data.gradeName = oneView.getStageView().getText().toString();

        if(StringUtils.textIsEmpty(oneView.getNumberView().getText().toString())){
            nofifyShowMesg("请输入班课人数",oneView.getNumberView());
            return false;
        }
        if(!StringUtils.textIsEmpty(oneView.getNumberView().getText().toString())&&(Long.parseLong(oneView.getNumberView().getText().toString().trim())<1||Long.parseLong(oneView.getNumberView().getText().toString().trim())>1000)){
            nofifyShowMesg("班课人数上限范围为1~1000",oneView.getNumberView());
            return false;
        }
        data.salesVolume = Integer.parseInt(oneView.getNumberView().getText().toString());

        if(StringUtils.textIsEmpty(oneView.getAddressView().getText().toString())){
            nofifyShowMesg("请选择省份、城市",null);
            return false;
        }

        if(StringUtils.textIsEmpty(oneView.getAddressDetailView().getText().toString())){
            nofifyShowMesg("请输入详细地址",oneView.getAddressDetailView());
            return false;
        }
        data.addressStr = oneView.getAddressDetailView().getText().toString();

        if(StringUtils.textIsEmpty(oneView.getTimesView().getText().toString())){
            nofifyShowMesg("请输入次数",oneView.getTimesView());
            return false;
        }
        if(!StringUtils.textIsEmpty(oneView.getTimesView().getText().toString())&&(Long.parseLong(oneView.getTimesView().getText().toString().trim())<1||Long.parseLong(oneView.getTimesView().getText().toString().trim())>500)){
            nofifyShowMesg("课程次数范围为1~500",oneView.getTimesView());
            return false;
        }
        data.courseTimes = Integer.parseInt(oneView.getTimesView().getText().toString());

        if(StringUtils.textIsEmpty(oneView.getSingleTimeView().getText().toString())){
            nofifyShowMesg("请选择单次时长",null);
            return false;
        }
        data.singleTime = Integer.parseInt(oneView.getSingleTimeView().getText().toString());

        if(StringUtils.textIsEmpty(oneView.getSinglePriceView().getText().toString())){
            nofifyShowMesg("请输入单价",oneView.getSinglePriceView());
            return false;
        }
        if(!StringUtils.textIsEmpty(oneView.getSinglePriceView().getText().toString())&&(Double.parseDouble(oneView.getSinglePriceView().getText().toString().trim())<0.1||Double.parseDouble(oneView.getSinglePriceView().getText().toString().trim())>9999)){
            nofifyShowMesg("单次课价格范围为0.1~9999元/小时",oneView.getSinglePriceView());
            return false;
        }
        data.singlePrice = ArithUtils.roundLong(oneView.getSinglePriceView().getText().toString());

        if(StringUtils.textIsEmpty(oneView.getExperienceView().getText().toString())){
            nofifyShowMesg("请选择体验次数",null);
            return false;
        }
        data.mExperience = Integer.parseInt(oneView.getExperienceView().getText().toString());

        if(StringUtils.textIsEmpty(oneView.getInlimitView().getText().toString())){
            nofifyShowMesg("请选择插班限制",null);
            return false;
        }
        data.mInlimit = StringUtils.canJoinToInt(oneView.getInlimitView().getText().toString());

        if(StringUtils.textIsEmpty(oneView.getIntroductionView().getText().toString())){
            nofifyShowMesg("请输入课程简介",oneView.getIntroductionView());
            return false;
        }
        if (oneView.getIntroductionView().getText().toString().length()<50 || oneView.getIntroductionView().getText().toString().length()>500) {
            nofifyShowMesg("课程简介字数范围为50~500字",oneView.getIntroductionView());
            return false;
        }
        if(RegUtil.containsEmoji(oneView.getIntroductionView().getText().toString())){
            nofifyShowMesg("禁止输入表情",oneView.getIntroductionView());
            return false;
        }
        data.introduction = oneView.getIntroductionView().getText().toString();

        if(StringUtils.textIsEmpty(oneView.getTargetView().getText().toString())){
            nofifyShowMesg("请输入教学目标",oneView.getTargetView());
            return false;
        }
        if (oneView.getTargetView().getText().toString().length()<40 || oneView.getTargetView().getText().toString().length()>400) {
            nofifyShowMesg("教学目标字数范围为40~400字",oneView.getTargetView());
            return false;
        }
        if(RegUtil.containsEmoji(oneView.getTargetView().getText().toString())){
            nofifyShowMesg("禁止输入表情",oneView.getTargetView());
            return false;
        }
        data.target = oneView.getTargetView().getText().toString();

        if(StringUtils.textIsEmpty(oneView.getFitcrowdView().getText().toString())){
            nofifyShowMesg("请输入适用人群",oneView.getFitcrowdView());
            return false;
        }

        if (oneView.getFitcrowdView().getText().toString().length()<30 || oneView.getFitcrowdView().getText().toString().length()>300) {
            nofifyShowMesg("适用人群字数范围为30~300字",oneView.getFitcrowdView());
            return false;
        }
        if(RegUtil.containsEmoji(oneView.getFitcrowdView().getText().toString())){
            nofifyShowMesg("禁止输入表情",oneView.getFitcrowdView());
            return false;
        }
        data.fitCrowd = oneView.getFitcrowdView().getText().toString();
        return true;
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
                .show(fragmentManager);
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

    public void setIClassCreateOneView(IClassCreateOneView oneView){
        this.oneView = oneView;
    }

    @Override
    public void onPhotoResult(String photoUrl) {
        data.pictureUrl = photoUrl;
        //ImageUtil.setItemRoundImageViewOnlyDisplay(oneView.getFaceView(), photoUrl);
        ImageUtil.loadImageViewLoding(context, photoUrl, oneView.getFaceView() ,R.mipmap.default_error,R.mipmap.default_error);
    }

    public void getPhoto() {
        int width = SystemInfoUtil.getScreenWidth()-40;
        CameraAlbumUtils.getInstance(context).setIPhotoCall(this).getPhoto(fragmentManager).setTailorType(VanCropType.CROP_TYPE_RECTANGLE).setParam(width,(int)(width*0.7));
    }

    public interface IClassCreateOneView{
        ImageView getFaceView();
        EditText getCourseNameView();
        TextView getSubjectNameView();
        TextView getStageView();
        EditText getNumberView();
        TextView getAddressView();
        EditText getAddressDetailView();
        EditText getTimesView();
        TextView getSingleTimeView();
        EditText getSinglePriceView();
        TextView getExperienceView();
        TextView getInlimitView();
        EditText getIntroductionView();
        EditText getTargetView();
        EditText getFitcrowdView();
        TextView getIntroductionNumView();
        TextView getTargetNumView();
        TextView getFitcrowdNumView();
        Button getNextView();
    }

}
