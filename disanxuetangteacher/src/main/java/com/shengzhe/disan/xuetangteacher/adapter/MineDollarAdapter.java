package com.shengzhe.disan.xuetangteacher.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.DateUtil;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangteacher.R;
import com.shengzhe.disan.xuetangteacher.bean.PresentRecordBean;
import com.shengzhe.disan.xuetangteacher.bean.WithdrawalsRecordListBean;
import java.util.List;

/**
 * Created by liukui on 2017/12/18.
 *
 * 记录适配器
 *
 */

public class MineDollarAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<PresentRecordBean> groupData;

    public MineDollarAdapter(Context c, List<PresentRecordBean> groupData) {
        this.context = c;
        this.groupData = groupData;
    }

    @Override
    public int getGroupCount() {
        if (groupData != null && groupData.size() > 0) {
            return groupData.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupData.get(groupPosition).getWithdrawalsRecordList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupData.get(groupPosition).getWithdrawalsRecordList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = View.inflate(context, R.layout.item_mine_dollars, null);
            holder.title_name = (TextView) convertView.findViewById(R.id.tv_mine_dollars_month);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        if(DateUtil.timeStampDate(groupData.get(groupPosition).getTimeDay(),"MM").equals("12")){
            holder.title_name.setText(DateUtil.timeStamp(groupData.get(groupPosition).getTimeDay(),"yyyy年MM月"));
        }else {
            holder.title_name.setText(Integer.parseInt(DateUtil.timeStamp(groupData.get(groupPosition).getTimeDay(),"MM"))+"月");
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = View.inflate(context, R.layout.item_mine_record, null);
            holder.tv_order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
            holder.tv_mine_dollars = (TextView) convertView.findViewById(R.id.tv_mine_dollars);
            holder.tv_mine_class_name = (TextView) convertView.findViewById(R.id.tv_mine_class_name);
            holder.bt_mine_type = (TextView) convertView.findViewById(R.id.bt_mine_type);
            holder.tv_mine_dollars = (TextView) convertView.findViewById(R.id.tv_mine_dollars);
            holder.v_minechild=(View) convertView.findViewById(R.id.v_minechild);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        WithdrawalsRecordListBean data = (WithdrawalsRecordListBean)getChild(groupPosition,childPosition);
        holder.tv_order_number.setText("全额提现");
        holder.tv_mine_dollars.setText("¥"+ ArithUtils.round(data.getAmount()));
        holder.v_minechild.setVisibility(isLastChild==true?View.GONE:View.VISIBLE);
        holder.tv_mine_class_name.setText(DateUtil.timeStampDate(data.getCreateTime(),"MM")+"月"+DateUtil.timeStampDate(data.getCreateTime(),"dd HH:mm"));
        //审核状态 1、待审核 2、审核中 3、审核通过 4、驳回
        switch (data.getAuditStatus()){
            case 1:
                holder.bt_mine_type.setText("待审核");
                holder.bt_mine_type.setTextColor(UiUtils.getColor(R.color.color_d92b2b));
                holder.tv_mine_dollars.setTextColor(UiUtils.getColor(R.color.color_333333));
                break;
            case 2:
                holder.bt_mine_type.setText("审核中");
                holder.bt_mine_type.setTextColor(UiUtils.getColor(R.color.color_d92b2b));
                holder.tv_mine_dollars.setTextColor(UiUtils.getColor(R.color.color_333333));
                break;
            case 3:
                holder.bt_mine_type.setText("提现成功");
                holder.bt_mine_type.setTextColor(UiUtils.getColor(R.color.color_1d97ea));
                holder.tv_mine_dollars.setTextColor(UiUtils.getColor(R.color.color_1d97ea));
                break;
            case 4:
                holder.bt_mine_type.setText("提现失败");
                holder.bt_mine_type.setTextColor(UiUtils.getColor(R.color.color_999999));
                holder.tv_mine_dollars.setTextColor(UiUtils.getColor(R.color.color_333333));
                break;
        }
        return convertView;
    }

    static class ChildViewHolder {
        TextView tv_order_number,tv_mine_dollars,tv_mine_class_name,bt_mine_type;
        View v_minechild;
    }

    static class GroupViewHolder {
        TextView title_name;
    }
}
