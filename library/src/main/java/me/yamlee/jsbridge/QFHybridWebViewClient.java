package me.yamlee.jsbridge;

import android.content.Intent;
import android.webkit.WebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.yamlee.jsbridge.jscall.AlertProcessor;
import me.yamlee.jsbridge.jscall.CloseProcessor;
import me.yamlee.jsbridge.jscall.ToastProcessor;
import timber.log.Timber;

/**
 * @author by fengruicong on 16/12/2.
 */
public class QFHybridWebViewClient extends WVJBWebViewClient {

    private Map<String, JsCallProcessor> jsCallProcessors;

    public QFHybridWebViewClient(final WebView webView, WVJBHandler wvjbHandler,
                                 final NativeComponentProvider componentProvider) {
        super(webView, wvjbHandler);
        registerCallProcessors(componentProvider);
        registerHandler("QFHybrid", new WVJBHandler() {
            @Override
            public void request(Object data, WVJBResponseCallback callback) {
                JSONObject jsonObject = (JSONObject) data;
                handleData(jsonObject, callback);
            }
        });
    }

    private void registerCallProcessors(NativeComponentProvider componentProvider) {
        registerJsCallProcessor(new AlertProcessor(componentProvider));
        registerJsCallProcessor(new CloseProcessor(componentProvider));
        registerJsCallProcessor(new ToastProcessor(componentProvider));
    }


    public void registerJsCallProcessor(JsCallProcessor callHandler) {
        if (jsCallProcessors == null) {
            jsCallProcessors = new HashMap<>();
        }
        jsCallProcessors.put(callHandler.getFuncName(), callHandler);
    }

    private void handleData(JSONObject jsonObject, WVJBResponseCallback callback) {
        JsCallData jsCallData = new JsCallData();
        try {
            jsCallData.setFunc(jsonObject.optString("func"));
            jsCallData.setParams(jsonObject.optJSONObject("params").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        proceed(jsCallData, callback);
    }

    public void proceed(JsCallData callData, WVJBWebViewClient.WVJBResponseCallback callback) {
        if (jsCallProcessors == null) return;
        String msg;
        JsCallProcessor jsCallProcessor = jsCallProcessors.get(callData.getFunc());
        if (jsCallProcessor != null) {
            boolean handled = jsCallProcessor.process(callData, callback);
            msg = handled ? String.format("%s Processor handled js call", jsCallProcessor.getFuncName()) :
                    String.format("%s Processor have not handled target js call", jsCallProcessor.getFuncName());
            Timber.i(msg);
        } else {
            msg = String.format("No JsCallProcessor can handle jsCall %s ", jsCallProcessor.getFuncName());
            Timber.e(msg);
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (jsCallProcessors == null) return;
        for (JsCallProcessor processor : jsCallProcessors.values()) {
            boolean handled = processor.onActivityResult(requestCode, resultCode, data);

            if (handled) {
                String msg = String.format("Processor %s handled ActivityResult",
                        processor.getFuncName());
                Timber.i(msg);
                return;
            }

        }
    }


}
