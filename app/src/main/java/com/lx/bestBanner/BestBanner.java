package com.lx.bestBanner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 顶部轮播展示
 *
 * @author JALEN  c9n9m@163.com
 * @version V1.0
 * @Title: ${FILE_NAME}
 * @Package com.lx.bestBanner
 * @Description: ${todo}
 * @date 16/8/1 15:37
 */
public class BestBanner<T extends BannerEntity> extends LinearLayout {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 图片轮播视图
     */
    private ViewPager mAdvPager = null;
    /**
     * 滚动图片视图适配
     */
    private ImageCycleAdapter mAdvAdapter;
    /**
     * 图片轮播指示器控件
     */
    private ViewGroup mGroup;

    private RelativeLayout rl_botom;

    /**
     * 图片轮播指示个图
     */
    private ImageView mImageView = null;

    /**
     * 滚动图片指示视图列表
     */
    private ImageView[] mImageViews = null;

    /**
     * 图片滚动当前图片下标
     */
    private int mImageIndex = 0;

    /**
     * 手机密度
     */
    private float mScale;
    private boolean isStop;
    /**
     * 默认图片切换时间
     */
    private long timeAlong = 3000l;
    //点击banner图片的事件
    private OnBannerItemClickListener onBannerItemClickListener;

    private TextView tv;

    private List<T> banners;


    /**
     * @param context
     */
    public BestBanner(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * @param context
     * @param attrs
     */
    public BestBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        LayoutInflater.from(context).inflate(R.layout.best_banner, this);
        mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
        tv = (TextView) findViewById(R.id.tv);
        rl_botom = (RelativeLayout) findViewById(R.id.rl_botom);

        mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
        mAdvPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        stopImageTimerTask();
                        break;
                    default:
                        // 停止图片滚动
                        stopImageTimerTask();
                        break;
                }
                return false;
            }
        });
        // 滚动图片右下指示器视
        mGroup = (ViewGroup) findViewById(R.id.viewGroup);
    }


    /**
     * 设置填充图片数据
     *
     * @param banners
     * @param handler
     */
    public void setBannerData(List<T> banners, Handler handler) {
        this.banners = banners;
        // 清除
        mGroup.removeAllViews();
        // 图片广告数量
        final int imageCount = banners.size();
        mImageViews = new ImageView[imageCount];
        List<String> imageUrlList = new ArrayList<String>();
        for (int i = 0; i < imageCount; i++) {
            mImageView = new ImageView(mContext);
            int imageParams = 16;// XP与DP转换，适应应不同分辨率
            int imagePadding = 4;
            LayoutParams params = new LayoutParams(imageParams, imageParams);
            params.leftMargin = 8;
            mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mImageView.setLayoutParams(params);
            mImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
            mImageViews[i] = mImageView;
            if (i == 0) {
                mImageViews[i].setBackgroundResource(R.drawable.best_banner_point_focus);
            } else {
                mImageViews[i].setBackgroundResource(R.drawable.best_banner_point_unfocus);
            }
            mGroup.addView(mImageViews[i]);
            imageUrlList.add(banners.get(i).getBaannerImgUrl());
        }

        if (onBannerItemClickListener != null)
            mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, onBannerItemClickListener);
        else
            mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList);

        mAdvPager.setAdapter(mAdvAdapter);

        if (imageCount > 1)
            startImageTimerTask();

        tv.setText(banners.get(0).getBannerDescrible());
    }

    /**
     * 图片轮播(手动控制自动轮播与否，便于资源控件）
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播—用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片滚动
        mHandler.postDelayed(mImageTimerTask, timeAlong);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        isStop = true;
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {
            if (mImageViews != null) {
                mAdvPager.setCurrentItem(mAdvPager.getCurrentItem() + 1);
                if (!isStop) {  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环
                    mHandler.postDelayed(mImageTimerTask, timeAlong);
                }

            }
        }
    };

    /**
     * 轮播图片监听
     */
    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                startImageTimerTask();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            index = index % mImageViews.length;
            //1:设置底部的文字
            if (banners != null)
                tv.setText(banners.get(index).getBannerDescrible());
            // 2:设置当前显示的图片
            mImageIndex = index;
            // 3:设置图片滚动指示器背
            mImageViews[index].setBackgroundResource(R.drawable.best_banner_point_focus);
            for (int i = 0; i < mImageViews.length; i++) {
                if (index != i) {
                    mImageViews[i].setBackgroundResource(R.drawable.best_banner_point_unfocus);
                }
            }
        }
    }

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private ArrayList<ImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private List<String> mAdList = new ArrayList<String>();

        private Context mContext;

        private OnBannerItemClickListener onBannerItemClickListener;

        public ImageCycleAdapter(Context context, List<String> adList) {
            this.mContext = context;
            this.mAdList = adList;
            mImageViewCacheList = new ArrayList<ImageView>();
        }

        public ImageCycleAdapter(Context context, List<String> adList, OnBannerItemClickListener onBannerItemClickListener) {
            this.mContext = context;
            this.mAdList = adList;
            this.onBannerItemClickListener = onBannerItemClickListener;
            mImageViewCacheList = new ArrayList<ImageView>();
        }

        @Override
        public int getCount() {
            return mAdList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String imageUrl = mAdList.get(position % mAdList.size());
            ImageView imageView = null;
            try {
                if (mImageViewCacheList.isEmpty()) {
                    imageView = new ImageView(mContext);
                    imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imageView = mImageViewCacheList.remove(0);
                }
                imageView.setTag(imageUrl);
                container.addView(imageView);
                Picasso.with(mContext).load(imageUrl).into(imageView);
                // 设置图片点击监听
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onBannerItemClickListener != null)
                            onBannerItemClickListener.onItemClick(position % mAdList.size());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            mAdvPager.removeView(view);
            mImageViewCacheList.add(view);
        }
    }
}
