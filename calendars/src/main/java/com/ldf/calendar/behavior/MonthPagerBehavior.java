package com.ldf.calendar.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import com.ldf.calendar.adapter.CalendarViewAdapter;
import com.ldf.calendar.utils.CalendarUtils;
import com.ldf.calendar.view.MonthPager;

/**
 * Created by ldf on 17/6/15.
 */

public class MonthPagerBehavior extends ViewOffsetBehavior<MonthPager> {
    private int top = 0;
    private int touchSlop = 1;
    private int offsetY = 0;
    private Context context;

    public MonthPagerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, MonthPager child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, MonthPager child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        child.offsetTopAndBottom(top);
        return true;
    }

    private int dependentViewTop = -1;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, MonthPager child, View dependency) {
        //dependency对其依赖的view(本例依赖的view是RecycleView)
        top = child.getTop();
        int dy = dependency.getTop() - dependentViewTop;
        boolean isTop = false;
        boolean isBottom = false;
        if (dy<0){//dy为负时表示向上滑动
            int sub = Math.abs((Math.abs(top) - child.getCellHeight()*child.getRowIndex()));
            if (sub > Math.abs(dy)){
                isTop = false;
            }else{
                dy = -sub;
                isTop = true;
                // dy为零时表示滑动停止
                child.offsetTopAndBottom(dy);
            }
        }else if(dy>0){//dy为正时表示向下滑动
            int sub = Math.abs(top);
            if (dy<sub){
                isBottom = false;
            }else{
                dy = sub;
                isBottom = true;
                // dy为零时表示滑动停止
                child.offsetTopAndBottom(dy);
            }
        }

        CalendarUtils.getInstance().setToTop(isTop);
        CalendarUtils.getInstance().setToBottom(isBottom);
        CalendarUtils.getInstance().setMoveY(dy);

        if (dependentViewTop != -1&&((isTop && dy<0)||(isBottom && dy>0))) {
            if (isBottom && dy>0)
                dependentViewTop = -1;
            return true;
        }
        // dy为零时表示滑动停止
        child.offsetTopAndBottom(dy);
        dependentViewTop = dependency.getTop();
        return true;
    }
}
