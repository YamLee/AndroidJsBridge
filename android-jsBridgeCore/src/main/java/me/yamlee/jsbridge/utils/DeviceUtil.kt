package me.yamlee.jsbridge.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import android.text.TextUtils

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.reflect.Method
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID

/**
 * 设备信息获取工具类
 *
 * @author yamlee
 */
object DeviceUtil {
    private var udid: String? = null

    private var sID: String? = ""
    private val UNIQUEID = UUID.randomUUID().toString()


    /**
     * 判断是否是魅族系统
     *
     * @return
     */
    // Invoke Build.hasSmartBar()
    val isFlyme: Boolean
        get() {
            try {
                val method = Build::class.java.getMethod("hasSmartBar")
                return method != null
            } catch (e: Exception) {
                return false
            }

        }

    /**
     * 获取手机设备名称
     *
     * @return
     */
    val deviceName: String
        get() = Build.BRAND + " " + Build.MODEL

    /**
     * 获取系统版本
     *
     * @return
     */
    val osVersionStr: String
        get() = Build.VERSION.RELEASE

    /**
     * @return
     */
    val osVersion: Int
        get() = Build.VERSION.SDK_INT

    /**
     * 获取设备唯一标识号
     *
     * @param context
     * @return
     */
    fun getDeviceID(context: Context): String {
        if (!TextUtils.isEmpty(udid)) {
            return udid!!
        }
        udid = getPhoneDeviceId(context)
        if (TextUtils.isEmpty(udid)) {
            udid = "0"
        }
        return udid!!
    }

    private fun getPhoneDeviceId(context: Context): String {
        var deviceId = "0"
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            deviceId = tm.deviceId
        }
        return deviceId
    }

    /**
     * 获取唯一标识号
     *
     * @param context
     * @return
     */
    fun getUniqueId(context: Context?): String {
        if (context == null) {
            return ""
        }
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var deviceId = ""

        try {
            deviceId = getPhoneDeviceId(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (TextUtils.isEmpty(deviceId)) {
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    deviceId = telephonyManager.simSerialNumber
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        if (TextUtils.isEmpty(deviceId)) {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val macAddress = wifiManager.connectionInfo.macAddress
            deviceId = md5(macAddress)
        }
        if (TextUtils.isEmpty(deviceId)) {
            try {
                deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = md5(handleUuid(context))
        }
        return deviceId
    }

    @Synchronized
    private fun handleUuid(context: Context): String {
        if (sID == null) {
            val uniqueid = File(context.filesDir, UNIQUEID)
            try {
                if (!uniqueid.exists())
                    writeUniqueIdFile(uniqueid)
                sID = readUniqueIdFile(uniqueid)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }
        return sID!!
    }

    @Throws(IOException::class)
    private fun readUniqueIdFile(installation: File): String {
        val f = RandomAccessFile(installation, "r")
        val bytes = ByteArray(f.length().toInt())
        f.readFully(bytes)
        f.close()
        return String(bytes)
    }

    @Throws(IOException::class)
    private fun writeUniqueIdFile(installation: File) {
        val out = FileOutputStream(installation)
        val id = UUID.randomUUID().toString()
        out.write(id.toByteArray())
        out.close()
    }

    private fun md5(plainText: String): String {
        if (TextUtils.isEmpty(plainText)) {
            return ""
        }
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(plainText.toByteArray())
            val b = md.digest()
            var i: Int
            val build = StringBuilder("")
            for (aB in b) {
                i = aB.toInt()
                if (i < 0) i += 256
                if (i < 16)
                    build.append("0")
                build.append(Integer.toHexString(i))
            }
            //            System.out.println("result: " + buf.toString());//32位的加密
            //            System.out.println("result: " + buf.toString().substring(8,24));//16位的加密
            return build.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }

    fun getNetworkAccessMode(context: Context): String {
        try {
            val pm = context.packageManager
            if (pm.checkPermission("android.permission.ACCESS_NETWORK_STATE",
                            context.packageName) != PackageManager.PERMISSION_GRANTED) {
                return "Unknown"
            }

            val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    ?: return "Unknown"

            val info = connectivityManager.activeNetworkInfo
            var typeName: String? = info.typeName.toLowerCase() // WIFI/MOBILE
            if (typeName == "wifi") {
            } else {
                typeName = info.extraInfo.toLowerCase()
                // 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
            }
            return if (typeName != null) {
                typeName
            } else "Unknown"

        } catch (e: Exception) {
            return "Unknown"
        }

    }


}
