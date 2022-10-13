package com.jyc.fast.result.launcher.compiler.activity.entity

import com.jyc.fast.result.launcher.annotations.FastLauncherResult
import java.util.*
import javax.lang.model.element.ExecutableElement
import kotlin.collections.HashSet


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：MethodCall
class MethodCall(private val element: ExecutableElement) {

    val methodName = element.simpleName.toString()

    //获取该方法体标记的注解
    val methodKey = element.getAnnotation(FastLauncherResult::class.java).launcherKey

    val methodResultCode = element.getAnnotation(FastLauncherResult::class.java).launcherResultCodes

    val parameterBeanList = HashSet<ParameterBean>()

    val parameterList = element.parameters
//    val methodResultCode = element.parameters[1]
}