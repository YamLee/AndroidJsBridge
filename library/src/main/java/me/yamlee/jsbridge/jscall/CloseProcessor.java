package me.yamlee.jsbridge.jscall;


import org.json.JSONException;
import org.json.JSONObject;

import me.yamlee.jsbridge.BaseJsCallProcessor;
import me.yamlee.jsbridge.JsCallData;
import me.yamlee.jsbridge.NativeComponentProvider;
import me.yamlee.jsbridge.ui.NearWebLogicView;

/**
 * Js调用界面关闭
 *
 * @author yamlee
 */
public class CloseProcessor extends BaseJsCallProcessor {

    public static final String FUNC_NAME = "close";
    private NearWebLogicView.WebLogicListener nearInteraction;

    public CloseProcessor(NativeComponentProvider componentProvider) {
        super(componentProvider);
    }

    @Override
    public boolean onHandleJsQuest(JsCallData callData) {
        if (FUNC_NAME.equals(callData.getFunc())) {
            nearInteraction = componentProvider.provideWebInteraction();
            try {
                JSONObject jsonObject = new JSONObject(callData.getParams());
                String type = jsonObject.optString("type");
                if ("1".equals(type)) {
                    nearInteraction.finishActivity();
                } else if ("2".equals(type)) {
                    nearInteraction.clearTopWebActivity();
                } else {
                    nearInteraction.finishActivity();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public String getFuncName() {
        return FUNC_NAME;
    }
}