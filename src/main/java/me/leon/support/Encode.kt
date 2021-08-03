package me.leon.support

import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*

fun String.b64Decode() = String(Base64.getDecoder().decode(this))

fun String.b64SafeDecode() =
    if (this.contains(":")) this
    else
        try {
            String(Base64.getDecoder().decode(this.trim().replace("_", "/").replace("-", "+")))
        } catch (e: Exception) {
            println("failed: $this ${e.message}")
            ""
        }

fun String.b64Encode() = Base64.getEncoder().encodeToString(this.toByteArray())

fun String.b64EncodeNoEqual() =
    Base64.getEncoder().encodeToString(this.toByteArray()).replace("=", "")

fun String.urlEncode() = URLEncoder.encode(this) ?: ""

fun String.urlDecode() = URLDecoder.decode(this) ?: ""
