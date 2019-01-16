package com.shengzhe.disan.xuetangparent.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.shengzhe.disan.xuetangparent.R;
import com.shengzhe.disan.xuetangparent.mvp.activity.OpenCityActivity;
import com.shengzhe.disan.xuetangparent.utils.SharedPreferencesManager;
import com.shengzhe.disan.xuetangparent.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/*****
 * 欢迎页
 */
public class GuideActivity extends BaseActivity{
    private List<ImageView> imageViewList;
    // 点的组
    @BindView(R.id.ll_guide_point_group)
    LinearLayout llPointGroup;
    // 选中的点view对象
    @BindView(R.id.select_point)
    View mSelectPointView;
    // 开始体验按钮
    @BindView(R.id.btn_guide_start_experience)
    Button btnStartExperience;
    @BindView(R.id.vp_guide)
    ViewPager mViewPager;

    /**
     * TODO：初始化ViewPager数据
     */
    @Override
    public void initData() {
        int[] imageResIDs = {R.mipmap.guide_one,R.mipmap.guide_two,R.mipmap.guide_three};
        imageViewList = new ArrayList<>();
        for (int i = 0; i < imageResIDs.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(imageResIDs[i]);
            imageViewList.add(iv);
            // 根据图片的个数, 每循环一次向LinearLayout中添加一个点
            View view = new View(this);
            view.setBackgroundResource(R.drawable.point_normal);
            // 设置参数
            LayoutParams params = new LayoutParams(10, 10);
            if (i != 0) {
                params.leftMargin = 10;
            }
            view.setLayoutParams(params);// 添加参数
        }

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageViewList.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            /*
             * 删除元素
             */
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv = imageViewList.get(position);
                container.addView(iv);// 1. 向ViewPager中添加一个view对象
                return iv; // 2. 返回当前添加的view对象
            }
        });

        // 设置监听器
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 当页面正在滚动时 position 当前选中的是哪个页面 positionOffset 比例 positionOffsetPixels 偏移像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            /**
             * 当页面被选中
             */
            @Override
            public void onPageSelected(int position) {
                // 显示体验按钮
                if (position == imageViewList.size() - 1) {
                    btnStartExperience.setVisibility(View.VISIBLE);// 显示
                } else {
                    btnStartExperience.setVisibility(View.GONE);// 隐藏
                }
            }

            /**
             * 当页面滚动状态改变
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.activity_guide;
    }

    /**
     * 打开新的界面
     */
    @OnClick(R.id.btn_guide_start_experience)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_guide_start_experience:
                SharedPreferencesManager.setIsEnter(false);
                Intent intent =  new Intent(this,OpenCityActivity.class);
                intent.putExtra(StringUtils.activcity_from,GuideActivity.class.getName());
                startActivity(intent);
                onBackPressed();
                break;
        }
    }

}
