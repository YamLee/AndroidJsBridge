package me.yamlee.jsbridge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("HardCodedStringLiteral")
@SuppressLint({"SetJavaScriptEnabled", "NewApi"})
public class WVJBWebViewClient extends WebViewClient {

    private static final String kTag = "WVJB";
    private static final String kInterface = kTag + "Interface";
    private static final String kCustomProtocolScheme = "wvjbscheme";
    private static final String kQueueHasMessage = "__WVJB_QUEUE_MESSAGE__";
    private static boolean logging = false;
    private WebView webView;
    //native发送给h5的数据以队列形式存放
    private ArrayList<WVJBMessage> startupMessageQueue = null;
    private Map<String, WVJBResponseCallback> responseCallbacks = null;
    private Map<String, WVJBHandler> messageHandlers = null;
    private long uniqueId = 0;
    private WVJBHandler messageHandler;
    private MyJavascriptInterface myInterface = new MyJavascriptInterface();

    public interface WVJBResponseCallback {
        void callback(Object data);
    }

    public interface WVJBHandler {
        void request(Object data, WVJBResponseCallback callback);
    }

    public WVJBWebViewClient(WebView webView) {
        this(webView, null);
    }

    /**
     * 构造器
     *
     * @param messageHandler 默认handler
     */
    @SuppressLint("AddJavascriptInterface")
    public WVJBWebViewClient(WebView webView, WVJBHandler messageHandler) {
        this.webView = webView;
        this.webView.getSettings().setJavaScriptEnabled(true);
        //此处不应加版本判断
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        this.webView.addJavascriptInterface(myInterface, kInterface);
//        }
        this.responseCallbacks = new HashMap<>();
        this.messageHandlers = new HashMap<>();
        this.startupMessageQueue = new ArrayList<>();
        this.messageHandler = messageHandler;
    }

    public void enableLogging() {
        logging = true;
    }

    /**
     * 注册H5向native发请求Handler(native等待h5指令做出响应)
     *
     * @param handlerName 双方协议handler名称
     * @param handler     请求handler
     */
    protected void registerHandler(String handlerName, WVJBHandler handler) {
        if (handlerName == null || handlerName.length() == 0 || handler == null)
            return;
        messageHandlers.put(handlerName, handler);
    }
//
//    public void send(Object data) {
//        sendData(data, null, null);
//    }
//
//    public void send(Object data, WVJBResponseCallback responseCallback) {
//        sendData(data, responseCallback, null);
//    }
//
//    public void callHandler(String handlerName) {
//        callHandler(handlerName, null, null);
//    }
//
//    public void callHandler(String handlerName, Object data) {
//        callHandler(handlerName, data, null);
//    }
//
//    public void callHandler(String handlerName, Object data,
//                            WVJBResponseCallback responseCallback) {
//        sendData(data, responseCallback, handlerName);
//    }
//
//    /**
//     * native主动发送数据给H5
//     *
//     * @param data             发送的数据
//     * @param responseCallback 响应h5结果
//     * @param handlerName      双方协议的handlerName
//     */
//    private void sendData(Object data, WVJBResponseCallback responseCallback,
//                          String handlerName) {
//        if (data == null && (handlerName == null || handlerName.length() == 0))
//            return;
//        WVJBMessage message = new WVJBMessage();
//        if (data != null) {
//            message.data = data;
//        }
//        if (responseCallback != null) {
//            String callbackId = "objc_cb_" + (++uniqueId);
//            responseCallbacks.put(callbackId, responseCallback);
//            message.callbackId = callbackId;
//        }
//        if (handlerName != null) {
//            message.handlerName = handlerName;
//        }
//        queueMessage(message);
//    }

    /**
     * onPageFinished前发送，页面初始化未完成前发送数据，以队列形式存储到startupMessageQueue待onPageFinished统一dispatch
     * onPageFinished后发送，直接dispatch
     *
     * @param message 发送的数据
     */
    private void queueMessage(WVJBMessage message) {
        if (startupMessageQueue != null) {
            startupMessageQueue.add(message);
        } else {
            dispatchMessage(message);
        }
    }

    private void dispatchMessage(WVJBMessage message) {
        String messageJSON = message2JSONObject(message).toString()
                .replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\"", "\\\\\"")
                .replaceAll("\'", "\\\\\'")
                .replaceAll("\n", "\\\\\n")
                .replaceAll("\r", "\\\\\r")
                .replaceAll("\f", "\\\\\f");

        log("SEND", messageJSON);
        executeJavascript("WebViewJavascriptBridge._handleMessageFromObjC('"
                + messageJSON + "');");
    }

    private void flushMessageQueue() {
        String script = "WebViewJavascriptBridge._fetchQueue()";
        executeJavascript(script, new JavascriptCallback() {
            public void onReceiveValue(String messageQueueString) {
                if (messageQueueString == null
                        || messageQueueString.length() == 0)
                    return;
                processQueueMessage(messageQueueString);
            }
        });
    }

    private void processQueueMessage(String messageQueueString) {
        try {
            JSONArray messages = new JSONArray(messageQueueString);
            for (int i = 0; i < messages.length(); i++) {
                JSONObject jo = messages.getJSONObject(i);
                log("RCVD", jo);
                WVJBMessage message = JSONObject2WVJBMessage(jo);
                if (message.responseId != null) {
                    WVJBResponseCallback responseCallback = responseCallbacks
                            .remove(message.responseId);
                    if (responseCallback != null) {
                        responseCallback.callback(message.responseData);
                    }
                } else {
                    WVJBResponseCallback responseCallback = null;
                    if (message.callbackId != null) {
                        final String callbackId = message.callbackId;
                        responseCallback = new WVJBResponseCallback() {
                            @Override
                            public void callback(Object data) {
                                WVJBMessage msg = new WVJBMessage();
                                msg.responseId = callbackId;
                                //返回的data需要时string类型
                                if (data instanceof String || data instanceof JSONObject) {
                                    msg.responseData = data;
                                } else {
                                    Gson gson = new Gson();
                                    msg.responseData = gson.toJson(data);
                                }
                                queueMessage(msg);
                            }
                        };
                    }
                    WVJBHandler handler;
                    if (message.handlerName != null) {
                        handler = messageHandlers.get(message.handlerName);
                    } else {
                        handler = messageHandler;
                    }
                    if (handler != null) {
                        handler.request(message.data, responseCallback);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void executeJavascript(String script) {
        executeJavascript(script, null);
    }

    /**
     * 执行JS语句
     *
     * @param script   待执行的js
     * @param callback js执行完之后返回的结果
     */
    private void executeJavascript(final String script,
                                   final JavascriptCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    if (callback != null) {
                        if (value != null && value.startsWith("\"")
                                && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1)
                                    .replaceAll("\\\\", "");
                        }
                        callback.onReceiveValue(value);
                    }
                }
            });
        } else {
            if (callback != null) {
                myInterface.addCallback(++uniqueId + "", callback);
                //JS调用Java window.jsInterfaceName.methodName(parameterValues)
                //Java调用JS loadUrl(javascript:methodName(parameterValues))
                webView.loadUrl("javascript:window." + kInterface
                        + ".onResultForScript(" + uniqueId + "," + script + ")");
            }//执行WebViewJavascriptBridge的js代码
            else {
                webView.loadUrl("javascript:" + script);
            }

        }
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        new LoadBridgeAsyncTask(webView.getContext()).execute();
        super.onPageFinished(view, url);
    }

    private class LoadBridgeAsyncTask extends AsyncTask<Void, Void, String> {
        private Context context;

        LoadBridgeAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                InputStream is = context.getAssets()
                        .open("WebViewJavascriptBridge.js");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                return new String(buffer);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsBridge) {
            super.onPostExecute(jsBridge);
            if (jsBridge != null) {
                executeJavascript(jsBridge);
                if (startupMessageQueue != null) {
                    for (int i = 0; i < startupMessageQueue.size(); i++) {
                        dispatchMessage(startupMessageQueue.get(i));
                    }
                    startupMessageQueue = null;
                }
            } else {
                Toast.makeText(context, "加载js引擎出错,请退出页面重试!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //接收h5发送的数据
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(kCustomProtocolScheme)) {
            if (url.indexOf(kQueueHasMessage) > 0) {
                flushMessageQueue();
            }
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    private JSONObject message2JSONObject(WVJBMessage message) {
        JSONObject jo = new JSONObject();
        try {
            if (message.callbackId != null) {
                jo.put("callbackId", message.callbackId);
            }
            if (message.data != null) {
                jo.put("data", message.data);
            }
            if (message.handlerName != null) {
                jo.put("handlerName", message.handlerName);
            }
            if (message.responseId != null) {
                jo.put("responseId", message.responseId);
            }
            if (message.responseData != null) {
                jo.put("responseData", message.responseData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    private WVJBMessage JSONObject2WVJBMessage(JSONObject jo) {
        WVJBMessage message = new WVJBMessage();
        try {
            if (jo.has("callbackId")) {
                message.callbackId = jo.getString("callbackId");
            }
            if (jo.has("data")) {
                message.data = jo.get("data");
            }
            if (jo.has("handlerName")) {
                message.handlerName = jo.getString("handlerName");
            }
            if (jo.has("responseId")) {
                message.responseId = jo.getString("responseId");
            }
            if (jo.has("responseData")) {
                message.responseData = jo.get("responseData");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    private class WVJBMessage {
        Object data = null;
        String callbackId = null;
        String handlerName = null;
        String responseId = null;
        Object responseData = null;
    }

    private class MyJavascriptInterface {
        Map<String, JavascriptCallback> map = new HashMap<>();

        private void addCallback(String key, JavascriptCallback callback) {
            map.put(key, callback);
        }

        @JavascriptInterface
        public void onResultForScript(final String key, final String value) {
            Log.i(kTag, "onResultForScript: " + value);
            ((Activity) webView.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JavascriptCallback callback = map.remove(key);
                    if (callback != null) {
                        callback.onReceiveValue(value);
                    }
                }
            });
        }
    }

    private interface JavascriptCallback {
        void onReceiveValue(String value);

    }

    private void log(String action, Object json) {
        if (!logging)
            return;
        String jsonString = String.valueOf(json);
        if (jsonString.length() > 500) {
            Log.i(kTag, action + ": " + jsonString.substring(0, 500) + " [...]");
        } else {
            Log.i(kTag, action + ": " + jsonString);
        }
    }
}
