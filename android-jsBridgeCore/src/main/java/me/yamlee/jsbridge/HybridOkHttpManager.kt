package me.yamlee.jsbridge

import android.content.Context

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.util.ArrayList

import me.yamlee.jsbridge.entity.HybridUpdateEntity
import me.yamlee.jsbridge.jscall.HttpRequestProcessor
import me.yamlee.jsbridge.network.RequestHeader
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import timber.log.Timber

/**
 * 原生Http client封装
 *
 * @author yamlee
 */
object HybridOkHttpManager {
    private val okHttpClient = OkHttpClient.Builder().build()

    @Throws(IOException::class)
    fun doRequest(context: Context, entity: HybridUpdateEntity): String {

        var builder = Request.Builder()
        builder = addHeader(builder)
        if (isPost(entity)) {

            Timber.i("---->HYBRID  HTTP POST " + entity.path)
            builder.url(entity.path).post(generatePostParam(entity))
        } else {
            builder.url(generateGetUrl(entity))
        }
        val request = builder.build()
        val response = okHttpClient.newCall(request).execute()
        if (response.isSuccessful) {
            val responseStr = response.body()!!.string()

            Timber.i("h5接口请求返回数据:$responseStr")
            return responseStr
        } else {
            throw IOException(context.getString(R.string.get_data_err))
        }
    }

    @Throws(IOException::class)
    fun doRequest(context: Context,
                  request: HttpRequestProcessor.HybridHttpRequest): String {
        var builder = Request.Builder()
        builder = addHeader(builder)
        if (HttpRequestProcessor.HybridHttpRequest.Method.POST == request.method) {

            Timber.i("---->HYBRID  HTTP POST " + request.url!!)
            builder.url(request.url!!)
                    .post(generatePostParam(request.jsonParams))
        } else {
            builder.url(generateGetUrl(request.url, request.jsonParams))
        }
        val okHttpRequest = builder.build()
        val response = okHttpClient.newCall(okHttpRequest).execute()
        if (response.isSuccessful) {
            val responseStr = response.body()!!.string()

            Timber.i("h5接口请求返回数据:$responseStr")
            return responseStr
        } else {
            throw IOException(context.getString(R.string.get_data_err))
        }
    }


    private fun addHeader(builder: Request.Builder): Request.Builder {
        val requestHeaders = ArrayList<RequestHeader>()
        if (requestHeaders != null) {
            for (i in requestHeaders.indices) {
                val requestHeader = requestHeaders[i]
                builder.addHeader(requestHeader.key(), requestHeader.value())
            }
        }
        return builder
    }

    private fun generatePostParam(entity: HybridUpdateEntity): RequestBody {
        return generatePostParam(entity.jsonParams!!.toString())
    }

    private fun generatePostParam(jsonParams: String?): RequestBody {
        val build = FormBody.Builder()
        var paramJsonObj: JSONObject? = null
        try {
            paramJsonObj = JSONObject(jsonParams)
            val keys = paramJsonObj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = paramJsonObj.optString(key)
                build.add(key, value)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return build.build()
    }

    private fun generateGetUrl(entity: HybridUpdateEntity): String {
        return generateGetUrl(entity.path, entity.jsonParams!!.toString())
    }

    private fun generateGetUrl(url: String?, jsonParams: String?): String {
        val pathBuilder = StringBuilder(url)
        pathBuilder.append("?")
        var paramJsonObj: JSONObject? = null
        try {
            paramJsonObj = JSONObject(jsonParams)
            val keys = paramJsonObj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                pathBuilder.append(key).append("=").append(paramJsonObj.optString(key)).append("&")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        var result = pathBuilder.toString()
        if (result.endsWith("&")) {
            result = result.substring(0, result.length - 1)
        }

        Timber.i("---->HYBRID  HTTP GET $result")
        return result
    }

    /**
     * 判断请求方式
     */
    private fun isPost(entity: HybridUpdateEntity): Boolean {
        //请求方式
        val action = entity.action
        if (HybridUpdateValue.VALUE_ACTION_GET.equals(action, ignoreCase = true)) {
            return false
        } else if (HybridUpdateValue.VALUE_ACTION_POST.equals(action, ignoreCase = true)) {
            return true
        }
        //默认是get
        return false
    }
}
