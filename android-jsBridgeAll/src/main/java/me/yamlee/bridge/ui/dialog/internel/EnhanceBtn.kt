package me.yamlee.bridge.ui.dialog.internel

import android.support.annotation.IntDef

object EnhanceBtn {
    const val ENHANCE_CONFIRM = 0//default
    const val ENHANCE_CANCEL = 1
    const val ENHANCE_NONE = 2

    @kotlin.annotation.Retention
    @IntDef(ENHANCE_CONFIRM, ENHANCE_CANCEL, ENHANCE_NONE)
    annotation class EnhanceBtnDef
}