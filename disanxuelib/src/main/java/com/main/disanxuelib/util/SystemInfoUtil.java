package com.main.disanxuelib.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.main.disanxuelib.app.BaseApplication;
import com.main.disanxuelib.bean.AppInfoBean;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SystemInfoUtil{
	static {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				Class<?> c = Class.forName("android.os.SystemProperties");
				Method m = c.getDeclaredMethod("get", String.class);
				m.setAccessible(true);
				sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
			} catch (Throwable e) {
				sNavBarOverride = null;
			}
		}
	}

	public static final String TAG = SystemInfoUtil.class.getSimpleName();
	private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
	private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
	private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
	private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
	private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";
	private static String sNavBarOverride;

	/**
	 * 得到sdk 版本号
	 * @return
	 */
	public static int getSdkVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 判断网络是否可用
	 * @return
	 */
	public static boolean isNetworkAvailable() {
		try {
			ConnectivityManager cm = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
			return (netWorkInfo != null && netWorkInfo.isAvailable());// 检测网络是否可用
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取当前手机系统版本号
	 *
	 * @return  系统版本号
	 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	/**
	 * 获取手机型号
	 *
	 * @return  手机型号
	 */
	public static String getSystemModel() {
		return android.os.Build.MODEL;
	}
	public static String getProjectName(Context _context){
		PackageManager pm = _context.getPackageManager();
		String appName = _context.getApplicationInfo().loadLabel(pm).toString();
		return  appName;
	}

	/**
	 * 得到版本名称
	 * @return
	 */
	public static String getVersionName() {
		try {
			return BaseApplication.contextApp.getPackageManager().getPackageInfo(BaseApplication.contextApp.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到版本号
	 * @return
	 */
	public static int getVersionCode() {
		try {
			return BaseApplication.contextApp.getPackageManager().getPackageInfo(BaseApplication.contextApp.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			return -1;
		}
	}

	/****
	 * 是否强行更新app
	 * @return
	 */
	public static boolean isUpdateApp(String webAppCode){
		String[] localCode = getVersionName().split("\\.");
		String[] webCode = webAppCode.split("\\.");
		if(localCode.length!=webCode.length)
			return true;
		if (localCode.length!=3)
			return true;
		return Integer.parseInt(localCode[0])>Integer.parseInt(webCode[0])||Integer.parseInt(localCode[1])>Integer.parseInt(webCode[1])||Integer.parseInt(localCode[2])>Integer.parseInt(webCode[2])+2;
	}


	public static String getPackageName(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			if (info != null) {
				return info.packageName;
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = BaseApplication.getContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(getPackageName(BaseApplication.getContext()), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName =
				(String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	public static String getPhoneNum(Context _context) {
		TelephonyManager phoneMgr = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
		return phoneMgr.getLine1Number();
	}

	public static String getIMEI(Context _context) {
		TelephonyManager _manager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
		String _imei = _manager.getDeviceId(); // 取出IMEI
		return _imei;
	}

	public static String getICCID(Context _context) {
		TelephonyManager _manager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
		String _iccid = _manager.getSimSerialNumber(); // 取出ICCID
		return _iccid;
	}

	public static String getIMSI(Context _context) {
		TelephonyManager _manager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
		String _imsi = _manager.getSubscriberId(); // 取出IMSI
		return _imsi;
	}

	public static boolean isPad(Context _context) {
		if (Build.VERSION.SDK_INT >= 11) {
			Configuration con = _context.getResources().getConfiguration();
			try {
				Method mIsLayoutSizeAtLeast = con.getClass().getMethod("isLayoutSizeAtLeast", int.class);
				return (Boolean) mIsLayoutSizeAtLeast.invoke(con, 0x00000004);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public static Intent call_1(String _phoneNum) {

		return new Intent(Intent.ACTION_CALL_BUTTON, Uri.parse("tel:" + _phoneNum));
	}

	/*****
	 * 去拨号界面
	 * @param _phoneNum
	 * @return
	 */
	public static void callDialing(String _phoneNum) {
		ActivityCompat.startActivity(BaseApplication.getContext(),new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _phoneNum)),null);
	}

	/**
	 * 得到设备的唯一Id
	 * @return
	 */
	public static String getInstallationId() {
		return Build.SERIAL;
	}

	/**
	 * 获取屏幕参数
	 */
	public static DisplayMetrics getDisplayMetrics() {
		return BaseApplication.getContext().getResources().getDisplayMetrics();
	}

	/**
	 * 得到设备屏幕的宽度
	 */
	public static int getScreenWidth() {
		return getDisplayMetrics().widthPixels;
	}

	/**
	 * 得到设备屏幕的高度
	 */
	public static int getScreenHeight() {
		return getDisplayMetrics().heightPixels;
	}

	/**
	 * 得到设备的密度
	 */
	public static float getScreenDensity() {
		return getDisplayMetrics().density;
	}

	public static int dpTpPx(float value) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getDisplayMetrics()) + 0.5);
	}

	/**
	 * 把密度转换为像素
	 */
	public static int dip2px(float px) {
		final float scale = getScreenDensity();
		return (int) (px * scale + 0.5);
	}

	public static int px2dip(float pxValue) {
		final float scale = getScreenDensity();
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取状态栏高度
	 *
	 * @return
	 */
	public static int getStatusBarHeight() {
		int result = 0;
		int resourceId = BaseApplication.getInstance().getResources().getIdentifier(STATUS_BAR_HEIGHT_RES_NAME, "dimen", "android");
		if (resourceId > 0) {
			result = BaseApplication.getInstance().getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/****
	 * 获取下边高度
	 * @return
	 */
	public static int getNavigationBarHeight() {
		int result = 0;
		int resourceId = BaseApplication.getInstance().getResources().getIdentifier(NAV_BAR_HEIGHT_RES_NAME, "dimen", "android");
		if (resourceId > 0) {
			result = BaseApplication.getInstance().getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}


	@SuppressLint("NewApi")
	public static float getSmallestWidthDp() {
		DisplayMetrics metrics = new DisplayMetrics();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			AppManager.getAppManager().currentActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
		} else {
			// TODO this is not correct, but we don't really care pre-kitkat
			AppManager.getAppManager().currentActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		}
		float widthDp = metrics.widthPixels / metrics.density;
		float heightDp = metrics.heightPixels / metrics.density;
		return Math.min(widthDp, heightDp);
	}

	@TargetApi(14)
	public static int getActionBarHeight() {
		int result = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			TypedValue tv = new TypedValue();
			BaseApplication.getInstance().getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
			result = TypedValue.complexToDimensionPixelSize(tv.data, BaseApplication.getInstance().getContext().getResources().getDisplayMetrics());
		}
		return result;
	}

	@TargetApi(14)
	public static int getNavigationBarWidth() {
		Resources res =  BaseApplication.getInstance().getContext().getResources();
		int result = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (hasNavBar( BaseApplication.getInstance().getContext())) {
				return getInternalDimensionSize(res, NAV_BAR_WIDTH_RES_NAME);
			}
		}
		return result;
	}

	@TargetApi(14)
	private static boolean hasNavBar(Context context) {
		Resources res = context.getResources();
		int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
		if (resourceId != 0) {
			boolean hasNav = res.getBoolean(resourceId);
			if ("1".equals(sNavBarOverride)) {
				hasNav = false;
			} else if ("0".equals(sNavBarOverride)) {
				hasNav = true;
			}
			return hasNav;
		} else {
			return !ViewConfiguration.get(context).hasPermanentMenuKey();
		}
	}

	/****
	 *
	 * @return
	 */
	public static boolean isNavigationAtBottom() {
		return (SystemInfoUtil.getSmallestWidthDp() >= 600 || BaseApplication.getInstance().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
	}

	public static boolean hasNavigtionBar() {
		return SystemInfoUtil.getNavigationBarHeight() > 0;
	}

	public static int getPixelInsetTop(boolean mTranslucentStatusBar ,boolean withActionBar) {
		return (mTranslucentStatusBar ? SystemInfoUtil.getStatusBarHeight() : 0) + (withActionBar ? SystemInfoUtil.getActionBarHeight() : 0);
	}

	public static int getPixelInsetBottom(boolean mTranslucentNavBar) {
		if (mTranslucentNavBar && SystemInfoUtil.isNavigationAtBottom()) {
			return SystemInfoUtil.getNavigationBarHeight();
		} else {
			return 0;
		}
	}

	public static int getPixelInsetRight(boolean mTranslucentNavBar) {
		if (mTranslucentNavBar && !SystemInfoUtil.isNavigationAtBottom()) {
			return SystemInfoUtil.getNavigationBarWidth();
		} else {
			return 0;
		}
	}

	private static int getInternalDimensionSize(Resources res, String key) {
		int result = 0;
		int resourceId = res.getIdentifier(key, "dimen", "android");
		if (resourceId > 0) {
			result = res.getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static boolean hasLollipop() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}

	/*****
	 * 查询本机所有可以分享
	 * @return
	 * @param share
	 */
	public static List<AppInfoBean> canShareList(List<String> share){
		List<AppInfoBean> canList = new ArrayList<>();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("*/*");//设置分享内容的类型 image/*、text/plain
		PackageManager pm = BaseApplication.contextApp.getPackageManager();
		List<ResolveInfo> resInfoList = pm.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo resInfo: resInfoList) {
			String pkgName = resInfo.activityInfo.packageName; // 获得应用程序的包名
			if (share==null || share.isEmpty() || share.contains(pkgName)){
				String activityName = resInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
				String appLabel = (String) resInfo.loadLabel(pm); // 获得应用程序的Label
				String appName = resInfo.activityInfo.applicationInfo.loadLabel(pm).toString();
				Drawable icon = resInfo.loadIcon(pm); // 获得应用程序图标
				// 为应用程序的启动Activity 准备Intent
				Intent launchIntent = new Intent();
				launchIntent.setComponent(new ComponentName(pkgName,activityName));
				// 创建一个AppInfo对象，并赋值
				AppInfoBean appInfo = new AppInfoBean();
				appInfo.setAppName(appName);
				appInfo.setAppLabel(appLabel);
				appInfo.setPkgName(pkgName);
				appInfo.setAppIcon(icon);
				appInfo.setIntent(launchIntent);
				canList.add(appInfo);
			}
		}
		return canList;
	}

	/**
	 * Retrieve launcher activity name of the application from the context
	 *
	 * @param context The context of the application package.
	 * @return launcher activity name of this application. From the
	 * "android:name" attribute.
	 */
	public static String getLauncherClassName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setPackage(context.getPackageName());
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		// All Application must have 1 Activity at least.
		// Launcher activity must be found!
		ResolveInfo info = packageManager
				.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
		// get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
		// if there is no Activity which has filtered by CATEGORY_DEFAULT
		if (info == null) {
			info = packageManager.resolveActivity(intent, 0);
		}
		//////////////////////另一种实现方式//////////////////////
		// ComponentName componentName = context.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName()).getComponent();
		// return componentName.getClassName();
		//////////////////////另一种实现方式//////////////////////
		return info.activityInfo.name;
	}

	/******
	 * 获取当前
	 * @return
	 */
	public static View getCurrentView(){
		FrameLayout contentParent = (FrameLayout) AppManager.getAppManager().currentActivity().getWindow().getDecorView().findViewById(android.R.id.content);
		return contentParent.getFocusedChild();
	}

	/*****
	 * 判断手机中是否安装了
	 * @param packageName
	 * @return
	 */
	public static boolean isPackageInstalled(String packageName) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = BaseApplication.getInstance().getContext().getPackageManager().getPackageInfo(packageName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		} finally {
			return packageInfo != null;
		}
	}

}
