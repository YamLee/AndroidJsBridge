package me.yamlee.jsbridge.network

/**
 * Network request header info
 *
 * @author yamlee
 */
interface RequestHeader {
    fun key(): String

    fun value(): String
}
