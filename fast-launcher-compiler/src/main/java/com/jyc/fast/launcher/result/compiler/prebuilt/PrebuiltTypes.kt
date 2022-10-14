package com.jyc.fast.launcher.result.compiler.prebuilt

import com.jyc.fast.launcher.result.compiler.aptutils.types.ClassType


/// @author jyc
/// 创建日期：2022/10/9
/// 描述：PrebuiltTypes
val CONTEXT = ClassType("android.content.Context")
val INTENT = ClassType("android.content.Intent")
val ACTIVITY = ClassType("android.app.Activity")
val BUNDLE = ClassType("android.os.Bundle")
val BUNDLE_UTILS = ClassType("com.jyc.annotations.runtime.utils.BundleUtils")

val ACTIVITY_RESULT_LAUNCHER = ClassType("androidx.activity.result.ActivityResultLauncher")
val ACTIVITY_RESULT_CONTRACTS = ClassType("androidx.activity.result.contract.ActivityResultContracts")

val ACTIVITY_RESULT = ClassType("androidx.activity.result.ActivityResult")
val ACTIVITY_RESULT_CALLBACK = ClassType("androidx.activity.result.ActivityResultCallback")
