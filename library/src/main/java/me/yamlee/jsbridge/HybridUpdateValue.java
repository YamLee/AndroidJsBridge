package me.yamlee.jsbridge;

import android.content.Context;
import android.os.Environment;

/**
 * 组件更新 所用常量数据
 * <p>
 * 文档 http://git.qfpay.net/client/near_merchant_doc/blob/master/Native_H5_Hybrid.md
 * Created by fengruicong on 16-3-25.
 */
@SuppressWarnings("HardCodedStringLiteral")
public class HybridUpdateValue {
    public static final String DOWNLOAD_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DOWNLOAD_FILE_NAME = "/website.tar.gz";
    public static final String DEFAULT_MAPPING_JSON =
            "{\"/templates/activity.html\":{\"offline\":\"/templates/activity.html\",\"online\":\"http://wx.qfpay.com/near/activity.html\"},\"member_actv_preview\":{\"offline\":\"/templates/activity-preview.html\",\"online\":\"http://wx.qfpay.com/near/activity-preview.html\"}}";

    public static String getHybridRootPath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    public static String getHybridPrefixUrl(Context context) {
        return "file://" + context.getFilesDir().getAbsolutePath() + "/website";
    }

    public static String getHybridPrefixUrlLocalPath(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/website";
    }

    public static String getHybridPrefixAssetLocalPath(String assetPath) {
        return "file:///android_asset/website" + assetPath;
    }

    public static String getDownloadImageName() {
        return System.currentTimeMillis() + ".jpg";
    }

    //
    public static final String KEY_SCHEME = "schema";
    public static final String KEY_PATH = "path";
    public static final String KEY_ACTION = "action";
    public static final String KEY_PARAMS = "params";
    //js请求页面接口数据
    public static final String VALUE_SCHEME_APIJS = "near-merchant-offlineAPIJS://";
    //h5请求调起本地组件
    public static final String VALUE_SCHEME_NATIVE = "near-merchant-native://";
    //h5请求本地提供页面请求参数
    public static final String VALUE_SCHEME_PARAMJS = "near-merchant-offlineParamsJS://";
    //h5请求跳转页面(包括离线和在线)
    public static final String VALUE_SCHEME_H5 = "near-merchant-h5://";

    //path参数（在线页面path以http开头）
    public static final String VALUE_PATH_ONLINE_START = "http";
    public static final String VALUE_PATH_OFFLINE_START = "/";
    public static final String VALUE_PATH_NATIVE_START = "nearmcht";

    //action参数
    public static final String VALUE_ACTION_GET = "get";
    public static final String VALUE_ACTION_POST = "post";
    public static final String VALUE_ACTION_TOAST = "toast";
    public static final String VALUE_ACTION_ALERT = "alert";
    public static final String VALUE_ACTION_DOWNLOAD = "download";
    public static final String VALUE_ACTION_BACK = "back";
    public static final String VALUE_ACTION_CHECKOUT = "checkout";
    public static final String VALUE_ACTION_NATIVE_PAGE = "native-page";
    public static final String VALUE_ACTION_EXIT_APP = "logout";

    //params参数
    public static final String KEY_PARAMS_CONTENT = "content";
    public static final String KEY_PARAMS_TITLE = "title";
    public static final String KEY_PARAMS_URL = "url";

    //固定页面的配置key
    public static final String KEY_NOTIFY_SALE_PREVIEW = "notify_special_sale_preview";

}
