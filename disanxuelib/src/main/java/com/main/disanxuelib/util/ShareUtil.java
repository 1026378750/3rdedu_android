package com.main.disanxuelib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.disanxuelib.R;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.app.BaseApplication;
import com.main.disanxuelib.bean.AppInfoBean;
import com.main.disanxuelib.bean.ShareBean;
import com.main.disanxuelib.view.dialog.BaseNiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialog;
import com.main.disanxuelib.view.dialog.NiceDialogViewHolder;
import com.main.disanxuelib.view.dialog.ProgressBarDialog;
import com.main.disanxuelib.view.dialog.ViewConvertListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.description;

/**
 * Created by 分享工具 on 2018/4/13.
 */

public class ShareUtil implements LongPictureUtil.PictureResultListener {
    private static ShareUtil shareUtil;
    private Context context;
    private FragmentManager fragmentManager;
    private NiceDialog niceDialog;
    private List<AppInfoBean> shareList;
    private String title;
    private LongPictureUtil longPictureUtil;
    private static int ic_logo;


    public static ShareUtil getInsatnce() {
        shareUtil = new ShareUtil();
        return shareUtil;
    }

    public ShareUtil setShareLogo(int mLogo){
        ic_logo = mLogo;
        return shareUtil;
    }

    public ShareUtil initDatas(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        if (longPictureUtil == null)
            longPictureUtil = new LongPictureUtil(context, fragmentManager);
        longPictureUtil.setPictureResultListener(this);
        return shareUtil;
    }

    public ShareUtil setTitle(String title) {
        this.title = title;
        return shareUtil;
    }

    public ShareUtil setExtoryBitmap(Bitmap extoryBitmap) {
        longPictureUtil.setExtroyBitmap(extoryBitmap);
        return shareUtil;
    }

    private ShareBean shareBean;

    public ShareUtil show(ShareBean shareBean) {
        this.shareBean = shareBean;
        longPictureUtil.setViewPageIndex(this.shareBean.viewPagerIndex);
        if (shareList == null || shareList.isEmpty()) {
            addMoreOperator();
        }
        if (niceDialog == null)
            niceDialog = NiceDialog.init();
        niceDialog.setLayoutId(R.layout.common_popup_share)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
                        if (!BaseStringUtils.textIsEmpty(title))
                            holder.setText(R.id.tv_share_title, "分享至");
                        RecyclerView recyclerView = holder.getView(R.id.rv_share_content);
                        recyclerView.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.HORIZONTAL));
                        recyclerView.setAdapter(new MyRecyclerViewAdapter(shareList));
                        setSystemShare(holder, R.id.ll_share_email, getEmail());
                        setSystemShare(holder, R.id.ll_share_notes, getNote());
                        setSystemShare(holder, R.id.ll_share_picture, getLongPicture());
                        holder.setOnClickListener(R.id.tv_share_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                colseDialog();
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(fragmentManager);
        return shareUtil;
    }

    private void setSystemShare(NiceDialogViewHolder holder, int viewId, final AppInfoBean emailBean) {
        View shareLayout = holder.getView(viewId);
        ImageView emailIcon = (ImageView) shareLayout.findViewById(R.id.im_share_icon);
        TextView emailtText = (TextView) shareLayout.findViewById(R.id.tv_share_item);
        emailIcon.setImageDrawable(emailBean.getAppIcon());
        emailtText.setText(emailBean.getAppLabel());
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorShare(emailBean);
            }
        });
    }

    /*****
     * 添加其他操作
     */
    private void addMoreOperator() {
        if (shareList == null)
            shareList = new ArrayList<>();

        AppInfoBean appInfo = new AppInfoBean();
        appInfo.setAppLabel("微信");
        appInfo.setAppName("微信");
        appInfo.setAppIcon(UiUtils.getDrawable(R.mipmap.umeng_socialize_wechat));
        shareList.add(appInfo);

        appInfo = new AppInfoBean();
        appInfo.setAppLabel("朋友圈");
        appInfo.setAppName("微信");
        appInfo.setAppIcon(UiUtils.getDrawable(R.mipmap.umeng_socialize_wxcircle));
        shareList.add(appInfo);

        appInfo = new AppInfoBean();
        appInfo.setAppLabel("微博");
        appInfo.setAppName("新浪微博");
        appInfo.setAppIcon(UiUtils.getDrawable(R.mipmap.umeng_socialize_sina));
        shareList.add(appInfo);

        appInfo = new AppInfoBean();
        appInfo.setAppLabel("支付宝");
        appInfo.setAppName("阿里巴巴");
        appInfo.setAppIcon(UiUtils.getDrawable(R.mipmap.umeng_socialize_alipay));
        shareList.add(appInfo);

        appInfo = new AppInfoBean();
        appInfo.setAppLabel("QQ");
        appInfo.setAppName("腾讯");
        appInfo.setAppIcon(UiUtils.getDrawable(R.mipmap.umeng_socialize_qq));
        shareList.add(appInfo);

        appInfo = new AppInfoBean();
        appInfo.setAppLabel("QQ空间");
        appInfo.setAppName("腾讯");
        appInfo.setAppIcon(UiUtils.getDrawable(R.mipmap.umeng_socialize_qzone));
        shareList.add(appInfo);

    }

    private AppInfoBean getEmail() {
        AppInfoBean appInfo = new AppInfoBean();
        appInfo.setAppLabel("邮件");
        appInfo.setAppName("系统");
        appInfo.setAppIcon(UiUtils.getDrawable(R.mipmap.umeng_socialize_email));
        return appInfo;
    }

    private AppInfoBean getNote() {
        AppInfoBean appInfo = new AppInfoBean();
        appInfo.setAppLabel("短信");
        appInfo.setAppName("系统");
        appInfo.setAppIcon(UiUtils.getDrawable(R.mipmap.umeng_socialize_notes));
        return appInfo;
    }

    private AppInfoBean getLongPicture() {
        AppInfoBean appInfo = new AppInfoBean();
        appInfo.setAppLabel("长图");
        appInfo.setAppName("系统");
        appInfo.setAppIcon(UiUtils.getDrawable(R.mipmap.umeng_socialize_picture));
        return appInfo;
    }


    private void colseDialog() {
        if (niceDialog != null && niceDialog.isVisible()) {
            niceDialog.dismiss();
        }
    }

    private class MyRecyclerViewAdapter extends SimpleAdapter<AppInfoBean> {

        public MyRecyclerViewAdapter(List<AppInfoBean> datas) {
            super(context, datas, R.layout.item_share);
        }

        @Override
        protected void onBindViewHolder(TrdViewHolder holder, final AppInfoBean data) {
            holder.setText(R.id.tv_share_item, data.getAppLabel());
            if (data.getAppIcon() != null)
                holder.setImageDrawable(R.id.im_share_icon, data.getAppIcon());
            holder.setOnItemListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    operatorShare(data);
                }
            });
        }
    }

    private void operatorShare(AppInfoBean data) {
        if (data.getAppLabel().equals("微信")) {
            platform = SHARE_MEDIA.WEIXIN;
            shareImage("微信");
        } else if (data.getAppLabel().equals("朋友圈")) {
            platform = SHARE_MEDIA.WEIXIN_CIRCLE;
            shareImage("微信朋友圈");
        } else if (data.getAppLabel().equals("微博")) {
            String mesg = "新浪微博";
            if (!isWeiboInstalled()) {
                if (listener != null)
                    listener.selectShareItem(mesg + "没有安装");
                return;
            }
            platform = SHARE_MEDIA.SINA;
            listener.selectLoggPicture(mesg);
        } else if (data.getAppLabel().equals("支付宝")) {
            platform = SHARE_MEDIA.ALIPAY;
            shareImage("支付宝");
        } else if (data.getAppLabel().equals("QQ")) {
            platform = SHARE_MEDIA.QQ;
            listener.selectLoggPicture("QQ");
        } else if (data.getAppLabel().equals("QQ空间")) {
            platform = SHARE_MEDIA.QZONE;
            listener.selectLoggPicture("QQ空间");
        } else if (data.getAppLabel().equals("邮件")) {
            platform = SHARE_MEDIA.EMAIL;
            shareEmail("邮件");
        } else if (data.getAppLabel().equals("短信")) {
            platform = SHARE_MEDIA.SMS;
            shareSms("短信");
        } else if (data.getAppLabel().equals("长图")) {
            platform = null;
            listener.selectLoggPicture("图片已经保存到系统相册");
        }
        colseDialog();
    }

    private SHARE_MEDIA platform;
    private ProgressBarDialog dialog = null;

    private void shareMessage(final String mesg) {
        UMWeb umWeb = new UMWeb("https://www.jianshu.com/u/38dae1253ae5");//连接地址
        umWeb.setTitle("推荐老师");//标题
        umWeb.setDescription("向你推荐一位超强的老师");//描述
        umWeb.setThumb(new UMImage(context, ic_logo));
        new ShareAction(AppManager.getAppManager().currentActivity())
                .setPlatform(this.platform)
                //  .withText(BaseStringUtils.textIsEmpty(shareBean.title) ? "第三学堂分享" : shareBean.title)
                .withMedia(umWeb)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        if (listener != null && platform != SHARE_MEDIA.SMS && platform != SHARE_MEDIA.EMAIL)
                            listener.selectShareItem(mesg + "分享成功");
                        if (dialog != null && dialog.isShowing())
                            dialog.closeProgress();
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (listener != null && platform != SHARE_MEDIA.SMS && platform != SHARE_MEDIA.EMAIL)
                            listener.selectShareItem(mesg + "分享失败");
                        if (dialog != null && dialog.isShowing())
                            dialog.closeProgress();
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        if (listener != null && platform != SHARE_MEDIA.SMS && platform != SHARE_MEDIA.EMAIL)
                            listener.selectShareItem(mesg + "分享取消");
                        if (dialog != null && dialog.isShowing())
                            dialog.closeProgress();
                    }
                }).share();
    }

    private void shareEmail(final String mesg) {
        new ShareAction(AppManager.getAppManager().currentActivity())
                .setPlatform(this.platform)
                .withText("推荐老师 向你推荐一位超强的老师 https://www.jianshu.com/u/38dae1253ae5 ")
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        if (listener != null && platform != SHARE_MEDIA.SMS && platform != SHARE_MEDIA.EMAIL)
                            listener.selectShareItem(mesg + "分享成功");
                        if (dialog != null && dialog.isShowing())
                            dialog.closeProgress();
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (listener != null && platform != SHARE_MEDIA.SMS && platform != SHARE_MEDIA.EMAIL)
                            listener.selectShareItem(mesg + "分享失败");
                        if (dialog != null && dialog.isShowing())
                            dialog.closeProgress();
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        if (listener != null && platform != SHARE_MEDIA.SMS && platform != SHARE_MEDIA.EMAIL)
                            listener.selectShareItem(mesg + "分享取消");
                        if (dialog != null && dialog.isShowing())
                            dialog.closeProgress();
                    }
                }).share();
    }

    private void shareSms(final String mesg) {
        new ShareAction(AppManager.getAppManager().currentActivity())
                .setPlatform(this.platform)
                .withText("推荐老师 向你推荐一位超强的老师 https://www.jianshu.com/u/38dae1253ae5 ")
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        if (listener != null && platform != SHARE_MEDIA.SMS && platform != SHARE_MEDIA.EMAIL)
                            listener.selectShareItem(mesg + "分享成功");
                        if (dialog != null && dialog.isShowing())
                            dialog.closeProgress();
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (listener != null && platform != SHARE_MEDIA.SMS && platform != SHARE_MEDIA.EMAIL)
                            listener.selectShareItem(mesg + "分享失败");
                        if (dialog != null && dialog.isShowing())
                            dialog.closeProgress();
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        if (listener != null && platform != SHARE_MEDIA.SMS && platform != SHARE_MEDIA.EMAIL)
                            listener.selectShareItem(mesg + "分享取消");
                        if (dialog != null && dialog.isShowing())
                            dialog.closeProgress();
                    }
                }).share();
    }

    public void shareImage(final String mesg) {
        if (this.platform!=null){
            shareMessage(mesg);
            return;
        }
        if (dialog == null) {
            dialog = new ProgressBarDialog(context);
        }
        if (dialog != null) {
            dialog.showProgress();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                longPictureUtil.setMesgFrom(mesg);
                longPictureUtil.getLongViewPicture(dialog);
            }
        }, 500);
    }

    public void setShareSelectListener(ShareSelectListener listener) {
        this.listener = listener;
    }

    private ShareSelectListener listener;

    public interface ShareSelectListener {
        void selectShareItem(String mesg);
        void selectLoggPicture(String mesg);
    }

    private boolean isWeiboInstalled() {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.sina.weibo".equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCutLongPictureResult(Bitmap bitmap, final String mesg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.closeProgress();
        }
        if (platform == null) {
            String path = FileUtil.cutoutActivity(bitmap);
            LogUtils.d("长图地址："+path);
            if (listener != null)
                listener.selectShareItem(mesg);
            return;
        }
    }

}
