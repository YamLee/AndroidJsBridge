package me.yamlee.jsbridge.ui.internel

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebSettings
import android.webkit.WebView
import me.yamlee.jsbridge.utils.LogUtil

/**
 * The common base WebView for bridge bridge framework user to extend
 *
 * @author yamlee
 */
class BridgeWebView : WebView {

    private var mLastX: Int = 0
    private var mLastY: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, privateBrowsing: Boolean) : super(context, attrs, defStyleAttr, privateBrowsing) {
        init()
    }


    private fun init() {
        //设置webview支持chrome调试 4.4以上系统支持
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.applicationInfo.flags = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
            if (0 != context.applicationInfo.flags) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
        setWebViewSetting()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebViewSetting() {
        val settings = settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true
        }
        settings.builtInZoomControls = false
        //设置UA user-agent
        //        String appVersion = "/version_name:" + ApkUtil.getVersionName(getContext()) +
        //                ";version_code:" + ApkUtil.getVersionCode(getContext());
        //
        //        String deviceName = "/device_name:" + DeviceUtil.getDeviceName() +
        //                ";deviceid:" + DeviceUtil.getDeviceID(getContext());
        //
        //        String osVersion = "/os version:" + DeviceUtil.getOsVersionStr() + ";"
        //                + DeviceUtil.getOsVersion();
        //
        //        settings.setUserAgentString(settings.getUserAgentString() + ";QMMWD" +
        //                appVersion + deviceName + osVersion);

        LogUtil.info("user-agent====" + settings.userAgentString)
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.defaultTextEncodingName = "utf-8"
        //启用地理定位
        settings.setGeolocationEnabled(true)
        //开启DomStorage缓存
        settings.domStorageEnabled = true
        settings.setAppCacheEnabled(true)
        var cachePath = context.cacheDir.absolutePath
        if (Build.VERSION.SDK_INT <= 20) {
            //4.4以下需手动设置database路径,方使domstorage生效
            cachePath += "/database"
            settings.databasePath = cachePath
        }
        settings.setAppCachePath(cachePath)
        settings.allowFileAccess = true
        settings.setGeolocationDatabasePath(context.filesDir.path)
        //启用数据库
        settings.databaseEnabled = true
    }

    fun setCookie(domain: String, values: List<String>) {
        LogUtil.info("start set cookie...")
        CookieSyncManager.createInstance(context)
        val cookieManager = CookieManager.getInstance()
        CookieSyncManager.getInstance().startSync()
        CookieManager.allowFileSchemeCookies()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(this, true)
        }
        cookieManager.setAcceptCookie(true)
        clearCache(true)
        clearHistory()
        //        boolean offlineDev = SpManager.getInstance(getContext()).getBoolean(SpKey.OFFLINE_DEV, false);
        //        UserCache userCache = UserCache.getInstance(getContext());
        //        if (userCache.hasLogin()) {
        //            String sessionId = userCache.getSessionId();
        //            String qfUid = userCache.getUserId();
        //            /**
        //             *  注：临时cookie(没有expires参数的cookie)不能带有domain选项。
        //             当客户端发送一个http请求时，会将有效的cookie一起发送给服务器。
        //             如果一个cookie的domain和path参数和URL匹配，那么这个cookie就是有效的。
        //             一个URL中包含有domain和path，可以参
        //             */
        //            cookieManager.setCookie(domainName, "sessionid=" + sessionId);
        //            cookieManager.setCookie(domainName, "qf_uid=" + qfUid);
        //        }
        //        if (offlineDev) {
        //            cookieManager.setCookie(domainName, "mmfct=1");
        //        }
        /**
         * 注：临时cookie(没有expires参数的cookie)不能带有domain选项。
         * 当客户端发送一个http请求时，会将有效的cookie一起发送给服务器。
         * 如果一个cookie的domain和path参数和URL匹配，那么这个cookie就是有效的。
         * 一个URL中包含有domain和path，可以参
         */
        for (i in values.indices) {
            val value = values[i]
            cookieManager.setCookie(domain, value)
        }
        CookieSyncManager.getInstance().sync()
        LogUtil.info("seted cookie---" + cookieManager.getCookie(domain))
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (parent == null) return super.dispatchTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = ev.x.toInt()
                mLastY = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val newX = ev.x.toInt()
                val newY = ev.y.toInt()
                val deltaX = newX - mLastX
                val deltaY = newY - mLastY
                if (Math.abs(deltaX) < Math.abs(deltaY)) {
                    parent!!.requestDisallowInterceptTouchEvent(false)
                    if (parent is SwipeRefreshLayout) {
                        (parent as SwipeRefreshLayout).isEnabled = true
                    }
                } else {
                    if (parent is SwipeRefreshLayout) {
                        (parent as SwipeRefreshLayout).isEnabled = false
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
