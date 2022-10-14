package com.jyc.fast.launcher.result.compiler.activity

import com.jyc.fast.launcher.result.compiler.activity.method.ConstantBuilder
import com.jyc.fast.launcher.result.compiler.activity.method.LauncherCallBuilder
import com.squareup.javapoet.*
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：LauncherActivityClassBuilder
class LauncherActivityClassBuilder(private val launcherActivityClass: LauncherActivityClass) {

    companion object {
        const val POSIX = "\$\$FastLauncherResult"
        const val METHOD_NAME = "init"
        const val TARGET_VARIABLE_NAME = "host"
        const val TYPE_VARIABLE_NAME = "HOST"
    }

    fun build(filer: Filer) {
        if (launcherActivityClass.isAbstract) return
        //创建一个class文件
        val typeBuilder = TypeSpec.classBuilder(launcherActivityClass.simpleName + POSIX)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addTypeVariable(TypeVariableName.get(TYPE_VARIABLE_NAME,launcherActivityClass.targetTypeName))

        //创建方法
        ConstantBuilder(launcherActivityClass).build(typeBuilder)
        LauncherCallBuilder(launcherActivityClass).build(typeBuilder)

        //创建一个文件
        writeJavaToFile(filer, typeBuilder.build())
    }

    private fun writeJavaToFile(filer: Filer, typeSpec: TypeSpec) {
        try {
            val file = JavaFile.builder(launcherActivityClass.packageName, typeSpec).build()
            file.writeTo(filer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}