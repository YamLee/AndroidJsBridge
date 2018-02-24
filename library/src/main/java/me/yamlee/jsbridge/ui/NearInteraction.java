package me.yamlee.jsbridge.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.AnimRes;
import android.support.v4.app.Fragment;

/**
 * Essential模块基础Activity交互接口
 */
public interface NearInteraction {
    /**
     * 跳转到下一个Activity,intent不含启动context和目的activity class
     *
     * @param intent        通过Activity的静态getCallIntent()方法构造intent
     * @param activityClass TargetActivity.class类
     */
    void startNearActivity(Intent intent, Class<? extends Activity> activityClass);


    /**
     * 跳转到下一个本应用Activity,限制本应用包名，
     * 实际上调用的就是{@link android.support.v7.app.AppCompatActivity}
     * 的startActivity()方法
     *
     * @param intent intent
     */
    void startNearActivity(Intent intent);

    /**
     * 跳转到下一个可以接受该action的Activity,
     * 无包名限定，
     * 实际上调用的就是{@link android.support.v7.app.AppCompatActivity}
     * 的startActivity()方法
     *
     * @param intent intent
     */
    void startOutsideActivity(Intent intent);

    /**
     * 启动Activity带有Result回调
     *
     * @param intent      带有启动Context的Intent
     * @param requestCode 请求码
     */
    void startNearActivityForResult(Intent intent, int requestCode);

    /**
     * 启动activity带有result回调
     *
     * @param intent        此Intent可以不用Context，BaseActivity会将activityClass指定的activity设置进去
     * @param requestCode   请求码
     * @param activityClass 需要跳转的Activity类
     */
    void startNearActivityForResult(Intent intent, int requestCode, Class<? extends Activity> activityClass);


    /**
     * 结束activity,实际上调用的是{@link android.support.v7.app.AppCompatActivity}
     * 的finish()方法
     */
    void finishActivity();

    /**
     * 延迟finish Activity
     *
     * @param millis 延迟的毫秒数
     */
    void finishActivityDelay(long millis);

    /**
     * 退场动画结束Activity
     */
    void finishActivityWithAnim();

    /**
     * 指定退场结束动画
     */
    void finishActivityWithAnim(@AnimRes int enterAnim, @AnimRes int exitAnim);

    /**
     * 设置activity回调
     *
     * @param resultCode 请求码
     * @param data       跳转的Intent
     */
    void setActivityResult(int resultCode, Intent data);

}
