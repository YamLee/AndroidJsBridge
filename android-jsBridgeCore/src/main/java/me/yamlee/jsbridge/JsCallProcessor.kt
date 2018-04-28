package me.yamlee.jsbridge

import android.content.Intent

/**
 * JS调原生，原始处理器接口规范
 * 相关文档链接
 *
 * @author yamlee
 */
interface JsCallProcessor {

    /**
     * 获取JsBridge调用接口名称
     *
     * @return 接口名称
     */
    fun getFuncName(): String

    /**
     * 处理JS调用
     *
     * @param callData 参数
     * @return true表示已处理，请求不会传递给下一个处理器，反之则会传递给下一个处理器
     */
    fun process(callData: JsCallData, callback: WVJBWebViewClient.WVJBResponseCallback): Boolean

    /**
     * 界面跳转后再回调的处理方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean

}
