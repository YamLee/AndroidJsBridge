package me.yamlee.jsbridge;

import android.content.Context;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import timber.log.Timber;

/**
 * Created by fengruicong on 16/6/2.
 */
@SuppressWarnings("HardCodedStringLiteral")
public class BridgeWebChromeClient extends WebChromeClient {
    public static final int FILECHOOSER_RESULTCODE = 10001;
    private ValueCallback mUploadMsg;
    private String cameraFilePath = "";
    private JsResult mResult;
    private Context mContext;

    public BridgeWebChromeClient(Context context) {
        mContext = context;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {
        Log.i("--Js Console.log:--", cm.message() + " -- From line "
                + cm.lineNumber() + " of "
                + cm.sourceId());
        return true;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        mResult = result;
        return super.onJsAlert(view, url, message, result);

    }


    public void onStop() {
        if (mResult != null) {
            mResult.cancel();
        }
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Timber.v("web load progress" + newProgress);
    }

    //配置H5请求地理位置权限
    @Override
    public void onGeolocationPermissionsShowPrompt(final String origin,
                                                   final GeolocationPermissions.Callback callback) {
        // 请求定位信息
        callback.invoke(origin, true, false);
    }

}
