package com.jyc.fast.result.launcher.compiler.activity.method

import com.jyc.annotations.compiler.utils.firstCapital
import com.jyc.fast.result.launcher.compiler.activity.LauncherActivityClass
import com.jyc.fast.result.launcher.compiler.activity.LauncherActivityClassBuilder.Companion.METHOD_NAME
import com.jyc.fast.result.launcher.compiler.activity.LauncherActivityClassBuilder.Companion.TARGET_VARIABLE_NAME
import com.jyc.fast.result.launcher.compiler.activity.LauncherActivityClassBuilder.Companion.TYPE_VARIABLE_NAME
import com.jyc.fast.result.launcher.compiler.prebuilt.*
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：LauncherCallBuilder
class LauncherCallBuilder(private val launcherActivityClass: LauncherActivityClass) :
    IMethodBuildInterface {

    override fun build(typeBuilder: TypeSpec.Builder) {
        launcherActivityClass.methodCalls.forEach { methodCall ->

            val initLauncherMethod =
                MethodSpec.methodBuilder(METHOD_NAME + methodCall.methodKey.firstCapital())
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

            val activityResult =
                TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(
                        ParameterizedTypeName.get(
                            ACTIVITY_RESULT_CALLBACK.className(),
                            ACTIVITY_RESULT.className()
                        )
                    )
                    .addMethod(
                        MethodSpec.methodBuilder("onActivityResult")
                            .addAnnotation(Override::class.java)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addParameter(parameter)
                            .build()
                    ).build()

            initLauncherMethod.addStatement(
                "\$T<\$T> \$L = \$L.registerForActivityResult(new \$T.StartActivityForResult(),\$L)",
                ACTIVITY_RESULT_LAUNCHER.java,
                INTENT.java,
                METHOD_NAME + methodCall.methodKey.firstCapital(),
                TARGET_VARIABLE_NAME,
                ACTIVITY_RESULT_CONTRACTS.java,
                activityResult
            )

            initLauncherMethod.addStatement(
                "\$L.\$L = \$L",
                TARGET_VARIABLE_NAME,
                methodCall.methodKey,
                METHOD_NAME + methodCall.methodKey.firstCapital()
            )

            typeBuilder.addMethod(initLauncherMethod.build())

        }

    }
}