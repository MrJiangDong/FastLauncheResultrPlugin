package com.jyc.fast.result.launcher.compiler.activity.method

import com.jyc.annotations.compiler.utils.cameToUnderline
import com.jyc.fast.result.launcher.compiler.activity.LauncherActivityClass
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier


/// @author jyc
/// 创建日期：2022/10/9
/// 描述：ConstantBuilder
class ConstantBuilder(private val launcherActivityClass: LauncherActivityClass) {

    //public static final String REQUIRED_NAME = "name"
    //initializer("\$S", field.name)  赋值 \$ 相当于 字符串占位符 %
    fun build(typeBuilder: TypeSpec.Builder) {
        launcherActivityClass.fields.forEach { field ->
            typeBuilder.addField(
                FieldSpec.builder(
                    String::class.java, field.prefix + field.name.cameToUnderline().uppercase(),
                    Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL
                ).initializer("\$S", field.name).build()
            )
        }
    }

}