package com.ldf.calendar.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ldf.calendar.utils.CalendarUtils;
import com.ldf.calendar.view.MonthPager;

public class RecyclerViewBehavior extends CoordinatorLayout.Behavior<RecyclerView> {
    private int initOffset = -1;
    private int minOffset = -1;
    private Context context;
    private boolean initiated = false;

    public RecyclerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, RecyclerView child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        MonthPager monthPager = getMonthPager(parent);
        initMinOffsetAndInitOffset(parent, child, monthPager);
        return true;
    }

    private void initMinOffsetAndInitOffset(CoordinatorLayout parent, RecyclerView child, MonthPager monthPager) {
        if (monthPager.getBottom() > 0 && initOffset == -1) {
            initOffset = monthPager.getViewHeight();
            saveTop(initOffset);
        }
        if (!initiated) {
            initOffset = monthPager.getViewHeight();
            saveTop(initOffset);
            initiated = true;
        }
        child.offsetTopAndBottom(CalendarUtils.getInstance().loadTop());
        minOffset = getMonthPager(parent).getCellHeight();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View directTargetChild, View target, int nestedScrollAxes) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) child.getLayoutManager();
        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0) {
            return false;
        }
        MonthPager monthPager = getMonthPager(coordinatorLayout);
        if (monthPager.getPageScrollState() != ViewPager.SCROLL_STATE_IDLE) {
            return false;
        }
        monthPager.setScrollable(false);
        boolean isVertical = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        int firstRowVerticalPosition = (child == null || child.getChildCount() == 0) ? 0 : child.getChildAt(0).getTop();
        boolean recycleviewTopStatus = firstRowVerticalPosition >= 0;
        return isVertical
                && (recycleviewTopStatus || !CalendarUtils.getInstance().isScrollToBottom())
                && child == directTargetChild;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        if (child.getTop() <= initOffset && child.getTop() >= getMonthPager(coordinatorLayout).getCellHeight()) {
            consumed[1] = CalendarUtils.getInstance().scroll(child, dy,
                    getMonthPager(coordinatorLayout).getCellHeight(),
                    getMonthPager(coordinatorLayout).getViewHeight());
            saveTop(child.getTop());
        }
    }

    @Override
    public void onStopNestedScroll(final CoordinatorLayout parent, final RecyclerView child, View target) {
        super.onStopNestedScroll(parent, child, target);
        MonthPager monthPager = getMonthPager(parent);
        monthPager.setScrollable(true);
        if (CalendarUtils.getInstance().getMoveY()<0 && !CalendarUtils.getInstance().isToTop()) {
            parent.dispatchDependentViewsChanged(child);
        }

        if (CalendarUtils.getInstance().getMoveY()>0 && !CalendarUtils.getInstance().isToBottom()) {
            parent.dispatchDependentViewsChanged(child);
            child.offsetTopAndBottom(CalendarUtils.getInstance().getMoveY());
        }
    }

    private MonthPager getMonthPager(CoordinatorLayout parent) {
        MonthPager monthPager = (MonthPager) parent.getChildAt(0);
        return monthPager;
    }

    private void saveTop(int top) {
        CalendarUtils.getInstance().saveTop(top);
        if (CalendarUtils.getInstance().loadTop() == initOffset) {
            CalendarUtils.getInstance().setScrollToBottom(false);
        } else if (CalendarUtils.getInstance().loadTop() == minOffset) {
            CalendarUtils.getInstance().setScrollToBottom(true);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, RecyclerView child, MotionEvent ev) {


        return super.onInterceptTouchEvent(parent, child, ev);
    }
}
