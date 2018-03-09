package me.yamlee.demo.jscall

import me.yamlee.demo.WebActivity
import me.yamlee.demo.bridge.CustomComponentProvider
import me.yamlee.jsbridge.jscall.AbstractSetHeaderRightProcessor

/**
 * Created by yamlee on 08/03/2018.
 */
class SetHeaderRightProcessor(val provider: CustomComponentProvider) : AbstractSetHeaderRightProcessor(provider) {

    override fun onClickRightBtn(clickJumpUrl: String) {
        val intent = WebActivity.getIntent(clickJumpUrl, provider.provideActivityContext())
        provider.provideWebInteraction().startActivity(intent)
    }
}