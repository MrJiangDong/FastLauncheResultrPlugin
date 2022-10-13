package com.jyc.fast.result.launcher.compiler.activity.method

import com.bennyhuo.aptutils.types.isSubTypeOf
import com.jyc.fast.result.launcher.compiler.activity.LauncherActivityClass
import com.jyc.fast.result.launcher.compiler.activity.LauncherActivityClassBuilder.Companion.TARGET_VARIABLE_NAME
import com.jyc.fast.result.launcher.compiler.activity.entity.MethodCall
import com.jyc.fast.result.launcher.compiler.activity.entity.ParameterBean
import com.jyc.fast.result.launcher.compiler.prebuilt.INTENT
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.NameAllocator
import java.util.*
import javax.lang.model.element.VariableElement
import kotlin.collections.HashSet


/// @author jyc
/// 创建日期：2022/10/13
/// 描述：MethodParameter
class MethodParameter(val launcherActivityClass: LauncherActivityClass) {

    fun addResultMethod(
        methodBuilder: MethodSpec.Builder,
        methodCall: MethodCall
    ) {
        val nameAllocator = NameAllocator()
        addNecessaryParameterVariables(
            methodBuilder,
            nameAllocator,
            methodCall.parameterBeanList,
            methodCall.parameterList
        )
        methodBuilder.addStatement(
            "\$L.\$L(\$L)",
            TARGET_VARIABLE_NAME,
            methodCall.methodName,
            parameterToString(nameAllocator, methodCall.parameterBeanList)
        )
    }

    private fun parameterToString(
        nameAllocator: NameAllocator,
        parameters: HashSet<ParameterBean>
    ): String {
        val stringBuilder = StringBuilder()
        if (parameters.size > 0) {
            for (parameter in parameters) {
                stringBuilder.append(nameAllocator[parameter.hashCode()]).append(", ")
            }
            stringBuilder.setLength(stringBuilder.length - 2)
        }
        return stringBuilder.toString()
    }

    private fun addNecessaryParameterVariables(
        methodBuilder: MethodSpec.Builder,
        nameAllocator: NameAllocator,
        parameters: HashSet<ParameterBean>,
        parametersList: List<VariableElement>
    ) {

        //将默认的参数添加到注解参数中
        parametersList.forEach {
            if (it.asType().isSubTypeOf(INTENT.className())){
                val parameterBean = ParameterBean(it)
                parameterBean.name = "data"
                parameters.add(parameterBean)
            }
        }

        parameters.forEach { parameter ->
            var parameterName: String

            parameterName = try {
                nameAllocator[parameter.hashCode()] // The only way to know whether a name has already been generated for that hashCode or not
            } catch (ignore: IllegalArgumentException) {
                nameAllocator.newName(parameter.name, parameter.hashCode())
            }

        }

    }
}