package com.shengzhe.disan.xuetangteacher.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.main.disanxuelib.R;
import com.main.disanxuelib.bean.VersionBean;
import com.main.disanxuelib.util.FileUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.http.AbsAPICallback;
import com.shengzhe.disan.xuetangteacher.http.Http;
import com.shengzhe.disan.xuetangteacher.http.exception.ResultException;
import com.shengzhe.disan.xuetangteacher.http.service.HttpService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/******
 * 版本检测、下载
 *
 * liukui
 *
 */
public class APKVersionUtil {
    private static HttpService httpService;
    private static OkHttpClient okHttpClient;
    private static APKVersionUtil versionUtil;
    private static Context context;
    /**
     * 文件目录
     */
    private String APK_DIR = "";
    private String fielName = "";
    private OnVersionUpdateCallback callback;

    /*****
     * 初始化
     * @return
     */
    public static APKVersionUtil getInstance(Context mContext) {
        context = mContext;
        if (versionUtil == null) {
            versionUtil = new APKVersionUtil();
        }
        if (httpService == null) {
            httpService = Http.getHttpService();
        }
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return versionUtil;
    }

    /*****
     * 获取回调函数
     * @param callback
     * @return
     */
    public APKVersionUtil setUpdateCallback(OnVersionUpdateCallback callback) {
        this.callback = callback;
        return versionUtil;
    }

    /****
     * 回调方法
     */
    public interface OnVersionUpdateCallback {
        void onCanUpdate(String mesg, boolean bool);

        void onUpdateSucced(String fileUrl);

        void onIgnoreUpdate();

        void onUpdateFaild(String mesg);
    }

    private VersionBean versionBean;

    /**
     * 检查版本更新，保存更新的数据
     */
    public void checkVersion() {
        httpService.appVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsAPICallback<VersionBean>(context, true) {
                    @Override
                    protected void onDone(VersionBean mVersionBean) {
                        versionBean = mVersionBean;
                        //不需要权限
                        APK_DIR = context.getExternalCacheDir().getAbsolutePath();
                        //fielName = "3rd_parent_" + versionBean.id + ".apk";
                        fielName = getNameFromUrl(versionBean.url);
                        //是否不需要升级
                        if (versionBean.versionType == 1 || StringUtils.compareVersions(versionBean.appVersion)!=-1){
                            //无需更新
                            FileUtil.clearAllFile(APK_DIR);
                            callback.onIgnoreUpdate();
                            return;
                        }
                        versionBean.remark=versionBean.remark.replaceAll("&rdquo","”").replaceAll("&ldquo","“");
                        callback.onCanUpdate(versionBean.remark, versionBean.forceUpdate == 1);
                    }

                    @Override
                    public void onResultError(ResultException ex) {
                        //网络错误
                        callback.onUpdateFaild("网络请求失败");
                    }
                });
    }

    /******
     * 更新下载次数
     */
    private void updateVersion() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appVersionId",versionBean.id);
        httpService.updateVersion(RequestBodyUtils.setRequestBody(map))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /**
     * 检查文件是否存在
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void checkApk() {
        File file = new File(APK_DIR + File.separator + fielName);
        if (file != null && file.exists()) {//文件是否存在
            //存在直接安装
            callback.onUpdateSucced(APK_DIR + File.separator + fielName);
        } else {
            downloadFile(versionBean);
        }
    }

    public String getAPKPath(){
        return APK_DIR + File.separator + fielName;
    }

    /**
     * 下载连接
     */
    private AlertDialog dialog;
    private ProgressBar progressBar;
    private TextView progressText;
    private void downloadFile(final VersionBean versionBean) {
        if (TextUtils.isEmpty(versionBean.url)) {
            //服务器错误
            callback.onUpdateFaild("下载地址错误");
            return;
        }
        dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        window.setContentView(R.layout.updata_progress_dialog);
        progressBar = (ProgressBar) dialog.findViewById(R.id.horizontal_progress);
        progressText = (TextView) dialog.findViewById(R.id.update_process_text);
        final TextView updataInfoText = (TextView) dialog.findViewById(R.id.update_info);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(UiUtils.getDrawable(R.drawable.progress_bg_teacher));
        updataInfoText.setText(versionBean.remark);
        progressText.setTextColor(UiUtils.getColor(R.color.color_ff1d97ea));

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadFile();
            }
        }).start();
    }

    private void loadFile(){
        okHttpClient.newCall(new Request.Builder().url(versionBean.url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                dialog.dismiss();
                callback.onUpdateFaild("安装包下载失败，请检查存储权限是否打开");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(APK_DIR);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(versionBean.url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        Message message = new Message();
                        message.what = loadProgress;
                        message.obj = progress;
                        handler.sendMessage(message);
                    }
                    fos.flush();
                    // 下载完成
                    handler.sendEmptyMessage(loadCompl);
                } catch (Exception e) {
                    dialog.dismiss();
                    callback.onUpdateFaild("安装包下载失败，请检查存储权限是否打开");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        callback.onUpdateFaild("安装包下载失败，请检查存储权限是否打开");
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.mkdirs();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private int loadProgress = 10000;
    private int loadCompl = 10001;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==loadProgress){
                int progress = (int) msg.obj;
                progressBar.setProgress(progress);
                progressText.setText(String.format("%d %%", progress));
                return;
            }
            if (msg.what==loadCompl){
                dialog.dismiss();
                callback.onUpdateSucced(APK_DIR + File.separator + fielName);
                updateVersion();
            }
        }
    };

}