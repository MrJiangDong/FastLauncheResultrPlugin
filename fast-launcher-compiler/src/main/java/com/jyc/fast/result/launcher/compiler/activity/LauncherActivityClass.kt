package com.jyc.fast.result.launcher.compiler.activity

import com.bennyhuo.aptutils.types.TypeUtils
import com.bennyhuo.aptutils.types.packageName
import com.bennyhuo.aptutils.types.simpleName
import com.jyc.fast.result.launcher.compiler.activity.entity.Field
import com.jyc.fast.result.launcher.compiler.activity.entity.MethodCall
import com.squareup.javapoet.TypeName
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import kotlin.collections.HashSet


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：LauncherActivityClass
/**
 * 收集信息的类
 */
class LauncherActivityClass(val typeElement: TypeElement) {

    val simpleName = typeElement.simpleName()

    val packageName = typeElement.packageName()

    val fields = HashSet<Field>()

    val methodCalls = HashSet<MethodCall>()

    val isAbstract = typeElement.modifiers.contains(Modifier.ABSTRACT)

    val isKotlin = typeElement.getAnnotation(Metadata::class.java) != null

    val targetTypeName: TypeName = TypeName.get(typeElement.asType())

    override fun toString(): String {
        return "$packageName.$simpleName[${fields.joinToString()}][${methodCalls.joinToString()}]"
    }

    val builder = LauncherActivityClassBuilder(this)
}