package me.yamlee.jsbridge;

/**
 * Created by yamlee on 1/11/17.
 */
public class JsCallData {

    /**
     * func : alert
     * params : {"title":"title","msg":"测试弹出消息"}
     */
    private String func;
    private String params;

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}
