package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.text.Selection;
import android.widget.EditText;
import com.main.disanxuelib.view.CommonCrosswiseBar;

/**
 * Created by Administrator on 2018/4/17.
 */

public class ModifyMessageView extends BaseView {
    public ModifyMessageView(Context context) {
        super(context);
    }

    private IModifyMessageView iView;
    public void setIModifyMessageView(IModifyMessageView iView){
        this.iView = iView;
    }

    public void setDatas(String title,String hintText,String text) {
        iView.getTitleView().setTitleText(title);
        iView.getContentView().setHint(hintText);
        iView.getContentView().setText(text);
        //设置光标
        Selection.setSelection(iView.getContentView().getText(), iView.getContentView().getText().toString().length());
    }

    public interface IModifyMessageView{
        CommonCrosswiseBar getTitleView();
        EditText getContentView();
    }
}
