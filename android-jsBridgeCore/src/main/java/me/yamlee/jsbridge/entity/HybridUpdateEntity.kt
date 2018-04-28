package me.yamlee.jsbridge.entity

import org.json.JSONObject

/**
 * Info for H5 files update
 * @author yamlee
 */
class HybridUpdateEntity {
    var scheme = ""
    var path = ""
    var action = ""
    var jsonParams: JSONObject? = null
    var content = ""
    var title = ""
    var url = ""
}
