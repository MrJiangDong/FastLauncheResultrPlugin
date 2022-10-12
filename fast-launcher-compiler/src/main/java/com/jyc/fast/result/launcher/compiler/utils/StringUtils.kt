package com.jyc.annotations.compiler.utils

import java.util.*


/// @author jyc
/// 创建日期：2022/10/9
/// 描述：StringUtils
/**
 * 扩展函数
 * 找到一个大写字母 就添加一个_
 */
fun String.cameToUnderline(): String {
    return fold(StringBuilder()) { acc, c ->
        if (c.isUpperCase()) {
            acc.append("_").append(c.uppercase())
        } else acc.append(c)
    }.toString()
}

fun String.underlineToCamel(): String {
    var upperNext = false
    return fold(StringBuilder()) { acc, c ->
        if (c == '_') upperNext = true
        else acc.append(if (upperNext) c.uppercase() else c)
        acc
    }.toString()
}

fun String.myCapitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.firstCapital(): String {
    val chars = this.toCharArray()
    chars[0] = chars[0] - 32
    return String(chars)
}