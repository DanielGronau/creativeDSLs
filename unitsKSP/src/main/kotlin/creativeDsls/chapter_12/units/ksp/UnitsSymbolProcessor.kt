package creativeDsls.chapter_12.units.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import creativeDSLs.chapter_12.units.Conversion
import creativeDSLs.chapter_12.units.MultiplicationResult
import creativeDSLs.chapter_12.units.QuantityOperations
import kotlin.reflect.KClass

class UnitsSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val conversionDeclarations = invokeVisitor(
            resolver,
            Conversion::class,
            ConversionVisitor(codeGenerator, logger)
        )

        val operationsDeclarations = invokeVisitor(
            resolver,
            QuantityOperations::class,
            OperationsVisitor(codeGenerator, logger)
        )

        val multiplicationDeclarations = invokeVisitor(
            resolver,
            MultiplicationResult::class,
            MultiplicationVisitor(codeGenerator, logger)
        )

        return listOf(conversionDeclarations, operationsDeclarations, multiplicationDeclarations)
            .flatten()
            .distinct()
            .filterNot { it.validate() }
            .toList()
    }

    private fun invokeVisitor(
        resolver: Resolver,
        annotation: KClass<*>,
        visitor: KSVisitorVoid
    ): List<KSClassDeclaration> =
        resolver.getSymbolsWithAnnotation(annotation.qualifiedName!!)
            .distinct()
            .filterIsInstance<KSClassDeclaration>()
            .toList()
            .onEach { it.accept(visitor, Unit) }
}