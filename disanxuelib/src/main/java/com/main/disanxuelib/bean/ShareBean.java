package com.main.disanxuelib.bean;

/**
 * Created by 分享实体 on 2018/4/16.
 */

public class ShareBean {
    //编号
    public int id = 0;
    //标题
    public String title = "";
    //信息
    public String mesg = "";
    //图片路径
    public String iamgeUrl = "";
    //链接路径
    public String linkUrl = "";
    //ViewPager 下表
    public int viewPagerIndex = 0;

    public ShareBean(int id,String title,String mesg,String iamgeUrl,String linkUrl,int viewPagerIndex){
        this.id = id;
        this.title = title;
        this.mesg = mesg;
        this.iamgeUrl = iamgeUrl;
        this.linkUrl = linkUrl;
        this.viewPagerIndex = viewPagerIndex;
    }

}
