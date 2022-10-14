package com.jyc.fast.launcher.result.compiler.activity.method

import com.squareup.javapoet.TypeSpec


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：IMethodBuildInterface
interface IMethodBuildInterface {

    fun build(typeBuilder:TypeSpec.Builder)
}