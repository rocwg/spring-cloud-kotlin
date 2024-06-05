package com.livk.auto.service.ksp

import com.google.devtools.ksp.isLocal
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName

/**
 * <p>
 * AbstractProcessor
 * </p>
 *
 * @author livk
 * @date 2024/5/22
 */
abstract class AbstractProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    protected val generator = environment.codeGenerator

    protected val logger = environment.logger

    protected lateinit var resolver: Resolver


    final override fun process(resolver: Resolver): List<KSAnnotated> {
        this.resolver = resolver
        val autoServiceType =
            resolver.getClassDeclarationByName(resolver.getKSNameFromString(supportAnnotation()))?.asType(emptyList())
                ?: run {
                    val message = "@SpringAutoService type not found on the classpath, skipping processing."
                    logger.info(message)
                    return emptyList()
                }
        processAnnotations(
            autoServiceType,
            resolver.getSymbolsWithAnnotation(supportAnnotation()).filterIsInstance<KSClassDeclaration>()
        )
        generateAndClearConfigFiles()
        return emptyList()
    }

    protected abstract fun supportAnnotation(): String

    protected abstract fun processAnnotations(autoServiceType: KSType, symbolAnnotations: Sequence<KSClassDeclaration>)

    protected abstract fun generateAndClearConfigFiles()

    protected fun KSClassDeclaration.toBinaryName(): String {
        return toClassName().reflectionName()
    }

    protected fun getArgument(annotation: KSAnnotation, key: String): Any? =
        annotation.arguments.find { it.name?.getShortName() == key }!!.value

    private fun KSClassDeclaration.toClassName(): ClassName {
        require(!isLocal()) { "Local/anonymous classes are not supported!" }
        val pkgName = packageName.asString()
        val typesString = qualifiedName!!.asString().removePrefix("$pkgName.")

        val simpleNames = typesString.split(".")
        return ClassName(pkgName, simpleNames)
    }
}