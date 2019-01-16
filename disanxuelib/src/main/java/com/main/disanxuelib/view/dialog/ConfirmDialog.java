package com.main.disanxuelib.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.main.disanxuelib.R;
import com.main.disanxuelib.app.BaseApplication;
import com.main.disanxuelib.util.BaseStringUtils;
import com.main.disanxuelib.util.UiUtils;

/**
 * 提示对话框
 *
 * liukui 2017/11/23 10:05
 *
 */

public class ConfirmDialog extends BaseNiceDialog {

    private  String title,message,leftBtn,rightBtn;
    private int gravity = Gravity.CENTER;

    /****
     * 初始化
     * @param title 标题
     * @param message 信息
     * @param leftBtn 左边按钮
     * @param rightBtn 右边按钮
     * @return 实体
     */
    public static ConfirmDialog newInstance(String title,String message,String leftBtn,String rightBtn) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("leftBtn", leftBtn);
        bundle.putString("rightBtn", rightBtn);
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setMessageGravity(int gravity){
        this.gravity = gravity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        title = bundle.getString("title");
        message = bundle.getString("message");
        leftBtn = bundle.getString("leftBtn");
        rightBtn = bundle.getString("rightBtn");
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_layout;
    }

    @Override
    public void convertView(NiceDialogViewHolder holder, final BaseNiceDialog dialog) {
        holder.setText(R.id.tv_dialog_title, title);
        TextView message = holder.getView(R.id.tv_dialog_message);
        //message.setText(BaseStringUtils.textFormatHtml(this.message.replace("\\r","")));
        message.setText(BaseStringUtils.textFormatHtml(this.message));
        message.setGravity(gravity);
        holder.setText(R.id.tv_dialog_cancel, leftBtn);
        holder.setText(R.id.tv_dialog_ok, rightBtn);
        message.setMovementMethod(ScrollingMovementMethod.getInstance());
        holder.setTextColor(R.id.tv_dialog_ok, UiUtils.getColor(BaseApplication.getInstance().getAppFrom().equals(BaseStringUtils.AppFromParent)?R.color.color_ffae12:R.color.color_ff1d97ea));
        holder.setVisibility(R.id.tv_dialog_title,!TextUtils.isEmpty(title));
        holder.setVisibility(R.id.tv_dialog_message,!TextUtils.isEmpty(this.message));
        holder.setVisibility(R.id.tv_dialog_cancel,!TextUtils.isEmpty(leftBtn));
        holder.setVisibility(R.id.tv_dialog_ok,!TextUtils.isEmpty(rightBtn));
        holder.setVisibility(R.id.v_dialog_line,!TextUtils.isEmpty(leftBtn)&&!TextUtils.isEmpty(rightBtn));
        holder.setOnClickListener(R.id.tv_dialog_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener!=null)
                    listener.dialogStatus(R.id.tv_dialog_cancel);
            }
        });

        holder.setOnClickListener(R.id.tv_dialog_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener!=null)
                    listener.dialogStatus(R.id.tv_dialog_ok);
            }
        });
    }

    private  ConfirmDialogListener listener;

    public void  setConfirmDialogListener(ConfirmDialogListener listener){
        this.listener = listener;
    }

    public interface ConfirmDialogListener{
        public void dialogStatus(int id);
    }

}
