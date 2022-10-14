package com.jyc.fast.result.launcher.compiler

import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.isSubTypeOf
import com.bennyhuo.aptutils.types.simpleName
import com.jyc.fast.result.launcher.annotations.FastIntentData
import com.jyc.fast.result.launcher.annotations.FastLauncher
import com.jyc.fast.result.launcher.annotations.FastLauncherResult
import com.jyc.fast.result.launcher.compiler.activity.LauncherActivityClass
import com.jyc.fast.result.launcher.compiler.activity.entity.Field
import com.jyc.fast.result.launcher.compiler.activity.entity.MethodCall
import com.jyc.fast.result.launcher.compiler.activity.entity.ParameterAnnotation
import com.jyc.fast.result.launcher.compiler.prebuilt.INTENT
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.TypeKind


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：LauncherProcess
class LauncherProcessor : AbstractProcessor() {

    private val supportedAnnotations =
        setOf(FastLauncher::class.java, FastLauncherResult::class.java, FastIntentData::class.java)


    override fun getSupportedSourceVersion() = SourceVersion.RELEASE_8

    override fun getSupportedAnnotationTypes() =
        supportedAnnotations.mapTo(HashSet<String>(), Class<*>::getCanonicalName)

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        AptContext.init(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val launcherActivityClasses = HashMap<Element, LauncherActivityClass>()
        roundEnv.getElementsAnnotatedWith(FastLauncher::class.java)
            .filter { it.kind == ElementKind.FIELD }
            .forEach { element ->
                try {
                    if (!element.asType()
                            .isSubTypeOf("androidx.activity.result.ActivityResultLauncher")
                    ) {
                        Logger.error(
                            element,
                            "Unsupported typeElement:${element.simpleName}"
                        )

                        return@forEach
                    }

                    if (!element.enclosingElement.asType().isSubTypeOf("android.app.Activity")) {
                        Logger.error(
                            element,
                            "Unsupported typeElement:${element.enclosingElement.simpleName}"
                        )

                        return@forEach
                    }

                    if (launcherActivityClasses[element.enclosingElement] == null) {
                        launcherActivityClasses[element.enclosingElement] =
                            LauncherActivityClass(element.enclosingElement as TypeElement)
                    }

                    launcherActivityClasses[element.enclosingElement]!!.fields.add(Field(element as VariableElement))
                } catch (e: Exception) {
                    Logger.logParsingError(element, FastLauncher::class.java, e)
                }
            }

        roundEnv.getElementsAnnotatedWith(FastLauncherResult::class.java)
            .filter { it.kind == ElementKind.METHOD }
            .forEach { element ->
                try {

                    if (launcherActivityClasses[element.enclosingElement]?.methodCalls == null) {
                        Logger.error(
                            element,
                            "Method $element annotated as Required while ${element.enclosingElement} not annotated"
                        )
                    }

                    val methodCall = MethodCall(
                        element as ExecutableElement
                    )

                    if ( methodCall.parameterList.size >2){
                        Logger.error(
                            element,
                            "Method $element only supported intent and resultCode"
                        )
                    }

                    methodCall.parameterList.forEach {
                        if (!(it.asType().isSubTypeOf(INTENT.className())) && (it.asType().kind != TypeKind.INT)){
                            Logger.error(element,"${methodCall.methodName} has unsupported parameters")
                        }
                    }

                    launcherActivityClasses[element.enclosingElement]!!.addMethodCall(
                        methodCall
                    )
                } catch (e: Exception) {
                    Logger.logParsingError(element, FastLauncherResult::class.java, e)
                }
            }

        roundEnv.getElementsAnnotatedWith(FastIntentData::class.java)
            .filter { it.kind == ElementKind.PARAMETER }
            .forEach { element ->
                try {
                    val methodCall: MethodCall? =
                        launcherActivityClasses[element.enclosingElement.enclosingElement]?.methodCalls?.get(
                            element.enclosingElement.simpleName()
                        )

                    if (methodCall == null) {
                        Logger.error(
                            element,
                            "Method $element annotated as Required while ${element.enclosingElement} not annotated"
                        )
                    }

                    val parameterAnnotation = ParameterAnnotation(element as VariableElement)
                    methodCall!!.parameterAnnotationList.add(parameterAnnotation)

                } catch (e: Exception) {
                    Logger.logParsingError(element, FastIntentData::class.java, e)
                }
            }

        launcherActivityClasses.values.forEach {
            it.builder.build(AptContext.filer)
        }
        return true
    }
}