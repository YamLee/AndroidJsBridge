package me.yamlee.jsbridge.model;


import org.json.JSONException;
import org.json.JSONObject;

import me.yamlee.jsbridge.HybridUpdateValue;
import me.yamlee.jsbridge.JsCallData;
import me.yamlee.jsbridge.entity.HybridUpdateEntity;
import me.yamlee.jsbridge.jscall.AlertProcessor;
import me.yamlee.jsbridge.jscall.HttpRequestProcessor;
import me.yamlee.jsbridge.jscall.OpenUriProcessor;
import me.yamlee.jsbridge.jscall.SendParamsProcessor;
import me.yamlee.jsbridge.jscall.ToastProcessor;

/**
 * Created by fengruicong on 16-4-1.
 */
public class HybridUpdateEntityMapper {
    /**
     * 查看文档 http://git.qfpay.net/client/near_merchant_doc/blob/master/Native_H5_Hybrid.md
     */
    public static HybridUpdateEntity transfer(JSONObject data) {
        HybridUpdateEntity model = new HybridUpdateEntity();
        model.setScheme(data.optString(HybridUpdateValue.KEY_SCHEME));
        model.setPath(data.optString(HybridUpdateValue.KEY_PATH));
        model.setAction(data.optString(HybridUpdateValue.KEY_ACTION));
        JSONObject jsonParams = data.optJSONObject(HybridUpdateValue.KEY_PARAMS);
        if (jsonParams != null) {
            model.setJsonParams(jsonParams);
            model.setContent(jsonParams.optString(HybridUpdateValue.KEY_PARAMS_CONTENT));
            model.setTitle(jsonParams.optString(HybridUpdateValue.KEY_PARAMS_TITLE));
            model.setUrl(jsonParams.optString(HybridUpdateValue.KEY_PARAMS_URL));
        }
        return model;
    }

    /**
     * 兼容老版本的JsBridge规范
     * @param entity
     * @return
     */
    public static JsCallData transfer(HybridUpdateEntity entity) {
        JsCallData jsCallData = new JsCallData();
        //将老的JSBridge请求原生的网络数据转换成新JsBridge数据形式
        if (HybridUpdateValue.VALUE_SCHEME_APIJS.equals(entity.getScheme())) {
            jsCallData.setFunc(HttpRequestProcessor.FUNC_NAME);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("method", entity.getAction());
                jsonObject.put("url", entity.getPath());
                jsonObject.put("params", entity.getJsonParams().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsCallData.setParams(jsonObject.toString());
        } else if (HybridUpdateValue.VALUE_SCHEME_H5.equals(entity.getScheme())) {
            jsCallData.setFunc(OpenUriProcessor.FUNC_NAME);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("uri", entity.getPath());
                jsonObject.put("params", entity.getJsonParams().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsCallData.setParams(jsonObject.toString());
        } else if (HybridUpdateValue.VALUE_SCHEME_PARAMJS.equals(entity.getScheme())) {
            jsCallData.setFunc(SendParamsProcessor.FUNC_NAME);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("module", entity.getAction());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsCallData.setParams(jsonObject.toString());
        } else if (HybridUpdateValue.VALUE_SCHEME_NATIVE.equals(entity.getScheme())) {
            jsCallData.setFunc(entity.getAction());
            if (AlertProcessor.FUNC_NAME.equals(entity.getAction())) {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonParams = entity.getJsonParams();
                try {
                    jsonObject.put("title", jsonParams.opt("title"));
                    jsonObject.put("msg", jsonParams.opt("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsCallData.setParams(jsonObject.toString());
            } else if (ToastProcessor.FUNC_NAME.equals(entity.getAction())) {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonParams = entity.getJsonParams();
                try {
                    jsonObject.put("msg", jsonParams.opt("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                jsCallData.setParams(entity.getJsonParams().toString());
            }
        }
        return jsCallData;
    }
}
