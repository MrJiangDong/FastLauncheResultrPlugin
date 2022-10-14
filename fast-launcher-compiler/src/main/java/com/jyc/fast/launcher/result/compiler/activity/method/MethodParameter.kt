package com.jyc.fast.launcher.result.compiler.activity.method

import com.jyc.fast.launcher.result.compiler.aptutils.types.isSubTypeOf
import com.jyc.fast.launcher.result.compiler.activity.LauncherActivityClass
import com.jyc.fast.launcher.result.compiler.activity.LauncherActivityClassBuilder.Companion.TARGET_VARIABLE_NAME
import com.jyc.fast.launcher.result.compiler.activity.entity.MethodCall
import com.jyc.fast.launcher.result.compiler.activity.entity.ParameterAnnotation
import com.jyc.fast.launcher.result.compiler.prebuilt.INTENT
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.NameAllocator
import javax.lang.model.type.TypeKind
import kotlin.collections.ArrayList


/// @author jyc
/// 创建日期：2022/10/13
/// 描述：MethodParameter
class MethodParameter(val launcherActivityClass: LauncherActivityClass) {

    fun addResultMethod(
        methodBuilder: MethodSpec.Builder,
        methodCall: MethodCall
    ) {
        val nameAllocator = NameAllocator()
        val parameterAnnotationList = addNecessaryParameterVariables(
            methodBuilder,
            nameAllocator,
            methodCall
        )

        methodBuilder.addStatement(
            "\$L.\$L(\$L)",
            TARGET_VARIABLE_NAME,
            methodCall.methodName,
            parameterToString(nameAllocator,parameterAnnotationList)
        )
    }

    private fun parameterToString(
        nameAllocator: NameAllocator,
        parameters: ArrayList<ParameterAnnotation>
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
        methodCall: MethodCall
    ): ArrayList<ParameterAnnotation> {
        val parameterAnnotationList = ArrayList<ParameterAnnotation>()
        //复制一份数据
        parameterAnnotationList.addAll(methodCall.parameterAnnotationList)
        val parametersList = methodCall.parameterList
        //将默认的参数添加到注解参数中
        parametersList.forEach {
            val parameterAnnotation = ParameterAnnotation(it)
            if (it.asType().isSubTypeOf(INTENT.className())) {
                parameterAnnotation.name = "data"
            } else if (it.asType().kind == TypeKind.INT) {
                parameterAnnotation.name = "resultCode"
            }
            parameterAnnotationList.add(parameterAnnotation)
        }

        parameterAnnotationList.forEach { parameter ->
            var parameterName: String

            parameterName = try {
                nameAllocator[parameter.hashCode()] // The only way to know whether a name has already been generated for that hashCode or not
            } catch (ignore: IllegalArgumentException) {
                nameAllocator.newName(parameter.name, parameter.hashCode())
            }

        }

        return parameterAnnotationList
    }
}