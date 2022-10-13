package com.jyc.fast.result.launcher.compiler.activity.entity

import javax.lang.model.element.VariableElement


/// @author jyc
/// 创建日期：2022/10/13
/// 描述：Parameter
open class ParameterBean(private val element: VariableElement) {

    open var name = element.simpleName.toString()

    val defaultValue = "null"

    val parameterType = element.asType()

    private fun getDefaultValueHashCode(): Int {
        return defaultValue.hashCode() ?: Int.MAX_VALUE // Need for distinguishing String("") & String(null)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + getDefaultValueHashCode()
//        result =
//            31 * result + if (fastAnnotatedParameterEnum != null) fastAnnotatedParameterEnum.hashCode() else 0
//        result = 31 * result + preCondition.hashCode()
//        result = 31 * result + if (className != null) className.hashCode() else 0
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterBean

        if (element != other.element) return false
        if (name != other.name) return false
        if (defaultValue != other.defaultValue) return false
        if (parameterType != other.parameterType) return false

        return true
    }
}