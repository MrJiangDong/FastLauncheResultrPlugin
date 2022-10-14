package com.jyc.fast.launcher.result.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// @author jyc
/// 创建日期：2022/10/11
/// 描述：Launcher
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface FastLauncher {
    String launcherKey();
}
