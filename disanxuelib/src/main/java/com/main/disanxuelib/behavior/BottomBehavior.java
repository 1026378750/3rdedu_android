package com.main.disanxuelib.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/4/3.
 */

public class BottomBehavior extends CoordinatorLayout.Behavior {

    public BottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //依赖 AppBarLayout 的写法
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        int delta = dependency.getTop();
        child.setTranslationY(-delta);
        return true;
    }

//-----------依赖 RecycleView 的写法-----------
//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, final View child, View dependency) {
//        boolean b = "Tag".equals(dependency.getTag());
//        if (b && this.child == null) {
//            this.child = child;
//            ((RecyclerView) dependency).addOnScrollListener(mOnScrollListener);
//        }
//        return b;
//    }
//
//    View child;
//    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            float translationY = child.getTranslationY();
//            translationY += dy;
//            translationY = translationY > 0 ? (translationY > child.getHeight() ? child.getHeight() : translationY) : 0;
//            Log.e("translationY", translationY + "");
//            child.setTranslationY(translationY);
//        }
//    };
}
