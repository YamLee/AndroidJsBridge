package me.yamlee.jsbridge

import android.content.Context
import android.os.Environment

/**
 * 组件更新 所用常量数据
 *
 *
 * 文档 http://git.qfpay.net/client/near_merchant_doc/blob/master/Native_H5_Hybrid.md
 * Created by fengruicong on 16-3-25.
 */
object HybridUpdateValue {
    val DOWNLOAD_FILE_PATH = Environment.getExternalStorageDirectory().absolutePath
    val DOWNLOAD_FILE_NAME = "/website.tar.gz"
    val DEFAULT_MAPPING_JSON = "{\"/templates/activity.html\":{\"offline\":\"/templates/activity.html\",\"online\":\"http://wx.qfpay.com/near/activity.html\"},\"member_actv_preview\":{\"offline\":\"/templates/activity-preview.html\",\"online\":\"http://wx.qfpay.com/near/activity-preview.html\"}}"

    val downloadImageName: String
        get() = System.currentTimeMillis().toString() + ".jpg"

    //
    val KEY_SCHEME = "schema"
    val KEY_PATH = "path"
    val KEY_ACTION = "action"
    val KEY_PARAMS = "params"
    //js请求页面接口数据
    val VALUE_SCHEME_APIJS = "near-merchant-offlineAPIJS://"
    //h5请求调起本地组件
    val VALUE_SCHEME_NATIVE = "near-merchant-native://"
    //h5请求本地提供页面请求参数
    val VALUE_SCHEME_PARAMJS = "near-merchant-offlineParamsJS://"
    //h5请求跳转页面(包括离线和在线)
    val VALUE_SCHEME_H5 = "near-merchant-h5://"

    //path参数（在线页面path以http开头）
    val VALUE_PATH_ONLINE_START = "http"
    val VALUE_PATH_OFFLINE_START = "/"
    val VALUE_PATH_NATIVE_START = "nearmcht"

    //action参数
    val VALUE_ACTION_GET = "get"
    val VALUE_ACTION_POST = "post"
    val VALUE_ACTION_TOAST = "toast"
    val VALUE_ACTION_ALERT = "alert"
    val VALUE_ACTION_DOWNLOAD = "download"
    val VALUE_ACTION_BACK = "back"
    val VALUE_ACTION_CHECKOUT = "checkout"
    val VALUE_ACTION_NATIVE_PAGE = "native-page"
    val VALUE_ACTION_EXIT_APP = "logout"

    //params参数
    val KEY_PARAMS_CONTENT = "content"
    val KEY_PARAMS_TITLE = "title"
    val KEY_PARAMS_URL = "url"

    //固定页面的配置key
    val KEY_NOTIFY_SALE_PREVIEW = "notify_special_sale_preview"

    fun getHybridRootPath(context: Context): String {
        return context.filesDir.absolutePath
    }

    fun getHybridPrefixUrl(context: Context): String {
        return "file://" + context.filesDir.absolutePath + "/website"
    }

    fun getHybridPrefixUrlLocalPath(context: Context): String {
        return context.filesDir.absolutePath + "/website"
    }

    fun getHybridPrefixAssetLocalPath(assetPath: String): String {
        return "file:///android_asset/website$assetPath"
    }

}
