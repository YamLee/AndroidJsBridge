package me.yamlee.jsbridge.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * 此工具类包含获取apk版本号和build号以及渠道号
 *
 *
 *
 * @author yamlee
 */
object ApkUtil {
    /**
     * 判断当前渠道包是否显示首发字样
     *
     * @param context application
     * @return
     */
    fun isFirstPublish(context: Context): Boolean {
        try {
            val ai = context.packageManager
                    .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            return bundle.getBoolean("FIRST_LAUNCHER")
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }

    /**
     * 获取程序包所属渠道信息
     */
    fun getChannel(context: Context): String {
        var channel: String? = null
        val sourceDir = context.applicationInfo.sourceDir
        val start_flag = "META-INF/channel_"
        var zipfile: ZipFile? = null
        try {
            zipfile = ZipFile(sourceDir)
            val entries = zipfile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                val entryName = entry.name
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "")
                    break
                }
            }
        } catch (e: IOException) {
            LogUtil.error(e)
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        if (TextUtils.isEmpty(channel)) {
            channel = getManifestChannel(context)
        }
        if (TextUtils.isEmpty(channel)) {
            //默认好近渠道
            channel = "haojin"
        }
        return channel ?: ""
    }

    private fun getManifestChannel(context: Context): String {
        try {
            val ai = context.packageManager
                    .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val bundle = ai.metaData
            return if (bundle != null) {
                bundle.getString("UMENG_CHANNEL", "")
            } else {
                ""
            }
        } catch (e: PackageManager.NameNotFoundException) {
            return ""
        }

    }

    /**
     * 获取版本名称
     *
     * @return 当前应用的版本号
     */
    fun getVersionName(context: Context): String {
        try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "1.0"
    }

    /**
     * 获取app版本号也可称为build号
     */
    fun getVersionCode(context: Context): Int {
        try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    /**
     * 获取应用名称
     *
     * @param context 上下文
     * @return
     */
    fun getApplicationName(context: Context): String {
        try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            return packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    fun isAppStarted(context: Context?): Boolean {
        var isRunning = false
        if (context == null) {
            LogUtil.error("context is null, please conform.")
            return isRunning
        }
        val pkgName = context.packageName
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT >= 23) {
            val appTasks = activityManager.appTasks
            for (appTask in appTasks) {
                val recentTaskInfo = appTask.taskInfo
                val topActivity = recentTaskInfo.topActivity
                if (topActivity != null && topActivity.packageName == pkgName) {
                    isRunning = true
                    break
                }
            }
        } else {
            val runningTaskInfos = activityManager.getRunningTasks(100)
            for (runningTaskInfo in runningTaskInfos) {
                val topActivity = runningTaskInfo.topActivity
                if (topActivity != null && topActivity.packageName == pkgName) {
                    isRunning = true
                    break
                }
            }
        }
        return isRunning
    }
}
