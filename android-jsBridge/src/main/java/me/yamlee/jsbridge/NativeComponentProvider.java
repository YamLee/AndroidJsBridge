package me.yamlee.jsbridge;

import android.app.Activity;
import android.content.Context;

import me.yamlee.jsbridge.ui.WebActionView;


/**
 * Js调用处理类中需要使用类的提供者
 *
 * @author yamlee
 */
public interface NativeComponentProvider {

    /**
     * 提供Web界面Fragment的View交互类
     *
     * @return Fragment交互接口
     */
    WebActionView provideWebLogicView();

    /**
     * 提供Web界面Activity的交互类
     *
     * @return Activity交互类
     */
    WebActionView.WebLogicListener provideWebInteraction();

    /**
     * 提供全局的Context对象
     *
     * @return 全局上下文
     */
    Context provideApplicationContext();

    /**
     * 提供全Activity上下文
     *
     * @return
     */
    Activity provideActivityContext();
}
