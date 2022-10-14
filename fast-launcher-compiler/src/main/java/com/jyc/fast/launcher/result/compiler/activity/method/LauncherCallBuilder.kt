package com.jyc.fast.launcher.result.compiler.activity.method

import com.jyc.fast.launcher.result.compiler.aptutils.utils.firstCapital
import com.jyc.fast.launcher.result.compiler.activity.LauncherActivityClass
import com.jyc.fast.launcher.result.compiler.activity.LauncherActivityClassBuilder.Companion.METHOD_NAME
import com.jyc.fast.launcher.result.compiler.activity.LauncherActivityClassBuilder.Companion.TARGET_VARIABLE_NAME
import com.jyc.fast.launcher.result.compiler.activity.LauncherActivityClassBuilder.Companion.TYPE_VARIABLE_NAME
import com.jyc.fast.launcher.result.compiler.prebuilt.*
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：LauncherCallBuilder
class LauncherCallBuilder(private val launcherActivityClass: LauncherActivityClass) :
    IMethodBuildInterface {

    override fun build(typeBuilder: TypeSpec.Builder) {
        launcherActivityClass.fields.forEach { field ->

            val initLauncherMethod =
                MethodSpec.methodBuilder(METHOD_NAME + field.name.firstCapital())
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(
                        TypeVariableName.get(TYPE_VARIABLE_NAME),
                        TARGET_VARIABLE_NAME
                    )
                    .returns(TypeName.VOID)


            //匿名内部类
            val parameter: ParameterSpec =
                ParameterSpec.builder(ACTIVITY_RESULT.java, "result")
                    .addModifiers(Modifier.FINAL)
                    .build()

            val anonymousClassMethod = MethodSpec.methodBuilder("onActivityResult")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(parameter)
                .addStatement("\$T data = result.getData()", INTENT.java)
                .addStatement("int resultCode = result.getResultCode()")

            //生成控制语句
            launcherActivityClass.resultCodes[field.launcherKey]?.forEachIndexed { index, fastResultCodes ->
                if (index == 0) {
                    anonymousClassMethod.beginControlFlow(
                        "if (resultCode == \$L)",
                        fastResultCodes.resultCode
                    )
                    MethodParameter(launcherActivityClass).addResultMethod(anonymousClassMethod,fastResultCodes.methodCall)
//                    anonymousClassMethod.addStatement(
//                        "\$L.\$L()",
//                        TARGET_VARIABLE_NAME,
//                        fastResultCodes.methodName
//                    )
                } else {
                    anonymousClassMethod.nextControlFlow(
                        "else if (resultCode == \$L)",
                        fastResultCodes.resultCode
                    )
                    MethodParameter(launcherActivityClass).addResultMethod(anonymousClassMethod,fastResultCodes.methodCall)
//                    anonymousClassMethod.addStatement(
//                        "\$L.\$L()",
//                        TARGET_VARIABLE_NAME,
//                        fastResultCodes.methodName
//                    )
                }
                if ((index + 1) == launcherActivityClass.resultCodes[field.launcherKey]?.size) {
                    anonymousClassMethod.endControlFlow()
                }
            }

            val activityResult =
                TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(
                        ParameterizedTypeName.get(
                            ACTIVITY_RESULT_CALLBACK.className(),
                            ACTIVITY_RESULT.className()
                        )
                    )
                    .addMethod(anonymousClassMethod.build()).build()

            initLauncherMethod.addStatement(
                "\$T<\$T> \$L = \$L.registerForActivityResult(new \$T.StartActivityForResult(),\$L)",
                ACTIVITY_RESULT_LAUNCHER.java,
                INTENT.java,
                METHOD_NAME + field.name.firstCapital(),
                TARGET_VARIABLE_NAME,
                ACTIVITY_RESULT_CONTRACTS.java,
                activityResult
            )

            initLauncherMethod.addStatement(
                "\$L.\$L = \$L",
                TARGET_VARIABLE_NAME,
                field.name,
                METHOD_NAME + field.name.firstCapital()
            )

            typeBuilder.addMethod(initLauncherMethod.build())
        }
    }
}