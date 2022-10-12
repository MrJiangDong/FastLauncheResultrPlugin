package com.jyc.fast.result.launcher.compiler.activity

import com.bennyhuo.aptutils.types.packageName
import com.bennyhuo.aptutils.types.simpleName
import com.jyc.fast.result.launcher.compiler.activity.entity.ResultCode
import com.jyc.fast.result.launcher.compiler.activity.entity.Field
import com.jyc.fast.result.launcher.compiler.activity.entity.MethodCall
import com.squareup.javapoet.TypeName
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import kotlin.collections.HashMap
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

    val isAbstract = typeElement.modifiers.contains(Modifier.ABSTRACT)

    val isKotlin = typeElement.getAnnotation(Metadata::class.java) != null

    val targetTypeName: TypeName = TypeName.get(typeElement.asType())

    val methodCalls = TreeMap<String,MethodCall>()

    val resultCodes = HashMap<String, TreeSet<ResultCode>>()

    fun addMethodCall(methodCall: MethodCall){
        methodCalls[methodCall.methodKey] = methodCall

        methodCall.methodResultCode.forEach {
            val resultCode = ResultCode(it,methodCall.methodName)

            if (resultCodes[methodCall.methodKey] != null) {
                resultCodes[methodCall.methodKey]!!.add(resultCode)
            } else {
                val treeSet = TreeSet<ResultCode>()
                treeSet.add(resultCode)
                resultCodes[methodCall.methodKey] = treeSet
            }
        }
    }

    override fun toString(): String {
        return "$packageName.$simpleName[${fields.joinToString()}][${methodCalls.size}]"
    }

    val builder = LauncherActivityClassBuilder(this)
}