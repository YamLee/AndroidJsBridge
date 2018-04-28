package me.yamlee.jsbridge.ui

import android.app.Activity
import android.content.Intent
import android.support.annotation.AnimRes

/**
 * 基础Activity交互接口
 */
interface Interaction {
    /**
     * 跳转到下一个Activity,intent不含启动context和目的activity class
     *
     * @param intent        通过Activity的静态getCallIntent()方法构造intent
     * @param activityClass TargetActivity.class类
     */
    fun startActivity(intent: Intent, activityClass: Class<out Activity>)


    /**
     * 跳转到下一个本应用Activity,限制本应用包名，
     * 实际上调用的就是[android.support.v7.app.AppCompatActivity]
     * 的startActivity()方法
     *
     * @param intent intent
     */
    fun startActivity(intent: Intent)

    /**
     * 跳转到下一个可以接受该action的Activity,
     * 无包名限定，
     * 实际上调用的就是[android.support.v7.app.AppCompatActivity]
     * 的startActivity()方法
     *
     * @param intent intent
     */
    fun startOutsideActivity(intent: Intent)

    /**
     * 启动Activity带有Result回调
     *
     * @param intent      带有启动Context的Intent
     * @param requestCode 请求码
     */
    fun startActivityForResult(intent: Intent, requestCode: Int)

    /**
     * 启动activity带有result回调
     *
     * @param intent        此Intent可以不用Context，BaseActivity会将activityClass指定的activity设置进去
     * @param requestCode   请求码
     * @param activityClass 需要跳转的Activity类
     */
    fun startActivityForResult(intent: Intent, requestCode: Int, activityClass: Class<out Activity>)


    /**
     * 结束activity,实际上调用的是[android.support.v7.app.AppCompatActivity]
     * 的finish()方法
     */
    fun finishActivity()

    /**
     * 延迟finish Activity
     *
     * @param millis 延迟的毫秒数
     */
    fun finishActivityDelay(millis: Long)

    /**
     * 退场动画结束Activity
     */
    fun finishActivityWithAnim()

    /**
     * 指定退场结束动画
     */
    fun finishActivityWithAnim(@AnimRes enterAnim: Int, @AnimRes exitAnim: Int)

    /**
     * 设置activity回调
     *
     * @param resultCode 请求码
     * @param data       跳转的Intent
     */
    fun setActivityResult(resultCode: Int, data: Intent)

}
