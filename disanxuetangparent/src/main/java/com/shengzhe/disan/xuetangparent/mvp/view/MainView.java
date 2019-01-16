package com.shengzhe.disan.xuetangparent.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.main.disanxuelib.adapter.SimpleAdapter.SimpleAdapter;
import com.main.disanxuelib.adapter.SimpleAdapter.TrdViewHolder;
import com.main.disanxuelib.bean.Subject;
import com.main.disanxuelib.util.ArithUtils;
import com.main.disanxuelib.util.CityDaoUtil;
import com.main.disanxuelib.util.ImageUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.main.disanxuelib.util.UiUtils;
import com.main.disanxuelib.view.MyFloatingActionButton;
import com.main.disanxuelib.view.banner.BannerBean;
import com.main.disanxuelib.view.banner.MyBanner;
import com.main.disanxuelib.view.refreshers.LoadMoreWithNomoreRefresher;
import com.mbg.library.IRefreshListener;
import com.mbg.library.RefreshRelativeLayout;
import com.shengzhe.disan.xuetangparent.R;
import com.main.disanxuelib.bean.BasePageBean;
import com.shengzhe.disan.xuetangparent.bean.LiveAndVideo;
import com.shengzhe.disan.xuetangparent.bean.SquadBean;
import com.shengzhe.disan.xuetangparent.bean.TeacherInformation;
import com.shengzhe.disan.xuetangparent.bean.User;
import com.shengzhe.disan.xuetangparent.mvp.activity.CourseDetailActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.HomeSubjectActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.OfflineTeacherActivity;
import com.shengzhe.disan.xuetangparent.mvp.activity.TeacherNewPagerActivity;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public class MainView extends BaseView {
    private int lastDy = 0;
    private boolean isDowmToUp = false;
    private boolean isFingerUp = true;
    private IRefreshListener listener;
    private List<SquadBean> hotList = new ArrayList<>();
    private List<TeacherInformation> oneOneList = new ArrayList<>();
    private List<Subject> subjectList = new ArrayList<>();
    private LiveAndVideo listMybanner = null;
    private SimpleAdapter subjectAdapter,teacherAdapter,recommendAdapter;

    public MainView(Context context) {
        super(context);
    }

    private IMainView iView;
    public void setIMainView(IMainView iView){
        this.iView = iView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initDatas(MyBanner.BannerItemOnClickListener bannerListener, IRefreshListener listener) {
        isDowmToUp = false;
        isFingerUp = true;
        this.listener = listener;
        iView.getTopLayout().setPadding(iView.getTopLayout().getPaddingLeft(), iView.getTopLayout().getPaddingTop() + SystemInfoUtil.getStatusBarHeight(), iView.getTopLayout().getPaddingRight(), iView.getTopLayout().getPaddingBottom());
        iView.getMyBanner().setBannerItemOnClickListener(bannerListener);
        iView.getChooseCity().setText(CityDaoUtil.getCityById(SharedPreferencesManager.getCityId()).getArea_name());
        ImageUtil.setCompoundDrawable(iView.getChooseCity(), 12, R.mipmap.ic_white_down_arrow, Gravity.RIGHT, 0);
        ImageUtil.setCompoundDrawable(iView.getSearchView(), 17, R.mipmap.ic_white_search, Gravity.LEFT, 0);
        //监听滑动距离改变top的背景(mRollPagerView高度为界限)
        iView.getBarLayout().addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int dy) {
                float alpha = (float) Math.abs(dy)/ (iView.getMyBanner().getMeasuredHeight()- SystemInfoUtil.getStatusBarHeight()-iView.getTopLayout().getMeasuredHeight());
                alpha = alpha>1?1:alpha;
                if (alpha==1) {
                    iView.getTopLayout().setBackgroundColor(UiUtils.getColor(R.color.color_ffffff));
                    iView.getChooseCity().setTextColor(UiUtils.getColor(R.color.color_333333));
                    iView.getSsearchLayout().setBackgroundResource(R.drawable.popup_gray_corners_20);
                    iView.getSearchView().setTextColor(UiUtils.getColor(R.color.color_999999));
                    iView.getTopLinerView().setVisibility(View.VISIBLE);
                    iView.getChooseCity().setBackgroundColor(UiUtils.getColor(R.color.color_ffffff));
                    ImageUtil.setCompoundDrawable(iView.getChooseCity(), 12, R.mipmap.ic_black_down_arrow, Gravity.RIGHT, 0);
                    ImageUtil.setCompoundDrawable(iView.getSearchView(), 17, R.mipmap.ic_black_search, Gravity.LEFT, 0);
                } else {
                    iView.getTopLayout().setBackgroundColor(UiUtils.changeAlpha(UiUtils.getColor(R.color.color_ffffff), alpha));
                    iView.getChooseCity().setTextColor(UiUtils.getColor(R.color.color_ffffff));
                    iView.getSearchView().setTextColor(UiUtils.getColor(R.color.color_ffffff));
                    iView.getSsearchLayout().setBackgroundResource(R.drawable.popup_white_corners_10);
                    iView.getChooseCity().setBackgroundResource(R.drawable.bg_right_white);
                    iView.getTopLinerView().setVisibility(View.GONE);
                    ImageUtil.setCompoundDrawable(iView.getChooseCity(), 12, R.mipmap.ic_white_down_arrow, Gravity.RIGHT, 0);
                    ImageUtil.setCompoundDrawable(iView.getSearchView(), 17, R.mipmap.ic_white_search, Gravity.LEFT, 0);
                }
                if (!(iView.getRefreshLayout().getPositiveEnable()&&dy==0))
                    iView.getRefreshLayout().setPositiveEnable(dy==0);

                if (lastDy==dy) {
                    if (isFingerUp&&iView.getFloatButton().isHidden()&& !iView.getFloatButton().isShown()) {
                        iView.getFloatButton().show(true);
                        isDowmToUp = false;
                    }else if(isDowmToUp){
                        iView.getFloatButton().hide(true);
                    }
                    return;
                }
                lastDy = dy;
                if (iView.getFloatButton().isShown()&&!iView.getFloatButton().isHidden()) {
                    iView.getFloatButton().hide(true);
                }
            }
        });
        iView.getMainView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_MOVE:
                        isFingerUp = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isFingerUp = true;
                        break;

                }
                return false;
            }
        });

        iView.getMainView().setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //上滑 并且 正在显示底部栏
                if (scrollY - oldScrollY > 0 && iView.getFloatButton().isShown()&&!iView.getFloatButton().isHidden()) {
                    iView.getFloatButton().hide(true);
                    isDowmToUp = false;
                } else if (scrollY - oldScrollY < 0) {
                    isDowmToUp = true;
                }
                boolean isButton = iView.getMainView().getScrollY() == iView.getMainView().getChildAt(0).getMeasuredHeight() - iView.getMainView().getMeasuredHeight();
                if (!(iView.getRefreshLayout().getNegativeEnable()&&isButton))
                    iView.getRefreshLayout().setNegativeEnable(isButton);
            }
        });
        /******
         * 科目
         */
        iView.getCourseClazz().setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.HORIZONTAL));
        iView.getCourseClazz().setNestedScrollingEnabled(false);
        /****
         * 热门课程
         */
        //给RecyclerView设置布局管理器
        iView.getRVRecommend().setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.HORIZONTAL));
        iView.getRVRecommend().setNestedScrollingEnabled(false);

        /*****
         * 一对一教师
         */
        iView.getRVOneToOne().setLayoutManager(UiUtils.getLayoutManager(UiUtils.LayoutManager.VERTICAL));
        iView.getRVOneToOne().setNestedScrollingEnabled(false);
        setRefirshParameter();
        iView.getFloatButton().hide(false);
        iView.getFloatButton().postDelayed(new Runnable() {
            @Override
            public void run() {
                iView.getFloatButton().show(true);
                iView.getFloatButton().setShowAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fab_slide_in_from_right));
                iView.getFloatButton().setHideAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fab_slide_out_to_right));
            }
        }, 300);
        oneOneList.clear();
        setAdapters();
        iView.getRefreshLayout().startPositiveRefresh();
    }

    private void setAdapters() {
        subjectAdapter =  new SimpleAdapter<Subject>(mContext, subjectList, R.layout.item_subject_course) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final Subject data) {
                holder.setText(R.id.iv_subject_name, data.getSubjectName())
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, HomeSubjectActivity.class);
                                intent.putExtra(StringUtils.ACTIVITY_DATA,data);
                                mContext.startActivity(intent);
                            }
                        });
                ImageUtil.loadImageViewLoding(mContext, data.getAppUrl(), holder.<ImageView>getView(R.id.iv_subject_image), R.mipmap.loading_figure, R.mipmap.loading_figure);
            }
        };
        iView.getCourseClazz().setAdapter(subjectAdapter);

        recommendAdapter = new SimpleAdapter<SquadBean>(mContext, hotList, R.layout.item_recommend_course) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final SquadBean data) {
                holder.setText(R.id.tv_recommend_name,data.courseName)
                        .setText(R.id.tv_recommend_subject, data.subjectName +" "+data.gradeName)
                        .setText(R.id.tv_recommend_number, StringUtils.textFormatHtml("<font color='#1d97ea'>"+data.salesVolume+"</font>"+"/"+data.maxUser+"人"))
                        .setText(R.id.tv_recommend_teachername, data.nickName)
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CourseDetailActivity.class);
                                intent.putExtra(StringUtils.COURSE_ID,data.getCourseId());
                                mContext.startActivity(intent);
                            }
                        });

                if(data.discountPrice!=0){
                    //原价大于折扣价
                    holder .setText(R.id.tv_recommend_price,"¥"+ArithUtils.round(data.discountPrice));
                } else {
                    //原价小于等于折扣价
                    holder .setText(R.id.tv_recommend_price,"¥"+ArithUtils.round(data.courseTotalPrice));
                }
                ImageUtil.loadCircularImageView(mContext, data.pictureUrl, holder.<ImageView>getView(R.id.tv_recommend_image), R.mipmap.default_iamge, R.mipmap.default_iamge,8);
                ImageUtil.loadCircleImageView(mContext, data.photoUrl, holder.<ImageView>getView(R.id.tv_recommend_teacherphone), R.mipmap.ic_personal_avatar);
            }
        };
        iView.getRVRecommend().setAdapter(recommendAdapter);

        teacherAdapter = new SimpleAdapter<TeacherInformation>(mContext, oneOneList, R.layout.item_oneone_teacher) {
            @Override
            protected void onBindViewHolder(TrdViewHolder holder, final TeacherInformation data) {
                holder.setText(R.id.iv_oneone_name, data.getTeacherName())
                        .setText(R.id.iv_oneone_message, StringUtils.getSex(data.getSex()) + " | " + data.getGradeName() + " " + data.getSubjectName() + " | " + data.getTeachingAge() + "年教龄")
                        .setText(R.id.iv_oneone_price, data.getCoursePrice() / 10000 == 0 ? "免费" : "¥" + ArithUtils.round(data.getCoursePrice()))
                        .setText(R.id.iv_oneone_priprice, "¥" + ArithUtils.round(data.getDiscountPrice()))
                        .setText(R.id.iv_oneone_latelycourse, "新开课：" + data.getMaxCourseName())
                        .setVisible(R.id.tv_oneone_isplant, data.getIdentity() > 0)
                        .setVisible(R.id.iv_quality_certification, data.getIpmpStatus() == 2)
                        .setVisible(R.id.iv_realname_certification, data.getCardApprStatus() != 0)
                        .setVisible(R.id.iv_teacher_certification, data.getQtsStatus() == 2)
                        .setVisible(R.id.iv_education_certification, data.getQuaStatus() == 2)
                        .setVisible(R.id.v_oneone_line, oneOneList.indexOf(data) != oneOneList.size() - 1)
                        .setOnItemListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Intent intent = new Intent(mContext, TeacherNewPagerActivity.class);
                                Intent intent = new Intent(mContext, OfflineTeacherActivity.class);
                                intent.putExtra(StringUtils.TEACHER_ID,data.getTeacherId());
                                mContext.startActivity(intent);
                            }
                        });
                //原价和折扣
                if (data.getCoursePrice() > data.getDiscountPrice()) {
                    //原价大于折扣价
                    holder.setText(R.id.iv_oneone_price, "¥" + ArithUtils.round(data.getDiscountPrice()));
                    holder.setText(R.id.iv_oneone_priprice, "¥" + ArithUtils.round(data.getCoursePrice()));
                    holder.setVisible(R.id.iv_oneone_priprice, true);
                } else {
                    //原价小于等于折扣价
                    holder.setText(R.id.iv_oneone_price, "¥" + ArithUtils.round(data.getCoursePrice()));
                    holder.setVisible(R.id.iv_oneone_priprice, false);
                }
                holder.<TextView>getView(R.id.iv_oneone_priprice).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ImageUtil.loadCircleImageView(mContext, data.getPhotoUrl(), holder.<ImageView>getView(R.id.iv_oneone_image), R.mipmap.teacher);

                if (data.getCourseType() == 1) {
                    ImageUtil.setCompoundDrawable(holder.<TextView>getView(R.id.iv_oneone_latelycourse), 16, R.mipmap.one_lines, Gravity.LEFT, 0);
                } else if (data.getCourseType() == 2) {
                    ImageUtil.setCompoundDrawable(holder.<TextView>getView(R.id.iv_oneone_latelycourse), 16, R.mipmap.ic_course_min, Gravity.LEFT, 0);
                }
                String address = "";
                if (!(TextUtils.isEmpty(data.getCityName()))) {
                    address = data.getCityName();
                }
                if (!(TextUtils.isEmpty(data.getAreaName()))) {
                    address = address + "-" + data.getAreaName();
                }
                holder.setText(R.id.tv_oneone_address, address);
            }
        };
        // 设置组件复用回收池(如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）)
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        iView.getRVOneToOne().setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 15);
        iView.getRVOneToOne().setAdapter(teacherAdapter);
    }

    private LoadMoreWithNomoreRefresher mRefresher;
    private void setRefirshParameter() {
        //设置是否可以刷新，加载更多
        iView.getRefreshLayout().setPositiveEnable(true);
        iView.getRefreshLayout().setNegativeEnable(false);
        //设置刷新颜色 必须放在setPositiveEnable方法过后
        iView.getRefreshLayout().setProgressColor(UiUtils.getColor(R.color.color_ffae12));
        //拖拽或是滑动到边缘自动加载
        iView.getRefreshLayout().setPositiveDragEnable(false);
        iView.getRefreshLayout().setNegativeDragEnable(false);
        //设置刷新、加载组件是否显示在布局上面
        iView.getRefreshLayout().setPositiveOverlayUsed(true);
        iView.getRefreshLayout().setNegativeOverlayUsed(false);
        mRefresher=new LoadMoreWithNomoreRefresher();
        iView.getRefreshLayout().setNegativeRefresher(mRefresher);
        mRefresher.setNothing("我是有底线的");

        iView.getRefreshLayout().addRefreshListener(listener);
    }

    public void setNotifyFloat() {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                //X轴初始位置
                Animation.RELATIVE_TO_SELF, 0.0f,
                //X轴移动的结束位置
                Animation.RELATIVE_TO_SELF, -1.0f,
                //y轴开始位置
                Animation.RELATIVE_TO_SELF, 0.0f,
                //y轴移动后的结束位置
                Animation.RELATIVE_TO_SELF, 0.0f);
        //3秒完成动画
        translateAnimation.setDuration(1000);
        //如果fillAfter的值为真的话，动画结束后，控件停留在执行后的状态
        animationSet.setFillAfter(true);
        //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
        animationSet.addAnimation(translateAnimation);
        iView.getNotify().startAnimation(animationSet);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iView.getNotify().setVisibility(View.GONE);
                iView.getNotify().clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void setBanner(List<BannerBean> banner) {
        iView.getMyBanner().initPageIndex(banner);
    }

    private void setNotify(int todayCourse) {
        if (todayCourse >= 1) {
            iView.getNotify().setVisibility(View.VISIBLE);
            iView.getNotifyText().setText("您今天有课哦，去我的课表吧~~");
        } else {
            iView.getNotify().setVisibility(View.GONE);
        }
    }


    private void setIsEmpty(boolean bool){
        if(bool){
            iView.getTvOneToOne().setVisibility(View.GONE);
            iView.getRVOneToOne().setVisibility(View.GONE);
            iView.getNoneView().setVisibility(View.VISIBLE);
            if (iView.getRefreshLayout()==null)
                return;
            iView.getRefreshLayout().setNegativeEnable(false);
        }else{
            iView.getTvOneToOne().setVisibility(View.VISIBLE);
            iView.getRVOneToOne().setVisibility(View.VISIBLE);
            iView.getNoneView().setVisibility(View.GONE);
        }
    }

    public boolean isSubjectListEmpty() {
        return subjectList==null || subjectList.isEmpty();
    }

    public void setSubjectResultDates(String str) {
        SharedPreferencesManager.saveSubject(str);
        subjectList.clear();
        subjectList.addAll(SharedPreferencesManager.getSubject());
        subjectAdapter.notifyDataSetChanged();
    }

    public void setLiveVideoResultDates(LiveAndVideo mListMybanner) {
        if(SharedPreferencesManager.getUserInfo()!=null) {
            User user = SharedPreferencesManager.getUserInfo();
            user.setIsApplyCourseListen(mListMybanner.getIsApplyCourseListen());
            SharedPreferencesManager.setUserInfo(user);
        }

        listMybanner = mListMybanner;
        setBanner(listMybanner.getBanner());
        setNotify(listMybanner.getTodayCourse());

        List<SquadBean> hotList = mListMybanner.getSquad();
        this.hotList.clear();
        if (hotList==null || hotList.isEmpty()) {
            iView.getTvRecommend().setVisibility(View.GONE);
            iView.getRVRecommend().setVisibility(View.GONE);
        } else {
            iView.getTvRecommend().setVisibility(View.VISIBLE);
            iView.getRVRecommend().setVisibility(View.VISIBLE);
            this.hotList.addAll(hotList);
        }
        recommendAdapter.notifyDataSetChanged();
    }

    public void setTeacherResultDates(BasePageBean<TeacherInformation> teacher) {
        loadFinish();
        oneOneList.addAll(teacher.getList());
        if (oneOneList == null || oneOneList.isEmpty()) {
            oneOneList.clear();
            mRefresher.setHasMore(false);
            setIsEmpty(true);
        } else {
            setIsEmpty(false);
            mRefresher.setHasMore(oneOneList.size() < 45 && teacher.isHasNextPage());
        }
        teacherAdapter.notifyDataSetChanged();
    }

    public void loadFinish() {
        iView.getRefreshLayout().positiveRefreshComplete();
        iView.getRefreshLayout().negativeRefreshComplete();
        mRefresher.setHasMore(false);
    }

    public void clearDatas() {
        oneOneList.clear();
    }

    public interface IMainView {
        RefreshRelativeLayout getRefreshLayout();

        AppBarLayout getBarLayout();

        MyBanner getMyBanner();

        TextView getChooseCity();

        RelativeLayout getTopLayout();

        RecyclerView getCourseClazz();

        View getNotify();

        TextView getNotifyText();

        TextView getTvRecommend();

        RecyclerView getRVRecommend();

        TextView getTvOneToOne();

        RecyclerView getRVOneToOne();

        TextView getSearchView();

        RelativeLayout getSsearchLayout();

        View getTopLinerView();

        MyFloatingActionButton getFloatButton();

        View getNoneView();

        NestedScrollView getMainView();
    }
}
