package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;

import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Message;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.view.RefreshCommonView;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/20.
 */

public class MineMessageView extends BaseView {
    private ArrayList<Message> messageArrayList =new ArrayList<>();
    private  SimpleAdapter adapter;

    public MineMessageView(Context context) {
        super(context);
    }

    public void initDatas(RefreshCommonView.RefreshLoadMoreListener listener) {
        adapter = new SimpleAdapter<Message>(mContext, messageArrayList, R.layout.item_mine_message) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Message data) {
                holder.setText(R.id.tv_meesage_content,data.getContent())
                        .setText(R.id.tv_meesage_time, DateUtil.timeStamp(data.getCreateTime(),"yyyy-MM-dd HH:mm"));
            }
        };
        iView.getRefreshCommonView().setRecyclerViewAdapter(adapter);
        iView.getRefreshCommonView().setRefreshLoadMoreListener(listener);
    }

    public ArrayList<Message> getMessageList() {
        return messageArrayList;
    }

    public void setResultDatas(BasePageBean<Message> myMessage) {
        finishLoad();
        messageArrayList.addAll(myMessage.getList());
        if (messageArrayList.isEmpty()){
            messageArrayList.clear();
            iView.getRefreshCommonView().setIsEmpty(true);
        }else{
            iView.getRefreshCommonView().setIsEmpty(false);
            iView.getRefreshCommonView().setIsLoadMore(myMessage.isHasNextPage());
        }
        adapter.notifyDataSetChanged();
    }

    public void finishLoad(){
        iView.getRefreshCommonView().finishRefresh();
        iView.getRefreshCommonView().finishLoadMore();
    }

    private IMineMessageView iView;
    public void setIMineMessageView(IMineMessageView iView){
        this.iView = iView;
    }
    public interface IMineMessageView{
        RefreshCommonView getRefreshCommonView();
    }

}
