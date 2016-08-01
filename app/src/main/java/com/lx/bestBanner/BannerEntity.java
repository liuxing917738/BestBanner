package com.lx.bestBanner;

import java.io.Serializable;

/**
 * banner实体数据
 * @author JALEN  c9n9m@163.com
 * @version V1.0
 * @Title: ${FILE_NAME}
 * @Package com.lx.bestBanner
 * @Description: ${todo}
 * @date 16/8/1 15:46
 */
public abstract class BannerEntity implements Serializable {
    private String bannerDescrible;
    private String baannerImgUrl;

    public String getBannerDescrible() {
        return bannerDescrible;
    }

    public void setBannerDescrible(String bannerDescrible) {
        this.bannerDescrible = bannerDescrible;
    }

    public String getBaannerImgUrl() {
        return baannerImgUrl;
    }

    public void setBaannerImgUrl(String baannerImgUrl) {
        this.baannerImgUrl = baannerImgUrl;
    }
}
