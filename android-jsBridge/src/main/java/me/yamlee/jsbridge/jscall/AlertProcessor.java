package me.yamlee.jsbridge.jscall;


import me.yamlee.jsbridge.BaseJsCallProcessor;
import me.yamlee.jsbridge.JsCallData;
import me.yamlee.jsbridge.NativeComponentProvider;
import me.yamlee.jsbridge.ui.WebActionView;

/**
 * Js调用原生显示弹框处理
 *
 * @author yamlee
 */
public class AlertProcessor extends BaseJsCallProcessor {
    public static final String FUNC_NAME = "alert";
    private WebActionView nearWebLogicView;

    public AlertProcessor(NativeComponentProvider componentProvider) {
        super(componentProvider);
        nearWebLogicView = componentProvider.provideWebLogicView();
    }

    @Override
    public boolean onHandleJsQuest(JsCallData callData) {
        if (FUNC_NAME.equals(callData.getFunc())) {
            AlertRequest alertRequest = convertJsonToObject(callData.getParams(), AlertRequest.class);
            nearWebLogicView.showAlert(alertRequest.title, alertRequest.msg);
            return true;
        }
        return false;
    }

    @Override
    public String getFuncName() {
        return FUNC_NAME;
    }


    static class AlertRequest {
        /**
         * title : title
         * msg : 测试弹出消息
         */
        public String title;
        public String msg;
    }

}
