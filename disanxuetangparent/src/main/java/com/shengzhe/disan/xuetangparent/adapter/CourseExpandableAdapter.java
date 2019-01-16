package com.shengzhe.disan.xuetangparent.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.CourseSubject;
import com.main.disanxuelib.bean.CourseType;
import com.main.disanxuelib.util.UiUtils;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程二级三级列表
 */
public class CourseExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CourseType> groupData;
    public Map<Integer,CourseType> selectMap = new HashMap<>();
    private String tag = "";

    public CourseExpandableAdapter(Context c, List<CourseType> groupData,String tag) {
        this.context = c;
        this.groupData = groupData;
        this.selectMap.clear();
        this.tag = tag;
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
        if (groupData.get(groupPosition) != null
                && groupData.get(groupPosition).childList != null
                && groupData.get(groupPosition).childList.size() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupData.get(groupPosition).childList.get(childPosition);
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
            convertView = View.inflate(context, R.layout.course_class2_item, null);
            holder.title_name = (TextView) convertView.findViewById(R.id.class2_text);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.title_name.setText(groupData.get(groupPosition).name);
        holder.title_name.setVisibility(TextUtils.isEmpty(groupData.get(groupPosition).name)?View.GONE:View.VISIBLE);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = View.inflate(context, R.layout.common_recycle_notitle, null);
            holder.commonRecyview = (RecyclerView) convertView.findViewById(R.id.rv_recycle_review);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.commonRecyview.setBackgroundColor(UiUtils.getColor(R.color.color_ffffff));
        //tag列表
        //把LayoutManager设置给RecyclerView
        holder.commonRecyview.setLayoutManager(UiUtils.getGridLayoutManager(4));

        holder.commonRecyview.setAdapter(new SimpleAdapter<CourseSubject>(context,groupData.get(groupPosition).childList,R.layout.item_select_tag) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder,final CourseSubject data) {
                holder.setText(R.id.history_text,data.name);
                RadioButton button = holder.getView(R.id.history_text);
                button.setText(data.name);
                boolean isChecked = (groupData.get(groupPosition)!=null&&selectMap.containsKey(groupData.get(groupPosition).id)&&selectMap.get(groupData.get(groupPosition).id).id==data.id)
                        ||(tag.equals(StringUtils.select_order)?(!selectMap.containsKey(groupData.get(groupPosition).id)&&(data.name.equals("不限")||data.name.equals("不限年级"))):(selectMap.isEmpty()&&(data.name.equals("不限")||data.name.equals("不限年级"))));
                button.setChecked(isChecked);
                holder.setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CourseType ty = new CourseType();
                                ty.id = data.id;
                                ty.name = data.name;
                                listener.tagOnClickListener(v,ty,groupData.get(groupPosition).id);
                            }
                        });
            }
        });
        return convertView;
    }

    static class ChildViewHolder {
        RecyclerView commonRecyview;
    }

    static class GroupViewHolder {
        TextView title_name;
    }

    public void setTagListener(SelectTagListener listener){
        this.listener = listener;
    }

    private  SelectTagListener listener;

    public interface SelectTagListener{
        void tagOnClickListener(View v, CourseType type,int id);
    }

}
