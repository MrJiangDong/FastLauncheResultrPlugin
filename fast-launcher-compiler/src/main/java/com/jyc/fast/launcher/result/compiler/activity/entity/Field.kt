package com.jyc.fast.launcher.result.compiler.activity.entity

import com.jyc.fast.launcher.result.annotations.FastLauncher
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：Field
open class Field(private val element: VariableElement) {

    open val prefix = "LAUNCHER_"

    val name = element.simpleName.toString()

    val launcherKey = element.getAnnotation(FastLauncher::class.java).launcherKey

    //是不是私有数据
    val isPrivate = Modifier.PRIVATE in element.modifiers
    //
}