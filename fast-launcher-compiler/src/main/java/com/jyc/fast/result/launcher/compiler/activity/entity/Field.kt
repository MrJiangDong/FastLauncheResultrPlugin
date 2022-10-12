package com.jyc.fast.result.launcher.compiler.activity.entity

import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：Field
open class Field(private val element:VariableElement) {

    open val prefix = "LAUNCHER_"

    val name = element.simpleName.toString()

    //是不是私有数据
    val isPrivate = Modifier.PRIVATE in element.modifiers
    //
}