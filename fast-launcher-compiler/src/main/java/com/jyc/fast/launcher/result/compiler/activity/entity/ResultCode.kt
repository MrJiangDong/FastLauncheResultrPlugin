package com.jyc.fast.launcher.result.compiler.activity.entity

class ResultCode(val resultCode: Int, val methodCall: MethodCall) : Comparable<ResultCode> {


    override fun compareTo(other: ResultCode): Int {
        return this.resultCode.compareTo(other.resultCode)
    }
}