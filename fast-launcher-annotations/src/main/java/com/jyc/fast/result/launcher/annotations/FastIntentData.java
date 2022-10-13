package com.jyc.fast.result.launcher.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// @author jyc
/// 创建日期：2022/10/13
/// 描述：FastIntent
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.CLASS)
public @interface FastIntentData {
}
