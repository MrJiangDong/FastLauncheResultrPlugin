package com.jyc.fast.result.launcher.compiler

import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.isSubTypeOf
import com.jyc.fast.result.launcher.annotations.FastLauncher
import com.jyc.fast.result.launcher.annotations.FastLauncherResult
import com.jyc.fast.result.launcher.compiler.activity.LauncherActivityClass
import com.jyc.fast.result.launcher.compiler.activity.entity.Field
import com.jyc.fast.result.launcher.compiler.activity.entity.MethodCall
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.*


/// @author jyc
/// 创建日期：2022/10/11
/// 描述：LauncherProcess
class LauncherProcessor : AbstractProcessor() {

    private val supportedAnnotations =
        setOf(FastLauncher::class.java, FastLauncherResult::class.java)


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
                    launcherActivityClasses[element.enclosingElement]?.methodCalls?.add(
                        MethodCall(
                            element as ExecutableElement
                        )
                    )
                        ?: Logger.error(
                            element,
                            "Method $element annotated as Required while ${element.enclosingElement} not annotated"
                        )
                } catch (e: Exception) {
                    Logger.logParsingError(element, FastLauncherResult::class.java, e)
                }
            }

        launcherActivityClasses.values.forEach {
            it.builder.build(AptContext.filer)
        }
        return true
    }
}