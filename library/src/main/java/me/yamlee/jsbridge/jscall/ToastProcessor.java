package me.yamlee.jsbridge.jscall;

import org.json.JSONException;
import org.json.JSONObject;

import me.yamlee.jsbridge.BaseJsCallProcessor;
import me.yamlee.jsbridge.JsCallData;
import me.yamlee.jsbridge.NativeComponentProvider;
import me.yamlee.jsbridge.ui.NearWebLogicView;

/**
 * JS调用Toast提示
 *
 * @author yamlee
 */
public class ToastProcessor extends BaseJsCallProcessor {

    public static final String FUNC_NAME = "toast";
    private NearWebLogicView view;

    public ToastProcessor(NativeComponentProvider componentProvider) {
        super(componentProvider);
    }

    @Override
    public boolean onHandleJsQuest(JsCallData callData) {
        if (FUNC_NAME.equals(callData.getFunc())) {
            view = componentProvider.provideWebLogicView();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(callData.getParams());
                String msg = (String) jsonObject.get("msg");
                view.showToast(msg);
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
