package com.jyc.fast.result.launcher.compiler.aptutils.utils

import com.jyc.fast.result.launcher.compiler.aptutils.AptContext
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import com.squareup.kotlinpoet.FileSpec
import java.io.IOException
import javax.tools.StandardLocation

fun FileSpec.writeToFile() {
    try {
        val fileObject = AptContext.filer.createResource(StandardLocation.SOURCE_OUTPUT, packageName, name + ".kt")
        val writer = fileObject.openWriter()
        writeTo(writer)
        writer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun JavaFile.writeToFile(){
    writeTo(AptContext.filer)
}

fun TypeSpec.writeToFile(packageName: String){
        try {
            val file = JavaFile.builder(packageName, this).build()
            file.writeTo(AptContext.filer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
}
