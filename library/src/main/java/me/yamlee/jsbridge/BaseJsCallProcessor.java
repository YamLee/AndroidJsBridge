package me.yamlee.jsbridge;

import android.content.Intent;

import com.google.gson.Gson;

/**
 * JsCall基础处理类，具体的JsCall接口处理类集成此接口
 *
 * @author yamlee
 */
public abstract class BaseJsCallProcessor implements JsCallProcessor {
    private Gson gson;
    protected NativeComponentProvider componentProvider;

    public BaseJsCallProcessor(NativeComponentProvider componentProvider) {
        this.componentProvider = componentProvider;
        gson = new Gson();
    }

    @Override
    public final boolean process(JsCallData callData,
                                 WVJBWebViewClient.WVJBResponseCallback callback) {
        boolean handled = onHandleJsQuest(callData);
        boolean onResponsed = false;
        if (handled) {
            onResponsed = onResponse(callback);
        }
        //如果子类处理这个请求但是没有做出回应，这里统一加上通用返回
        if (!onResponsed && handled) {
            BaseJsCallResponse response = new BaseJsCallResponse();
            response.ret = "OK";
            callback.callback(response);
        }
        return handled;
    }

    public final void destroy() {
        onDestroy();
    }

    public boolean onHandleJsQuest(JsCallData callData) {
        return false;
    }

    public boolean onResponse(WVJBWebViewClient.WVJBResponseCallback callback) {
        return false;
    }

    public void onDestroy() {

    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    public final <T> T convertJsonToObject(String json, Class<T> tClass) {
        T object = gson.fromJson(json, tClass);
        return object;
    }

    public final String convertObjectString(Object object) {
        return gson.toJson(object);
    }

}
