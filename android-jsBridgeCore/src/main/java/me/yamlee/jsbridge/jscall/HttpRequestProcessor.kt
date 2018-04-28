package me.yamlee.jsbridge.jscall

import android.content.Context

import java.io.IOException

import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.HybridOkHttpManager
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.NativeComponentProvider
import me.yamlee.jsbridge.WVJBWebViewClient
import rx.Observable
import rx.Subscriber
import timber.log.Timber

/**
 * Network request
 *
 * @author yamlee
 * @version 1.0
 */
class HttpRequestProcessor(componentProvider: NativeComponentProvider) : BaseJsCallProcessor(componentProvider) {
    private var context: Context? = null

    override fun onHandleJsQuest(callData: JsCallData): Boolean {
        if (FUNC_NAME == callData.func) {
            val params = callData.params
            val request = convertJsonToObject(params!!, HybridHttpRequest::class.java)
            context = componentProvider.provideApplicationContext()
            doRequest(context!!, request)
            return true
        }
        return false
    }

    private fun doRequest(context: Context,
                          request: HttpRequestProcessor.HybridHttpRequest): Observable<String> {
        return Observable.create { subscriber ->
            var data: String? = null
            try {
                data = HybridOkHttpManager.doRequest(context, request)
            } catch (e: IOException) {
                Timber.e(e)
            }

            subscriber.onNext(data)
            subscriber.onCompleted()
        }
    }


    override fun getFuncName(): String {
        return FUNC_NAME
    }

    class HybridHttpRequest {

        var method: String? = null
        var url: String? = null
        var jsonParams: String? = null

        interface Method {
            companion object {
                val GET = "GET"
                val POST = "POST"
            }
        }
    }

    companion object {

        const val FUNC_NAME = "httpRequest"
    }
}
