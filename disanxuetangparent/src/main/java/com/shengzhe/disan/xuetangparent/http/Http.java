package com.shengzhe.disan.xuetangparent.http;

import android.text.TextUtils;

import com.main.disanxuelib.util.NetworkUtil;
import com.main.disanxuelib.util.SystemInfoUtil;
import com.shengzhe.disan.xuetangparent.utils.ConstantUrl;
import com.shengzhe.disan.xuetangparent.app.MyApplication;
import com.shengzhe.disan.xuetangparent.http.responese.ResponseConverterFactory;
import com.shengzhe.disan.xuetangparent.http.service.HttpService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * service-下载接口
 * Created by hy on 2017/9/18.
 */

public class Http {
    private static OkHttpClient client;
    private static HttpService httpService;
    private static volatile Retrofit retrofit;

    /**
     * @return retrofit的底层利用反射的方式, 获取所有的api接口的类
     */
    public static HttpService getHttpService() {
        if (httpService == null) {
            httpService = getRetrofit().create(HttpService.class);
        }
        return httpService;
    }

    /**
     * 设置公共参数
     */
    private static Interceptor addQueryParameterInterceptor() {
        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
        return addQueryParameterInterceptor;
    }

    /**
     * 设置头
     */
    private static Interceptor addHeaderInterceptor() {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder();
                String ApiCode = builder.head().build().headers().get("ApiCode");
                ApiCode = ApiCode == null ? "1.0" : ApiCode;
                Request.Builder requestBuilder = builder
                        // Provide your custom header here
                        .addHeader("Content-Type", "application/json; charset=UTF-8")
                        //.addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "application/json")
                        //ShengZheYuan;1.0;ios;1.0;iphone9,1;
                        //1.项目名 2.项目版本  3.客户端   4.apiVersion
                        .addHeader("Client-Info", "patriarch" + ";" + SystemInfoUtil.getVersionName() + ";Android;" + ApiCode + ";" + SystemInfoUtil.getSystemModel() + ";" + SystemInfoUtil.getVersionCode());
                // .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
                if (!TextUtils.isEmpty(ConstantUrl.TOKN)) {
                    requestBuilder.addHeader("authorization", ConstantUrl.TOKN);
                }
                //   .addHeader("Cookie", "add cookies here")
                requestBuilder.method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }

    /**
     * 设置缓存
     */
    private static Interceptor addCacheInterceptor() {
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtil.isNetWorkAviliable()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetworkUtil.isNetWorkAviliable()) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" +
                                    maxStale)
                            .removeHeader("nyn")
                            .build();
                }
                return response;
            }
        };
        return cacheInterceptor;
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (Http.class) {
                if (retrofit == null) {
                    //添加一个log拦截器,打印所有的log
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    //可以设置请求过滤的水平,body,basic,headers
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    //设置 请求的缓存的大小跟位置
                    File cacheFile = new File(MyApplication.getInstance().getmContext().getCacheDir(), "cache");
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb 缓存的大小
                    client = new OkHttpClient
                            .Builder()
                            .addInterceptor(addQueryParameterInterceptor())  //参数添加
                            .addInterceptor(addHeaderInterceptor()) // token过滤
                            .addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
                            .addInterceptor(addCacheInterceptor())//缓存信息
                            .cache(cache)  //添加缓存
                            .connectTimeout(12, TimeUnit.SECONDS)
                            .readTimeout(12, TimeUnit.SECONDS)
                            .writeTimeout(12, TimeUnit.SECONDS)
                            .build();
                    // 获取retrofit的实例
                    retrofit = new Retrofit
                            .Builder()
                            .baseUrl(UrlHelper.BASE_URL)  //自己配置
                            .client(client)
                            .addConverterFactory(ResponseConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

}
