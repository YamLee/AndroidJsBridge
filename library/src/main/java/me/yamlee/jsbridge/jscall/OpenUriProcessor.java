package me.yamlee.jsbridge.jscall;

import android.content.Context;


import org.json.JSONException;
import org.json.JSONObject;

import me.yamlee.jsbridge.BaseJsCallProcessor;
import me.yamlee.jsbridge.JsCallData;
import me.yamlee.jsbridge.NativeComponentProvider;
import me.yamlee.jsbridge.ui.NearInteraction;

/**
 * 根据uri做指定的跳转动作，比如Uri是"nearmcht://view-login"就会跳转到登录界面
 *
 * @author yamlee
 */
public class OpenUriProcessor extends BaseJsCallProcessor {

    public static final String FUNC_NAME = "openUri";

    public OpenUriProcessor(NativeComponentProvider componentProvider) {
        super(componentProvider);
    }

    @Override
    public boolean onHandleJsQuest(JsCallData callData) {
        if (FUNC_NAME.equals(callData.getFunc())) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(callData.getParams());
                String uri = (String) jsonObject.get("uri");
                NearInteraction interaction = componentProvider.provideWebInteraction();
                Context context = componentProvider.provideApplicationContext();
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public String getFuncName() {
        return FUNC_NAME;
    }
}
