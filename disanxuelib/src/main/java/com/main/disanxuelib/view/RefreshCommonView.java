package com.main.disanxuelib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.main.disanxuelib.R;
import com.main.disanxuelib.app.BaseApplication;
import com.main.disanxuelib.util.BaseStringUtils;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.refreshers.LoadMoreWithNomoreRefresher;
import com.mbg.library.DefaultNegativeRefreshers.NegativeRefresherWithNodata;
import com.mbg.library.DefaultPositiveRefreshers.PositiveRefresherWithText;
import com.mbg.library.IRefreshListener;
import com.mbg.library.RefreshRelativeLayout;

/**
 * Created by 通用刷新自定义控件 on 2017/12/11.
 * liukui
 */

public class RefreshCommonView extends LinearLayout{
    //刷新控件
    private RefreshRelativeLayout refreshRelativeLayout;
    private LoadMoreWithNomoreRefresher mRefresher;
    private RecyclerView recyclerView;
    private ListView listView;
    private GridView gridView;
    private ExpandableListView expandableListView;
    private NestedScrollView scrollView;
    private HorizontalScrollView hScrollView;
    private View noneView;
    private ImageView noneImage;
    private TextView noneText;
    /**
     * 标题栏的背景颜色
     */
    private int background_color;
    /****
     * 内容背景
     */
    private int content_bgColor;
    /**
     * 下拉刷新或者右滑刷新是否可用
     */
    private boolean positiveEnable;
    /**
     * 上滑加载更多或左滑加载是否可用
     */
    private boolean negativeEnable;
    /**
     * positve方向可拖拽或是滑动到边缘自动加载
     */
    private boolean positiveDragEnable;

    /******
     * negative方向可拖拽或是滑动到边缘自动加载
     */
    private boolean negativeDragEnable;

    /****
     * positive方向刷新控件显示时是否悬浮在子View上方
     */
    private boolean positiveOverlay;
    /****
     * negative方向刷新控件显示时是否悬浮在子View上方
     */
    private boolean negativeOverlay;
    /****
     * 分割线颜色
     */
    private int lineColor;
    /****
     * 分割线尺寸
     */
    private float lineSize;

    /**
     * 默认提供的positive刷新控件
     */
    private int positiveRefresher;
    /**
     * 容器类型
     */
    private int vesselType;
    /**
     * 布局方式水平或竖直(horizontal/vertical)
     */
    private int orientation;
    /****
     * 列数
     */
    private int rowNum;
    /*****
     * 无加载更多时展示文本
     */
    private String bottonViewText;
    /*****
     * 空界面文本展示
     */
    private String noneMessage;
    /*****
     * 空界面图片
     */
    private int noneImageId;
    /****
     * 偏移量
     */
    private float paddingSize;

    public RefreshCommonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**加载布局文件*/
        LayoutInflater.from(context).inflate(R.layout.common_refresh_base, this, true);
        refreshRelativeLayout = (RefreshRelativeLayout) findViewById(R.id.common_refresh_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.common_refresh_RecyclerView);
        listView = (ListView) findViewById(R.id.common_refresh_ListView);
        gridView = (GridView) findViewById(R.id.common_refresh_GridView);
        expandableListView = (ExpandableListView) findViewById(R.id.common_refresh_ExpandableListView);
        scrollView = (NestedScrollView) findViewById(R.id.common_refresh_NestedScrollView);
        hScrollView = (HorizontalScrollView) findViewById(R.id.common_refresh_HorizontalScrollView);
        noneView = findViewById(R.id.common_refresh_none);
        noneImage = (ImageView) findViewById(R.id.common_refresh_noneimage);
        noneText = (TextView) findViewById(R.id.common_refresh_nonetext);
        /**获取属性值*/
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshCommonView);
        background_color = typedArray.getColor(R.styleable.RefreshCommonView_refresh_bgColor, UiUtils.getColor(R.color.background_color));
        content_bgColor = typedArray.getColor(R.styleable.RefreshCommonView_content_bgColor, UiUtils.getColor(R.color.background_color));
        positiveEnable = typedArray.getBoolean(R.styleable.RefreshCommonView_positiveEnable,true);
        negativeEnable = typedArray.getBoolean(R.styleable.RefreshCommonView_negativeEnable,true);
        positiveDragEnable = typedArray.getBoolean(R.styleable.RefreshCommonView_positiveDragEnable,true);
        negativeDragEnable = typedArray.getBoolean(R.styleable.RefreshCommonView_negativeDragEnable,true);
        positiveOverlay = typedArray.getBoolean(R.styleable.RefreshCommonView_isPositiveOverlay,true);
        negativeOverlay = typedArray.getBoolean(R.styleable.RefreshCommonView_isNegativeOverlay,false);
        lineColor = typedArray.getColor(R.styleable.RefreshCommonView_halving_line_color, UiUtils.getColor(R.color.line_color));
        lineSize = typedArray.getDimension(R.styleable.RefreshCommonView_halving_line_size, 10);
        positiveRefresher = typedArray.getInt(R.styleable.RefreshCommonView_positive_refresher, 0);
        vesselType = typedArray.getInt(R.styleable.RefreshCommonView_vessel_type,0);
        orientation = typedArray.getInt(R.styleable.RefreshCommonView_orientation, 0);
        rowNum = typedArray.getInt(R.styleable.RefreshCommonView_row_num, 1);
        bottonViewText = typedArray.getString(R.styleable.RefreshCommonView_bottonViewText);
        noneMessage = typedArray.getString(R.styleable.RefreshCommonView_noneText);
        noneImageId = typedArray.getResourceId(R.styleable.RefreshCommonView_noneImage,R.mipmap.default_iamge);
        paddingSize = typedArray.getDimension(R.styleable.RefreshCommonView_paddingSize,0);

        typedArray.recycle();

        int padding = SystemInfoUtil.dip2px(paddingSize);
        getView().setPadding(padding,padding,padding,padding);
        setBackground();
        setVesselType();
        setParameter();
    }

    /**
     * 设置背景颜色
     */
    public void setBackground() {
        setBackgroundColor(background_color);
        getView().setBackgroundColor(content_bgColor);
    }

    public void setContentBgColor(int mColor){
        this.content_bgColor = mColor;
        getView().setBackgroundColor(content_bgColor);
    }

    private void setVesselType() {
        recyclerView.setVisibility(GONE);
        scrollView.setVisibility(GONE);
        listView.setVisibility(GONE);
        gridView.setVisibility(GONE);
        hScrollView.setVisibility(GONE);
        expandableListView.setVisibility(GONE);
        switch (vesselType){

            case 0:
                recyclerView.setVisibility(VISIBLE);
                break;

            case 1:
                scrollView.setVisibility(VISIBLE);
                break;

            case 2:
                listView.setVisibility(VISIBLE);
                break;

            case 3:
                gridView.setVisibility(VISIBLE);
                break;

            case 4:
                hScrollView.setVisibility(VISIBLE);
                break;

            case 5:
                expandableListView.setVisibility(VISIBLE);
                break;

        }
    }

    private void setParameter() {
        //设置刷新(系统默认不设置)
        //refreshRelativeLayout.setPositiveRefresher(new PositiveRefresherWithText(false));
        //设置是否可以刷新，加载更多
        if (refreshRelativeLayout==null)
            return;
        refreshRelativeLayout.setPositiveEnable(positiveEnable);
        refreshRelativeLayout.setNegativeEnable(negativeEnable);
        if (positiveEnable){
            //设置刷新颜色 必须放在setPositiveEnable方法过后
            refreshRelativeLayout.setProgressColor(UiUtils.getColor(BaseApplication.getInstance().getAppFrom().equals(BaseStringUtils.AppFromParent)?R.color.color_ffae12:R.color.color_ff1d97ea));
        }
        //拖拽或是滑动到边缘自动加载
        refreshRelativeLayout.setPositiveDragEnable(positiveDragEnable);
        refreshRelativeLayout.setNegativeDragEnable(negativeDragEnable);
        //设置刷新、加载组件是否显示在布局上面
        refreshRelativeLayout.setPositiveOverlayUsed(positiveOverlay);
        refreshRelativeLayout.setNegativeOverlayUsed(negativeOverlay);

        noneImage.setImageResource(noneImageId);
        noneText.setText(noneMessage);

        if(!TextUtils.isEmpty(bottonViewText)){
            mRefresher=new LoadMoreWithNomoreRefresher();
            refreshRelativeLayout.setNegativeRefresher(mRefresher);
            mRefresher.setNothing(bottonViewText);
        }else{
            //加载布局
            refreshRelativeLayout.setNegativeRefresher(new NegativeRefresherWithNodata(false));
        }
    }

    /*****
     * 获取容器布局
     * @param <T>
     * @return
     */
    public <T extends View> T getView() {
        return (T) getCurrentView();
    }

    /*****
     * 设置RecyclerView适配器
     * @param adapter
     */
    public void setRecyclerViewAdapter(RecyclerView.Adapter adapter){
        if(orientation==0){
            //垂直布局
            recyclerView.setLayoutManager(UiUtils.getGridLayoutManager(rowNum));
        }else{
            //水平布局
            recyclerView.setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.HORIZONTAL));
        }
        recyclerView.setAdapter(adapter);
    }

    /*****
     * 设置ListView适配器
     * @param adapter
     */
    public void setListViewAdapter(ListAdapter adapter){
        listView.setAdapter(adapter);
    }

    /*****
     * 设置GridView适配器
     * @param adapter
     */
    public void setGridViewAdapter(ListAdapter adapter){
        gridView.setColumnWidth(rowNum);
        gridView.setAdapter(adapter);
    }

    /*****
     * 设置ExpandableListView适配器
     * @param adapter
     */
    public void setExpandableListViewAdapter(ExpandableListAdapter adapter){
        expandableListView.setAdapter(adapter);
    }

    /*****
     * 去掉ExpandableListView默认箭头
     */
    public void setExpandableListArrows(){
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildDivider(null);
        expandableListView.setDivider(null);
        expandableListView.setDividerHeight(0);
       // expandableListView.setBackgroundResource(R.drawable.dialog_backgrounds);
    }

    /*****
     * 添加ScrollView子布局
     * @param layoutId
     */
    public void addScrollViewChild(int layoutId){
        ((NestedScrollView)getView()).addView(LayoutInflater.from(getContext()).inflate(layoutId, null, false));
    }

    /****
     * 设置是否可以刷新
     * @param bool
     */
    public void setIsRefresh(boolean bool){
        positiveEnable = bool;
        if (refreshRelativeLayout==null)
            return;
        refreshRelativeLayout.setPositiveEnable(positiveEnable);
    }

    /****
     * 设置是否可以加载更多
     * @param bool
     */
    public void setIsLoadMore(boolean bool){
        negativeEnable = bool;
        if(!TextUtils.isEmpty(bottonViewText)){
            mRefresher.setHasMore(bool);
        }else{
            if (refreshRelativeLayout==null)
                return;
            refreshRelativeLayout.setNegativeEnable(negativeEnable);
        }
    }


    /*****
     * 是否有数据
     * @param bool
     */
    public void setIsEmpty(boolean bool){
        if(bool){
            negativeEnable = false;
            noneView.setVisibility(VISIBLE);
            if (refreshRelativeLayout==null)
                return;
            refreshRelativeLayout.setNegativeEnable(negativeEnable);
        }else{
            noneView.setVisibility(GONE);
        }
    }

    /****
     * 添加刷新事件
     * @param mListener
     */

    private boolean isAutoLoad = true ;
    public void setRefreshLoadMoreListener(RefreshLoadMoreListener mListener){
        this.listener = mListener;
        if (refreshRelativeLayout==null)
            return;
        refreshRelativeLayout.addRefreshListener(new IRefreshListener() {
            @Override
            public void onPositiveRefresh() {
                if(!positiveEnable)
                    return;
                listener.startRefresh();
            }

            @Override
            public void onNegativeRefresh() {
                if(!negativeEnable)
                    return;
                listener.startLoadMore();
            }
        });
        if(isAutoLoad)
            refreshRelativeLayout.startPositiveRefresh();
    }

    /****
     * 设置是否自动加载
     * @param isAutoLoad
     */
    public void setIsAutoLoad(boolean isAutoLoad){
        this.isAutoLoad = isAutoLoad;
    }

    /****
     * 结束刷新
     */
    public void finishRefresh(){
        if (refreshRelativeLayout==null)
            return;
        refreshRelativeLayout.positiveRefreshComplete();
    }

    /****
     * 结束加载更多
     */
    public void finishLoadMore(){
        if (refreshRelativeLayout==null)
            return;
        refreshRelativeLayout.negativeRefreshComplete();
    }

    /****
     * 空界面图片
     * @param noneImageId
     */
    public void setEmptyImage(int noneImageId){
        this.noneImageId = noneImageId;
        noneImage.setImageResource(noneImageId);
    }

    /****
     * 空界面图片
     * @param text
     */
    public void setEmptyText(String text){
        this.noneMessage = text;
        noneText.setText(text);
    }

    /*****
     * 刷新
     */
    public void notifyData(){
        if (refreshRelativeLayout==null)
            return;
        refreshRelativeLayout.startPositiveRefresh();
    }

    public View getCurrentView(){
        View view = null;
        switch (vesselType){

            case 0:
                view = recyclerView;
                break;

            case 1:
                view = scrollView;
                break;

            case 2:
                view = listView;
                break;

            case 3:
                view = gridView;
                break;

            case 4:
                view = hScrollView;
                break;

            case 5:
                view = expandableListView;
                break;

        }
        return view;
    }

    private RefreshLoadMoreListener listener;

    public interface  RefreshLoadMoreListener{
        void startRefresh();
        void startLoadMore();
    }

}