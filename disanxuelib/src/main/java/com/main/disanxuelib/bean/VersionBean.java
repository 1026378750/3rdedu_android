package com.main.disanxuelib.bean;

/*****
 * liukui
 *
 * 版本更新bean
 */
public class VersionBean{
    //1、当前版本和数据库最新版本一致 2、当前版本和数据库最新版本不一致
    public int versionType;
    //是否强制更新 0否 1是
    public int forceUpdate;
    //app版本号
    public String appVersion;
    //版本id
    public long id;
    //	更新内容
    public String remark;
    //更新地址
    public String url;
}
