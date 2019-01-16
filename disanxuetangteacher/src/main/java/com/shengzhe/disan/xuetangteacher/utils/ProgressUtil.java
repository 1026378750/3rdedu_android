package com.shengzhe.disan.xuetangteacher.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shengzhe.disan.xuetangteacher.R;


/**
 * Created by hy
 * 2015/11/13 09:05
 * Note :
 */
public class ProgressUtil {

    private  AlertDialog dialog;
    ProgressBar progressBar;
    TextView tv_progress;

    /**
     * 显示进度条Dialog
     *
     * @param context 上下文
     */
    public  void showProgress(Context context) {

        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.customer_dialog_progress);

        progressBar = ((ProgressBar) window.findViewById(R.id.customer_dialog_progress_pb_Progress));
        tv_progress = ((TextView) window.findViewById(R.id.customer_dialog_pragress_tv_Progress));
        progressBar.setMax(100);

        dialog.setCancelable(false);
    }

    /**
     * 关闭dialog
     */
    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    /**
     * 设置进度
     *
     * @param l  当前下载
     * @param l1 总量
     */
    public void setProgress(long l, long l1) {
        double num = l / (double) l1;
        int i = (int) (num * 100);
        if (l >= l1) {
            dialog.dismiss();
        }
        tv_progress.setText(i + "%");
        progressBar.setProgress(i);
    }
}