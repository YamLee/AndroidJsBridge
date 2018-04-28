package me.yamlee.jsbridge.jscall;

import org.json.JSONException;
import org.json.JSONObject;

import me.yamlee.jsbridge.BaseJsCallProcessor;
import me.yamlee.jsbridge.JsCallData;
import me.yamlee.jsbridge.NativeComponentProvider;
import me.yamlee.jsbridge.WVJBWebViewClient;
import timber.log.Timber;

/**
 * Js获取原生传过来的参数
 *
 * @author yamlee
 */
public class SendParamsProcessor extends BaseJsCallProcessor {
    public static final String FUNC_NAME = "getParams";

    public SendParamsProcessor(NativeComponentProvider componentProvider) {
        super(componentProvider);
    }

    @Override
    public boolean onHandleJsQuest(JsCallData callData) {
        if (FUNC_NAME.equals(callData.getFunc())) {
            String params = callData.getParams();
            try {
                JSONObject jsonObject = new JSONObject(params);
                String module = jsonObject.optString("module");
                Timber.i("module is" + module);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean onResponse(WVJBWebViewClient.WVJBResponseCallback callback) {
        callback.callback(new String(""));
        return true;
    }

    @Override
    public String getFuncName() {
        return FUNC_NAME;
    }
}
