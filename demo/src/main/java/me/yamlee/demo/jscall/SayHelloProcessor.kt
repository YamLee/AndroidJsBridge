package me.yamlee.demo.jscall

import android.widget.Toast
import me.yamlee.jsbridge.BaseJsCallProcessor
import me.yamlee.jsbridge.JsCallData
import me.yamlee.jsbridge.JsCallback
import me.yamlee.jsbridge.NativeComponentProvider

class SayHelloProcessor(provider: NativeComponentProvider) : BaseJsCallProcessor(provider) {

    private val context = provider.provideApplicationContext()

    override fun getFuncName(): String {
        return "sayHello"
    }

    override fun onHandleJsRequest(callData: JsCallData, callback: JsCallback): Boolean {
        Toast.makeText(context, "Hello World!", Toast.LENGTH_LONG).show()
        return true
    }

}